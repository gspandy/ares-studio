/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.engin.exception;

import java.text.MessageFormat;

/**
 * �׳�����˵����Ϣû�����õ��쳣
 * 
 * @author zhuyf
 * 
 */
public class MacroParameterDescNotDefineException extends HSException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9005560618098957090L;

	private static final String WHATSAYWHENEXCEPTION = "[{0}]�ĵ�{1}������˵����Ϣû�����ã�����λ�ã�{2}�еĵ�{3}��";// �쳣��ʾ��Ϣ

	public MacroParameterDescNotDefineException(String macroName, int indexParam, String functionName, int rowNum) {
		super(MessageFormat.format(MacroParameterDescNotDefineException.WHATSAYWHENEXCEPTION, new String[] { macroName,
				new Integer(indexParam).toString(), functionName, new Integer(rowNum).toString() }));
	}

}
