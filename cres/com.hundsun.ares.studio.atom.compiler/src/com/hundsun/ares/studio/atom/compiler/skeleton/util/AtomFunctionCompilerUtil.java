/**
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.atom.compiler.skeleton.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.util.BizInterfaceParameterUtil;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.cres.extend.core.constants.ICresExtendConstants;
import com.hundsun.ares.studio.cres.extend.cresextend.CresMoudleExtendProperty;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
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
	 * ��ȡ��������ʵ���ͣ�����Map����
	 * 
	 * key�������� value��������ʵ����
	 * 
	 * @param �����б�
	 * @param project
	 * @return
	 */
	public static Map<String, String> getParamterBusinessType2Map(
			List<Parameter> parameters, IARESProject project) {
		Map<String, String> businessType = new HashMap<String, String>();
		for (Parameter param : parameters) {
			if (param.getParamType() == ParamType.NON_STD_FIELD) {
				businessType.put(param.getId(), param.getRealType());
			} else if (param.getParamType() == ParamType.STD_FIELD) {
				businessType.put(
						param.getId(),
						getRealDataType(param.getId(), project,
								MetadataServiceProvider.C_TYPE));
			}
		}
		return businessType;
	}

	/**
	 * ���ݱ�׼�ֶ����ƻ����ʵ����
	 * 
	 * @param stdName
	 * @param project
	 * @return
	 */
	public static String getRealDataType(String stdName, IARESProject project,
			String type) {
		if (StringUtils.isBlank(type)) {
			type = MetadataServiceProvider.C_TYPE;
		}
		StandardDataType stdType = null;
		try {
			stdType = MetadataServiceProvider
					.getStandardDataTypeOfStdFieldByName(project, stdName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BusinessDataType busType = null;
		try {
			busType = MetadataServiceProvider
					.getBusinessDataTypeOfStdFieldByName(project, stdName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ((stdType != null) && (busType != null))// ��׼�ֶ�
		{
			String dataType = StringUtils.defaultIfBlank(
					stdType.getValue(type), "");
			int length = 0;
			if (StringUtils.isNotBlank(busType.getLength())) {
				try {
					length = NumberUtils.toInt(busType.getLength(), 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return dataType = dataType.replace("$L", length + "");
			}
			return dataType;
			/*
			 * if(busType.getPrecision() != null){ int precision = 0; try {
			 * precision = NumberUtils.toInt(busType.getPrecision(), 0) ; }
			 * catch (Exception e) { e.printStackTrace(); } dataType =
			 * dataType.replace("$P", precision + ""); return dataType;
			 * 
			 * }
			 */

		}
		return StringUtils.EMPTY;
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
