/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.exception;

import com.hundsun.ares.studio.engin.exception.HSException;

/**
 * ����Դû���ҵ��׳����쳣
 * 
 * @author liaogc
 *
 */
public class TableNotFoundException extends HSException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1638288612798107990L;

	private static final String MESSAGE = "����Դ��%1$s������";// �쳣��ʾ��Ϣ


	/**
	 * @param tableName
	 */
	public TableNotFoundException( String tableName) {
		super(String.format(TableNotFoundException.MESSAGE, tableName));
		
	}

	

	

}

