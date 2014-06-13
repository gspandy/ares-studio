/**
 * Դ�������ƣ�CellLinkColumnViewerEditorActivationStrategy.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.viewers.link;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;

/**
 * ���ڰ�����Ԫ�����ӵı༭��������ԣ����û�����Ctrl��ʱ�򣬲�Ҫ����CellEditor
 * @author gongyf
 *
 */
public class CellLinkColumnViewerEditorActivationStrategy extends
		JRESColumnViewerEditorActivationStrategy {

	/**
	 * @param viewer
	 */
	public CellLinkColumnViewerEditorActivationStrategy(ColumnViewer viewer) {
		super(viewer);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy#isEditorActivationEvent(org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent)
	 */
	@Override
	protected boolean isEditorActivationEvent(
			ColumnViewerEditorActivationEvent event) {
		if (event.sourceEvent instanceof MouseEvent) {
			// �����������¼������ж��Ƿ񱻰���Ctrl����Shift
			MouseEvent mEvent = (MouseEvent) event.sourceEvent;
			if ((mEvent.stateMask & SWT.CTRL) != 0 
					|| (mEvent.stateMask & SWT.SHIFT) != 0) {
				return false;
			}
		}
		return super.isEditorActivationEvent(event);
	}
}
