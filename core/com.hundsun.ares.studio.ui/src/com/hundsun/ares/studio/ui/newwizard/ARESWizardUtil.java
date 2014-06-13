/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.newwizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

/**
 * @author lvgao
 *
 */
public class ARESWizardUtil {

	/**
	 * ��������
	 * @param workbench
	 * @param resource
	 */
	public static  void openEditorAndReveal(IWorkbench workbench,IResource resource) {
		if (resource != null && resource instanceof IFile && workbench != null) {
			IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
			try {
				IDE.openEditor(page, (IFile) resource);
				BasicNewResourceWizard.selectAndReveal(resource, workbench.getActiveWorkbenchWindow());
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}
}
