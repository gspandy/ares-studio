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
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.exception.MacroParameterDescNotDefineException;
import com.hundsun.ares.studio.atom.compiler.mysql.exception.TableFieldNotFoundInStdFieldException;
import com.hundsun.ares.studio.atom.compiler.mysql.exception.TableFieldNotSettingException;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.AtomFunctionCompilerUtil;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.CodeUtil;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.GenStringUtil;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.TableResourceUtil;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.exception.ErrorParameterNumberException;
import com.hundsun.ares.studio.engin.exception.HSException;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;

/**
 * @author liaogc
 *
 */
public class SelectInsertTableInProcBlockToken implements ICodeToken{
	
	private IMacroToken macroToken ;//��ǰ����ĺ�
	private Map<Object, Object> context;//��ǰ������������
	private AtomFunction atomFunction;//ԭ�Ӻ���ģ��
	private Map<String, String> defaultValueMap = new HashMap<String, String>();//Ĭ��ֵ�б�
	
	public SelectInsertTableInProcBlockToken(IMacroToken macroToken,Map<Object, Object> context,Map<String, String> defaultValueMap){
		this.macroToken = macroToken;
		this.context = context;
		this.atomFunction = (AtomFunction) context.get(IAtomEngineContextConstantMySQL.ResourceModel);
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
		IARESProject project = (IARESProject) context.get(IAtomEngineContextConstantMySQL.Aresproject);
		String targerTableName = getMacroToken().getParameters()[0];//Ŀ�����
		String sourceTableName = getMacroToken().getParameters()[1];//Դ����
		insertCode.append("//�����" + targerTableName + "��¼").append(NEWLINE);
		List<TableColumn>  targetColumns = TableResourceUtil.getFieldsWithoutFlag("H",targerTableName,project);//��ȡĿ���������
		
		
		insertCode.append("insert "+"/*"+this.atomFunction.getObjectId()+"*/"+" into ");
		insertCode.append(targerTableName);
		insertCode.append(NEWLINE).append("(").append(NEWLINE+"\t");
		//����Ŀ����ǵĸ���
		for (int i = 0; i < targetColumns.size(); i++) {

			TableColumn targerTableColumn = targetColumns.get(i);
			StandardField tableFieldSTD  = MetadataServiceProvider.getStandardFieldByName((IARESProject)getContext().get(IAtomEngineContextConstantMySQL.Aresproject), targerTableColumn.getFieldName());
			if (tableFieldSTD != null) {
				if (i != 0) {
					insertCode.append(",");
					if (i % 4 == 0) {//ÿ���ֶλ�һ��
						insertCode.append(NEWLINE+"\t");
					}
				}
				
				insertCode.append(CodeUtil.formatInsert(targerTableColumn.getFieldName()));//����insert into�е�һ��
			} else {//������е��в��ڱ�׼�ֶ�����ֳ�����
				insertCode.append(NEWLINE).append("���ֶβ����ڣ�" + targerTableColumn.getFieldName() ).append(NEWLINE);
				throw new TableFieldNotFoundInStdFieldException(targerTableName,targerTableColumn.getFieldName());
			}
		}
		insertCode.append(NEWLINE).append(")");
		insertCode.append(NEWLINE).append("select").append(NEWLINE+"\t");
		for (int i = 0; i < targetColumns.size(); i++) {

			TableColumn targerTableColumn = targetColumns.get(i);
			String fieldDataType= AtomFunctionCompilerUtil.getRealDataType(targerTableColumn.getFieldName(), project, context);
				if (i != 0) {
					insertCode.append(",");
					if (i % 4 == 0) {//ÿ���ֶλ�һ��
						insertCode.append(NEWLINE+"\t");
					}
				}
				if (defaultValueMap.get(targerTableColumn.getFieldName()) != null) {//����ֶ���Ĭ��ֵ�б���
					
					if(defaultValueMap.get(targerTableColumn.getFieldName()).startsWith("@") ){
						//�ַ����ַ���������nvl����
						if(TypeRule.typeRuleCharArray(fieldDataType)||TypeRule.typeRuleChar(fieldDataType)){
							insertCode.append(CodeUtil.formatInsert("nvl("+":"+defaultValueMap.get(targerTableColumn.getFieldName())+",' ') " + " as " + targerTableColumn.getFieldName()));
						}else{//�����������Ӻ�������
							insertCode.append(CodeUtil.formatInsert(":"+defaultValueMap.get(targerTableColumn.getFieldName())+ " as "+ targerTableColumn.getFieldName()));
						}
						
					}
						
					else {
						//��Ĭ��ֵ��,��������ͨ��ֵ(�Ǳ���)
						insertCode.append(CodeUtil.formatInsert(defaultValueMap.get(targerTableColumn.getFieldName())+ " as "+ targerTableColumn.getFieldName()));
					}
				} else {
					TableColumn sourceTableColumn = null;
					// ����������Դ���в�����
					for (TableColumn f : TableResourceUtil.getFieldsWithoutFlag("H", sourceTableName,project)) {
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
						if (getProcVarList().contains(targerTableColumn.getFieldName())|| getProcVarList().contains(targerTableColumn.getFieldName())||AtomFunctionCompilerUtil.isParameterINAtomFunctionParameterByName(getAtomFunction(), targerTableColumn.getFieldName(),project)) {
							if(TypeRule.typeRuleCharArray(fieldDataType)||TypeRule.typeRuleChar(fieldDataType))
								insertCode.append(CodeUtil.formatInsert("nvl(:@" + targerTableColumn.getFieldName()+",' ')"));
							else {
								insertCode.append(CodeUtil.formatInsert(":@"+ targerTableColumn.getFieldName()));
							}
							
						} else {
							//�������ȡĬ��ֵ
							TypeDefaultValue typeDefaultValue  = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName((IARESProject)getContext().get(IAtomEngineContextConstantMySQL.Aresproject), targerTableColumn.getFieldName());
							if(typeDefaultValue!=null){
								String value = typeDefaultValue.getValue(MetadataServiceProvider.C_TYPE);
								if(StringUtils.equals(value, "{0}")){
									value = "' '";
								}
								insertCode.append(CodeUtil.formatInsert(value) + " as "+ targerTableColumn.getFieldName());
							}
						}
					}
				}
			}
		insertCode.append(NEWLINE).append("from " ).append(sourceTableName).append(" ;").append(NEWLINE);

		
		return insertCode.toString();
	}
	


	
	private String genCodeSelect() throws Exception {
		StringBuffer insertCode = new StringBuffer();
		IARESProject project = (IARESProject) context.get(IAtomEngineContextConstantMySQL.Aresproject);
		String targerTableName = getMacroToken().getParameters()[0];//���Ŀ�����
		insertCode.append("-- select�����"+targerTableName+"��¼").append(NEWLINE);
		
		List<TableColumn>  targetColumns = TableResourceUtil.getFieldsWithoutFlag("H",targerTableName, project) ;//��ȡĿ���������
		insertCode.append("insert "+"/*"+this.atomFunction.getObjectId()+"*/"+" into ");
		insertCode.append(targerTableName);
		insertCode.append(NEWLINE).append("(").append(NEWLINE);
		for (int i = 0; i < targetColumns.size(); i++) {

			TableColumn targerTableColumn = targetColumns.get(i);
			StandardField tableFieldSTD  = MetadataServiceProvider.getStandardFieldByName((IARESProject)getContext().get(IAtomEngineContextConstantMySQL.Aresproject), targerTableColumn.getFieldName());
			if (tableFieldSTD != null) {
				if (i != 0) {
					insertCode.append(",");
					if (i % 4 == 0) {
						insertCode.append(NEWLINE);//ÿ�ĸ��ֶλ�һ��
					}
				}
				
				insertCode.append(CodeUtil.formatInsert(targerTableColumn.getFieldName()));
			} else {
				insertCode.append(NEWLINE).append("���ֶβ����ڣ�" + targerTableColumn.getFieldName() ).append(NEWLINE);
				throw new TableFieldNotFoundInStdFieldException(targerTableName,targerTableColumn.getFieldName());
			}
		}
		insertCode.append(NEWLINE).append(")").append(NEWLINE);
		
		
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
			String fieldDataType= AtomFunctionCompilerUtil.getRealDataType(targerTableColumn.getFieldName(), project, context);
				if (i != 0) {
					realFields.append(",");
					if(i%4 == 0 ){
						realFields.append(NEWLINE);
					}
				}
				//���û������Ĭ��ֵ��ȥ��
               if (defaultValueMap.get(targerTableColumn.getFieldName()) != null) {//����ֶ���Ĭ��ֵ�б���
					
					if(defaultValueMap.get(targerTableColumn.getFieldName()).startsWith("@") ){
						
						//�ַ����ַ���������nvl����
						if(TypeRule.typeRuleCharArray(fieldDataType)||TypeRule.typeRuleChar(fieldDataType)){
							realFields.append(CodeUtil.formatInsert("nvl("+":"+defaultValueMap.get(targerTableColumn.getFieldName())+",' ') " + " as " + targerTableColumn.getFieldName()));
						}else{//�����������Ӻ�������
							realFields.append(CodeUtil.formatInsert(":"+defaultValueMap.get(targerTableColumn.getFieldName())+ " as "+ targerTableColumn.getFieldName()));
						}
						
					}
						
					else {
						//��Ĭ��ֵ��,��������ͨ��ֵ(�Ǳ���)
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
					} else {//���Դ���в�����
						//�����α�������,proc��������,���,�ڲ������д���
						if (getProcVarList().contains(targerTableColumn.getFieldName())|| getPopVarList().contains(targerTableColumn.getFieldName())||AtomFunctionCompilerUtil.isParameterINAtomFunctionParameterByName(getAtomFunction(), targerTableColumn.getFieldName(),project)) {
							if(TypeRule.typeRuleCharArray(fieldDataType)||TypeRule.typeRuleChar(fieldDataType))
								realFields.append(CodeUtil.formatInsert("nvl(:@" + targerTableColumn.getFieldName()+",' ')"));
							else {
								realFields.append(CodeUtil.formatInsert(":@"+ targerTableColumn.getFieldName()));
							}
							
						} else {
							//�������ȡĬ��ֵ
							TypeDefaultValue typeDefaultValue  = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName((IARESProject)getContext().get(IAtomEngineContextConstantMySQL.Aresproject), targerTableColumn.getFieldName());
							if(typeDefaultValue!=null){
								realFields.append(CodeUtil.formatInsert(typeDefaultValue.getValue(MetadataServiceProvider.C_TYPE))+" as "+ targerTableColumn.getFieldName());
							}
						}
					}
				
				}
				
			}
		realFields.append(NEWLINE);
		
		int index = sqlStr.indexOf(fieldStr);
		insertCode.append(sqlStr.substring(0,index)+NEWLINE+realFields.toString()+sqlStr.substring(index+fieldStr.length(),sqlStr.length()));
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
							IARESProject project = (IARESProject) context.get(IAtomEngineContextConstantMySQL.Aresproject);
								List<TableColumn> fields = TableResourceUtil.getFieldsWithoutFlag("H",sTempTableName, project);
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
								IARESProject project = (IARESProject) context.get(IAtomEngineContextConstantMySQL.Aresproject);
								
								List<TableColumn> fields = TableResourceUtil.getFieldsWithoutFlag("H",sTempTableName, project);
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
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)getContext().get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		return  (Set<String>)helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_VARIABLE_LIST);
	}
	
	
	/**
	 * ����ԭ�Ӻ���ģ��
	 * @return
	 */
	private AtomFunction getAtomFunction(){
		return this.atomFunction;
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
