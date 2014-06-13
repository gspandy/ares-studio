/**
 * Դ�������ƣ�ExtensibleModelTreeContentProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.extend;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 * input Ӧ����IARESResource
 * @author gongyf
 *
 */
public class ExtensibleModelTreeContentProvider implements ITreeContentProvider {

	/**
	 * @param model
	 * @param modelId
	 */
	public ExtensibleModelTreeContentProvider() {
		super();
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
		Assert.isTrue(inputElement instanceof ExtensibleModelEditingRoot);
		
		ExtensibleModelEditingRoot root = (ExtensibleModelEditingRoot) inputElement;
		return root.getCategories().toArray();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ExtensibleModelEditingCategory) {
			return ((ExtensibleModelEditingCategory) parentElement).getEntries().toArray();
		}
		return ArrayUtils.EMPTY_OBJECT_ARRAY;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		if (element instanceof ExtensibleModelEditingEntry) {
			return ((ExtensibleModelEditingEntry)element).getCategory();
		} else if (element instanceof ExtensibleModelEditingCategory) {
			return ((ExtensibleModelEditingCategory) element).getRoot();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		return element instanceof ExtensibleModelEditingCategory;
	}


}
