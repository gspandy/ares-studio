/**
 * Դ�������ƣ�MetadataContentProposalProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.context.statistic.IResourceTable;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.assist.JresResourceRefConentProposalPovider;
import com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper;

/**
 * ��ʾԪ�������õ�ʱ��ʹ�õ�ProposalContentProvider
 * @author sundl
 */
public class MetadataContentProposalProvider extends JresResourceRefConentProposalPovider {

	public MetadataContentProposalProvider(IContentProposalProviderHelper helper, String resType, IARESProject project) {
		super(helper, resType, project);
	}

	@Override
	protected boolean filter(Object inputItem, Object element) {
		if (inputItem instanceof Map && element instanceof MetadataItem) {
			@SuppressWarnings("rawtypes")
			Map map = (Map) inputItem;
			IARESResource res = (IARESResource) map.get(IResourceTable.TARGET_RESOURCE);
			MetadataItem item = (MetadataItem) map.get(IResourceTable.TARGET_OWNER);
			MetadataItem elementItem = (MetadataItem) element;
			if (res.getARESProject().equals(project) && item.getName().equals(elementItem.getName()) 
					&& item.getParent().getFullyQualifiedName().equals(elementItem.getParent().getFullyQualifiedName())) { //ͬ��Դ����Ҫ����
				return false;
			}
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.contentassist.JresResourceRefConentProposalPovider#updateContent(java.lang.Object)
	 */
	@Override
	public void updateContent(Object element) {
		List<ReferenceInfo> referenceInfoList =ReferenceManager.getInstance().getReferenceInfos(project,resType,true);
		if (referenceInfoList==null || referenceInfoList.size()==0) {
			setInput(new Object[0]);
			
		} else{
            // ���˵��ظ��ı�׼�ֶζ���
			
			Set<String> errorSet = new HashSet<String>();
			Set<String> processedSet = new HashSet<String>();
			Map<String, Object> processedObjects = new HashMap<String, Object>();
			for (ReferenceInfo refInfo : referenceInfoList) {
				Object object =refInfo.getObject();
				if (object instanceof MetadataItem) {
					MetadataItem item = (MetadataItem) object;
					if (!errorSet.contains(item.getName())) {
						if (processedSet.contains(item.getName())) {
							processedSet.remove(item.getName());
							errorSet.add(item.getName());
							processedObjects.remove(item.getName());
						} else {
							processedSet.add(item.getName());
							processedObjects.put(item.getName(), refInfo);
						}
					}
				}
			
			}
			// ��ΪcreateProposal()�ò���element��������ʾԪ�صĹ��˱���������ʹ����
			List<Object> inputItems = new ArrayList<Object>();
			for (Object inputItem : processedObjects.values().toArray()) {
				if (filter(inputItem, element))
					inputItems.add(inputItem);
			}
			
			setInput(inputItems.toArray());
		}
		
		
		if(helper instanceof MetadataContentProposalHelper){
			((MetadataContentProposalHelper)helper).initDuplicateList(input);
		}
	}

}
