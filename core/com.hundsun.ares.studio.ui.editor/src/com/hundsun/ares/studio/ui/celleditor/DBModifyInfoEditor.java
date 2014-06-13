/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.celleditor;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


/**
 * �ű���Ϣ�Ի��򣬸����޸�����ѡ��ͬ�ĶԻ���
 * @author xuzhen
 * @version 1.0
 * @history
 */
public class DBModifyInfoEditor extends DialogCellEditor {
	
	Grid grid;
	Object info;
	
	public DBModifyInfoEditor(Composite parent, Object info) {
		super(parent);
		this.grid = (Grid) parent;
		this.info = info;
	}
	
//	/**
//	 * @param grid
//	 * @param info2
//	 */
//	public DBModifyInfoEditor(Grid grid, Object info) {
//		super(parent);
//		this.grid = grid;
//		this.info = info;
//	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		GridItem[] items = grid.getSelection();
		
		if ( null == items || items.length == 0) {
			try {
				throw new Exception("openDialogBox ....");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int returnCode = -1;
		
		LongTxtDialog dialog = new LongTxtDialog(cellEditorWindow.getShell());
		dialog.setText(getValue().toString());
		dialog.setBlockOnOpen(true);
		dialog.open();
		//TODO FIXME �����޸���������ѡ���޸�����Ҫ�Ĳ���
//		Dialog dialog = ModifyDialogFactory.getDialog(info);
//		int returnCode = dialog.open();
		
		//TODO FIXME
		// ����
		
		return (returnCode == Window.OK) ? dialog.getResult() : null;
	}
	

}
