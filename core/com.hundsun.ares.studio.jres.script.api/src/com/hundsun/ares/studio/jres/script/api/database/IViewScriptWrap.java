/**
 * Դ�������ƣ�ITableScriptWrap.java
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
 * ���ݿ���ͼ
 * 
 * @author yanwj06282
 *
 */
public interface IViewScriptWrap  extends IDatabaseResScriptWrap{

	/**
	 * ��ȡ��ͼ��
	 * 
	 * @return
	 */
	public String getViewName();
	
	/**
	 * �Ƿ�������ʷ��
	 * 
	 * @return
	 */
	public boolean isGenHisTable();
	
	/**
	 * ��������
	 * @return
	 */
	public String getTableType();
	
	/**
	 * 	��ñ�ռ�
	 * @param prefix
	 * @return
	 */
	public String getTableSpace(String  prefix);
	
	
	/**
	 * ���sql���
	 * @param prefix
	 * @return
	 */
	public String getSql();
	
}
