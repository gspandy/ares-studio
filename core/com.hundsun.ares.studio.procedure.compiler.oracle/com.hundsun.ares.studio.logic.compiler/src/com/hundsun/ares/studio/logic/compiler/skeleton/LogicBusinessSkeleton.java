/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.logic.compiler.skeleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.token.ResultSetProxyToken;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.core.BizUtil;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.parser.IPseudocodeParser;
import com.hundsun.ares.studio.engin.parser.PseudoCodeParser;
import com.hundsun.ares.studio.engin.parser.PseudocodeParserFactory;
import com.hundsun.ares.studio.engin.skeleton.ISkeleton;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.DomainHandler;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.IResultSetToken;
import com.hundsun.ares.studio.engin.token.ITokenDomain;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.ConstantItem;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoItem;
import com.hundsun.ares.studio.logic.compiler.constant.ILogicEngineContextConstant;
import com.hundsun.ares.studio.logic.compiler.macro.DefaultMacroTokenFilter;
import com.hundsun.ares.studio.logic.compiler.macro.service.impl.AtomServiceMacroTokenService;
import com.hundsun.ares.studio.logic.compiler.macro.service.impl.LogicFunctionMacroTokenService;
import com.hundsun.ares.studio.logic.compiler.util.DefaultSkeletonAttributeHelper;
import com.hundsun.ares.studio.logic.compiler.util.ParamDefineHelper;
import com.hundsun.ares.studio.logic.compiler.util.ParamReplaceUtil;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.usermacro.compiler.handlers.UserMacroTokenService;

/**
 * @author qinyuan
 *
 */
public abstract class LogicBusinessSkeleton implements ISkeleton{
	
	IARESResource resource;
	Map<Object, Object> context = new HashMap<Object, Object>();
	
	/**
	 * 
	 */
	public LogicBusinessSkeleton(IARESResource resource,Map<Object, Object> context) {
		this.resource = resource;
		if(!context.containsKey(ILogicEngineContextConstant.Aresproject)){
			context.put(ILogicEngineContextConstant.Aresproject, resource.getARESProject());
		}
		if(!this.context.containsKey(IEngineContextConstant.CURR_RESOURCE)){
			this.context.put(IEngineContextConstant.CURR_RESOURCE, resource);
		}
		if(!context.containsKey(ILogicEngineContextConstant.MACRO_FILTER)){
			context.put(ILogicEngineContextConstant.MACRO_FILTER, new DefaultMacroTokenFilter(resource.getARESProject()));
		}
//		if(!context.containsKey(ILogicEngineContextConstant.Statistic_Provider)){
//			context.put(ILogicEngineContextConstant.Statistic_Provider, JRESContextManager.getStatisticProvider(resource.getARESProject()));
//		}
		if(!context.containsKey(ILogicEngineContextConstant.Logic_Function_Macro_Service)){//�߼���������
			context.put(ILogicEngineContextConstant.Logic_Function_Macro_Service, new LogicFunctionMacroTokenService(resource.getARESProject()));
		}
		if(!context.containsKey(ILogicEngineContextConstant.Atom_Service_Macro_Service)){//ԭ�Ӻ�������
			context.put(ILogicEngineContextConstant.Atom_Service_Macro_Service, new AtomServiceMacroTokenService(resource.getARESProject()));
		}
		if(!context.containsKey(ILogicEngineContextConstant.UserMacro_Service)){
			context.put(ILogicEngineContextConstant.UserMacro_Service, new UserMacroTokenService(resource.getARESProject(),false));
		}
		if(!context.containsKey(ILogicEngineContextConstant.ATTR_RESULTSET_PARAMETER)){//LS->AS,LF->AS,LS->LF,LF->ASʱ
			context.put(ILogicEngineContextConstant.ATTR_RESULTSET_PARAMETER, new HashMap<String,String>());
		}
		
		//����ʼ����������ӵ�������
		this.context.putAll(context);
		initRuntimeContext();
	}

