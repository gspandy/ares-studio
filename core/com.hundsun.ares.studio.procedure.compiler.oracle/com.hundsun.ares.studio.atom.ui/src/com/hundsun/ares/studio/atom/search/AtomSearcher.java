/**
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.constants.IAtomResType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.ui.search.ARESSearchQuery;
import com.hundsun.ares.studio.ui.search.IARESSarcher;

/**
 * @author liaogc
 *
 */
public class AtomSearcher implements IARESSarcher {

	public static String RES_TYPE_ATOM_FUNCTION = "CRESԭ�Ӻ���";
	public static String RES_TYPE_ATOM_SERVICE = "CRESԭ�ӷ���";
	public static String SEARCH_ITEM_NAME = "Ӣ����";
	public static String SEARCH_ITEM_CNAME = "������";
	public static String SEARCH_ITEM_PSEUDOCODE = "α����";
	public static String SEARCH_ITEM_IN_OUT_INTERNAL_VARS = "����������ڲ�����";
	public static String SEARCH_ITEM_OBJECT_ID = "�����";
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
	public void search( List<String> searchResTypes, List<String> searchItems, IARESElement[] scopes, ARESSearchQuery query) {
		Pattern searchPattern = query.getSearchPattern ();
		if ( searchResTypes.contains(RES_TYPE_ATOM_FUNCTION)||searchResTypes.contains(RES_TYPE_ATOM_SERVICE)) {
			if (scopes!=null && scopes.length> 0) {
				List<String> typeList = new ArrayList<String>();
				if(searchResTypes.contains(RES_TYPE_ATOM_FUNCTION)){
					typeList.add(IAtomResType.ATOM_FUNCTION);
				}
				if(searchResTypes.contains(RES_TYPE_ATOM_SERVICE)){
					typeList.add(IAtomResType.ATOM_SERVICE);
				}
				String[] types = typeList.toArray(new String[typeList.size()]);
				for(IARESElement scope:scopes){
					Map<AtomFunction,IARESResource> atomMap = this.getResources(types, scope);
					if(searchItems.contains(SEARCH_ITEM_NAME)||
							searchItems.contains(SEARCH_ITEM_CNAME)||
							searchItems.contains(SEARCH_ITEM_OBJECT_ID)||
							searchItems.contains(SEARCH_ITEM_PSEUDOCODE)||
							searchItems.contains(SEARCH_ITEM_IN_OUT_INTERNAL_VARS)
							){
						
						for (AtomFunction atom : atomMap.keySet()) {
							/*���Ҫ����Ӣ����*/
							if (searchItems.contains(SEARCH_ITEM_NAME)) {
								if(searchPattern.matcher(StringUtils.defaultIfEmpty(atom.getName(), "")).matches()){
									query.addMatch(atomMap.get(atom));
									continue;
								}
							}
							/*���Ҫ����������*/
							if (searchItems.contains(SEARCH_ITEM_CNAME)) {
								if(searchPattern.matcher(StringUtils.defaultIfEmpty(atom.getChineseName(), "")).matches()){
									query.addMatch(atomMap.get(atom));
									continue;
								}
							}
							/*���Ҫ���������*/
							if (searchItems.contains(SEARCH_ITEM_OBJECT_ID)) {
								if(searchPattern.matcher(StringUtils.defaultIfEmpty(atom.getObjectId(), "")).matches()){
									query.addMatch(atomMap.get(atom));
									continue;
								}
							}
							/*���Ҫ����α����*/
							if (searchItems.contains(SEARCH_ITEM_PSEUDOCODE)) {
								Matcher m = searchPattern.matcher(StringUtils.defaultIfEmpty(atom.getPseudoCode(), ""));
								if (m.find()) {
									query.addMatch(atomMap.get(atom));
									continue;
								}
							}
							
							/*���Ҫ������������ڲ�����*/
							if (searchItems.contains(SEARCH_ITEM_IN_OUT_INTERNAL_VARS)) {
								List<Parameter>  parameters = new ArrayList<Parameter>();
								parameters.addAll(atom.getInputParameters());
								parameters.addAll(atom.getOutputParameters());
								parameters.addAll(atom.getInternalVariables());
								for(Parameter parameter:parameters){
									if(searchPattern.matcher(StringUtils.defaultIfEmpty(parameter.getId(), "")).matches()){
										query.addMatch(atomMap.get(atom));
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
	private Map<AtomFunction,IARESResource> getResources(String[] types ,IARESElement scope){

		Map<AtomFunction,IARESResource> atomMap = new HashMap<AtomFunction,IARESResource>();
		
		if(scope instanceof IARESProject){
			IARESProject project = (IARESProject) scope;
			
			try {
				IARESResource[] resources = project.getResources(types);
				for(IARESResource resource:resources){
					AtomFunction atom = (AtomFunction) resource.getInfo(EObject.class);
					atomMap.put(atom, resource);
				}
			} catch (Exception e) {
				console.error("����ԭ��ʱ����:"+e.getMessage());
			}
			
		}else if(scope instanceof IARESModuleRoot){
			IARESModuleRoot moduleRoot = (IARESModuleRoot) scope;
			try {
				IARESModule[] modules = moduleRoot.getModules();
				for(IARESModule module:modules){
					
					IARESResource[] resources = module.getARESResources(types, true);;
					for(IARESResource resource:resources){
						AtomFunction atom = (AtomFunction) resource.getInfo(EObject.class);
						atomMap.put(atom, resource);
					}
				}
				
			} catch (Exception e) {
				console.error("����ԭ��ʱ����:"+e.getMessage());
			}
		}else if(scope instanceof IARESModule){

			IARESModule module = (IARESModule) scope;
			try {
					IARESResource[] resources = module.getARESResources(types, true);;
					for(IARESResource resource:resources){
						AtomFunction atom = (AtomFunction) resource.getInfo(EObject.class);
						atomMap.put(atom, resource);
					}
				
			} catch (Exception e) {
				console.error("����ԭ��ʱ����:"+e.getMessage());
			}
		
		} else if (scope instanceof IARESResource) {

			IARESResource resource = (IARESResource) scope;
			try {
				EObject eobject = resource.getInfo(EObject.class);
				if(eobject instanceof AtomFunction){
					AtomFunction atom = (AtomFunction) eobject;
					atomMap.put(atom, resource);
				}
				
			

			} catch (Exception e) {
				console.error("����ԭ��ʱ����:"+e.getMessage());
			}

		}
	
		return atomMap;

	}
}

