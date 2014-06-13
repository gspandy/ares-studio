package com.hundsun.ares.studio.atom.compiler.skeleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.macro.filter.DefaultMacroTokenFilter;
import com.hundsun.ares.studio.atom.compiler.macro.func.FunctionMacroTokenService;
import com.hundsun.ares.studio.atom.compiler.macro.procedure.ProcedureMacroTokenService;
import com.hundsun.ares.studio.atom.compiler.skeleton.util.DefaultSkeletonAttributeHelper;
import com.hundsun.ares.studio.atom.compiler.skeleton.util.ParamDefineHelper;
import com.hundsun.ares.studio.atom.compiler.skeleton.util.ParamReplaceUtil;
import com.hundsun.ares.studio.atom.compiler.token.PROCPoxyCodeToken;
import com.hundsun.ares.studio.atom.compiler.token.ResultSetProxyToken;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.core.BizUtil;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.context.IResStatisticProvider;
import com.hundsun.ares.studio.core.context.JRESContextManager;
import com.hundsun.ares.studio.core.context.statistic.IResourceTable;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.parser.PseudoCodeParser;
import com.hundsun.ares.studio.engin.skeleton.ISkeleton;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.DomainHandler;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.IResultSetToken;
import com.hundsun.ares.studio.engin.token.ITextWithParamsToken;
import com.hundsun.ares.studio.engin.token.ITokenDomain;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.ConstantItem;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoItem;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.usermacro.compiler.handlers.UserMacroTokenService;

public abstract class BussinessSkeleton implements ISkeleton{

	IARESResource resource;
	Map<Object, Object> context = new HashMap<Object, Object>();
	
	public BussinessSkeleton(IARESResource resource,Map<Object, Object> context){
		this.resource = resource;
		if(!this.context.containsKey(IAtomEngineContextConstant.Aresproject)){
			this.context.put(IAtomEngineContextConstant.Aresproject, resource.getARESProject());
		}
		if(!this.context.containsKey(IEngineContextConstant.CURR_RESOURCE)){
			this.context.put(IEngineContextConstant.CURR_RESOURCE, resource);
		}
		if(!this.context.containsKey(IAtomEngineContextConstant.MACRO_FILTER)){
			this.context.put(IAtomEngineContextConstant.MACRO_FILTER, new DefaultMacroTokenFilter(resource.getARESProject()));
		}
//		if(!this.context.containsKey(IAtomEngineContextConstant.Statistic_Provider)){
//			this.context.put(IAtomEngineContextConstant.Statistic_Provider, JRESContextManager.getStatisticProvider(resource.getARESProject()));
//		}
		if(!this.context.containsKey(IAtomEngineContextConstant.Function_Macro_Service)){
			this.context.put(IAtomEngineContextConstant.Function_Macro_Service, new FunctionMacroTokenService(resource.getARESProject()));
		}
		if(!this.context.containsKey(IAtomEngineContextConstant.UserMacro_Service)){
			this.context.put(IAtomEngineContextConstant.UserMacro_Service, new UserMacroTokenService(resource.getARESProject() ,false));
		}
		if(!this.context.containsKey(IAtomEngineContextConstant.Procedure_Macro_Service)){
			this.context.put(IAtomEngineContextConstant.Procedure_Macro_Service, new ProcedureMacroTokenService(resource.getARESProject()));
		}
		//ȫ�������ļ��뵽����ʱ������
		this.context.putAll(context);
		if(!this.context.containsKey(IEngineContextConstant.DOMAIN_HANDLER)){
			IDomainHandler domainHandler = new DomainHandler();
			this.context.put(IEngineContextConstant.DOMAIN_HANDLER, domainHandler);
		}
		if(!this.context.containsKey(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER)){
			ISkeletonAttributeHelper helper = new DefaultSkeletonAttributeHelper();
			this.context.put(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER,helper );
		}
		//�����������û��ģ�ͣ�����Դ��ȡģ��
		if(!this.context.containsKey(IAtomEngineContextConstant.ResourceModel)){
			try {
				this.context.put(IAtomEngineContextConstant.ResourceModel, resource.getInfo(EObject.class));
			} catch (ARESModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!this.context.containsKey(IAtomEngineContextConstant.PARAM_DEFINE_HELPER)){
			this.context.put(IAtomEngineContextConstant.PARAM_DEFINE_HELPER, new ParamDefineHelper());
		}
	}
	
	@Override
	public Map<Object, Object> getRuntimeContext() throws Exception {
		//����ֻȡ�����ģ���ʼ���ڹ��캯������ɡ�
		return this.context;
	}


	@Override
	public Object[] postValidate(){
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
	public ICodeToken handleTextToken(ICodeToken token, Map<Object, Object> context)	throws Exception {
		boolean inBlock = false;  //�Ƿ���proc���鵱��
		IDomainHandler handler = (IDomainHandler)context.get(IEngineContextConstant.DOMAIN_HANDLER);
		ITokenDomain domain = handler.getDomain(MacroConstant.PROC_BLOCK_BEGIN_MACRONAME);
		if(null == domain){
			inBlock = false;
		}else{
			inBlock = true;
		}

		//�����ͨ�ı��е�proc�����б�
		if((token instanceof ITextWithParamsToken) && inBlock){
			ITextWithParamsToken txtToken = (ITextWithParamsToken)token;
			ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
			for(String paramName : txtToken.getUsedParams()){
				helper.addAttribute(IAtomEngineContextConstant.ATTR_PROC_VARIABLE_LIST, paramName);
			}
			//�����proc�鵱�У�ʹ�ô���
			return new PROCPoxyCodeToken(token);
		}else if(token instanceof IResultSetToken){
			//ResultSet->�϶�������Proc������ʹ�ã��ʿ϶�����Ҫ��TextWithParamsToken
			ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
			Set<String> rsIdSet = helper.getAttribute(IAtomEngineContextConstant.ATTR_GETLAST_RESULTSET);
			String lastId = "";
			if(rsIdSet.size() > 0){
				lastId = (String)rsIdSet.toArray()[rsIdSet.size() - 1];
			}
			return new ResultSetProxyToken(token,lastId);
		}else{
			//�������proc�鵱�У�ֱ��ʹ��ԭ����token
			return token;
		}
		
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
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		//IResStatisticProvider provider = (IResStatisticProvider)context.get(IAtomEngineContextConstant.Statistic_Provider);
		Set<String> inoutParamList = helper.getAttribute(IAtomEngineContextConstant.ATTR_IN_OUT_PARAM_LIST);
		
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
		if (ref == null) {
			// ����
			ref = refManager.getFirstReferenceInfo(project, IMetadataRefType.Const, paraName, true);
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
		
		if (ref != null) {
			definitionCode = ref.getRefName();
		}
		
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
	
	
}
