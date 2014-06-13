package com.hundsun.ares.studio.atom.compiler.token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.skeleton.util.AtomFunctionCompilerUtil;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.parser.PseudoCodeParser;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.procdure.Procedure;

public class ProcedureCallStaticToken implements ICodeToken {

	Procedure procedure;
	IMacroToken token;
	String rsID;
	
	public ProcedureCallStaticToken(Procedure procedure, IMacroToken token, String rsID) {
		this.procedure = procedure;
		this.token = token;
		this.rsID = rsID;
	}

	@Override
	public String getContent() {
		return null;
	}

	@Override
	public int getType() {
		return 0;
	}

	@Override
	public String genCode(Map<Object, Object> context) throws Exception {
		StringBuffer buffer = new StringBuffer();
		StringBuffer setBuffer = new StringBuffer();
		
		String proName = procedure.getName();
		String[] params = token.getParameters();
		Map<String, String> values = new HashMap<String, String>();
		if(params.length>0){
			values.putAll(PseudoCodeParser.parserKeyValueWithAt(params[0]));
		}
		int index = 1;
		for(Parameter  param: procedure.getInputParameters()){
			index++;
			setBuffer.append(getSetValueString(param, values, context, index));
		}
		setBuffer.append(ITokenConstant.NL);
		for(Parameter param : procedure.getOutputParameters()){
			index++;
			String flag = param.getFlags();
			//IO��־���������
			if(StringUtils.isNotBlank(flag) && StringUtils.indexOf(flag, "IO")>=0){
				setBuffer.append(getSetValueString(param, values, context, index));
			}
		}
		
		String end = token.getFlag().indexOf("M")<0 ? "goto svr_end;\r\n" : "";
		String resultSet = String.format(RESULTSET, rsID,proName,end);
			
		buffer.append(COMMENT_BEGIN);
		buffer.append(String.format(CALL_BODY, proName,getQuestionMarkBuffer(),setBuffer.toString() + resultSet));
		
		return buffer.toString();
	}
	
	private String getSetValueString(Parameter param,Map<String, String> values,Map<Object, Object> context,int index) throws Exception{
		IARESProject project = (IARESProject) context.get(IAtomEngineContextConstant.Aresproject);
		AtomFunction callRes = (AtomFunction) context.get(IAtomEngineContextConstant.ResourceModel);
		List<String> list = (List<String>) context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		String id = param.getId();
		String type = getRealType(param, project);
		String setType = getSetType(type);
		if(values.containsKey(id)){
			//��Ĭ��ֵ
			String value = values.get(id);
			return String.format(SET_VALUE,setType,index,getRealFieldName(value,type,project));
		}else if(AtomFunctionCompilerUtil.isParameterINAtomFunctionParameterByName(callRes, param.getId(),project) || 
					list.contains(id)){
			//��������������ڲ������л���α������ʹ�õ��ı�������ֱ���ڱ�����ǰ����@
			String value = "@" + id;
			return String.format(SET_VALUE,setType,index,value);
		}else{
			//����ֱ��ȡĬ��ֵ
			TypeDefaultValue typeDftValue = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName(project, id);
			String dv = typeDftValue.getValue(MetadataServiceProvider.C_TYPE);
			if(TypeRule.typeRuleCharArray(type))
			{
				dv = "\" \"";
			}
			return String.format(SET_VALUE,setType,index,AtomFunctionCompilerUtil.getTrueDefaultValueByType(type, dv,project));
		}
	}
	
	//��ȡ��ȷ�Ĳ�������@occur_balance*-1����ȡ����Ӧ����@occur_balance����@��ͷ����ȡ��ʵĬ��ֵ
	private String getRealFieldName(String fieldName, String type, IARESProject project) throws Exception{
		Pattern p = Pattern.compile("@[\\w\\d_]+");
		Matcher m = p.matcher(fieldName);
		if(m.find()){
			return m.group();
		}
		return AtomFunctionCompilerUtil.getTrueDefaultValueByType(type, fieldName, project);
	}
	
	//����type��ȡ��������ʵ����
	private String getRealType(Parameter param,IARESProject project){
		return AtomFunctionCompilerUtil.getRealDataType(param.getId(), project, MetadataServiceProvider.C_TYPE);
	}
	private String getSetType(String type) throws Exception{
		if (TypeRule.typeRuleCharArray(type)) {
			return "setString";
		} else if (TypeRule.typeRuleChar(type)) {
			return "setChar";
		} else if (TypeRule.typeRuleInt(type)) {
			return "setInt";
		} else if (TypeRule.typeRuleDouble(type)) {
			return "setDouble";
		} else {
			throw  new Exception("�����������ô���");
		}
	}
	
