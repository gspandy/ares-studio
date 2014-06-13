/**
 * Դ�������ƣ�TableScriptWrapImpl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse.core.script.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.service.DataServiceManager;
import com.hundsun.ares.studio.jres.metadata.service.IBusinessDataType;
import com.hundsun.ares.studio.jres.metadata.service.IMetadataService;
import com.hundsun.ares.studio.jres.metadata.service.IStandardField;
import com.hundsun.ares.studio.jres.model.chouse.AddConstraintModification;
import com.hundsun.ares.studio.jres.model.chouse.AddTableColumnPKModification;
import com.hundsun.ares.studio.jres.model.chouse.AddTableColumnUniqueModifycation;
import com.hundsun.ares.studio.jres.model.chouse.CTCPMDetail;
import com.hundsun.ares.studio.jres.model.chouse.CTCUMDetail;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnPrimaryKeyModifycation;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnUniqueModifycation;
import com.hundsun.ares.studio.jres.model.chouse.ChouseFactory;
import com.hundsun.ares.studio.jres.model.chouse.ConstraintModifyDetail;
import com.hundsun.ares.studio.jres.model.chouse.IStock3Constant;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.chouse.ModifyDetail;
import com.hundsun.ares.studio.jres.model.chouse.RemoveConstraintModification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveTableColumnPKModification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveTableColumnUniqueModifycation;
import com.hundsun.ares.studio.jres.model.chouse.RevisionHistoryProperty;
import com.hundsun.ares.studio.jres.model.chouse.util.RevHistoryCompator;
import com.hundsun.ares.studio.jres.model.chouse.util.StockUtils;
import com.hundsun.ares.studio.jres.model.database.DatabaseFactory;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableIndex;
import com.hundsun.ares.studio.jres.model.database.TableKey;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.jres.model.database.key_type;
import com.hundsun.ares.studio.jres.model.database.oracle.DatabaseModuleExtensibleProperty;
import com.hundsun.ares.studio.jres.model.database.oracle.OracleSpaceResourceData;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpace;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpaceRelation;
import com.hundsun.ares.studio.jres.model.database.oracle.table_type;
import com.hundsun.ares.studio.jres.model.database.util.DatabaseUtil;
import com.hundsun.ares.studio.jres.script.api.database.ITableColScriptWrap;
import com.hundsun.ares.studio.jres.script.api.database.ITableIndexScriptWrap;
import com.hundsun.ares.studio.jres.script.api.database.ITableKeyScriptWrap;
import com.hundsun.ares.studio.jres.script.api.database.ITableScriptWrap;
import com.hundsun.ares.studio.jres.script.api.wrap.IModificationScriptWrap;
import com.hundsun.ares.studio.jres.script.api.wrap.ITableRevHistoryScriptWrap;
import com.hundsun.ares.studio.jres.script.internal.util.IJSONUtil;
import com.hundsun.ares.studio.jres.script.tool.HistoryCommentCompator;
import com.hundsun.ares.studio.jres.script.tool.HistoryCommentHelper;

/**
 * @author yanwj06282
 * 
 */
public class TableScriptWrapImpl extends DatabaseResScriptWrapImpl implements ITableScriptWrap {

	private TableResourceData table;
	private List<ITableColScriptWrap> columnScriptWraps ;
	private List<ITableIndexScriptWrap> indexScriptWraps ;
	private List<ITableKeyScriptWrap> keyScriptWraps ;
	private boolean isGenHisTable = false;
	private ITableRevHistoryScriptWrap history;

	/**
	 * @param resource
	 */
	public TableScriptWrapImpl(IARESResource resource) {
		super(resource);
		try {
			isGenHisTable = Boolean.parseBoolean(getValueByJson("chouse_history"));
		} catch (Exception e) {
		}
	}

	public String getChineseName(){
		return getOriginalInfo().getChineseName();
	}
	
	@Override
	public String getTableName() {
		return getOriginalInfo().getName();
	}
	
	@Override
	public int getTableType() {
		String key = IJSONUtil.instance.getStringFromJSON(toJSON(),
				"Oracle_tabletype");
		if (StringUtils.isNotBlank(key)) {
			return table_type.get(Integer.parseInt(key)).getValue();
		}
		return 0;
	}

