/**
 * Դ�������ƣ�IDBConstant.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.constant;


/**
 * @author gongyf
 *
 */
public interface IDBConstant {
	
	/**
	 * ��ѡ�����õ�key
	 * 
	 */
	public static final String TABLE_NAME_LENGTH = "table_name_length";//���ݿ����
	public static final String TABLE_COLUMN_LENGTH= "table_column_length";//���ݿ���г���
	public static final String INDEX_LENGTH = "table_index_length";//���ݿ���������
	public static final String CONSTRAINT_LENGTH = "table_constraint_length";//Լ��������
	
	/**
	 * ģ�鹤����չģ�͵ļ�ֵ
	 */
	public static final String MODULE_DATABASE_EXTEND_PROPERTY_KEY = "DBModuleCommonProperty";
	
	/**
	 * �������ݿ����ݽű��ļ�����ʽ
	 * @deprecated �����°�Ľű��滻
	 */
	public static final String GEN_JS_FILE_FORMAT = "database_generate_%s.js";
	
	
	/**
	 * �����Ż����������ݿ����ݵĽű�
	 */
	public static final String GEN_JS_FILE_DATABASE = "db_gensql_oracle.js";
	
	/**
	 * �������ݿ��ռ�Ľű�
	 */
	public static final String GEN_TABLE_SPACE_JS_FILE_DATABASE = "db_gensql_ts_oracle.js";
	
	/**
	 * �������ݿ��û��Ľű�
	 */
	public static final String GEN_DATABASE_USER_JS_FILE_DATABASE = "db_gensql_user_oracle.js";
	
	/**
	 * ���ڱ������ݿⴴ���ű�������
	 * �������ڱ���Ķ�������� ���ݿ�������ݿ�ģ�顢ĳ�����ݿ���Դ
	 */
	public static final String COMPILE_DATABASE_FULL = "#database.gensql.full#";
	
	/**
	 * ���ڱ������ݿ������ű�������
	 * �������ڱ���Ķ�������� ���ݿ�������ݿ�ģ�顢ĳ�����ݿ���Դ
	 */
	public static final String COMPILE_DATABASE_PATCH = "#database.gensql.patch#";
	
	/**
	 * ��ռ�
	 */
	public static final String COMPILE_DATABASE_SPACE = "#database.space.gensql.full#";
	
	/**
	 * ���ݿ��û�
	 */
	public static final String COMPILE_DATABASE_USER = "#database.user.gensql.full#";
	
	/**
	 * ���ɽű������ݿ�����������
	 * 
	 */
	public static final String JS_GEN_TABLE_INFO_FUNCTION = "genTableResource";
	
	/**
	 * ���ɽű�����ͼ����������
	 * 
	 */
	public static final String JS_GEN_VIEW_INFO_FUNCTION = "genViewResource";
	
	/**
	 * ���ɽű������е���������
	 * 
	 */
	public static final String JS_GEN_OSEQUENCE_INFO_FUNCTION = "genSequenceResource";
	
	/**
	 * ���ɽű��ڴ���������������
	 * 
	 */
	public static final String JS_GEN_OTRIGGER_INFO_FUNCTION = "genTriggerResource";
	
	/**
	 * ��������ű��ڵ���������
	 * 
	 */
	public static final String JS_GEN_INFO_FOREIGN_KEY_FUNCTION = "getTableForeignKeySql";
	
	/**
	 * ���ɽű����������ɲ�������������
	 */
	public static final String JS_GEN_INFO_PATCH_FUNCTION = "genTableResourcePatch";
	
	public static final String JS_GEN_TABLE_SPACE_INFO_FUNCTION = "genTableSpaceResource";
	
	public static final String JS_GEN_DATABASE_USER_INFO_FUNCTION = "genDatabaseUserResource";
	
	/**
	 * js�ű����������ĵ�key
	 */
	public static final String KEY_JS_GEN_CONTEXT = "genContext";
	
	/**
	 * ��༭���ı༭��ID
	 */
	public static final String ID_TABLE_EDITDOMAIN = "com.hundsun.ares.studio.jres.database.resource.table";
	
	/**
	 * ��ͼ�༭���ı༭��ID
	 */
	public static final String ID_VIEW_EDITDOMAIN = "com.hundsun.ares.studio.jres.database.resource.view";
	
	/**
	 * ���ݿ�����
	 */
	public static final String DATABASE_TYPE = "";
	
	/**
	 * ���ݿ�ģ�������
	 */
	public static final String ROOT_TYPE = "com.hundsun.ares.studio.jres.moduleroot.database";
	/**�����ŷ�Χ��չkey*/
	public static final String TABLE_ID_RANGE_KEY = "tableidrange";
	/**��ͼ����ŷ�Χ��չkey*/
	public static final String VIEW_ID_RANGE_KEY = "viewidrange";
	/**��ͼ����ŷ�Χ��չkey*/
	public static final String SEQUENCE_ID_RANGE_KEY = "sequenceidrange";
	
	/**��Ŀ������չ���Ƿ�ʹ�÷Ǳ�׼�ֶ� */
	public static final String USE_NON_STD_FIELD = "use_non_std_field";
}
