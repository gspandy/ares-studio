/**
 * Դ�������ƣ�OracleUserWizard.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�����
 */
package com.hundsun.ares.studio.jres.database.oracle.ui.wizard;

import com.hundsun.ares.studio.ui.newwizard.ModuleARESResourceNewWizard;

/**
 * @author wangbin
 *
 */
public class OracleUserWizard extends ModuleARESResourceNewWizard {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.wizard.ARESResourceWizard#getResType()
	 */
	@Override
	protected String getResType() {
		// TODO Auto-generated method stub
		return "oracleuser";
	}
	

//	@Override
//	public void addPages() {
//		IResDescriptor resDescriptor = ARESResRegistry.getInstance().getResDescriptor(getResType());		
//		page = new NewARESResourceWizardPage("����һ��" + resDescriptor.getName(), workbench, selectedElement, getResType()){
//			@Override
//			protected void initNewResourceInfo(Object info) {
//				super.initNewResourceInfo(info);
//			}
//		};
//		page.setNewName(initText_Name);
//		addPage(page);
//	}

}
