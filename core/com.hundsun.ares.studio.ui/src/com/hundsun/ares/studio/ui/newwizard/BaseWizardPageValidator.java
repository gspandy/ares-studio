/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.newwizard;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * @author lvgao
 *
 */
public class BaseWizardPageValidator {

	protected IStatus getErrorStatus(String message){
		return new Status(IStatus.ERROR, getPluginId(), -1, message, null);
	}
	
	protected String getPluginId(){
		return "com.hundsun.ares.studio.jres.ui";
	}
}
