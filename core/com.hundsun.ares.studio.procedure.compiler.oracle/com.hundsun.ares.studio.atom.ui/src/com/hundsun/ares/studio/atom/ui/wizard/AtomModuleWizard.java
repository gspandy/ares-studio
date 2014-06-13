package com.hundsun.ares.studio.atom.ui.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.hundsun.ares.studio.ui.wizard.ARESModuleWizard;

public class AtomModuleWizard extends ARESModuleWizard implements
		INewWizard {
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		setWindowTitle("�½�ģ��");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		this.page = new AtomModuleWizardPage("ģ��", workbench, selection);
		addPage(page);
	}
	
}
