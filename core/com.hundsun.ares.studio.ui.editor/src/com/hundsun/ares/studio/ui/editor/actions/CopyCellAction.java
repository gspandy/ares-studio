package com.hundsun.ares.studio.ui.editor.actions;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;
import com.hundsun.ares.studio.ui.util.ARESEMFClipboard;

/**
 * ���Ƶ�Ԫ���ı���Action.<br>
 * ���͵�ʹ�÷�����<br>
 * <code>
 *  copyCellAction =  new CopyCellAction(columviewer);<br>
 *  menumanager.add(copyCellAction);
 * </code>
 * @author sundl
 *
 */
public class CopyCellAction extends Action {
	
	public static final String ID = "copy.cell";
	
	private ColumnViewer viewer;
	private String textToBeCopyed;
	
	/**
	 * @param viewer
	 */
	public CopyCellAction(ColumnViewer viewer) {
		this.viewer = viewer;
		setId(ID);
		setText(StringUtils.EMPTY);
		setImageDescriptor(ARESEditorPlugin.imageDescriptorFromPlugin(ARESEditorPlugin.PLUGIN_ID, "icons/full/obj16/copyCell.png"));
		viewer.getControl().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				update(e);
			}
		});
	}
	
	@Override
	public void run() {
		ARESEMFClipboard.getInstance().copyToClipboard(null, textToBeCopyed, null);
	}

	public void update(MouseEvent event) {
		Point pt = new Point(event.x, event.y);
		ViewerCell cell = viewer.getCell(pt);
		if (cell == null) {
			textToBeCopyed = null;
		} else {
			textToBeCopyed = cell.getText();
		}
		updateEnablement();
		updateText();
	}
	
	/**
	 * ���¿���״̬
	 */
	public void updateEnablement() {
		if (StringUtils.isEmpty(textToBeCopyed)) {
			setEnabled(false);
		} else {
			setEnabled(true);
		}
	}
	
	/**
	 * ������ʾ�ı�
	 */
	public void updateText() {
		if (StringUtils.isEmpty(textToBeCopyed)) {
			setText("�����ı�:");
			return;
		}
		
		String text = textToBeCopyed.length() > 5 ? StringUtils.abbreviate(textToBeCopyed, 10) : textToBeCopyed;
		setText("�����ı�:    \"" + text + "\"");
	}
}
