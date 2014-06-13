/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.script.util.IScriptReferenceUtil;
import com.hundsun.ares.studio.model.reference.RelationInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * @author liaogc
 *
 */
public class ScriptReferenceUtilImpl implements IScriptReferenceUtil {
	public static ScriptReferenceUtilImpl instance = new ScriptReferenceUtilImpl();

	/**
	 * ������������,���������Լ���Ŀ���ƻ�����ô����õ���Դ�б�
	 * @param refType
	 * @param refName
	 * @param project
	 * @return
	 */
	public Collection<IARESResource> getRefResources(String refType,String refName,IARESProject project) {
		Set<IARESResource> result = new HashSet<IARESResource>();
		// �����Ǹ��ݵ�ǰ�����½��еģ��޷���֪֮ǰ�����ù�ϵ
          try{
        	  List<RelationInfo>  rels = ReferenceManager.getInstance().getRelationInfoByTarget(refType, refName, project);
  			for (RelationInfo rel : rels) {
  				result.add(rel.getHostResource());
  			} 
          }catch(Exception e){
        	  
          }
			
		
		return result;
	}

}
