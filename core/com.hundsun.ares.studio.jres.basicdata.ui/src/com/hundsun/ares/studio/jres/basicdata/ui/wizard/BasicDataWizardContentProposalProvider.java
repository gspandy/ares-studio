/**
 * Դ�������ƣ�BasicDataWizardContentProposalProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.basicdata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.basicdata.ui.wizard;

import java.util.List;

import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.cellEditor.AresContentProposalProvider;
import com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper;

/**
 * 
 * @author sundl
 *
 */
public class BasicDataWizardContentProposalProvider extends AresContentProposalProvider {

	public BasicDataWizardContentProposalProvider(IContentProposalProviderHelper helper) {
		super(helper);
	}
	
	public void updateContent(Object element) {
		if (element instanceof NewBasicDataWizard) {
			NewBasicDataWizard wizard = (NewBasicDataWizard) element;
			String resType = wizard.modeDfine.inputType;
			ReferenceManager manager = ReferenceManager.getInstance();
			List<ReferenceInfo> refList = manager.getReferenceInfos(wizard.getProject(), resType, true);
			setInput(refList.toArray());
		}
	};
	
}
