/**
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.AtomFunctionCompilerUtil;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;

/**
 * @author liaogc
 *
 */
public class SerEndHelper {
	
	private final static String ADD_FIELD_DEFAULT = "lpOutPacker->AddField(\"%1$s\",'S');\r\n";
	private final static String ADD_CHAR_ARRAY_FIELD = "lpOutPacker->AddField(\"%1$s\",'S',%2$s);\r\n";
	private final static String ADD_CHAR_ARRAY_FIELD2 = "lpOutPacker->AddField(\"%1$s\",'S');\r\n";
	private final static String ADD_INT_FIELD = "lpOutPacker->AddField(\"%1$s\",'I');\r\n";
	private final static String ADD_CHAR_FIELD = "lpOutPacker->AddField(\"%1$s\",'C');\r\n";
	private final static String ADD_FLOAT_FIELD = "lpOutPacker->AddField(\"%1$s\",'D',%2$s,%3$s);\r\n";
	private final static String ADD_CLOB_FIELD = "lpOutPacker->AddField(\"%1$s\",'R',pi_%1$s);\r\n";
	
	private final static String ADD_VALUE = "lpOutPacker->Add%1$s(@%2$s);//%2$s\r\n";
	private final static String ADD_CLOB_VALUE = "lpOutPacker->AddRaw(@%1$s,pi_%1$s);//%1$s\r\n";
	private final static String ADD_OBJECT_VALUE = "lpOutPacker->AddRaw(p_%1$s,pi_%1$s);//%1$s\r\n";
	/**
	 * �������д�"E"��ǵ��ֶ������ֶ�ֵ
	 * @param outputParameters
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public  String getErrorFieldAndFieldValue(List<Parameter> outputParameters,List<String> excludeParameters,Map<Object, Object> context) throws Exception{
		StringBuffer errorFieldBuffer = new StringBuffer();
		StringBuffer errorFieldValueBuffer = new StringBuffer();
		IARESProject project = (IARESProject) context.get(IAtomEngineContextConstantMySQL.Aresproject);
		for(Parameter parameter:outputParameters){
			if(isParameterExistEFlag(parameter) && !parameterInList(excludeParameters,parameter.getId())){
				if(parameter.getParamType().getValue()==ParamType.OBJECT_VALUE){
					errorFieldBuffer.append(AddFiled("object", parameter, project));
					errorFieldValueBuffer.append(AddValue("object", parameter));
				}else{
					String type = getRealType(parameter,project,context);
					errorFieldBuffer.append(AddFiled(type, parameter, project));
					errorFieldValueBuffer.append(AddValue(type, parameter));
				}
			}
		}
		
		return errorFieldBuffer.toString()+errorFieldValueBuffer.toString();
	}
	/**
	 * �жϲ����Ƿ����б���
	 * @param outputParameters
	 * @param paraId
	 * @return
	 */
	private  boolean parameterInList(List<String> excludeParameters,String paraId){
		if(excludeParameters==null || excludeParameters.isEmpty()) return false;
		for(String paramId2 :excludeParameters){
			if(StringUtils.equals(paramId2, paraId)){
				return true;
				
			}
		}
		return false;
	}
	
	/**
	 * �������Ƿ���ڴ�E���
	 * @param param
	 * @return
	 */
	private  boolean  isParameterExistEFlag(Parameter param){
		if(param==null || StringUtils.isEmpty(param.getFlags())){
			return false;
		}
		return StringUtils.indexOf(StringUtils.upperCase(param.getFlags()), "E")>-1;
		
	}
	/**
	 * ���ز���ҪaddField�Ĳ���
	 * @return
	 */
	public  List<String> getExcludeParameters(){
		List<String> excludeParameters = new ArrayList<String>();
		excludeParameters.add("error_pathinfo");
		excludeParameters.add("error_no");
		excludeParameters.add("error_info");
		return excludeParameters;
	}
	//����type��ȡ��������ʵ���ͣ���type��������Ϊ��c��oracle��
	private static String getRealType(Parameter param,IARESProject project,Map<Object,Object> context){
		return AtomFunctionCompilerUtil.getRealDataType(param, project, context);
	}
	
	//���������AddFiled
	private  String AddFiled(String type,Parameter param,IARESProject project) throws Exception{
		if (TypeRule.typeRuleChar(type)) {
			return String.format(ADD_CHAR_FIELD, param.getId());
		}  else if (TypeRule.typeRuleDouble(type)||TypeRule.typeRuleFloat(type)) {
			Map<String,String> parameterInfo = AtomFunctionCompilerUtil.getStandardFieldParameterInfo(param.getId(), project);
			String length = NumberUtils.toInt(StringUtils.defaultIfBlank(parameterInfo.get("length"), ""),0)+"";
			String precision = StringUtils.defaultIfBlank(parameterInfo.get("precision"), "0");
			if (StringUtils.isNotBlank(length)&& StringUtils.isNotBlank(precision)) {
				return String.format(ADD_FLOAT_FIELD, param.getId(),length,precision);
			} else {// �׳�ʵ�������������ô�����쳣
				throw new Exception("�����������ô���");
			}
		}else if (TypeRule.typeRuleInt(type)) {
			return String.format(ADD_INT_FIELD, param.getId());
		}else if (TypeRule.typeRuleClob(type)) {
			return String.format(ADD_CLOB_FIELD, param.getId());
		} else if (TypeRule.typeRuleObject(type)) {
			return String.format(ADD_CLOB_FIELD, param.getId());
		}else  if (TypeRule.typeRuleCharArray(type) && TypeRule.greaterThan255(type)) {// charArray���ͣ��ҳ��ȴ���255
			return String.format(ADD_CHAR_ARRAY_FIELD, param.getId(),TypeRule.getCharLength(type));
		} else if(TypeRule.typeRuleCharArray(type)){
			return String.format(ADD_CHAR_ARRAY_FIELD2, param.getId());//����С��255
		}else{
			return String.format(ADD_FIELD_DEFAULT, param.getId());
		}
	}
	
	//���������AddValue
	private  String AddValue(String type,Parameter param){
		if(TypeRule.typeRuleClob(type)){
			return String.format(ADD_CLOB_VALUE, param.getId());
		}if(TypeRule.typeRuleObject(type)){
			return String.format(ADD_OBJECT_VALUE, param.getId());
		}else{
			if (TypeRule.typeRuleChar(type)) {
				return String.format(ADD_VALUE, "Char",param.getId());
			} else if (TypeRule.typeRuleCharArray(type)) {
				return String.format(ADD_VALUE, "Str",param.getId());
			} else if (TypeRule.typeRuleDouble(type)) {
				return String.format(ADD_VALUE, "Double",param.getId());
			} else {
				return String.format(ADD_VALUE, "Int",param.getId());
			}
		}
	}
	
}
