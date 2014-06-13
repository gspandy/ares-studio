/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.action;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.util.ARESElementUtil;
import com.hundsun.ares.studio.internal.core.DeleteResouceElementsOperation;
import com.hundsun.ares.studio.ui.refactoring.RefactoringUtil;

/**
 * ɾ����Action��
 * 
 * @author sundl
 */
public class ARESDeleteAction extends SelectionListenerAction {

	private static Logger logger = Logger.getLogger(ARESDeleteAction.class);
	
	private Shell shell;

	public ARESDeleteAction(Shell shell) {
		super("ɾ��");
	}

	@Override
	protected boolean updateSelection(IStructuredSelection selection) {
		Object[] resources = getSelectedResources().toArray();
		IARESElement[] areses = ARESElementUtil.toARESElement(resources);

		if (areses.length == resources.length && areses.length == selection.size() && areses.length > 0) {
			return RefactoringUtil.canDelete(areses);
		}
		return false;
	}

	@Override
	public void run() {
		// must select some resources
		Object[] resources = getSelectedResources().toArray();
		if (resources.length <= 0) {
			return;
		}

		final IARESElement[] elements = ARESElementUtil.toARESElement(resources);
		// ȷ��
		boolean confirm = MessageDialog.openConfirm(shell, "ȷ��ɾ��", "ȷ��Ҫɾ��ѡ�е���Դ��?");

		if (confirm) {			
			IRunnableWithProgress op = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					DeleteResouceElementsOperation operation = new DeleteResouceElementsOperation(elements, true);
					try {
						operation.runOperation(monitor);
					} catch (CoreException e) {
						logger.warn("ɾ����Դ��ʱ�����.", e);
					}
				}
			};

			try {
				PlatformUI.getWorkbench().getProgressService().run(true, true, op);
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
			
		}
	}
}
