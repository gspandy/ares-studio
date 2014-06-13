/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.InternalParam;
import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.AtomFunctionCompilerUtil;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.MarkConfig;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.ParamReplaceUtil;
import com.hundsun.ares.studio.biz.BizInterface;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;

/**
 * @author liaogc
 *
 */
public class PORCStatementToken implements ICodeToken{
	public static final String NL = ITokenConstant.NL;
	private String sql;//sql���
	private String rsId;//�����Id
	private String stateId;//lpSP Id
	private List<String> queryFieldList;
	private List<String> inFieldList;
	/**
	 * @param context
	 */
	public PORCStatementToken(String sql,String rsId,String stateId,List<String> queryFieldList,List<String> inFieldList) {
		this.sql = sql;
		this.rsId = rsId;
		this.stateId = stateId;
		this.queryFieldList = queryFieldList;
		this.inFieldList = inFieldList;
	}

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
		Object obj = context.get(IAtomEngineContextConstantMySQL.ResourceModel);
		StringBuffer code = new StringBuffer();
		if(sql.startsWith("select") || sql.startsWith("SELECT")){//����PRO*C���select into
			code.append("lpSP" + stateId + " = lpConn->createCallableStatement();\r\n");
			code.append("lpSP" + stateId + "->prepare(\"" + getSelectRunSql(sql) + "\");\r\n");
			for(int i = 0;i < inFieldList.size();i++){
				String dataType = getFieldDataType(inFieldList.get(i),context);
				if(TypeRule.typeRuleCharArray(dataType)){//�ַ���
					code.append("lpSP" + stateId + "->setString(" + (i + 1) + ", @" + inFieldList.get(i) + ");\r\n");//��1��ʼ����
				}else if(TypeRule.typeRuleChar(dataType)){//�ַ�
					code.append("lpSP" + stateId + "->setChar(" + (i + 1) + ", @" + inFieldList.get(i) + ");\r\n");//��1��ʼ����
				}else if(TypeRule.typeRuleDouble(dataType)){//������
					code.append("lpSP" + stateId + "->setDouble(" + (i + 1) + ", @" + inFieldList.get(i) + ");\r\n");//��1��ʼ����
				}else if(TypeRule.typeRuleInt(dataType)){//����
					code.append("lpSP" + stateId + "->setInt(" + (i + 1) + ", @" + inFieldList.get(i) + ");\r\n");//��1��ʼ����
				}else{//����һ���ַ�������
					code.append("lpSP" + stateId + "->setString(" + (i + 1) + ", @" + inFieldList.get(i) + ");\r\n");//��1��ʼ����
				}
			}
			code.append("lpResultSet" + rsId + " = lpSP" + rsId + "->open();\r\n");
			code.append("if(lpResultSet" + rsId + " != NULL)\r\n");
			code.append("{\r\n");
			code.append("if (!lpResultSet" + rsId + "->IsEOF()){\r\n");
			for(int i = 0;i < queryFieldList.size();i++){
				String dataType = getFieldDataType(queryFieldList.get(i),context);
				if(TypeRule.typeRuleCharArray(dataType)){//�ַ���
					String len = TypeRule.getCharLength(dataType);
					code.append("hs_strncpy(@" + queryFieldList.get(i) + ",conversion((char *)lpResultSet" + rsId + "->GetStr(\"" + queryFieldList.get(i) + "\"))," + len + ");\r\n");
				}else if(TypeRule.typeRuleChar(dataType)){//�ַ�
					code.append("@" + queryFieldList.get(i) + " = lpResultSet" + rsId + "->GetChar(\"" + queryFieldList.get(i) + "\");\r\n");
				}else if(TypeRule.typeRuleDouble(dataType)){//������
					code.append("@" + queryFieldList.get(i) + " = lpResultSet" + rsId + "->GetDouble(\"" + queryFieldList.get(i) + "\");\r\n");
				}else if(TypeRule.typeRuleInt(dataType)){//����
					code.append("@" + queryFieldList.get(i) + " = lpResultSet" + rsId + "->GetInt(\"" + queryFieldList.get(i) + "\");\r\n");
				}
			}
			code.append("}\r\n");
			code.append("}\r\n");
			code.append("if(lpResultSet" + rsId + " != NULL)\r\n");
		}else if(sql.startsWith("update") || sql.startsWith("UPDATE")){
			code.append("lpSP" + stateId + " = lpConn->createCallableStatement();\r\n");
			code.append("lpSP" + stateId + "->prepare(\"" + getSelectRunSql(sql) + "\");\r\n");
			for(int i = 0;i < inFieldList.size();i++){
				String dataType = getFieldDataType(inFieldList.get(i),context);
				if(TypeRule.typeRuleCharArray(dataType)){//�ַ���
					code.append("lpSP" + stateId + "->setString(" + (i + 1) + ", @" + inFieldList.get(i) + ");\r\n");//��1��ʼ����
				}else if(TypeRule.typeRuleChar(dataType)){//�ַ�
					code.append("lpSP" + stateId + "->setChar(" + (i + 1) + ", @" + inFieldList.get(i) + ");\r\n");//��1��ʼ����
				}else if(TypeRule.typeRuleDouble(dataType)){//������
					code.append("lpSP" + stateId + "->setDouble(" + (i + 1) + ", @" + inFieldList.get(i) + ");\r\n");//��1��ʼ����
				}else if(TypeRule.typeRuleInt(dataType)){//����
					code.append("lpSP" + stateId + "->setInt(" + (i + 1) + ", @" + inFieldList.get(i) + ");\r\n");//��1��ʼ����
				}else{//����һ���ַ�������
					code.append("lpSP" + stateId + "->setString(" + (i + 1) + ", @" + inFieldList.get(i) + ");\r\n");//��1��ʼ����
				}
			}
			code.append("iReturnCode = lpSP" + stateId + "->exec();\r\n");
			code.append("if(!iReturnCode)\r\n");
		}
		return code.toString();
	}
	
	/**
	 * ��һ������SQL��ʹ֮����MySQL����Ҫ��
	 * ������Ϣ���£�
	 * 1��queryFieldȥ��@
	 * 2��inFieldת��Ϊ?
	 * ע�⣺intoת��Ϊas����ҵ�񿪷���Ա�ֹ���ɣ���ȥ����MySQL�﷨�����ϵĲ���
	 * @param sql ԭ��SQL
	 * @return ������SQL
	 */
	private String getSelectRunSql(String sql){
		for(int i = 0;i < queryFieldList.size();i++){
			sql = sql.replaceAll("\\s+as\\s+@" + queryFieldList.get(i), " as " + queryFieldList.get(i));//ȥ��@
		}
		for(int i = 0;i < inFieldList.size();i++){
			sql = sql.replace("@" + inFieldList.get(i), "?");
		}
		Pattern p = Pattern.compile("@[\\w\\d_]+");
		Matcher m = p.matcher(sql);
		while (m.find()) {
			sql = sql.replace(m.group(), "?");//��������ڱ�׼�ֶΣ�������δ���
			//����������䣬@password��Ҫ���⴦��
			//select decode(password,' ',@password,password) as @password_b,fundpwd as @fundpwd,password_errors as @password_errors,lock_flag as @lock_flag from fundaccountauth where fund_account  =  @fund_account
			String field = m.group().substring(1);
			List<String> newList = new ArrayList<String>();
			newList.add(field);//����©�µ���Σ�ҲҪ��ӵ�����б��У����򲻻�setֵ
			newList.addAll(this.inFieldList);
			this.inFieldList = newList;//����©���ı�׼�ֶΣ�һ������ǰ�棬�ʽ���������б�������ǰ���
		}
		return sql;
	}
	
	/**
	 * �����ֶ�����ȡ��������
	 * @param fieldName �ֶ���
	 * @param context ������ 
	 * @return String �ֶ���ʵ����������
	 */
	private String getFieldDataType(String fieldName,Map<Object,Object> context){
		AtomFunction atomFunction = (AtomFunction) context.get(IAtomEngineContextConstantMySQL.ResourceModel);
		IARESProject project = (IARESProject)context.get(IAtomEngineContextConstantMySQL.Aresproject);
		Parameter param = AtomFunctionCompilerUtil.getParameterINAtomFunctionParameterByName(atomFunction,fieldName);
		if(param != null){
			return AtomFunctionCompilerUtil.getRealDataType(param,project,context);
		}else{
			StandardField stdfield = MetadataServiceProvider.getStandardFieldByNameNoExp(project, fieldName);//getIdΪ��������getNameΪ������
			if(stdfield == null){
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("����[%1$s]��Ӧ�ı�׼�ֶβ����ڡ�", fieldName);
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				return "";//��׼�ֶβ�����ʱ���������ɣ���������д�����Ϣ���׳���
			}
			String bizTypeName = stdfield.getDataType();//��׼�ֶ�ʱ��ȡ��׼�ֶζ�Ӧҵ������
			int length = 0;
			BusinessDataType bizType = MetadataServiceProvider.getBusinessDataTypeByNameNoExp(project, bizTypeName);//���ﲻ��ʹ��param.getType()�����������Ǳ�ʱ��ȡ����ҵ�����͵��쳣
			if(bizType == null){
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("����[%1$s]��Ӧ��ҵ������[%2$s]�����ڡ�", fieldName,bizTypeName);
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				return "";//ҵ�����Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
			}
			try {
				length = Integer.parseInt(bizType.getLength()) + 1;//����Char�������ʱ������Ҫ��1
			} catch (Exception e) {
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("����[%1$s]��Ӧ��ҵ������:%2$s�ĳ���Ϊ�Ƿ����֣�%3$s��", fieldName,bizTypeName,bizType.getLength());
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				return "";//ҵ�����ͳ���Ϊ�Ƿ�����ʱ���������ɣ���������д�����Ϣ���׳���
			}
			StandardDataType stdType = MetadataServiceProvider.getStandardDataTypeByNameNoExp(project, bizType.getStdType());
			if(stdType == null){
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("����[%1$s]��Ӧ��ҵ������[%2$s]�еı�׼����[%3$s]�����ڡ�", fieldName,bizTypeName,bizType.getStdType());
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
				return "";//��׼���Ͳ�����ʱ���������ɣ���������д�����Ϣ���׳���
			}
			String dataType = StringUtils.defaultIfBlank(stdType.getValue(MetadataServiceProvider.C_TYPE), "");
			dataType = dataType.replace("$L", length + "");
			return dataType;
		}
		
	}
	
	
}
