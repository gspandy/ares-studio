/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.assist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

/**
 * ������ʾ�ı��ؼ���չ�ࡣ������ʾ��Ϣ��ʹ�������á�
 * 
 * @author mawb
 */
public class TextContentAssistEx extends Text implements IContentProposalProvider {
	
	protected IAssistantProvider provider;
	private SimpleContentProposalAdapter adapter;
	
	// �ı��ָ���
	private String separator = null;
	
	/**
	 * @param parent ������
	 * @param style �ؼ���ʽ
	 */
	public TextContentAssistEx(Composite parent, int style) {
		super(parent, style);
		initTextContentAssist();
	}
	
	/**
	 * @return the adapter
	 */
	public ContentProposalAdapter getAdapter() {
		return adapter;
	}
	
	/**
	 * 
	 * @param parent ������
	 * @param style �ؼ���ʽ
	 * @param provider ������ʾ�ṩ��
	 */
	public TextContentAssistEx(Composite parent, int style, IAssistantProvider provider) {
		super(parent, style);
		this.provider = provider;
		initTextContentAssist();
	}
	
	/**
	 * ��ʼ��������ʾ��Ϣ��
	 */
	private void initTextContentAssist() {
		adapter = new SimpleContentProposalAdapter(this, new TextContentAdapter(), this, KeyStroke.getInstance(SWT.ALT, '/'), null);
		adapter.setPropagateKeys(true);
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		
		adapter.addContentProposalListener(new IContentProposalListener2() {
			public void proposalPopupClosed(ContentProposalAdapter adapter) {
				Event e = new Event();
				e.data = new Boolean(false);
			}
			public void proposalPopupOpened(ContentProposalAdapter adapter) {
				Event e = new Event();
				e.data = new Boolean(true);
			}
		});
		
//		this.addKeyListener(new KeyAdapter(){
//			public void keyPressed(KeyEvent e) {
//				if(e.character=='/' && e.stateMask==SWT.ALT){
//					adapter.openProposalPopup();
//				}
//			}		
//		});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#checkSubclass()
	 */
	@Override
	protected void checkSubclass() {
//		super.checkSubclass();
	}
	
	/*
	 * @param contentProvider the contentProvider to set
	 */
	public void setContentProvider(IAssistantProvider provider) {
		this.provider = provider;
	}
	
	public void setContentSeparator(String separator) {
		this.separator = separator;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.fieldassist.IContentProposalProvider#getProposals(java.lang.String, int)
	 */
	public IContentProposal[] getProposals(String contents, int position) {
//		String text = getValidContent(contents, position);
		List<IContentProposal> list = new ArrayList<IContentProposal>();
		Object[] proposals = provider.getProposals();
		if (proposals == null) {
			return new IContentProposal[0];
		}
		
		// ��ȡ��ƥ���ַ�������ǰ׺�ַ����ͺ�׺�ַ���
		int start = getStartIndex(contents, position);
		int end = getEndIndex(contents, position);
		
		String matchingStr = contents.substring(start, position);
		String prefixStr = contents.substring(0, start);
		String suffixStr = contents.substring(end);
		
		for (int i = 0; i < proposals.length; i++) {
			final Object tempProposal = proposals[i];
			final String content = provider.getContent(tempProposal);
			if (content.length () >= matchingStr.length() && content.substring(0, matchingStr.length()).equalsIgnoreCase(matchingStr)) {
				list.add(getProposal(tempProposal, prefixStr, suffixStr));
			}
		}
		return (IContentProposal[]) list.toArray (new IContentProposal[list.size ()]);
	}
	
	/**
	 * ��ȡ��ƥ���ַ�������ʼλ�ã���ǰ�������λ�õ�ǰһ���ָ����ĺ�һλ���ָ�������ʱΪ0��
	 * 
	 * @param contents ��ʾ��
	 * @param position ��ǰ���λ��
	 * @return
	 */
	private int getStartIndex(String contents, int position) {
		if (separator != null && !separator.equals("")) {
			int start = contents.lastIndexOf(separator, position);
			if (start >= 0) {
				return start + separator.length();
			}
		}
		return 0;
	}
	
	/**
	 * ��ȡ��ƥ���ַ����Ľ���λ�ã���ǰ�������λ�õĺ�һ���ָ�������ʼλ�ã��ָ�������ʱΪ��ʾ���ĳ��ȣ�
	 * 
	 * @param contents ��ʾ��
	 * @param position ��ǰ���λ��
	 * @return
	 */
	private int getEndIndex(String contents, int position) {
		if (separator != null && !separator.equals("")) {
			int end = contents.indexOf(separator, position);
			if (end >= 0) {
				return end;
			}
		}
		return contents.length();
	}
	
	/**
	 * ��ȡ������ʾ��ʾ���ݡ�
	 * 
	 * @param tempProposal
	 * @return
	 */
	private IContentProposal getProposal(final Object tempProposal, final String prefix, final String suffix) {
		IContentProposal proposal = new IContentProposal() {

			public String getContent() {
				return prefix + provider.getContent(tempProposal) + suffix;
			}

			public int getCursorPosition() {
				return provider.getContent(tempProposal).length() + prefix.length();
			}

			public String getDescription() {
				return provider.getDescription(tempProposal);
			}

			public String getLabel() {
				return provider.getLabel(tempProposal);
			}
			
		};
		return proposal;
	}
	
}
