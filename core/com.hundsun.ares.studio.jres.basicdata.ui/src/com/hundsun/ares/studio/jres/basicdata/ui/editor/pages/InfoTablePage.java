/**
 * Դ�������ƣ�StandardFieldListPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.basicdata.ui.editor.pages;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.jres.basicdata.constant.IBasicDataEpacakgeConstant;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.validate.util.BasicDataValidateUnit;
import com.hundsun.ares.studio.jres.basicdata.logic.epackage.BasicDataEpackageFactory;
import com.hundsun.ares.studio.jres.basicdata.ui.editor.blocks.InfoViewerBlock;
import com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataFormPage;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock;
import com.hundsun.ares.studio.validate.ValidateUtil;

public class InfoTablePage extends AbstractMetadataFormPage {
	
	private static Logger logger = Logger.getLogger(InfoTablePage.class);
	
	InfoViewerBlock infoViewBlock;

	/**
	 * ��׼�ֶ��б����
	 * @param editor
	 * @param id
	 * @param title
	 */
	public InfoTablePage(EMFFormEditor editor,
			String id,
			String title) {
		super(editor, id, title); 
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerListPage#configureValidateControl()
	 */
	@Override
	protected void configureValidateControl() {
//		getValidateControl().addValidateUnit(new EReferenceValidateUnit(getInfo(), MetadataPackage.Literals.METADATA_RESOURCE_DATA__ROOT));
//		getValidateControl().addValidateUnit(new EReferenceValidateUnit(getInfo(), MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS));
//		getValidateControl().setContext(ValidateUtil.getValidateContext(getEditor().getARESResource()));
		getValidateControl().addValidateUnit(new BasicDataValidateUnit(getInfo(),IBasicDataEpacakgeConstant.Attr_Info_Items));
		getValidateControl().setContext(ValidateUtil.getValidateContext(getEditor().getARESResource()));
	}

//	/**
//	 * @return the standardFieldViewBlock
//	 */
	public InfoViewerBlock getStandardFieldViewBlock() {
		return infoViewBlock;
	}

	@Override
	protected void createMetadataComposite(Composite composite,FormToolkit toolkit){
		EPackage epackage = null;
		try {
			epackage = BasicDataEpackageFactory.eINSTANCE.createEPackage(getEditor().getARESResource());
		} catch (Exception e) {
			logger.error("�༭��������ʱ����ȡEPackageʧ��", e);
			return;
		}
		infoViewBlock = new InfoViewerBlock(getEditingDomain(),
				getEditor().getARESResource(), getProblemPool(), epackage);
//		(this, getEditingDomain(), 
//				getSite(), getEditor().getARESResource(), 
//				getProblemPool(),
//				epackage,className);
		infoViewBlock.setEditableControl(getEditableControl());
		infoViewBlock.createControl(composite, toolkit);
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(infoViewBlock);
		addPropertyListener(infoViewBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(infoViewBlock);
		
		infoViewBlock.setInput(getInfo());
	}
	
	@Override
	public void infoChange() {
		super.infoChange();
//		infoViewBlock.setInput(getInfo());
	}
	
	@Override
	public void dispose() {
		removePropertyListener(infoViewBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(infoViewBlock);
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataFormPage#getViewerBlock()
	 */
	@Override
	protected ColumnViewerBlock getViewerBlock() {
		return infoViewBlock;
	}

	
}
