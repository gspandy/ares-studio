/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util;


/**
 * @author lvgao
 *
 */
public interface IScriptOutUtil {

	/**
	 * ����̨���
	 * @param message
	 */
	public void info(String message);
	
	/**
	 * ������Ϣ���
	 * @param message
	 */
	public void warn(String message);
	
	/**
	 * �Ի������
	 * @param message
	 */
	public void dialog(String message);
	
	/**
	 * �쳣�Ի������
	 * 
	 * @param message
	 */
	public void dialogError(String message);
	
}
