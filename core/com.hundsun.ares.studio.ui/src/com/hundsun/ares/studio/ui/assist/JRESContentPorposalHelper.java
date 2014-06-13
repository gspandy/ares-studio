/**
 * Դ�������ƣ�JRESContentPorposalHelper.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui.contentassist
 * ����˵����
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.assist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.fieldassist.IContentProposal;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper;

/**
 * JRES���ֵ�IContentProposalHelper���ࣻ
 * ����ʵ���˴�Input[]��ȡ���ݵ��߼��������������ֱ���ɾ���Ķ���(�����׼�ֶ�)����IContentProposal����
 * @author sundl
 */
public abstract class JRESContentPorposalHelper implements IContentProposalProviderHelper {

	@Override
	public IContentProposal getProposal(String contents, int position, Object element) {
			ReferenceInfo referenceInfo = (ReferenceInfo) element;
			return getProposal(contents, position, (EObject)referenceInfo.getObject(), referenceInfo.getResource());
	}

	protected abstract IContentProposal getProposal(String contents, int position, EObject item, IARESResource resource);

}
