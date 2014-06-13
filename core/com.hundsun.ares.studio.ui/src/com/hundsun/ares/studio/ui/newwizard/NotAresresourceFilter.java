/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.newwizard;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.hundsun.ares.studio.core.IProjectProperty;
import com.hundsun.ares.studio.ui.RefLibContainer;

/**
 * @author lvgao
 *
 */
public class NotAresresourceFilter extends ViewerFilter{

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		//��������
		if (element instanceof RefLibContainer) {
			return false;
		}
		//������Ŀ����
		if (element instanceof IProjectProperty) {
			return false;
		}
		return true;
	}

}
