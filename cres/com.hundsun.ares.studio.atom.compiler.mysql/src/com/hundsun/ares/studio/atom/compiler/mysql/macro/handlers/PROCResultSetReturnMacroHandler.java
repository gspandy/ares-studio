/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.mysql.token.PROCResultSetReturnToken;

import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.ITokenDomain;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;

/**
 * @author zhuyf
 *
 */
public class PROCResultSetReturnMacroHandler implements IMacroTokenHandler {


	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.PROC_RESULTSET_RETURN_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*
		 * ����������
			[PRO*C���������]
			�������̣�
			1.����������䣺
			@result_num =  0;
			[�α����������ֶ�]
			while ( SQLCODE == OK_SUCCESS )
			'{'
			EXEC SQL FETCH cursor[�����]+[���] INTO: [�оٽ�����ֶβ����ֶ�������α��׺];
			if ( (SQLCODE == -28) || (SQLCODE == -1012) )
			lpConn->setErrMessage(HSDB_CONNECT_DISCONN,SQLCODE,sqlca.sqlerrm.sqlerrmc);    
			if ( SQLCODE == OK_SUCCESS)
			'{'
			@result_num = @result_num + 1;
			[�α����������ֶ�ֵ]
			'}'
			else
			break;
			'}'
			EXEC SQL CLOSE cursor[�����]+[���];
			2.����[�α����������ֶ�]Ϊ��Ԥ���PROC������ֶ��б��б����ֶΣ���ÿһ�ֶ����ɣ�
			lpOutPacker->AddField("[�ֶ���]");
			ע������������͵Ĳ���������Ҳ�������仯
			3.[�оٽ�����ֶβ����ֶ�������α��׺]Ϊ��Ԥ���PROC������ֶ��б��б����ֶΣ���ÿһ�ֶ����ɣ�
			[�ֶ���]_cur
			������ΪProc����������proc���������б���
			4.[�α����������ֶ�ֵ]Ϊ��Ԥ���PROC������ֶ��б��б����ֶΣ���ÿһ�ֶ����ɣ�
			lpOutPacker->Add[�ֶ�����]([�ֶ���]_cur); //[�ֶ�ע��]
		 */
		
		 addMacroNameToMacroList(token,context);//�Ѻ����ص������ݿ���Լ�proc���б���
		 addVarList(context);//�ѱ������뵽�����б���ȥ
		 List<ICodeToken> codeList = new ArrayList<ICodeToken>();
		 String rsId = getRsId(context);
		 List<String> sqlFields =this.getSqlFields(context);//ȡ���ֶ��б�
		 codeList.add(new PROCResultSetReturnToken(rsId,sqlFields) );//���CodeToken
		 removeDomain(context);//ɾ����
		return codeList.iterator();
	}
	
	
	
	/**
	 * �Ѻ������뵽���б���
	 */
	private void addMacroNameToMacroList(IMacroToken token,
			Map<Object, Object> context){
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_DATABASE_MACRO,token.getKeyword());//��ӵ����ݿ��б���
	    helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_MACRO,token.getKeyword());//��ӵ�proc���б���
	}
	
	/**
	 * ���ֶι���ӵ������б���ȥ
	 * 
	 * @param procVarList
	 */
	private void addVarList(Map<Object, Object> context) {
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
			//���뵽proc�����б��У�����proc����������proc����������
		List<String> varList = new ArrayList<String>(5);
		    varList.add("result_num");
			varList.add("error_no");
			varList.add("error_info");
			varList.add("error_sysinfo");
			varList.add("error_id");
		for(String varName:varList){
			popVarList.add(varName);
		}
			
	}
	
	/**
	 * @return ȡ���ֶ�����
	 */
	private List<String> getSqlFields(Map<Object, Object> context){
		ITokenDomain domain = getDomain(context);
		return (List<String>)domain.getArgs()[1];
	}
	
	private String getRsId(Map<Object, Object> context){
		ITokenDomain domain = getDomain(context);
		return (String)domain.getArgs()[0];
	}
	

	/**
	 * �����
	 */
	private ITokenDomain getDomain(Map<Object, Object> context){
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		return handler.getDomain(MacroConstant.PROC_RESULTSET_STATEMENT_MACRONAME);
	}
	
	/**
	 * ɾ����
	 */
	private void removeDomain(Map<Object, Object> context){
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		handler.removeDomain(MacroConstant.PROC_RESULTSET_STATEMENT_MACRONAME);
	}
	

}
