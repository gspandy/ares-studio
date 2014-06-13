/**
 * Դ�������ƣ�TriggerWizard.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.trigger.ui
 * ����˵�����������༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.oracle.ui.wizard;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.jres.model.database.oracle.TriggerResourceData;
import com.hundsun.ares.studio.ui.newwizard.ARESResourceNewWizardPage;
import com.hundsun.ares.studio.ui.newwizard.ModuleARESResourceNewWizard;

/**
 * @author wangxh
 *
 */
public class TriggerWizard extends ModuleARESResourceNewWizard{

	@Override
	protected String getResType() {
		return "jres_otrigger";
	}

	@Override
	protected void initNewResourceInfo(Object info) {
		super.initNewResourceInfo(info);
		if( info instanceof TriggerResourceData){
			Map<Object, Object> context = getContext();
			String resname = context.get(ARESResourceNewWizardPage.CONTEXT_KEY_NAME).toString();
			String resCName = context.get(ARESResourceNewWizardPage.CONTEXT_KEY_CNAME).toString();
			if(StringUtils.isNotBlank(resname)){
				((TriggerResourceData)info).setName(resname);
			}
			if(StringUtils.isNotBlank(resCName)){
				((TriggerResourceData)info).setChineseName(resCName);
			}
		}
		
	}

}
