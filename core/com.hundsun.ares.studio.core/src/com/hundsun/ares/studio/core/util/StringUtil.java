/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * �ַ�����ع�����
 * @author sundl
 */
public class StringUtil {

	/** �մ� 
	 * @Deprecated ʹ��apache commons�����util����
	*/
	public static final String EMPTY_STR = "";
	/**ʮ�������ַ���*/
	private static String hexString="0123456789ABCDEF";
	
	/**�ָ����ַ�*/
	public static final int SEPRATOR_ASCII_LEGAL=0;
	public static final int SEPRATOR_ASCII_ILLEGAL=1;
	public static final int SEPRATOR_LENGTH_RUN_OVER=2;

	/**
	 * ����������ַ���Ϊnull����nullֵת��Ϊ����Ϊ0���ַ���;
	 * 
	 * @param s
	 *            ��Ҫת�����ַ���
	 * @return �����Ϊnull�������ַ����������򷵻س���Ϊ0���ַ�����
	 */
	public static String convertString(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}

	/**
	 * ����ַ����Ƿ�Ϊ�ա�
	 * 
	 * @param target
	 * @return ���ַ�������Ϊnull��������Ϊ�գ���ֻ�пհ��ַ����򷵻�true��
	 * @deprecated ʹ��org.apache.common.lang�����ṩ�Ĺ��������
	 */
	public static boolean isEmpty(String target) {
		if (null == target || target.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * �Ƚ�����������ַ�����ʽ�Ƿ���ȡ�
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean equalsByString(Object obj1, Object obj2) {
		String str1 = String.valueOf(obj1);
		String str2 = String.valueOf(obj2);
		return str1.equals(str2);
	}

	/**
	 * �������ַ�����ɾ�������ĺ�׺��
	 * 
	 * @param s
	 *            �ַ���
	 * @param suffix
	 *            ��׺
	 * @return �������ַ�����
	 */
	public static String removeSuffix(String s, String suffix) {
		if (s == null) {
			return null;
		}
		if (suffix == null) {
			return s;
		}

		if (!s.endsWith(suffix)) {
			return s;
		}

		StringBuffer sb = new StringBuffer(s);
		int index = sb.lastIndexOf(suffix);
		sb.replace(index, sb.length(), EMPTY_STR); //$NON-NLS-1$
		return sb.toString();
	}

	/**
	 * �ڸ������ַ�����ȥ��������ǰ׺��
	 * 
	 * @param s
	 *            ��Ҫ������ַ���
	 * @param prefix
	 *            ǰ׺
	 * @return ȥ��ǰ׺����ַ���
	 */
	public static String removePrefix(String s, String prefix) {
		if (s == null) {
			return null;
		}
		if (prefix == null) {
			return s;
		}

		if (!s.startsWith(prefix)) {
			return s;
		}

		return s.replaceFirst(prefix, EMPTY_STR); //$NON-NLS-1$
	}

	/**
	 * ȥ�������ָ���֮ǰ�����ָ��������ִ��������ؽ���ַ�����
	 * 
	 * @param str
	 *            �������ַ���
	 * @param delimeter
	 *            �ָ���
	 * @return
	 * @deprecated use StringUtils.substringAfterLast instead
	 */
	public static String removePrefixBefore(String str, String delimeter) {
		return StringUtils.substringAfterLast(str, delimeter);
	}

	/**
	 * ����ָ���ķָ���֮ǰ���Ӵ���
	 * 
	 * @param str
	 *            �ַ���
	 * @param delimeter
	 *            �ָ���
	 * @return ָ���ķָ���֮ǰ���Ӵ�������������ַ��������ָ��������س���Ϊ0���ַ�����
	 * @deprecated ʹ��org.apache.common.lang�����ṩ�Ĺ��������
	 */
	public static String getPrefix(String str, String delimeter) {

		if (str == null)
			return null;
		if (delimeter == null)
			return str;

		int index = str.indexOf(delimeter);
		if (index >= 0) {
			return str.substring(0, index);
		} else {
			return EMPTY_STR;
		}

	}

	/**
	 * ȡ�ļ�����ȥ����չ����
	 * @param fullFileName
	 * @return
	 * @deprecated ʹ��org.apache.common.lang�����ṩ�Ĺ��������
	 */
	public static String getFileNameWithoutExt(String fullFileName) {
		int dot;
		if ((dot = fullFileName.indexOf(".")) != -1) {
			return fullFileName.substring(0, dot);
		}
		return fullFileName;
	}
	
	/**
	 * ����ַ����Ƿ�Ϊλ����8Ϊ���ڵ������ַ���������������
	 * 
	 * @param target
	 * @return ���ַ�������Ϊ���ַ���TRUE�����򷵻�FALSE��Ϊ��ʱ�ж�ΪFALSE
	 * @deprecated ʹ��org.apache.common.lang�����ṩ�Ĺ��������
	 */
	public static boolean isNumber(String target) {
		if(null ==target){
			return false;
		}
		Pattern pattern = Pattern.compile("-?\\d{1,8}");
		Matcher matcher = pattern.matcher(target);
		return matcher.matches();
	}
	
	/**
	 * ����ַ����Ƿ�Ϊλ����8Ϊ���ڵ���������
	 * 
	 * @param target
	 * @return ���ַ�������Ϊ���ַ���TRUE�����򷵻�FALSE��Ϊ��ʱ�ж�ΪFalSE
	 * @deprecated ʹ��org.apache.common.lang�����ṩ�Ĺ��������
	 */
	public static boolean isPositiveNumber(String target) {
		if(null ==target){
			return false;
		}
		Pattern pattern = Pattern.compile("\\d{1,8}");
		Matcher matcher = pattern.matcher(target);
		return matcher.matches();
	}

	
	/**
	 * ����ַ����Ƿ�Ϊ���֡�
	 * 
	 * @param target
	 * @return ���ַ�������Ϊ���ַ���TRUE�����򷵻�FALSE��
	 */
	public static boolean isXmlNodeSn(String target) {
		// ��������9λ����������n
		Pattern pattern = Pattern.compile("\\d{0,9}|n");
		Matcher matcher = pattern.matcher(target);
		return matcher.matches();
	}

	/**
	 * ����ַ����Ƿ�Ϊ����8583���ŵ����֡� ���Ϊ128 ��СΪ 0
	 * 
	 * @param target
	 * @return ����ַ�Ϊ�ա�������TRUE�����ַ�������Ϊ���ַ���TRUE�����򷵻�FALSE��
	 */
	public static boolean is8583Number(String target) {
		if (target.equals("")) {
			return false;
		}
		try {
			if (Integer.valueOf(target) >= 0 && Integer.valueOf(target) <= 128)
				return true;
		} catch (Exception ex) {
			return false;
		}
		return false;
	}

	/**
	 * ����ַ����Ƿ�Ϊ�������ַ����ַ�����
	 * 
	 * @param target
	 * @return ���ַ����������ַ��򷵻�TRUE�����Ϸ�����FALSE��
	 */
	public static boolean isNormal(String target) {
		Pattern pattern = Pattern.compile("[0-9a-zA-Z][0-9a-zA-Z]*");
		Matcher matcher = pattern.matcher(target);
		return matcher.matches();
	}

	/**
	 * aselect�������ַ���0�����򷵻���Ӧ����
	 * 
	 * @param aselect
	 * @return
	 */
	public static int parseInt(String aselect) {
		if (!StringUtil.isNumber(aselect)) {
			return 0;
		} else {
			return Integer.parseInt(aselect);
		}
	}
	
	public static String toHEX(String asciitxt) {
		// ����Ĭ�ϱ����ȡ�ֽ�����
		byte[] bytes = asciitxt.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// ���ֽ�������ÿ���ֽڲ���2λ16��������
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}
	
	public static String toASCII(String hextxt) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(hextxt.length() / 2);
		// ��ÿ2λ16����������װ��һ���ֽ�
		for (int i = 0; i < hextxt.length(); i += 2){
			baos.write((hexString.indexOf(hextxt.charAt(i)) << 4 | hexString.indexOf(hextxt.charAt(i + 1))));
		}
		return new String(baos.toByteArray());
	}
	
	/**
	 * ���ַ����еĸ�ʽ�ַ�ת���ɿ����ַ�
	 * 
	 * @param str
	 * @return
	 */
	public static String toViewText(String str) {
		StringBuffer sb = new StringBuffer();
		char[] chars = str.toCharArray();
		for (char c : chars) {
			switch (c) {
			case '\n':
				sb.append("\\n");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\r':
				sb.append("\\r");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		
		return sb.toString();
	}
	public static String getCDADAStr(String paramStr)
	{
		return MessageFormat.format("<![CDATA[{0}]]>", paramStr);
	}
	public static String removeCDADAStr(String cdataStr)
	{
		if(cdataStr.indexOf("<![CDATA[")!=-1)
			return cdataStr.replace("<![CDATA[", "").replace("]]>","");
		return cdataStr;
	}
	
	/**
	 * ���ܰ��������ַ�
	 */
	public static boolean isEspeciallyValidStr(String id) {
		return id.replaceAll("[a-z]*[A-Z]*\\d*_*\\s*", "").length() == 0;
	}

	/**
	 * ���ܰ��������ֿ�ͷ���ַ�
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isPreffixNum(String id) {
		return !id.matches("[0-9]\\w*");
	}
	
	/**
	 * ���ַ�������ĸ��д
	 * 
	 * @param target
	 * @return
	 */
	public static String upperFirstLetter(String target) {
		return target.substring(0, 1).toUpperCase() + target.substring(1);
	}

	/**
	 * @param sectionName
	 * @return
	 */
	public static String getStringSafely(String str) {
		if(str == null){
			return "";
		}
		return str;
	}
	
	/**
	 * ��һ��InputStream���һ���ı���
	 * 
	 * @param is
	 * @return
	 */
	public static String getString(InputStream is) {
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String readed;
		try {
			while ((readed = reader.readLine()) != null) {
				sb.append(readed);
				sb.append("\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * ����ļ�����
	 * 
	 * @param operation
	 * @return
	 */
	public static String getFileContent(String fileName) {
		File scriptFile = new File(fileName);
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(scriptFile);
			String scriptCode = IOUtils.toString(inputStream, "UTF-8");
			return scriptCode;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		return "";
	
	}
}