	/**
	 * 
	 */
	private void initRuntimeContext() {
		IDomainHandler domainHandler = new DomainHandler();
		context.put(IEngineContextConstant.DOMAIN_HANDLER, domainHandler);
		ISkeletonAttributeHelper helper = new DefaultSkeletonAttributeHelper();
		context.put(ILogicEngineContextConstant.SKELETON_ATTRIBUTE_HELPER,helper );
		//�����������û��ģ�ͣ�����Դ��ȡģ��
		if(!context.containsKey(ILogicEngineContextConstant.ResourceModel)){
			try {
				context.put(ILogicEngineContextConstant.ResourceModel, resource.getInfo(EObject.class));
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
		}
		
		context.put(ILogicEngineContextConstant.PARAM_DEFINE_HELPER, new ParamDefineHelper());
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.skeleton.ISkeleton#getRuntimeContext()
	 */
	@Override
	public Map<Object, Object> getRuntimeContext() throws Exception {
		
		return context;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.skeleton.ISkeleton#postValidate()
	 */
	@Override
	public Object[] postValidate() {
		List<String> tlist = new ArrayList<String>();
		IDomainHandler domainHandler = (IDomainHandler)context.get(IEngineContextConstant.DOMAIN_HANDLER);
		//����
		if(domainHandler.getDomains().length > 0){
			for(ITokenDomain item:domainHandler.getDomains()){
				if(ITokenDomain.GLOABL.equals(item.getType())){
					tlist.add(String.format("��[%s]����û�н�����", item.getKey()));
				}
			}
		}
		validateParameter();
		return tlist.toArray();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.skeleton.ISkeleton#handleTextToken(com.hundsun.ares.studio.engin.token.ICodeToken, java.util.Map)
	 */
	@Override
	public ICodeToken handleTextToken(ICodeToken token,
			Map<Object, Object> context) throws Exception {
		//TODO
		if(token instanceof IResultSetToken){
			//ResultSet->�϶�������Proc������ʹ�ã��ʿ϶�����Ҫ��TextWithParamsToken
			ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(ILogicEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
			Set<String> rsIdSet = helper.getAttribute(ILogicEngineContextConstant.ATTR_GETLAST_RESULTSET);
			String lastId = "";
			if(rsIdSet.size() > 0){
				lastId = (String)rsIdSet.toArray()[rsIdSet.size() - 1];
			}
			return new ResultSetProxyToken(token,lastId);
		}else{
			//ֱ��ʹ��ԭ����token
			return token;
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.skeleton.ISkeleton#getPseudocodeParser(com.hundsun.ares.studio.engin.token.ICodeToken, java.util.Map)
	 */
	@Override
	public IPseudocodeParser getPseudocodeParser(ICodeToken token,
			Map<Object, Object> context) throws Exception {
		return PseudocodeParserFactory.instance.createParser();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.skeleton.ISkeleton#onFinish(java.util.Map)
	 */
	@Override
	public void onFinish(Map<Object, Object> context) throws Exception {
		// �ο�UFT��ParaHandleToken��ɶ�@�����Ĵ����Լ�<>��������û������Ĵ���
		List<String> tlist = (List<String>) context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		if(tlist == null){
			return;
		}
		StringBuffer buffer = (StringBuffer)context.get(IEngineContextConstant.BUFFER);
		ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(ILogicEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		//IResStatisticProvider provider = (IResStatisticProvider)context.get(ILogicEngineContextConstant.Statistic_Provider);
		Set<String> inoutParamList = helper.getAttribute(ILogicEngineContextConstant.ATTR_IN_OUT_PARAM_LIST);
		List<String> popObjectVarList = (List<String>) context.get(IEngineContextConstant.PSEUDO_CODE_OBJECT_PARA_LIST);
		
		//������ı����Ĵ���
		String specialResult = ParamReplaceUtil.handleSpecialParams("", buffer.toString(), getSpecialsParams());
		
		//��������滻
		String objStrResult = ParamReplaceUtil.handleObjectParams("", specialResult, popObjectVarList);
		
		//�滻��ͨ�ı�׼�ֶ�
		String result = ParamReplaceUtil.handleParams(objStrResult, tlist.toArray(new String[0]), inoutParamList);
		
		
		if(result.toString().indexOf("<") == -1 || result.toString().indexOf(">") == -1){
			//����������
		}else{
			//�������г���
			List<String> constantList =  ParamReplaceUtil.findConstAll(result);
			//�滻���г���
			IARESProject aresProject = (IARESProject) context.get(IAtomEngineContextConstant.Aresproject);
			if (aresProject != null) {
				for(String constant:constantList){
					try {
						result = handleConstantReplace(result, constant, aresProject);
					} catch (Exception e) {
						manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,e.getMessage()));
					}
				}
			}
		}
		
		
		buffer.setLength(0);
		buffer.append(result);
	}
	
	/**
	 * �������滻
	 * @param content
	 * @param variableName
	 * @param provider
	 * @return
	 * @throws Exception
	 */
	private String handleConstantReplace(String content,String variableName, IARESProject project) throws Exception{
		//ȥ��<>ȡ�ó�����
		String paraName =variableName.trim().substring(variableName.trim().indexOf("<")+1,variableName.trim().indexOf(">"));
		String definitionCode = null;
		
		ReferenceManager refManager = ReferenceManager.getInstance();
		//�滻�����
		ReferenceInfo ref = refManager.getFirstReferenceInfo(project, IMetadataRefType.ErrNo, paraName, true);
		if (ref != null) {
			ErrorNoItem item = (ErrorNoItem) ref.getObject();
			definitionCode =  item.getNo();
		} else {
			ref = refManager.getFirstReferenceInfo(project, IMetadataRefType.Const, paraName, true);
		}
		if (ref != null) {
			ConstantItem item = (ConstantItem) ref.getObject();
			definitionCode =  item.getValue();
		}
		
		//////////////////////////////////////��ȷ��/////////////////////////////////////////
//		//�滻���ܺ�
//		objs = provider.getResouce(paraName, IResourceTable.Scope_IGNORE_NAMESPACE, IMetadataRefType.Menu_Function);
//		if(objs.length > 0){
//			Map<Object, Object> tmap= (Map<Object, Object>) objs[0];
//			Function item = (Function) tmap.get(IResourceTable.TARGET_OWNER);
//			definitionCode =  item.getName();
//		}
//		
//		//�滻�˵�
//		objs = provider.getResouce(paraName, IResourceTable.Scope_IGNORE_NAMESPACE, IMetadataRefType.Menu);
//		if(objs.length > 0){
//			Map<Object, Object> tmap= (Map<Object, Object>) objs[0];
//			MenuItem item = (MenuItem) tmap.get(IResourceTable.TARGET_OWNER);
//			definitionCode =  item.getRefId();
//		}
     //////////////////////////////////////��ȷ��/////////////////////////////////////////
		
		//û���ҵ���ض���
		if(null == definitionCode){
			throw new Exception(String.format("δ�ҵ��ĳ���%s����ض���", paraName));
		}
		
		//������Ҫ�滻����"��
		definitionCode = definitionCode.replaceAll("\"", "'");
		//�ó�����ʵ��ֵ�滻������
		return ParamReplaceUtil.replaceContentWithNotString(content,variableName,definitionCode);	
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.skeleton.ISkeleton#getCommentType()
	 */
	@Override
	public int getCommentType() {
		return PseudoCodeParser.CPP_TYPE;
	}
	
	/**
	 * У�����:����ֻ���������Դ�Ƿ����
	 */
	protected void validateParameter(){
		AtomFunction logic  = (AtomFunction)context.get(IAtomEngineContextConstant.ResourceModel);
		IARESProject project = (IARESProject) context.get(IAtomEngineContextConstant.Aresproject);
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.addAll(logic.getInputParameters());
		parameters.addAll(logic.getOutputParameters());
		parameters.addAll(logic.getInternalVariables());
		ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
		for(Parameter parameter:parameters){
			if(parameter.getParamType() == ParamType.OBJECT || parameter.getParamType() == ParamType.PARAM_GROUP){
				if(BizUtil.getObject(parameter, project)==null){
					if(logic.getInputParameters().contains(parameter)){
						String message = "�������:"+parameter.getComments()+"��Ӧ�Ķ�����Դ"+parameter.getType()+"������!";
						manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
					}else if(logic.getOutputParameters().contains(parameter)){
						String message = "�������:"+parameter.getComments()+"��Ӧ�Ķ�����Դ"+parameter.getType()+"������!";
						manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
					}else if(logic.getInternalVariables().contains(parameter)){
						String message = "�ڲ�����:"+parameter.getComments()+"��Ӧ�Ķ�����Դ"+parameter.getType()+"������!";
						manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
					}
				}
			}
		}
		
	}
	
	/**
	 * �������ı���,�Ա�����滻ʱ��������ı����滻
	 * @return
	 */
	private List<String> getSpecialsParams(){
		List<String> specialsParams = new ArrayList<String>();
		specialsParams.add("lpContext");
		specialsParams.add("lpInUnPacker");
		specialsParams.add("lpOutPacker");
		
		return specialsParams;
		
	}
}
