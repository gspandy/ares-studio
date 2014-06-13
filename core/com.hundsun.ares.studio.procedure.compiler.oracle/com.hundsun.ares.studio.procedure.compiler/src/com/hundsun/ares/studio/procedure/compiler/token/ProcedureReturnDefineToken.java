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
 * ���̷�����������
 * <br>Ŀǰ֤ȯһ��ǿ�Ʒ���number���ͣ���������ҲͳһΪ"as";
 * ֤ȯ�����������÷�����ע�⡣����������Է����������ͣ����߲����أ�
 * @author qinyuan
 */
public class ProcedureReturnDefineToken implements ICodeToken {

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
		// ���̷�����������
		StringBuffer result = new StringBuffer();
		
		//����������������ڣ�����Ҫ����
		int inputParamNum = procedure.getInputParameters().size();//�����������
		int outputParamNum = procedure.getOutputParameters().size();//�����������
		if(inputParamNum <= 0 && outputParamNum <= 0 && !procedure.isOutputCollection()){//���������Ҫ�����α�
		}else {
			result.append(")" + NEWLINE);
		}
		result.append(NEWLINE);
		
		if(StringUtils.isBlank(procedure.getBizType()) ||
				StringUtils.equalsIgnoreCase(procedure.getBizType(), IProcedureEngineContextConstant.function)){
			result.append("return ");
			result.append(StringUtils.isBlank(procedure.getReturnType())?"number":procedure.getReturnType());
			result.append(NEWLINE);
		}

		//�������ʹ���
		if(StringUtils.isBlank(procedure.getDefineType()) ||
				StringUtils.equalsIgnoreCase(procedure.getDefineType(), as)){
			result.append(as);
		}else {
			result.append(PIPELINED_IS);
		}
		result.append(NEWLINE);
		//��������
		if(procedure.isAutoTransaction()) {
			result.append(AUTO_TRANSCTION);
			result.append(NEWLINE);
		}
		
		return result.toString();
	}

	//�������ͣ�֤ȯ������չ��Ҫʹ��
	private static final String as = "as";
	private static final String PIPELINED_IS = "PIPELINED IS";
	
	private static final String AUTO_TRANSCTION = "pragmaautonomous_transaction;  -- ��������";
}
