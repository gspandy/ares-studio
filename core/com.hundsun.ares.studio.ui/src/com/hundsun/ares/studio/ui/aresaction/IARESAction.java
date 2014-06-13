/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.aresaction;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * ARESͨ�ò����ӿ�
 * @author sundl
 */
public interface IARESAction {

	/**
	 * ��ʼ������
	 * @param res ��ǰִ�в�������Դ
	 * @param targetPart ������õ�IWorkbenchPart������Action����UI
	 */
	public void init(IAresActionExcuteContext context);
	
	/**
	 * ִ�в���
	 * @param monitor ���ȼ�����
	 */
	public void execute(IProgressMonitor monitor);
	
	/**
	 * �Ƿ���ã�ֻ���ڳ�ʼ�����ú���ô˷�����
	 */
	public boolean isEnabled();
	
}
