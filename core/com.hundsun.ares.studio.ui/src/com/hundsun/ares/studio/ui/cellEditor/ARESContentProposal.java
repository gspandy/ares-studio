/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.cellEditor;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.viewers.StyledString;

/**
 * IContent proposal��һ��Ĭ�ϵ�ʵ��; Ĭ��ʹ�� ���� - ������ ����չʾ���Ҷ�����ʹ��ǳɫ���֡�
 * @author sundl
 */
public class ARESContentProposal implements IContentProposal, IContentProposalExtension{

	private static final String SEPERATOR = " - ";
	
	private String content;
	private String shortDesc;
	private String desc;
	
	/**
	 * @param content Ҫ�滻������ ����Ϊ��
	 * @param shortDesc ������ ����Ϊ��
	 * @param desc ������������ʾ���Ҳ൥���Ĵ����У�����ʾ�����Ϊnull
	 */
	public ARESContentProposal(String content, String shortDesc, String desc) {
		this.content = content;
		this.shortDesc = shortDesc;
		this.desc = desc;
	}
	
	/**
	 * @param content Ҫ�滻������ ����Ϊ��
	 * @param shortDesc ������ ����Ϊ��
	 */
	public ARESContentProposal(String content, String shortDesc) {
		this(content, shortDesc, null);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.cellEditor.IContentProposalExtension#getStyledDisplayString()
	 */
	@Override
	public StyledString getStyledDisplayString() {
		StyledString styledString = new StyledString(content);
		if (!StringUtils.isEmpty(shortDesc)) {
			styledString.append(new StyledString(SEPERATOR + shortDesc, StyledString.DECORATIONS_STYLER));
		}
		return styledString;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.fieldassist.IContentProposal#getContent()
	 */
	@Override
	public String getContent() {
		return content;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.fieldassist.IContentProposal#getCursorPosition()
	 */
	@Override
	public int getCursorPosition() {
		return content.length();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.fieldassist.IContentProposal#getLabel()
	 */
	@Override
	public String getLabel() {
		return StringUtils.isEmpty(shortDesc) ? content : content + SEPERATOR + shortDesc;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.fieldassist.IContentProposal#getDescription()
	 */
	@Override
	public String getDescription() {
		return desc;
	}

}
