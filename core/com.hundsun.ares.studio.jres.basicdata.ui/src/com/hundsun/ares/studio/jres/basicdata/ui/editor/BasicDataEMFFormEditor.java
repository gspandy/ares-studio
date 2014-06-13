/**
 * Դ�������ƣ�MetadataEMFFormEditor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.basicdata.ui.editor;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IGotoMarker;

import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.jres.basicdata.ui.editor.pages.BasicDataExtendPage;
import com.hundsun.ares.studio.jres.basicdata.ui.editor.pages.BasicDataSQLPreviewPage;
import com.hundsun.ares.studio.jres.metadata.ui.editors.OperationEditPage;
import com.hundsun.ares.studio.jres.model.metadata.Operation;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.EMFFormPage;
import com.hundsun.ares.studio.ui.editor.blocks.RevisionHistoryListPage;
import com.hundsun.ares.studio.ui.editor.text.TextEditorInput;


public abstract class BasicDataEMFFormEditor extends EMFFormEditor {
	
	protected OperationEditPage operationPage;
	protected RevisionHistoryListPage historyPage;
	
	/**
	 * �༭����ʼ���쳣
	 */
	protected PartInitException exception;


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
			
			addPage(new BasicDataSQLPreviewPage(this, "preview", "SQLԤ��"), new TextEditorInput());
			
			historyPage = new RevisionHistoryListPage(this, "histroy", "�޶���Ϣ");
			addPage(historyPage);
			
			addPage(new BasicDataExtendPage(this, "extend", "��չ��������"));
//			addPage(addMetadataItemOverViewPage());
			addPageChangedListener(new BasicDataSQLPreviewUpdater());
			
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
//	/**
//	 * Ҫ��ӵ�Ԫ���ݻ���ҳ
//	 * @return
//	 */
//	protected abstract EMFFormPage addMetadataItemOverViewPage();

	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormEditor#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		try {
			super.init(site, input);
		} catch (PartInitException e) {
			throw new PartInitException(
					"\r\n" +
					"�򿪻������ݱ༭��ʧ�ܣ���ϸ��Ϣ:" + e.getMessage() + "\r\n" +
					"�밴����ϸ��Ϣ����ʾ����Ϣ������Ӧ�ļ��,����Բ��ɹ�,�������¼�飺" + "\r\n" +
					"1����Ŀ���ļ��Ƿ���Ҫˢ�£�ѡ����Ŀ�Ҽ�-����ˢ�¡���"+ "\r\n" +
					"2���������Ϣ�л������ݵ������Ƿ���ȷ��",
					e.getCause()) ;
		}
	}
	
}
