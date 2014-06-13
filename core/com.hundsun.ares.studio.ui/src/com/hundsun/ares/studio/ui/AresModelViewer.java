/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * ��ʾAresģ������TreeViewer���Զ�������ContentProvider,LabelProvider,Sorter
 * @author sundl
 */
public class AresModelViewer extends TreeViewer {

	/**
	 * ������ʾAresģ������
	 * @param parent
	 * @param style
	 */
	public AresModelViewer(Composite parent, int style) {
		super(parent, style);
		init();
	}
	
	private void init() {
		CommonElementContentProvider cp = new CommonElementContentProvider();
		setContentProvider(cp);
		setLabelProvider(new CommonElementLabelProvider(cp));
		setComparator(new ARESElementSorter());
	}
	
}
