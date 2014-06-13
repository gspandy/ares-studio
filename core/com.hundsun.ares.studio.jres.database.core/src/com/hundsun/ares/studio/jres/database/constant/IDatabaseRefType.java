/**
 * Դ�������ƣ�IDatabaseRefType.java
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
public interface IDatabaseRefType {
	/**
	 * ���ݿ��
	 */
	public static final String Table = "jres.db.table";
	
	/**
	 * ���ݿ���ֶ�
	 */
	public static final String TableField = "jres.db.table.field";
	
	/**
	 * ���ݿ������
	 */
	public static final String TableIndex = "jres.db.table.index";
	
	/**
	 * ���ݿ���ͼ
	 */
	public static final String View = "jres.db.view";
	
	String Sequence = "jres_osequence";
	String Trigger = "jres_otrigger";
}
