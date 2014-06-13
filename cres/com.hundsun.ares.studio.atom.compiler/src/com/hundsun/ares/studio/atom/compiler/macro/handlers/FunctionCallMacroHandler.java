/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.macro.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.AtomService;
import com.hundsun.ares.studio.atom.compiler.constant.DomainConstant;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.macro.TokenDomain;
import com.hundsun.ares.studio.atom.compiler.token.SubFunctionCallToken;
import com.hundsun.ares.studio.atom.impl.AtomServiceImpl;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.core.BizUtil;
import com.hundsun.ares.studio.biz.util.ParamGroupUtil;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.parser.PseudoCodeParser;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;

/**
 * @author zhuyf
 *
 */
public class FunctionCallMacroHandler implements IMacroTokenHandler {
	
	String key;
	
	IARESResource resource;
	
	public FunctionCallMacroHandler(String key,IARESResource resource){
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
		 * ����Ҫ��
		 * 1.������������ǳ������磺[AS_�˻���ܹ���_�ʲ��˺Ż�ȡ][input_content = CNST_ACCTINTYPE_CLIENTID, account_content = @client_id]
		 * ��Ҫ����������Ϊ���ݴ���
		 * 2.������õ�ԭ�ӵ��ֶ�����������ֶΣ���ô���ָ���˸��ֶ�Ϊĳ�����������������Ĭ��ֵ��������Ϊ���������
				[AS_�˻���ܹ���_�ʲ��˻�״̬У��][client_id=@client_id_dest]
				client_id����������ֶΣ���ô���õ�ʱ��client_id��������ʱ���Ϊ���ַ��������������@client_id_dest��������
				�����������������Ϊ��һ��ԭ�ӵ��������
		 * 
		 */
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		helper.addAttribute(IAtomEngineContextConstant.ATTR_FUNC_CALL,getKey());
		AtomFunction af = resource.getInfo(AtomFunction.class);
		validateParameter(context,af);//�������
		//�����Ϊ�գ���Ӣ��������
		String objectid = af.getObjectId();
		
		if(af instanceof AtomService && StringUtils.isBlank(af.getObjectId())){//������õ���asʱ����objectidΪ����ʾ
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = "��Դ:"+af.getName()+"�����ù��ܺ�";
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
		}
		if(StringUtils.isBlank(objectid)){
			objectid = af.getName();
		}
		if(StringUtils.isBlank(objectid)){
			fireEventLessFunctionId(context);
		}
		
		//2014��4��14��13:45:16 ��Ԫ  �������������N��־���򲻽��н�����滻
		if(!StringUtils.contains(token.getFlag(), "N")){
			//ȡ�þͽ��Ľ����Id�����ǿ�����lpResultSet->ȡ�ͽ�������ģ�����Ҫ�ӵ�����б���
			helper.addAttribute(IAtomEngineContextConstant.ATTR_GETLAST_RESULTSET,objectid);
		}
		boolean returnResultSet = af.isOutputCollection();
		//if(returnResultSet){
		//����������ؽ���������÷��ؽ������AF�Լ�[ͨ��SELECT]������ֱ�Ӵ�����
		helper.addAttribute(IAtomEngineContextConstant.ATTR_FUNC_RESULTSET,objectid);
		//}
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		//��ͨ�����б������error_no,error_info,error_patchinfo,error_pathinfo_tmp
		popVarList.add("error_no");
		popVarList.add("error_info");
		popVarList.add("error_pathinfo");
		popVarList.add("error_pathinfo_tmp");
		if(af.isOutputCollection()){
			addDomain(context, objectid);
		}
		if(token.getParameters().length>0){
			addPopVarList(context,token);
		}
		List<ICodeToken> tokens = new ArrayList<ICodeToken>();
		SubFunctionCallToken subFuncCalltoken = new SubFunctionCallToken(token, resource);
		tokens.add(subFuncCalltoken);
		return tokens.iterator();
	}
	
	  /**
	   * ��ӱ���
	   * @param context
	   * @param token
	   */
	  private void addPopVarList(Map<Object, Object> context,IMacroToken token){
		  List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		  ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
			if(token.getParameters().length>0){
			Map<String,String>  defaultValueMap = PseudoCodeParser.parserKeyValueWithAt(token.getParameters()[0]);
			for(String paramKey: defaultValueMap.keySet()){
				String valueVarName =defaultValueMap.get(paramKey);
				if (valueVarName.indexOf("@") >= 0) {// ���Ĭ�ϲ���ֵΪ����
					String procVarName = valueVarName.substring(valueVarName.indexOf("@") + 1);
					Parameter parameter = getParameterInOutPutParameter(paramKey);
					if(parameter!=null && parameter.getParamType().getValue()!=ParamType.OBJECT_VALUE){
						popVarList.add(procVarName);
						} else if (parameter!=null && parameter.getParamType().getValue() == ParamType.OBJECT_VALUE) {
							helper.addAttribute(IAtomEngineContextConstant.ATTR_IN_OBJECT_NOINIT_VARIABLE_LIST,paramKey);
						}
					}
				}
			}
		  
	  }
	
	/**
	 * ����id���������������id��ͬ�Ĳ���
	 * 
	 * @param id
	 * @return
	 */
	public Parameter getParameterInOutPutParameter(String id){
			AtomFunction atomFunction = (AtomFunction) resource.getAdapter(AtomFunction.class);
			List<Parameter> outputParameters = new ArrayList<Parameter>();
			ParamGroupUtil.parserParameters(atomFunction.getOutputParameters(), outputParameters, resource.getARESProject());
			for(Parameter parameter:outputParameters){
				if(StringUtils.equals(parameter.getId(),id)){
					return parameter;
				}
			}
			return null;
		}
	/**
	 * �����
	 */
	private void addDomain(Map<Object, Object> context,String objectId){
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		String []args = new String[1];
		 args[0] = objectId;
		handler.addDomain(new TokenDomain(DomainConstant.FUNC_RESULT_OBJECT_RETURN_DOMAIN,args));
	}
	
	/**
	 * У�����:����ֻ���������Դ�Ƿ����
	 */
	protected void validateParameter(Map<Object, Object> context,AtomFunction af){
		IARESProject project = (IARESProject) context.get(IAtomEngineContextConstant.Aresproject);
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.addAll(af.getInputParameters());
		parameters.addAll(af.getOutputParameters());
		parameters.addAll(af.getInternalVariables());
		ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
		for(Parameter parameter:parameters){
			if(parameter.getParamType() == ParamType.OBJECT || parameter.getParamType() == ParamType.PARAM_GROUP){
				if(BizUtil.getObject(parameter, project)==null){
					if(af.getInputParameters().contains(parameter)){
						String message = "�������:"+parameter.getComments()+"��Ӧ�Ķ�����Դ"+parameter.getType()+"������!";
						manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
					}else if(af.getOutputParameters().contains(parameter)){
						String message = "�������:"+parameter.getComments()+"��Ӧ�Ķ�����Դ"+parameter.getType()+"������!";
						manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
					}else if(af.getInternalVariables().contains(parameter)){
						String message = "�ڲ�����:"+parameter.getComments()+"��Ӧ�Ķ�����Դ"+parameter.getType()+"������!";
						manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
					}
				}
			}
		}
		
	}
	/**
	 * ȱ�ٹ��ܺ�
	 * @param context
	 */
	private void fireEventLessFunctionId(Map<Object, Object> context){
		ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
		String message = "�����ñ����ú������߷����ܺ�";
		manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
	
	}

}
