/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.exception;

import com.hundsun.ares.studio.engin.exception.HSException;

/**
 * @author liaogc
 *
 */
public class TableFieldNotSettingException extends HSException{
		

	
	private static final long serialVersionUID = 4001788659826605526L;
		private static final String MESSAGE = "��%1$s��û�������ֶ�";// �쳣��ʾ��Ϣ
		
		public TableFieldNotSettingException(String message) {
			super(String.format(MESSAGE, message));
		}
}
