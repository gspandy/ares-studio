/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableColScriptWrap;

/**
 * �޸�ΨһԼ��
 * 
 * @author yanwj06282
 *
 */
public interface ITableColUniqueModificationSctiptWrap extends IModificationScriptWrap{

	/**
	 * ��ȡ�޸�ΨһԼ������ϸ��¼
	 * 
	 * @return
	 */
	public ITableColScriptWrap[] getDetails();
	
}
