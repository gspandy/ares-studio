/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.metadata;

import com.hundsun.ares.studio.jres.script.api.wrap.IMetadataItemScriptWrap;

/**
 * �˵��빦����ϸ����
 * 
 * @author yanwj06282
 *
 */
public interface IMenuItemScriptWrap extends IMetadataItemScriptWrap{

	/**
	 * ��ȡ�˵�����
	 * 
	 * @return
	 */
	public String getMenuTitle();
	
	/**
	 * ��ȡ�˵���
	 * 
	 * @return
	 */
	public String getMenuId();
	
	/**
	 * ��ȡҳ��
	 * 
	 * @return
	 */
	public String getUrl();
	
	/**
	 * ��ø���ϸ�����е��ӽ��������
	 * 
	 * @return
	 */
	public IFunctionProxyScriptWrap[] getFunctionProxys();
	
	/**
	 * �����������Ŀ����
	 * 
	 * @return
	 */
	public IMenuItemScriptWrap[] getSubItems();
	
	/**
	 * ����������
	 * 
	 * @param chineseName
	 */
	public void setMenuTitle(String menuTitle);
	
	/**
	 * ����name
	 * 
	 * @param name
	 */
	public void setMenuId(String menuId);
	
}
