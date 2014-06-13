/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.oracle.skeleton.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.emf.common.util.EList;

import com.hundsun.ares.studio.biz.ARESObject;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.constants.IBizRefType;
import com.hundsun.ares.studio.biz.util.BizInterfaceParameterUtil;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.internal.core.ARESProjectProperty;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * ���̱��������
 * @author liaogc
 *
 */
public class ProcedureCompilerUtil {
	
	/**
	 * ������ر���
	 * @return
	 */
	public static List<String> getErrorVarsName(){
		List<String> errorVarList = new ArrayList<String>();
			errorVarList.add("error_no");
			errorVarList.add("error_info");
			errorVarList.add("error_id");
			errorVarList.add("error_sysinfo");
			errorVarList.add("error_pathinfo");
		return errorVarList;
	}
	
	/**
	 * ��ȡ������ر���Լ����Ĭ������
	 * @param name ������ر�������
	 * @return
	 */
	public static String getErrorVarDefaultType(String name) {
		if(StringUtils.equalsIgnoreCase(name, "error_no")){
			return "int";
		}else if(StringUtils.equalsIgnoreCase(name, "error_info")){
			return "char[501]";
		}else if(StringUtils.equalsIgnoreCase(name, "error_id")){
			return "int";
		}else if(StringUtils.equalsIgnoreCase(name, "error_sysinfo")){
			return "char[501]";
		}else if(StringUtils.equalsIgnoreCase(name, "error_pathinfo")){
			return "char[501]";
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * ��ȡ������ر���Լ����Ĭ��ֵ
	 * @param name ������ر�������
	 * @return
	 */
	public static String getErrorVarDefaultValue(String name) {
		if(StringUtils.equalsIgnoreCase(name, "error_no")){
			return "0";
		}else if(StringUtils.equalsIgnoreCase(name, "error_info")){
			return "{0}";
		}else if(StringUtils.equalsIgnoreCase(name, "error_id")){
			return "0";
		}else if(StringUtils.equalsIgnoreCase(name, "error_sysinfo")){
			return "{0}";
		}else if(StringUtils.equalsIgnoreCase(name, "error_pathinfo")){
			return "{0}";
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * ��ȡ������ر���Լ����Ĭ�ϳ���
	 * @param name ������ر�������
	 * @return
	 */
	public static int getErrorVarDefaultLength(String name) {
		if(StringUtils.equalsIgnoreCase(name, "error_no")){
			return 0;
		}else if(StringUtils.equalsIgnoreCase(name, "error_info")){
			return 500;
		}else if(StringUtils.equalsIgnoreCase(name, "error_id")){
			return 0;
		}else if(StringUtils.equalsIgnoreCase(name, "error_sysinfo")){
			return 500;
		}else if(StringUtils.equalsIgnoreCase(name, "error_pathinfo")){
			return 500;
		}
		return 0;
	}

	/**
	 * ��ȡ������Ӧoracle��Ĭ��ֵ
	 * @param parameter ��������ҪΪ��׼�ֶ�
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public static String getParameterDefaultValue(Parameter parameter,IARESProject project){
		String value = parameter.getDefaultValue();
		try {
			if(StringUtils.isNotEmpty(value)){
				TypeDefaultValue typeDev = MetadataServiceProvider.getTypeDefaultValueByName(project, value);
				if (typeDev != null) {
					value = typeDev.getValue(ProcedureCompilerUtil.getDatabaseType(project));
				}
				return value;
			}else {
				TypeDefaultValue defaultValue = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName(project, parameter.getId());
				if(null != defaultValue){
					return defaultValue.getValue(getDatabaseType(project));
				}else {
					return "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * �������ܣ����������еĲ������ܵ����������
	 * 
	 * @param project
	 * @param params
	 * @param totleParams
	 */
	public static void getTotleParameters(IARESProject project , EList<Parameter> params , List<Parameter> totleParams) {
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
	
	public static String getDatabaseType(IARESProject project) {
		String dt = StringUtils.EMPTY;
		try {
			dt = (String) ((ARESProjectProperty)project.getProjectProperty()).getProperties().get("tabledir");
			int _index = -1 ;
			int dotIndex = -1;
			if((_index=StringUtils.lastIndexOf(dt,"_" ))>-1  && (dotIndex=StringUtils.lastIndexOf(dt,"."))>-1 ){
				dt = StringUtils.substring(dt,_index+1, dotIndex).toLowerCase();
			}else{
				dt = "oracle";
			}
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
		return dt;
	}
	
	/**
	 * �жϸ����Ĳ������Ƿ��ڹ��̵����������
	 * @param procedure ����
	 * @param parameterName ������
	 * @return
	 */
	public static boolean isParameterINInputParameterByName(Procedure procedure, String parameterName,IARESProject project) {
		return BizInterfaceParameterUtil.isInputParameter(procedure, parameterName,project);
	}
	
	/**
	 * �жϸ����Ĳ������Ƿ��ڹ��̵����������
	 * @param procedure ����
	 * @param parameterName ������
	 * @return
	 */
	public static boolean isParameterINOutputParameterByName(Procedure procedure, String parameterName,IARESProject project) {
		return BizInterfaceParameterUtil.isOutputParameter(procedure, parameterName, project);
	}
	
	/**
	 * �жϸ����Ĳ������Ƿ��ڹ��̵��������������
	 * @param procedure ����
	 * @param parameterName ������
	 * @return
	 */
	public static boolean isParameterINInputAndOutputParameterByName(Procedure procedure, String parameterName,IARESProject project) {
		return isParameterINInputParameterByName(procedure, parameterName,project) ||
				isParameterINOutputParameterByName(procedure, parameterName,project);
	}

	/**
	 * �жϸ����Ĳ������Ƿ��ڹ��̵��ڲ�������
	 * @param procedure ����
	 * @param parameterName ������
	 * @return
	 */
	public static boolean isParameterINInternalVariablesByName(Procedure procedure, String parameterName) {
		List<String> names = new ArrayList<String>();
		for (Parameter para : procedure.getInternalVariables()) {
			names.add(para.getId());
		}
		return names.contains(parameterName);
	}
	
	/**
	 * �жϸ����Ĳ������Ƿ��ڹ��̵���������Լ��ڲ�������
	 * @param procedure ����
	 * @param parameterName ������
	 * @return
	 */
	public static boolean isParameterINProcedureParameterByName(Procedure procedure, String parameterName,IARESProject project) {
		return isParameterINInputParameterByName(procedure, parameterName,project) ||
				isParameterINOutputParameterByName(procedure, parameterName,project) ||
				isParameterINInternalVariablesByName(procedure, parameterName);
	}
	
	/**
	 * ��ȡ�������ڲ�����������Map����
	 * 
	 * key��������
	 * value��������ʵ����
	 * 
	 * @param procedure
	 * @param project
	 * @return
	 */
	public static Map<String,String> getProVariableBusinessType2Map(Procedure procedure,IARESProject project){
		Map<String,String> businessType = new HashMap<String, String>();
		for(Parameter param : procedure.getInternalVariables()){
			if (param.getParamType() == ParamType.NON_STD_FIELD) {
				businessType.put(param.getId(), param.getRealType());
			}else if (param.getParamType() == ParamType.STD_FIELD) {
				businessType.put(param.getId(), getRealDataType(param.getId(),project,MetadataServiceProvider.C_TYPE));
			}
		}
		return businessType;
	}
	
	/**
	 * ��ȡ��������ʵ���ͣ�����Map����
	 * 
	 * key��������
	 * value��������ʵ����
	 * @param �����б�
	 * @param project
	 * @return
	 */
	public static Map<String,String> getParamterBusinessType2Map(List<Parameter>parameters ,IARESProject project){
		Map<String,String> businessType = new HashMap<String, String>();
		for(Parameter param : parameters){
			if (param.getParamType() == ParamType.NON_STD_FIELD) {
				businessType.put(param.getId(), param.getRealType());
			}else if (param.getParamType() == ParamType.STD_FIELD) {
				businessType.put(param.getId(), getRealDataType(param.getId(),project,MetadataServiceProvider.C_TYPE));
			}
		}
		return businessType;
	}
	/**
	 * ���ݱ�׼�ֶ����ƻ����ʵ����
	 * @param stdName
	 * @param project
	 * @return
	 */
	public static String  getRealDataType(String stdName,IARESProject project,String type){
		if(StringUtils.isBlank(type)){
			type = MetadataServiceProvider.C_TYPE;
		}
		StandardDataType stdType = null;
		try {
			stdType = MetadataServiceProvider.getStandardDataTypeOfStdFieldByName(project, stdName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BusinessDataType busType = null;
		try {
			busType = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(project, stdName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if((stdType != null)  && ( busType!= null))//��׼�ֶ�
		{
			String dataType = StringUtils.defaultIfBlank(stdType.getValue(type), "");
			int length = 0;
			if(StringUtils.isNotBlank(busType.getLength())){
				try {
					length = NumberUtils.toInt(busType.getLength(), 0) + 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return dataType = dataType.replace("$L", length + "");
			}
			return dataType;
			/*if(busType.getPrecision() != null){
				int precision = 0;
				try {
					precision = NumberUtils.toInt(busType.getPrecision(), 0) ;
				} catch (Exception e) {
					e.printStackTrace();
				}
				dataType = dataType.replace("$P", precision + "");
				return dataType;
			
			}*/
			
		}
		return StringUtils.EMPTY;
	}

}
