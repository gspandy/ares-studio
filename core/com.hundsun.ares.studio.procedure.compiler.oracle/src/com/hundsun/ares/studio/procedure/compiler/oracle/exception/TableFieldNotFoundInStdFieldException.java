/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.oracle.exception;

import com.hundsun.ares.studio.engin.exception.HSException;

/**
 * @author liaogc
 *
 */
public class TableFieldNotFoundInStdFieldException extends HSException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7508646265450259951L;
	
	private static final String MESSAGE = "�����ñ�%1$s�е�%2$s�ֶζ�Ӧ�ı�׼�ֶ�";// �쳣��ʾ��Ϣ
	
	public TableFieldNotFoundInStdFieldException(String tableName,String field) {
		super(String.format(MESSAGE, tableName,field));
	}


	

}
