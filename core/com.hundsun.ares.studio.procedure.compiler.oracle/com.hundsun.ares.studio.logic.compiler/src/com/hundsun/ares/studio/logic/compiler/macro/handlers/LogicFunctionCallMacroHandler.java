/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.logic.compiler.macro.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.constant.DomainConstant;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.macro.TokenDomain;
import com.hundsun.ares.studio.biz.BizInterface;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.core.BizUtil;
import com.hundsun.ares.studio.biz.util.BizInterfaceParameterUtil;
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
import com.hundsun.ares.studio.logic.LogicFunction;
import com.hundsun.ares.studio.logic.LogicService;
import com.hundsun.ares.studio.logic.compiler.constant.ILogicEngineContextConstant;
import com.hundsun.ares.studio.logic.compiler.tokens.LogicFunctionCallToken;

/**
 * 
 * ԭ�Ӻ������ú괦��
 * ע���߼���������߼��������߼����������߼�����������
 * �ο������������ߴ����ࣺ
 * (LS->LF):CallFunctionInterpreter;(LF->LF):CallFunctionUseVariableOnlyInterpreter
 * @author qinyuan
 *
 */
public class LogicFunctionCallMacroHandler implements  IMacroTokenHandler {
	
	private String key;
	private IARESResource resource;

