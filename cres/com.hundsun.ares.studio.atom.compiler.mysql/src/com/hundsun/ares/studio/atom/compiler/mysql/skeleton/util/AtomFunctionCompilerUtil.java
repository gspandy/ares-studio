/**
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.emf.common.util.EList;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.InternalParam;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.util.BizInterfaceParameterUtil;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.cres.extend.core.constants.ICresExtendConstants;
import com.hundsun.ares.studio.cres.extend.cresextend.CresMoudleExtendProperty;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.model.database.ColumnType;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * ԭ�Ӻ������������
 * 
 * @author qinyuan
 * 
 */
public class AtomFunctionCompilerUtil {

	/**
	 * ����Ĭ��ֵ�����ͻ�ȡ��ʵ��Ĭ��ֵ�����ַ�ֵ�����˫����
	 * 
	 * @param type
	 *            Ĭ��ֵ����
	 * @param defaultValue
	 *            Ĭ��ֵֵ
	 * @param project
	 *            ����
	 * @return ��ʵ��Ĭ��ֵ
	 * @throws Exception
	 */
	public static String getTrueDefaultValueByType(String type,
			String defaultValue, IARESProject project) throws Exception {
		if (TypeRule.typeRuleCharArray(type)) {
			if (defaultValue.startsWith("\"") && defaultValue.endsWith("\"")) {
				return defaultValue;
			}
			// 2013��8��21��14:36:49 �ַ����п����Ե����ų���
			if (defaultValue.startsWith("'") && defaultValue.endsWith("'")) {
				int length = defaultValue.length();
				if (length > 1) {
					return "\"" + defaultValue.substring(1, length - 1) + "\"";
				} else {// ��Ϊ�ַ���'��
					return defaultValue;
				}
			}

			if (MetadataServiceProvider.getConstantItemByName(project,
					defaultValue) == null
					&& MetadataServiceProvider.getErrorNoItemByName(project,
							defaultValue) == null) {// ��Ϊ�û�����,Ҳ��Ϊ��׼�����
				if (!defaultValue.startsWith("\"")) {
					defaultValue = "\"" + defaultValue;
				}
				if (!defaultValue.endsWith("\"")) {
					defaultValue += "\"";
				}
			}
			return defaultValue;
		} else if (TypeRule.typeRuleChar(type)) {
			if (defaultValue.startsWith("'") && defaultValue.endsWith("'")) {
				return defaultValue;
			}
			// 2013��8��21��14:37:15 �ַ��п�����˫���ų���
			if (defaultValue.startsWith("\"") && defaultValue.endsWith("\"")) {
				int length = defaultValue.length();
				if (length > 1) {
					return "'" + defaultValue.substring(1, length - 1) + "'";
				} else {
					return defaultValue;
				}
			}

			if (MetadataServiceProvider.getConstantItemByName(project,
					defaultValue) == null
					&& MetadataServiceProvider.getErrorNoItemByName(project,
							defaultValue) == null) {
				if (!defaultValue.startsWith("'")) {
					defaultValue = "'" + defaultValue;
				}
				if (!defaultValue.endsWith("'")) {
					defaultValue += "'";
				}
			}
			return defaultValue;
		} else if (TypeRule.typeRuleInt(type)) {
			return defaultValue;
		} else if (TypeRule.typeRuleClob(type)) {
			return "NULL";
		} else if (TypeRule.typeRuleDouble(type)) {
			return defaultValue;
		} else if (TypeRule.typeRulePacker(type)) {
			return "NULL";
		} else {
			throw new Exception(String.format("û�ж�ʵ���������ͣ�[%s]���д���", type));
		}
	}

	/**
	 * �жϸ����Ĳ������Ƿ���ԭ�Ӻ��������������
	 * 
	 * @param atomFunction
	 *            ԭ�Ӻ���
	 * @param parameterName
	 *            ������
	 * @return
	 */
	public static boolean isParameterINInputParameterByName(
			AtomFunction atomFunction, String parameterName,
			IARESProject project) {
		return BizInterfaceParameterUtil.isInputParameter(atomFunction,
				parameterName, project);
	}

