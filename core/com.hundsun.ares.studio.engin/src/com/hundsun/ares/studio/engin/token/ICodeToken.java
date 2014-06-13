package com.hundsun.ares.studio.engin.token;

import java.util.Map;

public interface ICodeToken {

	/**ע��*/
	public static final int COMMENT = 0;
	/**��*/
	public static final int MACRO = 1;
	/**��ͨ�����ı�*/
	public static final int CODE_TEXT = 2;
	/**α����*/
	public static final int PseudoCode = 3;
	/**�ַ���*/
	public static final int STRING = 4;
	
	/**����*/
	public static final String NEWLINE = "\r\n";
	/**���ַ���*/
	public static final String BlackString = "";
	
	/**
	 * ��ȡtoken����
	 * @return
	 */
	public String getContent();
	
	
	/**
	 * ��ȡ�������
	 * @return
	 */
	public int getType();
	
	/**
	 * ��ȡд���ļ�������
	 * @param context
	 * @return
	 */
	public String genCode(Map<Object, Object> context)throws Exception;
}
