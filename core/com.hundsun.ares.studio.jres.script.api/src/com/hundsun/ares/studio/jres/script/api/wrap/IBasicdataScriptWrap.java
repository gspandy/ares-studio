/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.api.wrap;

/**
 * �������ݶ���
 * 
 * @author lvgao
 *
 */
public interface IBasicdataScriptWrap {

	public static final String COMMONDATA_TYPE = "commondata";
	
	/**
	 * ��ȡ���ж�ά���������
	 * @return
	 */
	public ISingleTableScriptWrap[] getAllTableBasicData();
	
	/**
//	 * ��ȡ�������ӱ��������
	 * @return
	 */
	public IMasterSlaveTableScriptWrap[] getAllMasterSlaveTableBasicData();
	
	/**
	 * ��ȡ�������ӹ������������
	 * @return
	 */
	public IMasterSlaveLinkTableScriptWrap[] getAllMasterSlaveLinkTableBasicData();
	
	/**
	 * ������ϵͳ����ȡ��ά���������
	 * @param subsysName
	 * @return
	 */
	public ISingleTableScriptWrap[] getTableBasicDataBySubsys(String subsysName);
	
	/**
	 * ����ģ������ȡ��ά���������
	 * @param moduleName ģ�����֣�������ڶ༶���á�.���ָ�
	 * @return
	 */
	public ISingleTableScriptWrap[] getTableBasicDataByModule(String moduleName);
	
	/**
	 * ������ϵͳ����ȡ���ӱ��������
	 * @param subsysName
	 * @return
	 */
	public IMasterSlaveTableScriptWrap[] getMasterSlaveTableBasicDataBySubsys(String subsysName);
	
	/**
	 * ����ģ������ȡ���ӱ��������
	 * @param moduleName ģ�����֣�������ڶ༶���á�.���ָ�
	 * @return
	 */
	public IMasterSlaveTableScriptWrap[] getMasterSlaveTableBasicDataByModule(String moduleName);
	
	/**
	 * ������ϵͳ����ȡ������Ϣ���������
	 * @param subsysName
	 * @return
	 */
	public IMasterSlaveLinkTableScriptWrap[] getMasterSlaveLinkTableBasicDataBySubsys(String subsysName);
	
	/**
	 * ����ģ������ȡ������Ϣ���������
	 * @param moduleName ģ�����֣�������ڶ༶���á�.���ָ�
	 * @return
	 */
	public IMasterSlaveLinkTableScriptWrap[] getMasterSlaveLinkTableBasicDataByModule(String moduleName);
	
	/**
	 * ͨ�����Ʋ��Ҷ�ά��
	 * @param name
	 * @return
	 */
	public ISingleTableScriptWrap getTableBasicDataByName(String name);
	
	/**
	 * ͨ�����Ʋ������ӱ�
	 * @param name
	 * @return
	 */
	public IMasterSlaveTableScriptWrap getMasterSlaveTableBasicDataByName(String name);
	
	/**
	 * ͨ�����Ʋ������ӹ�����
	 * @param name
	 * @return
	 */
	public IMasterSlaveLinkTableScriptWrap getMasterSlaveLinkTableBasicDataByName(String name);
	
	/**
	 * ��ȡ����Ԫ���ݻ�������
	 * @return
	 */
	public ISingleTableScriptWrap[] getAllMetaDataBasicData();
	
	/**
	 * ͨ�����Ʋ���Ԫ���ݻ�������
	 * @param name
	 * @return
	 */
	public ISingleTableScriptWrap getMetaDataBasicDataByName(String name);
	
}
