/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.oracle.token;

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
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.UserExtensibleProperty;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.constant.MarkConfig;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procedure.compiler.oracle.constant.IProcedureEngineContextConstantOracle;
import com.hundsun.ares.studio.procedure.compiler.oracle.skeleton.util.ProcedureCompilerUtil;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * 
 * ���̱��������������ڲ������Լ�Ĭ�ϴ��������Լ�α�����еı�����
 * @author liaogc
 *
 */
public class ProcedureVariableDefineToken implements ICodeToken {
	private static String DATA_TYPE_CURSOR="cursor";//�α�����
	private static String BLANK = " ";//�ո�
	private static String NL = ICodeToken.NEWLINE;//����
	private static String COMMENT="--";//ע��
	private final static String PARAM_DEFINE = "\tv_%1$s  hstype.%2$s%3$s;";
	private static  Map<Object, Object> context;
	//private Procedure procedure;//����ģ��
	private boolean notDefineConnectType;

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
		this.context = context;
		   Procedure procedure = (Procedure)context.get(IProcedureEngineContextConstantOracle.ResourceModel);
		this.notDefineConnectType = 
			(Boolean) context.get(IProcedureEngineContextConstantOracle.not_define_connect_type);

		   
		 IARESProject project = (IARESProject)context.get(IProcedureEngineContextConstantOracle.Aresproject);
		 Set<String> parameters = new  HashSet<String>();//��¼�����Ƕ���
		 StringBuffer declareCode = new StringBuffer();
		
		 //�Զ������������Ϊ����
		 boolean autoDefineInputParam = 
				(Boolean) context.get(IProcedureEngineContextConstantOracle.auto_define_input_param);
		 if(autoDefineInputParam){
			 declareCode.append(getAutoDefineInputParameters(parameters, procedure, project));
		 }
		 
