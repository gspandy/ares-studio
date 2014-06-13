/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.internal.ui.refactoring;

import org.eclipse.ltk.core.refactoring.Refactoring;

/**
 * 
 * @author sundl
 */
public class RenameARESResourceWizard extends RenameRefactoringWizard{

	public RenameARESResourceWizard(Refactoring refactoring) {
		super(refactoring);
	}

	@Override
	protected void addUserInputPages() {
		INameUpdating updating = (INameUpdating)getRefactoring().getAdapter(INameUpdating.class);
		RenameARESResourceWizardPage page = new RenameARESResourceWizardPage("������", updating.getCurrentElementName());
		addPage(page);
	}
	
}
