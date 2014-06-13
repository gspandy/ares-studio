/**
 * Դ�������ƣ�BaseEditingSupport.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.editingsupport;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;

/**
 * @author gongyf
 *
 */
public abstract class BaseEditingSupport extends EditingSupport {

	public CellEditor cellEditor;
	private IEditingSupportDecorator decorator;
	
	/**
	 * @param viewer
	 */
	public BaseEditingSupport(ColumnViewer viewer) {
		super(viewer);
	}

	/**
	 * @param decorator the decorator to set
	 */
	public void setDecorator(IEditingSupportDecorator decorator) {
		this.decorator = decorator;
	}

	@Override
	final public CellEditor getCellEditor(Object element) {
		if (decorator == null) {
			return doGetCellEditor(element);
		} else {
			return decorator.decorateGetCellEditor(doGetCellEditor(element), element);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
	 */
	@Override
	final protected boolean canEdit(Object element) {
		if (decorator == null) {
			return doCanEdit(element);
		} else {
			return decorator.decorateCanEdit(doCanEdit(element), element);
		}
	}
	
	
	protected CellEditor doGetCellEditor(Object element) {
		if (cellEditor == null) {
			cellEditor = createCellEditor();
		}
		return cellEditor;
	}
	
	protected boolean doCanEdit(Object element) {
		// �����EObject�����ж����ڵ�Resource�Ƿ�ֻ��
		if (element instanceof EObject) {
			Resource resource = ((EObject) element).eResource();
			if (resource != null) {
				EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(element);
				if (editingDomain != null) {
					return !editingDomain.isReadOnly(resource);
				}
				
			}
		}
		
		return true;
	}
	/**
	 * ������ʹ�õ�CellEditor
	 * @return
	 */
	protected abstract CellEditor createCellEditor();

}
