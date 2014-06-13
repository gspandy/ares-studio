/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.InternalParam;
import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.AtomFunctionCompilerUtil;
import com.hundsun.ares.studio.atom.compiler.mysql.token.InternalParameterArrayHelper;
import com.hundsun.ares.studio.biz.ARESObject;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.util.ParamGroupUtil;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IParamDefineHelper;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.procdure.Procedure;

/**
 * @author zhuyf
 *
 */
public class FunctionParamDefineToken implements ICodeToken {
	//private IParamDefineHelper defineHelper=null ;
	private AtomFunction atomFunction =null;
	private Map<Object, Object> context;
	private static List<String> specialsParams = new ArrayList<String>();
	{
		specialsParams.add("lpContext");
		specialsParams.add("lpInUnPacker");
		specialsParams.add("lpOutPacker");
	}
	//����������������
	SpecialParamDefineHelper specialParamDefineHelper = null;
	

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#getContent()
	 */
	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#getType()
	 */
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#genCode(java.util.Map)
	 */
	@Override
	public String genCode(Map<Object, Object> context) throws Exception {
        this.context = context;
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		List<String> pseudoObjectParaList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_OBJECT_PARA_LIST);
		
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		
		//�������������
		IParamDefineHelper defineHelper = (IParamDefineHelper)context.get(IAtomEngineContextConstantMySQL.PARAM_DEFINE_HELPER);
		
		specialParamDefineHelper = new SpecialParamDefineHelper();//�����������������ʼ��
		
		atomFunction = (AtomFunction) context.get(IAtomEngineContextConstantMySQL.ResourceModel);
		

