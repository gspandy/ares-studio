/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.macro.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.token.ProcedureCallNoResultSetInProcStaticToken;
import com.hundsun.ares.studio.atom.compiler.token.ProcedureCallNoResultSetStaticToken;
import com.hundsun.ares.studio.atom.compiler.token.ProcedureCallStaticToken;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.ITokenDomain;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.procdure.Procedure;

/**
 * @author zhuyf
 *
 */
public class ProcedureCallMacroHandler implements IMacroTokenHandler {
	
	String key;
	
	IARESResource resource;
	
	public ProcedureCallMacroHandler(String key,IARESResource resource){
		this.key = key;
		this.resource = resource;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return this.key;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*
		 * �洢���̵��ã���������������
		 * 1.���÷��ؽ�����Ĵ洢���̣�lpSP->prepare��ʽ����
		 * 2.�����ؽ������EXEC SQL EXECUTE��ʽ����
		 * 3.����APʱ�����������������  *-1�����������ɴ���
			���磺��AF�����[AP_�˻���ܹ���_����ʽ������³���][occur_balance=@occur_balance*-1,current_balance=@occur_balance*-1]
		 */
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
//		Set<String> prcCallList = helper.getAttribute(IAtomEngineContextConstant.ATTR_PROCEDURE_CALL);
//		prcCallList.add(getKey());//����ǰ�洢���̵�������ӵ��洢���̵����б��У��Ա������������봦��
//		Set<String> databaseMacroList = helper.getAttribute(IAtomEngineContextConstant.ATTR_DATABASE_MACRO);
//		databaseMacroList.add(token.getKeyword());//����ǰ�洢���̵�������ӵ����ݿ������б��У��Ա������������봦��
		
		helper.addAttribute(IAtomEngineContextConstant.ATTR_DATABASE_MACRO, token.getKeyword());
		
		List<ICodeToken> codeList = new ArrayList<ICodeToken>();
		Procedure procedure = resource.getInfo(Procedure.class);
		if(procedure.isOutputCollection()){
			Set<String> rsList = helper.getAttribute(IAtomEngineContextConstant.ATTR_RESULTSET_LIST);
			AtomFunction func = (AtomFunction)context.get(IAtomEngineContextConstant.ResourceModel);
			int rsTotalSizeAdd1 = rsList.size() + 1;
			String rsID = func.getObjectId()+ procedure.getObjectId() + rsTotalSizeAdd1;
			helper.addAttribute(IAtomEngineContextConstant.ATTR_RESULTSET_LIST, rsID);
			//2014��4��14��13:45:16 ��Ԫ  �������������N��־���򲻽��н�����滻
			if(!StringUtils.contains(token.getFlag(), "N")){
				helper.addAttribute(IAtomEngineContextConstant.ATTR_GETLAST_RESULTSET,rsID);
			}
			codeList.add(new ProcedureCallStaticToken(procedure,token,rsID));
			if(StringUtils.isBlank(procedure.getObjectId())){
				fireEventLessFunctionId(context);
			}
		}else if(isInProcBlock(context)){
			codeList.add(new ProcedureCallNoResultSetInProcStaticToken(procedure,token));	
		}else{
			codeList.add(new ProcedureCallNoResultSetStaticToken(procedure,token));
		}
		if(procedure.isOutputCollection()){
			helper.addAttribute(IAtomEngineContextConstant.ATTR_PROCEDURE_CALL_RSRETURN, getKey());//������ؽ����������뵽���ؽ�����Ĺ��̵����б���
		}else{
			for(Parameter input : procedure.getInputParameters()){
				String procVarName = input.getId();
				helper.addAttribute(IAtomEngineContextConstant.ATTR_PROC_VARIABLE_LIST,procVarName);
			}
			for(Parameter input : procedure.getOutputParameters()){
				String procVarName = input.getId();
				helper.addAttribute(IAtomEngineContextConstant.ATTR_PROC_VARIABLE_LIST,procVarName);
			}
			helper.addAttribute(IAtomEngineContextConstant.ATTR_PROC_VARIABLE_LIST,"error_pathinfo");
			helper.addAttribute(IAtomEngineContextConstant.ATTR_PROC_VARIABLE_LIST,"error_pathinfo_tmp");
			helper.addAttribute(IAtomEngineContextConstant.ATTR_PROCEDURE_CALL_NOTRSRETURN, getKey());//��������ؽ���ӣ����뵽�Ƿ�����
		}
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		//����������ӵ���ͨ�б���
		for(Parameter parmeter:procedure.getInputParameters()){
			popVarList.add(parmeter.getId());
		}
		
		//����������ӵ���ͨ�б���
		for(Parameter parmeter:procedure.getOutputParameters()){
			popVarList.add(parmeter.getId());
		}
		
		//error_pathinfo_tmp���⴦��
		popVarList.add("error_pathinfo_tmp");
		
		return codeList.iterator();
	}
	
	/**
	 * ����Ƿ���proc�������
	 * @return
	 */
	private boolean isInProcBlock(Map<Object, Object> context){
		IDomainHandler handler = (IDomainHandler)  context.get(IEngineContextConstant.DOMAIN_HANDLER);
		ITokenDomain procBlockBeginDomain =handler.getDomain(MacroConstant.PROC_BLOCK_BEGIN_MACRONAME);
		//���ǰ����PRO*C���鿪ʼ��procBlockBeginDomain��Ϊnull
		return procBlockBeginDomain!=null;
	}
	/**
	 * ���ֶι���ӵ������б���ȥ
	 * 
	 * @param procVarList
	 */
	private void addVarList(Map<Object, Object> context) {
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		List<String> varList = new ArrayList<String>(5);
		    varList.add("error_pathinfo_tmp");
		for(String varName:varList){
			popVarList.add(varName);
		}
	}
	/**
	 * ȱ�ٹ��ܺ�
	 * @param context
	 */
	private void fireEventLessFunctionId(Map<Object, Object> context){

		ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
		String message = "���������ù��ܺ�";
		manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
	
	}

}
