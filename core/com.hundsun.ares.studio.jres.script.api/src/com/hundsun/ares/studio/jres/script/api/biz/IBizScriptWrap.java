package com.hundsun.ares.studio.jres.script.api.biz;

/**
 * ҵ���߼���ص�API�ӿ�
 * @author sundl
 *
 */
public interface IBizScriptWrap {

	/**
	 * ��ȡ���еķ�����Դ
	 * @return
	 */
	IBizServiceWrap[] getServices();
	
	/**
	 * ����<b>ȫ��</b>������Դ
	 * @param name  ��ģ��ǰ׺��ȫ��
	 * @return
	 */
	IBizServiceWrap getServiceByName(String name);
	
	/**
	 * ����<b>����</b>��������Դ��������������ҵ��ĵ�һ�����ϸ���������������Դ������еĻ���
	 * @param name ������
	 * @return
	 */
	IBizServiceWrap getServiceByCName(String name);
	
	/**
	 * ��ȡ��ϵͳ�µ����еķ�����Դ
	 * @param subsysName
	 * @return
	 */
	IBizServiceWrap[] getServicesBySubsys(String subsysName);
	
	/**
	 * ��ȡģ���µ����еķ�����Դ
	 * @param moduleName
	 * @return
	 */
	IBizServiceWrap[] getServicesByModule(String moduleName);
	
}
