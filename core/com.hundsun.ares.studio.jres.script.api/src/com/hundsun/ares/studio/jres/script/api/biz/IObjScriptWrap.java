/**
 * Դ�������ƣ�IObjScriptWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.script.api.biz;

/**
 * �ṩ��ѯ����ӿڣ�Ϊ�˶����͸��ö�����Դ�������API�ӿ�ʵ�ֶ���ʵ��
 * @author sundl
 *
 */
public interface IObjScriptWrap {
	/**
	 * ��ȡ���еĶ�����Դ
	 * @return ��ǰ��Ŀ�����еĶ�����Դ
	 */
	IBizObjectWrap[] getObjects();
	
	/**
	 * ���Ҷ�����Դ
	 * @param name ��ģ��ǰ׺��ȫ��
	 * @return
	 */
	IBizObjectWrap getObjectByName(String name);
	
	/**
	 * ������ϵͳ����ȡ��������ж�����Դ
	 * @param subsys
	 * @return
	 */
	IBizObjectWrap[] getObjectsBySubsys(String subsysName);
	
	/**
	 * ���ݰ�����ȡ��������ж�����Դ
	 * @param moduleName  ģ�����֣�������ڶ༶���á�.���ָ�
	 * @return
	 */
	public IBizObjectWrap[] getObjectsByModule(String moduleName);
}
