/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.logic.compiler.tokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.skeleton.util.SerEndHelper;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.util.ParamGroupUtil;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.logic.LogicService;
import com.hundsun.ares.studio.logic.compiler.constant.ILogicEngineContextConstant;
import com.hundsun.ares.studio.logic.compiler.util.Value;

/**
 * �߼�����svr_end����
 * �����������ߴ���ο���
 * com.hundsun.hdt.internal.generate.service.engine.BusinessServiceEngine#getSvrendCode
 * @author qinyuan
 *
 */
public class LogicServiceSvrEndToken implements ICodeToken {

	protected String NAME_SERVICE_OUTPACKER = "lpOutPacker";
	
	
	
	/** �������֣�����1 ���Ӵ��룬 ����2 ����������3 trueʱ�Ĵ��룬����4 false�Ĵ��� */
	protected static final String LOGIC_SERVICE_FORMAT_SVR_END = "\r\ngoto svr_end;\r\n" + "svr_end:\r\n" + "if (%1$s)\r\n" + "{\r\n" + "%2$s" + "\r\n}\r\nelse\r\n{" + "%3$s" + "\n}\n";
	
	private static final String FORMAT_SVR_END_A = "\r\ngoto svr_end;\r\n" + "svr_end:\n" + "if (%1$s)\n" + "{\n" 
														+ "if (p_audit_action != CNST_AUDIT_BUSI_CHK)\n{\n"
														+ "%2$s" 
														+"}\n"
														+ "\n}\nelse\n{" + "%3$s" + "\n}\n";
	
	private static final String INTERFACE_G =   "lpOutPacker->BeginPack(); \n"+
	  "lpOutPacker->AddField(\"error_pathinfo\",'S',%s);\n"+ 
	  "lpOutPacker->AddField(\"error_no\");\n"+ 
	  "lpOutPacker->AddField(\"error_info\",'S',%s);\n"+ 
	  "lpOutPacker->AddField(\"error_id\");\n"+ 
	  "lpOutPacker->AddField(\"ErrorNo\");\n"+ 
	  "lpOutPacker->AddField(\"ErrorInfo\",'S',%s);\n"+ 
	  "lpOutPacker->AddStr(%s);		//error_pathinfo 	\n"+ 
	  "lpOutPacker->AddInt(%s);		//error_no 	\n"+ 
	  "lpOutPacker->AddStr(%s);		//error_info 	\n"+ 
	  "lpOutPacker->AddInt(%s);		//error_id 	\n"+ 
	  "lpOutPacker->AddInt(getBankErrorXml(lpContext,%s));		//ErrorNo 	\n"+ 
	  "lpOutPacker->AddStr(GetErrorInfox(lpContext,%s,%s));\n";
	
	
	
	private String FORMAT_ADD_FIELD = "%s->AddField(\"%s\" %s);\n";
	
	private String NAME_SERVICE_ASCONTEXT = "pContext";
	
	private String FORMAT_RELEASE_CODE =
		"if (%1$s) \n" +
		"{ \n" +
		"  free(%1$s->GetPackBuf()); \n" +
		"  %1$s->Release(); \n" +
		"}\n";
	private String NAME_SERVICE_PACK_SERVICE = "lpPackService";
	
	private String NAME_SERVICE_FUNC_INPACKER = "lpFuncInPacker";
	
	
	private static int TYPE_INT =0;//����Ϊ����
	private static int TYPE_CHAR =1;//����Ϊ�ַ�
	private static int TYPE_STRING =2;//����Ϊ�ַ���
	private static int TYPE_DOUBLE =3;//����Ϊfloat����double
	private static int TYPE_RAW =4;//����ΪRAW
	private static String FLAG_M="m";//
	private static final String RESULT_SET_PREFIX = "lpResultSet";
	private static final String X_FLAG_PLACEHOLDER = "___XXX___";
	protected Map<String,String> resultsetParameters;
	protected List<String> popVarList ;//α�����б�
	protected IARESProject project;
	private Set<String> calledResultSetList;//�����õ�AS����LF��
	
