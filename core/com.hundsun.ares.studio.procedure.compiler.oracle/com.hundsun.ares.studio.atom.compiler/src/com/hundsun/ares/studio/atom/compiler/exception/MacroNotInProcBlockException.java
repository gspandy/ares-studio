/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.exception;

import com.hundsun.ares.studio.engin.exception.HSException;

/**
 * @author liaogc
 *
 */
public class MacroNotInProcBlockException extends HSException{
		

	private static final long serialVersionUID = 788181414949547427L;
		private static final String MESSAGE = "������PROC������ʹ�ú�%1$s";// �쳣��ʾ��Ϣ
		
		public MacroNotInProcBlockException(String message) {
			super(String.format(MESSAGE, message));
		}

}
