/**
 * Դ�������ƣ�BusinessDataTypeListPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.editors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.jres.metadata.ui.block.BusinessDataTypeListViewerBlock;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock;

/**
 * ����ҵ���������ͱ༭����ϸ����
 *
 */
public class BusinessDataTypeListPage extends AbstractMetadataFormPage {

	private BusinessDataTypeListViewerBlock businessDataTypeListViewerBlock;
	
	/**
	 * ҵ����������
	 * @param editor
	 * @param id
	 * @param title
	 */
	public BusinessDataTypeListPage(EMFFormEditor editor,
			String id,
			String title) {
		super(editor, id, title);
	}

	@Override
	protected void createMetadataComposite(Composite body, FormToolkit toolkit) {
		businessDataTypeListViewerBlock = new BusinessDataTypeListViewerBlock(this, getEditingDomain(), getSite(), getEditor().getARESResource(), getProblemPool());
		businessDataTypeListViewerBlock.setEditableControl(getEditableControl());
		businessDataTypeListViewerBlock.createControl(body, toolkit);
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(businessDataTypeListViewerBlock);
		addPropertyListener(businessDataTypeListViewerBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(businessDataTypeListViewerBlock);
	}

	/**
	 * @return the businessDataTypeListViewerBlock
	 */
	public BusinessDataTypeListViewerBlock getBusinessDataTypeListViewerBlock() {
		return businessDataTypeListViewerBlock;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerListPage#infoChange()
	 */
	@Override
	public void infoChange() {
		businessDataTypeListViewerBlock.setInput(getInfo());
		super.infoChange();
//		businessDataTypeListViewerBlock.getOperationControl().setData(getInfo());
//		businessDataTypeListViewerBlock.getOperationControl().setContext(createScriptContext());
	}
	
	@Override
	public void dispose() {
		removePropertyListener(businessDataTypeListViewerBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(businessDataTypeListViewerBlock);
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataFormPage#getViewerBlock()
	 */
	@Override
	protected ColumnViewerBlock getViewerBlock() {
		return businessDataTypeListViewerBlock;
	}
	
}
