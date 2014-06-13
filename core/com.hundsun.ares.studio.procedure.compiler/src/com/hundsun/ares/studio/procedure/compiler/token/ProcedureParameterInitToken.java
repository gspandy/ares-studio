/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;

import com.hundsun.ares.studio.biz.ARESObject;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.constants.IBizRefType;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.constant.MarkConfig;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procedure.compiler.constant.IProcedureEngineContextConstant;
import com.hundsun.ares.studio.procedure.compiler.skeleton.util.ProcedureCompilerUtil;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * 
 * ���̲�����ʼ�������������������pathinfo��
 *
 * @author liaogc
 */
public class ProcedureParameterInitToken implements ICodeToken {
	private Procedure procedure;//����ģ��
	private static  Map<Object, Object> context;
	private final static String PARAM_INIT = "\tv_%1$s  := %2$s ;" + ITokenConstant.NL;
	//�������ǰ׺ΪP_
	private final static String EXPORT_PARAM_INIT = "\tp_%1$s  := %2$s ;" + ITokenConstant.NL;
	private static final String NL = ITokenConstant.NL;
	

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#getContent()
	 */
	@Override
	public String getContent() {
		return StringUtils.EMPTY;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#getType()
	 */
	@Override
	public int getType() {
		return ICodeToken.CODE_TEXT;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#genCode(java.util.Map)
	 */
	@Override
	public String genCode(Map<Object, Object> context) throws Exception {
		this.context =context;
         this.procedure = (Procedure)context.get(IProcedureEngineContextConstant.ResourceModel);
         
		IARESProject project = (IARESProject)context.get(IProcedureEngineContextConstant.Aresproject);
		StringBuffer initCode = new StringBuffer();
	
		//���������ʼ��
		List<Parameter> totleOutputParam = new ArrayList<Parameter>();
		getTotleParameters(project ,this.procedure.getOutputParameters() ,totleOutputParam);
		for(Parameter parameter:totleOutputParam){
			String defaultValue =getDefaultValue(parameter,project);
			
			if(StringUtils.equalsIgnoreCase(defaultValue, "null")) {
				//��������
				continue;
			}
			
			if(!procedure.isOutputCollection()){//������Ƿ��ؽ����
				if(!StringUtils.equals(parameter.getFlags(), MarkConfig.MARK_IOFLAG)){//ֻ�������ʼ��
					if(defaultValue!=null){
						initCode.append(String.format(EXPORT_PARAM_INIT, parameter.getId(),defaultValue));
					}
				}
			}else{
				if(defaultValue!=null){
					initCode.append(String.format(EXPORT_PARAM_INIT, parameter.getId(),defaultValue));
				}
			}
		}
		
		Set<String> parameters = new  HashSet<String>();//��¼�����Ƿ��ʼ��
		 //�Զ������������Ϊ����
		 boolean autoDefineInputParam = 
				(Boolean) context.get(IProcedureEngineContextConstant.auto_define_input_param);
		 if(autoDefineInputParam) {//����Ѿ��������������Ϊ�ڲ�������������ͬ��������ʼ��
			//���������ʼ��
			for(Parameter parameter:procedure.getInputParameters()){
				parameters.add(parameter.getId());
			}
		 }
		
		//�ڲ�������ʼ��
		for(Parameter parameter:procedure.getInternalVariables()){
			if(!parameters.contains(parameter.getId())){
				parameters.add(parameter.getId());
				String defaultValue =getDefaultValue(parameter,project);
				if(defaultValue!=null){
					initCode.append(String.format(PARAM_INIT, parameter.getId(),defaultValue));
				}
			}
				
		}
			
		//������ر�������
		 for(String name:ProcedureCompilerUtil.getErrorVarsName()){
	    	if(!ProcedureCompilerUtil.isParameterINProcedureParameterByName(procedure, name,project)){//������������Լ��ڲ�������
	    		if(!parameters.contains(name)){
					parameters.add(name);
					
					//������ر���δ����ɱ�׼�ֶ�ʱ��ʹ��Լ����Ĭ��ֵ
    				ReferenceInfo stdField = ReferenceManager.getInstance().getFirstReferenceInfo(project,IMetadataRefType.StdField, name, false);
    				if(null == stdField) {
    					initCode.append(String.format(PARAM_INIT, name,ProcedureCompilerUtil.getErrorVarDefaultValue(name)));
    				}else {
    					String defaultValue =getStandardFieldDefaultValue(name,project);
    					if(defaultValue!=null){
    						initCode.append(String.format(PARAM_INIT, name,defaultValue));
    					}
    				}
	    		}
	    	}
		 }

		//α���������ʼ��
	  @SuppressWarnings("unchecked")
	  List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);//ȡ��α��������б�
		
		for(int i = 0;i < popVarList.size();i++){
			String fieldName = popVarList.get(i);
			if(!parameters.contains(fieldName)){
				parameters.add(fieldName);
				try{
					if(!ProcedureCompilerUtil.isParameterINProcedureParameterByName(procedure, fieldName,project)){
						String defaultValue =getStandardFieldDefaultValue(fieldName,project);
						if(defaultValue!=null){
							initCode.append(String.format(PARAM_INIT, fieldName,defaultValue));
						}
					}
				  
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
			
	    //��errorPathinfo��ʼ�����⴦��
		initCode.append(initErrorPathInfo());
		
		return  initCode.toString();
	}
	
	/**
	 * �������ܣ����������еĲ������ܵ����������
	 * 
	 * @param project
	 * @param params
	 * @param totleParams
	 */
	private void getTotleParameters(IARESProject project , EList<Parameter> params , List<Parameter> totleParams) {
		for(Parameter p :params){
			if (p.getParamType() == ParamType.OBJECT) {
				continue;
			}
			if (p.getParamType() == ParamType.PARAM_GROUP) {
				ReferenceInfo info = ReferenceManager.getInstance().getFirstReferenceInfo(project, IBizRefType.Object, p.getType(), false);
				if (info != null) {
					Object obj = info.getObject();
					if (obj instanceof ARESObject) {
						getTotleParameters(project ,((ARESObject) obj).getProperties(), totleParams);
					}
				}
			}else {
				totleParams.add(p);
			}
		}
	}
	
	/**
	 * 
	 * @param parameter
	 * @param project
	 * @return ���ز���Ĭ��ֵ
	 */
	private String getDefaultValue(Parameter parameter ,IARESProject project){
		String defaultValue = parameter.getDefaultValue();
		//���ж��Ƿ��Ǳ�׼Ĭ��ֵ�����ã�����ǣ���ֱ�ӷ��أ�����Ҫ�жϲ�������
		if (StringUtils.isNotBlank(defaultValue)) {
			try {
				TypeDefaultValue typeDev = MetadataServiceProvider.getTypeDefaultValueByName(project, defaultValue);
				if (typeDev != null) {
					return typeDev.getValue(ProcedureCompilerUtil.getDatabaseType(project));
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if(parameter.getParamType().getValue() == ParamType.NON_STD_FIELD_VALUE){//����Ǳ�׼�ֶ�
			if(StringUtils.isEmpty(defaultValue)){
				TypeDefaultValue typeDefValue = null;
				try {
					typeDefValue = MetadataServiceProvider.getTypeDefaultValueOfBizTypeByName(project, parameter.getType());
				} catch (Exception e) {
					ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
					String message = String.format("��ʼ������[%1s]����.����ԭ��:�ò���Ϊ�Ǳ�׼�ֶβ���,���ǲ������Ͷ�Ӧ��ҵ�����Ͳ�����", parameter.getId());
					manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				}
				if(typeDefValue != null){
					defaultValue = typeDefValue.getValue(ProcedureCompilerUtil.getDatabaseType(project));
				}
			}
		}else if(parameter.getParamType().getValue() == ParamType.STD_FIELD_VALUE){
			//����Ǳ�׼�ֶ�
			try {
				//����ʹ�ò�����Ĭ��ֵ,����Ĭ��ֵ��������ʹ�ñ�׼�ֶζ�Ӧ��Ĭ��ֵ
				if(StringUtils.isEmpty(defaultValue)){
					defaultValue = getStandardFieldDefaultValue(parameter.getId(),project);
				}
			}
			catch(Exception e){
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("��ʼ������[%1s]����.�ò���Ϊ��׼�ֶβ���,���ǻ�ñ�׼�ֶ������Ϣʱ����", parameter.getId());
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			}
		}else if(parameter.getParamType().getValue() == ParamType.OBJECT_VALUE){
			//����
			if(StringUtils.isNotEmpty(defaultValue)){
				return defaultValue;
			}else {
				return "null";
			}
		}
		return defaultValue;
	}
	
	/**
	 * ���ر�׼�ֶε�Ĭ��ֵ
	 * @param parameter
	 * @param project
	 * @return ����Ĭ��ֵ
	 */
	private String getStandardFieldDefaultValue(String paramName ,IARESProject project)throws Exception{
		String defaultValue = null;

		StandardDataType stdType = MetadataServiceProvider.getStandardDataTypeOfStdFieldByName(project, paramName);
		TypeDefaultValue typpeDefValue = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName(project, paramName);
		BusinessDataType	busType = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(project, paramName);
		
		if((stdType != null) && ( typpeDefValue!= null) && ( busType!= null)){//��׼�ֶ�
			defaultValue = typpeDefValue.getValue(ProcedureCompilerUtil.getDatabaseType(project));
		}
		return defaultValue;
		
	}
	private String initErrorPathInfo(){
		StringBuffer initCode = new StringBuffer();
		initCode.append("\t@error_pathinfo := substr(@error_pathinfo || '-->"+procedure.getName()+"',1,500);").append(NL);
		initCode.append("\tv_error_pathinfo_tmp := @error_pathinfo;").append(NL);
		return initCode.toString();
	}
	
	
	
}
