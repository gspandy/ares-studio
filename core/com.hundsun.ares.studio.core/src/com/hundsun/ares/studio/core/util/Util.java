/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.util;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.registry.ARESResRegistry;
import com.hundsun.ares.studio.core.registry.IResDescriptor;


/**
 * �����ࡣ
 * @author sundl
 */
public class Util {
	/** ģ�����ֵ�Pattern,��ĸ��ͷ���������ĸ�����֣���ʱû�г������� */
	public static final Pattern VALID_MODULE_NAME = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*");

	/**
	 * Combines two hash codes to make a new one.
	 */
	public static int combineHashCodes(int hashCode1, int hashCode2) {
		return hashCode1 * 17 + hashCode2;
	}
	
	/**
	 * Returns the concatenation of the given array parts using the given separator between each part.
	 * <br>
	 * <br>
	 * For example:<br>
	 * <ol>
	 * <li><pre>
	 *    array = {"a", "b"}
	 *    separator = '.'
	 *    => result = "a.b"
	 * </pre>
	 * </li>
	 * <li><pre>
	 *    array = {}
	 *    separator = '.'
	 *    => result = ""
	 * </pre></li>
	 * </ol>
	 *
	 * @param array the given array
	 * @param separator the given separator
	 * @return the concatenation of the given array parts using the given separator between each part
	 */
	public static final String concatWith(String[] array, char separator) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0, length = array.length; i < length; i++) {
			buffer.append(array[i]);
			if (i < length - 1)
				buffer.append(separator);
		}
		return buffer.toString();
	}
	
	/**
	 * Returns a new array adding the second array at the end of first array.
	 * It answers null if the first and second are null.
	 * If the first array is null or if it is empty, then a new array is created with second.
	 * If the second array is null, then the first array is returned.
	 * <br>
	 * <br>
	 * For example:
	 * <ol>
	 * <li><pre>
	 *    first = null
	 *    second = "a"
	 *    => result = {"a"}
	 * </pre>
	 * <li><pre>
	 *    first = {"a"}
	 *    second = null
	 *    => result = {"a"}
	 * </pre>
	 * </li>
	 * <li><pre>
	 *    first = {"a"}
	 *    second = {"b"}
	 *    => result = {"a", "b"}
	 * </pre>
	 * </li>
	 * </ol>
	 * 
	 * @param first the first array to concatenate
	 * @param second the array to add at the end of the first array
	 * @return a new array adding the second array at the end of first array, or null if the two arrays are null.
	 */
	public static final String[] arrayConcat(String[] first, String second) {
		if (second == null)
			return first;
		if (first == null)
			return new String[] {second};

		int length = first.length;
		if (first.length == 0) {
			return new String[] {second};
		}
		
		String[] result = new String[length + 1];
		System.arraycopy(first, 0, result, 0, length);
		result[length] = second;
		return result;
	}
	
	/**
	 * Returns a trimmed version the simples names returned by Signature.
	 */
	public static String[] getTrimmedSimpleNames(String name) {
		String[] result = name.split("\\.");
		for (int i = 0, length = result.length; i < length; i++) {
			result[i] = result[i].trim();
		}
		return result;
	}
	
	/**
	 * �ļ����Ƿ�һ�����õ���Դ��
	 * @param name �ļ���
	 * @return ���ָ�����ļ����ǺϷ�����Դ�����򷵻�<code>true</code>�����򷵻�<code>false</code>
	 */
	public static boolean isCommonResourceFileName(String name) {
		if (StringUtil.isEmpty(name))
			return false;
		
		IResDescriptor resDesc = null;
		String[] names = name.split("\\.");
		if (names.length == 2) {
			String ext = names[1];
			resDesc = ARESResRegistry.getInstance().getResDescriptor(ext);
		}
		if (resDesc == null) {
			resDesc = ARESResRegistry.getInstance().getResDescriptor(name);
		}
		
		return resDesc != null;
	}

	/**
	 * �Ƿ�Ϸ�����Դ����
	 * @param name ��Դ���֣���ֹ��null
	 * @return ����ǺϷ��������򷵻�<code>true</code>
	 */
	public static boolean isValiedCommonResourceName(String name) {
		String[] names = name.split("\\.");
		if (names.length == 2) {
			String fileName = names[0];
			String ext = names[1];
			return IARESResource.RES_NAME_PATTERN.matcher(fileName).matches()
				&& IARESResource.RES_NAME_PATTERN.matcher(ext).matches();
		} 
		return false;
	}

	/**
	 * �Ƿ�Ϸ���ģ�����֡�
	 * @param name �������֣�����"."
	 * @return ����Ϸ��򷵻�<code>true</code>
	 */
	public static boolean isValidNameForModule(String name) {
		return VALID_MODULE_NAME.matcher(name).matches();
	}

	/**
	 * ���������Ƿ�Ϸ�
	 * @param names ���ģ��ȫ������������
	 * @return ���ÿ�����ֶ��ǺϷ��ģ��򷵻�<code>true</code>
	 */
	public static boolean isValidNamesForModule(String[] names) {
		for (String name : names) {
			if (!isValidNameForModule(name))
				return false;
		}
		return true;
	}
	
	public static IProgressMonitor monitorFor(IProgressMonitor monitor) {
		if (monitor == null) {
			return new NullProgressMonitor();
		}
		return monitor;
	}

}