		//�ڲ���������
		for(Parameter parameter: procedure.getInternalVariables()){
			try {
				if(!parameters.contains(parameter.getId())){
					parameters.add(parameter.getId());
					String defineStr = getVarDefineCodeStr(procedure ,parameter, project);
					if(StringUtils.isNotBlank(defineStr)){
						String chineseName = null;
						if(parameter.getParamType().getValue()==ParamType.NON_STD_FIELD_VALUE){
							chineseName = parameter.getName();
						}
						if(StringUtils.isBlank(chineseName)){
							chineseName = this.getChineseName(project, parameter.getId());
						}
						 
						declareCode.append(defineStr).append(BLANK).append(COMMENT).append(chineseName).append(NL);
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//������ر�������
	    for(String name:ProcedureCompilerUtil.getErrorVarsName()){
	    	if(!ProcedureCompilerUtil.isParameterINProcedureParameterByName(procedure, name,project) ||
	    			(isOutPutParamWithOutIOFlag(procedure, name, project)	&& procedure.isOutputCollection())){//������������Լ��ڲ�������
	    		//����������в��Ҳ�����IO����ǣ�����Ϊ���������ʱ��Ҳ��Ҫ����
	    		if(!parameters.contains(name)){
	    			parameters.add(name);
	    			try{
	    				//������ر���δ����ɱ�׼�ֶ�ʱ��ʹ��Լ����Ĭ������
	    				ReferenceInfo stdField = ReferenceManager.getInstance().getFirstReferenceInfo(project, IMetadataRefType.StdField,name, false);
	    				if(null == stdField) {
	    					if(notDefineConnectType) {
	    						declareCode.append(String.format("v_%1$s  %2$s%3$s;", name,ProcedureCompilerUtil.getErrorVarDefaultType(name),StringUtils.EMPTY));
	    					}else{
	    						declareCode.append(String.format("v_%1$s  %2$s%3$s;", name,ProcedureCompilerUtil.getErrorVarDefaultType(name),"%type"));
	    					}
	    				}else {
	    					String defineStr = getVarDefineCodeStr(name, project);
	    					if(StringUtils.isNotBlank(defineStr)){
	    						String chineseName = this.getChineseName(project, name);
	    						declareCode.append(defineStr).append(BLANK).append(COMMENT).append(chineseName).append(NL);
	    					}
	    				}
					}
		    		catch(Exception e){
		    			e.printStackTrace();
		    		}
    			}
	    	}
	    }
		
		//α�����������
		  @SuppressWarnings("unchecked")
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);//ȡ��α��������б�
			for(int i = 0;i < popVarList.size();i++){
				String fieldName = popVarList.get(i);
				//�ж��Ƿ�Ϊ��׼�ֶ�
				ReferenceInfo std = ReferenceManager.getInstance().getFirstReferenceInfo(project, IMetadataRefType.StdField, fieldName, true);
				if(null == std) {
					continue;
				}
				
				if(!parameters.contains(fieldName)){
					parameters.add(fieldName);
					try{
						if(!ProcedureCompilerUtil.isParameterINProcedureParameterByName(procedure, fieldName,project)){
							String defineStr = getVarDefineCodeStr(fieldName,project);
							if(StringUtils.isNotBlank(defineStr)){
								String chineseName = this.getChineseName(project, fieldName);
								declareCode.append(defineStr).append(BLANK).append(COMMENT).append(chineseName).append(NL);
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			//��error_pathinfo_tmp�����⴦��
			if(!parameters.contains("error_pathinfo_tmp")){
				declareCode.append(defineError_pathinfo_tmp());
			}
		return declareCode.toString();
		
	}
	
	/**
	 * �������ƵĲ����Ƿ�Ϊ��IO��־���������
	 * @param procedure
	 * @param name
	 * @param project
	 * @return
	 */
	private boolean isOutPutParamWithOutIOFlag(Procedure procedure, String name,
			IARESProject project) {
		for(Parameter p : procedure.getOutputParameters()) {
			if(StringUtils.equalsIgnoreCase(p.getId(), name) && !StringUtils.equalsIgnoreCase(p.getFlags(), "IO")){
				return true;
			}
		}
		return false;
	}

	//�Զ�����char�����������
	private final String auto_define_input_param_char = "\tv_%1$s %2$s hstype.%3$s   := nvl(trim(p_%1$s),%4$s);  ";
	//�Զ������char�����������
	private final String auto_define_input_param = "\tv_%1$s %2$s hstype.%3$s   := nvl(p_%1$s,%4$s);  ";
	
	/**
	 * �Զ������������������ΪV����
	 * @param parameters
	 * @param procedure
	 * @param project
	 * @return
	 * @throws Exception
	 */
	private String getAutoDefineInputParameters(Set<String> parameters,Procedure procedure,IARESProject project) throws Exception{
		
		StringBuffer ret = new StringBuffer();
		
		List<Parameter> totleInputParam = new ArrayList<Parameter>();
		ProcedureCompilerUtil.getTotleParameters(project ,procedure.getInputParameters() ,totleInputParam);
		for (Parameter parameter : totleInputParam) {
			String name = parameter.getId();
			if(!parameters.contains(name)){
				parameters.add(name);
				
				String realType = StringUtils.EMPTY;

				String IOFlag = "";
				if(null != parameter.getFlags() && parameter.getFlags().indexOf("IO") != -1) {
					IOFlag = "  in  out ";
				}
				//��������
				String dataType = "";
				if(parameter.getParamType().getValue() == ParamType.NON_STD_FIELD_VALUE){//����Ǳ�׼�ֶ�
					dataType = parameter.getType();
				}else if(parameter.getParamType().getValue() == ParamType.STD_FIELD_VALUE){
					BusinessDataType businessDataType = null;
					try {
						businessDataType = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(project, parameter.getId());
					} catch (Exception e) {
						ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
						String message = String.format("����[%1s]]���ҵ�����ͳ���.����ԭ��:�ò���Ϊ��׼�ֶβ���,���ǻ�ȡ��Ӧ��ҵ������ʱ����",parameter.getId());
						manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
					}
					if(businessDataType!=null){
						dataType= businessDataType.getName();
						StandardDataType stdType = MetadataServiceProvider.getStandardDataTypeOfBizTypeByName(project, dataType);
						if(null != stdType) {
							realType = stdType.getValue(ProcedureCompilerUtil.getDatabaseType(project));
						}
					}
				}
				
				if(StringUtils.containsIgnoreCase(realType, "char")){
					ret.append(String.format(auto_define_input_param_char, name,IOFlag,
							dataType,ProcedureCompilerUtil.getParameterDefaultValue(parameter, project)));
				}else {
					ret.append(String.format(auto_define_input_param, name,IOFlag,
							dataType,ProcedureCompilerUtil.getParameterDefaultValue(parameter, project)));
				}
				
				ret.append(COMMENT);
				if(StringUtils.isNotBlank(parameter.getComments())){
					ret.append(parameter.getComments());
				}else {
					ret.append(getChineseName(project, name));
				}
				ret.append(NEWLINE);
			}
		}
		
		return ret.toString();
		
	}
	
	/**
	 * ����һ���ڲ������Ķ���
	 * @param parameter ����
	 * @param project ��ǰ��Ŀ
	 * @return
	 */
	
	private String getVarDefineCodeStr(Procedure procedure , Parameter parameter,IARESProject project) throws Exception{
		StringBuffer codeBuffer = new StringBuffer();

		if(parameter.getParamType().getValue() == ParamType.NON_STD_FIELD_VALUE){//����Ǳ�׼�ֶ�

			String dataType = parameter.getType();
			if(StringUtils.isNotBlank(dataType)){
				if(StringUtils.equals(DATA_TYPE_CURSOR, dataType)){//�����α�(���������ʱ�Ȳ�����)
					codeBuffer.append(String.format(PARAM_DEFINE, parameter.getId(),dataType,StringUtils.EMPTY));
				}else{//�������
					if(notDefineConnectType) {
						codeBuffer.append(String.format(PARAM_DEFINE, parameter.getId(),dataType,StringUtils.EMPTY));
					}else {
						codeBuffer.append(String.format(PARAM_DEFINE, parameter.getId(),dataType,"%type"));
					}
				}
				
			}
		}else if(parameter.getParamType().getValue() == ParamType.STD_FIELD_VALUE){
			//����Ǳ�׼�ֶ�
			BusinessDataType businessDataType = null;
			try {
				businessDataType = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(project, parameter.getId());
			} catch (Exception e) {
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("����[%1s]]���ҵ�����ͳ���.����ԭ��:�ò���Ϊ��׼�ֶβ���,���ǻ�ȡ��Ӧ��ҵ������ʱ����",parameter.getId());
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			}
			if(businessDataType!=null){
				String dataType= businessDataType.getName();
				
				String realType = StringUtils.EMPTY;
				StandardDataType stdType = MetadataServiceProvider.getStandardDataTypeOfBizTypeByName(project, dataType);
				if(null != stdType) {
					realType = stdType.getValue("oracle");
				}
				
				if(StringUtils.equals(DATA_TYPE_CURSOR, realType)){//�����α�(���������ʱ�Ȳ�����)
					codeBuffer.append(String.format(PARAM_DEFINE, parameter.getId(),dataType,StringUtils.EMPTY));
				}else{//�������
					if(notDefineConnectType) {
						codeBuffer.append(String.format(PARAM_DEFINE, parameter.getId(),dataType,StringUtils.EMPTY));
					}else {
						codeBuffer.append(String.format(PARAM_DEFINE, parameter.getId(),dataType,"%type"));
					}
				}
			}
		}else if(parameter.getParamType().getValue() == ParamType.OBJECT_VALUE) {
			//2013��7��23��9:04:44 ��������
			String paramType =  parameter.getType();
			String paramTypeShortName;
			if(StringUtils.contains(paramType, ".")){
				paramTypeShortName = paramType.substring(paramType.lastIndexOf(".")+1);
			}else {
				paramTypeShortName = paramType;
			}
			ReferenceInfo ref = ReferenceManager.getInstance().getFirstReferenceInfo(project, "jres.biz.object", paramType, true);
			if(ref != null){
				codeBuffer.append(parseObjectType(project,procedure ,ref, paramTypeShortName));
			}
			
			codeBuffer.append("\tv_" + parameter.getId() + "  " + paramTypeShortName + ";");
		}
		return codeBuffer.toString();
	}
	
	/**
	 * ������������
	 * @param obj
	 * @return
	 */
	private String parseObjectType(IARESProject project,Procedure procedure , ReferenceInfo ref,String paramTypeShortName) {
		StringBuffer ret = new StringBuffer();
		//�Ƿ�Ϊ����������������
		boolean isRefObject = false;
		
		IARESResource res = ref.getResource();
		System.out.println("����ģ�����ƣ�" + res.getParent().getElementName());
		ARESObject obj =  (ARESObject)ref.getObject();
		UserExtensibleProperty userExtend = (UserExtensibleProperty)obj.getData2().get("user");//�û���չ����
		String objType = userExtend.getMap().get("type");
		//��¼���Ͳ���Ҫ����������������object�Ȳ���Ҫ
		if(StringUtils.equalsIgnoreCase(objType, "record")){
			boolean status = isPackage(procedure, obj);
			if (status) {
				ret.append("\tv_" + paramTypeShortName + " " + ref.getResource().getModule().getShortName() + "." + paramTypeShortName + ";");
			}else {
				ret.append("\ttype " + paramTypeShortName + " is RECORD(");
			}
			ret.append("\r\n");
			// ������������
			StringBuffer paramBuffer = new StringBuffer();
			EList<Parameter> props = obj.getProperties();
			for (Parameter prop : props) {
				if(prop.getParamType().getValue() == ParamType.OBJECT_VALUE){
					isRefObject = true;
					break;
				}else if(prop.getParamType().getValue() == ParamType.STD_FIELD_VALUE && !status){
					BusinessDataType businessDataType = null;
					try {
						businessDataType = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(project, prop.getId());
					} catch (Exception e) {
						ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
						String message = String.format("����[%1s]]���ҵ�����ͳ���.����ԭ��:�ò���Ϊ��׼�ֶβ���,���ǻ�ȡ��Ӧ��ҵ������ʱ����",prop.getId());
						manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
					}
					if(businessDataType!=null){
						paramBuffer.append(String.format("\t  %s  %s.%s, \n", prop.getId(),
								res.getParent().getElementName(),businessDataType.getName()));
					}
				}
			}
			if (!status) {
				//�������Ķ���
				if(StringUtils.lastIndexOf(paramBuffer.toString(), MarkConfig.MARK_COMMA)!=-1){
					int index = StringUtils.lastIndexOf(paramBuffer.toString(), MarkConfig.MARK_COMMA);
					paramBuffer.delete(index, index+1);
				}
				ret.append("\t--- ������������ -- \r\n");
				ret.append(paramBuffer);
				ret.append("\t);\n");
			}
		}
		
		if(isRefObject) {
			return StringUtils.EMPTY;
		}else {
			return ret.toString();
		}
	}
	
	private boolean isPackage(Procedure procedure , ARESObject object){
		if (procedure == null || object == null) {
			return false;
		}
		String commonModule = StringUtils.getCommonPrefix(new String[]{StringUtils.substringBefore(procedure.getFullyQualifiedName(), ".") ,StringUtils.substringBefore(object.getFullyQualifiedName() ,".")});
		if (StringUtils.isNotBlank(commonModule) && StringUtils.indexOf(procedure.getFullyQualifiedName() , commonModule + ".") > -1 && StringUtils.indexOf(object.getFullyQualifiedName() , commonModule + ".") > -1) {
			return true;
		}
		return false;
	}
	
	private String getVarDefineCodeStr(String name,IARESProject project) throws Exception{
		StringBuffer codeBuffer = new StringBuffer();
		
		BusinessDataType businessDataType = null;
		try {
			businessDataType = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(project, name);
		} catch (Exception e) {
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("�ֶ�[%1s]���ҵ�����ͳ���", name);
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
		}
		if(businessDataType!=null){
			String dataType= businessDataType.getName();
			
			String realType = StringUtils.EMPTY;
			StandardDataType stdType = MetadataServiceProvider.getStandardDataTypeOfBizTypeByName(project, dataType);
			if(null != stdType) {
				realType = stdType.getValue("oracle");
			}

			if(StringUtils.equals(DATA_TYPE_CURSOR, realType)){//�����α�(���������ʱ�Ȳ�����)
				codeBuffer.append(String.format(PARAM_DEFINE, name,dataType,StringUtils.EMPTY));
			}else{//�������
				if(notDefineConnectType) {
					codeBuffer.append(String.format(PARAM_DEFINE, name,dataType,StringUtils.EMPTY));
				}else{
					codeBuffer.append(String.format(PARAM_DEFINE, name,dataType,"%type"));
				}
			}
		}
		
		return codeBuffer.toString();
	}
		
	/**
	 * ���ر�׼�ֶε�������
	 * @param project
	 * @param fieldName
	 * @return
	 */
	private String getChineseName(IARESProject project, String fieldName){
		StandardField field=null;
		try {
			field = MetadataServiceProvider.getStandardFieldByName(project, fieldName);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		if(field!=null){
			return StringUtils.defaultIfBlank(field.getChineseName(), StringUtils.EMPTY);
		}else {
			return StringUtils.defaultIfBlank(fieldName, StringUtils.EMPTY);
		}
	}
	/**
	 * errorPathInfo_tem
	 * @return
	 */
	private String defineError_pathinfo_tmp(){
		 IARESProject project = (IARESProject)context.get(IProcedureEngineContextConstantOracle.Aresproject);
		try {
			StandardField error_pathinfoEmpFieldp = null;
			try{
				 error_pathinfoEmpFieldp = MetadataServiceProvider.getStandardFieldByName(project, "error_pathinfo_tmp");
			}catch(Exception e){
				
			}
			
			if(error_pathinfoEmpFieldp!=null){
				BusinessDataType businessDataType = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(project, "error_pathinfo_tmp");
				if(businessDataType!=null){
					String dataType= businessDataType.getName();
					if(notDefineConnectType){
						return "\tv_error_pathinfo_tmp  hstype."+dataType+";  --����·����ʱ����\r\n";
					}else{
						return "\tv_error_pathinfo_tmp  hstype."+dataType+"%type;  --����·����ʱ����\r\n";
					}
					

				}
			}else{
				try{
					StandardField error_pathinfoField = MetadataServiceProvider.getStandardFieldByName(project, "error_pathinfo");
					if(error_pathinfoField!=null){
						BusinessDataType businessDataType = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(project, "error_pathinfo");
						if(businessDataType!=null){
							String dataType= businessDataType.getName();
							if(notDefineConnectType){
								return "\tv_error_pathinfo_tmp  hstype."+dataType+";  --����·����ʱ����\r\n";
							}
							else{
								return "\tv_error_pathinfo_tmp  hstype."+dataType+"%type;  --����·����ʱ����\r\n";
							}

						}
					}
				}catch(Exception e){
					throw new RuntimeException ("error_pathinfo_tmp������Ӧ�����ͻ�ȡ����,��ȷ���Ƿ���error_pathinfo_tmp��׼�ֶλ���error_pathinfo��׼�ֶ�");
				}
				
			}
			
		} catch (Exception e) {
			throw new RuntimeException ("error_pathinfo_tmp������Ӧ�����ͻ�ȡ����,��ȷ���Ƿ���error_pathinfo_tmp��׼�ֶλ���error_pathinfo��׼�ֶ�");
		}
		return "";
		
	}

}
