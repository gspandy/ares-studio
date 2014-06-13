/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.model.reference.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;

/**
 * @author liaogc
 * ��Դ����ʱӰ������õļ���
 */
public class ReferencesForBuilderCollection {
	private Map<IARESResource, List> references = new HashMap<IARESResource,List>();
	
	public ReferencesForBuilderCollection(){
	}
	/**
	 * �������е���Դ�ĸ�����Դʱ����Դ�仯����������
	 */
	public Map<IARESResource, List> getReferences(){
		return this.references;
	}
	
	/**
	 * ���ظ���resource��Ӱ������ü���
	 * @return
	 */
	public List<ReferenceInfo> getReferencesByResource(IARESResource resource){
		return references.get(resource);
	}

}
