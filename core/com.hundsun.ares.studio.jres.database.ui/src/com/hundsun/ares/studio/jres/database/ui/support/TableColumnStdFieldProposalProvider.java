/**
 * Դ�������ƣ�TableColumnStdFieldProposalProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.support;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.ui.assist.JresResourceRefConentProposalPovider;
import com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper;

/**
 * @author sundl
 *
 */
public class TableColumnStdFieldProposalProvider extends JresResourceRefConentProposalPovider {

	/**
	 * @param helper
	 * @param resType
	 * @param project
	 */
	public TableColumnStdFieldProposalProvider(IContentProposalProviderHelper helper, String resType, IARESProject project) {
		super(helper, resType, project);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.assist.JresResourceRefConentProposalPovider#filter(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected boolean filter(Object inputItem, Object element) {
		return false;
	}

}