	/** �����ô�����ʱ�����ȱ���ԭ�� */
	protected static Set<String> excludeUseVariable = new HashSet<String>();
	static {
		excludeUseVariable.add("branch_no");
		excludeUseVariable.add("sysnode_id");
		excludeUseVariable.add("subsys_id");
		excludeUseVariable.add("timeout");
	}
	
	@Override
	public String getContent() {
		return null;
	}

	@Override
	public int getType() {
		return CODE_TEXT;
	}

	@Override
	public String genCode(Map<Object, Object> context) throws Exception {
		AtomFunction ls = (AtomFunction)context.get(ILogicEngineContextConstant.ResourceModel);
		 project = (IARESProject) context.get(ILogicEngineContextConstant.Aresproject);
		resultsetParameters = this.getResultsetParameters(context);//������ֶ��б�
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(ILogicEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		calledResultSetList =  helper.getAttribute(ILogicEngineContextConstant.ATTR_RESULTSET_LIST);
		this.popVarList = (List<String>)context.get(ILogicEngineContextConstant.PseudoCode_Para_LIST);
		Set<String> rsSet = helper.getAttribute(ILogicEngineContextConstant.ATTR_RESULTSET_LIST);
		List<Parameter> outputParameters = new ArrayList<Parameter>();
		ParamGroupUtil.parserParameters(ls.getOutputParameters(), outputParameters, project);
		StringBuffer true_section = new StringBuffer();
		if (ls.isOutputCollection() && rsSet.size()>0) {
			true_section.append("PackResultSet(");
			String lastResultSetPrifex = getLastResultset(context);//ȡ����������
			true_section.append(getEndPack(ls.getInterfaceFlag(), lastResultSetPrifex));
			true_section.append(",");
			true_section.append("lpOutPacker");
			true_section.append(");\n");
		}else {
			// ���
			Set<String> in_set = new LinkedHashSet<String>();
			for (Parameter outParam : outputParameters) {
				String paramName = outParam.getId();
				if (!excludeUseVariable.contains(paramName)) {
					if(!("error_no".equals(paramName) || "error_info".equals(paramName))){
							in_set.add(paramName);
					}
				}
				if (isLastParameter(ls, paramName)) {
					in_set.add(paramName);
				}else if("error_no".equals(paramName) || "error_info".equals(paramName)){
					in_set.add(paramName);
				} else {
					String dataType = getStandardFieldParameterInfo(outParam.getId()).get("length");
					if (StringUtils.equals(dataType, Value.CLOB)) {
						in_set.add(paramName);
					}
				}
			}
			//��IO��׼���������
			List<Parameter> inputParameters = new ArrayList<Parameter>();
			ParamGroupUtil.parserParameters(ls.getInputParameters(), inputParameters, project);
			for(Parameter inParam : inputParameters){
				if (StringUtils.defaultIfBlank(inParam.getFlags(), "").indexOf("IO") != -1) {
					in_set.add(inParam.getId());
				}
			}

			doPack(true_section, in_set, "lpOutPacker", context);
			
		}
		StringBuffer false_section = new StringBuffer();
		// ���������Ϣ
		
		// 2008��12��18��10:58:38 ��Ҷ�� ��� E ��Ǵ�������������������Щ����
		boolean hasEFlag = false;
		{
			Set<String> in_set = new LinkedHashSet<String>();
			
			for (Parameter output : outputParameters) {
				if (StringUtils.isNotBlank(output.getFlags()) && output.getFlags().toLowerCase().indexOf("e") != -1) {
					in_set.add(output.getId());
					hasEFlag = true;
				}
			}
			
			if (hasEFlag) {
				in_set.add("error_info");
				in_set.add("error_no");
				in_set.add("error_pathinfo");
			}
			doPack(false_section, in_set, "lpOutPacker", context);
		}
		
		
		if (!hasEFlag ) {
			if (StringUtils.defaultIfBlank(ls.getInterfaceFlag(), StringUtils.EMPTY).toLowerCase().indexOf("o") != -1 ) {
				false_section.append("if (iReturnCode!= 1 )\n");
				false_section.append("{\n");
				false_section.append("@error_no= getExternErrorXml(" + NAME_SERVICE_ASCONTEXT + ",");
				false_section.append(ls.getObjectId());
				false_section.append(", @error_no);\n");
				false_section.append( "iReturnCode =  @error_no ;\n");
				false_section.append("}\n");
			} else if (StringUtils.defaultIfBlank(ls.getInterfaceFlag(), StringUtils.EMPTY).toLowerCase().indexOf("g") != -1) {
				String errorPathinfoLength = getStandardFieldParameterInfo("error_pathinfo").get("length");
				String errorInfoLen = getStandardFieldParameterInfo("error_info").get("length");
				false_section.append(String.format(INTERFACE_G, 
																errorPathinfoLength,
																errorInfoLen,
																errorInfoLen,
																"@error_pathinfo",
																"@error_no",
																"@error_info",
																"@error_id",
																"@error_no",
																"@error_no",
																"@error_info"));
			}
			
			if(StringUtils.defaultIfBlank(ls.getInterfaceFlag(), StringUtils.EMPTY).toLowerCase().indexOf("g") == -1){
				SerEndHelper serEndHelper = new SerEndHelper();
				false_section.append( String.format("\nSystemErrorPacker(%1$s,%2$s,%3$s,%4$s);\n", NAME_SERVICE_OUTPACKER, "@error_pathinfo", "@error_no", "@error_info")+serEndHelper.getErrorFieldAndFieldValue(outputParameters, serEndHelper.getExcludeParameters(), context));
			}
			
			
		}

		false_section.append("\r\n");
		
		
		StringBuffer sb = new StringBuffer();
		String condition = "iReturnCode" + " == OK_SUCCESS || iReturnCode  == ERR_SYSWARNING";
		if(StringUtils.defaultIfBlank(ls.getInterfaceFlag(), StringUtils.EMPTY).indexOf("A")<0){
			sb.append( String.format(LOGIC_SERVICE_FORMAT_SVR_END,condition, true_section, false_section) );
		}else{
			sb.append( String.format(FORMAT_SVR_END_A, condition, true_section, false_section) );
		}
		
		return sb.toString();
	}
	

	
	
	/**
	 * �ж��Ƿ��ǵ�ǰ���µĿ��ò���ӳ��
	 * 
	 * @param ls
	 * @param paramName
	 * @return
	 */
	private boolean isLastParameter(AtomFunction ls , String paramName){
		List<Parameter> inputParameters = new ArrayList<Parameter>();
		ParamGroupUtil.parserParameters(ls.getInputParameters(), inputParameters, project);
		for(Parameter ip : inputParameters){
			if (StringUtils.equals(ip.getId(), paramName)) {
				return true;
			}
		}
		List<Parameter> outputParameters = new ArrayList<Parameter>();
		ParamGroupUtil.parserParameters(ls.getOutputParameters(), outputParameters, project);
		for(Parameter op : outputParameters){
			if (op.getFlags() != null && op.getFlags().indexOf("IO") != -1 && StringUtils.equals(op.getId(), paramName)) {
				return true;
			}
		}
		return false;
	}
	

	protected Parameter getParameter(AtomFunction lr ,String fieldName){
		List<Parameter> inputParameters = new ArrayList<Parameter>();
		ParamGroupUtil.parserParameters(lr.getInputParameters(), inputParameters, project);
		for(Parameter param : inputParameters){
			if (StringUtils.equals(param.getId(), fieldName)) {
				return param;
			}
		}
		List<Parameter> outputParameters = new ArrayList<Parameter>();
		ParamGroupUtil.parserParameters(lr.getOutputParameters(), outputParameters, project);
		for(Parameter param : outputParameters){
			if (StringUtils.equals(param.getId(), fieldName)) {
				return param;
			}
		}
		for(Parameter param : lr.getInternalVariables()){
			if (StringUtils.equals(param.getId(), fieldName)) {
				return param;
			}
		}
		return null;
	}
	
	/*protected String relaseAndClose(Map<String ,Value> declareVars , AtomFunction lr){
		StringBuffer retsb = new StringBuffer();
		retsb.append( String.format(FORMAT_RELEASE_CODE, NAME_SERVICE_FUNC_INPACKER) );
		Value callPackage = declareVars.get(LogicServiceVariableDefineToken.OUT_PACKER_PREFIX + lr.getObjectId());
		if (callPackage != null) {
			retsb.append( String.format(FORMAT_RELEASE_CODE, callPackage.getVariableName()) );
		}
		retsb.append("return ");
		Value iReturnCode = declareVars.get("iReturnCode");
		if (iReturnCode != null) {
			retsb.append(iReturnCode.getVariableName());
		}
		retsb.append(";\r\n");
		return retsb.toString();
	}*/
	
	/**
	 * ����ֶ����ɴ���
	 * @param parameter
	 * @param parameterInfo
	 * @return
	 */
	protected String getAddFieldCode(Parameter parameter,Map<String,String> parameterInfo ){
		
		
		String type = parameterInfo.get("type");
		String type_param = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(type)) {
			  if (TypeRule.typeRuleChar(type)) {
				type_param = ",'C'";
			} else if (TypeRule.typeRuleFloat(type)|| TypeRule.typeRuleDouble(type)) {
				String length = parameterInfo.get("length");// ��Чλ
				String precision = parameterInfo.get("precision");// ����
			   type_param = ",'D'," + String.valueOf(NumberUtils.toInt(length)+ NumberUtils.toInt(precision) + 2) + "," + precision;
			} else if (TypeRule.typeRuleInt(type)) {
				type_param = ",'I'";
			}else if (TypeRule.typeRuleClob(type)||parameter.getParamType().getValue()==ParamType.OBJECT_VALUE) {
				String length = parameterInfo.get("length");// ����
				type_param = ",'R'," + length;
			}else if (TypeRule.typeRuleCharArray(type)) {
				String length = parameterInfo.get("length");//����
				if(NumberUtils.toInt(length)>255){
					type_param = ",'S'," + length;
				 }else{
					 type_param = ",'S'"; 
			}
			} else {
				type_param = ",'S'";
			}
			return String.format(FORMAT_ADD_FIELD, "lpOutPacker",parameter.getId(), type_param);
		}
		else if(StringUtils.isBlank(type)&& parameter.getParamType().getValue()==ParamType.OBJECT_VALUE){
			String id = this.resultsetParameters.get(parameter.getId());
			type_param = ",'R'," + "pi_"+ parameter.getId();
			String objectAssignStr = "";
			if(this.resultsetParameters.keySet().contains(parameter.getId())){
				String length = parameterInfo.get("length");
				if(StringUtils.isBlank(length)){
					length = "pi_"+parameter.getId();
				}
				String lpResultSetStr = "lpResultSet"+id+"->GetRaw"+"(\""+parameter.getId()+"\",&"+length+")";
				objectAssignStr = "p_"+parameter.getId() + " = " +lpResultSetStr+";";
			}
			if(StringUtils.isNotBlank(objectAssignStr)){
				return objectAssignStr+"\r\n"+String.format(FORMAT_ADD_FIELD, "lpOutPacker",parameter.getId(), type_param);
			}else{
				return String.format(FORMAT_ADD_FIELD, "lpOutPacker",parameter.getId(), type_param);
			}
			
		}
		
		
		return  StringUtils.EMPTY ;
	}
	/**
	 * ���ֶθ�ֵ���ɴ���
	 * @param parameter
	 * @param parameterInfo
	 * @return
	 */
	protected String getAddFieldValueCode(Parameter parameter,Map<String,String> parameterInfo ){
		StringBuffer addFieldValueCode = new StringBuffer();
		
		String dataType = parameterInfo.get("type");
		int type = -1; 
		if (StringUtils.isNotBlank(dataType)) {
			if (TypeRule.typeRuleInt(dataType)) {
				type = TYPE_INT;
			} else if (TypeRule.typeRuleChar(dataType)) {
				type = TYPE_CHAR;
			} else if (TypeRule.typeRuleCharArray(dataType)) {
				type = TYPE_STRING;
			} else if (TypeRule.typeRuleDouble(dataType)) {
				type = TYPE_DOUBLE;
			} else if (TypeRule.typeRuleClob(dataType)) {
				
			}
		}
		if(StringUtils.isBlank(dataType)&& parameter.getParamType().getValue()==ParamType.OBJECT_VALUE){
			type = TYPE_RAW;
		}
		 if(this.resultsetParameters.keySet().contains(parameter.getId())){//�ڽ�����б���
			//ֻ����LS�вſ����ý����������ʼ��
			addFieldValueCode.append(getAddFieldValueFromResultset(type,parameter.getId(),parameterInfo));
		}else if(popVarList.contains(parameter.getId())){//��α������
			addFieldValueCode.append(getAddFieldValueFromPopVar(type,parameter,parameterInfo));
		}else if(parameter.getParamType().getValue()==ParamType.OBJECT_VALUE ){
			addFieldValueCode.append(getAddFieldValueFromPopVar(type,parameter,parameterInfo));
		}
		else {//��Ĭ��ֵ
			addFieldValueCode.append(getDefaultValue(type,parameter,parameterInfo));
		}
		
		return   addFieldValueCode.toString();
	}
	/**
	 * ��α���������ȡֵ
	 * @param type
	 * @param parameter
	 * @return
	 */
	private String getAddFieldValueFromPopVar(int type,Parameter parameter,Map<String,String> parameterInfo){
		String code = StringUtils.EMPTY;
		String prefix = "lpOutPacker";
		if(TYPE_INT==type ){
			code = prefix+"->AddInt"+"("+"@"+parameter.getId()+");";
		}else if(TYPE_DOUBLE==type){
			code = prefix+"->AddDouble"+"("+"@"+parameter.getId()+");";
		}else if(TYPE_CHAR==type ){
			code = prefix+"->AddChar"+"("+"@"+parameter.getId()+");";
			
		}else if(TYPE_STRING==type ){
			code = prefix+"->AddStr"+"("+"@"+parameter.getId()+");";
			
		}else if(TYPE_RAW==type ){
			code = prefix+"->AddRaw"+"("+"p_"+parameter.getId()+",pi_"+parameter.getId()+");";
		}	
		return code;
	}
	