	//getTableSpace����Ǩ����IDatabaseResScriptWrap��ʵ�֣��Ա�����ͼ�������У�Ҳ����ͨ���˷�����ȡ��ռ���
	/*@Override
	public String getTableSpace(String prefix) {
		String space = getValueByJson("Oracle_space");
		if (StringUtils.isBlank(prefix)) {
			return space;
		}
		OracleSpaceResourceData dbobjectinfo = null;
		try {
			dbobjectinfo = getDBObjectInfo();
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
		if (dbobjectinfo != null) {
			if (StringUtils.equals("his_", prefix)){
				for (TableSpaceRelation relation : dbobjectinfo.getRelations()) {
					if (StringUtils.equals(relation.getMainSpace(), space)) {
						return IJSONUtil.instance.getStringFromJSON(relation.toJSON(),
						"tschouse_hisSpace");
					}
				}
			}else if (StringUtils.equals("fil_", prefix)){
				for (TableSpaceRelation relation : dbobjectinfo.getRelations()) {
					if (StringUtils.equals(relation.getMainSpace(), space)) {
						return IJSONUtil.instance.getStringFromJSON(relation.toJSON(),
						"tschouse_fileSpace");
					}
				}
			}else if (StringUtils.equals("rl_", prefix)){
				for (TableSpaceRelation relation : dbobjectinfo.getRelations()) {
					if (StringUtils.equals(relation.getMainSpace(), space)) {
						return IJSONUtil.instance.getStringFromJSON(relation.toJSON(),
						"chouse_reduTable");
					}
				}
			}else if (StringUtils.equals("sett_", prefix)){
				for (TableSpaceRelation relation : dbobjectinfo.getRelations()) {
					if (StringUtils.equals(relation.getMainSpace(), space)) {
						return IJSONUtil.instance.getStringFromJSON(relation.toJSON(),
						"chouse_chearTable");
					}
				}
			}
		}
		return space;
	}*/

