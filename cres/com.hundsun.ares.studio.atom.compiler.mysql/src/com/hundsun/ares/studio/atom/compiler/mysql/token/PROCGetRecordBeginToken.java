/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.token;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.AtomFunctionCompilerUtil;
import com.hundsun.ares.studio.biz.BizInterface;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;

/**
 * @author liaogc
 *
 */
public class PROCGetRecordBeginToken implements ICodeToken{
	public static final String NL = ITokenConstant.NL;
	private IMacroToken macroToken ;//��ǰ����ĺ�
	private Map<Object, Object> context;//��ǰ������������
	private String rsId ;//�����Id
	List<String> sqlFields;//�ֶ��б�
	
	public PROCGetRecordBeginToken(IMacroToken macroToken,Map<Object, Object> context,String rsId,List<String> sqlFields){
		this.macroToken = macroToken;
		this.context =context;
		this.rsId = rsId;
		this.sqlFields = sqlFields;
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
		StringBuffer code = new StringBuffer(100);
		boolean flagR = false;
		Object obj = context.get(IAtomEngineContextConstantMySQL.ResourceModel);
		if (obj instanceof BizInterface && StringUtils.equalsIgnoreCase(((BizInterface) obj).getInterfaceFlag(), "R")) {
			flagR = true;
		}
		if (!flagR) {
			
			code.append("while(!lpResultSet" + rsId + "->IsEOF())\r\n");
			code.append("{\r\n");
			Pattern p = Pattern.compile("@[\\w\\d_]+");
			//sql����ڵ�һ������
			Matcher m = p.matcher(macroToken.getParameters()[0]);
			while (m.find()) {
				String field_name = m.group().substring(1);
				for(int i = 0;i < this.sqlFields.size();i++){
					if(StringUtils.equals(this.sqlFields.get(i), field_name)){
						String dataType = getFieldDataType(sqlFields.get(i),context);
						if(TypeRule.typeRuleCharArray(dataType)){//�ַ���
							String len = TypeRule.getCharLength(dataType);
							//�����û����ܲ��ü���д������select a.cache_mod_times,a.cache_type from clientcacheinfo a where (operator_no = @operator_no or (operator_no = '0' and cache_type in ('2','3'))) and (cache_type = @cache_type or @cache_type = 0)
							//��ʱ�������ֻ�ȡ�������ݣ����������ȡֵ
							code.append("hs_strncpy(@" + sqlFields.get(i) + ",conversion((char *)lpResultSet" + rsId + "->GetStrByIndex(" + i + "))," + len + ");\r\n");
						}else if(TypeRule.typeRuleChar(dataType)){//�ַ�
							code.append("@" + sqlFields.get(i) + " = lpResultSet" + rsId + "->GetCharByIndex(" + i + ");\r\n");
						}else if(TypeRule.typeRuleDouble(dataType)){//������
							code.append("@" + sqlFields.get(i) + " = lpResultSet" + rsId + "->GetDoubleByIndex(" + i + ");\r\n");
						}else if(TypeRule.typeRuleInt(dataType)){//����
							code.append("@" + sqlFields.get(i) + " = lpResultSet" + rsId + "->GetIntByIndex(" + i + ");\r\n");
						}
					}
				}
			}
		}
		return code.toString();
	}
	
	/**
	 * ��ú��ı���
	 * @return
	 */
	private String getVarString(){
		return getMacroToken().getParameters()[0];
	}
	
	
	/**
	 * ��ú�
	 * @return IMacroToken
	 */
	private IMacroToken getMacroToken(){
		return this.macroToken;
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


