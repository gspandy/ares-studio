/**
 * Դ�������ƣ�ErrnoContentProposalHelper.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.ui.editor.page;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.ui.assist.JresResourceRefConentProposalPovider;
import com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper;

/**
 * @author sundl
 *
 */
public class ErrnoContentProposalProvider extends JresResourceRefConentProposalPovider{

	/**
	 * @param helper
	 * @param resType
	 * @param project
	 */
	public ErrnoContentProposalProvider(IContentProposalProviderHelper helper, String resType, IARESProject project) {
		super(helper, resType, project);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.assist.JresResourceRefConentProposalPovider#filter(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected boolean filter(Object inputItem, Object element) {
		return true;
	}

}
