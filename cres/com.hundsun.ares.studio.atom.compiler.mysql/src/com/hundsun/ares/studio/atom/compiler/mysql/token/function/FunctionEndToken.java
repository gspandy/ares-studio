/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.token.function;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.func.IFunctionMacroTokenService;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.AtomFunctionCompilerUtil;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.SerEndHelper;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.util.ParamGroupUtil;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.constant.MarkConfig;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;

/**
 * @author zhuyf
 *
 */
public class FunctionEndToken implements ICodeToken {

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
		/*
		 * 	goto svr_end;
			svr_end:
			
				if (iReturnCode == OK_SUCCESS || iReturnCode == ERR_SYSWARNING)
				{
					[���������]
				}
				else
				{
					GetErrorInfo(lpContext, v_error_no, v_error_info);
					SystemErrorPacker(lpOutPacker,v_error_pathinfo,v_error_no,v_error_info);
					WriteSystemLog(lpContext,v_error_pathinfo,v_error_no,v_error_info,v_error_id,v_error_sysinfo);
				}
				[lpFuncInPacker�ͷ�]
				[lpOut�ͷ�]
				[lpSP�ͷ�]
				[pack�����ͷ�]
				[�ͷ�����]
				return iReturnCode;
			}
		 */
		/*
		 * [���������]
		 * �������̣�
			����ú������ǽ�������غ��������������������������
			����ú����ǽ�������غ����������һ�������Ľ������Ϊ�����������
			����ú����ڰ�����[PROC���������]�꣬��������۽�����Ƿ񷵻أ��������д�������
			������������������������error_pathinfo/error_no/error_info/hfare�����а���IO�����������
			lpOutPacker->AddField("error_pathinfo", 'S', 500);
			lpOutPacker->AddField("error_no");
			lpOutPacker->AddField("error_info", 'S', 500);
			lpOutPacker->AddField("hfare", 'D', 21,2);
			lpOutPacker->AddStr(p_error_pathinfo);//[�ֶ�ע��]
			lpOutPacker->AddInt(p_error_no);//[�ֶ�ע��]
			lpOutPacker->AddStr(p_error_info);//[�ֶ�ע��]
			lpOutPacker->AddDouble(p_hfare);//[�ֶ�ע��]
		 */
		/*
		 * [lpFuncInPacker�ͷ�]������������䣺�������ں�������ʱ������Ҫ��
		 * if (lpFuncInPacker)
			{
				free(lpFuncInPacker->GetPackBuf());
				lpFuncInPacker->Release();
			}
		 */
		/*
		 * [lpOut�ͷ�]��ÿһ�κ������������������
		 * if (lpOut[�����])
			{
				free(lpOut[�����]->GetPackBuf());
				lpOut[�����]->Release();
			}
		 */
		/*
		 * [lpSP�ͷ�]��ֻ���ڴ��ڴ洢���̵��ã�������һ���洢���̷��ؽ�����������ɣ�
		 * if(lpSP)
			lpSP->destroy();
		 * 
		 */
		/*
		 * [pack�����ͷ�]��pack����������������ΪIF2UnPacker *��������������䣺
		 * if (v_[������])
			v_[������]->Release();
		 */
		/*
		 * [�ͷ�����]��ֻ���ڴ������ݿ���ú������£�����Ҫ�ͷţ�ע��洢���̵ĵ���Ҳ�����ݿ����
		 * if (lpConn && (lpParentConn==NULL) )
			lpConn->toFree();
		 */
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		Set<String> dbConns =helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_DATABASE_CONN_VARIABLE_LIST);
		if(dbConns.size()>0){
			return genEndCodes(context,FREE_CONN);
		}else{
			return genEndCodes(context,"");
		}
		
	}

	/**
	 * @param context
	 * @return
	 * @throws Exception 
	 */
	public static String genEndCodes(Map<Object, Object> context,String free_conn_str) throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\r\n");
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		IARESProject project = (IARESProject) context.get(IAtomEngineContextConstantMySQL.Aresproject);
		Object obj = context.get(IAtomEngineContextConstantMySQL.ResourceModel);
		
		if(obj != null && obj instanceof AtomFunction){
			boolean flagR = false;
			AtomFunction func = (AtomFunction)obj;
			if (StringUtils.equalsIgnoreCase(func.getInterfaceFlag(), MarkConfig.MARK_IFLAG_R)) {
				flagR = true;
			}
			buffer.append(AddSerEndConditon(func,project,context));
			
			Set<String> funcCallList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_FUNC_CALL);
			if(funcCallList.size()>0){
				//lpFuncInPacker�ͷ�
				IFunctionMacroTokenService fmservice = (IFunctionMacroTokenService)context.get(IAtomEngineContextConstantMySQL.Function_Macro_Service);
				buffer.append(FREE_FuncInPacker);
				//lpOut�ͷ�
				for(String funcName : funcCallList){
					AtomFunction function = fmservice.getFunction(funcName).getInfo(AtomFunction.class);
					if(function != null){
						buffer.append(String.format(FREE_OUT, (StringUtils.isBlank(function.getObjectId())?function.getName():function.getObjectId())));
					}
				}
			}
			//lpSP�ͷ�
			Set<String> statementList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_STATEMENT_LIST);
			Iterator<String> ite = statementList.iterator();
			while(ite.hasNext()){
				String rsId = ite.next();
				buffer.append("if (lpSP" + rsId + ")\r\n"
						+"lpSP" + rsId + "->destroy();\r\n");
			}
			
			//������ͷ�
			Set<String> rsList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_RESULTSET_LIST);
			for(String rs : rsList){
				//TODO lpResultSet δȷ����׺�����޸� by wangxh
				buffer.append(String.format(FREE_RS, rs));
			}
			
			//pack�����ͷ�
			Set<String> packList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_PACK_VALIABLESET);
			for(String pack : packList){
				buffer.append(String.format(FREE_PACK, pack));
			}
			buffer.append(getObjectDefine(context));
			//�����ͷ�
			Set<String> databaseMacroList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_DATABASE_MACRO);
			Set<String> procMacroList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_MACRO);
			Set<String> rsprocedureCallList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_PROCEDURE_CALL_RSRETURN);
			Set<String> nrsprocedureCallList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_PROCEDURE_CALL_RSRETURN);
			if((databaseMacroList.size() > 0) || (procMacroList.size() > 0) || (funcCallList.size() > 0) || (rsprocedureCallList.size() > 0) || (nrsprocedureCallList.size() > 0)){
				if (!flagR) {
					buffer.append(free_conn_str);
				}
			}
			buffer.append(RETURN);
		}
		return buffer.toString();
	}

	/**
	 * @param project 
	 * @param func 
	 * @param helper 
	 * @return
	 * @throws Exception 
	 */
	private static String AddSerEndConditon(AtomFunction func, IARESProject project, Map<Object, Object> context) throws Exception {
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		Set<String> procSet = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_MACRO);
		List<Parameter> outputParameters = new ArrayList<Parameter>();
		ParamGroupUtil.parserParameters(func.getOutputParameters(), outputParameters, project);
		SerEndHelper serEndHelper = new SerEndHelper();
		
		//����proc���ؽ���������ֹ������ʱֱ�ӷ���
		if(procSet.contains(MacroConstant.PROC_RESULTSET_RETURN_MACRONAME)){
			return String.format(FORMAT_SVR_END, "",ELSE_STATEMENT+ serEndHelper.getErrorFieldAndFieldValue(outputParameters,serEndHelper.getExcludeParameters(),context));
		}
		StringBuffer addFieldBuffer = new StringBuffer();
		StringBuffer addValueBuffer = new StringBuffer();
		//���ؽ����Ϊ��
		if(func.isOutputCollection()){
			Set<String> rsSet = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_FUNC_RESULTSET);
			Set<String> packList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_ACK_ADDVALUE_HANDWORK_LIST);//�ֹ�������б�
			//������ؽ����������[PRO*C���������]��[�ֹ������]������ֱ�Ӵ�������
			//����[PRO*C���������]���Ѿ���ǰ����߼��д���ã����Բ������ֹ�����꣬�Ҵ��ڽ����ʱ����PackResultSet(lpResultSet)���
			//rsSet�ж��ַ�ʽ�ṩ��������纯�����ã�ͨ��SELECT��
			if((rsSet.size() > 0) && (packList.size() <= 0)){
				return String.format(FORMAT_SVR_END, String.format(PACKSET, rsSet.toArray()[rsSet.size() - 1]),ELSE_STATEMENT+ serEndHelper.getErrorFieldAndFieldValue(outputParameters,serEndHelper.getExcludeParameters(),context));
			}else if(!procSet.contains(MacroConstant.PROC_RESULTSET_RETURN_MACRONAME) && (packList.size() <= 0)){
				//��α����δ���κη��ؽ�����ĺ괦��ʱ��Ӧ����
				   ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
					String message = func.getChineseName() + "����Ϊ���������,��α������δ�з��ؽ��������ش���,����!";
					manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			   }
			return String.format(FORMAT_SVR_END, "",ELSE_STATEMENT+ serEndHelper.getErrorFieldAndFieldValue(outputParameters,serEndHelper.getExcludeParameters(),context));//������������������κδ������
		}else{
			
			for(Parameter  param : outputParameters){
				try{
				String id = param.getId();
				//if(!AtomFunctionCompilerUtil.isParameterINInputParameterByName(func, id,project)){
					if(param.getParamType().getValue()==ParamType.OBJECT_VALUE){
						addFieldBuffer.append(AddFiled("object", param, project));
						addValueBuffer.append(AddValue("object", param));
					}else{
						String type = getRealType(param,project,context);
						addFieldBuffer.append(AddFiled(type, param, project));
						addValueBuffer.append(AddValue(type, param));
					}
					
				//}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			//��IO��׼���������
			List<Parameter> inputParameters = new ArrayList<Parameter>();
			ParamGroupUtil.parserParameters(func.getInputParameters(), inputParameters, project);
			for(Parameter param : inputParameters){
				if (StringUtils.defaultIfBlank(param.getFlags(), "").indexOf("IO") != -1) {	
					try{
						String type = getRealType(param,project,context);
						addFieldBuffer.append(AddFiled(type, param, project));
						addValueBuffer.append(AddValue(type, param));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			return String.format(FORMAT_SVR_END, addFieldBuffer.toString()+ addValueBuffer.toString(),ELSE_STATEMENT + serEndHelper.getErrorFieldAndFieldValue(outputParameters,serEndHelper.getExcludeParameters(),context));
		}
		
	}

	//����type��ȡ��������ʵ���ͣ���type��������Ϊ��c��oracle��
	private static String getRealType(Parameter param,IARESProject project,Map<Object, Object> context){
		return AtomFunctionCompilerUtil.getRealDataType(param, project, context);
	}
	
	
	//���������AddFiled
	private static String AddFiled(String type,Parameter param,IARESProject project) throws Exception{
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
	
	//���������ͷ�
	private static String   getObjectDefine(Map<Object, Object> context){
		AtomFunction af = (AtomFunction) context.get(IAtomEngineContextConstantMySQL.ResourceModel);
		
		StringBuffer obj_release_buffer = new StringBuffer();
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		Set<String> initInObjectList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_IN_OBJECT_INIT_VARIABLE_LIST);
		Set<String> noInInitObjectList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_IN_OBJECT_NOINIT_VARIABLE_LIST);
		Set<String> outObjectList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_OUT_OBJECT_INIT_VARIABLE_LIST);
		List<String> freeHelper = new ArrayList<String>();
		//��������ͷ�
		for(String objectSet:noInInitObjectList){
			obj_release_buffer.append(String.format(OBJ_RELEASE,"v_"+objectSet+"ResultSet"));
			freeHelper.add(objectSet);
		}
		for(String objectSet:initInObjectList){
			if(noInInitObjectList.add(objectSet) && !freeHelper.contains(objectSet)){
				obj_release_buffer.append(String.format(OBJ_RELEASE,"v_"+objectSet+"ResultSet"));
				freeHelper.add(objectSet);
			}
		}
		//��������ͷ�
		for(String objectSet:outObjectList){
			if(!freeHelper.contains(objectSet)){
				obj_release_buffer.append(String.format(OBJ_OUT_RELEASE,"v_"+objectSet+"ResultSet"));
				freeHelper.add(objectSet);
			}
				
		}
		return obj_release_buffer.toString();
	}
	
	//���������AddValue
	private static String AddValue(String type,Parameter param){
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
	
	private final static String FORMAT_SVR_END = "\r\ngoto svr_end;\r\n" 
													+"svr_end:\r\n\r\n"
													+"if (iReturnCode == OK_SUCCESS || iReturnCode == ERR_SYSWARNING)\r\n"
													+"{\r\n"
													+"%1$s"
													+"}\r\n"
													+"else\r\n"
													+"{\r\n"
													+"%2$s"
													+"}\r\n";
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
	
	private final static String PACKSET = "PackResultSet(lpResultSet%1$s,lpOutPacker);\r\n";
	
	private final static String ELSE_STATEMENT = "GetErrorInfo(lpContext, @error_no, @error_info);\r\n"
													+"SystemErrorPacker(lpOutPacker,@error_pathinfo,@error_no,@error_info);\r\n"
													+"WriteSystemLog(lpContext,@error_pathinfo,@error_no,@error_info,@error_id,@error_sysinfo);\r\n";
	private final static String FREE_FuncInPacker = "if (lpFuncInPacker)\r\n"
													+"{\r\n"
													+"free(lpFuncInPacker->GetPackBuf());\r\n"
													+"lpFuncInPacker->Release();\r\n"
													+"}\r\n";
	private final static String FREE_OUT = "if (lpOut%1$s)\r\n"
											+"{\r\n"
											+"free(lpOut%1$s->GetPackBuf());\r\n"
											+"lpOut%1$s->Release();\r\n"
											+"}\r\n";
	private final static String FREE_SP = "if (lpSP)\r\n"
											+"lpSP->destroy();\r\n";
	private final static String FREE_RS = "if (lpResultSet%1$s)\r\n"
											+"{\r\n"
											+"lpResultSet%1$s->Destroy();\r\n"
											+"lpResultSet%1$s = NULL;\r\n"
											+"}\r\n";
	private final static String FREE_PACK = "if (v_%1$s)\r\n"
											+"v_%1$s->Release();\r\n";
	private final static String FREE_CONN = "if (lpConn && (lpParentConn==NULL) )\r\n"
											+"lpConn->toFree();\r\n";
	private final static String RETURN = "return iReturnCode;\r\n"
											+"}";
	
	private final static String OBJ_RELEASE = "if (%1$s)\r\n"
		+"{\r\n"
		+"%1$s->Release();\r\n"
		+"}\r\n";
	private final static String OBJ_OUT_RELEASE = "if (%1$s)\r\n"
		+"{\r\n"
		+"free(%1$s->GetPackBuf());\r\n"
		+"%1$s->Release();\r\n"
		+"}\r\n";
}
