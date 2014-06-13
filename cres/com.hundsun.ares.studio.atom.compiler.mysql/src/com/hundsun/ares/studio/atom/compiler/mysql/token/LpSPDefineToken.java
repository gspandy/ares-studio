/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.token;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.procedure.IProcedureMacroTokenService;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.procdure.Procedure;

/**
 * @author zhuyf
 *
 */
public class LpSPDefineToken implements ICodeToken {
	
	private final static String lpSP_DEFINE_STR = "ICallableStatement * lpSP = NULL;" + ITokenConstant.NL;

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
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#genCode(java.util.Map)
	 */
	@Override
	public String genCode(Map<Object, Object> context) throws Exception {
		/*
		 * ֻ���ڴ��ڴ洢���̵��ã��Ҵ洢���̷���Ϊ�����ʱ������Ҫ����lpSP������������䣺
		 * ICallableStatement * lpSP = NULL;
		 */
		StringBuffer code = new StringBuffer();
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		Set<String> statementList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_STATEMENT_LIST);
		Iterator<String> ite = statementList.iterator();
		while(ite.hasNext()){
			String rsId = ite.next();
			code.append("ICallableStatement * lpSP" + rsId + " = NULL;\r\n");
		}
		return code.toString();
	}

}
