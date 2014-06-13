/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.oracle.token;

import java.util.Map;

import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procedure.compiler.oracle.constant.IProcedureEngineContextConstantOracle;

/**
 * ���ɹ���ǰ�ô���
 * @author qinyuan
 *
 */
public class ProcedureBeginCodeToken implements ICodeToken {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#getContent()
	 */
	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#getType()
	 */
	@Override
	public int getType() {
		return CODE_TEXT;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#genCode(java.util.Map)
	 */
	@Override
	public String genCode(Map<Object, Object> context) throws Exception {
		StringBuffer result = new StringBuffer();
		
		Procedure procedure = (Procedure)context.get(IProcedureEngineContextConstantOracle.ResourceModel);
		result.append(NEWLINE);
		result.append(begin_code_desc_begin);
		result.append(procedure.getBeginCode());
		result.append(NEWLINE);
		result.append(begin_code_desc_end);
		result.append(NEWLINE);
		return result.toString();
	}
	
	private final static String begin_code_desc_begin = 
		"/*****************************************************/\r\n" +
		"/*   ǰ�ô��� --��ʼ                                              */\r\n" +
		"/*****************************************************/\r\n";

	private final static String begin_code_desc_end = 
		"/*****************************************************/\r\n" +
		"/*   ǰ�ô��� --����                                              */\r\n" +
		"/*****************************************************/\r\n";
}
