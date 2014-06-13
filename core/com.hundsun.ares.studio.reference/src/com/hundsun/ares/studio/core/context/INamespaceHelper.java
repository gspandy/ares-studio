/**
 * Դ�������ƣ�INamespaceHelper.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.context;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;

/**
 * @author lvgao
 *
 */
public interface INamespaceHelper {

	/**
	 * ��ȡ������Դ�������ռ�
	 * @param master
	 * @param referdata
	 * @return
	 */
	public String getSlaveNamespace(IARESResource master, String referdata); 
	
	/**
	 * ��ȡ������Դ�������ռ�
	 * @param master
	 * @param referdata
	 * @return
	 */
	public String getSlaveNamespace(String masternamespace, String referdata); 
	
	/**
	 * ��ȡ�����ռ䣬����ַ�����û�������ռ��򷵻ؿ��ַ���
	 * @param referdata
	 * @return ��֤�����ؿ�ָ��
	 */
	public String getNamespace(String referdata);
	
	
	public String getResourceNamespace(IARESResource master);
	/**
	 * ȥ�����õ������ռ�
	 * @param referdata
	 * @return
	 */
	public String removeNamespace(String referdata);
	
	/**
	 *��ȡ��Ŀ�������ռ�
	 * @param project
	 * @return
	 */
	public String getProjectNamespace(IARESProject project);
	
	/**
	 * Ĭ�ϵİ���ʵ��
	 */
	public INamespaceHelper INSTANCE = new DefaultNamespaceHelper();
}
