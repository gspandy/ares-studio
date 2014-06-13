/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * 
 * @author sundl
 */
public class AresConventions {
	
	public static final char DOT = '.';
	
	public static IStatus checkProjectId(String id) {
		return checkProjectId(id, 10);
	}
	
	public static IStatus checkProjectId(String id, int maxLength) {
		if (StringUtils.isEmpty(id)) {
			return Status.OK_STATUS;
		}
		
		IStatus result = Status.OK_STATUS;
		result = checkLength(id, maxLength);
		if (!result.isOK())
			return result;
		
		result = checkBeginningChar(id);
		if (!result.isOK())
			return result;
		
		result = checkEndingChar(id);
		if (!result.isOK())
			return result;
		
		result = checkMultiDots(id);
		if (!result.isOK())
			return result;
		
		result = validateChars(id);
		if (!result.isOK())
			return result;
		
		return Status.CANCEL_STATUS;
	}
	
	public static IStatus checkLength(String str, int maxLength) {
		int length = str.length();
		
		if (length > maxLength)
			return new Status(IStatus.ERROR, ARESCore.PLUGIN_ID, String.format("ID���Ȳ��ܳ���%s!", maxLength));
		
		return Status.OK_STATUS;
	}
	
	public static IStatus checkBeginningChar(String str) {
		if (CharUtils.isAsciiNumeric(str.charAt(0))) {
			return new Status(IStatus.ERROR, ARESCore.PLUGIN_ID, "ID���������ֿ�ͷ");
		} else if (str.charAt(0) == DOT) {
			return new Status(IStatus.ERROR, ARESCore.PLUGIN_ID, "ID�����Ե�(.)��ͷ");
		}
		return Status.OK_STATUS;
	}
	
	public static IStatus checkEndingChar(String str) {
		if (str.charAt(str.length() - 1) == DOT) {
			return new Status(IStatus.ERROR, ARESCore.PLUGIN_ID, "ID�����Ե�(.)��β");
		}
		return Status.OK_STATUS;
	}
	
	public static IStatus checkMultiDots(String str) {
		int length = str.length();
		int dot = 0;
		while (dot != -1 && dot < length-1) {
			if ((dot = str.indexOf(DOT, dot+1)) != -1 && dot < length-1 && str.charAt(dot+1) == DOT) {
				return new Status(IStatus.ERROR, ARESCore.PLUGIN_ID, "ID���������������ĵ�");
			}
		}
		return Status.OK_STATUS;
	}
	
	public static IStatus validateChars(String str) {
		int length = str.length();
		for (int i = 0; i < length; i++) {
			char ch = str.charAt(i);
			if (isValidChar(ch) || ch == DOT) {
				// OK
			} else {
				return new Status(IStatus.ERROR, ARESCore.PLUGIN_ID,"\"" + ch + "\" �ǷǷ��ַ�(ֻ��ʹ��Ӣ����ĸ�����֣��»���)");
			}
		}
		return Status.OK_STATUS;
	}
	
	/**
	 * �������ķ�������ĸ���»��߿�ͷ�����԰������֣���ĸ���»��ߡ�
	 * @param name
	 * @return
	 */
	public static IStatus checkName(String name) {
		if (StringUtils.isEmpty(name)) {
			return new Status(IStatus.ERROR, ARESCore.PLUGIN_ID, "����Ϊ��");
		}
		
		return Status.OK_STATUS;
	}
	
	/**
	 * ��ĸ�����֣��»���
	 * @param ch
	 * @return
	 */
	public static boolean isValidChar(char ch) {
		return CharUtils.isAsciiAlpha(ch) || CharUtils.isAsciiNumeric(ch) || ch == '_';
	}
	
}
