/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.mysql.token;

import java.util.Map;

import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procedure.compiler.mysql.constant.IProcedureEngineContextConstantMySQL;

/**
 * ���̳�����
 * @author qinyuan
 *
 */
public class ProcedureExceptionHandlerToken implements ICodeToken {

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
		// TODO �ο������������ߺ꡾EXCEPTIONOTHERS��
		
		/*
		 * exception
  			when others then
		  [EXCEPTIONOTHERS]
		 */
		Procedure procedure = (Procedure) context.get(IProcedureEngineContextConstantMySQL.ResourceModel);
		StringBuffer exceptionCode = new StringBuffer();
		//��������н�������� ���Ҫ���α걨����
		exceptionCode.append("exception\r\n");
		exceptionCode.append("when others then\r\n");
		if (procedure.isOutputCollection()) {

			//��������������� ���ҪROLLBACK
			exceptionCode.append("rollback;\r\n");			

			exceptionCode.append("@error_no   := 101;\r\n");
			exceptionCode.append("@error_info :='ִ�д洢���̴���'||'"+procedure.getName()+"';\r\n");
			exceptionCode.append("@error_id := SQLCODE;\r\n");
			exceptionCode.append("@error_sysinfo := SQLERRM;\r\n");
			exceptionCode.append("open @cursor for\r\n");
			exceptionCode.append("select @error_pathinfo as error_pathinfo,@error_no as error_no, @error_info as error_info,@error_id as error_id, @error_sysinfo as error_sysinfo\r\n");
			exceptionCode.append("from dual;\r\n");
		} else {

			exceptionCode.append("rollback;\r\n");			

			exceptionCode.append("@error_no   := 101;\r\n");
			exceptionCode.append("@error_info :='ִ�д洢���̴���'||'"+procedure.getName()+"';\r\n");
			exceptionCode.append("@error_id := SQLCODE;\r\n");
			exceptionCode.append("@error_sysinfo := SQLERRM;\r\n");
		}
		
		exceptionCode.append("return(@error_no);" + NEWLINE);
		return exceptionCode.toString();
	}

}
