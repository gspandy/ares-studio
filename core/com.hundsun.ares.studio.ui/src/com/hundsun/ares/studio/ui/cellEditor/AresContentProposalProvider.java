/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.cellEditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

/**
 * һ������Input��ContentProposalProvider��ʵ�֣�Input����ͨ��setInput()������̬�ı�;
 * ͨ��Helper�����ݵ�ǰ�����ContentProposal���й���
 * 
 * �򵥵�˵��Providerʵ�ֻ��ڵ�ǰ�����ĵĹ��ˣ� ���൱�ڸ��ݡ�������ṩ��ͬ���߼���
 * ���磬�ڽӿ��к������ݿ�����ʾ���ֶεĲ�ͬ��ͨ����ͬ��Provider��ʵ�֡�
 * 
 * Helper��ʵ�ְ���ʾ����ת��Ϊ�����IContentProposal����Ҫ�ѱ�׼�ֶ�ת������ʾ���Ǹ��ַ�������Ҫ��
 * һ����������ת������ʾ���ַ����������������ͨ����ͬ��Helper��ʵ�֡� ���൱�ڸ��ݡ���ʾʲô���ṩ��ͬ��Helper��ʵ�֣�
 * 
 * ������ͬ��Provider��Helper����ϣ�ʵ�ֲ�ͬ���߼��� ����
 * 		 ���ڱ��ֶ�� ��ʾ����׼�ֶΡ�
 * 		���ڽӿڲ���� ��ʾ ����׼�ֶΡ�
 * 		 ...
 * @author sundl
 */
public class AresContentProposalProvider implements IContentProposalProvider {
	
	protected Object[] input;
	protected IContentProposalProviderHelper helper;
	
	public AresContentProposalProvider() {}
	
	public AresContentProposalProvider(IContentProposalProviderHelper helper) {
		this.helper = helper;
	}
	
	public void setHelper(IContentProposalProviderHelper helper) {
		this.helper = helper;
	}
	
	protected void setInput(Object[] input) {
		this.input = input;
	}
	
	/**
	 * ������ʾ����
	 */
	public void updateContent(Object element) {};

	/* (non-Javadoc)
	 * @see org.eclipse.jface.fieldassist.IContentProposalProvider#getProposals(java.lang.String, int)
	 */
	@Override
	public IContentProposal[] getProposals(String contents, int position) {
		List<IContentProposal> proposals = new ArrayList<IContentProposal>();
		
		if (input != null) {
			for (Object obj : input) {
				IContentProposal proposal = helper.getProposal(contents, position, obj);
				if (proposal != null) {
					proposals.add(proposal);
				}
			}
		}
		
		Collections.sort(proposals, new AresContentProposalComparator(contents));
		return proposals.toArray(new IContentProposal[0]);
	}
	
}