	@Override
	public String getIndexTableSpace(String prefix) {
		String tableSpace = getValueByJson("Oracle_space");
		try {
			OracleSpaceResourceData dbobjectinfo = getDBObjectInfo();
			if (dbobjectinfo == null || StringUtils.isBlank(tableSpace)) {
				return StringUtils.EMPTY;
			}
			for (TableSpaceRelation relation : dbobjectinfo.getRelations()) {
				if (StringUtils.equals(relation.getMainSpace(), tableSpace)) {
					if (StringUtils.isBlank(prefix)) {
						return relation.getIndexSpace();
					}
					if (StringUtils.equals("his_", prefix)){
						return IJSONUtil.instance.getStringFromJSON(relation.toJSON(),
							"tschouse_hisIndexSpace");
					}else if (StringUtils.equals("fil_", prefix)){
						return IJSONUtil.instance.getStringFromJSON(relation.toJSON(),
							"tschouse_fileIndexSpace");
					}else if (StringUtils.equals("sett_", prefix)){
						return IJSONUtil.instance.getStringFromJSON(relation.toJSON(),
						"chouse_chearTableIndex");
					}else if (StringUtils.equals("rl_", prefix)){
						return IJSONUtil.instance.getStringFromJSON(relation.toJSON(),
						"chouse_reduTable");
					}
					return relation.getIndexSpace();
				}
			}
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}

	public void setGenHisTable(boolean isGenHisTable){
		this.isGenHisTable = isGenHisTable;
	}
	
	@Override
	public boolean isGenHisTable() {
		return isGenHisTable;
	}

	@Override
	public String getPartitionStartDate() {
		if(isPartitionByUser()) {
			return getValueByJson("chouse_startData");
		}else {
			DatabaseModuleExtensibleProperty db = getModulePartitionProperty(resource.getModule(),"DatabaseModuleExtensibleProperty");
			if (db != null) {
				return db.getStartDate();
			}
		}
		return StringUtils.EMPTY;
	}

	@Override
	public int getPartitionNum() {
		String num = "";
		if(isPartitionByUser()) {
			num  = getValueByJson("chouse_splitNum");
		}else {
			DatabaseModuleExtensibleProperty db = getModulePartitionProperty(resource.getModule(),"DatabaseModuleExtensibleProperty");
			if (db != null) {
				num = db.getSplitNum();
			}
		}
		try {
			return Integer.parseInt(num);
		} catch (Exception e) {
		}
		return 0;
	}

	@Override
	public String getCommentSql(String prefix , boolean isUser) {
		String tableName = prefix + getOriginalInfo().getName();
		if (isUser) {
			String user = "";
			user = getDbuser(prefix);
			if (StringUtils.isNotBlank(user)) {
				tableName = user + "." + tableName;
			}
		}

		StringBuffer commentBuffer = new StringBuffer();
		String desc = getOriginalInfo().getDescription();
		//�����˵��δ��д����ȥ��������
		if (StringUtils.isBlank(desc)) {
			desc = getOriginalInfo().getChineseName();
		}
		if (StringUtils.isNotBlank(desc)) {
			commentBuffer.append("comment on table " + tableName + " is '"
					+ desc + "';");
			commentBuffer.append("\r\n");
		}
		IMetadataService service = DataServiceManager.getInstance()
				.getService(resource.getARESProject(),
						IMetadataService.class);
		for (TableColumn column : getTableCols(getOriginalInfo().getColumns(), prefix)) {
			IStandardField field = service.getStandardField(column
					.getFieldName());
			if (field != null) {
				String cn = column.getComments();
				if (StringUtils.isBlank(cn)) {
					cn = field.getChineseName();
				}
				if (StringUtils.isNotBlank(cn)) {
					commentBuffer.append("comment on column " + tableName
							+ "." + column.getFieldName() + " is '" + cn
							+ "';");
					commentBuffer.append("\r\n");
				}
			}
		}
		commentBuffer.append("\r\n");
		return commentBuffer.toString();
	}

	@Override
	public String getHistoryComment(String commentMark) {
		return HistoryCommentHelper.getHistoryCommentForDatabase(getOriginalInfo(),commentMark);
	}

	public ITableRevHistoryScriptWrap[] getHistories(){
		List<ITableRevHistoryScriptWrap>	hisWraps = new ArrayList<ITableRevHistoryScriptWrap>();
		List<RevisionHistory> histories = new ArrayList<RevisionHistory>();
		
		for (RevisionHistory his : getOriginalInfo().getHistories()){
			try {
				if (HistoryCommentCompator.compareVersion(his.getVersion(), resource.getARESProject().getProjectProperty().getVersion())) {
					histories.add(his);
				}
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
		}
		//����,��ǰ����
		Collections.reverse(histories);
		Collections.sort(histories, new RevHistoryCompator());
		for (RevisionHistory his : histories){
			hisWraps.add(new TableRevHistoryScriptWrapImpl(his, resource));
		}
		return hisWraps.toArray(new ITableRevHistoryScriptWrap[0]);
	}
	
	@Override
	public ITableColScriptWrap[] getTableColumns() {
		if (columnScriptWraps == null) {
			columnScriptWraps = transTableColumn(getOriginalInfo().getColumns());
		}
		return columnScriptWraps.toArray(new ITableColScriptWrap[0]);
	}
	
	@Override
	public ITableColScriptWrap getTableColumnByName(String column_name) {
		if (columnScriptWraps == null) {
			columnScriptWraps = transTableColumn(getOriginalInfo().getColumns());
		}
		for(int i = 0;i < columnScriptWraps.size();i++){
			ITableColScriptWrap col = columnScriptWraps.get(i);
			if(col != null && (col.getName().equalsIgnoreCase(column_name))){
				return col;
			}
		}
		return null;
	}

	@Override
	public ITableIndexScriptWrap[] getTableIndexs() {
		if (indexScriptWraps == null) {
			indexScriptWraps = transTableIndex(getOriginalInfo().getIndexes());
		}
		return indexScriptWraps.toArray(new ITableIndexScriptWrap[0]);
	}
	
	@Override
	public ITableKeyScriptWrap[] getTableKeys() {
		if (keyScriptWraps == null) {
			keyScriptWraps = transTableKey(getOriginalInfo().getKeys());
		}
		return keyScriptWraps.toArray(new ITableKeyScriptWrap[0]);
	}
	
	@Override
	public TableResourceData getOriginalInfo() {
		if (table == null) {
			try {
				table = resource.getInfo(TableResourceData.class);
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
		}
		return table;
	}
	
	private String getValueByJson(String key) {
		return IJSONUtil.instance.getStringFromJSON(toJSON(), key);
	}

	@Override
	public boolean isGenReduTable() {
		String result = getValueByJson("chouse_isRedu");
		try {
			return Boolean.parseBoolean(result);
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public boolean isGenSettTable() {
		String result = getValueByJson("chouse_isClear");
		try {
			return Boolean.parseBoolean(result);
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public String getPartitionfield() {
		if(isPartitionByUser()) {
			return getValueByJson("chouse_splitField");
		}else {
			DatabaseModuleExtensibleProperty db = getModulePartitionProperty(resource.getModule(),"DatabaseModuleExtensibleProperty");
			if (db != null) {
				return db.getSplitField();
			}
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * ��ȡģ��ķ�����Ϣ
	 * @param module ģ��
	 * @param key �ֶ�
	 * @return
	 */
	private DatabaseModuleExtensibleProperty getModulePartitionProperty(IARESModule module,String key) {
		
		try {
			Stack<IARESModule> stack = new Stack<IARESModule>();
			stack.push(module);
			while (!stack.isEmpty()) {
				IARESModule m = stack.pop();
				IARESResource mr = m.getARESResource(IARESModule.MODULE_PROPERTY_FILE);
				ModuleProperty mp = mr.getInfo(ModuleProperty.class);
				Object mProperty = mp.getMap().get(key);
				if(mProperty instanceof DatabaseModuleExtensibleProperty && 
						(StringUtils.isNotBlank(((DatabaseModuleExtensibleProperty) mProperty).getSplitField())) &&
						(StringUtils.isNotBlank(((DatabaseModuleExtensibleProperty) mProperty).getSplitNum())) &&
						(StringUtils.isNotBlank(((DatabaseModuleExtensibleProperty) mProperty).getStartDate()))) {
					return (DatabaseModuleExtensibleProperty)mProperty;
				}else {//��ģ�����չ��Ϣ�п���Ϊ��
					if(m.getParentModule() instanceof IARESModule) {
						stack.push((IARESModule) m.getParentModule());
					}
				}
			}
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean isPartitionByUser() {
		String userSplit = getValueByJson("chouse_userSplit");
		try {
			return Boolean.parseBoolean(userSplit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String getStdFieldChineseName(String columnName) {
		for (TableColumn column : getOriginalInfo().getColumns()){
			String cn = "";
			if (StringUtils.isNotBlank(column.getColumnName())) {
				cn = column.getColumnName();
			}else {
				cn = column.getFieldName();
			}
			
			if (StringUtils.equals(cn, columnName)) {
				IMetadataService service = DataServiceManager.getInstance()
				.getService(resource.getARESProject(), IMetadataService.class);
				IStandardField stdField = service.getStandardField(column.getFieldName());
				if (stdField != null) {
					return stdField.getChineseName();
				}
			}
		}
		return StringUtils.EMPTY;
	}
	
	@Override
	public String getDataTypeOracle(String type) {
		IMetadataService service = DataServiceManager.getInstance()
			.getService(resource.getARESProject(), IMetadataService.class);
		IBusinessDataType dataType = service.getBusinessDataType(type);
		if (dataType != null) {
			return dataType.getRealType("oracle");
		}
	
		return type;
	}
	
	private List<ITableColScriptWrap> transTableColumn(List<TableColumn> tableColumns){
		List<ITableColScriptWrap> columnScriptWraps = new ArrayList<ITableColScriptWrap>();
		for (TableColumn column : tableColumns) {
			columnScriptWraps.add(new TableColScriptWrapImpl(column,
					resource));
		}
		return columnScriptWraps;
	}

	private List<ITableIndexScriptWrap> transTableIndex(List<TableIndex> tableIndexs){
		List<ITableIndexScriptWrap> indexScriptWraps = new ArrayList<ITableIndexScriptWrap>();
		for (TableIndex index : tableIndexs) {
			indexScriptWraps.add(new TableIndexScriptWrapImpl(this ,index,
					resource));
		}
		return indexScriptWraps;
	}
	
	private List<ITableKeyScriptWrap> transTableKey(List<TableKey> tableKeys){
		List<ITableKeyScriptWrap> keyScriptWraps = new ArrayList<ITableKeyScriptWrap>();
		for (TableKey key : tableKeys) {
			keyScriptWraps.add(new TableKeyScriptWrapImpl(key,resource));
		}
		return keyScriptWraps;
	}
	
	private TableColumn[] getTableCols(List<TableColumn> columns ,String prefix){
		List<TableColumn> reColumns = new ArrayList<TableColumn>();
		for (TableColumn column : columns) {
	
			String colFlag = column.getMark();
			if(StringUtils.equals(prefix, "") && colFlag != null && colFlag.toUpperCase().indexOf('H') >= 0) {
				//��ǰ�� ȥ����H��־���ֶ�(hһ��Ϊ��ʷ��)
				continue;
			}else if(StringUtils.equals(prefix, "cl_") && colFlag != null && colFlag.toUpperCase().indexOf('H') >= 0) {
				//��ǰ���ձ� ȥ����H��־���ֶ�(hһ��Ϊ��ʷ��)
				continue;
			}
			
			reColumns.add(column);
		}
		return reColumns.toArray(new TableColumn[0]);
	}

	public void setObjectId(String objectId){
		getOriginalInfo().setObjectId(objectId);
	}
	
	@Override
	public String getObjectId() {
		return StringUtils.defaultString(getOriginalInfo().getObjectId());
	}
	
	@Override
	public void setTableColumns(List<ITableColScriptWrap> columns) {
		this.columnScriptWraps = columns;
	}

	@Override
	public void setTableIndexs(List<ITableIndexScriptWrap> indexs) {
		this.indexScriptWraps = indexs;
	}
	
	@Override
	public void setTableKeys(List<ITableKeyScriptWrap> keys) {
		this.keyScriptWraps = keys;
		
	}
	
	public ITableScriptWrap getTableInfoByHisInfo(ITableRevHistoryScriptWrap history){
		ITableScriptWrap newInfo = null;
		IModificationScriptWrap action = history.getAction();
		if (action instanceof AddTableModificationScriptWrapImpl) {
			ITableColScriptWrap[] columns = ((AddTableModificationScriptWrapImpl)action).getTableColumns();
			ITableIndexScriptWrap[] indexes = ((AddTableModificationScriptWrapImpl)action).getTableIndexes();
			newInfo = new TableScriptWrapImpl(resource);
			newInfo.setTableColumns(Arrays.asList(columns));
			newInfo.setTableIndexs(Arrays.asList(indexes));
			newInfo.setHistory(history);
		}
		return newInfo;
	}
	
	@Override
	public void setHistory(ITableRevHistoryScriptWrap history) {
		this.history = history;
	}

	@Override
	public ITableRevHistoryScriptWrap getHistory() {
		return history;
	}
	
	/**������Լ���󣬶����ݿ����������ķ����������ط���Ӧ���õ��ⷽ��*/
	public void updateTableKeys(){
		TableResourceData info = getOriginalInfo();
		info.getKeys().clear();
		Map<String,List<TableColumn>> pkMap = new HashMap<String,List<TableColumn>>();
		Map<String,List<TableColumn>> ukMap = new HashMap<String,List<TableColumn>>();
		Map<String,List<TableColumn>> fkMap = new HashMap<String,List<TableColumn>>();
		for(TableColumn col : info.getColumns()){
			String mark = StringUtils.defaultString(col.getMark());
			if(col.isPrimaryKey()){
				if(pkMap.containsKey(mark)){
					pkMap.get(mark).add(col);
				}else{
					List<TableColumn> cols = new ArrayList<TableColumn>();
					cols.add(col);
					pkMap.put(mark, cols);
				}
			}
			if(col.isUnique()){
				if(ukMap.containsKey(mark)){
					ukMap.get(mark).add(col);
				}else{
					List<TableColumn> cols = new ArrayList<TableColumn>();
					cols.add(col);
					ukMap.put(mark, cols);
				}
			}
			if(col.getForeignkey().size()>0){
				if(fkMap.containsKey(mark)){
					fkMap.get(mark).add(col);
				}else{
					List<TableColumn> cols = new ArrayList<TableColumn>();
					cols.add(col);
					fkMap.put(mark, cols);
				}
			}
		}
		
		if(pkMap.size()>0){
			for(Entry<String, List<TableColumn>> entry : pkMap.entrySet()){
				TableKey key = DatabaseFactory.eINSTANCE.createTableKey();
				String mark = entry.getKey();
				String prefix = StringUtils.isBlank(mark) ? "" : mark + "_" ;
				key.setName(prefix + info.getName()+ "_pk");
				key.setType(key_type.PRIMARY);
				key.setMark(mark);
				for(TableColumn col : entry.getValue()){
					key.getColumns().add(col);
				}
				info.getKeys().add(key);
			}
		}
		
		if(ukMap.size()>0){
			for(Entry<String, List<TableColumn>> entry : ukMap.entrySet()){
				TableKey key = DatabaseFactory.eINSTANCE.createTableKey();
				String mark = entry.getKey();
				String prefix = StringUtils.isBlank(mark) ? "" : mark + "_"  ;
				key.setName(prefix + info.getName() + "_uk");
				key.setType(key_type.UNIQUE);
				key.setMark(mark);
				for(TableColumn col : entry.getValue()){
					key.getColumns().add(col);
				}
				info.getKeys().add(key);
			}
		}
		
		if(fkMap.size()>0){
			for(Entry<String, List<TableColumn>> entry : fkMap.entrySet()){
				TableKey key = DatabaseFactory.eINSTANCE.createTableKey();
				String mark = entry.getKey();
				String prefix = StringUtils.isBlank(mark) ? "" : mark + "_" ;
				key.setName(prefix + info.getName() + "_fk");
				key.setType(key_type.FOREIGN);
				key.setMark(mark);
				for(TableColumn col : entry.getValue()){
					key.getColumns().add(col);
					key.getForeignKey().add(col.getForeignkey().get(0));
				}
				info.getKeys().add(key);
			}
		}
		save();
	}
	
	/**
	 * �����޶���¼ģ�ͣ����ڽű��е��ã�APIδ��¶�˷�����
	 */
	public void upgradeConstraintHistory() {
		try {
			TableResourceData info = getOriginalInfo();
			List<RevisionHistory> histories = info.getHistories();
			
			// ������Щ��¼��Ҫһ����������������ѭ�����ǲ������޸�list�ģ����Ե�һ��ѭ��������Ҫ������������¼��������ݴ�����
			List<RevisionHistory> toBeUpdated = new ArrayList<RevisionHistory>();
			
			for (RevisionHistory his : histories) {
				RevisionHistoryProperty hisPro = (RevisionHistoryProperty) his.getData2().get(IStock3Constant.HISTORY_DATA2_KEY);
				Modification oldModify = hisPro.getAction();
				// �������ɾ�����֣�ֱ����ģ��ת��ģ�ͣ�Ȼ����hisPro.setAction(newModify);��������ģ����þ����ˡ�
				// ���ǣ������޸�ΨһԼ��������Ҫ������������¼���ͱ�����histories���list�������Ԫ���ˣ���ѭ���в������޸�list
				// ���Ա����ȼ�¼�����������ٽ�����������
				if (oldModify instanceof AddTableColumnPKModification) {
					AddTableColumnPKModification addPkModify = (AddTableColumnPKModification) oldModify;
					AddConstraintModification newModify = upgradeAddPKModify(addPkModify);
					hisPro.setAction(newModify);
					his.setModified(StockUtils.getModificationDescription(info, newModify));
				} else if (oldModify instanceof AddTableColumnUniqueModifycation) {
					AddTableColumnUniqueModifycation addUkModify = (AddTableColumnUniqueModifycation) oldModify;
					AddConstraintModification newModify = upgradeAddUKModify(addUkModify);
					hisPro.setAction(newModify);
					his.setModified(StockUtils.getModificationDescription(info, newModify));
				} else if (oldModify instanceof ChangeTableColumnPrimaryKeyModifycation) {
					toBeUpdated.add(his);
				} else if (oldModify instanceof ChangeTableColumnUniqueModifycation) {
					toBeUpdated.add(his);
				} else if (oldModify instanceof RemoveTableColumnPKModification) {
					RemoveConstraintModification newModify = ChouseFactory.eINSTANCE.createRemoveConstraintModification();
					ConstraintModifyDetail detail = ChouseFactory.eINSTANCE.createConstraintModifyDetail();
					detail.setName(getName() + "_pk");
					detail.setType(key_type.PRIMARY);
					newModify.getDetails().add(detail);
					hisPro.setAction(newModify);
					his.setModified(StockUtils.getModificationDescription(info, newModify));
				} else if (oldModify instanceof RemoveTableColumnUniqueModifycation) {
					RemoveConstraintModification newModify = ChouseFactory.eINSTANCE.createRemoveConstraintModification();
					ConstraintModifyDetail detail = ChouseFactory.eINSTANCE.createConstraintModifyDetail();
					detail.setName(getName() + "_uk");
					detail.setType(key_type.UNIQUE);
					newModify.getDetails().add(detail);
					hisPro.setAction(newModify);
					his.setModified(StockUtils.getModificationDescription(info, newModify));
				} //else if (oldModify instanceof Removetable)
			}
			
			// ǰ���ݴ����Ҫ������������¼�ģ���ʱֻ����������ˣ��޸��������޸�ΨһԼ��
			for (RevisionHistory his : toBeUpdated) {
				RevisionHistoryProperty hisPro = (RevisionHistoryProperty) his.getData2().get(IStock3Constant.HISTORY_DATA2_KEY);
				Modification oldModify = hisPro.getAction();
				if (oldModify instanceof ChangeTableColumnPrimaryKeyModifycation) {
					upgradeChangePKModify(his);
				} else if (oldModify instanceof ChangeTableColumnUniqueModifycation) {
					upgradeChangeUKModify(his);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			ConsoleHelper.getLogger().error("���������з�������", e);
		}
		save();
	}
	
	/**
	 * ���������������޸�ģ��;
	 * ���ص�����������µ�modify����
	 */
	private AddConstraintModification upgradeAddPKModify(AddTableColumnPKModification addPKModify) {
		TableResourceData info = getOriginalInfo();
		List<ModifyDetail> details = addPKModify.getDetails();
		List<TableColumn> columns = new ArrayList<TableColumn>();
		AddConstraintModification newModify = null;
		
		for (ModifyDetail detail : details) {
			@SuppressWarnings("deprecation")
			String col = detail.getName();
			TableColumn c = DatabaseUtil.findColumn(col, info);
			if (c != null)
				columns.add(EcoreUtil.copy(c));
		}
		
		newModify = ChouseFactory.eINSTANCE.createAddConstraintModification();
		ConstraintModifyDetail detail = ChouseFactory.eINSTANCE.createConstraintModifyDetail();
		detail.getColumns().addAll(columns);
		detail.setName(getName() + "_pk");
		detail.setType(key_type.PRIMARY);
		detail.getColumns().addAll(columns);
		
		newModify.getDetails().add(detail);
		return newModify;
	}
	
	/**
	 * �������ΨһԼ�����޸�ģ��
	 * @param addUKModify ���޸�ģ��
	 * @return �µ��޸�ģ��
	 */
	private AddConstraintModification upgradeAddUKModify(AddTableColumnUniqueModifycation addUKModify) {
		TableResourceData info = getOriginalInfo();
		List<ModifyDetail> details = addUKModify.getDetails();
		List<TableColumn> columns = new ArrayList<TableColumn>();
		AddConstraintModification newModify = null;
		for (ModifyDetail detail : details) {
			newModify = ChouseFactory.eINSTANCE.createAddConstraintModification();
			@SuppressWarnings("deprecation")
			String col = detail.getName();
			TableColumn c = DatabaseUtil.findColumn(col, info);
			if (c != null)
				columns.add(EcoreUtil.copy(c));
		}
		newModify = ChouseFactory.eINSTANCE.createAddConstraintModification();
		ConstraintModifyDetail detail = ChouseFactory.eINSTANCE.createConstraintModifyDetail();
		detail.getColumns().addAll(columns);
		detail.setName(getName() + "_uk");
		detail.setType(key_type.UNIQUE);
		detail.getColumns().addAll(columns);
		
		newModify.getDetails().add(detail);
		return newModify;
	}
	
	// �����޸��������޸ļ�¼ģ�ͣ�
	// �޸�xxԼ�����ᱻ������������¼�� ɾ��Լ����Ȼ������µ�Լ��
	// ���������Ӧ�õ������ã�ʹ��ע�⣬�����ֱ���޸Ľű������info
	private void upgradeChangePKModify(RevisionHistory his) {
		TableResourceData info = getOriginalInfo();
		List<RevisionHistory> histories = info.getHistories();
		int index = histories.indexOf(his);
		RevisionHistoryProperty property = (RevisionHistoryProperty) his.getData2().get(IStock3Constant.HISTORY_DATA2_KEY);
		// ע�����ﲻ����֤���ͺ��Ƿ�null����Ϊ����ǰ���Ѿ���֤��
		ChangeTableColumnPrimaryKeyModifycation oldModify = (ChangeTableColumnPrimaryKeyModifycation) property.getAction();
		
		// ���ϵ��޸ļ�¼�л�ȡ�ֶ���Ϣ
		List<TableColumn> pkColumns = new ArrayList<TableColumn>();
		for (CTCPMDetail oldDetail : oldModify.getDetails()) {
			if (oldDetail.isPrimarkKey()) {
				TableColumn c = DatabaseUtil.findColumn(oldDetail.getName(), info);
				if (c != null) {
					pkColumns.add(EcoreUtil.copy(c));
				}
			}
		}

		// ����һ����ɾ��Լ�����ļ�¼
		// ע�������ɵļ�¼��EcoreUtil.copy������new���������ԭ�����������Զ�������������汾���޸�ʱ�䣬�Լ�������չ���ԡ�
		// Ȼ�����ͨ��set��ʽ��ԭ�����޸���Ϣ�滻��
		RevisionHistory removeRevisionHistory = EcoreUtil.copy(his);
		RemoveConstraintModification removeModify = ChouseFactory.eINSTANCE.createRemoveConstraintModification();
		ConstraintModifyDetail detail = ChouseFactory.eINSTANCE.createConstraintModifyDetail();
		detail.setName(getName() + "_pk");
		detail.setType(key_type.PRIMARY);
		removeModify.getDetails().add(detail);
		RevisionHistoryProperty rmProperty = (RevisionHistoryProperty) removeRevisionHistory.getData2().get(IStock3Constant.HISTORY_DATA2_KEY);
		rmProperty.setAction(removeModify);
		removeRevisionHistory.setModified(StockUtils.getModificationDescription(info, removeModify));
		removeRevisionHistory.getData2().put(IStock3Constant.HISTORY_DATA2_KEY, rmProperty);
		histories.add(index + 1, removeRevisionHistory);
		
		// �����ɡ�����Լ�����ļ�¼
		AddConstraintModification addModify = ChouseFactory.eINSTANCE.createAddConstraintModification();
		ConstraintModifyDetail addPkDetail = ChouseFactory.eINSTANCE.createConstraintModifyDetail();
		addPkDetail.setName(getName() + "_pk");
		addPkDetail.setType(key_type.PRIMARY);
		addPkDetail.getColumns().addAll(pkColumns);
		addModify.getDetails().add(addPkDetail);
		property.setAction(addModify);
		his.setModified(StockUtils.getModificationDescription(info, addModify));
	}

	// �����޸�ΨһԼ�����޸ļ�¼ģ�ͣ�
	// �޸�xxԼ�����ᱻ������������¼�� ɾ��Լ����Ȼ������µ�Լ��
	// ���������Ӧ�õ������ã�ʹ��ע�⣬�����ֱ���޸Ľű������info
	private void upgradeChangeUKModify(RevisionHistory his) {
		TableResourceData info = getOriginalInfo();
		List<RevisionHistory> histories = info.getHistories();
		int index = histories.indexOf(his);
		RevisionHistoryProperty property = (RevisionHistoryProperty) his.getData2().get(IStock3Constant.HISTORY_DATA2_KEY);
		// ע�����ﲻ����֤���ͺ��Ƿ�null����Ϊ����ǰ���Ѿ���֤��
		ChangeTableColumnUniqueModifycation oldModify = (ChangeTableColumnUniqueModifycation) property.getAction();
		
		// ���ϵ��޸ļ�¼�л�ȡ�ֶ���Ϣ
		List<TableColumn> pkColumns = new ArrayList<TableColumn>();
		for (CTCUMDetail oldDetail : oldModify.getDetails()) {
			if (oldDetail.isUnique()) {
				TableColumn c = DatabaseUtil.findColumn(oldDetail.getName(), info);
				if (c != null) {
					pkColumns.add(EcoreUtil.copy(c));
				}
			}
		}

		// ����һ����ɾ��Լ�����ļ�¼
		RevisionHistory removeRevisionHistory = EcoreUtil.copy(his);
		RemoveConstraintModification removeModify = ChouseFactory.eINSTANCE.createRemoveConstraintModification();
		ConstraintModifyDetail detail = ChouseFactory.eINSTANCE.createConstraintModifyDetail();
		detail.setName(getName() + "_uk");
		detail.setType(key_type.UNIQUE);
		removeModify.getDetails().add(detail);
		RevisionHistoryProperty rmProperty = (RevisionHistoryProperty) removeRevisionHistory.getData2().get(IStock3Constant.HISTORY_DATA2_KEY);
		rmProperty.setAction(removeModify);
		removeRevisionHistory.setModified(StockUtils.getModificationDescription(info, removeModify));
		removeRevisionHistory.getData2().put(IStock3Constant.HISTORY_DATA2_KEY, rmProperty);
		histories.add(index + 1, removeRevisionHistory);
		
		// �����ɡ�����Լ�����ļ�¼
		AddConstraintModification addModify = ChouseFactory.eINSTANCE.createAddConstraintModification();
		ConstraintModifyDetail addUkDetail = ChouseFactory.eINSTANCE.createConstraintModifyDetail();
		addUkDetail.setName(getName() + "_uk");
		addUkDetail.setType(key_type.UNIQUE);
		addUkDetail.getColumns().addAll(pkColumns);
		addModify.getDetails().add(addUkDetail);
		property.setAction(addModify);
		his.setModified(StockUtils.getModificationDescription(info, addModify));
	}
	
}
