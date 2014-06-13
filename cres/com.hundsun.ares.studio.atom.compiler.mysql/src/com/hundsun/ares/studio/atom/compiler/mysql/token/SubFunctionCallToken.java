/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.AtomService;
import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.AtomFunctionCompilerUtil;
import com.hundsun.ares.studio.atom.constants.IAtomRefType;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.util.ParamGroupUtil;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.constant.MarkConfig;
import com.hundsun.ares.studio.engin.parser.PseudoCodeParser;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;

/**
 * @author qinyuan
 *
 */
public class SubFunctionCallToken implements ICodeToken{
	
	//�Ӻ������ú�
	private IMacroToken token; 
	
	//�����õĺ�����Դ
	private IARESResource calledRes;
	//���õĺ�����Դ,��af/as��
	private AtomFunction callRes;
	
	//�Ӻ����������շ��ش���
	//[cName][ipField][ipValue][objID][, lpConn][resultset][errinfolength][��һ�б�־K����][��һ�б�־E����][svr_end][�����ȡֵ�޸ı��ر���][������,��F��]
	private final static String SUB_FUNC_CALL_RETURN_CODE = NEWLINE +"//�����Ӻ���:%1$s" + NEWLINE +
			"lpFuncInPacker->BeginPack();" + NEWLINE +
			"%2$s" + NEWLINE +
			"%3$s" + NEWLINE +
			"lpFuncInPacker->EndPack();" + NEWLINE +
			"//��������תΪ���������������ʹ��" + NEWLINE +
			"lpOut%4$s->BeginPack();" + NEWLINE +
			"iReturnCode = %12$s%4$s(lpContext,lpFuncInPacker->UnPack(),lpOut%4$s%5$s);" + NEWLINE +
			"lpOut%4$s->EndPack();" + NEWLINE +
			"lpResultSet%6$s = lpOut%4$s->UnPack();//������" + NEWLINE +
			"if ( 0 != iReturnCode&& iReturnCode != ERR_SYSWARNING)" + NEWLINE +
			"{" + NEWLINE +
			"@error_no = iReturnCode;" + NEWLINE +
			"hs_strncpy(@error_info, lpResultSet%6$s->GetStr(\"error_info\"),%7$s);" + NEWLINE +
			"%8$s" + NEWLINE +
			"%9$s" + NEWLINE +
			"%10$s" + NEWLINE +
			"}" + NEWLINE +
			"else" + NEWLINE +
			"{" + NEWLINE +
			"%11$s" + NEWLINE +
			"}" + NEWLINE;
	
	//chararry ���� ����ֶ�
	private final static String IP_PAKER_ADDFIELD_CHARARRAY = "lpFuncInPacker->AddField(\"%1$s\", \'S\', %2$s);";
	private final static String IP_PAKER_ADDFIELD_CHARARRAY2 = "lpFuncInPacker->AddField(\"%1$s\", \'S\');";
	//char ���� ����ֶ�
	private final static String IP_PAKER_ADDFIELD_CHAR = "lpFuncInPacker->AddField(\"%1$s\", \'C\');";
	//clob ���� ����ֶ�
	private final static String IP_PAKER_ADDFIELD_COLB = "lpFuncInPacker->AddField(\"%1$s\", \'R\', %2$s);";
	//float ���� ����ֶ�
	private final static String IP_PAKER_ADDFIELD_FLOAT = "lpFuncInPacker->AddField(\"%1$s\", \'D\', %2$s, %3$s);";
	//int ���� ����ֶ�
	private final static String IP_PAKER_ADDFIELD_INT = "lpFuncInPacker->AddField(\"%1$s\",\'I\');";
	//default ���� ����ֶ�
	private final static String IP_PAKER_ADDFIELD_DEFAULT = "lpFuncInPacker->AddField(\"%1$s\",\'S\');";
	//error_pathinfo �ֶ�  ���
	private final static String IP_PAKER_ADDFIELD_ERROR_PATHINFO = "lpFuncInPacker->AddField(\"error_pathinfo\", \'S\', %1$s);";
	
//	//int ���� ���ֵ
//	private final static String IP_PAKER_ADDVALUE_INT = "lpFuncInPacker->AddInt(%1$s);\t//%2$s";
//	//chararry ���� ���ֵ
//	private final static String IP_PAKER_ADDVALUE_CHARARRAY = "lpFuncInPacker->AddStr(%1$s);\t//%2$s";
//	//char ���� ���ֵ
//	private final static String IP_PAKER_ADDVALUE_CHAR = "lpFuncInPacker->AddChar(%1$s);\t//%2$s";
//	//float ���� ���ֵ
//	private final static String IP_PAKER_ADDVALUE_FLOAT = "lpFuncInPacker->AddDouble(%1$s);\t//%2$s";
	//error_pathinfo �ֶ�ֵ  ���
	private final static String IP_PAKER_ADDVALUE_ERROR_PATHINFO = "lpFuncInPacker->AddStr(%1$s);\t//%2$s";
	//clob ���� ���ֵ
	private final static String IP_PAKER_ADDVALUE_COLB = "lpFuncInPacker->AddRaw(%1$s,%2$s);\t//%3$s";
	//default ���� ���ֵ
	private final static String IP_PAKER_ADDVALUE_DEFAULT = "lpFuncInPacker->Add%1$s(%2$s);\t//%3$s";
	
	//��һ�б�־K����
	private final static String FIRST_ROW_FLAG_E = "EXEC SQL CLOSE cursor%s;";
	//��һ�б�־E����
	
