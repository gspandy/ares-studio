/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableColScriptWrap;
import com.hundsun.ares.studio.jres.script.api.database.ITableIndexScriptWrap;

/**
 * �޶���¼��װ�������ӱ�
 * 
 * @author yanwj06282
 *
 */
public interface IAddTableModificationScriptWrap extends
		IModificationScriptWrap {

	/**
	 * ������ʷ��
	 * 
	 * @return
	 */
	public boolean isGenHisTable();
	
	/**
	 * ����ԭ��
	 * 
	 * @return
	 */
	public boolean isGenTable();
	
	/**
	 * ��ȡ�����
	 * 
	 * @return
	 */
	public ITableColScriptWrap[] getTableColumns();
	
	/**
	 * ��ȡ������
	 * 
	 * @return
	 */
	public ITableIndexScriptWrap[] getTableIndexes();
	
}
