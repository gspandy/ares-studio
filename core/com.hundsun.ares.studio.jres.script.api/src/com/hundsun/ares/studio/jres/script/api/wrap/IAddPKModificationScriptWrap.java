/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableColScriptWrap;

/**
 * ���ӱ�����
 * 
 * @author yanwj06282
 *
 */
public interface IAddPKModificationScriptWrap extends IModificationScriptWrap{

	/**
	 * ��ȡ���ӱ���������ϸ��¼
	 * 
	 * @return
	 */
	public ITableColScriptWrap[] getDetails();
	
}
