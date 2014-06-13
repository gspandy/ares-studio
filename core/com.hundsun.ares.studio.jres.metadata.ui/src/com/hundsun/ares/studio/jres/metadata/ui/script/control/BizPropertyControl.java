/**
 * Դ�������ƣ�BizPropertyControl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.metadata.ui.script.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.jres.model.metadata.BizPropertyConfig;
import com.hundsun.ares.studio.jres.script.internal.useroption.MultiSelectionCheckControl;
import com.hundsun.ares.studio.jres.script.internal.useroption.UserOptionControl;

/**
 * @author sundl
 *
 */
public class BizPropertyControl extends UserOptionControl {
	
	public void setOptionValue(Map<String, Object> option) {
		if (control != null) {
			MultiSelectionCheckControl multiSelectionCheckControl = (MultiSelectionCheckControl) control;
			Object[] selection = multiSelectionCheckControl.getSelected();
			List<String> ids = new ArrayList<String>();
			for (Object obj : selection) {
				if (obj instanceof BizPropertyConfig) {
					BizPropertyConfig bizConfig = (BizPropertyConfig) obj;
					ids.add(bizConfig.getName());
				}
			}
			option.put(getID(), StringUtils.join(ids, ','));
		}
	}
	
}
