/**
 * Դ�������ƣ�SequenceAssistantProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.basicdata.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.assist.IAssistantProvider;

/**
 * @author yanwj06282
 *
 */
public class NewBasicDataWizardAssistantProvider implements IAssistantProvider {

	NewBasicDataWizard wizard;
	public NewBasicDataWizardAssistantProvider(NewBasicDataWizard wizard) {
		this.wizard = wizard;
	}
	
	@Override
	public Object[] getProposals() {
		List<Object> inputItems = new ArrayList<Object>();
		ReferenceManager manager = ReferenceManager.getInstance();
		List<ReferenceInfo> refList = manager.getReferenceInfos(wizard.getProject(), wizard.modeDfine.inputType, true);
		for (ReferenceInfo ref : refList) {
			inputItems.add(ref.getResource());
		}
		return inputItems.toArray();
	}

	@Override
	public String getContent(Object obj) {
		if (obj instanceof IARESResource) {
			return ((IARESResource) obj).getName();
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
