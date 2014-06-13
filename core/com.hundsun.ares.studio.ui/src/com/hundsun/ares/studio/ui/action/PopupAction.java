/**
 * Դ�������ƣ�PopupAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * @author gongyf
 *
 */
public abstract class PopupAction implements IObjectActionDelegate {

	private ISelection selection;
	private IWorkbenchPart workbenchPart;
	
	/**
	 * @return the selection
	 */
	protected ISelection getSelection() {
		return selection;
	}
	
	/**
	 * @return the workbenchPart
	 */
	protected IWorkbenchPart getWorkbenchPart() {
		return workbenchPart;
	}
	
	/**
	 * һ����ui�߳��е���
	 * @return
	 */
	protected Shell getShell() {
		if (getWorkbenchPart() != null) {
			return getWorkbenchPart().getSite().getShell();
		} else {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.workbenchPart = targetPart;
	}

}
