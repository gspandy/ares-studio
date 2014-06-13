/**
 * Դ�������ƣ�MetadataContentProposalHelper.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.fieldassist.IContentProposal;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.context.INamespaceHelper;
import com.hundsun.ares.studio.core.context.statistic.IResourceTable;
import com.hundsun.ares.studio.internal.core.ArchiveARESResource;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.ui.assist.JRESContentPorposalHelper;
import com.hundsun.ares.studio.ui.cellEditor.ARESContentProposal;
import com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper;

/**
 * Ԫ�������õ�������ʾ
 */
// 2012-2-23 sundl �ӻ���̳У���ʵ��
public class MetadataContentProposalHelper extends JRESContentPorposalHelper implements IContentProposalProviderHelper{

	/**
	 * �ظ�MAP
	 */
	protected List< String> duplicateList = new ArrayList<String>();
	
	protected IARESProject project;
	public MetadataContentProposalHelper(IARESProject project) {
		this.project = project;
	}
	
	public void initDuplicateList(Object[] input){
		//��ʼ���ظ��б�
		this.duplicateList.clear();
		List< String> tlist = new ArrayList<String>();
		for(Object element:input){
			if(element instanceof MetadataItem){
				MetadataItem item = (MetadataItem) element;
				if(tlist.contains(item.getName())){
					if(!duplicateList.contains(item.getName())){
						duplicateList.add(item.getName());
					}
				}else{
					tlist.add(item.getName());
				}
			}
		}
		tlist.clear();
	}
	
	protected  String getContent(MetadataItem item, IARESResource resource) {
		if(duplicateList.contains(item.getName())){
			String nameSpace = "";
			if (resource != null){
				if(resource.getARESProject().equals(project)){
					if(resource instanceof ArchiveARESResource){
						//������Դ��
						nameSpace = INamespaceHelper.INSTANCE.getResourceNamespace(resource);
					}else{
						//��ǰ����
						nameSpace = "";
					}
				}else{
					//���ù���
					nameSpace = INamespaceHelper.INSTANCE.getResourceNamespace(resource);
				}
			}

			if(!StringUtils.isEmpty(nameSpace)){
				return String.format("%s.%s", nameSpace,item.getName());
			}
		}
		return item.getName();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.contentassist.JRESContentPorposalHelper#getProposal(java.lang.String, int, org.eclipse.emf.ecore.EObject, com.hundsun.ares.studio.core.IARESResource)
	 */
	@Override
	protected IContentProposal getProposal(String contents, int position,
			EObject item, IARESResource resource) {
		if (item instanceof MetadataItem) {
			MetadataItem mdItem = (MetadataItem) item;
			String content = getContent(mdItem, resource);
			// 2012-04-27 sundl ���Ϊ�ղ�������ʾ��Ŀ
			if (StringUtils.isEmpty(content))
				return null;
			if (!content.toUpperCase().contains(contents.toUpperCase())) {
				return null;
			}
			
			String shortDesc = mdItem.getChineseName();
			return new ARESContentProposal(content, shortDesc);
		}
		return null;
	}
	
}
