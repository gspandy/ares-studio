/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.internal.ui.refactoring;

import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import com.hundsun.ares.studio.core.IARESElement;

/**
 * 
 * @author sundl
 */
public interface INameUpdating {

	public String getNewElementName();
	
	public void setNewElementName(String name);
	
	public String getCurrentElementName();
	
	/** �������������Element���󣬱������������������ָ�selection״̬ */
	public IARESElement getNewElement();
	
	public RefactoringStatus checkNewElementName(String name);
}
