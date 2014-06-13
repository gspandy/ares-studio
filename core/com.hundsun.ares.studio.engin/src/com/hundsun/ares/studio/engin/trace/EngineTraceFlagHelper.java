/**
 * Դ�������ƣ�JRESTraceFlag.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.engin.trace;

import org.eclipse.core.runtime.Platform;

/**
 * @author Administrator
 *
 */
public class EngineTraceFlagHelper {
	
	public static final String UFTEngine_DEBUG_OPTION = "com.hundsun.ares.studio.uft.engin/debug";
	
	private static boolean is_debug = false;
	
	static{
		try {
			is_debug = "true".equalsIgnoreCase(Platform.getDebugOption(UFTEngine_DEBUG_OPTION));
		} catch (Exception e) {
		}
	}
	
	/**
	 * ��ȡdebug���ٱ�־
	 * @return
	 */
	public static boolean getDebugTraceFlag(){
		return is_debug;
	}
}
