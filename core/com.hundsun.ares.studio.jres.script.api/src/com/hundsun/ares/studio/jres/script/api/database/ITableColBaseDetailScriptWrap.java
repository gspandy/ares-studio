/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.database;

import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;

/**
 * �޶���¼����ϸ���ݵķ�װ������ϸ���ݰ����޶���¼�е�ɾ���У�ɾ���������޸ı��ֶ����͵�
 * 
 * @author yanwj06282
 *
 */
public interface ITableColBaseDetailScriptWrap extends IScriptModelWrap{

	/**
	 * ��ȡ����
	 * 
	 */
	public String getName();
	
	/**
	 * ��ȡ���
	 * 
	 * @return
	 */
	public String getMark();
	
}
