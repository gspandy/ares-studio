/**
 * 
 */
package com.hundsun.ares.studio.jres.service.ui.wizard;

/**
 * ҵ���߼������ṹ��������
 * 
 * @author yanwj06282
 *
 */
public class ExcelStructConstantDefine {

	//--------------------------------------����-------------------------------------------
	
	//����
	public static final String OBJECT_PREFIX = "����-";
	public static final String OBJECT_NAME = "������";
	public static final String OBJECT_CHINESE_NAME = "����������";
	public static final String OBJECT_VERSION = "�汾��";
	public static final String OBJECT_DESCRIPTION = "��������";
	public static final String OBJECT_SHEET_NAME = "ҵ������б�";
	
	/**
	 * ����˵�ҳ����
	 */
	public static final String[] OBJECT_MENU_TITLES = new String[]{"ģ����" ,"ģ��������","�����","������","����������","��������"};
	/**
	 * �����������
	 */
	public static final String[] OBJECT_PARAMETER_Titles = new String[]{"��������","������","������","����","��ϵ����","Ĭ��ֵ","��ע"};
	

	//--------------------------------------����-------------------------------------------
	
	
	//����
	public static final String SERVICE_PREFIX = "����-";
	public static final String SERIVCE_SHEET_NAME = "���ܽӿ��б�";
	public static final String SERIVCE_OBJECT_ID = "���ܺ�";
	public static final String SERIVCE_NAME = "������";
	public static final String SERIVCE_CHINESE_NAME = "����������";
	public static final String SERIVCE_VERSION = "�汾��";
	public static final String SERIVCE_UPDATE_DATE = "��������";
	
	/**
	 * ���ܲ˵�ҳ����
	 */
	public static final String[] SERVICE_MENU_TITLES = new String[]{"ģ����","ģ��������","���ܺ�","��������" ,"����������","����˵��"};
	/**
	 * ���������������
	 */
	public static final String[] SERVICE_INPUT_PARAMETER_TITLES = new String[]{"�������","������","������","����" ,"��ϵ����","Ĭ��ֵ","��ע"};
	
	/**
	 * ���������������
	 */
	public static final String[] SERVICE_OUTPUT_PARAMETER_TITLES = new String[]{"�������","������","������","����","��ϵ����","Ĭ��ֵ","��ע"};
	
	
	
	//---------------------------------------------------------------------------------
	
	public static final String HYPERLINK = "HYPERLINK(\"#'%1$s'A%2$s\",\"%3$s\")";
	
	/**
	 * ��ȡ�����ӱ��ʽ
	 * 
	 * @param packageName
	 * @param resourceName
	 * @param rowNum
	 * @return
	 */
	public static String getRefExpression(String packageName ,String resourceName , int rowNum ){
		return String.format(ExcelStructConstantDefine.HYPERLINK, packageName , rowNum ,resourceName);
	}
	
}
