/**
 * Դ�������ƣ�SequenceAssistantProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.assist;

import java.util.ArrayList;
import java.util.List;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * @author yanwj06282
 *
 */
public class IARESResourceAssistantProvider implements IAssistantProvider {

	private IARESProject project;
	private String resType;
	
	public IARESResourceAssistantProvider(IARESProject project , String refType) {
		this.project = project;
		this.resType = refType;
	}
	
	@Override
	public Object[] getProposals() {
		List<Object> inputItems = new ArrayList<Object>();
		ReferenceManager manager = ReferenceManager.getInstance();
		List<ReferenceInfo> refList = manager.getReferenceInfos(project, resType, true);
		for (ReferenceInfo inputItem : refList) {
				IARESResource res = inputItem.getResource();
				if (res != null) {
					inputItems.add(res);
				}
		}
		return inputItems.toArray();
	}

	@Override
	public String getContent(Object obj) {
		if (obj instanceof IARESResource) {
			return ((IARESResource) obj).getFullyQualifiedName();
		}
		return null;
	}

	@Override
	public String getDescription(Object obj) {
		return null;
	}

	@Override
	public String getLabel(Object obj) {
		return getContent(obj);
	}

}
