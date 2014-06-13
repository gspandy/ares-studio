/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.cellEditor;

import org.eclipse.jface.viewers.StyledString;

/**
 * Content Proposal�ӿڵ���չ����StyledString��ʾ˵����Ϣ��
 * @author sundl
 */
public interface IContentProposalExtension {

	/**
	 * Returns the styled string used to display this proposal in the list of completion proposals.
	 * This can for example be used to draw mixed colored labels.
	 * <p>
	 * <strong>Note:</strong> {@link ICompletionProposal#getDisplayString()} still needs to be
	 * correctly implemented as this method might be ignored in case of uninstalled owner draw
	 * support.
	 * </p>
	 *
	 * @return the string builder used to display this proposal
	 */
	StyledString getStyledDisplayString();
}
