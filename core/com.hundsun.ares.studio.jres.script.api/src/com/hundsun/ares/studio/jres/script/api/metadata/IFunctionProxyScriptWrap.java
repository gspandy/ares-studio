/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.metadata;

import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;

/**
 * �˵��빦�ܹ��ܶ���
 * 
 * @author yanwj06282
 *
 */
public interface IFunctionProxyScriptWrap extends IScriptModelWrap{

	/**
	 * ��ȡ���ܶ���
	 * 
	 * @return
	 */
	public IMenuFunctionScriptWrap getFunctions();
	
	/**
	 * �ӽ�����(���ܱ��)
	 * 
	 * @return
	 */
	public String getFunCode();
	
	/**
	 * ������(�˵���)
	 * 
	 * @return
	 */
	public String getMenuId();
	
}
