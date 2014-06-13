/**
 * Դ�������ƣ�PropertyValueEditingSupport.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.extend;

import java.util.HashMap;
import java.util.Map;

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
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.util.Pair;
import com.hundsun.ares.studio.core.model.util.PutCommand;
import com.hundsun.ares.studio.ui.editor.extend.user.IUserExtendedPropertyDescriptor;

/**
 * @author gongyf
 *
 */
public class PropertyValueEditingSupport extends EditingSupport {

	private Map<Object, CellEditor> cellEditorMap = new HashMap<Object, CellEditor>();
	
	/**
	 * @param viewer
	 */
	public PropertyValueEditingSupport(ColumnViewer viewer) {
		super(viewer);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		if (element instanceof ExtensibleModelEditingEntry) {
			ExtensibleModelEditingEntry entry = (ExtensibleModelEditingEntry) element;
			// ��Ҫ�ظ�����
			
			IExtensibleModelPropertyDescriptor descriptor = entry.getDescriptor();
			String key = entry.getGroup().getEditingSupport().getKey();
			
			// ����key��֪���᲻��������
			Object cellEditorKey = new Pair<String, IExtensibleModelPropertyDescriptor>(key, descriptor);
			
			CellEditor cellEditor = cellEditorMap.get(cellEditorKey);
			if (cellEditor == null) {
				cellEditor = entry.getDescriptor().createPropertyEditor((Composite) getViewer().getControl());
				if (cellEditor != null) {
					cellEditorMap.put(cellEditorKey, cellEditor);
				}
			}
			
			if (cellEditor instanceof IExtensibleModelCellEditor) {
				((IExtensibleModelCellEditor) cellEditor).setModel(entry.getGroup().getRoot().getModel());
			}
			
			return cellEditor;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
	 */
	@Override
	protected boolean canEdit(Object element) {
		if (element instanceof ExtensibleModelEditingEntry) {
			return getViewer().getControl().isEnabled();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
	 */
	@Override
	protected Object getValue(Object element) {
		Object value = null;
		
		ExtensibleModelEditingEntry entry = (ExtensibleModelEditingEntry) element;
		IExtensibleModelPropertyDescriptor descriptor = entry.getDescriptor();
		if (descriptor instanceof IUserExtendedPropertyDescriptor) {
			value = ((IUserExtendedPropertyDescriptor) descriptor).getValue(entry.getGroup().getRoot().getModel());
		} else {
			EStructuralFeature feature = descriptor.getStructuralFeature();
			String key = entry.getGroup().getEditingSupport().getKey();
			EObject owner = entry.getGroup().getRoot().getModel().getData2().get(key);
			if (owner != null) {
				value = owner.eGet(feature);
				// ֧��map���͵���չ
				if (descriptor instanceof IMapExtensibleModelPropertyDescriptor) {
					Assert.isTrue(value instanceof EMap<?, ?>);
					value = ((EMap<?, ?>) value).get(((IMapExtensibleModelPropertyDescriptor) descriptor).getKey());
				}
			}
		}

		//feature.getDefaultValue()
		return ObjectUtils.defaultIfNull(value, StringUtils.EMPTY);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		
		// ���û���޸ĵı�Ҫ��ֱ�ӷ���
		Object oldValue = getValue(element);
		if (ObjectUtils.equals(oldValue, value)) {
			return;
		}
		
		ExtensibleModelEditingEntry entry = (ExtensibleModelEditingEntry) element;
		IExtensibleModelPropertyDescriptor descriptor = entry.getDescriptor();
		if (descriptor instanceof IUserExtendedPropertyDescriptor) {
			((IUserExtendedPropertyDescriptor) descriptor).setValue(entry.getGroup().getRoot().getModel(), String.valueOf(value));
		} else {
			EStructuralFeature feature = descriptor.getStructuralFeature();
			String key = entry.getGroup().getEditingSupport().getKey();
			
			CompoundCommand command = new CompoundCommand();
			EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(entry.getGroup().getRoot().getModel());
			
			EObject owner = entry.getGroup().getRoot().getModel().getData2().get(key);
			if (owner == null) {
				// û�л����Ķ���,��Ҫ����
				owner = entry.getGroup().getEditingSupport().createMapValueObject();
				if (editingDomain == null) {
					entry.getGroup().getRoot().getModel().getData2().put(key, owner);
				} else {
					command.append(new PutCommand(entry.getGroup().getRoot().getModel(), CorePackage.Literals.EXTENSIBLE_MODEL__DATA2, key, owner));
				}
			}
			
//			command.append(SetCommand.create(editingDomain, owner, feature, value));
			
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
		}
		
		// ���ʹ��RefreshViewerJob���ӳ���ʾ��ȷ����
		getViewer().update(element, null);
	}

}
