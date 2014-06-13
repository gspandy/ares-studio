/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.AtomService;
import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.exception.MacroParameterErrorException;
import com.hundsun.ares.studio.atom.compiler.mysql.exception.TableNotFoundException;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.TokenDomain;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.TableResourceUtil;
import com.hundsun.ares.studio.atom.compiler.mysql.token.PROCResultSetStatementToken;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.ITokenDomain;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.jres.model.database.TableColumn;

/**
 * @author zhuyf
 *
 */
public class PROCResultSetStatementMacroHandler implements IMacroTokenHandler {
	
	private List<String> queryFieldList;//���ڱ���Select���Ĳ�ѯ�ֶ�
	
	private List<String> inFieldList;//���ڱ��涯̬���������ֶ�
	

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.PROC_RESULTSET_STATEMENT_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*
		 * ����������
			[PRO*C��������][ sql��ѯ���]
			�������̣�
			1.��ȡselect���Ĳ�ѯ�ֶ�
				��Ҫ��*�Ĵ������ܱ�����Ƿ���ڣ�����������ȡ�ֶΣ��ҿ���selectǶ�׵���������������select�ֶ�Ϊ׼
				ȥ��ע������*��Ӱ��
				������ֶ��п��ܴ��к�������nvl
				�ֶο�����as�����������ô�ڽ��������ʱ��ʹ��as������ΪAddField���ֶ���
			2.��ѯ�ֶδ����У��������ĺ괦����[POR*C���������]
			3.����������䣺
			EXEC SQL DECLARE cursor[�����]+[���] CURSOR FOR [select���];
			EXEC SQL OPEN cursor[�����]+[���];

			if (CheckDbLinkMethod(lpConn,SQLCODE) < 0)
			{
				if ((SQLCODE<= ERR_DB_NO_CONTINUE_FETCH) && (SQLCODE>= ERR_DB_FAILOVER_NETWORK_OPER_FAIL))
				{
					iReturnCode = SQLCODE;
					v_error_no = SQLCODE;
					hs_strncpy(v_error_info,sqlca.sqlerrm.sqlerrmc,sqlca.sqlerrm.sqlerrml);
					v_error_id = SQLCODE;
					hs_strncpy(v_error_sysinfo,sqlca.sqlerrm.sqlerrmc,sqlca.sqlerrm.sqlerrml);
					EXEC SQL rollback;

					goto svr_end;
				}

				lpConn->setErrMessage(HSDB_CONNECTION_STATUS_DISCONN,SQLCODE,sqlca.sqlerrm.sqlerrmc);
			}