	/**
	 * ��ȡĬ��ֵ
	 * @param type
	 * @param parameter
	 * @return
	 */
	private String getDefaultValue(int type,Parameter parameter,Map<String,String> parameterInfo){
		String code = StringUtils.EMPTY;
		String prefix = "lpOutPacker";
		String value = parameterInfo.get("value");
		if(TYPE_INT==type ){
			code = prefix+"->AddInt"+"("+value+");";
		}else if(TYPE_DOUBLE==type){
			code = prefix+"->AddDouble"+"("+value+");";
		}else if(TYPE_CHAR==type ){
			if(this.isConstant(value)){

				if(StringUtils.startsWith(value, "\"")){
                	value = StringUtils.removeStart(value, "\"");
                }
                if(StringUtils.endsWith(value, "\"")){
                	value = StringUtils.removeEnd(value,"\"");
                }
				if(!StringUtils.startsWith(value, "'")&& !StringUtils.endsWith(value, "'")){//��������ڵ�����
					value = "'"+value+"'";
				}
				
			}
			code = prefix+"->AddChar"+"("+value+");";
			
		}else if(TYPE_STRING==type ){
			if(this.isConstant(value)){
				if(StringUtils.startsWith(value, "'")){
                	value = StringUtils.removeStart(value, "'");
                }
                if(StringUtils.endsWith(value, "'")){
                	value = StringUtils.removeEnd(value, "'");
                }
				if(!StringUtils.startsWith(value, "\"")&& !StringUtils.endsWith(value, "\"")){//���������˫����
					value = "\""+value+"\"";
				}
				
			}
			code = prefix+"->AddStr"+"("+value+");";
		}	
		return code;
	}
	
