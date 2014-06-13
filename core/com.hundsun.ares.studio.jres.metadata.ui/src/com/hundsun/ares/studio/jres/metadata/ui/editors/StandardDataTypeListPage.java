/**
 * Դ�������ƣ�StandardDataTypeListPage.java
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

import com.hundsun.ares.studio.jres.metadata.ui.block.StandardDataTypeListViewerBlock;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock;

public class StandardDataTypeListPage extends AbstractMetadataFormPage {
	
	private StandardDataTypeListViewerBlock standardDataTypeViewerBlock;	
	/**
	 * ��׼�������ͱ༭����׼�������ͽ���
	 * @param editor
	 * @param id
	 * @param title
	 */
	public StandardDataTypeListPage(EMFFormEditor editor,String id,
			String title) {
		super(editor, id, title);
	}

	/**
	 * 
	 * @param body
	 * @param toolkit
	 */
	@Override
	protected void createMetadataComposite(Composite body, FormToolkit toolkit) {
		standardDataTypeViewerBlock = new StandardDataTypeListViewerBlock(this, getEditingDomain(), getSite(), getEditor().getARESResource(), getProblemPool());
		standardDataTypeViewerBlock.setEditableControl(getEditableControl());
		standardDataTypeViewerBlock.createControl(body, toolkit);
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(standardDataTypeViewerBlock);
		addPropertyListener(standardDataTypeViewerBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(standardDataTypeViewerBlock);
	}

	/**
	 * @return the standardDataTypeViewerBlock
	 */
	public StandardDataTypeListViewerBlock getStandardDataTypeViewerBlock() {
		return standardDataTypeViewerBlock;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerListPage#infoChange()
	 */
	@Override
	public void infoChange() {
		standardDataTypeViewerBlock.setInput(getInfo());
		super.infoChange();
	}

	@Override
	public void dispose() {
		removePropertyListener(standardDataTypeViewerBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(standardDataTypeViewerBlock);
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataFormPage#getViewerBlock()
	 */
	@Override
	protected ColumnViewerBlock getViewerBlock() {
		return standardDataTypeViewerBlock;
	}
	
}
