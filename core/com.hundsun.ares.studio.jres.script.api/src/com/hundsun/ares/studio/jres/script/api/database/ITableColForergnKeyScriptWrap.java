/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.database;

import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;

/**
 * 
 * ���ݿ���ֶ����
 * 
 * @author yanwj06282
 *
 */
public interface ITableColForergnKeyScriptWrap extends IScriptModelWrap{

	/**
	 * ���ֶ�������Ӧ�༭���ġ���������
	 * 
	 * @return
	 */
	public String getTableName();
	
	/**
	 * �ֶ�������,��Ӧ�༭���ġ��ֶ�����
	 * 
	 * @return
	 */
	public String getFieldName();
	
}
