/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.api.model;



/**
 * @author lvgao
 *
 */
public interface IScriptModelWrap {

	/**
	 * ��ȡ����
	 * @return
	 */
	public String getType();
	
	/**
	 * ��ȡ����
	 * @return
	 */
	public String getName();
	
	/**
	 * ��ȡ��չ��Ϣ
	 * 
	 * @param key
	 * @return
	 */
	public String getExtendsValue(String key);
	
	/**
	 * �����û���չ
	 * 
	 * @param key
	 * @param value
	 */
	public void setExtendsValue(String key , String value);
	
}
