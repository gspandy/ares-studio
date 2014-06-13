/**
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util.wizard;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

/**
 * ����ӿ��com.hundsun.ares.studio.ui.editorֱ���ù���(��������com.hundsun.ares.studio.ui.editor)
 * @author liaogc
 *
 */
public class CheckboxTreeViewerEx extends CheckboxTreeViewer {

	private CheckStateListener listener = new CheckStateListener();
	
	public CheckboxTreeViewerEx(Composite parent, int style) {
		super(parent, style);
		addCheckStateListener(listener);
	}

	public CheckboxTreeViewerEx(Composite parent) {
		super(parent);
		addCheckStateListener(listener);
	}

	public CheckboxTreeViewerEx(Tree tree) {
		super(tree);
		
		addCheckStateListener(listener);
	}
	
	public void checkChange(CheckStateChangedEvent event){
		listener.checkStateChanged(event);
	}

	/**
	 * ���ø���Ԫ�ص�ѡ��״̬�����Զ����������¼��ڵ��ѡ��״̬��
	 * @param elements
	 */
	public void setCheckedElementsWithNotify(Object[] elements) {
		super.setCheckedElements(elements);
		for(Object o : elements) {
			listener.checkStateChanged(new CheckStateChangedEvent(this, o, true));
		}	
	}
	
	class CheckStateListener implements ICheckStateListener {

		private final int CHECK_WHITE = 1;
		private final int CHECK_GRAY = 2;
		private final int CHECK_NOT = 0;
		
		public void checkStateChanged(CheckStateChangedEvent event) {
			Object thisObj = event.getElement();
			Object parent = CheckboxTreeViewerEx.this.getParentElement(thisObj);
			CheckboxTreeViewerEx.this.setGrayed(thisObj, false);
				if (event.getChecked()) {
					if(parent != null){
						check(parent, CHECK_WHITE);
					}
					// ��ѡ���ˣ���ѡ�������ӽ��
					setSubtreeChecked(thisObj, true);
					
				} else {
					if(parent != null){
						check(parent, CHECK_NOT);
					}
					setSubtreeChecked(thisObj, false);
					
				}
			
			while (parent != null) {
				setCurrentCheckState(parent);
				parent = CheckboxTreeViewerEx.this.getParentElement(parent);
			}
		}
		
		/**
		 * �����ӽ��״̬����ѡ��״̬
		 * @param parent
		 */
		private void setCurrentCheckState(Object parent) {
			Object[] children = CheckboxTreeViewerEx.this.getSortedChildren(parent);
			
			int checkCount1 = 0; // ��ѡ����ӽ��
			int checkCount2 = 0; // ����ѡ����ӽ��
			for (Object child : children) {
				if (CheckboxTreeViewerEx.this.getChecked(child)) {
					if (  CheckboxTreeViewerEx.this.getGrayed(child)) {
						checkCount2++;
					} else {
						checkCount1++;
					}
				}
				
			}
			
			if (checkCount1 == children.length) {
				check(parent, CHECK_WHITE);
			} else if (checkCount1 > 0 || checkCount2 > 0) {
				check(parent, CHECK_GRAY);
			} else {
				check(parent, CHECK_NOT);
			}
			
		}
		
		private void check(Object element, int state) {
			switch (state) {
			case CHECK_WHITE:
				CheckboxTreeViewerEx.this.setGrayed(element, false);
				CheckboxTreeViewerEx.this.setChecked(element, true);
				break;
			case CHECK_GRAY:
				CheckboxTreeViewerEx.this.setGrayChecked(element, true);
				break;
			case CHECK_NOT:
				CheckboxTreeViewerEx.this.setGrayChecked(element, false);
				break;
			default:
				break;
			}
		}
		
	}
}
