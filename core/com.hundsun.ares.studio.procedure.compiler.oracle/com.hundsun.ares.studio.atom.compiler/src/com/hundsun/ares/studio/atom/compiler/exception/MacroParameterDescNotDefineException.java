/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.exception;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.engin.exception.HSException;

/**
 * @author liaogc
 *
 */
public class MacroParameterDescNotDefineException extends HSException{
		


	private static final long serialVersionUID = 5049629016231976830L;
		private static final String MESSAGE = "[%1$s]�ĵ�%2$s������˵����Ϣû�����ã�����λ�ã�%3$s�еĵ�%4$s��";// �쳣��ʾ��Ϣ
		
		public MacroParameterDescNotDefineException(String macroName, int indexParam, String functionName, int rowNum) {
			super(String.format(MacroParameterDescNotDefineException.MESSAGE, macroName,StringUtils.EMPTY+indexParam,functionName,StringUtils.EMPTY+rowNum));
		}
}