	/**
	 * @param chineseName
	 * @param resource
	 */
	public LogicFunctionCallMacroHandler(String chineseName,
			IARESResource resource) {
		this.key = chineseName;
		this.resource = resource;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return key;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		IARESProject project = (IARESProject) context.get(ILogicEngineContextConstant.Aresproject);
		AtomFunction logic =  (AtomFunction) context.get(ILogicEngineContextConstant.ResourceModel);//��ǰ��Դ
		Map<String, String> defaultValueList=getDefaultValuseList(token);//����Ĭ��ֵ�б�
		AtomFunction logicFunction = (AtomFunction) resource.getAdapter(AtomFunction.class);//AtomFunction�ǻ��࣬��ȡ������������б�ͳһ���������
		List<Parameter> inParameters = getInParameters(logicFunction,context);//��ȡ�������������
		Set<String> inList = getInList(inParameters, defaultValueList, logicFunction,project);//�����ض����б�
		Set<Parameter> outList = getOutList(defaultValueList,logicFunction,logic,project);//����ض����в������б�
		Map<String,String> resultsetParameters = getResultsetParameters(context);//������ֶ�(�������γɴ����ǻ��)
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(ILogicEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		//�������
		validateParameter(context,logic);
		//���÷����뵽���������������ȥ
		if(logic.isOutputCollection()){
			helper.addAttribute(ILogicEngineContextConstant.ATTR_RESULTSET_LIST,
					StringUtils.isBlank(logic.getObjectId())?logic.getName():logic.getObjectId());
			if(StringUtils.isBlank(logic.getObjectId())){
				fireEventLessFunctionId(context,logic.getName());
			}
		}
		//���뵽�߼����������б���
		helper.addAttribute(ILogicEngineContextConstant.ATTR_LOGIC_FUNC_CALL,getKey());
		//�����õļ��뵽���������������
		helper.addAttribute(ILogicEngineContextConstant.ATTR_RESULTSET_LIST,StringUtils.isBlank(logicFunction.getObjectId())?logicFunction.getName():logicFunction.getObjectId());
		if(StringUtils.isBlank(logicFunction.getObjectId())){
			fireEventLessFunctionId(context,logicFunction.getName());
		}
		//2014��4��14��13:45:16 ��Ԫ  �������������N��־���򲻽��н�����滻
		if(!StringUtils.contains(token.getFlag(), "N")){
			//ȡ�þͽ��Ľ����Id�����ǿ�����lpResultSet->ȡ�ͽ�������ģ�����Ҫ�ӵ�����б���
			helper.addAttribute(ILogicEngineContextConstant.ATTR_GETLAST_RESULTSET,logicFunction.getObjectId());
		}
		
		/**
		 * �������ض��ֵ������б���
		 */
		addOutParameterToPopVarList(outList,context);
		addPopVarList(context,token);//��������ض���Ӱൽα�����б�ȥ
		if(logicFunction.isOutputCollection()){//����ǽ�������ؼ��뵽����
			addDomain(context, StringUtils.isBlank(logicFunction.getObjectId())?logicFunction.getName():logicFunction.getObjectId());
		}
		List<ICodeToken> tokens = new ArrayList<ICodeToken>(1);
		tokens.add(new LogicFunctionCallToken( logic, logicFunction, defaultValueList, inParameters , inList ,outList,resultsetParameters,token, context));
		addOutParameters(logicFunction,context);//������������뵽�������ֶ��б���
		return tokens.iterator();
	}
	
	/**
	 * ������е��ֶ���ӵ�����������
	 * @param logicFunction
	 * @param context
	 */
  private void addOutParameters(AtomFunction logicFunction,Map<Object, Object> context){
	  Map<String, String> resultParameters =(Map<String, String>) context.get(ILogicEngineContextConstant.ATTR_RESULTSET_PARAMETER);
	  IARESProject project = (IARESProject) context.get(IAtomEngineContextConstant.Aresproject);
	  for(Parameter parameter:logicFunction.getOutputParameters()){
		  if(parameter.getParamType().getValue()==ParamType.PARAM_GROUP_VALUE){
			  List<Parameter> parameters = new ArrayList<Parameter>();
			  ParamGroupUtil.parserParamGroup(parameter, parameters,1, project);
			  for(Parameter p:parameters){
				  resultParameters.put(p.getId(), StringUtils.isBlank(logicFunction.getObjectId())?logicFunction.getName():logicFunction.getObjectId()); 
			  }
		  }else{
			  resultParameters.put(parameter.getId(), StringUtils.isBlank(logicFunction.getObjectId())?logicFunction.getName():logicFunction.getObjectId()); 
		  }
		 
		}

	}
  /**
   * �������ض��򵽱����б���
   * @param context
   * @param token
   */
  private void addOutParameterToPopVarList(Set<Parameter> outList,Map<Object, Object> context){
	  List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		for(Parameter parameter:outList){
			if(parameter.getParamType().getValue()!= ParamType.PARAM_GROUP_VALUE && parameter.getParamType().getValue()!= ParamType.OBJECT_VALUE){
				popVarList.add(parameter.getId());
			}
			
		}
  }
  /**
   * ��ӱ���
   * @param context
   * @param token
   */
  private void addPopVarList(Map<Object, Object> context,IMacroToken token){
	  List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
	  List<String> popObjectVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_OBJECT_PARA_LIST);
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(ILogicEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		if(token.getParameters().length>0){
		Map<String,String>  defaultValueMap = PseudoCodeParser.parserKeyValueWithAt(token.getParameters()[0]);
		for(String paramKey: defaultValueMap.keySet()){
			String valueVarName =defaultValueMap.get(paramKey);
			if (valueVarName.indexOf("@") >= 0) {// ���Ĭ�ϲ���ֵΪ����
				String procVarName = valueVarName.substring(valueVarName.indexOf("@") + 1);
				Parameter parameter = getParameterInOutPutParameter(paramKey);
				if(null == parameter) {
					continue;
				}
				popVarList.add(procVarName);
				if(parameter!=null && parameter.getParamType().getValue()!=ParamType.OBJECT_VALUE){
					}else if (parameter.getParamType().getValue() == ParamType.OBJECT_VALUE) {
						helper.addAttribute(IAtomEngineContextConstant.ATTR_IN_OBJECT_NOINIT_VARIABLE_LIST,paramKey);

						popObjectVarList.add(procVarName);//���뵽����α�����б���
						popVarList.remove(procVarName);//����ͨα�����б� ��ɾ��
					
					}
				}
			}
		}
		List<String> vars = new ArrayList<String>(4);//�̶�Ҫ�ӵĴ���
		 
		  for(String var:vars){
			  helper.addAttribute(ILogicEngineContextConstant.ATTR_PROC_VARIABLE_LIST,var);
				popVarList.add(var);
		  }
		  LogicFunction logic =  (LogicFunction) resource.getAdapter(LogicFunction.class);
		  if(logic instanceof LogicFunction){
				if(((LogicFunction)logic).isIsTransFunc()){
					helper.addAttribute(ILogicEngineContextConstant.ATTR_PROC_VARIABLE_LIST,"cancel_serialno");
					popVarList.add("cancel_serialno");
				}
		}
	}


	/**
	 * ��ȡĬ��ֵ�б�
	 * @param token
	 * @return
	 */
	private Map<String,String> getDefaultValuseList(IMacroToken token){
		if(token.getParameters().length>0){
			return PseudoCodeParser.parserKeyValueWithAt(token.getParameters()[0]);
		}
		return  new Hashtable<String, String>();
	}
	
	
	/**
	 * 
	 * @return ���������ض����б�
	 */
	private Set<String> getInList(List<Parameter> inParameters,Map<String, String> defaultValueList,AtomFunction logicFunction,IARESProject project){
		Set<String> inList = new HashSet<String>();
		for(Parameter parameter:inParameters){

			boolean io = false;
			
			if(BizInterfaceParameterUtil.isOutputParameter(logicFunction, parameter.getId(), project)){//����������
				io = true;
			}
			if (defaultValueList.containsKey(parameter.getId())) {
				// ���û�����ָ��
				String text = defaultValueList.get(parameter.getId());
				if(text.startsWith("@") && io){//������ض���
					continue;
				}
				
				inList.add(parameter.getId());
			}
		
		}
		
		
		return inList;
		
	}
	
	/**
	 * 
	 * @return ��������ض����б�
	 */
	private Set<Parameter> getOutList(Map<String, String> defaultValueList,AtomFunction logicFunction,AtomFunction logic,IARESProject project){
		Set<Parameter> outList = new HashSet<Parameter>();
		List<Parameter> parameters = new ArrayList<Parameter>();
		for(Parameter parameter:logicFunction.getOutputParameters()){
			if(parameter.getParamType().getValue()==ParamType.PARAM_GROUP_VALUE){
				List<Parameter> groupParameters = new ArrayList<Parameter>();
				ParamGroupUtil.parserParamGroup(parameter, groupParameters,1, project);
				parameters.addAll(parameters);
			}else{
				parameters.add(parameter);
			}
		}
		for(Parameter parameter:parameters){

			boolean io = false;
			if(StringUtils.defaultIfBlank(parameter.getFlags(), "").indexOf("IO") >= 0){
				io = true;
			}
			if (defaultValueList.containsKey(parameter.getId()) || defaultValueList.containsKey("@" + parameter.getId())) {
				String s = defaultValueList.get(parameter.getId());
				if (s == null) {
					s = defaultValueList.get("@" + parameter.getId());
					if (s != null) {
						//ָ�����������ֵʱ��������ֶΣ���Ӧ����@��ͷ
						
					}
				}
				
				if (s != null) {
					if (!s.startsWith("@")) {
						//�����IO���������ұ��Ǳ������ǻ�����Ϊ�����ض���
						if(io){
							continue;
						}
						//ָ�����������ֵʱ���Ⱥ��ұ�һ����Ҫ�Ǳ��������Աض�Ҫ@��ͷ;
						s = "@" + s;
					}
					//ͬ��������LS�㲻Ӧ����Ϊ��� 4.23 ��ΰ��
					if(logic instanceof LogicService){
						if(parameter.getId().trim().equals(s.substring(1).trim())){
							//ͬ��������LS�㲻Ӧ����Ϊ���";
							continue;
						}
					}
					
					outList.add(parameter);
				}

			}
		
		}
		
		
		return outList;
		
	}
	
	/**
	 * ��ȡ�����ú������Ե��������
	 * @return
	 */
	
	private List<Parameter> getInParameters(BizInterface bizInterface,Map<Object, Object> context){
		IARESProject project = (IARESProject) context.get(IAtomEngineContextConstant.Aresproject);
		List<Parameter> inParameters = new ArrayList<Parameter>();
		for (Parameter parameter :bizInterface.getInputParameters()) {
			if(parameter.getParamType().getValue()==ParamType.PARAM_GROUP_VALUE ){
				List<Parameter> parameters = new ArrayList<Parameter>();
				ParamGroupUtil.parserParamGroup(parameter, parameters,1, project);
				inParameters.addAll(parameters);
			}else{
				inParameters.add(parameter);
			}
			
		}
		
		for ( Parameter parameter :bizInterface.getOutputParameters()) {
			if (StringUtils.defaultIfBlank(parameter.getFlags(), "").indexOf("IO") != -1) {
				if(parameter.getParamType().getValue()==ParamType.PARAM_GROUP_VALUE ){
					List<Parameter> parameters = new ArrayList<Parameter>();
					ParamGroupUtil.parserParamGroup(parameter, parameters,1, project);
					inParameters.addAll(parameters);
				}else{
					inParameters.add(parameter);
				}
			}
		}
		
		return inParameters;
	}
	/**
	 * ����id���������������id��ͬ�Ĳ���
	 * @param id
	 * @return
	 */
	public Parameter getParameterInOutPutParameter(String id){
		LogicFunction logicFunction = (LogicFunction) resource.getAdapter(LogicFunction.class);
		List<Parameter> outputParameters = new ArrayList<Parameter>();
		ParamGroupUtil.parserParameters(logicFunction.getOutputParameters(), outputParameters, resource.getARESProject());
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
	 * ��ȡ����еĲ���
	 * @return
	 */
	private Map<String,String> getResultsetParameters(Map<Object, Object> context){
		//һ��Ҫ���¸���һ�ݲ�Ȼ����ȡ������Ľ��������
		return  new HashMap<String,String>((Map<String, String>) context.get(ILogicEngineContextConstant.ATTR_RESULTSET_PARAMETER));
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
	private void fireEventLessFunctionId(Map<Object, Object> context,String resoureceName){
		ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
		String message = resoureceName+"�����ù��ܺ�";
		manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
	}

}
