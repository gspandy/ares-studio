/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableColScriptWrap;

/**
 * �޸ı��ֶ�����Ϊ��
 * 
 * @author yanwj06282
 *
 */
public interface ITableColNullableModificationScriptWrap extends IModificationScriptWrap{

	/**
	 * ��ȡ�޸ı��ֶ�����Ϊ�յ���ϸ��¼
	 * 
	 * @return
	 */
	public ITableColScriptWrap[] getDetails();
	
}
