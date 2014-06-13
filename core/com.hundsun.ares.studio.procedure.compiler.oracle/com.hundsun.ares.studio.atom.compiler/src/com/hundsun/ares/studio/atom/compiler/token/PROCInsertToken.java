/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
/***********************************************************************************************************************************************
   �޶�ʱ��          �޶��汾    �޸ĵ�    �޸���    ������      �޸�����      �޸�ԭ��          ��ע 
 2013-03-31                  zhuyf         ��C_TYPE��ΪORACLE_TYPE��������¼�ȶ��Ƕ����ݿ������Ӧȡ���ݿ��������� 
************************************************************************************************************************************************/
package com.hundsun.ares.studio.atom.compiler.token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.skeleton.util.AtomFunctionCompilerUtil;
import com.hundsun.ares.studio.atom.compiler.skeleton.util.MarkConfig;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.model.BasicResourceInfo;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;

/**
 * @author liaogc
 *
 */
public class PROCInsertToken implements ICodeToken{
	public static final String NL = ITokenConstant.NL;
	
	private IMacroToken macroToken ;//��ǰ����ĺ�
	private AtomFunction atomFunction;//ԭ�Ӻ���ģ��
	private Map<String, String> defaultValueMap =  new HashMap<String, String>();//Ĭ��ֵ�б�
	private List<String> fieldNames = new ArrayList<String>(40);//���Ӧ���ֶ�

	private List<TableColumn> tableColumns;
	
	
	
