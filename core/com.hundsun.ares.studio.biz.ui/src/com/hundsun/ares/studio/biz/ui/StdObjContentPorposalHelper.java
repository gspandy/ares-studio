/**
 * Դ�������ƣ�StdObjContentPorposalHelper.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.ui;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.fieldassist.IContentProposal;

import com.hundsun.ares.studio.biz.ARESObject;
import com.hundsun.ares.studio.biz.StandardObjField;
import com.hundsun.ares.studio.biz.constants.IBizRefType;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.assist.JRESContentPorposalHelper;
import com.hundsun.ares.studio.ui.cellEditor.ARESContentProposal;

/**
 * @author sundl
 *
 */
public class StdObjContentPorposalHelper extends JRESContentPorposalHelper {
	
	public static StdObjContentPorposalHelper INSTANCE = new StdObjContentPorposalHelper();

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.assist.JRESContentPorposalHelper#getProposal(java.lang.String, int, org.eclipse.emf.ecore.EObject, com.hundsun.ares.studio.core.IARESResource)
	 */
	@Override
	protected IContentProposal getProposal(String contents, int position, EObject item, IARESResource resource) {
		if (item instanceof StandardObjField) {
			StandardObjField field = (StandardObjField) item;
			String content = field.getName();
			String type = field.getType();
			String desc = null;
			ReferenceInfo ref = ReferenceManager.getInstance().getFirstReferenceInfo(resource.getARESProject(), IBizRefType.Object, type, true);
			if (ref != null) {
				desc = ((ARESObject) ref.getObject()).getChineseName();
			}
			return new ARESContentProposal(content, desc);
		}
		return null;
	}

}
