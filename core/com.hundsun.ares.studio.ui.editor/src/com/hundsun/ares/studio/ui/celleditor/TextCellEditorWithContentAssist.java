/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.celleditor;

import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import com.hundsun.ares.studio.ui.assist.SimpleContentProposalAdapter;

/**
 * һ����������ʾ��TextCellEditor<BR>
 * ע����ǣ����캯������ͨCellEditor��������Ҫ����һ��TableViewer<BR>
 * 
 * ����ؼ��ƺ��и�BUG������ᵼ��ͬ�е�Ԫ��������ʾ��ֵ���ԣ�δȷ����Ŀǰ�޷�����<BR>
 * Ҳ�п��ܲ�����������⣬��CellEditorҪ���GetValue��null����
 * 
 * @author gongyf
 *
 */
public class TextCellEditorWithContentAssist extends TextCellEditor {

	public static final String SEPARATOR = "-->";
	
	private boolean popupOpen;
	
	protected SimpleContentProposalProvider proposalProvider;
	private SimpleContentProposalAdapter adapter;
	private ViewerCell lastViewCell = null;
	private ColumnViewer viewer = null;
	
	/**
	 * @return the useDialogPatch
	 */
	public boolean isUseDialogPatch() {
		return false;
	}
	
	//���Ӳ���Info��Ϊ��ʾ���Ӹ�����ʾ��Ϣ��
	public TextCellEditorWithContentAssist(Composite parent,ColumnViewer viewer, String[] proposals, String[] infos) {
		super(parent);
		
		this.viewer = viewer;
		String[] propsalsWithInfo = new String[proposals.length];
		for(int i = 0; i < proposals.length; i++) {
			if(infos != null && infos[i] != null) {
				propsalsWithInfo[i] = proposals[i] + SEPARATOR + infos[i];
			} else {
				propsalsWithInfo[i] = proposals[i];
			}
		}
		proposalProvider.setProposals(propsalsWithInfo);
	}
	
	public TextCellEditorWithContentAssist(Composite parent, String[] proposals, String[] infos) {
		super(parent);
		
		String[] propsalsWithInfo = new String[proposals.length];
		for(int i = 0; i < proposals.length; i++) {
			if(infos != null && infos[i] != null) {
				propsalsWithInfo[i] = proposals[i] + SEPARATOR + infos[i];
			} else {
				propsalsWithInfo[i] = proposals[i];
			}
		}
		proposalProvider.setProposals(propsalsWithInfo);
	}
	
	
	
	@Override
	protected Control createControl(Composite parent) {
		Control control = super.createControl(parent);
		
		proposalProvider = new SimpleContentProposalProvider(new String[0]);
		proposalProvider.setFiltering(true);
		adapter = new SimpleContentProposalAdapter(control, new MyControlContentAdapter(),
				proposalProvider, null, null);
		adapter.setPropagateKeys(true);
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		adapter.addContentProposalListener(new IContentProposalListener2(){
			public void proposalPopupClosed(ContentProposalAdapter adapter) {
				Event e = new Event();
				e.data = new Boolean(false);
				popupOpen = false;
			}
			public void proposalPopupOpened(ContentProposalAdapter adapter) {
				Event e = new Event();
				e.data = new Boolean(true);
				popupOpen = true;
			}});
		control.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if(e.character=='/' && e.stateMask==SWT.ALT){
					adapter.openProposalPopup();
				}
			}		
		});
		return control;
	}
	
	@Override
	protected void focusLost() {
		if (!isPopupOpen()) {
			super.focusLost();
		}
	}
	
	/**
	 * ���������ʾ���˫�����ܻ�������⡣�����������дfocusLost()��dependsOnExternalFocusListener()������
	 */
	protected boolean dependsOnExternalFocusListener() {
		return false;
	}

	@Override
	public void activate(ColumnViewerEditorActivationEvent activationEvent) {
		ViewerCell thisViewrCell = (ViewerCell) activationEvent.getSource();

		lastViewCell = thisViewrCell;
		super.activate(activationEvent);
	}
	
	public void setProposals(String[] proposals) {
		proposalProvider.setProposals(proposals);
		
	}
	
	private class MyControlContentAdapter implements IControlContentAdapter {

		public String getControlContents(Control control) {
			
			return ((Text) control).getText();
		}

		public int getCursorPosition(Control control) {
			return ((Text) control).getCaretPosition();
		}

		public Rectangle getInsertionBounds(Control control) {
			Text text = (Text) control;
			Point caretOrigin = text.getCaretLocation();
			return new Rectangle(caretOrigin.x, caretOrigin.y, 1, text
					.getLineHeight());
		}



		public void insertControlContents(Control control, String text,
				int cursorPosition) {
			Point selection = ((Text) control).getSelection();
			((Text) control).insert(text);
			if (cursorPosition < text.length()) {
				((Text) control).setSelection(selection.x + cursorPosition,
						selection.x + cursorPosition);
			}
			
		}

		public void setControlContents(Control control, String text,
				int cursorPosition) {
			if (isUseDialogPatch()) {
				Rectangle rect = control.getBounds();
				Point p = new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
				control.toDisplay(p);
				viewer.getControl().toControl(p);
				ViewerCell cell = viewer.getCell(p);
				
				
				viewer.editElement(cell.getElement(), cell.getColumnIndex());
			}

//			if (!isActivated() && viewer != null && lastViewCell != null) {
//				viewer.editElement(lastViewCell.getElement(), lastViewCell.getColumnIndex());
//			}
			((Text) control).setFocus();
			String s = text;
			int indexOfColon = text.indexOf(SEPARATOR);
			if(indexOfColon != -1) {
				s = text.substring(0, indexOfColon).trim();
			}
			((Text) control).setText(s);
			((Text) control).setSelection(cursorPosition, cursorPosition);

		}

		public void setCursorPosition(Control control, int position) {
			((Text) control).setSelection(new Point(position, position));
		}

		
	}

	/**
	 * @return the popupOpen
	 */
	public boolean isPopupOpen() {
		return popupOpen;
	}

}
