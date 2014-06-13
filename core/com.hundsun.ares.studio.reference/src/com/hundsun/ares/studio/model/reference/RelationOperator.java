/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.model.reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.reference.ViewerUtils;

/**
 * @author liaogc
 * ������ù�ϵһϵ�з�������
 *
 */
public class RelationOperator {
	
	private RelationRefTypesMapEntry entry = null;
	public RelationOperator(RelationRefTypesMapEntry entry){
		this.entry = entry;
	}

	/**
	 * ����refType,resName�Լ�refNamespace��ù�ϵ�б�
	 * @param refType
	 * @param refName
	 * @param refNamespace
	 * @return
	 */
	public EList<RelationInfo> getRelationInfos(String refType, String refName, String refNamespace){
		EList<RelationInfo> result = new BasicEList<RelationInfo>();
		RelationRefNamesMapEntry refNamesMapEntry = entry.getRelations().get(refType);
		if (refNamesMapEntry != null){
			List<RelationInfo> relationInfoList =refNamesMapEntry.getRelationInfoByRefName(refName);
			if(StringUtils.isNotBlank(refNamespace)){
				for(RelationInfo relationInfo:relationInfoList){
					if(StringUtils.equals(relationInfo.getUsedRefNamespace(), refNamespace)){
						result.add(relationInfo);
					}
				}
			}else{
				result.addAll(refNamesMapEntry.getRelationInfoByRefName(refName));
			}
			
			
		}
		 return result;
	}
	/**
	 * ����refType�Լ�resName��ù�ϵ�б�
	 * @param refType
	 * @param refName
	 * @return
	 */
	public EList<RelationInfo> getRelationInfos(String refType, String refName){
		EList<RelationInfo> result = new BasicEList<RelationInfo>();
		 result.addAll(entry.getRelationInfosByRefTypeAndRefName(refType, refName));
		 return result;
	}
	/**
	 * ����Դɾ��ʱ�޸����ù�ϵ
	 * @param resource
	 */
	public void removeRelationsByResource(IARESResource resource){
		//�����ٶȿ��ܸ���
		List<RelationInfo> allRelationList = ViewerUtils.getRelationInfos(resource, Collections.emptyMap());
		ListMultimap<String,RelationInfo> allRelationMap = getRelationCategoryByRefType(allRelationList);
		for(String refType:allRelationMap.keySet()){
			for(RelationInfo relationInfo:allRelationMap.get(refType)){
				RelationRefNamesMapEntry refNamesMapEntry = entry.getRelations().get(refType);
				if(refNamesMapEntry!=null){
				  refNamesMapEntry.getRelationInfoByRefName(relationInfo.getUsedRefName()).remove(relationInfo);
				}
			}
		}
	}

	/**
	 * ����Դ�޸�ʱ�������ù�ϵ
	 * 
	 * @param resource
	 */
	public void updateRelationsByResourceChange(IARESResource resource,List<RelationInfo> oldElements, List<RelationInfo> newElements) {
		ListMultimap<String,RelationInfo> oldMap = getRelationCategoryByRefType(oldElements);
		ListMultimap<String,RelationInfo> newMap = getRelationCategoryByRefType(newElements);
		//���������ù�ϵɾ���ɵĹ�ϵ
		for(String refType:oldMap.keySet()){
			for(RelationInfo oldRelationInfo:oldMap.get(refType)){
				RelationRefNamesMapEntry refNamesMapEntry = entry.getRelations().get(refType);
				if(refNamesMapEntry!=null){
				  refNamesMapEntry.getRelationInfoByRefName(oldRelationInfo.getUsedRefName()).remove(oldRelationInfo);
				}
			}
		}
		//�����µĹ�ϵ
		for(String refType:newMap.keySet()){
			for(RelationInfo newRelationInfo:newMap.get(refType)){
				RelationRefNamesMapEntry refNamesMapEntry = entry.getRelations().get(refType);
				if(refNamesMapEntry!=null){
				   refNamesMapEntry.addRelation(newRelationInfo);
				}else{
					RelationRefNamesMapEntry newRefTypeMapEntry = new RelationRefNamesMapEntry();
					newRefTypeMapEntry.addRelation(newRelationInfo);
					entry.getRelations().put(newRelationInfo.getUsedRefType(), newRefTypeMapEntry);
				}
			}
		
		}
		
		
	}
	/**
	 * ������еĹ�ϵ
	 * @return
	 */
	public List<RelationInfo> getAllRelation(){
		List<RelationInfo> allRelationInfo = new ArrayList<RelationInfo>();
		for(String refType:entry.getRelations().keySet()){
			RelationRefNamesMapEntry refNamesMapEntry  =  entry.getRelations().get(refType);
			if(refNamesMapEntry!=null){
				allRelationInfo.addAll(refNamesMapEntry.values());
			}
			
		}
		return allRelationInfo;
	}
	
	/**
	 * ��ʼ�����ù�ϵ
	 * @param relationInfoList
	 */
	public void initProjectRelation( List<RelationInfo>  relationInfoList ){
		ListMultimap<String,RelationInfo> relationInfoMap = getRelationCategoryByRefType(relationInfoList);
		for(String refType:relationInfoMap.keySet()){
			RelationRefNamesMapEntry refNamesMapEntry  =  entry.getRelations().get(refType);
			if(refNamesMapEntry!=null){
				for(RelationInfo relationInfo:relationInfoMap.get(refType)){
					refNamesMapEntry.addRelation(relationInfo);
				}
			}else{
				RelationRefNamesMapEntry newRefNamesMapEntry = new RelationRefNamesMapEntry();
				for(RelationInfo relationInfo:relationInfoMap.get(refType)){
					newRefNamesMapEntry.addRelation(relationInfo);
				}
				entry.getRelations().put(refType, newRefNamesMapEntry);
			}
		}
		
	}
	
	/**
	 * ���ݹ�ϵ���ͷ���
	 * @param relationInfoList
	 * @return
	 */
	private ListMultimap<String,RelationInfo> getRelationCategoryByRefType(List<RelationInfo> relationInfoList){
		ListMultimap<String,RelationInfo> relationInfoMap =  ArrayListMultimap.create() ;
		for(RelationInfo relationInfo:relationInfoList){
			relationInfoMap.get(relationInfo.getUsedRefType()).add(relationInfo);
			
		}
		return relationInfoMap;
	}
	
}
