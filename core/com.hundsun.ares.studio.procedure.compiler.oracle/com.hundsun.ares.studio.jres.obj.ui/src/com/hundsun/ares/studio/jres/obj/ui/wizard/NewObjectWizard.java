package com.hundsun.ares.studio.jres.obj.ui.wizard;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.biz.ARESObject;
import com.hundsun.ares.studio.biz.constants.IBizResType;
import com.hundsun.ares.studio.ui.newwizard.ARESResourceNewWizardPage;
import com.hundsun.ares.studio.ui.newwizard.ModuleARESResourceNewWizard;


/**
 * �½������򵼣� �����Խ�����µ���Ŀ���ͣ���׼������JRES20����
 * @author sundl
 *
 */
public class NewObjectWizard extends ModuleARESResourceNewWizard{
	
	@Override
	protected void initNewResourceInfo(Object info) {
		super.initNewResourceInfo(info);
		if(info instanceof ARESObject){
			Map<Object, Object> context = getContext();
			String resname = context.get(ARESResourceNewWizardPage.CONTEXT_KEY_NAME).toString();
			String resCName = context.get(ARESResourceNewWizardPage.CONTEXT_KEY_CNAME).toString();
			if(StringUtils.isNotBlank(resname)){
				((ARESObject)info).setName(resname);
			}
			if(StringUtils.isNotBlank(resCName)){
				((ARESObject)info).setChineseName(resCName);
			}
		}
	}
	
	@Override
	protected String getResType() {
		return IBizResType.Object;
	}
	
}