			if (SQLCODE == OK_SUCCESS)
			����[select���]��5���ֶ�Ϊһ�У������select��׷�Ӷ���ŵ�ע����Ϣ
			4.�����α굽�����б���
			5.SQL���֧����ʷ���鵵�����ࡢ�������ձ�
			[PRO*C��������][select * from his_clientjour][clientjour]
			[PRO*C��������][select * from fil_clientjour][clientjour]
			[PRO*C��������][select * from r_clientjour][clientjour]
			[PRO*C��������][select * from rl_clientjour][clientjour]
		 */
		queryFieldList = new ArrayList<String>();
		inFieldList = new ArrayList<String>();
		addMacroNameToMacroList(token,context);//�Ѻ����ӵ������ݿ��б��Լ�proc�б���
		List<ICodeToken> codeList = new ArrayList<ICodeToken>();
		if(token.getParameters().length==3){//����û��г����ֶ�
			String[] tempSqlFields = StringUtils.split(token.getParameters()[2], ",");
			for(int i = 0;i<tempSqlFields.length;i++){
				queryFieldList.add(StringUtils.trim(tempSqlFields[i]));
			}
		}else{//û���г��ֶ������sql���,��sql����л���ֶ�
			getAllFieldsFromSqlStr(getFieldString(token,context),context);
			splitFieldList(token,context);
		}	
		
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		Set<String> rsList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_RESULTSET_LIST);
		Set<String> stateList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_STATEMENT_LIST);
		AtomFunction func = (AtomFunction)context.get(IAtomEngineContextConstantMySQL.ResourceModel);
		int rsTotalSizeAdd1 = rsList.size() + 1;
		int stateTotalSizeAdd1 = stateList.size() + 1;
		String objectId = func.getObjectId();
		if(StringUtils.isBlank(objectId) && (func instanceof AtomService)){
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = "��ȡ���ܺ�ʧ��,�޷���������lpResultSet";
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
		}else if(StringUtils.isBlank(objectId) && !(func instanceof AtomService)){
			objectId = func.getName();
		}
		String rsID = objectId + rsTotalSizeAdd1;
		addDomain(token,context,queryFieldList,rsID);//�����
		String stateID = objectId + stateTotalSizeAdd1;
		helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_STATEMENT_LIST,stateID);//�����ͷŵĶ�̬����б�
		helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_RESULTSET_LIST,rsID);//�����ͷŵĽ�����б�
		helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_GETLAST_RESULTSET,rsID);//
		codeList.add(new PROCResultSetStatementToken(token,rsID,stateID,queryFieldList,inFieldList));//���codeToken
		return codeList.iterator();
	}
	
	/**
	 * �����
	 */
	private void addDomain(IMacroToken token,
			Map<Object, Object> context,List<String> sqlFields,String rdId){
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		String key = getKey();
		Object[] args = new Object[2];
		args[0] = rdId;//��ӽ����Id��������������ػ�PRO*C��¼��ȡ��ʼ��PRO*C��¼��ȡ���������ɴ���ʱ��Ҫʹ�á�
		args[1] = sqlFields;//�α��ֶ�����
		ITokenDomain tokenDomain = new TokenDomain(key,args);
		handler.addDomain(tokenDomain);
	}
	
	/**
	 * �Ѻ������뵽���б���
	 */
	private void addMacroNameToMacroList(IMacroToken token, Map<Object, Object> context){
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_DATABASE_MACRO,token.getKeyword());//��ӵ����ݿ��б���
	    helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_MACRO,token.getKeyword());//��ӵ�proc���б���
	}
	
	/**
	 * ����[PRO*C��������]SQL���
	 * 
	 * @return ������sql���
	 * @throws TableNotFoundException 
	 */
	
	private String getFieldString(IMacroToken token, Map<Object, Object> context) throws TableNotFoundException,MacroParameterErrorException{
		String sqlStr = getSqlStrReplaceComm(token);
		String sTableName = "";
		if (token.getParameters().length >= 2) {
			sTableName = token.getParameters()[1];
		}
		Pattern p = Pattern.compile("\\s*select\\s+\\*\\s+from\\s*\\(");
		Matcher m = p.matcher(sqlStr);
		while (m.find()) {
			if (m.start() == 0) {
				sqlStr = sqlStr.substring(m.end());
				m = p.matcher(sqlStr);
			}
		}
		String fieldStr = sqlStr.substring(sqlStr.indexOf("select ") + 7);
		p = Pattern.compile("(\\s+from\\s+)");
		m = p.matcher(fieldStr);
		if (m.find()) {
			fieldStr = fieldStr.substring(0, m.start()).trim();
		}
		String sFieldStr = fieldStr + ",";
		String result = "";
		while (sFieldStr.indexOf(",") >= 0) {
			// ��ı�������
			String sOtherTableName = "";
			// �滻*���ֶα���
			String sOtherFieldStr = "";

			String sTempField = sFieldStr.substring(0, sFieldStr.indexOf(",")).trim();
			
			if(sTempField.indexOf ("*/") != -1 && sTempField.indexOf ("/*") != -1){
				sTempField = sTempField.substring (sTempField.indexOf ("*/")+2);
			}
			
			if(sTempField.trim().startsWith("distinct") || sTempField.trim().startsWith("DISTINCT") || sTempField.trim().startsWith("Distinct")){
				sTempField = sTempField.trim().substring(8);
			}

			// ����Ǵ��������ֶΣ�����ȥ������
			if (sTempField.indexOf("(") >= 0) {
				if (sTempField.toLowerCase().indexOf(" as ") < 0) {
					sFieldStr = sFieldStr.substring(sFieldStr.indexOf(",") + 1).trim();
					do {
						if (sFieldStr.indexOf(",") >= 0) {
							sTempField = sFieldStr.substring(0, sFieldStr.indexOf(",")).trim();
							if (sTempField.toLowerCase().indexOf(" as ") >= 0) {
								break;
							} else {
								sFieldStr = sFieldStr.substring(sFieldStr.indexOf(",") + 1).trim();
							}
						} else {
							break;
						}
					} while (true);
				}
			}

			// �п�����ȥ������ǰ�벿���Ժ����µ��ֶΣ���ʱӦ��AS����
			if (sTempField.toLowerCase().indexOf(" as ") >= 0) {
				sTempField = sTempField.substring(sTempField.toLowerCase().indexOf(" as ") + 4);
			}

			// ����Ǵ��������ֶΣ�����ȥ������
			if (sTempField.indexOf(".") >= 0) {
				// ȡ����
				sOtherTableName = sTempField.substring(0, sTempField.indexOf("."));
				sTempField = sTempField.substring(sTempField.toLowerCase().indexOf(".") + 1);
			}

			// ���ȡ�õ��ֶ��д�*�ţ�������Ҫ�ӱ���ȡ�ֶ�
			if (sTempField.trim().endsWith("*")&& sTempField.indexOf("/*") < 0) {
				if(token.getParameters().length<2 || StringUtils.isBlank(sTableName) ){
					throw new RuntimeException("�������ĵڶ�������[����]!");
				}
				if (!sTableName.equals("")) {
					if (!sTableName.substring(1).equals(",")) {
						sTableName = sTableName + ",";
					}
					if (sTableName.indexOf(",") >= 0) {
						String sTempTableName = sTableName.substring(0, sTableName.indexOf(","));
						if (!sTempTableName.equals("")) {
							// �����ֶ�׷�ӵ��α��ֶ��б���
							IARESProject project = (IARESProject) context.get(IAtomEngineContextConstantMySQL.Aresproject);
							List<TableColumn> fields = TableResourceUtil.getFieldsWithoutFlag("H",sTempTableName,project);
							for (int index = 0; index < fields.size(); index++) {
								TableColumn field = fields.get(index);
								if (sOtherTableName.equals("")) {
									if (index == fields.size() - 1) {
										sOtherFieldStr = sOtherFieldStr + field.getFieldName();
									} else {
										sOtherFieldStr = sOtherFieldStr + field.getFieldName() + ",";
									}
								} else {
									if (index == fields.size() - 1) {
										sOtherFieldStr = sOtherFieldStr + sOtherTableName + "." + field.getFieldName();
									} else {
										sOtherFieldStr = sOtherFieldStr + sOtherTableName + "." + field.getFieldName() + ",";
									}
								}
							}
							
							//��ͼ��������ٴ���
						}
						if (result.equals("")) {
							result += sOtherFieldStr;
						} else {
							result += "," + sOtherFieldStr;
						}
						sTableName = sTableName.substring(sTableName.length() - sTableName.indexOf(","));
					}
				}
			} else {
				if (sOtherTableName.equals("")) {
					if (result.equals("")) {
						result += sTempField;
					} else {
						result += "," + sTempField;
					}
				} else {
					if (result.equals("")) {
						result += sOtherTableName + "." + sTempField;
					} else {
						result += "," + sOtherTableName + "." + sTempField;
					}
				}
			}
			if (sFieldStr.length() > sFieldStr.indexOf(",")) {
				sFieldStr = sFieldStr.substring(sFieldStr.indexOf(",") + 1);
			} else {
				sFieldStr = "";
			}
		}
		return result;
	}
	
	/**
	 * ���ر��ֶ�
	 * @param sqlStr
	 * @return
	 */
	private  void getAllFieldsFromSqlStr(String sqlStr,Map<Object, Object> context)  {
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		int selectIndex = sqlStr.toLowerCase().indexOf("select");
		int fromIndex = sqlStr.toLowerCase().indexOf("from");
		if (selectIndex >= 0 && fromIndex >= 0) {
			sqlStr = sqlStr.substring(selectIndex + 6, fromIndex).trim();// ѡȡselect��from֮�������
		}
		String[] tfields = sqlStr.split(",");
		//List<String> fields = new ArrayList<String>();
		for (int i = 0; i < tfields.length; i++) {
			// ȥ��as
			int asIndex = tfields[i].indexOf(" as ");
			if (asIndex >= 0) {
				String name = tfields[i].substring(asIndex + 4).trim();
				queryFieldList.add(name);
				popVarList.add(name);
			} else {
				int dotIndex = tfields[i].indexOf(".");
				if (dotIndex >= 0) {
					String name = tfields[i].substring(dotIndex + 1).trim();
					queryFieldList.add(name);
					popVarList.add(name);
				} else {
					String name = tfields[i].trim();
					queryFieldList.add(name);
					popVarList.add(name);
				}
			}
		}
	}
	//����滻�������SQL���
	private String getSqlStrReplaceComm(IMacroToken token) throws MacroParameterErrorException{
		String str = token.getParameters()[0];
		if(str.indexOf("insert into") >= 0){
			throw new MacroParameterErrorException("[PRO*C��������]",str,"����ʹ��INSERT��");
		}else if(str.indexOf("update ") >= 0){
			throw new MacroParameterErrorException("[PRO*C��������]",str,"����ʹ��UPDATE��");
		}
		
		//str = EngineUtil.replaceConstant(str, this.getFunction(), true);ȥ���ǳ����߻�����ֲ
		str = str.replaceAll("\\-\\-[^\n]*\n", "\n");
		return str; 
	}
	
	/**
	 * ����SQL����е�@���������뵽set�б���
	 * @param procVarList
	 */
	private void splitFieldList(IMacroToken token,Map<Object, Object> context){
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		String sql = token.getParameters()[0];
		Pattern p = Pattern.compile("@[\\w\\d_]+");
		Matcher m = p.matcher(sql);
		while (m.find()) {
			int index = m.group().indexOf("@");
			String field = m.group().substring(index + 1);
			inFieldList.add(field);//�����ж��ٸ�@�ͼӶ��ٸ����ظ����ȥ��
			//�����滻�б�ע�����ﲻ����helper.addAttribute(IAtomEngineContextConstant.PSEUDO_CODE_PARA_LIST, field);
			popVarList.add(field);//�ѱ����ӵ�porc�����б��Լ�α��������б���
		}
		
		
	}

}
