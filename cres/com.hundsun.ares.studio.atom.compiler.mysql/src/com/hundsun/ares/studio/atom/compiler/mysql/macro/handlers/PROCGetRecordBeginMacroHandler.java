/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.TokenDomain;
import com.hundsun.ares.studio.atom.compiler.mysql.token.PROCGetRecordBeginToken;
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
public class PROCGetRecordBeginMacroHandler implements IMacroTokenHandler {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.PROC_GET_RECORD_BEGIN_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*
		 * ����������
			[PRO*C��¼��ȡ��ʼ][Ҫ����ֶ��б�]
			�������̣�
			1.��ȡ�ֶ��б����뵽PRO*C������
			2.����������䣺
			{
			EXEC SQL FETCH cursor[�����]+[���] INTO [�ֶ��б�@ת��Ϊproc����];
			 if ( (SQLCODE == -28) || (SQLCODE == -1012) )
			lpConn->setErrMessage(HSDB_CONNECT_DISCONN,SQLCODE,sqlca.sqlerrm.sqlerrmc);
			 if (SQLCODE == OK_SUCCESS)
			 {
			   do
			   {

		 */
		List<ICodeToken> codeToken = new ArrayList<ICodeToken>(1);
		addProcVarLis( token,context);//����SQL����е�@���������뵽PRO*C���������б���
		//ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		String lastId = getRsId(context);
		List<String> sqlFields =this.getSqlFields(context);//ȡ���ֶ��б�
		codeToken.add(new PROCGetRecordBeginToken(token,context,lastId,sqlFields));//����codeToken
	   addDomain(context);//�����
	   removeDomain(context);
	  return codeToken.iterator();//���ش˺�codeToken�б�
	}
	
	/**
	 * ����SQL����е�@���������뵽PRO*C���������б���
	 * @param procVarList
	 */
	private void addProcVarLis(IMacroToken token,Map<Object, Object> context ){
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		Pattern p = Pattern.compile("@[\\w\\d_]+");
		//sql����ڵ�һ������
		Matcher m = p.matcher(token.getParameters()[0]);
		while (m.find()) {
			popVarList.add(m.group().substring(1));
		}
		
	}

	/**
	 * �����
	 */
	private void addDomain(Map<Object, Object> context){
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		Object[] args = new Object[2];
		args[0] = getRsId(context);//��ӽ����Id��������������ػ�PRO*C��¼��ȡ��ʼ��PRO*C��¼��ȡ���������ɴ���ʱ��Ҫʹ�á�
		args[1] = getSqlFields(context);//�α��ֶ�����
		handler.addDomain(new TokenDomain(getKey(),args));
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
