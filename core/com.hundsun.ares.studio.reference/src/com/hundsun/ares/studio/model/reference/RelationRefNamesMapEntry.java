/**
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.model.reference;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;


/**
 * @author liaogc
 * 
 */
public class RelationRefNamesMapEntry {
	
	private ListMultimap<String,RelationInfo> relationRefNameMap =  ArrayListMultimap.create() ;
	public RelationRefNamesMapEntry() {
	}
	/**
	 * ���һ�������µ�������ͬ����������
	 * @return
	 */
	public List<String> getRefNames(){
		return new ArrayList<String>(this.relationRefNameMap.keySet());
	}
	/**
	 * �����������Ʒ�����ͬ���û���������ϵ�б�
	 * @param refName
	 * @return
	 */
	public List<RelationInfo> getRelationInfoByRefName(String refName){
		return relationRefNameMap.get(refName);
	}
	/**
	 * �����ϵ
	 * @param relationInfo
	 */
	public void addRelation(RelationInfo relationInfo){
		relationRefNameMap.put(relationInfo.getUsedRefName(), relationInfo);
	}
	
	/**
	 * ����ͬһrefType���еĹ�ϵ
	 * @return
	 */
	public List<RelationInfo> values(){
		List<RelationInfo> values = new ArrayList<RelationInfo>();
		for(String refName:this.relationRefNameMap.keySet()){
			values.addAll(relationRefNameMap.get(refName));
		}
		return values;
	}
	

}
