/**
 * Դ�������ƣ�IContextUpdateSource.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.context;

/**
 * �����ĸ���Դ
 * @author lvgao
 *
 */
public interface IContextUpdateSource {

	/**
	 * ��������
	 * @return
	 */
	public String getType();
	
	/**
	 * ��ȡ������ص�����
	 * @return
	 */
	public Object[] getContent();
}
