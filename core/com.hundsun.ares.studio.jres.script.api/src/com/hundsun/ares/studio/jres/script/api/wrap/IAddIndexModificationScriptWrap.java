/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableIndexScriptWrap;

/**
 * ���ӱ�����
 * 
 * @author yanwj06282
 *
 */
public interface IAddIndexModificationScriptWrap extends IModificationScriptWrap{

	/**
	 * ��ȡ���ӱ���������ϸ��¼
	 * 
	 * @return
	 */
	public ITableIndexScriptWrap[] getDetails();
	
}
