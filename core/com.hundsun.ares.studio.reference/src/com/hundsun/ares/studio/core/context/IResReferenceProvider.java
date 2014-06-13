/**
 * Դ�������ƣ�IResReferenceProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.context;

/**
 * ��Դ������Ϣ�ṩ��
 * @author lvgao
 *
 */
public interface IResReferenceProvider {

	/**
	 * ��ȡ����Դ���õ���Դ�б�
	 * @param masterUniqueName    ����Դ������
	 * @param type                ����Դ����
	 * @return
	 */
	public Object[] getReferList(String masterUniqueName,String masterNamespace,String masterType);
	
	
	/**
	 * ��ȡ�����˴���Դ����Դ�б�
	 * @param uniqueName        ��Դ������
	 * @param type              ��Դ����
	 * @return
	 */
	public Object[] getLinkList(String uniqueName,String namespace,String type);
}