	public PROCInsertToken(IMacroToken macroToken,Map<String, String> defaultValueMap,final List<TableColumn> tableColumns){
		this.macroToken= macroToken;
		this.tableColumns = tableColumns;
		this.initFieldNames(tableColumns);
		this.defaultValueMap = defaultValueMap;
		
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
		StringBuffer insertCode = new StringBuffer();
		
		String tName = getTableName(this.macroToken.getParameters()[0]);
		insertCode.append(String.format("// �����%1$s��¼",tName)).append(ITokenConstant.NL);
		BasicResourceInfo brInfo = (BasicResourceInfo) context.get(IAtomEngineContextConstant.ResourceModel);
		insertCode.append("EXEC SQL insert "+"/*"+brInfo.getObjectId()+"*/"+" into ");
		insertCode.append(tName);//����
		insertCode.append(MarkConfig.MARK_LEFTSIGN);
		
		// ���ֶδ���
		insertCode.append(getTableFieldStr(tableColumns));
		
		insertCode.append(NL);
		insertCode.append(MarkConfig.MARK_RIGHTSIGN);
		insertCode.append(MarkConfig.MARK_BLANK + MarkConfig.MARK_VALUE +MarkConfig. MARK_LEFTSIGN);
		insertCode.append(NL);
		
		int index = 1;//���ڿؼ�����
		for(int i=0;i< tableColumns.size();i++){
			TableColumn tableColumn = tableColumns.get(i);
			if (i != 0) {
				insertCode.append(MarkConfig.MARK_COMMA);
			}
			String valueVarName = defaultValueMap.get(tableColumn.getFieldName());
			if (valueVarName != null) {
				//String fFieldName = tableColumn.getFieldName();
				if (valueVarName.indexOf(MarkConfig.MARK_AT) >= 0) {// ���Ĭ�ϲ���ֵΪ����
					insertCode.append(MarkConfig.MARK_COLON).append(valueVarName);
				} else {// ���Ĭ�ϲ���ֵΪȷ����ֵ
					insertCode.append(valueVarName);
				}

			}else{
				
				ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
				List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
				Set<String> procVars = (Set<String>)helper.getAttribute(IAtomEngineContextConstant.ATTR_PROC_VARIABLE_LIST);
				String colomnType=StringUtils.EMPTY;
				colomnType = AtomFunctionCompilerUtil.getRealDataType(tableColumn.getFieldName(), (IARESProject)context.get(IAtomEngineContextConstant.Aresproject), MetadataServiceProvider.C_TYPE);
				AtomFunction atomFunction =(AtomFunction) context.get(IAtomEngineContextConstant.ResourceModel);
				IARESProject project = (IARESProject) context.get(IAtomEngineContextConstant.Aresproject);
				if (TypeRule.typeRuleInt(colomnType))// ��������Ϊint
				{
					// ����ֶ������������������������ڲ������У�Ĭ��ֵȡ����ֵ
					if (AtomFunctionCompilerUtil.isParameterINAtomFunctionParameterByName(atomFunction, tableColumn.getFieldName(),project)|| procVars.contains(tableColumn.getFieldName())||popVarList.contains(tableColumn.getFieldName()) ) {
						insertCode.append(MarkConfig.MARK_COLON + MarkConfig.MARK_AT);
						insertCode.append(tableColumn.getFieldName());
					} else{// ��ʾ���롢��������������ж�û�иö��壬��Ĭ��ֵȡ�����ֵ
						
						TypeDefaultValue typeDefaultValue  = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName((IARESProject)context.get(IAtomEngineContextConstant.Aresproject), tableColumn.getFieldName());
						if(typeDefaultValue!=null){
							// 2014-03-31 modified by zhuyf ��C_TYPE��ΪORACLE_TYPE��������¼�ȶ��Ƕ����ݿ������Ӧȡ���ݿ��������� 
							insertCode.append(typeDefaultValue.getValue(MetadataServiceProvider.ORACLE_TYPE));
						}
						
					}

				} else if (TypeRule.typeRuleDouble(colomnType)){// ��������Ϊdouble
					// ����ֶ������������������������ڲ������У�Ĭ��ֵȡ����ֵ
					if (AtomFunctionCompilerUtil.isParameterINAtomFunctionParameterByName(atomFunction, tableColumn.getFieldName(),project)|| procVars.contains(tableColumn.getFieldName())||popVarList.contains(tableColumn.getFieldName())) {
						insertCode.append(MarkConfig.MARK_COLON + MarkConfig.MARK_AT);
						insertCode.append(tableColumn.getFieldName());
					} else{// ��ʾ���롢��������������ж�û�иö��壬��Ĭ��ֵȡ�����ֵ
						TypeDefaultValue typeDefaultValue  = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName((IARESProject)context.get(IAtomEngineContextConstant.Aresproject), tableColumn.getFieldName());
						if(typeDefaultValue!=null){
							// 2014-03-31 modified by zhuyf ��C_TYPE��ΪORACLE_TYPE��������¼�ȶ��Ƕ����ݿ������Ӧȡ���ݿ��������� 
							insertCode.append(typeDefaultValue.getValue(MetadataServiceProvider.ORACLE_TYPE));
						}
					}

				} else if (TypeRule.typeRuleClob(colomnType)){// ��������Ϊdouble
					// ����ֶ������������������������ڲ������У�Ĭ��ֵȡ����ֵ
					if (AtomFunctionCompilerUtil.isParameterINAtomFunctionParameterByName(atomFunction, tableColumn.getFieldName(),project)|| procVars.contains(tableColumn.getFieldName())||popVarList.contains(tableColumn.getFieldName())) {
						insertCode.append(MarkConfig.MARK_COLON + MarkConfig.MARK_AT);
						insertCode.append(tableColumn.getFieldName());
					} else{// ��ʾ���롢��������������ж�û�иö��壬��Ĭ��ֵȡ�����ֵ
					
						TypeDefaultValue typeDefaultValue  = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName((IARESProject)context.get(IAtomEngineContextConstant.Aresproject), tableColumn.getFieldName());
						if(typeDefaultValue!=null){
							// 2014-03-31 modified by zhuyf ��C_TYPE��ΪORACLE_TYPE��������¼�ȶ��Ƕ����ݿ������Ӧȡ���ݿ��������� 
							insertCode.append(typeDefaultValue.getValue(MetadataServiceProvider.ORACLE_TYPE));
						}
					}

				} else if (TypeRule.typeRuleChar(colomnType) || TypeRule.typeRuleCharArray(colomnType)){// ��������Ϊchar��char[]
					// ����ֶ����������������������������������У�Ĭ��ֵȡ�����ֵ
					if (AtomFunctionCompilerUtil.isParameterINAtomFunctionParameterByName(atomFunction, tableColumn.getFieldName(),project)|| procVars.contains(tableColumn.getFieldName())||popVarList.contains(tableColumn.getFieldName())) {
						TypeDefaultValue typeDefaultValue  = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName((IARESProject)context.get(IAtomEngineContextConstant.Aresproject), tableColumn.getFieldName());
						if(typeDefaultValue!=null){
							// 2014-03-31 modified by zhuyf ��C_TYPE��ΪORACLE_TYPE��������¼�ȶ��Ƕ����ݿ������Ӧȡ���ݿ��������� 
							insertCode.append(MarkConfig.MARK_NVL + MarkConfig.MARK_LEFTSIGN + MarkConfig.MARK_COLON + MarkConfig.MARK_AT + tableColumn.getFieldName()
									+ MarkConfig.MARK_COMMA + typeDefaultValue.getValue(MetadataServiceProvider.ORACLE_TYPE) + MarkConfig.MARK_RIGHTSIGN);
						}else{
						insertCode.append(MarkConfig.MARK_NVL + MarkConfig.MARK_LEFTSIGN + MarkConfig.MARK_COLON + MarkConfig.MARK_AT + tableColumn.getFieldName()
							+ MarkConfig.MARK_COMMA + MarkConfig.MARK_SINGLEBLANK + MarkConfig.MARK_RIGHTSIGN);
						}
					} else{// ��ʾ���롢��������������ж�û�иö��壬��Ĭ��ֵȡ�����ֵ
					
						TypeDefaultValue typeDefaultValue  = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName((IARESProject)context.get(IAtomEngineContextConstant.Aresproject), tableColumn.getFieldName());
						if(typeDefaultValue!=null){
							// 2014-03-31 modified by zhuyf ��C_TYPE��ΪORACLE_TYPE��������¼�ȶ��Ƕ����ݿ������Ӧȡ���ݿ��������� 
							insertCode.append(typeDefaultValue.getValue(MetadataServiceProvider.ORACLE_TYPE));
						}
					}
				}/* else {
					throw new WrongRealDataTypeSettingException(colomnType, column.getType());
				}*/
				
			}
			// ÿ4���ֶλ���
			if (index% 4 == 0 && index != 0) {
				insertCode.append(NL);
			}
			index++;
		}
		insertCode.append(MarkConfig.MARK_RIGHTSIGN).append(MarkConfig.MARK_SEMICOLON);
		insertCode.append(NL);
		
		insertCode.append("if (CheckDbLinkMethod(lpConn,SQLCODE) < 0) ").append(NL);
		insertCode.append("{").append(NL);
		insertCode.append("if ((SQLCODE<= ERR_DB_NO_CONTINUE_FETCH) && (SQLCODE>= ERR_DB_FAILOVER_NETWORK_OPER_FAIL))").append(NL);
		insertCode.append("{").append(NL);
		insertCode.append("iReturnCode = SQLCODE;").append(NL);
		insertCode.append(" @error_no = SQLCODE; ").append(NL);
		insertCode.append("hs_strncpy(@error_info,sqlca.sqlerrm.sqlerrmc,sqlca.sqlerrm.sqlerrml);").append(NL);
		insertCode.append(" @error_id = SQLCODE; ").append(NL);
		insertCode.append("hs_strncpy(@error_sysinfo,sqlca.sqlerrm.sqlerrmc,sqlca.sqlerrm.sqlerrml);").append(NL);
		insertCode.append("EXEC SQL rollback;").append(NL);
		insertCode.append(NL);
		insertCode.append("goto svr_end;").append(NL);
		insertCode.append("}").append(NL);
		insertCode.append("lpConn->setErrMessage(HSDB_CONNECTION_STATUS_DISCONN,SQLCODE,sqlca.sqlerrm.sqlerrmc); ").append(NL);
		insertCode.append("}").append(NL);
		
	 return  insertCode.toString();
	
	}
	/**
	 * ���ݱ��ֶγ�ʼ�ֶ��б�
	 * @param tableColumns
	 */
	private void  initFieldNames(List<TableColumn> tableColumns){
		for(TableColumn tableColumn:tableColumns){
			fieldNames.add(tableColumn.getFieldName());
		}
		
	}
	
	/**
	 * ���ݱ��е�������select�еĻ������ֶ��б�
	 * @param columns
	 * @return
	 */
	private StringBuffer getTableFieldStr(List<TableColumn>  columns){
		StringBuffer tableFieldBuffer = new StringBuffer();
		int index = 1;
		for (TableColumn field : columns) {
			
			if (index > 1) {
				tableFieldBuffer.append(MarkConfig.MARK_COMMA);
			}
			tableFieldBuffer.append(field.getFieldName());
			//ÿ4���ֶλ���
			if (index % 4 == 0)
				tableFieldBuffer.append(NL);
			index++;
		}
		return tableFieldBuffer;
	}

	/**
	 * ��ñ���(����)
	 * @param tableAllName
	 * @return
	 */
	private String getTableName(String tableAllName){
		if(StringUtils.isNotBlank(tableAllName)){
			//�������ǰ������û���
			if (tableAllName.indexOf(".") != -1) {
				return tableAllName.substring(tableAllName.indexOf(".") + 1);
			}
		}
		return tableAllName;
		
		
	}
	

}
