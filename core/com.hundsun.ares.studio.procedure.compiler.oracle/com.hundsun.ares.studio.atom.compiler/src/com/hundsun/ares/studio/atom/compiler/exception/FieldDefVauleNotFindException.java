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
public class FieldDefVauleNotFindException extends HSException {

	private static final long serialVersionUID = 4148689458686019952L;
	private static final String MESSAGE = "��׼�ֶΣ�{%1$s}��Ĭ��ֵ�޷��õ�";// �쳣��ʾ��Ϣ

	public FieldDefVauleNotFindException(String fieldName) {
		super(String.format(MESSAGE, fieldName));
	}

}
