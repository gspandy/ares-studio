/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.newwizard;

import java.util.Map;

/**
 * @author lvgao
 *
 */
public interface IAresWizardPage {

	/**
	 * ���������ģ����Է���
	 * context����Wizard
	 * 1�����ڼ�������
	 * 2��page֮������ݴ���
	 * 3��wizard��page֮�䴫������
	 * @param context
	 */
	public void setContext(Map<Object, Object> context);
	
}