	/**
	 * �ӽ������ȡֵ
	 * @param type
	 * @param parameterName
	 * @return
	 */
	private String getAddFieldValueFromResultset(int type,String parameterName,Map<String,String> parameterInfo){
		String code = StringUtils.EMPTY;
		String id = this.resultsetParameters.get(parameterName);
		String prefix = "lpOutPacker";
		
		if(TYPE_INT==type ){
			String lpResultSetStr = "lpResultSet"+id+"->GetInt"+"(\""+parameterName+"\")";
			code = prefix+"->AddInt"+"("+lpResultSetStr +");";
		}else if(TYPE_DOUBLE==type){
			String lpResultSetStr = "lpResultSet"+id+"->GetDouble"+"(\""+parameterName+"\")";
			code = prefix+"->AddDouble"+"("+lpResultSetStr +");";
		}else if(TYPE_CHAR==type ){
			String lpResultSetStr = "lpResultSet"+id+"->GetChar"+"(\""+parameterName+"\")";
			code = prefix+"->AddChar"+"("+lpResultSetStr +");";
		}else if(TYPE_STRING==type ){
			String lpResultSetStr = "lpResultSet"+id+"->GetStr"+"(\""+parameterName+"\")";
			code = prefix+"->AddStr"+"("+lpResultSetStr +");";
		}else if(TYPE_RAW==type ){
			String length = parameterInfo.get("length");
			if(StringUtils.isBlank(length)){
				length = "pi_"+parameterName;
			}
			String lpResultSetStr = "lpResultSet"+id+"->GetRaw"+"(\""+parameterName+"\",&"+length+")";
			//String objectStr = "p_"+parameterName + " = " +lpResultSetStr+";";
			code = prefix+"->AddRaw"+"("+"p_"+parameterName  +","+length+");";
		}
		return code;
	}
	
	
	/**
	 * �Ƿ��ǳ��� 
	 * @param parameter
	 * @return
	 */
	private boolean isConstant(String parameter){
		return !StringUtils.startsWith(parameter, "@");
		
	}
	
	
	protected void doPack(StringBuffer sb, Set<String> in_set, String packerName,Map<Object, Object> context) {
		
		LogicService ls = (LogicService)context.get(ILogicEngineContextConstant.ResourceModel);
		StringBuffer addField = new StringBuffer();
		StringBuffer addFieldValue = new StringBuffer();
		
		for (String key : in_set) {
			Parameter p = getParameter(ls, key);
			//2013��8��27��9:12:31 ��Ԫ ��ָ���ж�
			if(null == p){
				continue;
			}
			Map<String,String> parameterInfo = getStandardFieldParameterInfo(p.getId());
			addField.append(getAddFieldCode(p,parameterInfo ));
			
			
			// ����������Ϣ��������
			if(StringUtils.equals(key, "error_pathinfo")){
				addFieldValue.append( String.format("%1$s->AddStr(%2$s);	//%3$s \n", packerName, "@error_pathinfo", key) );
			}else{
				addFieldValue.append(getAddFieldValueCode(p, parameterInfo));
			}
		}
		sb.append(addField).append(addFieldValue);
	}
	
