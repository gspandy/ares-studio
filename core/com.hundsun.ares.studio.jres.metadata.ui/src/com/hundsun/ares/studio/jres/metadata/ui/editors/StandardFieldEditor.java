/**
 * Դ�������ƣ�StandardFieldEditor.java
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
 *��׼�ֵ�༭��
 *
 */
public class StandardFieldEditor extends MetadataEMFFormEditor {

	private StandardFieldListPage standardFieldListPage;
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormEditor#getInfoClass()
	 */
	@Override
	protected EClass getInfoClass() {
		return MetadataPackage.Literals.STANDARD_FIELD_LIST;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.MetadataEMFFormEditor#addMetadataitemPage()
	 */
	@Override
	protected EMFFormPage addMetadataItemPage() {
		standardFieldListPage = new StandardFieldListPage(this, "list", "��׼�ֶ��б�");
		return standardFieldListPage;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.MetadataEMFFormEditor#setSelection(java.lang.Object)
	 */
	@Override
	protected void setSelection(Object element) {
		standardFieldListPage.getEditor().setActivePage(standardFieldListPage.getId());
		standardFieldListPage.getStandardFieldViewBlock().getColumnViewer().setSelection(new StructuredSelection(element), true);
	}

	@Override
	protected EMFFormPage addMetadataItemOverViewPage() {
		StandardFieldOverviewPage overviewPage=new StandardFieldOverviewPage(this, "overview", "�����б�");
		return overviewPage;
	}

}
