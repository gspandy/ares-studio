/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.exception.ErrorParameterNumberException;
import com.hundsun.ares.studio.engin.exception.HSException;
import com.hundsun.ares.studio.engin.exception.MacroParameterDescNotDefineException;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procedure.compiler.constant.IProcedureEngineContextConstant;
import com.hundsun.ares.studio.procedure.compiler.exception.TableFieldNotFoundInStdFieldException;
import com.hundsun.ares.studio.procedure.compiler.exception.TableFieldNotSettingException;
import com.hundsun.ares.studio.procedure.compiler.exception.TableNotFoundException;
import com.hundsun.ares.studio.procedure.compiler.skeleton.util.CodeUtil;
import com.hundsun.ares.studio.procedure.compiler.skeleton.util.GenStringUtil;
import com.hundsun.ares.studio.procedure.compiler.skeleton.util.ProcedureCompilerUtil;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * @author liaogc
 *
 */
public class SelectInsertTableInProcBlockToken implements ICodeToken{
	
	private IMacroToken macroToken ;//��ǰ����ĺ�
	private Map<Object, Object> context;//��ǰ������������
	private Procedure p;//ģ��
	private Map<String, String> defaultValueMap = new HashMap<String, String>();//Ĭ��ֵ�б�
	
	public SelectInsertTableInProcBlockToken(IMacroToken macroToken,Map<Object, Object> context,Map<String, String> defaultValueMap){
		this.macroToken = macroToken;
		this.context = context;
		this.p = (Procedure) context.get(IProcedureEngineContextConstant.ResourceModel);
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
		try{
		String sqlStr = getMacroToken().getParameters()[1];
		if(sqlStr.trim().toLowerCase().startsWith("select")){
			return genCodeSelect();//�����ĵ�һ�ڶ��������Ǳ���ʱ
		}else{
			return genCodeTable();//�����ĵڶ���������sql���ʱ
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	
	}
	
	/**
	 * �����ĵ�һ��,�ڶ����������Ǳ���ʱ,������ش���
	 * @return
	 * @throws Exception
	 */
	private String genCodeTable()throws Exception {
		StringBuffer insertCode = new StringBuffer();
		IARESProject project = (IARESProject) context.get(IProcedureEngineContextConstant.Aresproject);
		String targerTableName = getMacroToken().getParameters()[0];//Ŀ�����
		String sourceTableName = getMacroToken().getParameters()[1];//Դ����
		insertCode.append("//�����" + targerTableName + "��¼").append(NEWLINE);
		TableResourceData targetTable = getTableByName(getTableName(targerTableName));// ���ݱ������ҵ���Ӧ��
		TableResourceData sourceTable = getTableByName(getTableName(sourceTableName));// ���ݱ������ҵ���Ӧ��
		if (targetTable == null) {
			insertCode.append("//������¼,�����ڣ�" + targerTableName ).append(NEWLINE);
			//���Ŀ�����Դ�������׳��쳣
			throw new TableNotFoundException(targerTableName);
		}
		if (sourceTable == null) {
			insertCode.append("//������¼,�����ڣ�" + sourceTableName ).append(NEWLINE);
			//���Դ����Դ�������׳��쳣
			throw new TableNotFoundException(sourceTableName);
		}
		List<TableColumn>  targetColumns = getFieldsWithoutFlag("H",targerTableName,targetTable.getColumns());//��ȡĿ���������
		
		
		insertCode.append("\tinsert "+"/*"+this.p.getObjectId()+"*/"+" into ");
		insertCode.append(targerTableName);
		insertCode.append(NEWLINE).append("\t(").append(NEWLINE + "\t\t");
		//����Ŀ����ǵĸ���
		for (int i = 0; i < targetColumns.size(); i++) {

			TableColumn targerTableColumn = targetColumns.get(i);
			StandardField tableFieldSTD  = MetadataServiceProvider.getStandardFieldByName((IARESProject)getContext().get(IProcedureEngineContextConstant.Aresproject), targerTableColumn.getFieldName());
			if (tableFieldSTD != null) {
				if (i != 0) {
					insertCode.append(",");
					if (i % 4 == 0) {//ÿ���ֶλ�һ��
						insertCode.append(NEWLINE+"\t\t");
					}
				}
				
				insertCode.append(CodeUtil.formatInsert(targerTableColumn.getFieldName()));//����insert into�е�һ��
			} else {//������е��в��ڱ�׼�ֶ�����ֳ�����
				insertCode.append(NEWLINE).append("���ֶβ����ڣ�" + targerTableColumn.getFieldName() ).append(NEWLINE);
				throw new TableFieldNotFoundInStdFieldException(targerTableName,targerTableColumn.getFieldName());
			}
		}
		insertCode.append(NEWLINE).append("\t)");
		insertCode.append(NEWLINE).append("\tselect").append(NEWLINE + "\t\t");
		for (int i = 0; i < targetColumns.size(); i++) {

			TableColumn targerTableColumn = targetColumns.get(i);
				String fieldDataType= ProcedureCompilerUtil.getRealDataType(targerTableColumn.getFieldName(), project, MetadataServiceProvider.C_TYPE);
				if (i != 0) {
					insertCode.append(",");
					if (i % 4 == 0) {//ÿ���ֶλ�һ��
						insertCode.append(NEWLINE+"\t\t");
					}
				}
				if (defaultValueMap.get(targerTableColumn.getFieldName()) != null) {//����ֶ���Ĭ��ֵ�б���
					
					if(defaultValueMap.get(targerTableColumn.getFieldName()).startsWith("@") ){
						//�ַ����ַ���������nvl����
						if(TypeRule.typeRuleCharArray(fieldDataType)||TypeRule.typeRuleChar(fieldDataType)){
							insertCode.append(CodeUtil.formatInsert("nvl("+defaultValueMap.get(targerTableColumn.getFieldName())+",' ') " + " as " + targerTableColumn.getFieldName()));
						}else{//�����������Ӻ�������
							insertCode.append(CodeUtil.formatInsert(defaultValueMap.get(targerTableColumn.getFieldName())+ " as "+ targerTableColumn.getFieldName()));
						}
						
					}
						
					else {
						//��Ĭ��ֵ��,��������ͨ��ֵ(�Ǳ���)
						insertCode.append(CodeUtil.formatInsert(defaultValueMap.get(targerTableColumn.getFieldName())+ " as "+ targerTableColumn.getFieldName()));
					}
				} else {
					TableColumn sourceTableColumn = null;
					// ����������Դ���в�����
					for (TableColumn f : getFieldsWithoutFlag("H", sourceTableName,sourceTable.getColumns())) {
						if (f.getFieldName().equals(targerTableColumn.getFieldName())) {
							sourceTableColumn = f;
							break;
						}
					}
					if (sourceTableColumn != null)// Դ���д����ֶ�
					{
						insertCode.append(CodeUtil.formatInsert(targerTableColumn.getFieldName()));
					} else {//���Դ���в�����
						//�����α�������,proc��������,���,�ڲ������д���
						if (getProcVarList().contains(targerTableColumn.getFieldName())|| getPopVarList().contains(targerTableColumn.getFieldName())||ProcedureCompilerUtil.isParameterINProcedureParameterByName(this.p, targerTableColumn.getFieldName(),project)) {
							if(TypeRule.typeRuleCharArray(fieldDataType)||TypeRule.typeRuleChar(fieldDataType))
								insertCode.append(CodeUtil.formatInsert("nvl(@" + targerTableColumn.getFieldName()+",' ')"));
							else {
								insertCode.append(CodeUtil.formatInsert("@"+ targerTableColumn.getFieldName()));
							}
							
						} else {
							//�������ȡĬ��ֵ
							TypeDefaultValue typeDefaultValue  = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName((IARESProject)getContext().get(IProcedureEngineContextConstant.Aresproject), targerTableColumn.getFieldName());
							if(typeDefaultValue!=null){
								insertCode.append(CodeUtil.formatInsert(typeDefaultValue.getValue(MetadataServiceProvider.C_TYPE)) + " as "+ targerTableColumn.getFieldName());
							}
						}
					}
				}
			}
		insertCode.append(NEWLINE).append("\tfrom " ).append(sourceTableName).append(" ;").append(NEWLINE);

		
		return insertCode.toString();
	}
	
	private String genCodeSelect() throws Exception {
		StringBuffer insertCode = new StringBuffer();
		IARESProject project = (IARESProject) context.get(IProcedureEngineContextConstant.Aresproject);
		String targerTableName = getMacroToken().getParameters()[0];//���Ŀ�����
		insertCode.append("\t-- select�����"+targerTableName+"��¼").append(NEWLINE);
		
		TableResourceData targetTable =getTableByName(getTableName(targerTableName));//ȡ��Ŀ�����Դ

		if (targetTable == null) {
			insertCode.append("\t--������¼,�����ڣ�" + targerTableName + "\n");
			throw new TableNotFoundException(targerTableName);
		}
		List<TableColumn>  targetColumns =getFieldsWithoutFlag("H",targerTableName, targetTable.getColumns()) ;//��ȡĿ���������
		insertCode.append("\tinsert "+"/*"+this.p.getObjectId()+"*/"+" into ");
		insertCode.append(targerTableName);
		insertCode.append(NEWLINE).append("\t(").append(NEWLINE + "\t\t");
		for (int i = 0; i < targetColumns.size(); i++) {

			TableColumn targerTableColumn = targetColumns.get(i);
			StandardField tableFieldSTD  = MetadataServiceProvider.getStandardFieldByName((IARESProject)getContext().get(IProcedureEngineContextConstant.Aresproject), targerTableColumn.getFieldName());
			if (tableFieldSTD != null) {
				if (i != 0) {
					insertCode.append(",");
					if (i % 4 == 0) {
						insertCode.append(NEWLINE + "\t\t");//ÿ�ĸ��ֶλ�һ��
					}
				}
				
				insertCode.append(CodeUtil.formatInsert(targerTableColumn.getFieldName()));
			} else {
				insertCode.append(NEWLINE).append("\t���ֶβ����ڣ�" + targerTableColumn.getFieldName() ).append(NEWLINE);
				throw new TableFieldNotFoundInStdFieldException(targerTableName,targerTableColumn.getFieldName());
			}
		}
		insertCode.append(NEWLINE).append("\t)").append(NEWLINE + "\t");
		
		
		String sqlStr = getSqlMacroParameter();//ȡ�ú�����ǵ�sql���
		String fieldStr = "";
		try {
			fieldStr = GenStringUtil.getMainSelectContent(sqlStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		OldSelectFieldsBean old_selectFields = new OldSelectFieldsBean(getFieldString().split(","),splitOldField(getOldField()));
		Map<String,String> selectFields1 = new HashMap<String,String>();
		for(int i = 0;i <old_selectFields.getSelectFileds().length;i++){
			String key = old_selectFields.getSelectFileds()[i];
			String newkey = key;
			if(key.indexOf(".") > 0){
				newkey = removeOtherName(key);
			}
			
			if(!selectFields1.containsKey(newkey)){
				selectFields1.put(newkey,old_selectFields.getOldFileds()[i]);
			}else{
				if(selectFields1.get(newkey).indexOf(".")>=0){
					selectFields1.remove(newkey);
					selectFields1.put(key,old_selectFields.getOldFileds()[i]);
				}
			}
		}
		
		StringBuffer realFields = new StringBuffer();
		//����ȥ�Ҹò���ֵ
		for (int i = 0; i < targetColumns.size(); i++) {

			TableColumn targerTableColumn = targetColumns.get(i);
				
				String fieldDataType= ProcedureCompilerUtil.getRealDataType(targerTableColumn.getFieldName(), project, MetadataServiceProvider.C_TYPE);
				if (i != 0) {
					realFields.append(",");
					if(i%4 == 0 ){
						realFields.append(NEWLINE + "\t\t");
					}
				}
				//���û������Ĭ��ֵ��ȥ��
               if (defaultValueMap.get(targerTableColumn.getFieldName()) != null) {//����ֶ���Ĭ��ֵ�б���
					
					if(defaultValueMap.get(targerTableColumn.getFieldName()).startsWith("@") ){
						
						//�ַ����ַ���������nvl����
						if(TypeRule.typeRuleCharArray(fieldDataType)||TypeRule.typeRuleChar(fieldDataType)){
							realFields.append(CodeUtil.formatInsert("nvl("+defaultValueMap.get(targerTableColumn.getFieldName())+",' ') " + " as " + targerTableColumn.getFieldName()));
						}else{//�����������Ӻ�������
							realFields.append(CodeUtil.formatInsert(defaultValueMap.get(targerTableColumn.getFieldName())+ " as "+ targerTableColumn.getFieldName()));
						}
						
					}
						
					else {
						//��Ĭ��ֵ�б���,��������ͨ��ֵ(�Ǳ���)
						realFields.append(CodeUtil.formatInsert(defaultValueMap.get(targerTableColumn.getFieldName())+ " as "+ targerTableColumn.getFieldName()));
					}
				}else{
					String sourceField = null;
					
					for (int k = 0;k<old_selectFields.getSelectFileds().length;k++) {
						if(selectFields1.containsKey(targerTableColumn.getFieldName())){
							sourceField = selectFields1.get(targerTableColumn.getFieldName()).trim();
							break;
						}
					}
					if (sourceField != null)// Դ���д����ֶ�
					{
						realFields.append(CodeUtil.formatInsert(sourceField));
//						realFields.append(CodeUtil.formatInsert(targerTableColumn.getFieldName()));
					} else {//���Դ���в�����
						//�����α�������,proc��������,���,�ڲ������д���
						if (getProcVarList().contains(targerTableColumn.getFieldName())|| getPopVarList().contains(targerTableColumn.getFieldName())||ProcedureCompilerUtil.isParameterINProcedureParameterByName(this.p, targerTableColumn.getFieldName(),project)) {
							if(TypeRule.typeRuleCharArray(fieldDataType)||TypeRule.typeRuleChar(fieldDataType))
								realFields.append(CodeUtil.formatInsert("nvl(@" + targerTableColumn.getFieldName()+",' ')"));
							else {
								realFields.append(CodeUtil.formatInsert("@"+ targerTableColumn.getFieldName()));
							}
							
						} else {
							//�������ȡĬ��ֵ
							TypeDefaultValue typeDefaultValue  = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName((IARESProject)getContext().get(IProcedureEngineContextConstant.Aresproject), targerTableColumn.getFieldName());
							if(typeDefaultValue!=null){
								realFields.append(CodeUtil.formatInsert(typeDefaultValue.getValue(MetadataServiceProvider.C_TYPE))+" as "+ targerTableColumn.getFieldName());
							}
						}
					}
				
				}
				
			}
		realFields.append(NEWLINE);
		
		int index = sqlStr.indexOf(fieldStr);
		insertCode.append(sqlStr.substring(0,index)+NEWLINE + "\t\t" +realFields.toString()+ "\t" +sqlStr.substring(index+fieldStr.length(),sqlStr.length()));
		return insertCode.toString()+";";
	}
	

	
	/**
	 * 
	 * @return ���غ�ĵ�һ�������е�sql(�Ѿ�ȥ��ע�͵�sql)
	 */
	private String getSqlMacroParameter() {
		String str = getMacroToken().getParameters()[1];
		str = str.replaceAll("\\-\\-[^\n]*\n", "\n");
		return str; 
	}
	
	/**
	 * ��SQL�������ȡ�ֶ�
	 * 
	 * @return �ֶε��ַ������ö��ŷָ�
	 * @throws ErrorParameterNumberException
	 *          �׳����������������쳣
	 * @throws MacroParameterDescNotDefineException
	 *          �׳�����˵����Ϣû�����õ��쳣
	 */
	private String getFieldString() throws HSException {
		String sqlStr = getSqlMacroParameter();
		String sTableName = "";
		if (getMacroToken().getParameters().length > 2) {
			sTableName = getMacroToken().getParameters()[2];
		}
		Pattern p = Pattern.compile("\\s*select\\s+\\*\\s+from\\s*\\(");
		Matcher m = p.matcher(sqlStr);
		while (m.find()) {
			if (m.start() == 0) {
				sqlStr = sqlStr.substring(m.end());
				m = p.matcher(sqlStr);
			}
		}
		
		String fieldStr = "";
		try {
			fieldStr = GenStringUtil.getMainSelectContent(sqlStr);
		} catch (Exception e) {
			e.printStackTrace();
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
				if (!sTableName.equals("")) {
					if (!sTableName.substring(1).equals(",")) {
						sTableName = sTableName + ",";
					}
					if (sTableName.indexOf(",") >= 0) {
						String sTempTableName = sTableName.substring(0, sTableName.indexOf(","));
						if (!sTempTableName.equals("")) {
							// �ӱ�ṹ�л�ȡ��������ֶ��б�
							TableResourceData talbe = getTableByName(getTableName(sTempTableName));
							if (talbe == null) {
								throw new TableNotFoundException(sTempTableName);
							} else {
								List<TableColumn> fields = getFieldsWithoutFlag("H",sTempTableName, talbe.getColumns());
								if (fields.size() == 0) {
									throw new TableFieldNotSettingException(sTempTableName);
								}
								// �����ֶ�׷�ӵ��α��ֶ��б���
								for (int index = 0; index < fields.size(); index++) {
									TableColumn tfield = fields.get(index);
									if (sOtherTableName.equals("")) {
										if (index == fields.size() - 1) {
											sOtherFieldStr = sOtherFieldStr + tfield.getName();
										} else {
											sOtherFieldStr = sOtherFieldStr + tfield.getName() + ",";
										}
									} else {
										if (index == fields.size() - 1) {
											sOtherFieldStr = sOtherFieldStr + sOtherTableName + "." + tfield.getName();
										} else {
											sOtherFieldStr = sOtherFieldStr + sOtherTableName + "." + tfield.getName() + ",";
										}
									}
								}
							}
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
	private String removeOtherName(String str) {
		if(str.indexOf(".") == -1 || str.endsWith(".")){
			return str;
		}
		return str.substring(str.indexOf(".")+1);
	}
	
	

	String[] splitOldField(String fields){
		List<String> result = new ArrayList<String>();
		String temp = "";
		int i = 0;
		while(fields.indexOf(",") >= 0){
			i++;
			temp = "";
			temp = temp + fields.substring(0, fields.indexOf(","));
			fields = fields.substring(fields.indexOf(",")+1, fields.length());
			if(temp.indexOf("(") != -1){
				if(temp.indexOf(" as ") != -1) {
					//select as �������
				}else {
					int next = fields.indexOf(',', fields.indexOf(" as ")+ 4);
					if(next == -1){
						temp = temp + "," + fields;
						fields = "";
						result.add(temp.trim());
						break;
					}
					temp = temp + ","  + fields.substring(0, next);
					fields = fields.substring(next+1, fields.length());
				}
			}
			result.add(temp.trim());
		}
		if(fields.length() != 0)
			result.add(fields);
		return result.toArray(new String[result.size()]);
	}
		
	
	private String getOldField() throws HSException {
		String sqlStr = getSqlMacroParameter();
		String sTableName = "";
		Stack<String> addHead = new Stack<String>();
		if (getMacroToken().getParameters().length > 2) {
			sTableName = getMacroToken().getParameters()[2];
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
		
		String fieldStr = "";
		try {
			fieldStr = GenStringUtil.getMainSelectContent(sqlStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String sFieldStr = fieldStr + ",";
		// ����ĳ������
		int k = 0;
		while (sFieldStr.indexOf(",") >= 0) {
			// ��ı�������
			String sOtherTableName = "";
			// �滻*���ֶα���
			String sOtherFieldStr = "";

			String sTempField = sFieldStr.substring(0, sFieldStr.indexOf(",")).trim();
			
			if(sTempField.indexOf ("*/") != -1 && sTempField.indexOf ("/*") != -1){//����ע��
				sTempField = sTempField.substring (sTempField.indexOf ("*/")+2);
			}

			// ����Ǵ��������ֶΣ�����ȥ������
			if (sTempField.indexOf("(") >= 0) {
				if (sTempField.toLowerCase().indexOf(" as ") < 0) {
					sFieldStr = sFieldStr.substring(sFieldStr.indexOf(",") + 1).trim();
					do {//��Ϊ�����к�������","�������кܶ��
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
				if (!sTableName.equals("")) {
					if (!sTableName.substring(1).equals(",")) {
						sTableName = sTableName + ",";
					}
					if (sTableName.indexOf(",") >= 0) {
						String sTempTableName = sTableName.substring(0, sTableName.indexOf(","));
						if (!sTempTableName.equals("")) {
							// �ӱ�ṹ�л�ȡ��������ֶ��б�
							TableResourceData table = getTableByName(getTableName(sTempTableName));

							if (table == null) {
								throw new TableNotFoundException(sTempTableName);
							} else {
								List<TableColumn> fields = getFieldsWithoutFlag("H", sTempTableName, table.getColumns());
								if (fields.size() == 0) {
									throw new TableFieldNotSettingException(sTempTableName);
								}
								
								// �����ֶ�׷�ӵ��α��ֶ��б���
								for (int index = 0; index < fields.size(); index++) {
									TableColumn tfield = fields.get(index);
									if (sOtherTableName.equals("")) {
										if (index == fields.size() - 1) {
											sOtherFieldStr = sOtherFieldStr + tfield.getName();
										} else {
											sOtherFieldStr = sOtherFieldStr + tfield.getName() + ",";
										}
									} else {
										if (index == fields.size() - 1) {
											sOtherFieldStr = sOtherFieldStr + sOtherTableName + "." + tfield.getName();
										} else {
											sOtherFieldStr = sOtherFieldStr + sOtherTableName + "." + tfield.getName() + ",";
										}
									}
								}
							}
						}
						// �滻.*
						if (!sOtherFieldStr.equals("")) {
							if (sOtherTableName.equals("")) {
								sqlStr = sqlStr.replace("*", sOtherFieldStr);
							} else {
								sqlStr = sqlStr.replace(sOtherTableName + ".*", sOtherFieldStr);
							}
						}
						sTableName = sTableName.substring(sTableName.length() - sTableName.indexOf(","));
					}
				}
			}
			if (sFieldStr.length() > sFieldStr.indexOf(",")) {//������һ���ֶ�
				sFieldStr = sFieldStr.substring(sFieldStr.indexOf(",") + 1);
			} else {
				sFieldStr = "";
			}
		}
		String result = "";
		try {
			result = GenStringUtil.getMainSelectContent(sqlStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	
	/**
	 * ���ݱ�����ñ���Դ
	 * @param tableName
	 * @return
	 */
	private TableResourceData getTableByName(String tableName){
		if(!tableName.isEmpty()){
			tableName = tableName.trim();
		}
		String realTableResourceName =tableName;
		if(tableName.startsWith("his_")){
			realTableResourceName = tableName.replaceFirst("his_", StringUtils.EMPTY);
		}else if(tableName.startsWith("fil_")){
			realTableResourceName = tableName.replaceFirst("fil_", StringUtils.EMPTY);
		}else if(tableName.startsWith("r_")){
			realTableResourceName = tableName.replaceFirst("r_", StringUtils.EMPTY);
		}else if(tableName.startsWith("rl_")){
			realTableResourceName = tableName.replaceFirst("rl_", StringUtils.EMPTY);
		}
		
		IARESProject aresProject = (IARESProject) this.context.get(IProcedureEngineContextConstant.Aresproject);
		ReferenceManager manager = ReferenceManager.getInstance();
		ReferenceInfo ref = manager.getFirstReferenceInfo(aresProject, IDatabaseRefType.Table, realTableResourceName, true);
		if (ref != null) {
			return (TableResourceData) ref.getObject();
		}
			
		return null;
		
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
				return  tableAllName.substring(tableAllName.indexOf(".") + 1);
			}
		}
		return tableAllName;
	}
	
	/**
	 * ����α����ʹ�ñ����б�
	 * @return
	 */
	private List<String> getPopVarList(){
		return  (List<String>)getContext().get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
	}
	
	/**
	 * ����proc�����б�
	 * @return
	 */
	private Set<String> getProcVarList(){
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)getContext().get(IProcedureEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		return  (Set<String>)helper.getAttribute(IProcedureEngineContextConstant.ATTR_PROC_VARIABLE_LIST);
	}
	
	private List<TableColumn> getFieldsWithoutFlag(String flag,String tableName,List<TableColumn> tableColumns) {
		
		if(tableName.indexOf(".") >= 0){
			tableName = tableName.substring(tableName.lastIndexOf(".")+1);
		}
		if(!tableName.isEmpty()){
			tableName = tableName.trim();
			if(tableName.startsWith("his_")||
					tableName.startsWith("fil_")||
					tableName.startsWith("r_")||
					tableName.startsWith("rl_")){
				return tableColumns;
			}
		}
		
		List<TableColumn> filtered = new ArrayList<TableColumn>();
		char[] flags = flag.toCharArray();
		for (TableColumn field : tableColumns) {
			boolean contains = false;
			
			for (int ch : flags) {
				if (field.getMark() != null) {
					if (field.getMark().indexOf(ch) != -1) {
						contains = true;
						break;
					}
				}
			}
			if (!contains) {
				filtered.add(field);
			}
		}
		return filtered;
		}

	/**
	 * ��ú�
	 * @return IMacroToken
	 */
	private IMacroToken getMacroToken(){
		return this.macroToken;
	}
	/**
	 * ���������
	 * @return
	 */
	private Map<Object,Object> getContext(){
		return this.context;
	}
	
	/*
	 * 
	 */
	private class OldSelectFieldsBean {
		
		String[] oldFileds = {};
		
		String[] selectFileds = {};

		
		/**
		 * @return the oldFiledName
		 */
		public String[] getOldFileds() {
			return oldFileds;
		}


		/**
		 * @param oldFiledName the oldFiledName to set
		 */
		public void setOldFileds(String[] oldFileds) {
			this.oldFileds = oldFileds;
		}


		/**
		 * @return the selectFiledName
		 */
		public String[] getSelectFileds() {
			return selectFileds;
		}


		/**
		 * @param selectFiledName the selectFiledName to set
		 */
		public void setSelectFileds(String[] selectFileds) {
			this.selectFileds = selectFileds;
		}


		public OldSelectFieldsBean(String[] selectFileds,String[] oldFileds){
			this.oldFileds = oldFileds;
			this.selectFileds = selectFileds;
		}

	}

}
