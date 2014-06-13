/**
 * Դ�������ƣ�ExtensibleModelEditingSupport.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.extend;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.core.model.util.PutCommand;
import com.hundsun.ares.studio.ui.editor.editingsupport.EMFEditingSupport;

/**
 * @author gongyf
 *
 */
public class ExtensibleModelEditingSupport extends EMFEditingSupport {

	IExtensibleModelEditingSupport editingSupport;
	IExtensibleModelPropertyDescriptor descriptor;
	
	/**
	 * @param viewer
	 * @param editingSupport
	 * @param descriptor
	 */
	public ExtensibleModelEditingSupport(ColumnViewer viewer,
			IExtensibleModelEditingSupport editingSupport,
			IExtensibleModelPropertyDescriptor descriptor) {
		super(viewer, descriptor.getStructuralFeature());
		this.editingSupport = editingSupport;
		this.descriptor = descriptor;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editingsupports.EMFEditingSupport#getOwner(java.lang.Object)
	 */
	@Override
	protected EObject getOwner(Object element) {
		ExtensibleModel model = (ExtensibleModel) element;
		return model.getData2().get(editingSupport.getKey());
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editingsupports.EMFEditingSupport#getValue(java.lang.Object)
	 */
	@Override
	protected Object getValue(Object element) {
		Object value = ObjectUtils.defaultIfNull(super.getValue(element), descriptor.getStructuralFeature().getDefaultValue());
		// ֧��map���͵���չ
		if (descriptor instanceof IMapExtensibleModelPropertyDescriptor) {
			Assert.isTrue(value instanceof EMap<?, ?>);
			value = ((EMap<?, ?>) value).get(((IMapExtensibleModelPropertyDescriptor) descriptor).getKey());
		}
		return ObjectUtils.defaultIfNull(value, StringUtils.EMPTY);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editingsupports.BaseEditingSupport#createCellEditor()
	 */
	@Override
	protected CellEditor createCellEditor() {
		return descriptor.createPropertyEditor((Composite) getViewer().getControl());
	}
	
	@Override
	protected CellEditor doGetCellEditor(Object element) {
		CellEditor cellEditor = super.doGetCellEditor(element);
		if (cellEditor instanceof IExtensibleModelCellEditor) {
			((IExtensibleModelCellEditor) cellEditor).setModel((ExtensibleModel) element);
		}
		return cellEditor;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editingsupports.EMFEditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		// ���û���޸ĵı�Ҫ��ֱ�ӷ���
		Object oldValue = getValue(element);
		if (ObjectUtils.equals(oldValue, value)) {
			return;
		}
		
		ExtensibleModel model = (ExtensibleModel) element;
		EStructuralFeature feature = descriptor.getStructuralFeature();
		String key = editingSupport.getKey();
		
		CompoundCommand command = new CompoundCommand();
		EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(model);
		
		EObject owner = model.getData2().get(key);
		if (owner == null) {
			// û�л����Ķ���,��Ҫ����
			owner = editingSupport.createMapValueObject();
			if (editingDomain == null) {
				model.getData2().put(key, owner);
			} else {
				command.append(new PutCommand(model, CorePackage.Literals.EXTENSIBLE_MODEL__DATA2, key, owner));
			}
		}
		
		if (editingDomain != null) {
			if (descriptor instanceof IMapExtensibleModelPropertyDescriptor) {
				
				Assert.isTrue(feature instanceof EReference);
				command.append(new PutCommand(owner, (EReference)feature, ((IMapExtensibleModelPropertyDescriptor) descriptor).getKey(), value));
			} else {
				command.append(SetCommand.create(editingDomain, owner, feature, value));
			}
			
			editingDomain.getCommandStack().execute(command.unwrap());
		} else {
			if (descriptor instanceof IMapExtensibleModelPropertyDescriptor) {
				Object map = owner.eGet(feature);
				Assert.isTrue(map instanceof EMap<?, ?>);
				((EMap<Object, Object>)map).put(((IMapExtensibleModelPropertyDescriptor) descriptor).getKey(), value);
			} else {
				owner.eSet(feature, value);
			}
			
			
		}
		
		// ���ʹ��RefreshViewerJob���ӳ���ʾ��ȷ����
		getViewer().update(element, null);
	}
}
