/**
 * Դ�������ƣ�EMFMapEditingSupport.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.editingsupport;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ColumnViewer;

public abstract class EMFMapEditingSupport extends BaseEditingSupport {

	protected EReference reference;
	protected Object key;
	
	public EMFMapEditingSupport(ColumnViewer viewer, EReference reference,
			Object key) {
		super(viewer);
		this.reference = reference;
		this.key = key;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
	 */
	@Override
	protected boolean doCanEdit(Object element) {
		return getMap(element) != null && super.doCanEdit(element);
	}
	
	/**
	 * ��ȡ��Ҫ������EObject
	 * @param element
	 * @return
	 */
	protected EObject getOwner(Object element) {
		return (EObject) element;
	}
	
	protected EMap getMap(Object element) {
		EObject owner = getOwner(element);
		if (owner != null) {
			if (owner.eClass().getEAllReferences().contains(reference)) {
				Object obj = getOwner(element).eGet(reference);
				if (obj instanceof EMap) {
					return (EMap) obj;
				}
			}
		}
		return null;
	}
	
	@Override
	protected Object getValue(Object element) {
		EMap map = getMap(element);
		if (map != null) {
			return map.get(key);
		}
		return null;
	}

	@Override
	protected void setValue(final Object element, final Object value) {
		Object oldValue = getValue(element);
		if (ObjectUtils.equals(oldValue, value)) {
			return;
		}
		
		EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(element);
		if (editingDomain != null) {
			ChangeCommand command = new ChangeCommand(getOwner(element)) {
				
				@Override
				protected void doExecute() {
					getMap(element).put(key, value);
				}
			};
			
			editingDomain.getCommandStack().execute(command);
		} else {
			getMap(element).put(key, value);
		}
		
		// ���ʹ��RefreshViewerJob���ӳ���ʾ��ȷ����
		getViewer().update(element, null);
	}

}
