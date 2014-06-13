/**
 * Դ�������ƣ�DatabaseSearcher.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseResType;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableIndex;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.ui.search.ARESSearchQuery;
import com.hundsun.ares.studio.ui.search.IARESSarcher;

/**
 * @author liaogc
 * 
 */
public class TableSearcher implements IARESSarcher {

	public static String RES_TYPE_TABLE = "���ݿ��";
	public static String SEARCH_ITEM_NAME = "Ӣ����";
	public static String SEARCH_ITEM_CAME = "������";
	public static String SEARCH_ITEM_OBJECT_ID = "�����";
	public static String SEARCH_ITEM_FIELD_NAME = "���ֶ�";
	public static String SEARCH_ITEM_INDEX_NAME = "������";
	static final Logger console = ConsoleHelper.getLogger();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hundsun.ares.studio.ui.search.IARESSarcher#search(java.lang.String,
	 * boolean, java.util.List, java.util.List,
	 * com.hundsun.ares.studio.core.IARESElement)
	 */
	@Override
	public void search(List<String> searchResTypes, List<String> searchItems, IARESElement[] scopes, ARESSearchQuery query) {
		Pattern searchPattern = query.getSearchPattern ();
		if (searchResTypes.contains(RES_TYPE_TABLE)) {
			if (scopes!=null && scopes.length> 0 ) {
				for(IARESElement scope:scopes){
					Map<TableResourceData,IARESResource> tableMap = this.getResources(new String[]{IDatabaseResType.Table}, scope);
					if(searchItems.contains(SEARCH_ITEM_NAME)||
							searchItems.contains(SEARCH_ITEM_CAME)||
							searchItems.contains(SEARCH_ITEM_OBJECT_ID)||
							searchItems.contains(SEARCH_ITEM_FIELD_NAME)||
							searchItems.contains(SEARCH_ITEM_INDEX_NAME)
							){
						
						for (TableResourceData table : tableMap.keySet()) {
							/*���Ҫ������Ӣ����*/
							if (searchItems.contains(SEARCH_ITEM_NAME)) {
								if(searchPattern.matcher(StringUtils.defaultIfEmpty(table.getName(), "")).matches()){
									query.addMatch(tableMap.get(table));
									continue;
								}
							}
							/*���Ҫ������������*/
							if (searchItems.contains(SEARCH_ITEM_CAME)) {
								if(searchPattern.matcher(StringUtils.defaultIfEmpty(table.getChineseName(), "")).matches()){
									query.addMatch(tableMap.get(table));
									continue;
								}
							}
							/*���Ҫ����������*/
							if (searchItems.contains(SEARCH_ITEM_OBJECT_ID)) {
								if(searchPattern.matcher(StringUtils.defaultIfEmpty(table.getObjectId(), "")).matches()){
									query.addMatch(tableMap.get(table));
									continue;
								}
							}
							/*���Ҫ�������ֶ���*/
							if (searchItems.contains(SEARCH_ITEM_FIELD_NAME)) {
								EList<TableColumn> columns = table.getColumns();
								boolean isFound = false;
								for(TableColumn column:columns){
									if(searchPattern.matcher(StringUtils.defaultIfEmpty(column.getFieldName(), "")).matches()){
										query.addMatch(tableMap.get(table));
										isFound = true;
										break;
									}	
								}
								if(isFound){
									continue;
								}
								
							}
							
							/*���Ҫ������������*/
							if (searchItems.contains(SEARCH_ITEM_INDEX_NAME)) {
								EList<TableIndex>indexs = table.getIndexes();
								boolean isFound = false;
								for(TableIndex index:indexs){
									if(searchPattern.matcher(StringUtils.defaultIfEmpty(index.getName(), "")).matches()){
										query.addMatch(tableMap.get(table));
										isFound = true;
										break;
									}	
								}
								if(isFound){
									continue;
								}
								
							}
							
						}
					}
					
					

				}
			}
			
		}

	}
	/**
	 * �����Դ
	 * @param types
	 * @param scope
	 * @return
	 */
	private Map<TableResourceData,IARESResource> getResources(String[] types ,IARESElement scope){

		Map<TableResourceData,IARESResource> tableMap = new HashMap<TableResourceData,IARESResource>();
		
		if(scope instanceof IARESProject){
			IARESProject project = (IARESProject) scope;
			
			try {
				IARESResource[] resources = project.getResources(types);
				for(IARESResource resource:resources){
					TableResourceData table = (TableResourceData) resource.getInfo(EObject.class);
					tableMap.put(table, resource);
				}
			} catch (Exception e) {
				console.error("�������ݿ��ʱ����:"+e.getMessage());
			}
			
		}else if(scope instanceof IARESModuleRoot){
			IARESModuleRoot moduleRoot = (IARESModuleRoot) scope;
			try {
				IARESModule[] modules = moduleRoot.getModules();
				for(IARESModule module:modules){
					
					IARESResource[] resources = module.getARESResources(types, true);;
					for(IARESResource resource:resources){
						TableResourceData table = (TableResourceData) resource.getInfo(EObject.class);
						tableMap.put(table, resource);
					}
				}
				
			} catch (Exception e) {
				console.error("�������ݿ��ʱ����:"+e.getMessage());
			}
		}else if(scope instanceof IARESModule){

			IARESModule module = (IARESModule) scope;
			try {
					IARESResource[] resources = module.getARESResources(types, true);;
					for(IARESResource resource:resources){
						TableResourceData table = (TableResourceData) resource.getInfo(EObject.class);
						tableMap.put(table, resource);
					}
				
			} catch (Exception e) {
				console.error("�������ݿ��ʱ����:"+e.getMessage());
			}
		
		} else if (scope instanceof IARESResource) {

			IARESResource resource = (IARESResource) scope;
			try {
				EObject eobject = resource.getInfo(EObject.class);
				if(eobject instanceof TableResourceData){
					TableResourceData table = (TableResourceData) eobject;
					tableMap.put(table, resource);
				}

			} catch (Exception e) {
				console.error("�������ݿ��ʱ����:"+e.getMessage());
			}

		}
	
		return tableMap;

	}
}
