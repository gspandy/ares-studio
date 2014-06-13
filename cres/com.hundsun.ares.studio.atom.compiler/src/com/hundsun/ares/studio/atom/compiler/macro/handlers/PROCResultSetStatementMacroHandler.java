/**
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.atom.compiler.macro.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.exception.MacroParameterErrorException;
import com.hundsun.ares.studio.atom.compiler.exception.TableNotFoundException;
import com.hundsun.ares.studio.atom.compiler.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.macro.TokenDomain;
import com.hundsun.ares.studio.atom.compiler.skeleton.util.TableResourceUtil;
import com.hundsun.ares.studio.atom.compiler.token.PROCResultSetStatementToken;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.ITokenDomain;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.jres.model.database.TableColumn;

/**
 * @author zhuyf
 * 
 */
public class PROCResultSetStatementMacroHandler implements IMacroTokenHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.PROC_RESULTSET_STATEMENT_MACRONAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(
	 * com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*
		 * ���������� [PRO*C��������][ sql��ѯ���] �������̣� 1.��ȡselect���Ĳ�ѯ�ֶ�
		 * ��Ҫ��*�Ĵ������ܱ�����Ƿ���ڣ�����������ȡ�ֶΣ��ҿ���selectǶ�׵���������������select�ֶ�Ϊ׼ ȥ��ע������*��Ӱ��
		 * ������ֶ��п��ܴ��к�������nvl �ֶο�����as�����������ô�ڽ��������ʱ��ʹ��as������ΪAddField���ֶ���
		 * 2.��ѯ�ֶδ����У��������ĺ괦����[POR*C���������] 3.����������䣺 EXEC SQL DECLARE
		 * cursor[�����]+[���] CURSOR FOR [select���]; EXEC SQL OPEN
		 * cursor[�����]+[���];
		 * 
		 * if (CheckDbLinkMethod(lpConn,SQLCODE) < 0) { if ((SQLCODE<=
		 * ERR_DB_NO_CONTINUE_FETCH) && (SQLCODE>=
		 * ERR_DB_FAILOVER_NETWORK_OPER_FAIL)) { iReturnCode = SQLCODE;
		 * v_error_no = SQLCODE;
		 * hs_strncpy(v_error_info,sqlca.sqlerrm.sqlerrmc,sqlca
		 * .sqlerrm.sqlerrml); v_error_id = SQLCODE;
		 * hs_strncpy(v_error_sysinfo,sqlca
		 * .sqlerrm.sqlerrmc,sqlca.sqlerrm.sqlerrml); EXEC SQL rollback;
		 * 
		 * goto svr_end; }
		 * 
		 * lpConn->setErrMessage(HSDB_CONNECTION_STATUS_DISCONN,SQLCODE,sqlca.
		 * sqlerrm.sqlerrmc); }
		 * 
		 * if (SQLCODE == OK_SUCCESS) ����[select���]��5���ֶ�Ϊһ�У������select��׷�Ӷ���ŵ�ע����Ϣ
		 * 4.�����α굽�����б��� 5.SQL���֧����ʷ���鵵�����ࡢ�������ձ� [PRO*C��������][select * from
		 * his_clientjour][clientjour] [PRO*C��������][select * from
		 * fil_clientjour][clientjour] [PRO*C��������][select * from
		 * r_clientjour][clientjour] [PRO*C��������][select * from
		 * rl_clientjour][clientjour]
		 */

		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper) context
				.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		addMacroNameToMacroList(token, context);// �Ѻ����ӵ������ݿ��б��Լ�proc�б���
		Set<String> cursorList = helper
				.getAttribute(IAtomEngineContextConstant.ATTR_CURSOR_LIST);
		AtomFunction func = (AtomFunction) context
				.get(IAtomEngineContextConstant.ResourceModel);
		int cursorTotalSizeAdd1 = cursorList.size() + 1;
		String cursorID = func.getObjectId() + cursorTotalSizeAdd1;
		helper.addAttribute(IAtomEngineContextConstant.ATTR_CURSOR_LIST,
				cursorID);
		String sql = "";
		List<ICodeToken> codeList = new ArrayList<ICodeToken>();
		String[] sqlFields = {};
		if (token.getParameters().length == 3) {// ����û��г����ֶ�
			sql =  token.getParameters()[0];
			String[] tempSqlFields = StringUtils.split(token.getParameters()[2], ",");
			sqlFields = new String[tempSqlFields.length];
			for (int i = 0; i < tempSqlFields.length; i++) {
				sqlFields[i] = StringUtils.trim(tempSqlFields[i]);
			}
			
			
		} else {// û���г��ֶ������sql���,��sql����л���ֶ�
			sql = this.getSqlStatement(token, context);
			sqlFields = getAllFieldsFromSqlStr(getFieldString(token, context));// sql����е��ֶ�
			
		}
		addVarList(token, context, sqlFields);// �ѱ����ӵ�porc�����б��Լ�α��������б���
		addDomain(token, context, sqlFields);// �����
		String cursorName = getCursorName(context);
		 codeList.add(new
		 PROCResultSetStatementToken(token,cursorName,sql));//���codeToken
		return codeList.iterator();
	}

	/**
	 * �ѱ�����ӵ�proc�б���ȥ
	 * 
	 * @param procVarList
	 */
	private void addVarList(IMacroToken token, Map<Object, Object> context,
			String[] sqlField) {
		List<String> popVarList = (List<String>) context
				.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper) context
				.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		Pattern p = Pattern.compile("@[\\w\\d_]+");
		// sql����ڵ�һ������
		Matcher m = p.matcher(token.getParameters()[0]);
		while (m.find()) {
			String fieldName = m.group().substring(1);
			// ���뵽proc�����б��У�����proc����������proc����������
			helper.addAttribute(
					IAtomEngineContextConstant.ATTR_PROC_VARIABLE_LIST,
					fieldName);
			// �����滻�б�ע�����ﲻ����helper.addAttribute(IAtomEngineContextConstant.PSEUDO_CODE_PARA_LIST,
			// field);
			popVarList.add(fieldName);
		}
		for (String field : sqlField) {
			// ���뵽proc�α�����б��У�����proc�α����������proc����������
			helper.addAttribute(
					IAtomEngineContextConstant.ATTR_CURSOR_PROC_VARIABLE_LIST,
					field);// ע�����ﲻ��Ҫ��_cur���������ʱ����Ҫ�õ�ԭ�������Ա���ԭ�����ڱ�������ʱ���ż�_cur
			// �����滻�б�ע�����ﲻ����helper.addAttribute(IAtomEngineContextConstant.PSEUDO_CODE_PARA_LIST,
			// field);
			// �α������׺��_cur
			popVarList.add(field + "_cur");
		}
	}

	/**
	 * �����
	 */
	private void addDomain(IMacroToken token, Map<Object, Object> context,
			String[] sqlFields) {
		IDomainHandler handler = (IDomainHandler) context
				.get(IEngineContextConstant.DOMAIN_HANDLER);
		String key = getKey();
		Object[] args = new Object[2];
		args[0] = getCursorName(context);// ����α���cursor[�����]+[���]�����
		args[1] = sqlFields;// �α��ֶ�����
		ITokenDomain tokenDomain = new TokenDomain(key, args);
		handler.addDomain(tokenDomain);
	}

	// /**
	// * �Ѻ������뵽���б���
	// */
	private void addMacroNameToMacroList(IMacroToken token,
			Map<Object, Object> context) {
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper) context
				.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		helper.addAttribute(IAtomEngineContextConstant.ATTR_DATABASE_MACRO,
				token.getKeyword());// ��ӵ����ݿ��б���
		helper.addAttribute(IAtomEngineContextConstant.ATTR_PROC_MACRO,
				token.getKeyword());// ��ӵ�proc���б���
	}

	/**
	 *���sql���
	 */
	private String getSqlStatement(IMacroToken token,Map<Object, Object> context) throws TableNotFoundException,MacroParameterErrorException {
		String sqlStr = getSqlStrReplaceComm(token);
		String sTableName = "";
		Stack<String> addHead = new Stack<String>();
		if (token.getParameters().length >= 2) {
			sTableName = token.getParameters()[1];
		}
		// ȥ��Ƕ�׵�select
		Pattern p = Pattern.compile("\\s*select\\s+\\*\\s+from\\s*\\(");
		Matcher m = p.matcher(sqlStr);
		while (m.find()) {
			if (m.start() == 0) {
				addHead.add(sqlStr.substring(0, m.end()));
				sqlStr = sqlStr.substring(m.end());
				m = p.matcher(sqlStr);
			}
		}
		// ȡ��select��from�м�ı�������
		String fieldStr = sqlStr.substring(sqlStr.indexOf("select ") + 7);
		p = Pattern.compile("(\\s+from\\s+)");
		m = p.matcher(fieldStr);
		if (m.find()) {
			fieldStr = fieldStr.substring(0, m.start()).trim();
		}
		// ����һ�����������еı�������ж�����
		String sFieldStr = fieldStr + ",";
		// ����ĳ������
		int k = 0;
		while (sFieldStr.indexOf(",") >= 0) {
			// ��ı�������
			String sOtherTableName = "";
			// �滻*���ֶα���
			String sOtherFieldStr = "";

			String sTempField = sFieldStr.substring(0, sFieldStr.indexOf(","))
					.trim();

			if (sTempField.indexOf("*/") != -1
					&& sTempField.indexOf("/*") != -1) {
				sTempField = sTempField.substring(sTempField.indexOf("*/") + 2);
			}

			// ����Ǵ��������ֶΣ�����ȥ������
			if (sTempField.indexOf("(") >= 0) {
				if (sTempField.toLowerCase().indexOf(" as ") < 0) {
					sFieldStr = sFieldStr.substring(sFieldStr.indexOf(",") + 1)
							.trim();
					do {
						if (sFieldStr.indexOf(",") >= 0) {
							sTempField = sFieldStr.substring(0,
									sFieldStr.indexOf(",")).trim();
							if (sTempField.toLowerCase().indexOf(" as ") >= 0) {
								break;
							} else {
								sFieldStr = sFieldStr.substring(
										sFieldStr.indexOf(",") + 1).trim();
							}
						} else {
							break;
						}
					} while (true);
				}
			}

			// �п�����ȥ������ǰ�벿���Ժ����µ��ֶΣ���ʱӦ��AS����
			if (sTempField.toLowerCase().indexOf(" as ") >= 0) {
				sTempField = sTempField.substring(sTempField.toLowerCase()
						.indexOf(" as ") + 4);
			}

			// ����Ǵ��������ֶΣ�����ȥ������
			if (sTempField.indexOf(".") >= 0) {
				// ȡ����
				sOtherTableName = sTempField.substring(0,
						sTempField.indexOf("."));
				sTempField = sTempField.substring(sTempField.toLowerCase()
						.indexOf(".") + 1);
			}

			// ���ȡ�õ��ֶ��д�*�ţ�������Ҫ�ӱ���ȡ�ֶ�
			if (sTempField.trim().endsWith("*") && sTempField.indexOf("/*") < 0) {
				if (!sTableName.equals("")) {
					if (!sTableName.substring(1).equals(",")) {
						sTableName = sTableName + ",";
					}
					if (sTableName.indexOf(",") >= 0) {
						String sTempTableName = sTableName.substring(0,
								sTableName.indexOf(","));
						if (!sTempTableName.equals("")) {
							// �ӱ�ṹ�л�ȡ��������ֶ��б�
							IARESProject project = (IARESProject) context
									.get(IAtomEngineContextConstant.Aresproject);
							List<TableColumn> fields = TableResourceUtil
									.getFieldsWithoutFlag("H", sTempTableName,
											project);

							if (fields.size() == 0) {

								if ("1" == null) {
									// ��ͼ��������ٴ���Ļ�ͨ����ͼ���fields
								} else {
									String errorInfo = "";

									for (int index = 0; index < fields.size(); index++) {
										TableColumn field = fields.get(index);

										if (sOtherTableName.equals("")) {
											if (index == fields.size() - 1) {
												sOtherFieldStr = sOtherFieldStr
														+ field.getName();
											} else {
												sOtherFieldStr = sOtherFieldStr
														+ field.getName() + ",";
											}
										} else {
											if (index == fields.size() - 1) {
												sOtherFieldStr = sOtherFieldStr
														+ sOtherTableName + "."
														+ field.getName();
											} else {
												sOtherFieldStr = sOtherFieldStr
														+ sOtherTableName + "."
														+ field.getName() + ",";
											}
										}
										k++;
										// ÿ4���ֶλ���
										if (k % 4 == 0)
											sOtherFieldStr += "\n";
									}

								}

							} else {

								// �����ֶ�׷�ӵ��α��ֶ��б���
								for (int index = 0; index < fields.size(); index++) {
									TableColumn field = fields.get(index);
									if (sOtherTableName.equals("")) {
										if (index == fields.size() - 1) {
											sOtherFieldStr = sOtherFieldStr
													+ field.getName();
										} else {
											sOtherFieldStr = sOtherFieldStr
													+ field.getName() + ",";
										}
									} else {
										if (index == fields.size() - 1) {
											sOtherFieldStr = sOtherFieldStr
													+ sOtherTableName + "."
													+ field.getName();
										} else {
											sOtherFieldStr = sOtherFieldStr
													+ sOtherTableName + "."
													+ field.getName() + ",";
										}
									}
									k++;
									// ÿ4���ֶλ���
									if (k % 4 == 0)
										sOtherFieldStr += "\n";
								}
							}
						}
						// �滻.*
						if (!sOtherFieldStr.equals("")) {
							if (sOtherTableName.equals("")) {
								sqlStr = sqlStr.replace("*", sOtherFieldStr);
							} else {
								sqlStr = sqlStr.replace(sOtherTableName + ".*",
										sOtherFieldStr);
							}
						}
						sTableName = sTableName.substring(sTableName.length()
								- sTableName.indexOf(","));
					}
				}
			}
			if (sFieldStr.length() > sFieldStr.indexOf(",")) {
				sFieldStr = sFieldStr.substring(sFieldStr.indexOf(",") + 1);
			} else {
				sFieldStr = "";
			}
			k++;
			// ÿ4���ֶλ���
			if (k % 4 == 0)
				sFieldStr += "\n";
		}

		while (addHead.size() > 0) {
			sqlStr = addHead.pop().replace("*",
					removeOtherName(getFieldString(token, context)))
					+ sqlStr;
		}
		// ��where��䣬ÿһ��and��һ��
		sqlStr = sqlStr.replaceAll(" and ", "\n and ");
		sqlStr = sqlStr.replaceAll(" order by ", "\n order by ");
		return sqlStr;
	}

	/**
	 * ����[PRO*C��������]SQL���
	 * 
	 * @return ������sql���
	 * @throws TableNotFoundException
	 */

	private String getFieldString(IMacroToken token, Map<Object, Object> context)
			throws TableNotFoundException, MacroParameterErrorException {
		String sqlStr = getSqlStrReplaceComm(token);
		String sTableName = "";
		if (token.getParameters().length >= 2) {
			sTableName = token.getParameters()[1];
		}
		//�����Ƕ�׵�select * from(����ȥ��
		Pattern p = Pattern.compile("\\s*(select|SELECT)\\s+\\*\\s+(from|FROM)\\s*\\(");
		Matcher m = p.matcher(sqlStr);
		while (m.find()) {
			if (m.start() == 0) {
				sqlStr = sqlStr.substring(m.end());
				m = p.matcher(sqlStr);
			}
		}
		String fieldStr = sqlStr.substring(sqlStr.indexOf("select ") + 7);
		p = Pattern.compile("\\s+(from|FROM)\\s+");
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
							IARESProject project = (IARESProject) context.get(IAtomEngineContextConstant.Aresproject);
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

	// ȥ������ǰ�ı����
	public String removeOtherName(String str) {
		String result = "";
		List<String> para = new ArrayList<String>();
		str = str + ",";
		int k = 0;
		while (str.indexOf(",") != -1) {
			String sTempField = str.substring(0, str.indexOf(",")).trim();
			str = str.substring(str.indexOf(",") + 1).trim();
			para.add(sTempField);
		}
		for (int i = 0; i < para.size(); i++) {
			if (para.get(i).indexOf(".") != -1) {
				result = result
						+ para.get(i).substring(para.get(i).indexOf(".") + 1)
								.trim();
			} else {
				result = result + para.get(i).trim();
			}
			if (i != para.size() - 1)
				result = result + ",";
			if (i % 4 == 0)
				result += "\n";
		}

		return result;

	}

	/**
	 * ���ر��ֶ�
	 * 
	 * @param sqlStr
	 * @return
	 */
	private String[] getAllFieldsFromSqlStr(String sqlStr) {
		int selectIndex = sqlStr.toLowerCase().indexOf("select");
		int fromIndex = sqlStr.toLowerCase().indexOf("from");
		if (selectIndex >= 0 && fromIndex >= 0) {
			sqlStr = sqlStr.substring(selectIndex + 6, fromIndex).trim();// ѡȡselect��from֮�������
		}
		String[] tfields = sqlStr.split(",");
		List<String> fields = new ArrayList<String>();
		for (int i = 0; i < tfields.length; i++) {
			// ȥ��as
			int asIndex = tfields[i].indexOf(" as ");
			if (asIndex >= 0) {
				String name = tfields[i].substring(asIndex + 4).trim();
				fields.add(name);

			} else {
				int dotIndex = tfields[i].indexOf(".");
				if (dotIndex >= 0) {
					String name = tfields[i].substring(dotIndex + 1).trim();
					fields.add(name);
				} else {
					String name = tfields[i].trim();
					fields.add(name);
				}
			}
		}
		return fields.toArray(new String[fields.size()]);
	}

	// ����滻�������SQL���
	private String getSqlStrReplaceComm(IMacroToken token)
			throws MacroParameterErrorException {
		String str = token.getParameters()[0];
		if (str.indexOf("insert into") >= 0) {
			throw new MacroParameterErrorException("[PRO*C��������]", str,
					"����ʹ��INSERT��");
		} else if (str.indexOf("update ") >= 0) {
			throw new MacroParameterErrorException("[PRO*C��������]", str,
					"����ʹ��UPDATE��");
		}

		// str = EngineUtil.replaceConstant(str, this.getFunction(),
		// true);ȥ���ǳ����߻�����ֲ
		str = str.replaceAll("\\-\\-[^\n]*\n", "\n");
		return str;
	}

	/**
	 * ����α��ֶδ�
	 * 
	 * @return
	 */
	private String getCursorName(Map<Object, Object> context) {
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper) context
				.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		Set<String> cursorList = helper
				.getAttribute(IAtomEngineContextConstant.ATTR_CURSOR_LIST);
		if (cursorList.size() > 0) {
			return "cursor"
					+ (cursorList.toArray(new String[cursorList.size()]))[cursorList
							.size() - 1];// LinkHashSet�ṹ�����µ������
		}
		return "cursor";
	}

}
