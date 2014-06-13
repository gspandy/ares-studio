/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.model.reference.impl.ReferenceForBuilderTable;
import com.hundsun.ares.studio.model.reference.impl.ReferencesForBuilderCollection;

/**
 * @author liaogc
 * ��Դ����ʱӰ������ü���(Ŀǰֻ�Ա�׼�ֶ������ù���)builer
 */
public class ReferenceForBuilderManager {
	
	private ReferenceForBuilderManager() {
	}
	private static ReferenceForBuilderManager instance;
	private ReferenceForBuilderTable referenceForBuilderTable = new ReferenceForBuilderTable();
	
	/**
	 * ��ȡ���ù��������
	 * @return
	 */
	public synchronized static ReferenceForBuilderManager getInstance() {
		if (instance == null) {
			instance = new ReferenceForBuilderManager();
		}
		return instance;
	}
	
	public Map<IARESProject, ReferencesForBuilderCollection> getProjects(){
		return referenceForBuilderTable.getProjects();
	}
	
	/**
	 * ���ظ���resource��Ӱ������ü���
	 * @return
	 */
	public List<ReferenceInfo> getReferencesByResource(IARESResource resource){
		if( referenceForBuilderTable.getProjects().get(resource.getARESProject())==null){
			 referenceForBuilderTable.getProjects().put(resource.getARESProject(), new ReferencesForBuilderCollection());
		}
		return referenceForBuilderTable.getProjects().get(resource.getARESProject()).getReferencesByResource(resource);
	}
	
	/**
	 * 
	 * ���Ӹ���resource��Ӱ������ü���
	 */
	public void addReferences(IARESResource resource,List<ReferenceInfo> references){
		if( referenceForBuilderTable.getProjects().get(resource.getARESProject())==null){
			 referenceForBuilderTable.getProjects().put(resource.getARESProject(), new ReferencesForBuilderCollection());
		}
		 referenceForBuilderTable.getProjects().get(resource.getARESProject()).getReferences().remove(resource);
		 referenceForBuilderTable.getProjects().get(resource.getARESProject()).getReferences().put(resource, references);
	}
	
	/**
	 * 
	 * ɾ��resource��Ӱ������ü���
	 */
	public void clearReferences(IARESResource resource){
		if( referenceForBuilderTable.getProjects().get(resource.getARESProject())==null){
			 referenceForBuilderTable.getProjects().put(resource.getARESProject(), new ReferencesForBuilderCollection());
		}
		 referenceForBuilderTable.getProjects().get(resource.getARESProject()).getReferences().remove(resource);
	}
	
	
}
