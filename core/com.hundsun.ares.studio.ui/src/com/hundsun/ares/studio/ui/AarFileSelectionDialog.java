/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

/**
 * ѡ��Aar�ļ��ĶԻ���
 * @author sundl
 */
public class AarFileSelectionDialog extends ElementTreeSelectionDialog {

	/**
	 * @param parent
	 * @param labelProvider
	 * @param contentProvider
	 */
	public AarFileSelectionDialog(Shell parent, ILabelProvider labelProvider,
			ITreeContentProvider contentProvider) {
		super(parent, labelProvider, contentProvider);
		
	}

}