	//proc����ع�
	private final static String ROLL_BACK = "EXEC SQL rollback;";
	//goto_svr_end
	private final static String goto_svr_end = "hs_strncpy(@error_pathinfo,lpResultSet%1$s->GetStr(\"error_pathinfo\"),%2$s);\r\n%3$s";
	
	//�����ȡֵ�޸ı��ر��� chararray����
	private final static String RESULTSET_GETVALUE_CHARARRAY = "hs_strncpy(%1$s, lpResultSet%2$s->GetStr(\"%3$s\"), sizeof(%1$s) - 1);";
	//�����ȡֵ�޸ı��ر��� clob����
	private final static String RESULTSET_GETVALUE_CLOB = "%1$s = lpResultSet%2$s->GetRaw(\"%3$s\",&%4$s);";
	//�����ȡֵ�޸ı��ر��� defaultĬ�����ͣ���Ҫ��Char\Double\Int
	private final static String RESULTSET_GETVALUE_DEAFULT = "%1$s = lpResultSet%2$s->Get%3$s(\"%4$s\");";
	private List<String> pseudoObjectParaList = new ArrayList<String>();
	
	/**
	 * �Ӻ�������
	 */
	public SubFunctionCallToken(IMacroToken token,IARESResource resource) {
		this.token = token;
		this.calledRes = resource;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#getContent()
	 */
	@Override
	public String getContent() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#getType()
	 */
	@Override
	public int getType() {
		return CODE_TEXT;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#genCode(java.util.Map)
	 */
	@Override
	public String genCode(Map<Object, Object> context) throws Exception {
		
		callRes = (AtomFunction)context.get(IAtomEngineContextConstantMySQL.ResourceModel);//������Դ���п�����af/as
		pseudoObjectParaList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_OBJECT_PARA_LIST);
		IARESProject project = (IARESProject)context.get(IAtomEngineContextConstantMySQL.Aresproject);
		AtomFunction atomFuc = calledRes.getInfo(AtomFunction.class);//�����õ�ԭ�Ӻ���
		List<Parameter> ipParas = new ArrayList<Parameter>();//�����õ�ԭ�Ӻ������������
		for(Parameter parameter:atomFuc.getInputParameters()){
			
			if(parameter.getParamType().getValue()== ParamType.PARAM_GROUP_VALUE){
				List<Parameter> parameters = new ArrayList<Parameter>();
				ParamGroupUtil.parserParamGroup(parameter, parameters,1, project);
				ipParas.addAll(parameters);
			}else{
				ipParas.add(parameter);
			}
			
		}
		List<Parameter> opParas = new ArrayList<Parameter>();//�����õ�ԭ�Ӻ������������
	    for(Parameter parameter:atomFuc.getOutputParameters()){
			
			if(parameter.getParamType().getValue()== ParamType.PARAM_GROUP_VALUE){
				List<Parameter> parameters = new ArrayList<Parameter>();
				ParamGroupUtil.parserParamGroup(parameter, parameters,1 ,project);
				opParas.addAll(parameters);
			}else{
				opParas.add(parameter);
			}
			
		}
		
		String[] macroParas = token.getParameters();//�����
		String defValuePara = "";//
		if(macroParas.length > 0) {
			defValuePara = macroParas[0];
		}
		//������ĺ�Ĭ��ֵ����
		Map<String, String> defaultValue = PseudoCodeParser.parserKeyValueWithAt(defValuePara);
		
		List<String> pararms = (List<String>) context.get(IAtomEngineContextConstantMySQL.PseudoCode_Para_LIST);
		return String.format(SUB_FUNC_CALL_RETURN_CODE, atomFuc.getChineseName(),genInputParamentPackAddField(ipParas,opParas,defaultValue,context),
				genInputParamentPackAddValue(ipParas, opParas,pararms , defaultValue,context),
				(StringUtils.isBlank(atomFuc.getObjectId())?atomFuc.getName():atomFuc.getObjectId())
				,deliverDBConnection(atomFuc ,project),
				(StringUtils.isBlank(atomFuc.getObjectId())?atomFuc.getName():atomFuc.getObjectId()),getFieldTypeLength("error_info"),
				getFirstRowFlagK(),getFirstRowFlagE(context),getGoToSvrEnd(context),resultSetGetValue(context,opParas, defaultValue),
				(StringUtils.isBlank(atomFuc.getObjectId())?"":"F"));
	}
	
	/**
	 * ��������ض���
	 * @param context ������
	 * @param opParas ���ú������������
	 * @param defaultValue �����-Ĭ��ֵ
	 * @return
	 * @throws Exception
	 */
	private String resultSetGetValue(Map<Object, Object> context,List<Parameter> opParas,Map<String, String> defaultValue) throws Exception {
		StringBuffer ret = new StringBuffer();
		for (Parameter parameter : opParas) {
			if((parameter.getFlags() != null) &&parameter.getFlags().contains(MarkConfig.MARK_IOFLAG)) {
				continue;
			}
			String outParamName = parameter.getId();
			String localVariableName ="";
			
			ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
			Set<String> rsList = (Set<String>)helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_VARIABLE_LIST);
			if(defaultValue.containsKey(outParamName)) {
				String stdFieldName = defaultValue.get(outParamName).replace("@", "");
				//TODO �ж�proc�����Լ���proc����
//				if (stdField != null || getFunction().getCommonInformation().getProc_declare_list().isContain(stdFieldName)
//						|| getFunction().getCommonInformation().getNon_proc_declare_list().isContain(stdFieldName))
				//if(rsList.contains(stdFieldName))
					localVariableName = defaultValue.get(outParamName);
			}else if(defaultValue.containsKey(MarkConfig.MARK_AT + outParamName)){
				String stdFieldName = defaultValue.get(MarkConfig.MARK_AT + outParamName).replace("@", "");
				//TODO �ж�proc�����Լ���proc����
				//if(rsList.contains(stdFieldName))
					localVariableName = defaultValue.get(outParamName);
			}else{
				continue;
			}
			
			if (localVariableName.indexOf(MarkConfig.MARK_AT) < 0)// û��@,��Ҫ����
			{
				localVariableName = "@" + localVariableName;
			}
			AtomFunction atomFuc = calledRes.getInfo(AtomFunction.class);//�����õ�ԭ�Ӻ���
			String resultSetId = StringUtils.isBlank(atomFuc.getObjectId())?atomFuc.getName():atomFuc.getObjectId();//�����Id
			if(!localVariableName.equals("")){
				StandardDataType stdType = null;
				String type = StringUtils.EMPTY;
				BusinessDataType busType = null;
				try {
					if(parameter.getParamType().getValue()!=ParamType.OBJECT_VALUE && parameter.getParamType().getValue()!=ParamType.PARAM_GROUP_VALUE){
						stdType = MetadataServiceProvider.getStandardDataTypeOfStdFieldByName(calledRes.getARESProject(), outParamName);
						if(stdType!=null){
							 type = stdType.getValue(MetadataServiceProvider.C_TYPE);
						}
						busType = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(calledRes.getARESProject(), outParamName);
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				int length = 0;
				if(busType!=null && busType.getLength() != null){
					try {
						length = Integer.parseInt(busType.getLength()) ;
					} catch (Exception e) {
						//throw new Exception(String.format("ҵ����������:%1$s�ĳ���Ϊ�Ƿ����֣�%2$s��", busType.getName(),busType.getLength()));
					}
					type = type.replace("$L", length + "");
				}
				if(TypeRule.typeRuleClob(type)|| parameter.getParamType().getValue()==ParamType.OBJECT_VALUE){// ��������ΪClob����Ϊ����
					String firstFlag = "";
					String firstIntFlag = "";
					String localVariableNameWithOutAT = StringUtils.replaceOnce(localVariableName, MarkConfig.MARK_AT, "").trim();
					String realParam = StringUtils.EMPTY;
					//���ڶ������⴦��
					if( StringUtils.endsWith(localVariableNameWithOutAT,"ResultSet")){
						realParam = StringUtils.substring(localVariableNameWithOutAT,0,StringUtils.indexOf(localVariableNameWithOutAT,"ResultSet")).trim();
					}else{
						realParam = localVariableNameWithOutAT;
					}
					//��������ж���
					if(AtomFunctionCompilerUtil.isParameterINInputAndOutputParameterByName(callRes,
							localVariableNameWithOutAT,calledRes.getARESProject()) ){
						firstFlag += "p_";
						firstIntFlag += "pi_";
						ret.append(String.format(RESULTSET_GETVALUE_CLOB, firstFlag + localVariableNameWithOutAT,resultSetId,
								outParamName,firstIntFlag + localVariableNameWithOutAT));
					}else if(AtomFunctionCompilerUtil.isParameterINInputAndOutputParameterByName(callRes,
							realParam,calledRes.getARESProject()) ){
						firstFlag += "p_";
						firstIntFlag += "pi_";
						ret.append(String.format(RESULTSET_GETVALUE_CLOB, firstFlag + realParam,resultSetId,
								outParamName,firstIntFlag + realParam));
					}else if( StringUtils.endsWith(localVariableNameWithOutAT,"ResultSet")&& AtomFunctionCompilerUtil.isParameterINInputAndOutputParameterByName(atomFuc,
							realParam,calledRes.getARESProject()) ){//�ڵ������������û��,������Ƕ������ڱ����õ���������������⴦��
						firstFlag += "p_";
						firstIntFlag += "pi_";
						ret.append(String.format(RESULTSET_GETVALUE_CLOB, firstFlag + realParam,resultSetId,
								outParamName,firstIntFlag + realParam));
					}else if(AtomFunctionCompilerUtil.isParameterINInputAndOutputParameterByName(atomFuc,
							realParam,calledRes.getARESProject()) ){//�ڵ������������û��,������Ƕ������ڱ����õ���������������⴦��
						firstFlag += "p_";
						firstIntFlag += "pi_";
						ret.append(String.format(RESULTSET_GETVALUE_CLOB, firstFlag + realParam,resultSetId,
								outParamName,firstIntFlag + realParam));
					}
					else {
						firstFlag += "v_";
						firstIntFlag += "vi_";
						ret.append(String.format(RESULTSET_GETVALUE_CLOB, firstFlag + localVariableNameWithOutAT,resultSetId,
								outParamName,firstIntFlag + localVariableNameWithOutAT));
					}
					
				}else if (TypeRule.typeRuleCharArray(type)){// ��������Ϊchar[]
					ret.append(String.format(RESULTSET_GETVALUE_CHARARRAY, localVariableName,resultSetId,outParamName));
				}else {
					if (TypeRule.typeRuleInt(type)){// ��������Ϊint
						ret.append(String.format(RESULTSET_GETVALUE_DEAFULT, localVariableName,resultSetId,"Int",outParamName));
					}else if (TypeRule.typeRuleChar(type)){// ��������Ϊchar
						ret.append(String.format(RESULTSET_GETVALUE_DEAFULT, localVariableName,resultSetId,"Char",outParamName));
					} else if (TypeRule.typeRuleDouble(type)){// ��������Ϊdouble
						ret.append(String.format(RESULTSET_GETVALUE_DEAFULT, localVariableName,resultSetId,"Double",outParamName));
					}
				}
			}
			ret.append(NEWLINE);
		}
		
		return ret.toString();
	}
	
	/**
	 * goto_svr_end����
	 * @return
	 */
	private String getGoToSvrEnd(Map<Object, Object> context) throws Exception{
		AtomFunction atomFuc = calledRes.getInfo(AtomFunction.class);//�����õ�ԭ�Ӻ���
		String resultSet = StringUtils.isBlank(atomFuc.getObjectId())?atomFuc.getName():atomFuc.getObjectId();//getAttrLastValue(context,IAtomEngineContextConstant.ATTR_FUNC_CALL);
		String error_pathinfo_length = getFieldTypeLength("error_pathinfo");
		String otherInfo = "goto svr_end;\r\n";
		if((token.getFlag() != null) && (token.getFlag() != "") && token.getFlag().contains(MarkConfig.MARK_IFLAG_M))
		{
			otherInfo = "";
		}else if((callRes.getInterfaceFlag() != null) && (callRes.getInterfaceFlag() != "") && callRes.getInterfaceFlag().contains(MarkConfig.MARK_IFLAG_M)){
			otherInfo = "";
		}
		
		return String.format(goto_svr_end, resultSet,error_pathinfo_length,otherInfo);
	}
	
	/**
	 * ��ȡ���������һ��ֵ
	 * @param context
	 * @param arg �����������ȡ����Ľ����ΪIAtomEngineContextConstant.ATTR_FUNC_RESULTSET
	 * 			<br>�ο�{@link com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant}
	 * @return
	 * @throws Exception
	 */
//	private String getAttrLastValue(Map<Object, Object> context,String arg) throws Exception{
//		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
//		Set<String> rsList = helper.getAttribute(arg);
//		int size = rsList.size();
//		String[] rss = rsList.toArray(new String[size]);
//		if(size > 0) {
//			IFunctionMacroTokenService fmservice = (IFunctionMacroTokenService)context.get(IAtomEngineContextConstant.Function_Macro_Service);
//			String functionName = rss[size - 1];
//			AtomFunction func = fmservice.getFunction(functionName).getInfo(AtomFunction.class);
//			return func.getObjectId();
//		}
//		return "";
//	}
	
	/**
	 * ��һ�б�־E����
	 * @return
	 */
	private String getFirstRowFlagE(Map<Object, Object> context) {
		if(token.getFlag().contains(MarkConfig.MARK_IFLAG_E)) {
			ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
			Set<String> curVars = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_CURSOR_LIST);
			if(curVars.size()>0){
				return String.format(FIRST_ROW_FLAG_E, (curVars.toArray(new String[0]))[curVars.size()-1]);
			}
			
		}
		
		return "";
	}
	
	/**
	 * ��һ�б�־K����
	 * @return
	 */
	private String getFirstRowFlagK() {
		if(token.getFlag().contains(MarkConfig.MARK_IFLAG_K)) {
			return ROLL_BACK;
		}
		return "";
	}
	
	/**
	 * ��ȡ�ֶ����ͳ���
	 * @param fieldName �ֶ���
	 * @return ����ֶ�Ϊ��׼�ֶΣ������ֶζ�Ӧ��׼���͵ĳ��ȣ����򷵻ء�500��
	 * @throws Exception
	 */
	private String getFieldTypeLength(String fieldName) throws Exception{
		StandardField field = MetadataServiceProvider.getMetadataModelByName(calledRes.getARESProject(), fieldName, IMetadataRefType.StdField, StandardField.class);

		if((null != field)) {
			BusinessDataType type = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(calledRes.getARESProject(), fieldName);
			if(type != null){
				return "500";
			}
			return type.getLength();
		}else{
			return "500";
		}
	}
	
	/**
	 * �������ݿ�����<br>
	 * �����õĺ����Ƿ��������������ͬ,���ұ����õĺ���������"V"��־���򴫵����ݿ�����
	 * @param atomFuc ���õ�ԭ�Ӻ���
	 * @return
	 */
	private String deliverDBConnection(AtomFunction atomFuc ,IARESProject project) {
		String database = "";
		if(atomFuc instanceof AtomService){
			database = AtomFunctionCompilerUtil.getAtomDatabase(project, atomFuc.getDatabase(), atomFuc.getChineseName(), IAtomRefType.ATOM_SERVICE_CNAME ,atomFuc.getInterfaceFlag());
		}else{
			database = AtomFunctionCompilerUtil.getAtomDatabase(project, atomFuc.getDatabase(), atomFuc.getChineseName(), IAtomRefType.ATOM_FUNCTION_CNAME ,atomFuc.getInterfaceFlag());
		}
		String ad = "";
		if(callRes instanceof AtomService){
			ad = AtomFunctionCompilerUtil.getAtomDatabase(project, callRes.getDatabase(), callRes.getChineseName(), IAtomRefType.ATOM_SERVICE_CNAME ,callRes.getInterfaceFlag());
		}else{
			ad = AtomFunctionCompilerUtil.getAtomDatabase(project, callRes.getDatabase(), callRes.getChineseName(), IAtomRefType.ATOM_FUNCTION_CNAME ,callRes.getInterfaceFlag());
		}
		
		if(StringUtils.isNotBlank(database) && StringUtils.equals(ad, database) && !StringUtils.equalsIgnoreCase(callRes.getInterfaceFlag(), MarkConfig.MARK_IFLAG_R)){
			if (StringUtils.isNotBlank(atomFuc.getInterfaceFlag()) && !atomFuc.getInterfaceFlag().contains(MarkConfig.MARK_IFLAG_V)) {
				return ", lpConn";
			}
			return ", lpConn";
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * ��������������ֶ�
	 * @param ipParas �������
	 * @param opParas �������
	 * @return
	 * @throws Exception
	 */
	private String genInputParamentPackAddField(List<Parameter> ipParas,List<Parameter> opParas,Map<String, String> defaultValue,Map<Object, Object> context)  throws Exception{
		StringBuffer ret = new StringBuffer();
		boolean isErrorPathInfo = false;
		
		//1.�����������
		for (Parameter parameter : ipParas) {
			setInputParaPackAddField(ret, parameter,defaultValue,context);
			if(parameter.getId().equals("error_pathinfo")){
				isErrorPathInfo = true;
			}
		}
		
		//2.�����д���IO�����������
		for (Parameter parameter : opParas) {
			if((parameter.getFlags() != null) && parameter.getFlags().toUpperCase().equals(MarkConfig.MARK_IOFLAG)) {
				setInputParaPackAddField(ret, parameter,defaultValue,context);
				if(parameter.getId().equals("error_pathinfo")){
					isErrorPathInfo = true;
				}
			}
		}
		
		//error_pathinfo ���⴦������������û�У�����ҪĬ�����
		if(!isErrorPathInfo) { 
			StandardField sField = MetadataServiceProvider.getMetadataModelByName(calledRes.getARESProject(), "error_pathinfo", IMetadataRefType.StdField, StandardField.class);
			//error_pathinfoΪ��׼�ֶΣ�����ҵ���������ʹ���
			if((sField != null)){
				BusinessDataType bType = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(calledRes.getARESProject(), "error_pathinfo");
				if(bType != null)
				{
					ret.append(String.format(IP_PAKER_ADDFIELD_ERROR_PATHINFO, bType.getLength()));
					ret.append(NEWLINE);
				}else{
					ret.append(String.format(IP_PAKER_ADDFIELD_ERROR_PATHINFO, 500));
					ret.append(NEWLINE);
				}
			}else{
				ret.append(String.format(IP_PAKER_ADDFIELD_ERROR_PATHINFO, 500));
				ret.append(NEWLINE);
			}
			
		}
		return ret.toString();
	}

	private void setInputParaPackAddField(StringBuffer ret, Parameter parameter, Map<String, String> defaultValue,Map<Object,Object> context)  throws Exception{
		String type = "";
		if(parameter.getParamType().getValue()!=ParamType.OBJECT_VALUE && parameter.getParamType().getValue()!=ParamType.PARAM_GROUP_VALUE){
			type = AtomFunctionCompilerUtil.getRealDataType(parameter, calledRes.getARESProject(),context);
		}
		
		/*if(parameter.getParamType().getValue()==ParamType.STD_FIELD_VALUE && StringUtils.isBlank(type)){
			throw new Exception("��ȡ����"+parameter.getId()+"���ͳ���,��ò����Ƿ��Ǳ�׼�ֶ��Լ���Ӧ�������Ƿ����");
		}*/
		Map<String,String> parameterInfo = AtomFunctionCompilerUtil.getStandardFieldParameterInfo(parameter.getId(), calledRes.getARESProject());
		 if (TypeRule.typeRuleChar(type)) {//�ַ�
			ret.append(String.format(IP_PAKER_ADDFIELD_CHAR, parameter.getId()));
		}else if (TypeRule.typeRuleDouble(type) ||TypeRule.typeRuleFloat(type)) {//Double����Float
			String length = NumberUtils.toInt(StringUtils.defaultIfBlank(parameterInfo.get("length"), ""),0)+"";
			String precision = StringUtils.defaultIfBlank(parameterInfo.get("precision"), "");
			ret.append(String.format(IP_PAKER_ADDFIELD_FLOAT, parameter.getId(),length,
					precision));
		}else if (TypeRule.typeRuleInt(type)) {
			ret.append(String.format(IP_PAKER_ADDFIELD_INT, parameter.getId()));
		}else if (parameter.getParamType().getValue()==ParamType.OBJECT_VALUE||TypeRule.typeRuleClob(type)) {//����
			String paramNameOrValue = StringUtils.defaultIfBlank(defaultValue.get(parameter.getId()), "");//����������Ĭ��ֵ
			String objectName = StringUtils.replaceOnce(paramNameOrValue, MarkConfig.MARK_AT, "").trim();
			if(StringUtils.endsWith(objectName, "ResultSet")){//����֧�ֱ���
				objectName = StringUtils.substring(objectName, 0,StringUtils.lastIndexOf(objectName, "ResultSet"));
			}else{
				if(StringUtils.isBlank(paramNameOrValue)){
					objectName = parameter.getId();
				}
				
			}
			ret.append(String.format(IP_PAKER_ADDFIELD_COLB, parameter.getId(), "pi_" + objectName));
		}else if (TypeRule.typeRuleCharArray(type) && TypeRule.greaterThan255(type)) {//�ַ����������ͣ��ҳ��ȴ���255
			String length = StringUtils.defaultIfBlank(parameterInfo.get("length"), "");
			ret.append(String.format(IP_PAKER_ADDFIELD_CHARARRAY, parameter.getId(), length));
		}else if(TypeRule.typeRuleCharArray(type) ) {//�ַ����������ͳ���С�ڵ���255
			ret.append(String.format(IP_PAKER_ADDFIELD_CHARARRAY2, parameter.getId()));
		} else {//Ĭ��Ϊ�ַ������ͳ���С�ڵ���255
			ret.append(String.format(IP_PAKER_ADDFIELD_DEFAULT, parameter.getId()));
		}
		ret.append(NEWLINE);
		
	}
	
	/**
	 * �������������ֵ
	 * @param ipParas �������
	 * @param opParas �������
	 * @param pararms α�����еĲ�������"@"
	 * @param defaultValue �����-Ĭ��ֵ
	 * @return
	 * @throws Exception
	 */
	private String genInputParamentPackAddValue(List<Parameter> ipParas,List<Parameter> opParas,List<String> pararms ,Map<String, String> defaultValue,Map<Object,Object> context)  throws Exception{
		
		StringBuffer ret = new StringBuffer();
		boolean isErrorPathInfo = false;
		//1.�����������
		for (Parameter parameter : ipParas) {
			setInputParaPackAddValue(ret, defaultValue, parameter ,pararms,context);
			if(parameter.getId().equals("error_pathinfo")){
				isErrorPathInfo = true;
			}
		}
		
		//2.�����д���IO�����������
		for (Parameter parameter : opParas) {
			if((parameter.getFlags() != null) && parameter.getFlags().toUpperCase().equals(MarkConfig.MARK_IOFLAG)) {
				setInputParaPackAddValue(ret, defaultValue, parameter ,pararms,context);
				if(parameter.getId().equals("error_pathinfo")){
					isErrorPathInfo = true;
				}
			}
		}
		
		//3.error_pathinfo ���⴦������������û�У�����ҪĬ�����
		if(!isErrorPathInfo) { 
//			StandardDataType stdType = MetadataServiceProvider.getStandardDataTypeOfStdFieldByName(calledRes.getARESProject(), "error_pathinfo");
//			String realType = stdType.getValue(MetadataServiceProvider.C_TYPE);
			if(((token.getFlag() != null) && token.getFlag().contains(MarkConfig.MARK_IFLAG_M)) || ((callRes.getInterfaceFlag() != null) && callRes.getInterfaceFlag().contains(MarkConfig.MARK_IFLAG_M))){
				ret.append(String.format(IP_PAKER_ADDVALUE_ERROR_PATHINFO,"v_error_pathinfo_tmp" ,"v_error_pathinfo_tmp"));
			}else {
				ret.append(String.format(IP_PAKER_ADDVALUE_ERROR_PATHINFO,"@error_pathinfo" ,"error_pathinfo"));
			}
			ret.append(NEWLINE);
		}
		
		return ret.toString();
	}
	
	private void setInputParaPackAddValue(StringBuffer ret, Map<String, String> defaultValue, Parameter parameter ,List<String> pararms,Map<Object,Object> context) throws Exception{
		String paraName = parameter.getId();//������
		String type = "";
		if(parameter.getParamType().getValue()!=ParamType.OBJECT_VALUE && parameter.getParamType().getValue()!=ParamType.PARAM_GROUP_VALUE){
			type = AtomFunctionCompilerUtil.getRealDataType(parameter, calledRes.getARESProject(),context);
		}
		
		String paramNameOrValue = defaultValue.get(paraName);//����������Ĭ��ֵ
		if(paramNameOrValue != null) {//Ĭ��ֵ����
			if(paramNameOrValue.indexOf(MarkConfig.MARK_AT) < 0) {//Ĭ��ֵ������@�������ȡ��ʵֵ
				paramNameOrValue = AtomFunctionCompilerUtil.getTrueDefaultValueByType(type, paramNameOrValue, calledRes.getARESProject());
			}
		}else {
			
			if (SPECIAL_PARAM_SUBSTITUTE.containsKey(paraName)) {
				// �ò����ı����Ϊ��һ������
				paraName = SPECIAL_PARAM_SUBSTITUTE.get(paraName);
			}
			
			if(AtomFunctionCompilerUtil.isParameterINAtomFunctionParameterByName(callRes, paraName,calledRes.getARESProject()) || pseudoObjectParaList.contains(paraName)|| isPsoudoCode(pararms, paraName)) {
				//�����ڵ��ú����ı�����
				paramNameOrValue = MarkConfig.MARK_AT + paraName.trim();
			}else {
				// ���������������⴦��
				StringBuffer spDftValue = new StringBuffer();
				if(!beforeAddValueUseDefaultValueV2(paraName,spDftValue)) {
					if((parameter.getParamType() == ParamType.STD_FIELD) || (parameter.getParamType() == ParamType.NON_STD_FIELD))//��׼�ֶβ�����Ǳ����
					{
						String bizTypeName = "";
						if(parameter.getParamType() == ParamType.STD_FIELD)//��׼�ֶβ���
						{
							StandardField stdfield = MetadataServiceProvider.getStandardFieldByNameNoExp(calledRes.getARESProject(), parameter.getId());//getIdΪ��������getNameΪ������
							if(stdfield == null){
								ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
								String message = String.format("����[%1$s]��Ӧ�ı�׼�ֶβ����ڡ�", parameter.getId());
								manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
								return ;//��׼�ֶβ�����ʱ���������ɣ���������д�����Ϣ���׳���
							}
							bizTypeName = stdfield.getDataType();//��׼�ֶ�ʱ��ȡ��׼�ֶζ�Ӧҵ������
						}else if(parameter.getParamType() == ParamType.NON_STD_FIELD){//�Ǳ����
							bizTypeName = parameter.getType();//�Ǳ��ֶ�ʱ��ȡ������ֱ�������ҵ������
						}
						int length = 0;
						BusinessDataType bizType = MetadataServiceProvider.getBusinessDataTypeByNameNoExp(calledRes.getARESProject(), bizTypeName);//���ﲻ��ʹ��param.getType()�����������Ǳ�ʱ��ȡ����ҵ�����͵��쳣
						if(bizType == null){
							ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
							String message = String.format("����[%1$s]��Ӧ��ҵ������[%2$s]�����ڡ�", parameter.getId(),bizTypeName);
							manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
							return ;//ҵ�����Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
						}
						try {
							length = Integer.parseInt(bizType.getLength()) + 1;//����Char�������ʱ������Ҫ��1
						} catch (Exception e) {
							ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
							String message = String.format("����[%1$s]��Ӧ��ҵ������:%2$s�ĳ���Ϊ�Ƿ����֣�%3$s��", parameter.getId(),bizTypeName,bizType.getLength());
							manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
							return ;//ҵ�����ͳ���Ϊ�Ƿ�����ʱ���������ɣ���������д�����Ϣ���׳���
						}
						StandardDataType stdType = MetadataServiceProvider.getStandardDataTypeByNameNoExp(calledRes.getARESProject(), bizType.getStdType());
						if(stdType == null){
							ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
							String message = String.format("����[%1$s]��Ӧ��ҵ������[%2$s]�еı�׼����[%3$s]�����ڡ�", parameter.getId(),bizTypeName,bizType.getStdType());
							manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
							return ;//��׼���Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
						}
						String dataType = StringUtils.defaultIfBlank(stdType.getValue(MetadataServiceProvider.C_TYPE), "");
						dataType = dataType.replace("$L", length + "");
						if(TypeRule.typeRuleCharArray(dataType)){//�ַ�����������
							spDftValue.append("\" \"");
						}else{
							//��������ж�Ӧ��Ĭ��ֵ��Ϊ�գ���Ĭ��ֵ�Ը�ֵΪ׼��ע����������ʹ�ñ�׼Ĭ��ֵ��ͬʱҲ��������ʵĬ��ֵ��
							if(StringUtils.isNotEmpty(parameter.getDefaultValue())){
								TypeDefaultValue typpeDefValue = MetadataServiceProvider.getTypeDefaultValueByNameNoExp(calledRes.getARESProject(), parameter.getDefaultValue());
								//����Ҳ�����׼Ĭ��ֵ����ͳһ����ʵĬ��ֵ�����û�����ʲô�������ʲô
								if(typpeDefValue == null){
									spDftValue.append(parameter.getDefaultValue());
								}else{
									String defValue = typpeDefValue.getValue(MetadataServiceProvider.C_TYPE);
									spDftValue.append(defValue);
								}
							}
							//������Ĭ��ֵΪ�գ�ȡҵ�����Ͷ�Ӧ�ı�׼Ĭ��ֵ����������Ǳ�׼Ĭ��ֵ��������Ҫ����
							else{
								TypeDefaultValue typpeDefValue = MetadataServiceProvider.getTypeDefaultValueByNameNoExp(calledRes.getARESProject(), bizType.getDefaultValue());
								if(typpeDefValue == null){
									ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
									String message = String.format("����[%1$s]��Ӧ��ҵ������[%2$s]�еı�׼Ĭ��ֵ[%3$s]�����ڡ�", parameter.getId(), bizTypeName,bizType.getDefaultValue());
									manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
									return ;//��׼Ĭ��ֵ������ʱ���������ɣ���������д�����Ϣ���׳���
								}
								String defValue = typpeDefValue.getValue(MetadataServiceProvider.C_TYPE);
								spDftValue.append(defValue);
							}
						}
					}
					

				}
				paramNameOrValue = spDftValue.toString();
			}
		}
		
		
		if(StringUtils.equals("error_pathinfo", paraName) && (
				token.getFlag().contains(MarkConfig.MARK_IFLAG_M) || callRes.getInterfaceFlag().contains(MarkConfig.MARK_IFLAG_M))) {
			ret.append("lpFuncInPacker->AddStr(v_error_pathinfo1);"); 
		}else {
			
			if(parameter.getParamType().getValue()==ParamType.OBJECT_VALUE || TypeRule.typeRuleClob(type)){
				String firstFlag = "";
				String firstIntFlag = "";
				List<Parameter> inputParameters = new ArrayList<Parameter>();
				ParamGroupUtil.parserParameters(callRes.getInputParameters(), inputParameters, calledRes.getARESProject());
				List<Parameter> outParameters = new ArrayList<Parameter>();
				ParamGroupUtil.parserParameters(callRes.getOutputParameters(), outParameters, calledRes.getARESProject());
				if(parameter.getParamType().getValue()==ParamType.OBJECT_VALUE|| inputParameters.contains(parameter)|| outParameters.contains(parameter)) {
					firstFlag += "p_";
					firstIntFlag += "pi_";
				}else {
					firstFlag += "v_";
					firstIntFlag += "vi_";
				}
				if(parameter.getParamType().getValue()==ParamType.OBJECT_VALUE){
					String objectName = StringUtils.replaceOnce(paramNameOrValue, MarkConfig.MARK_AT, "").trim();
					if(StringUtils.endsWith(objectName, "ResultSet")){//����֧�ֱ���
						objectName = StringUtils.substring(objectName, 0,StringUtils.lastIndexOf(objectName, "ResultSet"));
						paraName = objectName;
					}else{
						paraName = objectName;
					}
					ret.append(String.format(IP_PAKER_ADDVALUE_COLB, firstFlag + paraName, firstIntFlag + paraName, paraName));
				}else{
					ret.append(String.format(IP_PAKER_ADDVALUE_COLB, firstFlag + parameter.getId(), firstIntFlag + paramNameOrValue, paraName));
				}
				
			}else {
				if (TypeRule.typeRuleInt(type)) {
					ret.append(String.format(IP_PAKER_ADDVALUE_DEFAULT, "Int",paramNameOrValue,paraName));
				} else if (TypeRule.typeRuleDouble(type)) {
					ret.append(String.format(IP_PAKER_ADDVALUE_DEFAULT, "Double",paramNameOrValue,paraName));
				} else if (TypeRule.typeRuleChar(type)) {
					ret.append(String.format(IP_PAKER_ADDVALUE_DEFAULT, "Char",paramNameOrValue,paraName));
				} else {
					ret.append(String.format(IP_PAKER_ADDVALUE_DEFAULT, "Str",paramNameOrValue,paraName));
				}
			}
		}
		ret.append(NEWLINE);
	}
	
	private boolean isPsoudoCode(List<String> pararms , String paramName){
		for(String param : pararms){
			if (StringUtils.equals(param, paramName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ��������������Ҫ�����⴦����ֶ�<BR>
	 * Ҳ���ǵ�ǰ��û�б�ָ��ʱ������Ϊ���ߵ���ݽ��н���
	 * 
	 */
	private final static HashMap<String, String> SPECIAL_PARAM_SUBSTITUTE = new HashMap<String, String>();
	static {
		SPECIAL_PARAM_SUBSTITUTE.put("op_station_on", "station_no");
	}
	
	/**
	 * ���������������⴦��
	 * ����������ֶδ����ֵ��ʹ������ֵ
	 * @param fieldName
	 * @param valueBuffer
	 * @return
	 */
	private boolean beforeAddValueUseDefaultValueV2(String fieldName, StringBuffer valueBuffer) {
		/*
		 * '20050126 zhouwm ����_���÷����Ӧ����Ҫ����� ��ʼ 'BodyCodeCol.Add (Space(iSpace) &
		 * "lpFuncInPacker->AddValue(""" & ParamInitValue(sProcParamType) & """);") If
		 * (InStr(sProcParamName, "inout_flag") > 0) Then BodyCodeCol.Add
		 * (Space(iSpace) & "lpFuncInPacker->AddValue(0);") ElseIf
		 * (InStr(sProcParamName, "param_name") > 0) Then BodyCodeCol.Add
		 * (Space(iSpace) &
		 * "lpFuncInPacker->AddValue(lpInUnPackerParent->getColName(v_i));") ElseIf
		 * (InStr(sProcParamName, "param_type") > 0) Then BodyCodeCol.Add
		 * (Space(iSpace) &
		 * "lpFuncInPacker->AddValue(lpInUnPackerParent->getColType(v_i));") ElseIf
		 * (InStr(sProcParamName, "param_width") > 0) Then BodyCodeCol.Add
		 * (Space(iSpace) &
		 * "lpFuncInPacker->AddValue(lpInUnPackerParent->getColWidth(v_i));") ElseIf
		 * (InStr(sProcParamName, "param_scale") > 0) Then BodyCodeCol.Add
		 * (Space(iSpace) &
		 * "lpFuncInPacker->AddValue(lpInUnPackerParent->getColScale(v_i));") ElseIf
		 * (InStr(sProcParamName, "param_value") > 0) Then BodyCodeCol.Add
		 * (Space(iSpace) &
		 * "lpFuncInPacker->AddValue(lpInUnPackerParent->getString(lpInUnPackerParent->getColName(v_i)));")
		 * Else BodyCodeCol.Add (Space(iSpace) & "lpFuncInPacker->AddValue(""" &
		 * ParamInitValue(sProcParamType) & """);") End If '20050126 zhouwm
		 * ����_���÷����Ӧ����Ҫ����� ����
		 */

		// 10.20MXH�޸� ���Ӷ԰汾���ж�
		if (fieldName.indexOf("inout_flag") != -1) {
			valueBuffer.append("0");
			return true;
		} else if (fieldName.indexOf("param_name") != -1) {
			valueBuffer.append("lpInUnPackerParent->GetColName(v_i)");
			return true;
		} else if (fieldName.indexOf("param_type") != -1) {
			valueBuffer.append("lpInUnPackerParent->GetColType(v_i)");
			return true;
		} else if (fieldName.indexOf("param_width") != -1) {
			valueBuffer.append("lpInUnPackerParent->GetColWidth(v_i)");
			return true;
		} else if (fieldName.indexOf("param_scale") != -1) {
			valueBuffer.append("lpInUnPackerParent->GetColScale(v_i)");
			return true;
		} else if (fieldName.indexOf("param_value") != -1) {
			valueBuffer.append("lpInUnPackerParent->GetStr(lpInUnPackerParent->GetColName(v_i))");
			return true;
		}

		return false;
	}
	
}
