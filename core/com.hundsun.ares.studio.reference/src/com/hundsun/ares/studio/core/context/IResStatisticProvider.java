/**
 * Դ�������ƣ�IResStatisticProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.context;

public interface IResStatisticProvider {

	/**
	 * �ж���Դ�Ƿ����
	 * �ô�:������Դ�ж�   �½���Դ�ж�
	 * @param uniqueName
	 * @param restype
	 * @return
	 */
	public boolean isResouceExist(String uniqueName,String namespace,String restype);
	
	/**
	 * �ж���Դ�Ƿ�Ψһ
	 * �ô��������ж�
	 * @param uniqueName
	 * @param restype
	 * @return
	 */
	public boolean isResouceUnique(String uniqueName,String namespace,String restype);
	
	/**
	 * ��ȡ��Դ
	 * �ô�:������Դ��ת
	 * @param uniqueName
	 * @param restype
	 * @return
	 */
	public Object[] getResouce(String uniqueName,String namespace,String restype);
	
	/**
	 * ͨ����Դ���ͻ�ȡ��Դ
	 * @param restype
	 * @return
	 */
	public Object[] getResouceByType(String restype);
	
	/**
	 * ��ȡ�����͵���Դ
	 * @param restype
	 * @return
	 */
	public Object[] getResouceByTypes(String[] restypes);
	
	/**
	 * ��ȡ��Դ������
	 * @param uniqueName
	 * @param namespace
	 * @param restype
	 * @return
	 */
	public int getResourceCount(String uniqueName, String namespace,String restype);
	
	/**
	 * ͨ������ֵ��ȡ��Դ
	 * @param index
	 * @return
	 */
	public Object getResouceByIndex(String index);
}
