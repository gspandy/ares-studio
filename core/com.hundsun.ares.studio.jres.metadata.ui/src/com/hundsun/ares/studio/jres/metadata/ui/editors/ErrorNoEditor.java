/**
 * Դ�������ƣ�ErrorNoEditor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.editors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.viewers.StructuredSelection;

import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.ui.editor.EMFFormPage;

/**
 * ����ű༭��
 * @author qinyuan
 *
 */
public class ErrorNoEditor extends MetadataEMFFormEditor {
	

	private ErrorNoListPage metadataItemPage;

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormEditor#getInfoClass()
	 */
	@Override
	protected EClass getInfoClass() {
		return MetadataPackage.Literals.ERROR_NO_LIST;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.MetadataEMFFormEditor#addMetadataitemPage()
	 */
	@Override
	protected EMFFormPage addMetadataItemPage() {
		metadataItemPage = new ErrorNoListPage(this, "list", "�����");
		return metadataItemPage;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.MetadataEMFFormEditor#setSelection(java.lang.Object)
	 */
	@Override
	protected void setSelection(Object element) {
		metadataItemPage.getEditor().setActivePage(metadataItemPage.getId());
		metadataItemPage.getErrorNoListViewerBlock().getColumnViewer().setSelection(new StructuredSelection(element), true);
	}

	@Override
	protected EMFFormPage addMetadataItemOverViewPage() {
		return new ErrorOverviewPage(this,"overview","�����б�");
	}

}
