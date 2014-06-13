/**
 * Դ�������ƣ�GenDatabaseSQLAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;

import com.hundsun.ares.studio.jres.database.ui.wizard.GenSQLWizard;
import com.hundsun.ares.studio.ui.action.PopupAction;


/**
 * @author gongyf
 *
 */
public class GenDatabaseSQLAction extends PopupAction {

	/**
	 * 
	 */
	public GenDatabaseSQLAction() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) {
		if (getSelection() instanceof IStructuredSelection && !((IStructuredSelection) getSelection()).isEmpty()) {
			
			WizardDialog dlg = new WizardDialog(getShell(), new GenSQLWizard(getSelection()));
			dlg.open();
			
//			ProgressMonitorDialog dlg = new ProgressMonitorDialog(getShell());
//			try {
//				// ��ui�߳��е��ã���Ҫ��Ϊ�˽ű��ܹ����öԻ���
//				dlg.run(false, false, new IRunnableWithProgress() {
//					
//					@Override
//					public void run(IProgressMonitor monitor) throws InvocationTargetException,
//							InterruptedException {
//						monitor.beginTask("����SQL�ű�", IProgressMonitor.UNKNOWN);
//						
//						try {
//							IResourceCompilerFactory factory = CompileManager.getInstance().getFactoryByType(getCompileType());
//							if (factory != null) {
//								
//								// ����������Ҫ����IARESResource����
//								Map<Object, Object> context = new HashMap<Object, Object>();
//								
//								Object obj = ((IStructuredSelection)getSelection()).getFirstElement();
//								
//								final CompilationResult result = factory.create(obj).compile(obj, context);
//								
//								if (result.getStatus().getSeverity() != IStatus.OK) {
//									throw new InvocationTargetException(result.getStatus().getException(), result.getStatus().getMessage());
//									
//								}
//							} else {
//								throw new InterruptedException("�޷��ҵ�����������");
//								
//							}
//						} finally {
//							monitor.done();
//						}
//					}
//				});
//			} catch (Exception e) {
//				MessageDialog.openError(getShell(), "����", e.getMessage());
//			} 
		}
	}

}
