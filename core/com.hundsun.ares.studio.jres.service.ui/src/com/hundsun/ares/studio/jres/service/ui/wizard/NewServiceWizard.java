package com.hundsun.ares.studio.jres.service.ui.wizard;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.biz.constants.IBizResType;
import com.hundsun.ares.studio.jres.service.Service;
import com.hundsun.ares.studio.ui.newwizard.ARESResourceNewWizardPage;
import com.hundsun.ares.studio.ui.newwizard.ModuleARESResourceNewWizard;

/**
 * �½���������򵼣� �����Խ�����µ���Ŀ���ͣ���׼������JRES20����
 * @author sundl
 *
 */
public class NewServiceWizard extends ModuleARESResourceNewWizard{
	
	@Override
	protected void initNewResourceInfo(Object info) {
		super.initNewResourceInfo(info);
		if(info instanceof Service){
			Map<Object, Object> context = getContext();
			String resname = context.get(ARESResourceNewWizardPage.CONTEXT_KEY_NAME).toString();
			String resCName = context.get(ARESResourceNewWizardPage.CONTEXT_KEY_CNAME).toString();
			if(StringUtils.isNotBlank(resname)){
				((Service)info).setName(resname);
			}
			if(StringUtils.isNotBlank(resCName)){
				((Service)info).setChineseName(resCName);
			}
		}
	}
	
	@Override
	protected String getResType() {
		return IBizResType.Service;
	}
	
}