	/**
	 * ��ȡ��׼�ֶβ�����Ϣ
	 * @param name
	 * @return
	 */
	protected Map<String,String> getStandardFieldParameterInfo( String name){
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
	 * ��ȡ����еĲ���
	 * @return
	 */
	protected Map<String,String> getResultsetParameters(Map<Object, Object> context){
		//һ��Ҫ���¸���һ�ݲ�Ȼ����ȡ������Ľ��������
		return  new HashMap<String,String>((Map<String, String>) context.get(ILogicEngineContextConstant.ATTR_RESULTSET_PARAMETER));
	}
	protected String getLastResultset(Map<Object, Object> context){
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(ILogicEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		Set<String> resultSet = (Set<String>)helper.getAttribute(ILogicEngineContextConstant.ATTR_RESULTSET_LIST);
		if(resultSet.size()>0){
			return resultSet.toArray(new String[resultSet.size()])[resultSet.size()-1];
		}
		return StringUtils.EMPTY;
		
	}
	private String getEndPack(String flag, String objectId) {
		if (StringUtils.isNotBlank(flag)) {
			if (flag.toLowerCase().indexOf("j") != -1) {
				return RESULT_SET_PREFIX + "_J";
			} else if (flag.toLowerCase().indexOf("x") != -1) {// X���
				return RESULT_SET_PREFIX + X_FLAG_PLACEHOLDER;
			}
		}
		return RESULT_SET_PREFIX + objectId;
	}

	

	
}
