/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.newwizard;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;

/**
 * @author lvgao
 *
 */
public interface IWizardPageValidator {

	/**
	 * ������
	 * @param context   ������
	 * @return
	 */
	public IStatus validate(Map<Object, Object> context);
	
	
}
