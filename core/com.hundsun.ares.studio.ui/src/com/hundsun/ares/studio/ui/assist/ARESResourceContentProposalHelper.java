/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.assist;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.fieldassist.IContentProposal;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.util.ResourcesUtil;
import com.hundsun.ares.studio.ui.cellEditor.ARESContentProposal;

/**
 * ��ʾͨ�õ�ARES��Դ��helperʵ��
 * @author sundl
 */
public class ARESResourceContentProposalHelper extends JRESContentPorposalHelper {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.assist.JRESContentPorposalHelper#getProposal(java.lang.String, int, org.eclipse.emf.ecore.EObject, com.hundsun.ares.studio.core.IARESResource)
	 */
	@Override
	protected IContentProposal getProposal(String contents, int position, EObject item, IARESResource resource) {
		String name = resource.getName();
		String cname = ResourcesUtil.getCName(resource);
		if (StringUtils.containsIgnoreCase(name, contents) || StringUtils.containsIgnoreCase(cname, contents)) {
			return new ARESContentProposal(name, cname);
		}
		return null;
	}

}
