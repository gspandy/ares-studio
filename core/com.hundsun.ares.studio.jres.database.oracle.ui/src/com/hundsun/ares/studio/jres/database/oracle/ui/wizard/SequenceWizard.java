/**
 * Դ�������ƣ�SequenceWizard.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.sequence.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.oracle.ui.wizard;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hundsun.ares.studio.jres.database.oracle.ui.OracleUI;
import com.hundsun.ares.studio.jres.model.database.oracle.SequenceResourceData;
import com.hundsun.ares.studio.ui.newwizard.ARESResourceNewWizardPage;
import com.hundsun.ares.studio.ui.newwizard.ModuleARESResourceNewWizard;

/**
 * @author wangbin
 *
 */
public class SequenceWizard  extends ModuleARESResourceNewWizard {

	@Override
	protected String getResType() {
		return "jres_osequence";
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.newwizard.ARESResourceNewWizard#initNewResourceInfo(java.lang.Object)
	 */
	@Override
	protected void initNewResourceInfo(Object info) {
		super.initNewResourceInfo(info);
		if( info instanceof SequenceResourceData){
			Map<Object, Object> context = getContext();
			String resname = context.get(ARESResourceNewWizardPage.CONTEXT_KEY_NAME).toString();
			String resCName = context.get(ARESResourceNewWizardPage.CONTEXT_KEY_CNAME).toString();
			if(StringUtils.isNotBlank(resname)){
				((SequenceResourceData)info).setName(resname);
			}
			if(StringUtils.isNotBlank(resCName)){
				((SequenceResourceData)info).setChineseName(resCName);
			}
			
		}
	
	}
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.newwizard.ModuleARESResourceNewWizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		getShell().setImage(AbstractUIPlugin.imageDescriptorFromPlugin(OracleUI.PLUGIN_ID, "icons/sequence.gif").createImage());
		
	}


}
