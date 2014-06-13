/**
 * Դ�������ƣ�ErrnoContentPropasolHelper.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl	
 */
package com.hundsun.ares.studio.biz.ui.editor.page;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.fieldassist.IContentProposal;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoItem;
import com.hundsun.ares.studio.ui.assist.JRESContentPorposalHelper;
import com.hundsun.ares.studio.ui.cellEditor.ARESContentProposal;

/**
 * @author sundl
 *
 */
public class ErrnoContentPropasolHelper extends JRESContentPorposalHelper{

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.assist.JRESContentPorposalHelper#getProposal(java.lang.String, int, org.eclipse.emf.ecore.EObject, com.hundsun.ares.studio.core.IARESResource)
	 */
	@Override
	protected IContentProposal getProposal(String contents, int position, EObject item, IARESResource resource) {
		if (item instanceof ErrorNoItem) {
			ErrorNoItem errornoItem = (ErrorNoItem) item;
			String no = errornoItem.getNo();
			String msg = errornoItem.getMessage();
			String level = errornoItem.getLevel();
			String desc = errornoItem.getDescription();
			String description = String.format("������Ϣ��%s\n���󼶱�:%s\n˵����%s\n", msg, level, desc);
			return new ARESContentProposal(no, null, description);
		}
		return null;
	}

}
