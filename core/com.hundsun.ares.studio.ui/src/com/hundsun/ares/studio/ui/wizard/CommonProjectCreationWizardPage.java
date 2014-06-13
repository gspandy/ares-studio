/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.wizard;

import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

/**
 * 
 * @author sundl
 */
public class CommonProjectCreationWizardPage extends WizardNewProjectCreationPage{

	public CommonProjectCreationWizardPage(String pageName) {
		super(pageName);		
	}
	@Override
	 public void createControl(Composite parent) {
		super.createControl(parent);
		validatePage();
	 }
	@Override
	 protected boolean validatePage() {
		 String name=getProjectName();
		 if(name==null||name==""){
			 setErrorMessage("��Ŀ������Ϊ�գ�");
			 return false;
		 }
		 Pattern PROJECT_NAME_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]{0,49}$");
		 if (!PROJECT_NAME_PATTERN.matcher(name).matches()) {
				setErrorMessage("���ֲ��Ϸ�(" + PROJECT_NAME_PATTERN.toString() + ")");
				return false;
			}
		 if(!super.validatePage())
			 return false;
		 return true;
	 }

}
