/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util;

/**
 * @author lvgao
 *
 */
public interface IScriptSysUtil {

	public static final String KEY_USER_NAME = "user.name";
	
	/**
	 * ��ȡ��ѡ��
	 * @param id
	 * @return
	 */
	public Object getConfig(String id);
	
	
	/**
	 * ��ȡ����
	 * @param id
	 * @return
	 */
	public Object get(String id);
	
}
