/**
 * Դ�������ƣ�ErrnoColumnLabelProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.ui.editor.page;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.biz.ErrorInfo;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoItem;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;

/**
 * @author sundl
 *
 */
public class ErrnoColumnLabelProvider extends EObjectColumnLabelProvider {

	private IARESProject project;
	/**
	 * @param attribute
	 */
	public ErrnoColumnLabelProvider(EStructuralFeature attribute, IARESProject project) {
		super(attribute);
		this.project = project;
	}

	protected EObject getOwner(Object element) {
		if (element instanceof ErrorInfo) {
			ErrorNoItem item = (ErrorNoItem) element;
			ReferenceManager manager = ReferenceManager.getInstance();
			ReferenceInfo refInfo = manager.getFirstReferenceInfo(project, IMetadataRefType.ErrNo_No, item.getNo(), false);
			if (refInfo != null) {
				ErrorNoItem refObj = (ErrorNoItem) refInfo.getObject();
				if (refObj != null) {
					return refObj;
				}
			}
		}
		return null;
	}
	
}
