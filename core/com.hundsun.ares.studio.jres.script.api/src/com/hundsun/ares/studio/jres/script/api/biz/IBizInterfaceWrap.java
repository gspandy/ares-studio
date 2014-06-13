/**
 * Դ�������ƣ�IBizInterfaceWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.script.api.biz;

/**
 * @author sundl
 *
 */
public interface IBizInterfaceWrap {
	/**
	 * �����
	 * @return
	 */
	String getId();
	
	/**
	 * ������
	 * @return
	 */
	String getChineseName();
	
	/**
	 * �ӿڱ�־
	 * @return
	 */
	String getInterfaceFlag();
	
	/**
	 * ˵����Ϣ
	 * @return
	 */
	String getDescription();
	
	/**
	 * �汾��Ϣ�����޸ļ�¼�е����汾��
	 * @return
	 */
	String getVersion();
	
	/**
	 * �޸����ڣ��޸ļ�¼������޸�����
	 * @return
	 */
	String getUpdateDate();
	
	/**
	 * �Ƿ���������
	 * 
	 * @return
	 */
	boolean isInputCollection();
	
	/**
	 * �Ƿ���������
	 * 
	 * @return
	 */
	boolean isOutputCollection();
	
	/**
	 * ��ȡ�������
	 * 
	 * @return
	 */
	IParameterWrap[] getInputParameters();
	
	/**
	 * ��ȡ�������
	 * 
	 * @return
	 */
	IParameterWrap[] getOutputParameters();
	
	/**
	 * ���ݲ���ID ��ɾ���������
	 * 
	 * @param id
	 */
	void deleteInputParameter(String id);
	
	/**
	 * ���ݲ���ID ��ɾ���������
	 * 
	 * @param id
	 */
	void deleteOutputParameter(String id);
}
