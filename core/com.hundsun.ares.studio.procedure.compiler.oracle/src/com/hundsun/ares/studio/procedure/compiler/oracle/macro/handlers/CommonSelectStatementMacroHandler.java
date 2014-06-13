/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.oracle.macro.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.parser.PseudoCodeParser;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procedure.compiler.oracle.constant.IProcedureEngineContextConstantOracle;
import com.hundsun.ares.studio.procedure.compiler.oracle.macro.MacroConstant;
import com.hundsun.ares.studio.procedure.compiler.oracle.token.CommonSelectStatementToken;

/**
 * @author zhuyf
 *
 */
public class CommonSelectStatementMacroHandler implements IMacroTokenHandler {
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return "";//MacroConstant.COMMON_SELECT_STATEMENT_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*
		 * ����������
		[ͨ��SELECT][select ���]
		�������̣�
		��α����༭��������[ͨ��SELECT][select ���]���������Ԥ��tabҳ���鿴��Ӧ����ʵ���롣
		����������м���һ���µĽ��������������Ϊ����ż��Ͻ�������еĳ��ȡ�
		��������sQueryText������Ϊchar sQueryText[4096]��Ĭ��ֵΪ{0}��
		���[select ���]��ȡ�õ�ֵΪ@���ſ�ͷ�ģ���˵������һ��������ֱ��д��lpConn->executeQuery()�У�������ǣ���˵�����ǹ̶����ַ�����������ַ���û�м����ţ��Զ�Ϊ����ϣ��������ַ�����ֵ������sQueryText��Ȼ���������lpConn->executeQuery(sQueryText)��

		 */
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IProcedureEngineContextConstantOracle.SKELETON_ATTRIBUTE_HELPER);
		Set<String> rsList = helper.getAttribute(IProcedureEngineContextConstantOracle.ATTR_RESULTSET_LIST);
		Procedure func = (Procedure)context.get(IProcedureEngineContextConstantOracle.ResourceModel);
		int rsTotalSizeAdd1 = rsList.size() + 1;
		String rsID = func.getObjectId() + rsTotalSizeAdd1;
		//����������ؽ���������÷��ؽ������AF�Լ�[ͨ��SELECT]������ֱ�Ӵ�����
		helper.addAttribute(IProcedureEngineContextConstantOracle.ATTR_FUNC_RESULTSET,rsID);
		helper.addAttribute(IProcedureEngineContextConstantOracle.ATTR_RESULTSET_LIST,rsID);//�����ͷŵĽ�����б�
		helper.addAttribute(IProcedureEngineContextConstantOracle.ATTR_GETLAST_RESULTSET,rsID);//ȡ�þͽ��Ľ����Id�����ǿ�����lpResultSet->ȡ�ͽ�������ģ�����Ҫ�ӵ�����б���
		//[ͨ��SELECT]Ϊ���ݿ��
		helper.addAttribute(IProcedureEngineContextConstantOracle.ATTR_DATABASE_MACRO,getKey());
		
		List<ICodeToken> codeTokens= new ArrayList<ICodeToken>();
		String resultSetVarName = "lpResultSet"+func.getObjectId() + rsTotalSizeAdd1;
		String sql =PseudoCodeParser.insertCommonForSql(token.getParameters()[0], func.getObjectId());
		codeTokens.add(new CommonSelectStatementToken(resultSetVarName,sql));//���ͨ��select����Token
		return codeTokens.iterator();
	}
	
	
	

}
