/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.aresaction;


/**
 * �����һϵ��AresResourceִ��ĳ������
 * @author sundl
 */
public class AresActionAdvisor implements IAresActionAdvisor{
	
	protected IAresActionExcuteContext context;
	
	/**
	 * ��ʼ���������ܴ�����������ִ�й��������ݽ�����context.
	 * @param context
	 */
	public void init(IAresActionExcuteContext context) {
		this.context = context;
	}
	
	/**
	 * ִ��һЩ������
	 */
	public void dispose() {
		context = null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.aresaction.IAresActionAdvisor#preExcute()
	 */
	public void preExcute() {
	}


	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.aresaction.IAresActionAdvisor#postExcute()
	 */
	public void postExcute() {
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.aresaction.IAresActionAdvisor#willModifyWorkspace()
	 */
	@Override
	public boolean willModifyWorkspace() {
		return true;
	}
	
}