		EList<InternalParam> internalVars = atomFunction.getInternalVariables();
		EList<Parameter> inputParams = atomFunction.getInputParameters();
		EList<Parameter> outputParams = atomFunction.getOutputParameters();

		
		//��Դ����������������뵽���������У��Ա����������߷���Ч�ʡ�
		for(int i = 0;i < inputParams.size();i++){
			Parameter parameter = inputParams.get(i);
			String paramName = parameter.getId();
			if(parameter.getParamType().getValue() != ParamType.PARAM_GROUP_VALUE){
				helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_IN_OUT_PARAM_LIST,paramName);
			}
			if((parameter.getParamType().getValue() == ParamType.STD_FIELD_VALUE) || (parameter.getParamType().getValue() == ParamType.NON_STD_FIELD_VALUE) ){
				if(!popVarList.contains(paramName)){
					popVarList.add(paramName);
				}
			}else if(parameter.getParamType().getValue() == ParamType.OBJECT_VALUE){
				pseudoObjectParaList.add(parameter.getId());
				popVarList.remove(parameter.getId());
				
			}else if( parameter.getParamType().getValue()==ParamType.PARAM_GROUP_VALUE ){//��������ǲ�����
				List<Parameter> stdParameters = new ArrayList<Parameter>();
				parserParamGroup(parameter,stdParameters,true,1,context);
				for(Parameter stdParameter:stdParameters){
					helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_IN_OUT_PARAM_LIST,stdParameter.getId());
					if(!popVarList.contains(stdParameter.getId())){
						popVarList.add(stdParameter.getId());

					}

				}

			}
		}
		for(int i = 0;i < outputParams.size();i++){
			Parameter parameter = outputParams.get(i);
			String paramName = parameter.getId();
			if(parameter.getParamType().getValue() != ParamType.PARAM_GROUP_VALUE){
				helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_IN_OUT_PARAM_LIST,paramName);
			}
			if((parameter.getParamType().getValue() == ParamType.STD_FIELD_VALUE) || (parameter.getParamType().getValue() == ParamType.NON_STD_FIELD_VALUE) ){
				if(!popVarList.contains(paramName)){
					popVarList.add(paramName);
				}
			}else if(parameter.getParamType().getValue() == ParamType.OBJECT_VALUE){
				pseudoObjectParaList.add(parameter.getId());
				popVarList.remove(parameter.getId());
				
			}else if( parameter.getParamType().getValue()==ParamType.PARAM_GROUP_VALUE ){//��������ǲ�����
				List<Parameter> stdParameters = new ArrayList<Parameter>();
				parserParamGroup(parameter,stdParameters,false,1,context);
				for(Parameter stdParameter:stdParameters){
					helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_IN_OUT_PARAM_LIST,stdParameter.getId());
					if(!popVarList.contains(stdParameter.getId())){
						popVarList.add(stdParameter.getId());

					}

				}

			}
		}
		for(int i = 0;i < internalVars.size();i++){
			String paramName = internalVars.get(i).getId();
			if(!popVarList.contains(paramName)){
				popVarList.add(paramName);
			}
		}
		
		IARESProject project = (IARESProject)context.get(IAtomEngineContextConstantMySQL.Aresproject);
		StringBuffer codeBuffer = new StringBuffer();

		codeBuffer.append("//IF2PackSvr��������ʼ��\r\n");
		codeBuffer.append("IF2PackSvr * lpPackService = lpContext->GetF2PackSvr();\r\n");	
		helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_LP_PACK_SERVICE_LIST, "lpPackService");
		if(inputParams.size() > 0){
			codeBuffer.append("//���������ʼ��" + ITokenConstant.NL);
		}
		StringBuffer paramGroupBuffer = new StringBuffer();
		for(int i = 0;i < inputParams.size();i++){
			Parameter p = inputParams.get(i);
			codeBuffer.append(getInParameterDefineInitCodeBuffer(p,project));
		}
		//���岢��ʼ����Ҫ��lpInUnPacker��ʼ���Ķ���(�ɺ����)
		Set<String> initObjectList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_IN_OBJECT_INIT_VARIABLE_LIST);
		for(String objectParam:initObjectList){
			if(defineHelper.canInit(IParamDefineHelper.RECORD_OBJECT, objectParam)){
				codeBuffer.append(getObjectParamDefineInitCodeStr(objectParam,context,objectParam,true));
				defineHelper.addInit(IParamDefineHelper.RECORD_OBJECT, objectParam);
				pseudoObjectParaList.add(objectParam);
				popVarList.remove(objectParam);
			}
			
		}
		
		//ֻ���嵫����ʼ����Ҫ��lpInUnPacker��ʼ�����������(�ɺ����)
		Set<String> noInitObjectList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_IN_OBJECT_NOINIT_VARIABLE_LIST);
		for(String objectParam:noInitObjectList){
			if(defineHelper.canInit(IParamDefineHelper.RECORD_OBJECT, objectParam)){
			codeBuffer.append(getObjectParamDefineInitCodeStr(objectParam,context,objectParam,false));
			defineHelper.addInit(IParamDefineHelper.RECORD_OBJECT, objectParam);
			pseudoObjectParaList.add(objectParam);
			popVarList.remove(objectParam);
		   }
		}
		
		if(outputParams.size() > 0){
			codeBuffer.append("//���������ʼ��" + ITokenConstant.NL);
		}
		for(int i = 0;i < outputParams.size();i++){
			Parameter p = outputParams.get(i);
			if(defineHelper.canInit(IParamDefineHelper.STD, p.getId())){
				String outParamDefineCode  = "";
				if(p.getFlags().equalsIgnoreCase("IO")){//IO������������������������弰��ʼ������
					outParamDefineCode = getInParameterDefineInitCodeBuffer(p,project);
				}else{
					outParamDefineCode = getOutParameterDefineCodeBuffer(p,project);
				}
				if(StringUtils.isNotBlank(outParamDefineCode)){
					defineHelper.addInit(IParamDefineHelper.STD, p.getId());
					codeBuffer.append(outParamDefineCode);
				}
			}
		}
		//������������ʼ��(�����ɺ������������)
		Set<String> initOutObjectList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_OUT_OBJECT_INIT_VARIABLE_LIST);
		for(String objectParam:initOutObjectList){
			if(defineHelper.canInit(IParamDefineHelper.RECORD_OBJECT, objectParam)){
			    codeBuffer.append(getOutObjectParamDefineInitCodeStr(objectParam,context,objectParam));
			    defineHelper.addInit(IParamDefineHelper.RECORD_OBJECT, objectParam);
			    pseudoObjectParaList.add(objectParam);
			    popVarList.remove(objectParam);
			}
		}
		
		if(internalVars.size() > 0){
			codeBuffer.append("//�ڲ�������ʼ��" + ITokenConstant.NL);
		}
		for(int i = 0;i < internalVars.size();i++){
			InternalParam var = internalVars.get(i);
			if(defineHelper.canInit(IParamDefineHelper.STD, var.getId())){
				codeBuffer.append(getVarDefineCodeBuffer(var,project));
				defineHelper.addInit(IParamDefineHelper.STD, var.getId());
			}
		}
		
		//α����
		for(int i = 0;i < popVarList.size();i++){
			String fieldName = popVarList.get(i);
			//���������Ҳ����Ҫ������������Զ�����
			if(!specialsParams.contains(fieldName) && !specialParamDefineHelper.isSpecialParam(fieldName) && (fieldName != null) && defineHelper.canInit(IParamDefineHelper.STD, fieldName)){//��δ�����������
				StandardField stdfield = MetadataServiceProvider.getStandardFieldByNameNoExp(project, fieldName);
				//�α����
				if(fieldName.endsWith("_cur") && (stdfield == null)){
					codeBuffer.append(getPopVarDefineCodeBuffer(fieldName.replace("_cur",""),project,"_cur"));//�α��������
					defineHelper.addInit(IParamDefineHelper.STD, fieldName);
				}else{
					if(stdfield == null){
						String message = String.format("α�������[%1$s]��Ӧ�ı�׼�ֶβ����ڡ�", fieldName);
						throw new Exception(String.format(message,fieldName));
					}else{
						codeBuffer.append(getPopVarDefineCodeBuffer(fieldName,project,""));//��ͨ��������
						defineHelper.addInit(IParamDefineHelper.STD, fieldName);
					}
				}
			}
		}
		//�����α�����зǱ�׼�ֶ�����
		specialVarIntoPopVarList(popVarList);
		codeBuffer.append(specialParamDefineHelper.getSpecialParamsDefineCodeStr(project, context));
		
		//��Ӳ�����ʼ������
		if(StringUtils.isNotBlank(paramGroupBuffer.toString())){
			codeBuffer.append(paramGroupBuffer.toString());
		}
		
		//���error_path_info�����⴦��
		codeBuffer.append(getErrorPathInfoDefineCodeStr(context));
		//���ؽ�����Ĺ��̵����б�
		Set<String> rsPrcCallList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_PROCEDURE_CALL_RSRETURN);
		//δ���ؽ�����Ĺ��̵����б�
		Set<String> nrsPrcCallList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_PROCEDURE_CALL_NOTRSRETURN);
		Set<String> funcCallList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_FUNC_CALL);
		if (funcCallList.size() > 0 || (rsPrcCallList.size() > 0) || (nrsPrcCallList.size() > 0)) {
			codeBuffer.append("hs_strncpy(@error_pathinfo_tmp,@error_pathinfo,500);\r\n");//�������ã����̵��ã�����ǰ����·��������ʱ��������
		}
		
		
		return codeBuffer.toString();
	}
	/**
	 * @return
	 */
	private String getErrorPathInfoDefineCodeStr(Map<Object, Object> context) {
		IARESProject project = (IARESProject)context.get(IAtomEngineContextConstantMySQL.Aresproject);
		String length = "500";//error_pathinfoĬ�ϳ���500
		StandardField stdField = MetadataServiceProvider.getMetadataModelByName(project, "error_pathinfo", IMetadataRefType.StdField, StandardField.class);
		if(stdField != null){
			BusinessDataType bType = MetadataServiceProvider.getMetadataModelByName(project, stdField.getDataType(), IMetadataRefType.BizType, BusinessDataType.class);
			if(bType != null  && !StringUtils.isBlank(bType.getLength())){
				length = bType.getLength();
			}
		}
		AtomFunction af = (AtomFunction) context.get(IAtomEngineContextConstantMySQL.ResourceModel);
		String error_path_info_str  = 
			"hs_strncpy(@error_pathinfo,conversion((char *)lpInUnPacker->GetStr(\"error_pathinfo\"))," + length + ");\r\n" +
			"hs_strncat(@error_pathinfo,\"->" + 
			(StringUtils.isBlank(af.getObjectId())?af.getName():"F"+af.getObjectId()) + "()\"," + length + ");\r\n";
		return error_path_info_str;
	}

	/**
	 * 
	 */
	private void specialVarIntoPopVarList(List<String> popVarList) {
		// TODO Auto-generated method stub
		popVarList.add("error_no");//error_no
		popVarList.add("error_info");//error_info
		popVarList.add("error_id");//error_id
		popVarList.add("error_sysinfo");//error_sysinfo
		popVarList.add("error_pathinfo");//error_pathinfo
	}
	
	/**
	 * ��ȡ�������������䣬�����׼�ֶβ�����Ǳ���������������
	 * @param param �������
	 * @param project ����
	 * @return String ��������������
	 */
	private String getInParameterDefineInitCodeBuffer(Parameter param,IARESProject project){
		StringBuffer codeBuffer = new StringBuffer();
		IParamDefineHelper defineHelper = (IParamDefineHelper)context.get(IAtomEngineContextConstantMySQL.PARAM_DEFINE_HELPER);
		if(defineHelper.canInit(IParamDefineHelper.STD, param.getId()) && ((param.getParamType() == ParamType.STD_FIELD) || (param.getParamType() == ParamType.NON_STD_FIELD)))//��׼�ֶβ�����Ǳ����
		{
			String bizTypeName = "";
			if(param.getParamType() == ParamType.STD_FIELD)//��׼�ֶβ���
			{
				StandardField stdfield = MetadataServiceProvider.getStandardFieldByNameNoExp(project, param.getId());//getIdΪ��������getNameΪ������
				if(stdfield == null){
					ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
					String message = String.format("�������[%1$s]��Ӧ�ı�׼�ֶβ����ڡ�", param.getId());
					manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
					return "";//��׼�ֶβ�����ʱ���������ɣ���������д�����Ϣ���׳���
				}
				bizTypeName = stdfield.getDataType();//��׼�ֶ�ʱ��ȡ��׼�ֶζ�Ӧҵ������
			}else if(param.getParamType() == ParamType.NON_STD_FIELD){//�Ǳ����
				bizTypeName = param.getType();//�Ǳ��ֶ�ʱ��ȡ������ֱ�������ҵ������
			}
			int length = 0;
			BusinessDataType bizType = MetadataServiceProvider.getBusinessDataTypeByNameNoExp(project, bizTypeName);//���ﲻ��ʹ��param.getType()�����������Ǳ�ʱ��ȡ����ҵ�����͵��쳣
			if(bizType == null){
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("�������[%1$s]��Ӧ��ҵ������[%2$s]�����ڡ�", param.getId(),bizTypeName);
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				return "";//ҵ�����Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
			}
			try {
				length = Integer.parseInt(bizType.getLength()) + 1;//����Char�������ʱ������Ҫ��1
			} catch (Exception e) {
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("�������[%1$s]��Ӧ��ҵ������:%2$s�ĳ���Ϊ�Ƿ����֣�%3$s��", param.getId(),bizTypeName,bizType.getLength());
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				return "";//ҵ�����ͳ���Ϊ�Ƿ�����ʱ���������ɣ���������д�����Ϣ���׳���
			}
			StandardDataType stdType = MetadataServiceProvider.getStandardDataTypeByNameNoExp(project, bizType.getStdType());
			if(stdType == null){
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("�������[%1$s]��Ӧ��ҵ������[%2$s]�еı�׼����[%3$s]�����ڡ�", param.getId(),bizTypeName,bizType.getStdType());
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				return "";//��׼���Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
			}
			String dataType = StringUtils.defaultIfBlank(stdType.getValue(MetadataServiceProvider.C_TYPE), "");
			dataType = dataType.replace("$L", length + "");
			if(TypeRule.typeRuleChar(dataType)){
				codeBuffer.append(String.format(PARAM_DEFINE_INIT_CHAR,param.getId()));
				defineHelper.addInit(IParamDefineHelper.STD, param.getId());
			}else if(TypeRule.typeRuleCharArray(dataType)){
				codeBuffer.append(getOutParameterDefineCodeBuffer(param,project));
				//��������ʼ��Char�������ʱ������Ҫ��1
				codeBuffer.append(String.format(PARAM_INIT_STR,param.getId(),length));
				defineHelper.addInit(IParamDefineHelper.STD, param.getId());
			}
			else
			{
				if (TypeRule.typeRuleInt(dataType)) {
					codeBuffer.append(String.format(PARAM_DEFINE_INIT,dataType, param.getId(),"Int"));
					defineHelper.addInit(IParamDefineHelper.STD, param.getId());
				} else if (TypeRule.typeRuleDouble(dataType)) {
					codeBuffer.append(String.format(PARAM_DEFINE_INIT,dataType,param.getId(), "Double"));
					defineHelper.addInit(IParamDefineHelper.STD, param.getId());
				}else if (TypeRule.typeRuleClob(dataType)) {
					codeBuffer.append(getClobParamDefineInitCodeStr(param.getId()));
					defineHelper.addInit(IParamDefineHelper.STD, param.getId());
				}else if (TypeRule.typeRulePacker(dataType)) {
					codeBuffer.append(String.format(PARAM_DEFIN_UNPACKER,param.getId() ));
					defineHelper.addInit(IParamDefineHelper.STD, param.getId());
				}else {
					ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
					String message = String.format(String.format("�������[%1$s]��Ӧ����������[%2$s]û�ж�Ӧ�ĳ�ʼ����ʽ��",param.getId(),dataType));
					manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
					return "";//��׼����û�ж�Ӧ�ĳ�ʼ����ʽʱ���������ɣ���������д�����Ϣ���׳���
				}
			}
		}
		else if(param.getParamType() == ParamType.OBJECT)//�������
		{
			ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
			
			if(defineHelper.canInit(IParamDefineHelper.RECORD_OBJECT, param.getId())){
				codeBuffer.append(getObjectParamDefineInitCodeStr(param.getId(),context,param.getId(),true));
				defineHelper.addInit(IParamDefineHelper.RECORD_OBJECT, param.getId());
				helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_IN_OBJECT_INIT_VARIABLE_LIST, param.getId());
				List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
				popVarList.remove(param.getId());
			}
		}
		else if(param.getParamType() == ParamType.PARAM_GROUP)//������
		{
			String path = param.getType();
			codeBuffer.append("//��ʼ��������������("+path+")"+"\r\n");
			List<Parameter> stdParameters = new ArrayList<Parameter>();
			parserParamGroup(param,stdParameters,true,1,context);
			for(Parameter stdParameter:stdParameters){
				codeBuffer.append(getInParameterDefineInitCodeBuffer(stdParameter,project));	
			}
			codeBuffer.append("\r\n");
		}else{
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("����[%1$s]��Ӧ�Ĳ�������δ֪��", param.getId());
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			return "";//��������δ֪ʱ���������ɣ���������д�����Ϣ���׳���
		}
		return codeBuffer.toString();
	}

	/**
	 * ��ȡ�������������䣬�����׼�ֶβ�����Ǳ���������������
	 * �����ڲ���������ʱ����׼�ֶα�����������Ǳ�������巽ʽ������������巽ʽһ�£��ʿɸ��ô˷�����������������ڲ������Ķ������
	 * ���������������ʱ���ַ������͵���������Ķ��巽ʽ������������巽ʽһ�£��ʿɸ��ô˷�������ַ���������������Ķ������
	 * @param param 
	 * 1���������
	 * 2���ڲ���������ʱ����׼�ֶα�����������Ǳ����
	 * 3�������������ʱ���ַ������͵��������
	 * @param project ����
	 * @return String ��������������
	 */
	private String getOutParameterDefineCodeBuffer(Parameter param,IARESProject project){
		StringBuffer codeBuffer = new StringBuffer();
		if((param.getParamType() == ParamType.STD_FIELD) || (param.getParamType() == ParamType.NON_STD_FIELD))//��׼�ֶβ�����Ǳ����
		{
			String bizTypeName = "";
			if(param.getParamType() == ParamType.STD_FIELD)//��׼�ֶβ���
			{
				StandardField stdfield = MetadataServiceProvider.getStandardFieldByNameNoExp(project, param.getId());//getIdΪ��������getNameΪ������
				if(stdfield == null){
					ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
					String message = String.format("����[%1$s]��Ӧ�ı�׼�ֶβ����ڡ�", param.getId());
					manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
					return "";//��׼�ֶβ�����ʱ���������ɣ���������д�����Ϣ���׳���
				}
				bizTypeName = stdfield.getDataType();//��׼�ֶ�ʱ��ȡ��׼�ֶζ�Ӧҵ������
			}else if(param.getParamType() == ParamType.NON_STD_FIELD){//�Ǳ����
				bizTypeName = param.getType();//�Ǳ��ֶ�ʱ��ȡ������ֱ�������ҵ������
			}
			int length = 0;
			BusinessDataType bizType = MetadataServiceProvider.getBusinessDataTypeByNameNoExp(project, bizTypeName);//���ﲻ��ʹ��param.getType()�����������Ǳ�ʱ��ȡ����ҵ�����͵��쳣
			if(bizType == null){
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("����[%1$s]��Ӧ��ҵ������[%2$s]�����ڡ�", param.getId(),bizTypeName);
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				return "";//ҵ�����Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
			}
			try {
				length = Integer.parseInt(bizType.getLength()) + 1;//����Char�������ʱ������Ҫ��1
			} catch (Exception e) {
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("����[%1$s]��Ӧ��ҵ������:%2$s�ĳ���Ϊ�Ƿ����֣�%3$s��", param.getId(),bizTypeName,bizType.getLength());
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				return "";//ҵ�����ͳ���Ϊ�Ƿ�����ʱ���������ɣ���������д�����Ϣ���׳���
			}
			StandardDataType stdType = MetadataServiceProvider.getStandardDataTypeByNameNoExp(project, bizType.getStdType());
			if(stdType == null){
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("����[%1$s]��Ӧ��ҵ������[%2$s]�еı�׼����[%3$s]�����ڡ�", param.getId(),bizTypeName,bizType.getStdType());
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				return "";//��׼���Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
			}
			String dataType = StringUtils.defaultIfBlank(stdType.getValue(MetadataServiceProvider.C_TYPE), "");
			dataType = dataType.replace("$L", length + "");
			if(TypeRule.typeRuleCharArray(dataType)){//�ַ�����������
				codeBuffer.append(String.format(PARAM_DEFINE_STR,param.getId(),length));
			}else{
				//��������ж�Ӧ��Ĭ��ֵ��Ϊ�գ���Ĭ��ֵ�Ը�ֵΪ׼��ע����������ʹ�ñ�׼Ĭ��ֵ��ͬʱҲ��������ʵĬ��ֵ��
				if(StringUtils.isNotEmpty(param.getDefaultValue())){
					TypeDefaultValue typpeDefValue = MetadataServiceProvider.getTypeDefaultValueByNameNoExp(project, param.getDefaultValue());
					//����Ҳ�����׼Ĭ��ֵ����ͳһ����ʵĬ��ֵ�����û�����ʲô�������ʲô
					if(typpeDefValue == null){
						codeBuffer.append(String.format(PARAM_DEFINE,dataType,param.getId(),param.getDefaultValue()));
					}else{
						String defValue = typpeDefValue.getValue(MetadataServiceProvider.C_TYPE);
						codeBuffer.append(String.format(PARAM_DEFINE,dataType,param.getId(),defValue));
					}
				}
				//������Ĭ��ֵΪ�գ�ȡҵ�����Ͷ�Ӧ�ı�׼Ĭ��ֵ����������Ǳ�׼Ĭ��ֵ��������Ҫ����
				else{
					TypeDefaultValue typpeDefValue = MetadataServiceProvider.getTypeDefaultValueByNameNoExp(project, bizType.getDefaultValue());
					if(typpeDefValue == null){
						ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
						String message = String.format("����[%1$s]��Ӧ��ҵ������[%2$s]�еı�׼Ĭ��ֵ[%3$s]�����ڡ�", param.getId(), bizTypeName,bizType.getDefaultValue());
						manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
						return "";//��׼Ĭ��ֵ������ʱ���������ɣ���������д�����Ϣ���׳���
					}
					String defValue = typpeDefValue.getValue(MetadataServiceProvider.C_TYPE);
					codeBuffer.append(String.format(PARAM_DEFINE,dataType,param.getId(),defValue));
				}
			}
		}
		else if(param.getParamType() == ParamType.OBJECT)//�������
		{
			ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
			IParamDefineHelper defineHelper = (IParamDefineHelper)context.get(IAtomEngineContextConstantMySQL.PARAM_DEFINE_HELPER);
			if(defineHelper.canInit(IParamDefineHelper.RECORD_OBJECT, param.getId())){
				codeBuffer.append(getOutObjectParamDefineInitCodeStr(param.getId(),context,param.getId()));
				defineHelper.addInit(IParamDefineHelper.RECORD_OBJECT, param.getId());
				helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_OUT_OBJECT_INIT_VARIABLE_LIST, param.getId());
				List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
				List<String> pseudoObjectParaList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_OBJECT_PARA_LIST);
				pseudoObjectParaList.add(param.getId());
				popVarList.remove(param.getId());
			}
		}
		else if(param.getParamType() == ParamType.PARAM_GROUP)//������
		{
			IParamDefineHelper defineHelper = (IParamDefineHelper)context.get(IAtomEngineContextConstantMySQL.PARAM_DEFINE_HELPER);
			List<Parameter> stdParameters = new ArrayList<Parameter>();
			parserParamGroup(param, stdParameters, false,1, context);
			for (Parameter stdParameter : stdParameters) {
				if (defineHelper.canInit(IParamDefineHelper.STD,stdParameter.getId())) {
					String varDefine = getOutParameterDefineCodeBuffer(stdParameter, project);
					if(StringUtils.isNotBlank(varDefine)){
						codeBuffer.append(varDefine);
						defineHelper.addInit(IParamDefineHelper.STD,stdParameter.getId());
					}
				}
			}
			codeBuffer.append("\r\n");
		}else{
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("����[%1$s]��Ӧ�Ĳ�������δ֪��", param.getId());
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			return "";//��������δ֪ʱ���������ɣ���������д�����Ϣ���׳���
		}
		return codeBuffer.toString();
	}
	
	/**
	 * ��ȡ�ڲ�����������䣬�����׼�ֶα�����Ǳ������ע�����ﲻ��������������������
	 * @param param �ڲ�����
	 * @param project ����
	 * @return String �ڲ������������
	 */
	private String getVarDefineCodeBuffer(InternalParam var,IARESProject project){
		StringBuffer codeBuffer = new StringBuffer();
		if(var != null){
			if(var.getParamType().getValue() == ParamType.NON_STD_FIELD_VALUE){
				//����ڲ������Ƕ�ά������
				if(InternalParameterArrayHelper.isArrayParameter(var)){
					String bizTypeName = InternalParameterArrayHelper.getArrayBusType(var);//ȥ������
					int length = 0;
					BusinessDataType bizType = MetadataServiceProvider.getBusinessDataTypeByNameNoExp(project, bizTypeName);//������ȥ�����Ⱥ��ҵ������
					if(bizType == null){
						ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
						String message = String.format("�ڲ�����[%1$s]��Ӧ��ҵ������[%2$s]�����ڡ�", var.getId(),bizTypeName);
						manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
						return "";//ҵ�����Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
					}
					try {
						length = Integer.parseInt(bizType.getLength()) + 1;//����Char�������ʱ������Ҫ��1
					} catch (Exception e) {
						ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
						String message = String.format("�ڲ�[%1$s]��Ӧ��ҵ������:%2$s�ĳ���Ϊ�Ƿ����֣�%3$s��", var.getId(),bizTypeName,bizType.getLength());
						manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
						return "";//ҵ�����ͳ���Ϊ�Ƿ�����ʱ���������ɣ���������д�����Ϣ���׳���
					}
					StandardDataType stdType = MetadataServiceProvider.getStandardDataTypeByNameNoExp(project, bizType.getStdType());
					if(stdType == null){
						ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
						String message = String.format("�ڲ�����[%1$s]��Ӧ��ҵ������[%2$s]�еı�׼����[%3$s]�����ڡ�", var.getId(),bizTypeName,bizType.getStdType());
						manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
						return "";//��׼���Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
					}
					String dataType = StringUtils.defaultIfBlank(stdType.getValue(MetadataServiceProvider.C_TYPE), "");
					boolean hasTwoArray = false;//�Ƿ�����Ƕ�ά����
					if(StringUtils.indexOf(dataType, "$L")>-1){//�����$L��ô�ɶ���ɶ�������
						hasTwoArray = true;
					}else {
						if(TypeRule.typeRuleChar(dataType)){//�����ַ�Ҫ���⴦��
							length = 2;
							hasTwoArray = true;
						}
					}
					dataType = dataType.replace("$L", length + "");
					IParamDefineHelper defineHelper = (IParamDefineHelper)context.get(IAtomEngineContextConstantMySQL.PARAM_DEFINE_HELPER);
					if(defineHelper.canInit(IParamDefineHelper.STD, var.getId())){
					    if(hasTwoArray){
					    	 codeBuffer.append(String.format(PARAM_DEFINE_ARRAY2,InternalParameterArrayHelper.getArrayDataType(dataType),var.getId(),InternalParameterArrayHelper.getArrayLength(var),length));
							 defineHelper.addInit(IParamDefineHelper.STD, var.getId());
							 codeBuffer.append(String.format(PARAM_DEFINE_INIT_ARRAY2,var.getId(),InternalParameterArrayHelper.getArrayDataType(dataType),InternalParameterArrayHelper.getArrayLength(var),length)); 
							
					    }else{
					    	 codeBuffer.append(String.format(PARAM_DEFINE_ARRAY1,InternalParameterArrayHelper.getArrayDataType(dataType),var.getId(),InternalParameterArrayHelper.getArrayLength(var)));
					    	 defineHelper.addInit(IParamDefineHelper.STD, var.getId());
					    	 codeBuffer.append(String.format(PARAM_DEFINE_INIT_ARRAY1,var.getId(),InternalParameterArrayHelper.getArrayDataType(dataType),InternalParameterArrayHelper.getArrayLength(var))); 
						}
					}
				}else{
					//����ǷǱ�����ҷ����飬��������ʽ������������巽ʽһ��
					codeBuffer.append(getOutParameterDefineCodeBuffer(var,project));
				}
			}
			else if(var.getParamType().getValue() == ParamType.STD_FIELD_VALUE){
				//����Ǳ�׼�ֶΣ���������ʽ������������巽ʽһ��
				codeBuffer.append(getOutParameterDefineCodeBuffer(var,project));
			}
		}
		return codeBuffer.toString();
	}
	
	/**
	 * ��ȡα�������������䣬ֻ�����׼�ֶΣ�����ȥ��_cur���α������
	 * @param fieldName ��׼�ֶ�����
	 * @param project ����
	 * @param suffix ������׺��һ�������Ϊ�գ�������_cur�α����ʱ������Ҫ����
	 * @return String α��������������
	 */
	private String getPopVarDefineCodeBuffer(String fieldName,IARESProject project,String suffix){
		StringBuffer codeBuffer = new StringBuffer();
		StandardField stdfield = MetadataServiceProvider.getStandardFieldByNameNoExp(project, fieldName);//α��������������Ǳ�׼�ֶ�
		if(stdfield == null){
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("α�������[%1$s]��Ӧ�ı�׼�ֶβ����ڡ�", fieldName);
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			return "";//��׼�ֶβ�����ʱ���������ɣ���������д�����Ϣ���׳���
		}
		int length = 0;
		BusinessDataType bizType = MetadataServiceProvider.getBusinessDataTypeByNameNoExp(project, stdfield.getDataType());
		if(bizType == null){
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("α�������[%1$s]��Ӧ��ҵ������[%2$s]�����ڡ�", fieldName,stdfield.getDataType());
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			return "";//ҵ�����Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
		}
		try {
			length = Integer.parseInt(bizType.getLength()) + 1;//����Char�������ʱ������Ҫ��1
		} catch (Exception e) {
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("α�������[%1$s]��Ӧ��ҵ������:%2$s�ĳ���Ϊ�Ƿ����֣�%3$s��", fieldName,stdfield.getDataType(),bizType.getLength());
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			return "";//ҵ�����ͳ���Ϊ�Ƿ�����ʱ���������ɣ���������д�����Ϣ���׳���
		}
		StandardDataType stdType = MetadataServiceProvider.getStandardDataTypeByNameNoExp(project, bizType.getStdType());
		if(stdType == null){
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("α�������[%1$s]��Ӧ��ҵ������[%2$s]�еı�׼����[%3$s]�����ڡ�", fieldName,stdfield.getDataType(),bizType.getStdType());
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			return "";//��׼���Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
		}
		String dataType = StringUtils.defaultIfBlank(stdType.getValue(MetadataServiceProvider.C_TYPE), "");
		dataType = dataType.replace("$L", length + "");
		if(TypeRule.typeRuleCharArray(dataType)){//�ַ�����������
			codeBuffer.append(String.format(PARAM_DEFINE_STR,fieldName + suffix,length));
		}else{
			TypeDefaultValue typpeDefValue = MetadataServiceProvider.getTypeDefaultValueByNameNoExp(project, bizType.getDefaultValue());
			if(typpeDefValue == null){
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("����[%1$s]��Ӧ��ҵ������[%2$s]�еı�׼Ĭ��ֵ[%3$s]�����ڡ�", fieldName, stdfield.getDataType(),bizType.getDefaultValue());
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				return "";//��׼Ĭ��ֵ������ʱ���������ɣ���������д�����Ϣ���׳���
			}
			String defValue = typpeDefValue.getValue(MetadataServiceProvider.C_TYPE);
			codeBuffer.append(String.format(PARAM_DEFINE,dataType,fieldName + suffix,defValue));
		}
	
		return codeBuffer.toString();
	}
	
	/**
	 * �������ʼ��
	 * @param paramName
	 * @param context
	 * @param prefix
	 * @return
	 */
	private String getObjectParamDefineInitCodeStr(String paramName,Map<Object, Object> context,String prefix,boolean isInitObject){
		StringBuffer codeBuffer = new StringBuffer();
	
		codeBuffer.append("int pi_"+paramName+ " = 0;").append("\r\n");
		if(isInitObject){
			codeBuffer.append(String.format("void * %1$s = lpInUnPacker->GetRaw(%2$s,&%3$s);\r\n","p_"+paramName, "\""+paramName+"\"","pi_"+paramName));
			codeBuffer.append(String.format("IF2UnPacker * %1$s = lpPackService->GetUnPacker(%2$s,%3$s);\r\n", "v_"+prefix+"ResultSet","p_"+paramName,"pi_"+paramName));
		}else{
			codeBuffer.append(String.format("void * %1$s = NULL;\r\n","p_"+paramName));
			codeBuffer.append(String.format("IF2UnPacker * %1$s = NULL;\r\n", "v_"+prefix+"ResultSet"));
		}
		
		return codeBuffer.toString();
	}
	
	/**
	 * �������ʼ��(�������)
	 * @param paramName
	 * @param context
	 * @param prefix
	 * @return
	 */
	private String getOutObjectParamDefineInitCodeStr(String paramName,Map<Object, Object> context,String prefix){
		StringBuffer codeBuffer = new StringBuffer();
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		codeBuffer.append("int pi_"+paramName+ " = 16*1024*1024;").append("\r\n");
		codeBuffer.append(String.format("void * %1$s = NULL;\r\n","p_"+paramName));
		codeBuffer.append(String.format("IF2Packer * %1$s = lpPackService->GetPacker(2);\r\n","v_"+prefix+"ResultSet"));
		
		return codeBuffer.toString();
	}
	
	
	
	private String getClobParamDefineInitCodeStr(String paramName){
		StringBuffer codeBuffer = new StringBuffer();
		codeBuffer.append("int pi_"+paramName+ " = 0;").append("\r\n");
		codeBuffer.append(String.format("void * %1$s = lpInUnPacker->GetRaw(%2$s,&%3$s);\r\n","p_"+paramName, "\""+paramName+"\"","pi_"+paramName));
		
		return codeBuffer.toString();
	}
	
	
	private Parameter containsKey(String key,List params,Map<Object, Object> context){
		IARESProject project = (IARESProject) context.get(IAtomEngineContextConstantMySQL.Aresproject);
		for(int i = 0;i < params.size();i++){
			Parameter p = (Parameter) params.get(i);
			if( p.getParamType().getValue()==ParamType.PARAM_GROUP_VALUE ){//��������ǲ�����
				List<Parameter> parameters = new ArrayList<Parameter>();
				ParamGroupUtil.parserParameters(params, parameters, project);
				for(Parameter pg:parameters){
					if((pg != null) && (pg.getId() != null) && (pg.getId() != "") && pg.getId().equalsIgnoreCase(key)){
						return pg;
					}
				}

			}
			else if((p != null) && (p.getId() != null) && (p.getId() != "") && p.getId().equalsIgnoreCase(key)){
				return p;
			}
		}
		return null;
	}
	
	private Parameter containsKey(String key,List params){
		for(int i = 0;i < params.size();i++){
			Parameter p = (Parameter) params.get(i);
			if((p != null) && (p.getId() != null) && (p.getId() != "") && p.getId().equalsIgnoreCase(key)){
				return p;
			}
		}
		return null;
	}
	/**
	 * �ݹ���������
	 * @param gruopParam
	 * @param retParameter
	 * @param isInParameter
	 * @param context
	 */
	private void parserParamGroup( Parameter gruopParam,List<Parameter> retParameter,boolean isInParameter,int callDepth,Map<Object, Object> context){
		String path = gruopParam.getType();
		IARESProject project = (IARESProject) context.get(IAtomEngineContextConstantMySQL.Aresproject);
		try {
			IARESResource objectRes = project.findResource(path, "object");
			ARESObject aresObject = objectRes.getInfo(ARESObject.class);
			List<Parameter> properties = aresObject.getProperties();
			for(Parameter parameter:properties){
				if( parameter.getParamType().getValue()==ParamType.PARAM_GROUP_VALUE ){//����ǲ�������ݹ����
					if(callDepth<=5){
						parserParamGroup(parameter,retParameter,isInParameter,callDepth+1,context);
					}
					
				}else if( parameter.getParamType().getValue()==ParamType.OBJECT_VALUE && isInParameter){
					ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
					 helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_IN_OBJECT_INIT_VARIABLE_LIST,parameter.getId());
				}else if( parameter.getParamType().getValue()==ParamType.OBJECT_VALUE && !isInParameter){
					ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
					 helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_OUT_OBJECT_INIT_VARIABLE_LIST,parameter.getId());
				}else if( parameter.getParamType().getValue()==ParamType.STD_FIELD_VALUE ){
					retParameter.add(parameter);
				}
			}
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
		
	}
	

	
	//proc���������а���iReturnCode
	private final static String DECLARE_SECTION_BEGIN_IRETURNCODE
			=
			"EXEC SQL BEGIN DECLARE SECTION;" + ITokenConstant.NL +
			"sql_context ctx = NULL;" + ITokenConstant.NL +
			"int iReturnCode = 0;" + ITokenConstant.NL;
	//proc���������в�����iReturnCode
	private final static String DECLARE_SECTION_BEGIN 
	=
	"int iReturnCode = 0;" + ITokenConstant.NL +
	"EXEC SQL BEGIN DECLARE SECTION;" + ITokenConstant.NL + 
	"sql_context ctx = NULL;" + ITokenConstant.NL;
	
	private final static String DECLARE_SECTION_END
			=
			"EXEC SQL END DECLARE SECTION;" + ITokenConstant.NL;
	private final static String STRUCT_SQLCA = "struct sqlca sqlca;" + ITokenConstant.NL;
	
	private final static String PARAM_DEFINE = "%1$s @%2$s = %3$s;" + ITokenConstant.NL;
	
	private final static String PARAM_INIT = "@%2$s = lpInUnPacker->Get%1$s(\"%2$s\");" + ITokenConstant.NL;
	
	private final static String PARAM_INIT_CHAR = "@%1$s = conversion(lpInUnPacker->GetChar(\"%1$s\"));" + ITokenConstant.NL;
	
	private final static String PARAM_INIT_STR = "hs_strncpy(@%1$s,conversion((char *)lpInUnPacker->GetStr(\"%1$s\")),%2$s);" + ITokenConstant.NL;
	
	private final static String PARAM_DEFINE_STR = "char @%1$s[%2$s] = {0};" + ITokenConstant.NL;
	private final static String PARAM_DEFINE_ARRAY2 = "%1$s @%2$s[%3$s][%4$s] = {0};" + ITokenConstant.NL;//��ά���鶨��
	private final static String PARAM_DEFINE_INIT_ARRAY2 = "memset(@%1$s,0,sizeof(%2$s)*%3$s*%4$s);" + ITokenConstant.NL;//��ά�����ʼ��
	
	private final static String PARAM_DEFINE_ARRAY1 = "%1$s @%2$s[%3$s] = {0};" + ITokenConstant.NL;//-ά���鶨��
	private final static String PARAM_DEFINE_INIT_ARRAY1 = "memset(@%1$s,0,sizeof(%2$s)*%3$s);" + ITokenConstant.NL;//-ά�����ʼ��
	
	private final static String PARAM_DEFINE_INIT_CHAR = "char @%1$s = conversion(lpInUnPacker->GetChar(\"%1$s\"));" + ITokenConstant.NL;

	private final static String PARAM_DEFINE_INIT = "%1$s @%2$s = lpInUnPacker->Get%3$s(\"%2$s\");" + ITokenConstant.NL;
	private final static String PARAM_DEFINE_OBJECT_INIT = "%1$s @%2$s = %4$s->Get%3$s(\"%2$s\");" + ITokenConstant.NL;
	private final static String PARAM_DEFINE_INIT_OBJECT_CHAR = "@%1$s = conversion(%2$s->GetChar(\"%1$s\"));" + ITokenConstant.NL;
	private final static String PARAM_INIT_OBJECT_STR = "hs_strncpy(@%1$s,conversion((char *)%3$s->GetStr(\"%1$s\")),%2$s);" + ITokenConstant.NL;
	private final static String PARAM_DEFIN_UNPACKER = "IF2UnPacker * %1$s = NULL;"+ITokenConstant.NL;
	//private boolean isDefineLpPackService = false;//�Ƿ���clob����
	
	private final static String iReturnCode_DEFINE_STR = "int iReturnCode = 0;" + ITokenConstant.NL;//iReturnCodeͳһ��FunctionParamDefineToken�д���
	
}
