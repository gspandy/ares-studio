/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.oracle.token;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procedure.compiler.oracle.constant.IProcedureEngineContextConstantOracle;

/**
 * @author qinyuan
 *
 */
public class ProcedureAnnotationToken implements ICodeToken {

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
		Procedure procedure = (Procedure)context.get(IProcedureEngineContextConstantOracle.ResourceModel);
		
		//2014��5��13��13:47:37 ֤ȯ������Ҫʹ�ù������ƣ���ԭ�����������߱���һ�£����Թ�������ֻ��֤ȯ������չ����չʾ
		String procName = StringUtils.isNotBlank(procedure.getProcName())?procedure.getProcName():procedure.getName();
		
		return String.format(annotation_info, procName,procedure.getDescription(),procedure.getObjectId());
	}
	
	/**
	 * ע��ͷ��Ϣ
	 */
	private static final String annotation_info = 
		"/****************************************************************************************************/" +NEWLINE+
		"/* ��          ��: %s                                                                                */" +NEWLINE+
		"/* ��          ��: %s                                                                                */" +NEWLINE+
		"/* ��  ��  ��  ��: %s                                                                                   */" +NEWLINE+
		"/* ��          ��:                                                                                    */" +NEWLINE+
		"/* ���ú��������:                                                                                     */" +NEWLINE+
		"/*                                                                                                  */" +NEWLINE+
		"/****************************************************************************************************/" + NEWLINE;

}
