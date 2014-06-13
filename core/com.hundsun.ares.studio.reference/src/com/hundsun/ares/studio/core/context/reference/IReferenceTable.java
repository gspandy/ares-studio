/**
 * Դ�������ƣ�IReferenceTable.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.context.reference;

public interface IReferenceTable {
	/////////////////////////////////key����/////////////////////////////////////////
	public static final String TARGET_OWNER = "����������";
	public static final String TARGET_RESOURCE = "������Դ";
	
	///////////////////////////////ֵ����////////////////////////////////////////////
	public static final String VALUE_RES_IS_LIB_TRUE = "1"; 
	public static final String VALUE_RES_IS_LIB_FALSE = "0"; 

	
	//////////////////////////////////�ֶζ���/////////////////////////////////////
	/**����Դ��������**/
	public static final String MASTER_INDEX = "id";
	/**ԴID**/
	public static final String PROVIDER_ID = "providerid";
	/**����Դ��������**/
	public static final String MASTER_NAME = "m_name";
	/**����Դ����**/
	public static final String MASTER_TYPE = "m_type";
	/**����Դ�����ռ�**/
	public static final String MASTER_NAMESPACE = "m_namespace";
	/**������Դ��**/
	public static final String SLAVE_NAME = "s_name";
	/**��������**/
	public static final String SLAVE_TYPE = "s_type";
	/**���������ռ�**/
	public static final String SLAVE_NAMESPACE = "s_namespace";
	/**�Ƿ���������Դ����Դ**/
	public static final String RES_IS_LIB = "is_lib";
}
