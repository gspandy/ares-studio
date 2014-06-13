/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.newwizard;

import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.hundsun.ares.studio.core.IARESResource;

/**
 * @author lvgao
 *
 */
public class ReourceNameValidator extends BaseWizardPageValidator  implements IWizardPageValidator{

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.wizards.pages.IWizardPageValidator#validate(java.util.Map)
	 */
	@Override
	public IStatus validate(Map<Object, Object> context) {
		String name = context.get(ElementSelectionWizardPageWithNameInputEX.CONTEXT_KEY_NAME).toString();
		
		if (name.equals("")) {
			return getErrorStatus("���ֲ���Ϊ��");
		} else {
			Pattern pt = getNamePattern();
			if (pt != null) {
				if (!pt.matcher(name).matches()) {
					return getErrorStatus("���ֲ��Ϸ�(" + pt.toString() + ")");
				}
			}
		}
		return Status.OK_STATUS;
	}
	
	/** ������֤���ʽ, Ĭ��Ϊ��Դ�����ֹ���; ���������д�˷���, ����null��������    */
	protected Pattern getNamePattern() {
		return IARESResource.RES_NAME_PATTERN;
	}
	


}
