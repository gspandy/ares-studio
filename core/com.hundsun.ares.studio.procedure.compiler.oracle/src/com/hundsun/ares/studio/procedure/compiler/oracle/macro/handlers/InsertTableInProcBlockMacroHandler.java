/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.oracle.macro.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.model.BasicResourceInfo;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.database.ColumnType;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.procedure.compiler.oracle.constant.IProcedureEngineContextConstantOracle;
import com.hundsun.ares.studio.procedure.compiler.oracle.macro.MacroConstant;
import com.hundsun.ares.studio.procedure.compiler.oracle.skeleton.util.ParamReplaceUtil;
import com.hundsun.ares.studio.procedure.compiler.oracle.skeleton.util.ProcedureCompilerUtil;
import com.hundsun.ares.studio.procedure.compiler.oracle.skeleton.util.TableResourceUtil;
import com.hundsun.ares.studio.reference.ReferenceManager;

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
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IProcedureEngineContextConstantOracle.SKELETON_ATTRIBUTE_HELPER);
		helper.addAttribute(IProcedureEngineContextConstantOracle.ATTR_PROC_MACRO, this.getKey());
		helper.addAttribute(IProcedureEngineContextConstantOracle.ATTR_DATABASE_MACRO, this.getKey());
		List<ICodeToken> tokens = new ArrayList<ICodeToken>();
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
//		ITokenDomain procBlockBeginDomain =handler.getDomain(MacroConstant.PROC_BLOCK_BEGIN_MACRONAME);
//		if (procBlockBeginDomain == null && !(context.get(IAtomEngineContextConstant.ResourceModel) instanceof Procedure)) {
//			throw new RuntimeException(getKey() + " δ��PROC������ʹ��");
//		}
		if (token.getParameters().length > 0) {
			TableResourceData table = getTableByName(context , getTableName(token.getParameters()[0]));
			if (table == null) {
				throw new RuntimeException(String.format("������¼,��:%1$s�����ڡ�",token.getParameters()[0] ));
			}else {
				IARESProject project = (IARESProject)context.get(IProcedureEngineContextConstantOracle.Aresproject);
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
						helper.getAttribute(IProcedureEngineContextConstantOracle.ATTR_PROC_VARIABLE_LIST).add(tc.getFieldName());
						String defaultValue = getDefaultValue(defaultMap, context, project, tc.getFieldName());
//						tc.setDefaultValue(defaultValue);
						if(!defaultMap.containsKey(tc.getFieldName())){
							defaultMap.put(tc.getFieldName(), defaultValue);
						}
					}else {
						throw new RuntimeException(String.format("���ֶΣ�%1$s��Ӧ��׼�ֶβ����ڡ�",tc.getFieldName()));
					}
				}
				tokens.add(new InsertTableCodeTokenImpl(table,defaultMap));
			}
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
		colomnType= ProcedureCompilerUtil.getRealDataType(fieldName, project, MetadataServiceProvider.C_TYPE);
		if (TypeRule.typeRuleInt(colomnType) || TypeRule.typeRuleDouble(colomnType) || TypeRule.typeRuleClob(colomnType))// ��������Ϊint || double
		{
			return "@" + fieldName;

		}  else if (TypeRule.typeRuleChar(colomnType) || TypeRule.typeRuleCharArray(colomnType))// ��������Ϊchar��char[]
		{
			return "nvl(@"+fieldName+" , ' ')";
		}
		return StringUtils.EMPTY;
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
				return tableAllName = tableAllName.substring(tableAllName.indexOf(".") + 1);
			}else{
				return tableAllName;
			}
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * ���ݱ�����ñ���Դ
	 * @param tableName
	 * @return
	 */
	private TableResourceData getTableByName(Map<Object, Object> context ,String tableName){
		IARESProject aresProject = (IARESProject) (context.get(IProcedureEngineContextConstantOracle.Aresproject));
		ReferenceManager manager = ReferenceManager.getInstance();
		ReferenceInfo ref = manager.getFirstReferenceInfo(aresProject, IDatabaseRefType.Table, tableName, true);
		if (ref != null) {
			return (TableResourceData) ref.getObject();
		}else{
			ITokenListenerManager  tokenListenerManager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("����Դ��ȡ����,��ȷ������Դ�Ƿ���ڻ����Ƿ�ͬ��", MacroConstant.INSERT_TABLE_INPROCBLOCK_MACRONAME);
			tokenListenerManager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
		}
		return null;
	}
	
	private class InsertTableCodeTokenImpl implements ICodeToken{

		private TableResourceData table;
		private Map<String ,String> defaultMap;
		//���Ĭ��ֵ����ֹһ����Դ�ж�γ�һ�����в����¼ʱ��Ĭ��ֵ��ͻ
		public InsertTableCodeTokenImpl(TableResourceData table,Map<String ,String> defaultMap){
			this.table = table;
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
			IARESProject aresProject = (IARESProject) (context.get(IProcedureEngineContextConstantOracle.Aresproject));
			StringBuffer insertCode = new StringBuffer();
			String tableName = table.getName();

			TableColumn[] columns = ParamReplaceUtil.getFieldsWithoutFlag(table , "H",tableName);
			BasicResourceInfo brInfo = (BasicResourceInfo) context.get(IProcedureEngineContextConstantOracle.ResourceModel);
			insertCode.append("\tinsert "+"/*"+brInfo.getObjectId()+"*/"+" into ");
			insertCode.append(tableName);
			insertCode.append("\r\n\t(\r\n\t\t");
			for (int i = 0; i < columns.length; i++) {
				TableColumn field = columns[i];
				if (i != 0) {
					insertCode.append(",");
					if (i % 4 == 0) {
						insertCode.append("\r\n\t\t");
					}
				}
				insertCode.append(ParamReplaceUtil.formatInsert(field.getName()));
			}
			insertCode.append("\r\n\t)\r\n");
			insertCode.append("\tvalues\r\n\t(\r\n\t\t");
			for (int i = 0; i < columns.length; i++) {
				TableColumn column = columns[i];
				if (i != 0) {
					insertCode.append(",");
					if (i % 4 == 0) {
						insertCode.append("\r\n\t\t");
					}
				}
				if(defaultMap.containsKey(column.getFieldName()) && StringUtils.isNotBlank(defaultMap.get(column.getFieldName()))){
					insertCode.append(defaultMap.get(column.getFieldName()));
				}else {
					String dv = column.getDefaultValue();
					if (StringUtils.isBlank(dv) && column.getColumnType() == ColumnType.STD_FIELD) {
						ReferenceInfo info = ReferenceManager.getInstance().getFirstReferenceInfo(aresProject, IMetadataRefType.StdField, column.getFieldName(), false);
						if (info != null && info.getObject() instanceof StandardField) {
							StandardField std = (StandardField) info.getObject();
							String bizType = std.getDataType();
							info = ReferenceManager.getInstance().getFirstReferenceInfo(aresProject, IMetadataRefType.BizType, bizType, false);
							if (info != null && info.getObject() instanceof BusinessDataType) {
								BusinessDataType busType = (BusinessDataType) info.getObject();
								String defaultValue = busType.getDefaultValue();
								info = ReferenceManager.getInstance().getFirstReferenceInfo(aresProject, IMetadataRefType.DefValue, defaultValue, false);
								if (info != null && info.getObject() instanceof TypeDefaultValue) {
									TypeDefaultValue tdv = (TypeDefaultValue) info.getObject();
									dv = tdv.getValue("oracle");
								}
							}
						}
					}
					insertCode.append(dv);
				}
			}
			insertCode.append("\r\n\t);\r\n");
		
			return insertCode.toString();
		}
	}
	
}
