/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.util;

/**
 * ��Ϊϵͳ�ļ�����
 * 
 * @author gongyf
 *
 */
public class Clipboard {
	
	final public static Clipboard instance = new Clipboard();
	private Object data;
	
	private Clipboard() {
		
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
	
	
	
}
