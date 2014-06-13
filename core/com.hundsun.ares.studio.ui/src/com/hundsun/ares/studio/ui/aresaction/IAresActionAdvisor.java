/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.aresaction;


/**
 * ����ִ��ĳ��Ares Action��Advisor�࣬��������Action�ĵ��ù��̡�
 * @author sundl
 */
public interface IAresActionAdvisor {

	/**
	 * ��ʼ���������ܴ�����������ִ�й��������ݽ�����context.
	 * @param context
	 */
	void init(IAresActionExcuteContext context);
	
	/**
	 * 2012-03-12 sundl ������������ж��Ƿ���޸Ĺ������䣬����ǣ������action�����
	 * IWorkspaceRunnable��ִ�С�
	 * @return
	 */
	boolean willModifyWorkspace();
	
	void preExcute();
	
	/**
	 * ִ������Ժ����
	 */
	void postExcute();
	
	/**
	 * ִ��һЩ������
	 */
	void dispose();
	
}