	/**
	 * �жϸ����Ĳ������Ƿ���ԭ�Ӻ����������������
	 * 
	 * @param atomFunction
	 *            ԭ�Ӻ���
	 * @param parameterName
	 *            ������
	 * @return
	 */
	public static boolean isParameterINOutputParameterByName(
			AtomFunction atomFunction, String parameterName,
			IARESProject project) {
		return BizInterfaceParameterUtil.isOutputParameter(atomFunction,
				parameterName, project);
	}

	/**
	 * �жϸ����Ĳ������Ƿ���ԭ�Ӻ������������������
	 * 
	 * @param atomFunction
	 *            ԭ�Ӻ���
	 * @param parameterName
	 *            ������
	 * @return
	 */
	public static boolean isParameterINInputAndOutputParameterByName(
			AtomFunction atomFunction, String parameterName,
			IARESProject project) {
		return isParameterINInputParameterByName(atomFunction, parameterName,
				project)
				|| isParameterINOutputParameterByName(atomFunction,
						parameterName, project);
	}

	/**
	 * �жϸ����Ĳ������Ƿ���ԭ�Ӻ������ڲ�������
	 * 
	 * @param atomFunction
	 *            ԭ�Ӻ���
	 * @param parameterName
	 *            ������
	 * @return
	 */
	public static boolean isParameterINInternalVariablesByName(
			AtomFunction atomFunction, String parameterName) {
		List<String> names = new ArrayList<String>();
		for (Parameter para : atomFunction.getInternalVariables()) {
			names.add(para.getId());
		}
		return names.contains(parameterName);
	}

	/**
	 * �жϸ����Ĳ������Ƿ���ԭ�Ӻ�������������Լ��ڲ�������
	 * 
	 * @param atomFunction
	 *            ԭ�Ӻ���
	 * @param parameterName
	 *            ������
	 * @return
	 */
	public static boolean isParameterINAtomFunctionParameterByName(
			AtomFunction atomFunction, String parameterName,
			IARESProject project) {
		return isParameterINInputParameterByName(atomFunction, parameterName,
				project)
				|| isParameterINOutputParameterByName(atomFunction,
						parameterName, project)
				|| isParameterINInternalVariablesByName(atomFunction,
						parameterName);
	}
	
	/**
	 * ��ȡ������������п��������룬������ڲ�������)
	 * 
	 * @param atomFunction
	 *            ԭ�Ӻ���
	 * @param parameterName
	 *            ������
	 * @return Parameter
	 */
	public static Parameter getParameterINAtomFunctionParameterByName(AtomFunction atomFunction, String parameterName) {
		EList<InternalParam> internalVars = atomFunction.getInternalVariables();
		EList<Parameter> inputParams = atomFunction.getInputParameters();
		EList<Parameter> outputParams = atomFunction.getOutputParameters();
		for(int i = 0;i < internalVars.size();i++){
			if(StringUtils.equals(internalVars.get(i).getId(),parameterName)){
				return internalVars.get(i);
			}
		}
		for(int i = 0;i < inputParams.size();i++){
			if(StringUtils.equals(inputParams.get(i).getId(),parameterName)){
				return inputParams.get(i);
			}
		}
		for(int i = 0;i < outputParams.size();i++){
			if(StringUtils.equals(outputParams.get(i).getId(),parameterName)){
				return outputParams.get(i);
			}
		}
		return null;
	}

	/**
	 * ��ȡ��������ʵ���ͣ�����Map����
	 * 
	 * key�������� value��������ʵ����
	 * 
	 * @param �����б�
	 * @param project
	 * @return
	 */
	public static Map<String, String> getParamterBusinessType2Map(
			List<Parameter> parameters, IARESProject project,Map<Object, Object> context) {
		Map<String, String> businessType = new HashMap<String, String>();
		for (Parameter param : parameters) {
			if (param.getParamType() == ParamType.NON_STD_FIELD) {
				businessType.put(param.getId(), param.getRealType());
			} else if (param.getParamType() == ParamType.STD_FIELD) {
				businessType.put(param.getId(),getRealDataType(param, project,context));
			}
		}
		return businessType;
	}

