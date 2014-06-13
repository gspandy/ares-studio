/**
 * Դ�������ƣ�ServiceSearcher.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.service.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�liaogc
 */
package com.hundsun.ares.studio.jres.service.ui.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.service.Service;
import com.hundsun.ares.studio.ui.search.ARESSearchQuery;
import com.hundsun.ares.studio.ui.search.IARESSarcher;

/**
 * @author liaogc
 *
 */
public class ServiceSearcher implements IARESSarcher {

	public static String RES_TYPE_SERVICE = "����";
	public static String SEARCH_ITEM_NAME = "Ӣ����";
	public static String SEARCH_ITEM_CAME = "������";
	public static String SEARCH_ITEM_OBJECT_ID = "�����";
	public static String SEARCH_ITEM_IN_OUT_VARS = "�����������";
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
		if ( searchResTypes.contains(RES_TYPE_SERVICE)) {
			if (scopes!=null && scopes.length> 0 ) {
				for(IARESElement scope:scopes){
					Map<Service,IARESResource> serviceMap = this.getResources(new String[]{"service"}, scope);
					if(searchItems.contains(SEARCH_ITEM_NAME)||
							searchItems.contains(SEARCH_ITEM_CAME)||
							searchItems.contains(SEARCH_ITEM_OBJECT_ID)||
							searchItems.contains(SEARCH_ITEM_IN_OUT_VARS)
							){
						
						for (Service service : serviceMap.keySet()) {
							/*���Ҫ����Ӣ����*/
							if (searchItems.contains(SEARCH_ITEM_NAME)) {
								if(searchPattern.matcher(StringUtils.defaultIfEmpty(service.getName(), "")).matches()){
									query.addMatch(serviceMap.get(service));
									continue;
								}
							}
							/*���Ҫ����������*/
							if (searchItems.contains(SEARCH_ITEM_CAME)) {
								if(searchPattern.matcher(StringUtils.defaultIfEmpty(service.getChineseName(), "")).matches()){
									query.addMatch(serviceMap.get(service));
									continue;
								}
							}
							/*���Ҫ���������*/
							if (searchItems.contains(SEARCH_ITEM_OBJECT_ID)) {
								if(searchPattern.matcher(StringUtils.defaultIfEmpty(service.getObjectId(), "")).matches()){
									query.addMatch(serviceMap.get(service));
									continue;
								}
							}
							
							/*���Ҫ������������ڲ�����*/
							if (searchItems.contains(SEARCH_ITEM_IN_OUT_VARS)) {
								List<Parameter>  parameters = new ArrayList<Parameter>();
								parameters.addAll(service.getInterface().getInputParameters());
								parameters.addAll(service.getInterface().getInputParameters());
								for(Parameter parameter:parameters){
									if(searchPattern.matcher(StringUtils.defaultIfEmpty(parameter.getId(), "")).matches()){
										query.addMatch(serviceMap.get(service));
										break;
									}
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
	private Map<Service,IARESResource> getResources(String[] types ,IARESElement scope){

		Map<Service,IARESResource> serviceMap = new HashMap<Service,IARESResource>();
		
		if(scope instanceof IARESProject){
			IARESProject project = (IARESProject) scope;
			
			try {
				IARESResource[] resources = project.getResources(types);
				for(IARESResource resource:resources){
					Service service = (Service) resource.getInfo(EObject.class);
					serviceMap.put(service, resource);
				}
			} catch (Exception e) {
				console.error("��������ʱ����:"+e.getMessage());
			}
			
		}else if(scope instanceof IARESModuleRoot){
			IARESModuleRoot moduleRoot = (IARESModuleRoot) scope;
			try {
				IARESModule[] modules = moduleRoot.getModules();
				for(IARESModule module:modules){
					
					IARESResource[] resources = module.getARESResources(types, true);;
					for(IARESResource resource:resources){
						Service service = (Service) resource.getInfo(EObject.class);
						serviceMap.put(service, resource);
					}
				}
				
			}  catch (Exception e) {
				console.error("��������ʱ����:"+e.getMessage());
			}
		}else if(scope instanceof IARESModule){

			IARESModule module = (IARESModule) scope;
			try {
					IARESResource[] resources = module.getARESResources(types, true);;
					for(IARESResource resource:resources){
						Service service = (Service) resource.getInfo(EObject.class);
						serviceMap.put(service, resource);
					}
				
			}  catch (Exception e) {
				console.error("��������ʱ����:"+e.getMessage());
			}
		
		} else if (scope instanceof IARESResource) {

			IARESResource resource = (IARESResource) scope;
			try {
				EObject eobject = resource.getInfo(EObject.class);
				if(eobject instanceof Service){
					Service service = (Service) eobject;
					serviceMap.put(service, resource);
				}
			}  catch (Exception e) {
				console.error("��������ʱ����:"+e.getMessage());
			}

		}
	
		return serviceMap;

	}
}
