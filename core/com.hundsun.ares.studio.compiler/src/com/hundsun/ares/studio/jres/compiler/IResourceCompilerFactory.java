/**
 * Դ�������ƣ�IResourceCompilerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.compiler;

import com.hundsun.ares.studio.core.IARESProject;

/**
 * ��Դ������������ͨ�������Ӧ����Դ���������ʵ���Դ������
 * @author gongyf
 *
 */
public interface IResourceCompilerFactory {
	
	/**
	 * ��ǰ��Դ�����������Ƿ���ڵ�ǰ������ʹ��
	 * @param project
	 * @return
	 */
	boolean isSupport(IARESProject project);
	
	/**
	 * Ϊһ����Դ����һ���µ���Դ������
	 * @param resource
	 * @return
	 */
	IResourceCompiler create(Object resource);
	

}
