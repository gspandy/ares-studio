/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.cellEditor;

import org.eclipse.jface.fieldassist.IContentProposal;

/**
 * 
 * @author sundl
 */
public interface IContentProposalProviderHelper {

	/**
	 * @param contents  ��ǰ����
	 * @param position  λ��
	 * @param obj       Ҫ�ṩ���ݵĶ���(input[]�е�һ��)
	 * @return
	 */
	public IContentProposal getProposal(String contents, int position, Object element);

}
