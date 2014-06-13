/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.newwizard;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.hundsun.ares.studio.ui.ARESUI;

/**
 * 
 * @author sundl
 */
public class GroupNameValidator extends BaseWizardPageValidator  implements IWizardPageValidator{

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.newwizard.IWizardPageValidator#validate(java.util.Map)
	 */
	@Override
	public IStatus validate(Map<Object, Object> context) {
		String group = (String) context.get(ARESResourceNewWizardPage.CONTEXT_KEY_GROUP);
		if (StringUtils.contains(group, '/'))
			return new Status(IStatus.ERROR, ARESUI.PLUGIN_ID, "���������ܰ����ַ� /");
		return Status.OK_STATUS;
	}

}