	/**
	 * ��ȡ������ʵ����
	 * @param param ����������������
	 * @param project ����
	 * @context ������
	 * @return String C��ʵ����
	 */
	public static String getRealDataType(Parameter param, IARESProject project,Map<Object, Object> context) {
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
		return dataType;
	}
	
	/**
	 * �����ֶ����ƻ�ȡ��׼�ֶ���ʵ����
	 * @param stdName ��׼�ֶ�����
	 * @param project ����
	 * @context ������
	 * @return String C��ʵ����
	 */
	public static String getRealDataType(String stdName, IARESProject project,Map<Object, Object> context) {
		StandardField stdfield = MetadataServiceProvider.getStandardFieldByNameNoExp(project, stdName);//getIdΪ��������getNameΪ������
		if(stdfield == null){
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("��׼�ֶ�[%1$s]�����ڡ�", stdName);
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			return "";//��׼�ֶβ�����ʱ���������ɣ���������д�����Ϣ���׳���
		}
		String bizTypeName = stdfield.getDataType();
		int length = 0;
		BusinessDataType bizType = MetadataServiceProvider.getBusinessDataTypeByNameNoExp(project, bizTypeName);
		if(bizType == null){
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("��׼�ֶ�[%1$s]��Ӧ��ҵ������[%2$s]�����ڡ�", stdName,bizTypeName);
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			return "";//ҵ�����Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
		}
		try {
			length = Integer.parseInt(bizType.getLength()) + 1;//����Char�������ʱ������Ҫ��1
		} catch (Exception e) {
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("��׼�ֶ�[%1$s]��Ӧ��ҵ������:%2$s�ĳ���Ϊ�Ƿ����֣�%3$s��", stdName,bizTypeName,bizType.getLength());
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			return "";//ҵ�����ͳ���Ϊ�Ƿ�����ʱ���������ɣ���������д�����Ϣ���׳���
		}
		StandardDataType stdType = MetadataServiceProvider.getStandardDataTypeByNameNoExp(project, bizType.getStdType());
		if(stdType == null){
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("��׼�ֶ�[%1$s]��Ӧ��ҵ������[%2$s]�еı�׼����[%3$s]�����ڡ�", stdName,bizTypeName,bizType.getStdType());
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			return "";//��׼���Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
		}
		String dataType = StringUtils.defaultIfBlank(stdType.getValue(MetadataServiceProvider.C_TYPE), "");
		dataType = dataType.replace("$L", length + "");
		return dataType;
	}
	
	/**
	 * ��ȡ���ֶ���ʵ����
	 * @param column ���ֶ�
	 * @param project ����
	 * @context ������
	 * @return String C��ʵ����
	 */
	public static String getRealDataType(String tableName,TableColumn column, IARESProject project,Map<Object, Object> context) {
		String bizTypeName = "";
		if(column.getColumnType() == ColumnType.STD_FIELD)//��׼�ֶβ���
		{
			StandardField stdfield = MetadataServiceProvider.getStandardFieldByNameNoExp(project, column.getName());
			if(stdfield == null){
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("���ݿ��[%1$s]�б��ֶ�[%2$s]��Ӧ�ı�׼�ֶβ����ڡ�", tableName ,column.getName());
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				return "";//��׼�ֶβ�����ʱ���������ɣ���������д�����Ϣ���׳���
			}
			bizTypeName = stdfield.getDataType();//��׼�ֶ�ʱ��ȡ��׼�ֶζ�Ӧҵ������
		}else if(column.getColumnType() == ColumnType.NON_STD_FIELD){//�Ǳ����
			bizTypeName = column.getDataType();//�Ǳ��ֶ�ʱ��ȡ������ֱ�������ҵ������
		}
		int length = 0;
		BusinessDataType bizType = MetadataServiceProvider.getBusinessDataTypeByNameNoExp(project, bizTypeName);//���ﲻ��ʹ��param.getType()�����������Ǳ�ʱ��ȡ����ҵ�����͵��쳣
		if(bizType == null){
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("���ݿ��[%1$s]�б��ֶ�[%2$s]��Ӧ��ҵ������[%3$s]�����ڡ�", tableName ,column.getName(),bizTypeName);
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			return "";//ҵ�����Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
		}
		try {
			length = Integer.parseInt(bizType.getLength()) + 1;//����Char�������ʱ������Ҫ��1
		} catch (Exception e) {
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("���ݿ��[%1$s]�б��ֶ�[%2$s]��Ӧ��ҵ������[%3$s]�ĳ���Ϊ�Ƿ�����[%4$s]��", tableName ,column.getName(),bizTypeName,bizType.getLength());
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			return "";//ҵ�����ͳ���Ϊ�Ƿ�����ʱ���������ɣ���������д�����Ϣ���׳���
		}
		StandardDataType stdType = MetadataServiceProvider.getStandardDataTypeByNameNoExp(project, bizType.getStdType());
		if(stdType == null){
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("���ݿ��[%1$s]�б��ֶ�[%2$s]��Ӧ��ҵ������[%3$s]�еı�׼����[%4$s]�����ڡ�", tableName ,column.getName(),bizTypeName,bizType.getStdType());
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			return "";//��׼���Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
		}
		String dataType = StringUtils.defaultIfBlank(stdType.getValue(MetadataServiceProvider.C_TYPE), "");
		dataType = dataType.replace("$L", length + "");
		return dataType;
	}
	
