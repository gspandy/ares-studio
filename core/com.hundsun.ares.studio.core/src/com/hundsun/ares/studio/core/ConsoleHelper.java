/*
 * ϵͳ����: ARES Ӧ�ÿ��ٿ�����ҵ�׼�
 * ģ������:
 * �� �� ��: ConsoleHelper.java
 * �����Ȩ: ���ݺ������ӹɷ����޹�˾
 * ����ĵ�:
 * �޸ļ�¼:
 * �޸�����      �޸���Ա                     �޸�˵��<BR>
 * ========     ======  ============================================
 * 20110224     mawb	��Ӧ�޸ĵ��ţ�20110128022
 * ========     ======  ============================================
 * �����¼��
 * 
 * ������Ա��
 * �������ڣ�
 * �������⣺
 */
package com.hundsun.ares.studio.core;

import org.apache.log4j.Logger;


/**
 * ����̨
 * @author sundl
 */
public class ConsoleHelper {

	public static final String CONSOLE_ID = "ARES����̨";
	private static final Logger consoleLogger = Logger.getLogger("com.hundsun.ares.studio.core.Console");
	
	public static void println(String message) {
		consoleLogger.info(message);
	}
	
	public static void print(String message) {
		consoleLogger.info(message);
	}
	
//	public static void logException(Throwable e) {
//		e.printStackTrace(ARESCore.getDefault().getSystemConsoleWriter());
//	}
	
//	public static void logException(String message, Throwable e) {
//		println(message);
//		logException(e);
//	}
	
	public static Logger getLogger() {
		return consoleLogger;
	}
	
}
