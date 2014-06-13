/**
 * Դ�������ƣ�ITableIndexScriptWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.script.api.database;

import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;

/**
 * ���ݿ������
 * 
 * @author yanwj06282
 *
 */
public interface ITableIndexScriptWrap extends IScriptModelWrap{

	/**
	 * ��ȡ������sql
	 * 
	 * @param type ��������
	 * @param prefix
	 * @param isUser �Ƿ�������ݿ��û�ǰ׺
	 * @return
	 */
	public String getSql(String type ,String prefix , boolean isUser);
	
	/**
	 * ��ȡ���
	 * 
	 * @return
	 */
	public String getMark();
	
	/**
	 * �Ƿ�Ψһ
	 * 
	 * @return
	 */
	public boolean isUnique();
	
	/**
	 * �Ƿ�۴�
	 * 
	 * @return
	 */
	public boolean isCluster();
	
	/**
	 * �Ƿ�ת
	 * 
	 * @return
	 */
	public boolean isReverse();
	
	/**
	 * ��ȡ�����е�������
	 * 
	 * @return
	 */
	public ITableColScriptWrap[] getTableIndexColumns();
	
	public void trim();

	
}
