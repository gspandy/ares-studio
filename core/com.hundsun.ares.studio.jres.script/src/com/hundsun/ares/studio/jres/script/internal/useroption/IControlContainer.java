/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.internal.useroption;

/**
 * @author lvgao
 *
 */
public interface IControlContainer {

	public  void addChildren(IControl control);
	
	/**
	 * ��ȡ�ӿؼ�
	 * @return
	 */
	public IControl[]  getChildren();
}
