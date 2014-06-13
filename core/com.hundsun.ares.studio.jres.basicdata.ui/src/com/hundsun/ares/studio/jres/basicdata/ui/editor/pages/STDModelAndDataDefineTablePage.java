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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.jres.basicdata.core.basicdata.StandardFieldModelAndData;
import com.hundsun.ares.studio.jres.basicdata.ui.editor.blocks.STDModelDefineViewerBlock;
import com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataFormPage;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock;

public class STDModelAndDataDefineTablePage extends AbstractMetadataFormPage {
	
	private static Logger logger = Logger.getLogger(STDModelAndDataDefineTablePage.class);
	
	STDModelDefineViewerBlock standardFieldViewBlock;
	
	/**
	 * ��׼�ֶ��б����
	 * @param editor
	 * @param id
	 * @param title
	 */
	public STDModelAndDataDefineTablePage(EMFFormEditor editor,
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
//		getValidateControl().addValidateUnit(new BasicDataValidateUnit(getInfo(),IBasicDataEpacakgeConstant.Attr_Master_Items));
//		getValidateControl().setContext(ValidateUtil.getValidateContext(getEditor().getARESResource()));
	}

//	/**
//	 * @return the standardFieldViewBlock
//	 */
	public STDModelDefineViewerBlock getStandardFieldViewBlock() {
		return standardFieldViewBlock;
	}

	@Override
	protected void createMetadataComposite(Composite composite,FormToolkit toolkit){
		standardFieldViewBlock = new STDModelDefineViewerBlock( getEditingDomain(), 
				getEditor().getARESResource(), 
				getProblemPool());
		standardFieldViewBlock.setEditableControl(getEditableControl());
		standardFieldViewBlock.createControl(composite, toolkit);
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(standardFieldViewBlock);
		addPropertyListener(standardFieldViewBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(standardFieldViewBlock);
	}
	
	@Override
	public void infoChange() {
		Object model =  ((StandardFieldModelAndData)getEditor().getInfo()).getModel();
		standardFieldViewBlock.setInput(model);
		super.infoChange();
//		standardFieldViewBlock.getOperationControl().setData(getInfo());
//		standardFieldViewBlock.getOperationControl().setContext(createScriptContext());
	}
	
	@Override
	public void dispose() {
		removePropertyListener(standardFieldViewBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(standardFieldViewBlock);
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataFormPage#getViewerBlock()
	 */
	@Override
	protected ColumnViewerBlock getViewerBlock() {
		return standardFieldViewBlock;
	}
	
	
	public void reset(){
		super.infoChange();
		Object model =  ((StandardFieldModelAndData)getEditor().getInfo()).getModel();
		standardFieldViewBlock.setInput(model);
	}
	
}
