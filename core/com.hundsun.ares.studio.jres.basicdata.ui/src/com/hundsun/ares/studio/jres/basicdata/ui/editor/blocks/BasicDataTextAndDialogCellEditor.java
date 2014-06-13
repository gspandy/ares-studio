/**
 * 
 */
package com.hundsun.ares.studio.jres.basicdata.ui.editor.blocks;

import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

import com.hundsun.ares.studio.ui.cellEditor.ContentProposalAdapter;
import com.hundsun.ares.studio.ui.cellEditor.IContentProposalListener2;
import com.hundsun.ares.studio.ui.cellEditor.SimpleContentProposalAdapter;
import com.hundsun.ares.studio.ui.celleditor.TextAndDialogCellEditor;

/**
 * @author yanwj06282
 *
 */
public abstract class BasicDataTextAndDialogCellEditor extends TextAndDialogCellEditor {
	
	private boolean popupOpen;
	
	private SimpleContentProposalAdapter adapter;
	
	public BasicDataTextAndDialogCellEditor(Composite parent) {
		super(parent);
	}
	
	@Override
	protected Control createControl(Composite parent) {
		Control control = super.createControl(parent);
		
		adapter = new SimpleContentProposalAdapter(text, new TextContentAdapter(), getProposalProvider(), null, null);
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
		
		text.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				// FIXME ����Ӧ�ô���ѡ���ж�ȡ��ǰ���õ��ı��༭����������ʾ��ݼ���������new SimpleContentProposalAdapterʱ��ָ���ÿ�ݼ�
				if(e.character=='/' && e.stateMask==SWT.ALT){
					adapter.openProposalPopup();
				}
			}		
		});
		adapter.setPopupSize(new Point(200, 100));
		
		return control;
	}
	
	public abstract IContentProposalProvider getProposalProvider();
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.TextCellEditor#doSetValue(java.lang.Object)
	 */
	@Override
	protected void doSetValue(Object value) {
		// ��ֹ�ռ���༭��Ԫ�͵���������ʾ��
		adapter.setEnabled(false);
		super.doSetValue(value);
		adapter.setEnabled(true);
	}
	
	@Override
	protected void focusLost() {
		if (!isPopupOpen()) {
			// Focus lost deactivates the cell editor.
			// This must not happen if focus lost was caused by activating
	        // the completion proposal popup.
			super.focusLost();
		}
	}
	
	/**
	 * ���������ʾ���˫�����ܻ�������⡣�����������дfocusLost()��dependsOnExternalFocusListener()������
	 */
	protected boolean dependsOnExternalFocusListener() {
		return false;
	}
	
	public void setContentProposalProvider(IContentProposalProvider provider) {
		this.adapter.setContentProposalProvider(provider);
	}
	
	public boolean isPopupOpen() {
		return popupOpen;
	}
}