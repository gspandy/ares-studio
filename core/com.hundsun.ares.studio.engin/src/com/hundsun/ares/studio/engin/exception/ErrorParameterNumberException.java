/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.engin.exception;

import java.text.MessageFormat;

/**
 * ��������������׳����쳣
 * 
 * @author zhuyf
 * 
 */
public class ErrorParameterNumberException extends HSException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5376473981367851674L;

	private static final String WHATSAYWHENEXCEPTION = "[{0}]ȱ�ٵ�{1}��������{2}������λ�ã�{3}�е�{4}��";// �쳣�׳���ʾ��Ϣ

	public ErrorParameterNumberException(String macroName, int indexParam, String paramDesc, String functionName, int rowNum) {
		super(MessageFormat.format(ErrorParameterNumberException.WHATSAYWHENEXCEPTION, new String[] { macroName,
				new Integer(indexParam).toString(), paramDesc, functionName, new Integer(rowNum + 1).toString() }));
	}
	
	public ErrorParameterNumberException(String macroName, String para, int indexParam, String paramDesc) {
		super(MessageFormat.format(ErrorParameterNumberException.ERROR_MSG,new String[] {macroName,
				new Integer(indexParam).toString(),para,paramDesc}));
	}

	private static final String ERROR_MSG = "[{0}]��{1}������[{2}]������󡣸�ʽӦΪ{3}";// �쳣�׳���ʾ��Ϣ
	

}