	private StringBuffer getQuestionMarkBuffer() {
		StringBuffer questionBuffer = new StringBuffer();
		int paramNum = procedure.getInputParameters().size();
		for (int i = 0; i < paramNum; i++) {
			if (i == 0) {
				questionBuffer.append("?");
			} else {
				questionBuffer.append(",");
				questionBuffer.append("?");

			}
		}

		return questionBuffer;
	}
	
	private static final String COMMENT_BEGIN = "//�洢����׼��" + ITokenConstant.NL;
	private static final String CALL_BODY = "lpSP =  lpConn->createCallableStatement();"+ITokenConstant.NL
											+"if (lpSP)" + ITokenConstant.NL
											+"{" + ITokenConstant.NL
											+"iReturnCode = lpSP->prepare(\"?=call %1$s(%2$s)\");" + ITokenConstant.NL
											+"//ȡ�������"+ ITokenConstant.NL
											+"lpSP->registerOutParameter(1,HSQL_DATATYPE_INT,0); //��1���ʺ��Ƿ���ֵ" + ITokenConstant.NL
											+"//���������" + ITokenConstant.NL
											+"%3$s" + ITokenConstant.NL
											+"}" + ITokenConstant.NL
											+"else" + ITokenConstant.NL
											+"{" + ITokenConstant.NL
											+"iReturnCode = 102;" + ITokenConstant.NL
											+"@error_no = 102;" + ITokenConstant.NL
											+"hs_strcpy(@error_info, \"ICallableStatement is NULL��\");" + ITokenConstant.NL
											+"@error_id = 0;" + ITokenConstant.NL
											+"goto svr_end;" + ITokenConstant.NL+ITokenConstant.NL+"}";
	private static final String SET_VALUE = "lpSP->%1$s(%2$d,%3$s);" + ITokenConstant.NL;
	
	private static final String RESULTSET = "lpResultSet%1$s = lpSP->open();" + ITokenConstant.NL
											+"//�������" + ITokenConstant.NL 
											+"if (lpResultSet%1$s)" + ITokenConstant.NL
											+"{" + ITokenConstant.NL
											+"if (lpResultSet%1$s->GetColCount() == 5 && strcmp(lpResultSet%1$s->GetColName(0),\"error_no\") == 0 )" + ITokenConstant.NL
											+"{" + ITokenConstant.NL
											+"@error_no = lpResultSet%1$s->GetInt(\"error_no\");" + ITokenConstant.NL
											+"@error_id = lpResultSet%1$s->GetInt(\"error_id\");" + ITokenConstant.NL
											+"hs_strncpy(@error_sysinfo,lpResultSet%1$s->GetStr(\"error_sysinfo\"),500);" + ITokenConstant.NL
											+"hs_strncpy(@error_info,lpResultSet%1$s->GetStr(\"error_info\"),500);" + ITokenConstant.NL
											+"hs_strncpy(@error_pathinfo,lpResultSet%1$s->GetStr(\"error_pathinfo\"),500);" + ITokenConstant.NL
											+"GetErrorInfo(lpContext, lpResultSet%1$s->GetInt(\"error_no\"), @error_info);" + ITokenConstant.NL
											+"if ((lpResultSet%1$s->GetInt(\"error_no\") == 1) || (lpResultSet%1$s->GetInt(\"error_no\") == -1))" + ITokenConstant.NL
											+"iReturnCode = 100;" + ITokenConstant.NL
											+"else" + ITokenConstant.NL
											+"iReturnCode = lpResultSet%1$s->GetInt(\"error_no\");" + ITokenConstant.NL
											+"}" + ITokenConstant.NL
											+"}" + ITokenConstant.NL
											+"else" + ITokenConstant.NL
											+"{" + ITokenConstant.NL
											+"iReturnCode = 101;" + ITokenConstant.NL
											+"@error_no = 101;" + ITokenConstant.NL
											+"hs_strcpy(@error_info, \"Exec SP ERROR��%2$s\");" + ITokenConstant.NL
											+"@error_id = 0;" + ITokenConstant.NL
											+"%3$s" + ITokenConstant.NL
											+"}" + ITokenConstant.NL;

}
