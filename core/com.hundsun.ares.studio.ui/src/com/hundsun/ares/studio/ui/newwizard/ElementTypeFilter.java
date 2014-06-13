/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.newwizard;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.hundsun.ares.studio.core.IARESElement;

/**
 * @author lvgao
 *
 */
public class ElementTypeFilter  extends ViewerFilter{

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return filte(viewer, parentElement, element);
	}
	
	/**
	 * ����Ԫ��
	 * @param viewer
	 * @param parentElement
	 * @param element
	 * @return
	 */
	protected boolean filte(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof IARESElement) {
			IARESElement ares = (IARESElement)element;
			int type = ares.getElementType();
			int[] allowedTypes = getDisplayedElementTypes();
			if (allowedTypes != null) {
				for (int allowedType : allowedTypes) {
					if (allowedType == type) {
						return true;
					}
				}
				return false;
			} else 
				return true;
		}
		return true;
	}
	
	/**
	 * ����Ҫ��ʾ������
	 */
	protected int[] getDisplayedElementTypes() {
		return null;
	}

}
