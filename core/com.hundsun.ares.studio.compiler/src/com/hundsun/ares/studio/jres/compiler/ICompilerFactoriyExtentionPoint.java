/**
 * Դ�������ƣ�ICompilerFactoriyExtentionPoint.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.compiler;

/**
 * @author lvgao
 *
 */
public interface ICompilerFactoriyExtentionPoint {

	public static final String NAMESPACE = "com.hundsun.ares.studio.jres.core";
	public static final String EP_Name = "compilerFactories";
	public static final String EP_Attribute_ID = "id";
	public static final String EP_Attribute_Class = "class";
	public static final String EP_Attribute_Types = "types";
	public static final String EP_Attribute_Name = "name";
	public static final String EP_Attribute_Priority = "priority";
	
	public static final String EP_Element_EClass = "EClass";
	public static final String EP_Attribute_EClass_Uri = "uri";
	public static final String EP_Attribute_EClass_Name = "name";
	
}
