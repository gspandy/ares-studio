/**
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.exception;

import com.hundsun.ares.studio.engin.exception.HSException;

/**
 * @author zhuyf
 *
 */
public class MacroParameterErrorException extends HSException {

	
	private static final String MESSAGE = "��[%1$s]�У�SQL����[%2$s]������ԭ�������[%3$s]��";
	/**
	 * @param errorInfo
	 */
	public MacroParameterErrorException(String macroName,String SQL ,String reason) {
		super(String.format(MESSAGE, macroName,SQL,reason));
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8637123673513891276L;
	
	

}
