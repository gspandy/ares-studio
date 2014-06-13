/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.oracle.macro.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.MarkConfig;
import com.hundsun.ares.studio.engin.exception.HSException;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.parser.PseudoCodeParser;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.ITokenDomain;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procedure.compiler.oracle.constant.IProcedureEngineContextConstantOracle;
import com.hundsun.ares.studio.procedure.compiler.oracle.macro.IProcedureMacroTokenService;
import com.hundsun.ares.studio.procedure.compiler.oracle.skeleton.util.ProcedureCompilerUtil;
import com.hundsun.ares.studio.procedure.compiler.oracle.token.SubProcedureCallToken;

/**
 * @author qinyuan
 *
 */
public class ProcedureCallMacroHandler implements IMacroTokenHandler {
	 private IARESResource resource;//���õĹ���ģ��
	 private String key;

	/**
	 * @param chineseName
	 * @param resource
	 */
	public ProcedureCallMacroHandler(String chineseName, IARESResource resource) {
		this.key = chineseName;
		this.resource = resource;
		
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return this.key;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		
		 Procedure procedure=null;//���õĹ���ģ��
		 Procedure subProcedure=null;//�����õĹ���ģ��
		 procedure = resource.getInfo(Procedure.class);
		 subProcedure = getSubProcedure(token.getKeyword(),context);
		 Map<String, String> defaultValueMap = null;
		 if(token.getParameters().length > 0){//���̵��ò��������ж�
			 defaultValueMap = PseudoCodeParser.parserKeyValueWithAt(token.getParameters()[0]);
		 }else {
			 defaultValueMap = new HashMap<String, String>();
		 }
		 List<ICodeToken> tokens = new ArrayList<ICodeToken>();
		 if(subProcedure==null){
			 throw new HSException("--���̲�����"+token.getKeyword());
		 }
		 boolean isInTransaction =isInTransaction(context);//�ж��Ƿ���������
		 addVarList(context,procedure,subProcedure,defaultValueMap);//��ӱ���
		 tokens.add(new SubProcedureCallToken(token,procedure,subProcedure,defaultValueMap,isInTransaction));
		return tokens.iterator();
	}
	/**
	 * ��ȡ�������ӹ���ģ��
	 * @param name
	 * @param context
	 * @return
	 */
	private Procedure getSubProcedure(String name,Map<Object, Object> context){
		
		IProcedureMacroTokenService service =(IProcedureMacroTokenService) context.get(IProcedureEngineContextConstantOracle.Procedure_Macro_Service);
		try {
			return  (service.getProcedure(name)).getInfo(Procedure.class);
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	/**
	 * �Ƿ��������ʼ��
	 * @param context
	 * @return
	 */
	private boolean isInTransaction(Map<Object, Object> context){
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		ITokenDomain procTransactionBegin =handler.getDomain(""/**MacroConstant.TRANSACTION_BEGIN*/);
		return procTransactionBegin!=null;
	}
	/**
	 * �ѱ�����ӵ��б���
	 * @param context
	 * @param subProcedure
	 * @param procedure
	 * @param defaultValueMap
	 */
	private void addVarList(Map<Object, Object> context,Procedure procedure,Procedure subProcedure,Map<String, String> defaultValueMap) {
		
		for(Parameter parameter :subProcedure.getInputParameters()){

			String defaultValue = defaultValueMap.get(parameter.getId());
			if(defaultValue!=null){
				String valueVarName =defaultValueMap.get(parameter.getId());
				if (valueVarName.indexOf(MarkConfig.MARK_AT) >= 0 && !ProcedureCompilerUtil.isParameterINProcedureParameterByName(procedure, valueVarName.substring(valueVarName.indexOf("@") + 1),resource.getARESProject())) {// ���Ĭ�ϲ���ֵΪ����
					String varName = valueVarName.substring(valueVarName.indexOf(MarkConfig.MARK_AT) + 1);
					addToPVarList(context,varName);
				}
				
			}else if(!ProcedureCompilerUtil.isParameterINProcedureParameterByName(procedure,parameter.getId(),resource.getARESProject())){
				addToPVarList(context,parameter.getId());
			}

		}
			
		for(Parameter parameter :subProcedure.getOutputParameters()){

			String defaultValue = defaultValueMap.get(parameter.getId());
			if(defaultValue!=null){
				String valueVarName =defaultValueMap.get(parameter.getId());
				if (valueVarName.indexOf(MarkConfig.MARK_AT) >= 0 && !ProcedureCompilerUtil.isParameterINProcedureParameterByName(procedure, valueVarName.substring(valueVarName.indexOf("@") + 1),resource.getARESProject())) {// ���Ĭ�ϲ���ֵΪ����
					String varName = valueVarName.substring(valueVarName.indexOf(MarkConfig.MARK_AT) + 1);
					addToPVarList(context,varName);
				}
				
			}else if(!ProcedureCompilerUtil.isParameterINProcedureParameterByName(procedure,parameter.getId(),resource.getARESProject())){
				addToPVarList(context,parameter.getId());
				
			}
			
			
		}	
			
	}
	/**
	 * 
	 * @param context
	 * @param name
	 */
	private void addToPVarList(Map<Object, Object> context ,String name){
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IProcedureEngineContextConstantOracle.SKELETON_ATTRIBUTE_HELPER);
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
			helper.addAttribute(IProcedureEngineContextConstantOracle.ATTR_PROC_VARIABLE_LIST, name);
			popVarList.add(name);
		
	}


}
