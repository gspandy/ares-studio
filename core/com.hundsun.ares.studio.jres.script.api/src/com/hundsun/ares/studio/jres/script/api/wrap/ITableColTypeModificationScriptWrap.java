/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableColTypeModifyDetailScriptWrap;

/**
 * �޶���¼�ֶ�����
 * 
 * @author yanwj06282
 *
 */
public interface ITableColTypeModificationScriptWrap extends IModificationScriptWrap{

	/**
	 * ��ȡ�޸ı��ֶ����͵���ϸ��¼
	 * 
	 * @return
	 */
	public ITableColTypeModifyDetailScriptWrap[] getDetails();
	
}
