/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.assist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;

import com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper;

/**
 * �����ϣ����δ���ֱ���ܴ���Ϊֹ�� 
 * ע��˳����ܻ���Ӱ����������
 * @author sundl
 */
public class CompositeProposalHelper implements IContentProposalProviderHelper {
	
	private List<IContentProposalProviderHelper> delegates = new ArrayList<IContentProposalProviderHelper>();
	
	public CompositeProposalHelper(IContentProposalProviderHelper... delegates) {
		this.delegates = Arrays.asList(delegates);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper#getProposal(java.lang.String, int, java.lang.Object)
	 */
	@Override
	public IContentProposal getProposal(String contents, int position, Object element) {
		for (IContentProposalProviderHelper d : this.delegates) {
			IContentProposal p = d.getProposal(contents, position, element);
			if (p != null)
				return p;
		}
		return null;
	}

}
