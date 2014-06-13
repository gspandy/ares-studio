/**
 * Դ�������ƣ�MetadataEMFFormEditor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IGotoMarker;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.internal.core.ARESProject;
import com.hundsun.ares.studio.jres.model.metadata.Operation;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.EMFFormPage;
import com.hundsun.ares.studio.ui.editor.blocks.RevisionHistoryListPage;

/**
 * �����༭��ģ�ͣ�ΪԪ���ݱ༭�����ӻ���ҳ���û�����ҳ���޶���Ϣҳ��
 * @author qinyuan
 *
 */
public abstract class MetadataEMFFormEditor extends EMFFormEditor {
	
	protected OperationEditPage operationPage;
	protected RevisionHistoryListPage historyPage;
	


	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IGotoMarker.class) {
			return new IGotoMarker() {
				
				@Override
				public void gotoMarker(IMarker marker) {
					String uri;
					try {
						uri = (String) marker.getAttribute(IMarker.LOCATION);
						EObject obj = getInfo().eResource().getEObject(uri);
						
						if(obj instanceof Operation) {
							operationPage.getEditor().setActivePage(operationPage.getId());
							operationPage.getBlock().getViewer().setSelection(new StructuredSelection(obj), true);
						}else if(obj instanceof RevisionHistory) {
							historyPage.getEditor().setActivePage(historyPage.getId());
							historyPage.getColumnViewer().setSelection(new StructuredSelection(obj), true);
						}else {
							setSelection(obj);
						}
						
//						if(obj instanceof MetadataItem) {
//							getMetaDataItemPage().getEditor().setActivePage(getMetaDataItemPage().getId());
//							getMetaDataItemPage().getColumnViewer().setSelection(new StructuredSelection(obj), true);
//						}else if(obj instanceof Operation) {
//							operationPage.getEditor().setActivePage(operationPage.getId());
//							operationPage.getBlock().getViewer().setSelection(new StructuredSelection(obj), true);
//						}else if(obj instanceof RevisionHistory) {
//							historyPage.getEditor().setActivePage(historyPage.getId());
//							historyPage.getColumnViewer().setSelection(new StructuredSelection(obj), true);
//						}else if(obj instanceof DictionaryItem) {
//							getMetaDataItemPage().getEditor().setActivePage(getMetaDataItemPage().getId());
//							getMetaDataItemPage().getColumnViewer().setSelection(new StructuredSelection(obj.eContainer()), true);
//							((DictionaryListPage)getMetaDataItemPage()).getDetailColumnViewer().setSelection(new StructuredSelection(obj), true);
//						}
						
					} catch (CoreException e) {
						e.printStackTrace();
					}
					
				}
			};
		}
		return super.getAdapter(adapter);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	@Override
	protected void addPages() {
		

		
		try {
			addPage(addMetadataItemPage());
			
			operationPage = new OperationEditPage(this, "oparetion", "�û�����");
			addPage(operationPage);
			
			historyPage = new RevisionHistoryListPage(this, "histroy", "�޶���Ϣ");
			addPage(historyPage);
			
			// 2013-05-15 sundl ֻ����ref nature�����overviewҳ��
			// ע��������߼���Ϊ�����Ϲ��̵ļ����ԣ���ref nature�Ĳ���ʾ���ýڵ㣻��ref nature�Ĳ���ʾ���ýڵ�
			IARESResource res = getARESResource();
			if (res == null) 
				return;
			IARESProject proj = res.getARESProject();
			if (proj != null && !ARESProject.hasRefNature(proj.getProject())) {
				EMFFormPage overview = addMetadataItemOverViewPage();
				if (overview != null)
					addPage(overview);
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ҫ��ӵ�Ԫ���ݻ���ҳ
	 * @return
	 */
	protected abstract EMFFormPage addMetadataItemPage();
	
	/**
	 * gotoMarker
	 * @param element
	 */
	protected abstract void setSelection(Object element);
	/**
	 * Ҫ��ӵ�Ԫ���ݻ���ҳ
	 * @return
	 */
	protected abstract EMFFormPage addMetadataItemOverViewPage();
	
}
