/**
 * Դ�������ƣ�TextEditingSupport.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.basicdata.ui.editor.blocks;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.jres.basicdata.core.basicdata.validate.util.IKeyTableLocator;
import com.hundsun.ares.studio.ui.editor.editingsupport.EMFEditingSupport;
import com.hundsun.ares.studio.ui.editor.viewers.IEStructuralFeatureProvider;

public class BasicDataTextEditingSupportEX extends EMFEditingSupport {


	IKeyTableLocator locator;
	EStructuralFeature feature;

	/**
	 * @param viewer
	 * @param featureProvider
	 */
	public BasicDataTextEditingSupportEX(ColumnViewer viewer,
			IEStructuralFeatureProvider featureProvider,
			IKeyTableLocator locator,
			EStructuralFeature feature) {
		super(viewer, featureProvider);
		this.locator = locator;
		this.feature = feature;
	}

	@Override
	protected CellEditor doGetCellEditor(Object element) {
		if(null == featureProvider.getFeature(element)){
			return null;
		}
		return super.doGetCellEditor(element);
	}
	
	@Override
	protected CellEditor createCellEditor() {
		return new TextCellEditor((Composite) getViewer().getControl());
	}
	
	@Override
	protected Object getValue(Object element) {
		// ��ֹnull���õ�Text��ȥ
		return ObjectUtils.defaultIfNull(super.getValue(element), "");
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
		EObject owner = getOwner(element);
		
		if (EcoreUtil.getRootContainer(owner.eContainer()) instanceof ChangeDescription) {
			// FIXME ���ڼ���celleditor��ʱ����г������������ܵ�����������Ϊ�仯��һ���֣������������Ȼ�޷��õ��༭�򣬵���Ҳ��Ӧ�ý��б༭
			return;
		}
		
		EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(owner);
		
		if (editingDomain != null) {
			
			CompoundCommand command = new CompoundCommand();
			
			
			Command setCommand = SetCommand.create(editingDomain, owner, featureProvider.getFeature(element), value);
			command.append(setCommand);
			
			try {
				EObject copyElement =  EcoreUtil.copy(((EObject)element)) ;
				copyElement.eSet(featureProvider.getFeature(element), value);
				EObject refer =  locator.getLinkObject(copyElement);
				
				Command setReferCommand = SetCommand.create(editingDomain, owner,feature, refer);
				command.append(setReferCommand);
			} catch (Exception e) {
			}
			
			editingDomain.getCommandStack().execute(command);
		} else {
			owner.eSet(featureProvider.getFeature(element), value);
		}
		
		// ���ʹ��RefreshViewerJob���ӳ���ʾ��ȷ����
		getViewer().update(element, null);
		
	}
}
