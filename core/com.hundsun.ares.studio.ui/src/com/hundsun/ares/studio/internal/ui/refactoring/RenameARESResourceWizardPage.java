/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.internal.ui.refactoring;

import org.eclipse.ltk.core.refactoring.RefactoringStatus;

/**
 * ��������Դ��ҳ��
 * @author sundl
 */
public class RenameARESResourceWizardPage extends RenameInputWizardPage {

	public RenameARESResourceWizardPage(String description, String initialValue) {
		super(description, initialValue);
	}

	protected RefactoringStatus validateTextField(String text) {
		INameUpdating updating = (INameUpdating)getRefactoring().getAdapter(INameUpdating.class);
		
		if (updating != null) {
			updating.setNewElementName(text);
			return updating.checkNewElementName(text);
		}
			
		return null;
	}
	
}
