/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.engin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * ���������жϹ���
 * @author maxh
 * @version 1.0
 * @history
 */
public class TypeRule {
	
	public static boolean typeRuleClob(String type) {
		return type.equalsIgnoreCase("clob");
	}
	
	public static boolean typeRuleObject(String type) {
		return type.equalsIgnoreCase("object");
	}

	public static boolean typeRuleDouble(String type) {
		return type.equalsIgnoreCase("double");
	}

	public static boolean typeRuleFloat(String type) {
		return type.equalsIgnoreCase("float");
	}

	public static boolean typeRuleInt(String type) {
		return type.equalsIgnoreCase("int");
	}

	public static boolean typeRuleChar(String type) {
		return type.equalsIgnoreCase("char");
	}
	
	public static boolean typeRulePacker(String type) {
		return type.toLowerCase().startsWith("if2unpacker");
	}
	public static boolean typeRulePacker1(String type) {
		return type.toLowerCase().startsWith("if2packer");
	}

	public static boolean typeRuleCharArray(String type) {
		Pattern p = Pattern.compile("(char\\[\\d*\\])");
		Matcher m = p.matcher(type);
		return m.find();
	}

	// �Ƿ���numeric����
	public static boolean typeRuleNumeric(String type) {
		if (type != null && !type.equals("")) {
			Pattern p = Pattern.compile("(numeric\\(\\d+,\\d+\\))|(number\\(\\d+,\\d+\\))");
			Matcher m = p.matcher(type);
			return m.find();
		} else {
			return false;
		}
	}

	// �õ�numeric��һ������ֵ
	private static String getFirstPrecision(String type) {
		return type.substring(type.indexOf("(") + 1, type.indexOf(","));
	}

	// �õ�numeric�ڶ�������ֵ
	public static String getSecondPrecision(String type) {
		return type.substring(type.indexOf(",") + 1, type.indexOf(")"));
	}

	/**
	 * �õ�numeric�ܳ���
	 * 
	 * @param type
	 * @return
	 */
	public static int getTotalPrecision(String type) {
		String first = TypeRule.getFirstPrecision(type);
		int total = Integer.parseInt(first) + 2;
		return total;
	}

	/**
	 * ��char[num]�в��������
	 * 
	 * ����char ����[num]
	 */
	public static String insertNameToCharArray(String type, String name) {

		// return "char " + name + type.substring(type.indexOf("["));
		// �ַ�����+1
		return "char " + name + "[" + (Integer.parseInt(getCharLength(type)) + 1) + "]";
	}

	/**
	 * ��ȡ�ַ������͵ĳ���
	 * @param type �ַ�������
	 * @return String ����
	 */
	public static String getCharLength(String type) {
		String length = type.substring(type.indexOf("[") + 1, type.indexOf("]"));
		if(StringUtils.isEmpty(length)){//��ֹ[]���
			length= "0";
		}
		return length;
	}

	// charArray�ĳ��ȴ���255
	public static boolean greaterThan255(String type) {
		if (typeRuleCharArray(type)) {
			return Integer.parseInt(getCharLength(type)) > 255;
		}
		return false;
	}

	// charArray�ĳ��ȴ���1
	public static boolean greaterThan1(String type) {
		if (typeRuleCharArray(type)) {
			return Integer.parseInt(getCharLength(type)) > 1;
		}
		return false;
	}

}
