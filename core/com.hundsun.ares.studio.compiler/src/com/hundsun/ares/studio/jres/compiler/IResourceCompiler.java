/**
 * Դ�������ƣ�IResourceCompiler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�����
 */
package com.hundsun.ares.studio.jres.compiler;

import java.util.Map;

/**
 * ����һ����Դ
 * @author gongyf
 *
 */
public interface IResourceCompiler {
	/**
	 * ��һ����Դ���б��룬����һ��������
	 * 
	 * TODO ���Կձ��룬��ֻ���ر�����ܵ��ļ���
	 * @param resource
	 * @param unitFactory
	 * @param context
	 * @return
	 */
	CompilationResult compile(Object resource, Map<Object, Object> context);

	/**
	 * �����Դ����������ļ�
	 * @param resource
	 * @param context
	 */
	public void clean(Object resource,Map<Object, Object> context);
}
