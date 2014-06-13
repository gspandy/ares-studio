/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.token;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procedure.compiler.constant.IProcedureEngineContextConstant;

/**
 * ���̷���ֵ����
 * <br>֤ȯһ��Ĭ�Ϸ���0��֤ȯ��������������ʽ��
 * ���粻���صȣ���ע��
 * @author qinyuan
 *
 */
public class ProcedureReturnValueToken implements ICodeToken {

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
		Procedure procedure = (Procedure)context.get(IProcedureEngineContextConstant.ResourceModel);
		
		//����֤ȯ���������ֻ�ڶ���Ϊfunction,���ҷ�������Ϊnumberʱ������return(0)
		if(StringUtils.equalsIgnoreCase(procedure.getBizType(), IProcedureEngineContextConstant.function) &&
				StringUtils.equalsIgnoreCase(procedure.getReturnType(), IProcedureEngineContextConstant.return_type)){
			return proc_return_value;
		}else {
			return "";
		}
						
	}
	
	private static final String proc_return_value = NEWLINE + "return(0);" + NEWLINE;

}
