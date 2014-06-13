/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.ui.editor.decorators;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

import com.hundsun.ares.studio.ui.editor.actions.IActionIDConstant;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlockDecorator;

/**
 * 
 * �س����ƶ�����һ�У��������ĩ�������ָ��������Action
 * @author gongyf
 *
 */
public class EnterForNewLineTableViewerBlockDecorator extends
		ColumnViewerBlockDecorator<TableViewer> {
	
	public final static String ID = EnterForNewLineTableViewerBlockDecorator.class.getName();
	
	private int editIndex = 0;
	
	/**
	 * @return the editIndex
	 */
	public int getEditIndex() {
		return editIndex;
	}
	
	/**
	 * @param editIndex the editIndex to set
	 */
	public void setEditIndex(int editIndex) {
		this.editIndex = editIndex;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerBlockDecorator#decorateViewer(com.hundsun.ares.studio.jres.ui.pages.ColumnViewerBlock, org.eclipse.jface.viewers.ColumnViewer)
	 */
	@Override
	public void decorateViewer(final ColumnViewerBlock<TableViewer> block,
			final TableViewer viewer) {
		
		viewer.getTable().addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == '\r' && viewer.getTable().getSelectionCount() == 1) {
					int index = viewer.getTable().getSelectionIndex();
					if (index >= 0) {
						if (index == viewer.getTable().getItemCount() - 1) {
							// ���һ��
							IAction action = block.getActionRegistry().getAction(IActionIDConstant.CV_ADD);
							if (action != null && action.isEnabled()) {
								action.run();
								
								// ����Ҫ������ӵ��г��ֺ���ܼ���༭
//								viewer.refresh();
//								viewer.editElement(viewer.getTable().getItem(viewer.getTable().getItemCount() - 1).getData(), getEditIndex());
							}
						} else {
							viewer.getTable().setSelection(index + 1);
							// ��Ҫviewr��controlͬ��
							viewer.setSelection(viewer.getSelection());
						}
					}
				}
			}
		});
	}
}
