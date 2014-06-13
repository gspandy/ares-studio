/**
 * Դ�������ƣ�DBTableDefine.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author yanwj06282
 *
 */
public class DBTableDefine {
	
	private static List<String> textTitles = new ArrayList<String>(); 
	private static List<String> columnTitles = new ArrayList<String>();
	private static List<String> indexTitles = new ArrayList<String>();
	private static List<String> keyTitles = new ArrayList<String>();
	private static Map<String , Integer> tableType = new HashMap<String, Integer>();
	
	static {
		textTitles.add(DBTableDefine.BASE_NAME);
		textTitles.add(DBTableDefine.BASE_CHINESE_NAME);
		textTitles.add(DBTableDefine.BASE_DESCRIPTION);
//		textTitles.add(DBTableDefine.BASE_VERSION);
//		textTitles.add(DBTableDefine.BASE_TYPE);
//		textTitles.add(DBTableDefine.BASE_DATABASE);
		
		columnTitles.add(DBTableDefine.COLUMN_STATUS);
		columnTitles.add(DBTableDefine.COLUMN_NAME);
//		columnTitles.add(DBTableDefine.COLUMN_IS_PRIMARY_KEY);
		columnTitles.add(DBTableDefine.COLUMN_NULL);
		columnTitles.add(DBTableDefine.COLUMN_DESCRIPTION);
//		columnTitles.add(DBTableDefine.COLUMN_MARK);
		
		indexTitles.add(DBTableDefine.INDEX_STATUS);
		indexTitles.add(DBTableDefine.INDEX_NAME);
		indexTitles.add(DBTableDefine.INDEX_UNIQUE);
		indexTitles.add(DBTableDefine.INDEX_CLUST);
		indexTitles.add(DBTableDefine.INDEX_COLUMNS);
		indexTitles.add(DBTableDefine.INDEX_MARK);
		
		keyTitles.add(DBTableDefine.KEY_DEFINE);
		keyTitles.add(DBTableDefine.KEY_NAME);
		keyTitles.add(DBTableDefine.KEY_TYPE);
		keyTitles.add(DBTableDefine.KEY_COLUMNS);
		keyTitles.add(DBTableDefine.KEY_FOREIGN_KEY_TABLE);
		keyTitles.add(DBTableDefine.KEY_FOREIGN_KEY_FIELD);
		
		tableType.put("U", 	0);
		tableType.put("T",  1);
		tableType.put("M", 	2);
	}
	public static final String DATABASE_MENU = "���ݱ�Ŀ¼";
	public static final String DATABASE_MODULE_INFO = "ģ����Ϣ";
	public static final String BASE_OBJECT_NUM = "�����";
	public static final String BASE_NAME = "����";
	public static final String BASE_CHINESE_NAME = "������";
	public static final String BASE_DESCRIPTION = "˵��";
	public static final String BASE_VERSION = "�汾��";
	public static final String BASE_TYPE = "������";
	public static final String VIEW_TYPE = "V";
	public static final String BASE_DATABASE = "�������ݿ�";
	public static final String BASE_CUS_DATABASE_PART = "�Ƿ��Զ�������";
	public static final String BASE_DATABASE_PART_COL = "������ֶ�";
	public static final String BASE_DATABASE_PART_NUM = "��������";
	public static final String BASE_DATABASE_PART_START_DATE = "������ʼ����";
	public static final String IS_HISTORY_TABLE = "������ʷ��";
	public static final String IS_DIR_TABLE = "���������";
	public static final String IS_CLEAR_TABLE = "���������";
	public final static String FIELD_DEFINE = "�ֶ�";
	public final static String INDEX_DEFINE = "����";
	public final static String KEY_DEFINE = "��Լ��";
	public final static String INDEX_DEFINE_UTIL = "�����ֶ�";
	public final static String MODIFY_DEFINE = "�޸ļ�¼";
	
