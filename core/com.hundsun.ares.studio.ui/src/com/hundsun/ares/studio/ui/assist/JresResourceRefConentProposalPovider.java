/**
 * Դ�������ƣ�JresResourceRefConentProposalPovider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.assist;

import java.util.ArrayList;
import java.util.List;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.cellEditor.AresContentProposalProvider;
import com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper;

/**
 * Jres����Դ����ʹ�õ�ContentProposalProvider�� ���ʵ��ʹ��IResStatisticProvider��ȡ��ʾ���ݡ�
 * @author sundl
 */
public abstract class JresResourceRefConentProposalPovider extends AresContentProposalProvider{

	protected String resType;
	protected IARESProject project;
	
	/**
	 * @param helper
	 * @param resType ��Ҫ��ʾ����Դ����
	 * @param project
	 */
	public JresResourceRefConentProposalPovider(IContentProposalProviderHelper helper, String resType, IARESProject project) {
		super(helper);
		this.resType = resType;
		this.project = project;
	}
	
	@Override
	public void updateContent(Object element) {
		List<ReferenceInfo> infoList = ReferenceManager.getInstance().getReferenceInfos(project, resType, true);
		// ��ΪcreateProposal()�ò���element��������ʾԪ�صĹ��˱���������ʹ����
		List<Object> inputItems = new ArrayList<Object>();
		if (infoList != null) {
			for (ReferenceInfo inputItem : infoList) {
				if (filter(inputItem, element))
					inputItems.add(inputItem);
			}
		}
		
		setInput(inputItems.toArray());
	}
	
	/**
	 * ָ����inputItem�Ƿ�Ӧ�ó�������ʾ�б���; 
	 * @param inputItem
	 * @param element һ���Ǳ�/���е�ǰѡ�е���һ��
	 * @return
	 */
	protected abstract boolean filter(Object inputItem, Object element);

}
