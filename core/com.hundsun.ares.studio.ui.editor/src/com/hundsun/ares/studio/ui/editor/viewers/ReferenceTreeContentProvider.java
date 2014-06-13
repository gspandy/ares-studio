/**
 * Դ�������ƣ�ReferenceTreeContentProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.viewers;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author gongyf
 *
 */
public class ReferenceTreeContentProvider implements ITreeContentProvider {

	static Object[] NONE = new Object[0];
	EReference reference;
	
	
	
	/**
	 * @param reference
	 */
	public ReferenceTreeContentProvider(EReference reference) {
		super();
		this.reference = reference;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof EObject) {
			if (!((EObject) inputElement).eClass().getEAllContainments().contains(reference)) {
				return NONE;
			}
			Object children = ((EObject) inputElement).eGet(reference);
			if (children instanceof Collection) {
				return ((Collection) children).toArray();
			}
		}
		return NONE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof EObject) {
			if (!((EObject) parentElement).eClass().getEAllContainments().contains(reference)) {
				return NONE;
			}
			Object children = ((EObject) parentElement).eGet(reference);
			if (children instanceof Collection) {
				return ((Collection) children).toArray();
			}
		}
		return NONE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		if (element instanceof EObject) {
			return ((EObject) element).eContainer();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof EObject) {
			if (!((EObject) element).eClass().getEAllContainments().contains(reference)) {
				return false;
			}
			Object children = ((EObject) element).eGet(reference);
			if (children instanceof Collection) {
				return !((Collection) children).isEmpty();
			}
		}
		return false;
	}

}
