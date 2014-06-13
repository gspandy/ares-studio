package com.hundsun.ares.studio.atom.compiler.mysql.token;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.AtomFunctionCompilerUtil;
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
	String stateID;
	
	public ProcedureCallStaticToken(Procedure procedure, IMacroToken token, String rsID, String stateID) {
		this.procedure = procedure;
		this.token = token;
		this.rsID = rsID;
		this.stateID = stateID;
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
		StringBuffer registersetsetBuffer = new StringBuffer();
		StringBuffer getBuffer = new StringBuffer();
		String proName = procedure.getName();
		String[] params = token.getParameters();
		Map<String, String> values = new HashMap<String, String>();
		if(params.length>0){
			values.putAll(PseudoCodeParser.parserKeyValueWithAt(params[0]));
		}
		int index = 1;
		Map<String,Integer> out_indexs = new HashMap<String,Integer>();
		Map<String,Integer> in_indexs = new HashMap<String,Integer>();
		//�ȴ��������������洢���̶��屣��һ��
		for(Parameter  param: procedure.getInputParameters()){
			if(!in_indexs.containsKey(param.getId())){
				if(!out_indexs.containsKey(param.getId())){//������ţ���Ҫ���ã������Ѿ��ж��Ƿ��ظ������Խ�������϶������ظ�
					String flag = param.getFlags();
					//IO��־���������
					if(StringUtils.isNotBlank(flag) && StringUtils.indexOf(flag, "IO") >= 0){
						getBuffer.append(getInitCode(param,context,stateID,index));
						registersetsetBuffer.append(getOutParameterRegisterCode(param,context,stateID,index));
					}
					in_indexs.put(param.getId(), index);//��¼��ţ�������ʱ���ò��ϣ���������ظ���飬���ֿ�������
					registersetsetBuffer.append(getSetValueString(param, values, context, index));
					index++;
				}else{//�����������
					registersetsetBuffer.append(getSetValueString(param, values, context, out_indexs.get(param.getId())));
					//������������Ѿ�ע�ᣬ�ʼ�ʹIO������Ҳ��������
				}
			}
		}
		registersetsetBuffer.append(ITokenConstant.NL);//����������������֮�䣬�ÿ��зָ�
		//����������������ٴ����������
		for(Parameter param : procedure.getOutputParameters()){
			if(!out_indexs.containsKey(param.getId())){//���ظ����
				out_indexs.put(param.getId(), index);//��¼��ţ��������ظ��ģ��踴�����
				getBuffer.append(getInitCode(param,context,stateID,index));
				registersetsetBuffer.append(getOutParameterRegisterCode(param,context,stateID,index));
				String flag = param.getFlags();
				//IO��־���������
				if(StringUtils.isNotBlank(flag) && StringUtils.indexOf(flag, "IO")>=0){//IO�������
					if(!in_indexs.containsKey(param.getId())){//���ظ����
						in_indexs.put(param.getId(), index);//��¼��ţ�������ʱ���ò��ϣ���������ظ���飬���ֿ�������
						registersetsetBuffer.append(getSetValueString(param, values, context, index));
					}
				}
				index++;//��ż�1
			}
		}
		if(procedure.isOutputCollection()){//�����
			String resultSetBufffer = "";
			if(token.getFlag().indexOf("M") < 0){
				resultSetBufffer = String.format(RESULTSET, rsID,proName,stateID);
			}else{
				resultSetBufffer = String.format(RESULTSET_NOEND, rsID,proName,stateID);
			}
			buffer.append(COMMENT_BEGIN);
			buffer.append(String.format(CALL_BODY, proName,getQuestionMarkBuffer(),registersetsetBuffer.toString() + resultSetBufffer,stateID));
		}else{//�����ؽ����
			String execBufffer = "";
			if(token.getFlag().indexOf("M") < 0){
				execBufffer = String.format(EXEC, stateID,getBuffer.toString(),proName);
			}else{
				execBufffer = String.format(EXEC_NOEND, stateID,getBuffer.toString(),proName);
			}
			buffer.append(COMMENT_BEGIN);
			buffer.append(String.format(CALL_BODY, proName,getQuestionMarkBuffer(),registersetsetBuffer.toString() + execBufffer,stateID));
		}
		
		return buffer.toString();
	}
	
	private String getSetValueString(Parameter param,Map<String, String> values,Map<Object, Object> context,int index) throws Exception{
		IARESProject project = (IARESProject) context.get(IAtomEngineContextConstantMySQL.Aresproject);
		AtomFunction callRes = (AtomFunction) context.get(IAtomEngineContextConstantMySQL.ResourceModel);
		List<String> list = (List<String>) context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		String id = param.getId();
		String type = getRealType(param, project,context);
		String setType = getSetType(type);
		if(values.containsKey(id)){
			//��Ĭ��ֵ
			String value = values.get(id);
			return String.format(SET_VALUE,setType,index,getRealFieldName(value,type,project),this.stateID);
		}else if(AtomFunctionCompilerUtil.isParameterINAtomFunctionParameterByName(callRes, param.getId(),project) || 
					list.contains(id)){
			//��������������ڲ������л���α������ʹ�õ��ı�������ֱ���ڱ�����ǰ����@
			String value = "@" + id;
			return String.format(SET_VALUE,setType,index,value,stateID);
		}else{
			//����ֱ��ȡĬ��ֵ
			TypeDefaultValue typeDftValue = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName(project, id);
			String dv = typeDftValue.getValue(MetadataServiceProvider.C_TYPE);
			if(TypeRule.typeRuleCharArray(type))
			{
				dv = "\" \"";
			}
			return String.format(SET_VALUE,setType,index,AtomFunctionCompilerUtil.getTrueDefaultValueByType(type, dv,project),stateID);
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
	private String getRealType(Parameter param,IARESProject project,Map<Object,Object> context){
		return AtomFunctionCompilerUtil.getRealDataType(param.getId(), project,context);
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
	
	/**
	 * 
	 * @return String ���̵��õĲ���?��
	 */
	private StringBuffer getQuestionMarkBuffer() {
		StringBuffer questionBuffer = new StringBuffer();
		int paramNum = procedure.getInputParameters().size();
		Set<String> in_names = new HashSet<String>();
		for (int i = 0; i < paramNum; i++) {
			if(!in_names.contains(procedure.getInputParameters().get(i).getId())){
				in_names.add(procedure.getInputParameters().get(i).getId());//������ظ���
				if (i == 0) {
					questionBuffer.append("?");
				} else {
					questionBuffer.append(",");
					questionBuffer.append("?");
	
				}
			}
		}
		Set<String> out_names = new HashSet<String>();
		for (int i = 0; i < procedure.getOutputParameters().size(); i++) {
			if(!out_names.contains(procedure.getOutputParameters().get(i).getId())){
				out_names.add(procedure.getOutputParameters().get(i).getId());//������ظ���
				if(!in_names.contains(procedure.getOutputParameters().get(i).getId())){//��������Ѿ����
					if (questionBuffer.toString().equals("")) {
						questionBuffer.append("?");
					} else {
						questionBuffer.append(",");
						questionBuffer.append("?");
					}
				}
			}
		}

		return questionBuffer;
	}
	
	/**
	 * �����ؽ����������£��������������ֵ
	 * @param param ����������п�����IO�����룩
	 * @param context ������
	 * @param stateId ��̬���Id
	 * @return String һ����������ı�����ֵ���
	 */
	private String getInitCode(Parameter param,Map<Object,Object> context,String stateId,int index){
		IARESProject project = (IARESProject)context.get(IAtomEngineContextConstantMySQL.Aresproject);
		String dataType = AtomFunctionCompilerUtil.getRealDataType(param,project,context);
		if(TypeRule.typeRuleCharArray(dataType)){//�ַ���
			String len = TypeRule.getCharLength(dataType);
			//����Ż�ȡֵ
			return "hs_strncpy(@" + param.getId() + ",conversion((char *)lpSP" + stateId + "->getString(" + index + "))," + len + ");\r\n";
		}else if(TypeRule.typeRuleChar(dataType)){//�ַ�
			return "@" + param.getId() + " = lpSP" + stateId + "->getChar(" + index + ");\r\n";
		}else if(TypeRule.typeRuleDouble(dataType)){//������
			return "@" + param.getId() + " = lpSP" + stateId + "->getDouble(" + index + ");\r\n";
		}else if(TypeRule.typeRuleInt(dataType)){//����
			return "@" + param.getId() + " = lpSP" + stateId + "->getInt(" + index + ");\r\n";
		}else{
			return "";
		}
	}
	
	/**
	 * �������ע��
	 * @param param ����������п�����IO�����룩
	 * @param context ������
	 * @param stateId ��̬���Id
	 * @return String һ�����������ע�����
	 */
	private String getOutParameterRegisterCode(Parameter param,Map<Object,Object> context,String stateId,int index){
		IARESProject project = (IARESProject)context.get(IAtomEngineContextConstantMySQL.Aresproject);
		String dataType = AtomFunctionCompilerUtil.getRealDataType(param,project,context);
		if(TypeRule.typeRuleCharArray(dataType)){//�ַ���
			String len = TypeRule.getCharLength(dataType);
			return "lpSP" + stateId + "->registerOutParameter(" + index + ",HSQL_DATATYPE_STRING," + len + ");//" + param.getId() + "\r\n";
		}else if(TypeRule.typeRuleChar(dataType)){//�ַ�
			return "lpSP" + stateId + "->registerOutParameter(" + index + ",HSQL_DATATYPE_CHAR,0);//" + param.getId() + "\r\n";
		}else if(TypeRule.typeRuleDouble(dataType)){//������
			return "lpSP" + stateId + "->registerOutParameter(" + index + ",HSQL_DATATYPE_DOUBLE,0);//" + param.getId() + "\r\n";
		}else if(TypeRule.typeRuleInt(dataType)){//����
			return "lpSP" + stateId + "->registerOutParameter(" + index + ",HSQL_DATATYPE_INT,0);//" + param.getId() + "\r\n";
		}else{
			return "";
		}
	}
	
	private static final String COMMENT_BEGIN = "//�洢����׼��" + ITokenConstant.NL;
	private static final String CALL_BODY = "lpSP%4$s =  lpConn->createCallableStatement();"+ITokenConstant.NL
											+"if (lpSP%4$s)" + ITokenConstant.NL
											+"{" + ITokenConstant.NL
											+"iReturnCode = lpSP%4$s->prepare(\"call %1$s(%2$s)\");" + ITokenConstant.NL
											//+"lpSP%4$s->registerOutParameter(1,HSQL_DATATYPE_INT,0); //��1���ʺ��Ƿ���ֵ" + ITokenConstant.NL
											+"%3$s" + ITokenConstant.NL
											+"}" + ITokenConstant.NL
											+"else" + ITokenConstant.NL
											+"{" + ITokenConstant.NL
											+"iReturnCode = 102;" + ITokenConstant.NL
											+"@error_no = 102;" + ITokenConstant.NL
											+"hs_strcpy(@error_info, \"ICallableStatement is NULL��\");" + ITokenConstant.NL
											+"@error_id = 0;" + ITokenConstant.NL
											+"goto svr_end;" + ITokenConstant.NL+ITokenConstant.NL+"}";
	
	private static final String SET_VALUE = "lpSP%4$s->%1$s(%2$d,%3$s);" + ITokenConstant.NL;
	
	private static final String RESULTSET = "lpResultSet%1$s = lpSP%3$s->open();" + ITokenConstant.NL
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
											+"goto svr_end;" + ITokenConstant.NL
											+"}" + ITokenConstant.NL;
	
	private static final String RESULTSET_NOEND = "lpResultSet%1$s = lpSP%3$s->open();" + ITokenConstant.NL
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
												+"}" + ITokenConstant.NL;
	
	private static final String EXEC = "iReturnCode = lpSP%1$s->exec();\r\n" + 
									   "if(!iReturnCode){\r\n" + 
									   "%2$s\r\n" + 
									   "}else\r\n" + 
									   "{\r\n" + 
									   "iReturnCode = 101;\r\n" + 
									   "@error_no = 101;\r\n" + 
									   "hs_strcpy(@error_info, \"Exec SP ERROR��%3$s\");\r\n" + 
									   "@error_id = 0;\r\n" +
									   "goto svr_end;\r\n" + 
									   "}\r\n";
	
	private static final String EXEC_NOEND = "iReturnCode = lpSP%1$s->exec();\r\n" + 
										   "if(!iReturnCode){\r\n" + 
										   "%2$s\r\n" + 
										   "}else\r\n" + 
										   "{\r\n" + 
										   "iReturnCode = 101;\r\n" + 
										   "@error_no = 101;\r\n" + 
										   "hs_strcpy(@error_info, \"Exec SP ERROR��%3$s\");\r\n" + 
										   "@error_id = 0;\r\n" +
										   "}\r\n";

}
