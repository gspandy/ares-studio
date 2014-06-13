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

import java.util.EventObject;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.ui.PartInitException;

import com.hundsun.ares.studio.core.model.JRESResourceInfo;
import com.hundsun.ares.studio.jres.basicdata.constant.IBasicDataEpacakgeConstant;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.StandardFieldColumn;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.StandardFieldModelAndData;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.StandardFieldPackageDefine;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.provider.BasicdataItemProviderAdapterFactory;
import com.hundsun.ares.studio.jres.basicdata.ui.editor.pages.STDModelAndDataDefineTablePage;
import com.hundsun.ares.studio.jres.basicdata.ui.editor.pages.STDModelAndDataPage;
import com.hundsun.ares.studio.jres.basicdata.ui.editor.pages.SingleTableOverviewPage;
import com.hundsun.ares.studio.jres.metadata.ui.editors.OperationEditPage;
import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;
import com.hundsun.ares.studio.ui.editor.EMFFormPage;
import com.hundsun.ares.studio.ui.editor.blocks.RevisionHistoryListPage;

/**
 *��׼�ֵ�༭��
 *
 */
public class STDModelAndDataEditor extends BasicDataEMFFormEditor {

	private STDModelAndDataPage singleTableListPage;
	
	private STDModelAndDataDefineTablePage definePage;
	
	private boolean shouldReloadModel = false;
	

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.MetadataEMFFormEditor#addMetadataitemPage()
	 */
	@Override
	protected EMFFormPage addMetadataItemPage() {
		return null;
	}

	
	@Override
	protected void configureComposedAdapterFactory(
			ComposedAdapterFactory adapterFactory) {
		adapterFactory.addAdapterFactory(new BasicdataItemProviderAdapterFactory());
	}

	@Override
	protected void addPages() {
		
		try {
			addPage(new SingleTableOverviewPage(this, "overview", "������Ϣ"){
				@Override
				protected JRESResourceInfo getInfo() {
					return ((StandardFieldModelAndData) getEditor().getInfo()).getData();
				}
				
			});
			
			singleTableListPage = new STDModelAndDataPage(this, "list", "����",IBasicDataEpacakgeConstant.MasterItem);
			addPage(singleTableListPage);
			
			definePage = new STDModelAndDataDefineTablePage(this, "info", "ģ�Ͷ���");
			addPage(definePage);
			
			operationPage = new OperationEditPage(this, "oparetion", "�û�����"){
				protected JRESResourceInfo getInfo() {
					StandardFieldModelAndData info = (StandardFieldModelAndData)getEditor().getInfo();
					return  info.getData();
				};
			};
			addPage(operationPage);
			
			historyPage = new RevisionHistoryListPage(this, "histroy", "�޶���Ϣ"){
				protected JRESResourceInfo getInfo() {
					StandardFieldModelAndData info = (StandardFieldModelAndData)getEditor().getInfo();
					return  info.getData();
				};
			};
			addPage(historyPage);
			
			getEditingDomain().getCommandStack().addCommandStackListener(new CommandStackListener() {
				
				@Override
				public void commandStackChanged(EventObject event) {
					Command command = getEditingDomain().getCommandStack().getMostRecentCommand();
					if(command != null){
						for(Object obj:command.getAffectedObjects()){
							if(obj instanceof  StandardFieldPackageDefine){
								shouldReloadModel = true;
							}else if(obj instanceof  StandardFieldColumn){
								shouldReloadModel = true;
							}
						}
					}
				}
			});
			
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormEditor#handleAfterSave()
	 */
	@Override
	protected void handleAfterSave() {
		try {
			if(shouldReloadModel){
				createModel();
				singleTableListPage.recreateViewerBlock();
				//�����������ʱ���п��ܻ�û�򿪵ڶ���ҳ�棬����block���ܻ����null�����
				if(definePage.getStandardFieldViewBlock() != null){
					definePage.reset();
				}
				shouldReloadModel = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormEditor#getInfoClass()
	 */
	@Override
	protected EClass getInfoClass() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.basicdata.ui.editor.BasicDataEMFFormEditor#setSelection(java.lang.Object)
	 */
	@Override
	protected void setSelection(Object element) {
	}
	
	//ר�����ڵ���������ݵ�ģ�Ͷ���󣬶Ա༭�����б��棬��Ϊ��ʱEMF����ջ������AffectedObjectsΪ�գ�
	//�����shouldReloadModel���иı䣬�����ֶ���Ϊtrue���Խ������ˢ��
	public void save(){
		shouldReloadModel = true;
		doSave(new NullProgressMonitor());
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