	/**
	 * ��ȡ���ֶ���ʵĬ��ֵ
	 * @param column ���ֶ�
	 * @param project ����
	 * @context ������
	 * @return String Oracle��ʵĬ��ֵ�����ɵ���SQL��䣬������Oracle��
	 */
	public static String getRealDefaultValue(String tableName,TableColumn column, IARESProject project,Map<Object, Object> context) {
		String bizTypeName = "";
		if(column.getColumnType() == ColumnType.STD_FIELD)//��׼�ֶβ���
		{
			StandardField stdfield = MetadataServiceProvider.getStandardFieldByNameNoExp(project, column.getName());
			if(stdfield == null){
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("���ݿ��[%1$s]�б��ֶ�[%2$s]��Ӧ�ı�׼�ֶβ����ڡ�", tableName ,column.getName());
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				return "";//��׼�ֶβ�����ʱ���������ɣ���������д�����Ϣ���׳���
			}
			bizTypeName = stdfield.getDataType();//��׼�ֶ�ʱ��ȡ��׼�ֶζ�Ӧҵ������
		}else if(column.getColumnType() == ColumnType.NON_STD_FIELD){//�Ǳ����
			bizTypeName = column.getDataType();//�Ǳ��ֶ�ʱ��ȡ������ֱ�������ҵ������
		}
		BusinessDataType bizType = MetadataServiceProvider.getBusinessDataTypeByNameNoExp(project, bizTypeName);
		if(bizType == null){
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("���ݿ��[%1$s]�б��ֶ�[%2$s]��Ӧ��ҵ������[%3$s]�����ڡ�", tableName ,column.getName(),bizTypeName);
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			return "";//ҵ�����Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
		}
		//��������ж�Ӧ��Ĭ��ֵ��Ϊ�գ���Ĭ��ֵ�Ը�ֵΪ׼��ע����������ʹ�ñ�׼Ĭ��ֵ��ͬʱҲ��������ʵĬ��ֵ��
		if(StringUtils.isNotEmpty(column.getDefaultValue())){
			TypeDefaultValue typpeDefValue = MetadataServiceProvider.getTypeDefaultValueByNameNoExp(project, column.getDefaultValue());
			//����Ҳ�����׼Ĭ��ֵ����ͳһ����ʵĬ��ֵ�����û�����ʲô�������ʲô
			if(typpeDefValue == null){
				return column.getDefaultValue();
			}else{
				String defValue = typpeDefValue.getValue(MetadataServiceProvider.MYSQL_TYPE);
				return defValue;
			}
		}
		//������Ĭ��ֵΪ�գ�ȡҵ�����Ͷ�Ӧ�ı�׼Ĭ��ֵ����������Ǳ�׼Ĭ��ֵ��������Ҫ����
		else{
			TypeDefaultValue typpeDefValue = MetadataServiceProvider.getTypeDefaultValueByNameNoExp(project, bizType.getDefaultValue());
			if(typpeDefValue == null){
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("����[%1$s]��Ӧ��ҵ������[%2$s]�еı�׼Ĭ��ֵ[%3$s]�����ڡ�", column.getName(), bizTypeName,bizType.getDefaultValue());
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				return "";//��׼Ĭ��ֵ������ʱ���������ɣ���������д�����Ϣ���׳���
			}
			String defValue = typpeDefValue.getValue(MetadataServiceProvider.MYSQL_TYPE);
			return defValue;
		}
	}
	
