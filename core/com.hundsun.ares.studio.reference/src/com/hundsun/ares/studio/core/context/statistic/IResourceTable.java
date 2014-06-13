/**
 * Դ�������ƣ�IResourceTable.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.context.statistic;

public interface IResourceTable {
	
////////////////////////////////Ŀ�����///////////////////////////////
	public static final String TARGET_OWNER = "����������";
	public static final String TARGET_RESOURCE = "������Դ";
	
	
	

////////////////////////��������///////////////////////////////////
	public static final String VALUE_RES_IS_LIB_TRUE = "1"; 
	public static final String VALUE_RES_IS_LIB_FALSE = "0"; 
	
	public static final String TREECAHCE_ROOT = "������Ϣ������ȫ�ֻ�����ڵ�";
	
	/**
	 * ���������ռ�
	 */
	public static final String Scope_IGNORE_NAMESPACE = "#ignore.namespace#"; 
	
	/**
	 * ��ǰ��Ŀ
	 */
	public static final String Scope_CURRENT = "#current#"; 
	
	
	/**
	 * ����
	 */
	public static final String Scope_REFERENCE = "#reference#"; 
	
	/**
	 * ȥ������
	 */
	public static final String Scope_UNIQUE = "#unique#"; 
	
	
////////////////////////////�ֶζ���////////////////////////
	/**��Դ��������**/
	public static final String RES_INDEX = "id";
	/**ԴID**/
	public static final String PROVIDER_ID = "providerid";
	/**��Դ��**/
	public static final String RES_NAME = "resname";
//	/**��Դ����**/
//	public static final String RES_LONGNAME = "reslongname";
	/**��Դ����**/
	public static final String RES_TYPE = "restype";
	/**�����ռ�**/
	public static final String RES_NAMESPACE = "namespace";
	/**�Ƿ��ǹ�����Դ**/
	public static final String RES_IS_PUBLIC = "is_public";
	/**�Ƿ������ð�**/
	public static final String RES_IS_LIB = "is_lib";
}
