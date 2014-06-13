/**
 * Դ�������ƣ�ISequenceScriptWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.script.api.database;

import com.hundsun.ares.studio.jres.script.api.wrap.IDatabaseResScriptWrap;

/**
 * ���з�װ����
 * 
 * @author yanwj06282
 *
 */
public interface ISequenceScriptWrap extends IDatabaseResScriptWrap {

	/**
	 * ������
	 * 
	 * @return
	 */
	public String getSeqName();
	
	/**
	 * �Ƿ������ʷ��
	 * 
	 * @return
	 */
	public boolean isGenHisTable();
	
	/**
	 * ��ñ�ռ�
	 * 
	 * @return
	 */
	public String getTableSpace();
	
	/**
	 * �Ƿ�ѭ��
	 * 
	 * @return
	 */
	public boolean isCycle();
	
	/**
	 * �Ƿ��û�����
	 * 
	 * @return
	 */
	public boolean isUseCache();
	
	/**
	 * ����
	 * 
	 * @return
	 */
	public String getCache();
	
	/**
	 * ��ʼ
	 * @return
	 */
	public String getStart();
	
	/**
	 * ����
	 * @return
	 */
	public String getTableName();
	
	/**
	 * ����
	 * @return
	 */
	public String getIncrement();
	
	/**
	 * ���ֵ
	 * @return
	 */
	public String getMaxValue();
	
	/**
	 * ��ʼֵ
	 * @return
	 */
	public String getMinValue();
	
}