	public final static String COLUMN_PRIMARY_KEY = "Y";
	public final static String COLUMN_NULL_ABLE_N = "N";
	public final static String COLUMN_NULL_ABLE_Y = "Y";
	public final static String COLUMN_STATUS = "�ֶ�";
	public final static String COLUMN_NAME = "�ֶ���";
	public final static String COLUMN_CHINESE_NAME = "������";
	public final static String COLUMN_TYPE = "�ֶ�����";
	public final static String COLUMN_NULL = "��ֵ";
	public final static String COLUMN_DESCRIPTION = "�ֶ�˵��";
	public final static String COLUMN_COMMENTS = "��ע";
	public final static String COLUMN_REMARK = "�ֶ�ע��";
	public final static String COLUMN_MARK = "���";
	public final static String COLUMN_IS_PRIMARY_KEY = "�Ƿ�����";
	public final static String COLUMN_DEFAULT_VALUE = "Ĭ��ֵ";
	
	public final static String INDEX_UNIQUE_Y = "Y";
	public final static String INDEX_CLUST_Y = "Y";
	public final static String INDEX_STATUS = "����";
	public final static String INDEX_MARK = "���";
	public final static String INDEX_NAME = "��������";
	public final static String INDEX_UNIQUE = "Ψһ";
	public final static String INDEX_CLUST = "�۴�";
	public final static String INDEX_COLUMNS = "�����ֶ�";
	
	public final static String KEY_MARK = "���";
	public final static String KEY_NAME = "����";
	public final static String KEY_TYPE = "����";
	public final static String KEY_COLUMNS = "�ֶ��б�";
	public final static String KEY_FOREIGN_KEY_TABLE = "������ձ�";
	public final static String KEY_FOREIGN_KEY_FIELD = "���������";
	
	public final static String VIEW_SQL_NAME = "��ͼ����";
	
	
	public final static String SEQUENCE_INC = "����";
	public final static String SEQUENCE_MIN = "��Сֵ";
	public final static String SEQUENCE_MAX = "���ֵ";
	public final static String SEQUENCE_START = "��ʼֵ";
	public final static String SEQUENCE_CYC = "�Ƿ�ѭ��";
	public final static String SEQUENCE_CACHE = "�Ƿ񻺴�";
	public final static String SEQUENCE_CACHE_SIZE = "�����С";
	public final static String SEQUENCE_DATABASE_NAME = "���ݿ��";
	
	/**
	 * ��ȡ������
	 * 
	 * @param key
	 * @return
	 */
	public static Integer getTableType (String key){
		return tableType.get(key) == null ? 0 : tableType.get(key);
	}
	
	/**
	 * �鿴����ı����Ƿ��Ǳ���������
	 * 
	 * @param title
	 * @return
	 */
	public static boolean isMainText (String title){
		if (textTitles.contains(title)) {
			return true;
		}
		return false;
	}
	
	/**
	 * �鿴����ı����Ƿ��Ǳ���������
	 * 
	 * @param title
	 * @return
	 */
	public static boolean isColumnMainTitle (String title){
		if (columnTitles.contains(title)) {
			return true;
		}
		return false;
	}
	
	/**
	 * �鿴����ı����Ƿ��Ǳ���������
	 * 
	 * @param title
	 * @return
	 */
	public static boolean isIndexMainTitle (String title){
		if (indexTitles.contains(title)) {
			return true;
		}
		return false;
	}
	
	/**
	 * �鿴����ı����Ƿ��Ǳ���������
	 * 
	 * @param title
	 * @return
	 */
	public static boolean isKeyMainTitle (String title){
		if (keyTitles.contains(title)) {
			return true;
		}
		return false;
	}
	
	public static boolean isExtendsText (String text){
		if (!textTitles.contains(text) && !StringUtils.equals(FIELD_DEFINE, text) && !StringUtils.equals(INDEX_DEFINE, text)) {
			return true;
		}
		return false;
	}
	
}
