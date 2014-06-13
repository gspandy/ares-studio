/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.mysql.token;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpace;
import com.hundsun.ares.studio.jres.model.database.oracle.util.OracleDataBaseUtil;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procedure.compiler.mysql.constant.IProcedureEngineContextConstantMySQL;

/**
 * @author qinyuan
 *
 */
public class ProcedureCreateToken implements ICodeToken {

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
		
		Procedure procedure = (Procedure)context.get(IProcedureEngineContextConstantMySQL.ResourceModel);
		IARESProject project = (IARESProject)context.get(IProcedureEngineContextConstantMySQL.Aresproject);
		//�������������Ӧ���ݿ��û�����Ϊ�գ���Ҫ�����û���
		StringBuffer functionNameBuffer = new StringBuffer();
		String dbName = procedure.getDatabase();
		// �������ݿ�����ȡ���ݿ��û���
		TableSpace tableSpace = OracleDataBaseUtil.getTableSpaceByName(project, dbName);
		if(null != tableSpace) {
			String user = tableSpace.getUser();
			if(StringUtils.isNotBlank(user)){//�û���Ϊ��
				functionNameBuffer.append(user.trim());
				functionNameBuffer.append(".");
			}
		}
		functionNameBuffer.append(procedure.getName().trim());
		
		//����������������ڣ�����Ҫ����
		int inputParamNum = procedure.getInputParameters().size();//�����������
		int outputParamNum = procedure.getOutputParameters().size();//�����������
		if(inputParamNum <= 0 && outputParamNum <= 0 && !procedure.isOutputCollection()){//���������Ҫ�����α�
			if(StringUtils.equalsIgnoreCase(procedure.getBizType(), IProcedureEngineContextConstantMySQL.procedure)){
				return String.format(procedure_create_info, functionNameBuffer.toString());
			}else {
				return String.format(function_create_info, functionNameBuffer.toString());
			}
		}else {
			if(StringUtils.equalsIgnoreCase(procedure.getBizType(), IProcedureEngineContextConstantMySQL.procedure)){
				return String.format(procedure_create_info, functionNameBuffer.toString())+ "(" + NEWLINE;
			}else {
				return String.format(function_create_info, functionNameBuffer.toString())+ "(" + NEWLINE;
			}
		}
	}
	
	private static final String function_create_info = 
		//"-- �����MySQL�洢�������档" + NEWLINE +
		"prompt create function '%1$s' ..." + NEWLINE +
		"create or replace function %1$s" + NEWLINE ;
	
	//�������ͣ�֤ȯ������չʹ��
	private static final String procedure_create_info = 
		"prompt create procedure '%1$s' ..." + NEWLINE +
		"create or replace procedure %1$s" + NEWLINE ;
}
