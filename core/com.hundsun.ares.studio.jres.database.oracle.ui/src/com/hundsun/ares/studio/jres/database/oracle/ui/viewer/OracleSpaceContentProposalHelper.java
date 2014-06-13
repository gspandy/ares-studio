/**
 * Դ�������ƣ�OracleSpaceContentProposalHelper
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
// 2012-2-22 sundl �޸����µ�ContentProposal���󣬿���ʵ���ò�ͬ��ɫ��ʾ������
// ����ɾ�����ô�����䡣
package com.hundsun.ares.studio.jres.database.oracle.ui.viewer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.fieldassist.IContentProposal;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpace;
import com.hundsun.ares.studio.ui.assist.JRESContentPorposalHelper;
import com.hundsun.ares.studio.ui.cellEditor.ARESContentProposal;
import com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper;

/**
 * ��ռ���ʾ
 */
// 2012-2-23 sundl �޸ģ� �ӻ���̳У���ʵ�֣� �޸��ĵ�ע��
public class OracleSpaceContentProposalHelper extends JRESContentPorposalHelper implements IContentProposalProviderHelper{

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.contentassist.JRESContentPorposalHelper#getProposal(java.lang.String, int, org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected IContentProposal getProposal(String contents, int position, EObject item, IARESResource resource) {
		if (item instanceof TableSpace && resource != null) {
			TableSpace tableSpace = (TableSpace) item;
			String content = tableSpace.getName();
			if (!content.toUpperCase().contains(contents.toUpperCase())) {
				return null;
			}
			
			String shortDesc = tableSpace.getChineseName();
			return new ARESContentProposal(content, shortDesc);
		}
		return null;
	}

}
