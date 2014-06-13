/**
 * Դ�������ƣ�StandardFieldEditor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.basicdata.ui.editor;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.TriggerListener;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PartInitException;

import com.hundsun.ares.studio.jres.basicdata.constant.IBasicDataEpacakgeConstant;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.provider.BasicdataItemProviderAdapterFactory;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.validate.util.InfoTableLocatorHelper;
import com.hundsun.ares.studio.jres.basicdata.ui.editor.pages.BasicDataExtendPage;
import com.hundsun.ares.studio.jres.basicdata.ui.editor.pages.BasicDataSQLPreviewPage;
import com.hundsun.ares.studio.jres.basicdata.ui.editor.pages.InfoTablePage;
import com.hundsun.ares.studio.jres.basicdata.ui.editor.pages.MasterLinkTablePage;
import com.hundsun.ares.studio.jres.basicdata.ui.editor.pages.SingleTableOverviewPage;
import com.hundsun.ares.studio.jres.metadata.ui.editors.OperationEditPage;
import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;
import com.hundsun.ares.studio.ui.editor.EMFFormPage;
import com.hundsun.ares.studio.ui.editor.blocks.RevisionHistoryListPage;
import com.hundsun.ares.studio.ui.editor.text.TextEditorInput;

/**
 *��׼�ֵ�༭��
 *
 */
public class MasterSlaveLinkModelEditor extends BasicDataEMFFormEditor {

	private MasterLinkTablePage listPage;
	
	private InfoTablePage infoPage;
	
	private InfoTableLocatorHelper locator;
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormEditor#getInfoClass()
	 */
	@Override
	protected EClass getInfoClass() {
		return null;
//		return (EClass)BasicDataEpackageFactory.eINSTANCE.createEPackage(getARESResource()).getEClassifier(IBasicDataEpacakgeConstant.ResourceRoot);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.MetadataEMFFormEditor#addMetadataitemPage()
	 */
	@Override
	protected EMFFormPage addMetadataItemPage() {
//		listPage = new MasterLinkTablePage(this, "list", "���ӱ�");
		return listPage;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.MetadataEMFFormEditor#setSelection(java.lang.Object)
	 */
	@Override
	protected void setSelection(Object element) {
		listPage.getEditor().setActivePage(listPage.getId());
		listPage.getMasterListViewerBlock().getColumnViewer().setSelection(new StructuredSelection(element), true);
	}
	
	@Override
	protected void configureComposedAdapterFactory(
			ComposedAdapterFactory adapterFactory) {
		adapterFactory.addAdapterFactory(new BasicdataItemProviderAdapterFactory());
	}

	
	@Override
	protected void addPages() {
		
		locator = new InfoTableLocatorHelper(getInfo());
		locator.reset();
		
		getEditingDomain().addResourceSetListener(new TriggerListener() {
			
			@Override
			protected Command trigger(TransactionalEditingDomain domain,
					Notification notification) {
				int type = notification.getEventType();
				EStructuralFeature feature = (EStructuralFeature) notification.getFeature();
				String name =  ((EObject)notification.getNotifier()).eClass().getName();
				if(Notification.REMOVE == type
						&& StringUtils.equals(feature.getName(), IBasicDataEpacakgeConstant.Attr_Info_Items)){
					locator.reset();
				}
				
				if((Notification.SET == type ||Notification.UNSET == type)
						&& StringUtils.equals(name, IBasicDataEpacakgeConstant.InfoItem)){
					locator.reset();
				}
				return null;
			}
		});
		
		try {
			
			listPage = new MasterLinkTablePage(this, "list", "���ӱ�",locator);
			addPage(listPage);
			
			infoPage = new InfoTablePage(this, "info", "��Ϣ��");
			addPage(infoPage);
			
			operationPage = new OperationEditPage(this, "oparetion", "�û�����");
			addPage(operationPage);
			
			addPage(new BasicDataSQLPreviewPage(this, "preview", "SQLԤ��"), new TextEditorInput());
			
			historyPage = new RevisionHistoryListPage(this, "histroy", "�޶���Ϣ");
			addPage(historyPage);
			
			addPage(0, new SingleTableOverviewPage(this, "overview", "������Ϣ"));
			
			addPage(new BasicDataExtendPage(this, "extend", "��չ��������"));
			
			addPageChangedListener(new BasicDataSQLPreviewUpdater());
			
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	public InfoTableLocatorHelper getLocator() {
		return locator;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#pageChange(int)
	 */
	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		//�л�ҳʱˢ��һ�¹�����
		if(1 == newPageIndex && !locator.isReady()){
			listPage.relink();
		}
	}
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.EMFFormEditor#getDialogSettings()
	 */
	@Override
	protected IDialogSettings getDialogSettings() {
		IDialogSettings settings = ARESEditorPlugin.getDefault().getDialogSettings();
		IDialogSettings blockSettings = null;
		blockSettings = settings.getSection(this.getClass().getSimpleName());
		if (blockSettings == null) {
			blockSettings = settings.addNewSection(this.getClass().getSimpleName());
			blockSettings.put(ACTIVE_EDITOR_INDEX, 0);
		}
		
		return blockSettings;
	}

	

}
