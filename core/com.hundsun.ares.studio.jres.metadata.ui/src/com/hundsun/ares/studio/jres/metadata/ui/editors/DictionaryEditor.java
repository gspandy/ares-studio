/**
 * Դ�������ƣ�DictionaryEditor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.editors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PartInitException;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.internal.core.ARESProject;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.ui.editor.EMFFormPage;
import com.hundsun.ares.studio.ui.editor.blocks.RevisionHistoryListPage;
import com.hundsun.ares.studio.ui.editor.sync.JRESDefaultSyncnizeUnit;
import com.hundsun.ares.studio.ui.editor.sync.JRESEditorSyncManager;

/**
 * �����ֵ�
 * @author qinyuan
 *
 */
public class DictionaryEditor extends MetadataEMFFormEditor {
	
	private DictionaryListPage metadataItemPage;

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormEditor#getInfoClass()
	 */
	@Override
	protected EClass getInfoClass() {
		return MetadataPackage.Literals.DICTIONARY_LIST;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.MetadataEMFFormEditor#addMetadataitemPage()
	 */
	@Override
	protected EMFFormPage addMetadataItemPage() {
		metadataItemPage = new DictionaryListPage(this, "list", "�ֵ���");
		return metadataItemPage;
	}
	@Override
	protected void addPages() {
		
		try {
			addPage(addMetadataItemPage());
			
			operationPage = new OperationEditPage(this, "oparetion", "�û�����");
			addPage(operationPage);
			
			addPage(new DictionaryItemListPage(this,"items"," �ֵ������"));
			
			addPage(new DictionaryConflictPage(this,"conflict"," ��ͻҳ"));
			
			historyPage = new RevisionHistoryListPage(this, "histroy", "�޶���Ϣ");
			addPage(historyPage);

			// 2013-05-15 sundl ֻ����ref nature�����overviewҳ��
			// ע��������߼���Ϊ�����Ϲ��̵ļ����ԣ���ref nature�Ĳ���ʾ���ýڵ㣻��ref nature�Ĳ���ʾ���ýڵ�
			IARESResource res = getARESResource();
			if (res == null) 
				return;
			IARESProject proj = res.getARESProject();
			if (proj != null && !ARESProject.hasRefNature(proj.getProject())) {
				addPage(addMetadataItemOverViewPage());
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.MetadataEMFFormEditor#setSelection(java.lang.Object)
	 */
	@Override
	protected void setSelection(Object element) {
		metadataItemPage.getEditor().setActivePage(metadataItemPage.getId());
		if(element instanceof DictionaryType){
			metadataItemPage.getDictionaryListViewerBlock().getColumnViewer().setSelection(new StructuredSelection(element), true);
		}else{
			metadataItemPage.getDictionaryListViewerBlock().getColumnViewer().setSelection(new StructuredSelection(((EObject)element).eContainer()), true);
			metadataItemPage.getDictionaryDetailViewerBlock().getColumnViewer().setSelection(new StructuredSelection(element), true);
		}
		
	}

	@Override
	protected EMFFormPage addMetadataItemOverViewPage() {
		return new DictionaryOverViewPage(this,"overview","�����б�");
	}
}
