/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.AtomFunctionCompilerUtil;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.MarkConfig;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.ParamReplaceUtil;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.TableResourceUtil;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.model.BasicResourceInfo;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.ITokenDomain;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.procdure.Procedure;

/**
 * @author zhuyf
 *
 */
public class InsertTableInProcBlockMacroHandler implements IMacroTokenHandler {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return MacroConstant.INSERT_TABLE_INPROCBLOCK_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*
		 * ����������
		[������¼][table][Ĭ��ֵ]
		�������̣�
		1.��α����༭��������[������¼][table][Ĭ��ֵ]���������Ԥ��tabҳ���鿴��Ӧ����ʵ���롣
		�ھ���Ĵ�������У�����ȡ�ñ���������ñ����ڣ����׳��쳣��������¼,�����ڡ����ٸ��ݱ����õ������е��ֶ�����������ֶ��ڱ�׼�ֶ����Ҳ��������׳��쳣�����ֶβ����ڡ�����ÿ���ֶ�ֵ��������ֵ��ȡֵ�������£�
		�����ж���[Ĭ��ֵ]���Ƿ��и��ֶε�Ĭ��ֵ���������ȡ����Ĭ��ֵ�����û�оʹ���������������ڲ�������Ѱ�ң�������ں͸��ֶ�ͬ���ı����򽫸ñ�����ֵ��Ϊ����ֵ�������û��������ֶ����ӱ�׼�ֶ��б���ȡ�ĸ��ֶε���ʵĬ��ֵ��Ϊ����ֵ��
		2.�ú�һ������ٹ��̻�����PROC�����С�
		3.���δ��PROC������ʹ�ã����׳��쳣��
		4.֧�ֿ��û���������[������¼][his_clientjour][clientjour]
			[������¼][fil_clientjour][clientjour]
			[������¼][r_clientjour][clientjour]
			[������¼][rl_clientjour][clientjour]
		 */
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_MACRO, this.getKey());
		helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_DATABASE_MACRO, this.getKey());
		List<ICodeToken> tokens = new ArrayList<ICodeToken>();
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		ITokenDomain procBlockBeginDomain =handler.getDomain(MacroConstant.PROC_BLOCK_BEGIN_MACRONAME);
		if (procBlockBeginDomain == null && !(context.get(IAtomEngineContextConstantMySQL.ResourceModel) instanceof Procedure)) {
			throw new RuntimeException(getKey() + " δ��PROC������ʹ��");
		}
		if (token.getParameters().length > 0) {
			IARESProject project = (IARESProject) context.get(IAtomEngineContextConstantMySQL.Aresproject);
			List<TableColumn> tableColumns = TableResourceUtil.getFieldsWithoutFlag("H",token.getParameters()[0],project);
				Map<String ,String> defaultMap = new HashMap<String, String>();
				if (token.getParameters().length > 1) {
					for (String defaultValue : token.getParameters()[1].split(",")) {
						String[] pair = StringUtils.split(defaultValue, "=");
						if (StringUtils.isNotBlank(defaultValue) && StringUtils.split(defaultValue, "=").length > 1) {
							defaultMap.put(pair[0].trim(), pair[1].trim());
						}
					}
				}
				for(TableColumn tc : tableColumns){
					StandardField std = MetadataServiceProvider.getStandardFieldByName(project, tc.getFieldName());
					if (std != null) {
						//����PROC��������
						helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_VARIABLE_LIST, tc.getFieldName());
						String defaultValue = getDefaultValue(defaultMap, context, project, tc.getFieldName());
//						tc.setDefaultValue(defaultValue);
						if(!defaultMap.containsKey(tc.getFieldName())){
							defaultMap.put(tc.getFieldName(), defaultValue);
						}else if(defaultMap.containsKey(tc.getFieldName())){//���Ĭ��ֵ�����ֶζ�Ӧ��Ĭ��ֵ
							String valueVarName = defaultMap.get(tc.getFieldName());
							if (valueVarName.indexOf(MarkConfig.MARK_AT) >= 0) {// ���Ĭ�ϲ���ֵΪ����
								String procVarName = valueVarName.substring(valueVarName.indexOf(MarkConfig.MARK_AT) + 1);
								helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_VARIABLE_LIST,procVarName);
								defaultMap.put(tc.getFieldName(),":" +defaultMap.get(tc.getFieldName()));
							}
						}
					
					}else {
						throw new RuntimeException(String.format("���ֶΣ�%1$s��Ӧ��׼�ֶβ����ڡ�",tc.getFieldName()));
					}
				}
				tokens.add(new InsertTableCodeTokenImpl(token.getParameters()[0],tableColumns,defaultMap));
		}
		
		return tokens.iterator();
	}
	
	/**
	 * ��ȡĬ��ֵ
	 * 
	 * @param token
	 * @param context
	 * @param project
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	private String getDefaultValue(Map<String ,String> defaultMap ,Map<Object, Object> context , IARESProject project ,String fieldName) throws Exception{
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		popVarList.add(fieldName);
		
		if (defaultMap.get(fieldName) != null) {
			return defaultMap.get(fieldName);
		}
		
		String colomnType=StringUtils.EMPTY;
		colomnType= AtomFunctionCompilerUtil.getRealDataType(fieldName, project, context);
		if (TypeRule.typeRuleInt(colomnType) || TypeRule.typeRuleDouble(colomnType) || TypeRule.typeRuleClob(colomnType))// ��������Ϊint || double
		{
			return ":@" + fieldName;

		}  else if (TypeRule.typeRuleChar(colomnType) || TypeRule.typeRuleCharArray(colomnType))// ��������Ϊchar��char[]
		{
			return "nvl(:@"+fieldName+" , ' ')";
		}
		return StringUtils.EMPTY;
	}
	

	private class InsertTableCodeTokenImpl implements ICodeToken{

		private String tableName;
		private List<TableColumn>columns; 
		private Map<String ,String> defaultMap;
		
		public InsertTableCodeTokenImpl(String tableName,List<TableColumn>columns,Map<String ,String> defaultMap){
			this.tableName = tableName;
			this.columns = columns;
			this.defaultMap = defaultMap;
		}
		
		@Override
		public String getContent() {
			return null;
		}

		@Override
		public int getType() {
			return ICodeToken.CODE_TEXT;
		}

		@Override
		public String genCode(Map<Object, Object> context) throws Exception {

			StringBuffer insertCode = new StringBuffer();

			BasicResourceInfo brInfo = (BasicResourceInfo) context.get(IAtomEngineContextConstantMySQL.ResourceModel);
			insertCode.append("insert "+"/*"+brInfo.getObjectId()+"*/"+" into ");
			insertCode.append(tableName);
			insertCode.append("\r\n(\r\n");
			for (int i = 0; i < columns.size(); i++) {
				TableColumn field = columns.get(i);
				if (i != 0) {
					insertCode.append(",");
					if (i % 4 == 0) {
						insertCode.append("\r\n");
					}
				}
				insertCode.append(ParamReplaceUtil.formatInsert(field.getName()));
			}
			insertCode.append("\r\n)");
			insertCode.append(" \r\nvalues\r\n(\r\n");
			for (int i = 0; i < columns.size(); i++) {
				TableColumn column = columns.get(i);
				if (i != 0) {
					insertCode.append(",");
					if (i % 4 == 0) {
						insertCode.append("\r\n");
					}
				}
				if(defaultMap.containsKey(column.getFieldName())){
					insertCode.append(defaultMap.get(column.getFieldName()));
				}else {
					insertCode.append(column.getDefaultValue());
				}
			}
			insertCode.append("\r\n);\r\n");
		
			return insertCode.toString();
		}
	}
	
}