	/**
	 * ��ȡ��׼�ֶβ�����Ϣ
	 * @param name
	 * @return
	 */
	public static Map<String,String> getStandardFieldParameterInfo( String name,IARESProject project){
		Map<String,String> parameterInfo = new HashMap<String,String>();
			StandardDataType stdType = null;
			try {
				stdType = MetadataServiceProvider.getStandardDataTypeOfStdFieldByName(project, name);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			TypeDefaultValue typpeDefValue = null;
			try {
				typpeDefValue = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName(project, name);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			BusinessDataType busType = null;
			try {
				busType = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(project, name);
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			if((stdType != null) && ( typpeDefValue!= null) && ( busType!= null))//��׼�ֶ�
			{
				String dataType = stdType.getValue(MetadataServiceProvider.C_TYPE);
				String defValue = typpeDefValue.getValue(MetadataServiceProvider.C_TYPE);
				String  length = StringUtils.defaultIfBlank(busType.getLength(), "0");
				String precision =StringUtils.defaultIfBlank(busType.getPrecision(),"0");
				dataType = dataType.replace("$L", length);
				parameterInfo.put("type", dataType);
				parameterInfo.put("value", defValue);
				parameterInfo.put("length", length);
				parameterInfo.put("precision", precision);
			
			}
		
		return parameterInfo;
	}

	/**
	 * ��ȡAS AF�����ݿ����ԣ��������Դ�����ڣ���ȥģ�������л�ȡ
	 * 
	 * @param project
	 * @param database
	 * @param resName
	 * @param type
	 * @return
	 */
	public static String getAtomDatabase(IARESProject project, String database,
			String chineseName, String type, String flag) {
		if (StringUtils.isNotBlank(database)) {
			return database;
		}
		database = getCRESModuleDatabase(project, chineseName, type);
		/*
		 * if (StringUtils.isBlank(database) &&
		 * !StringUtils.equalsIgnoreCase(flag, "r")) { throw new
		 * RuntimeException(chineseName + ": ���ݿ��������"); }
		 */
		return database;
	}

	/**
	 * ��ȡģ���е�CRES����ҳ�����ݿ� ������������ݿ⣬���׳��쳣
	 * 
	 * @param project
	 * @param resName
	 * @param type
	 * @return
	 */
	public static String getCRESModuleDatabase(IARESProject project,
			String resName, String type) {
		ReferenceManager manager = ReferenceManager.getInstance();

		ReferenceInfo ref = manager.getFirstReferenceInfo(project, type,
				resName, true);
		if (ref != null) {
			try {
				IARESResource res = ref.getResource();
				Stack<IARESModule> stack = new Stack<IARESModule>();
				stack.push(((IARESModule) res.getModule()));
				while (!stack.isEmpty()) {
					IARESModule module = stack.pop();
					IARESResource mr = module
							.getARESResource(IARESModule.MODULE_PROPERTY_FILE);
					ModuleProperty mp = mr.getInfo(ModuleProperty.class);
					Object mProperty = mp.getMap().get(
							ICresExtendConstants.CRES_EXTEND_MOUDLE_PROPERTY);
					if (mProperty != null
							&& mProperty instanceof CresMoudleExtendProperty
							&& StringUtils
									.isNotBlank(((CresMoudleExtendProperty) mProperty)
											.getDataBaseName())) {
						return ((CresMoudleExtendProperty) mProperty)
								.getDataBaseName();
					} else if (module.getParentModule() instanceof IARESModule) {
						stack.push((IARESModule) module.getParentModule());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return StringUtils.EMPTY;
	}

}
