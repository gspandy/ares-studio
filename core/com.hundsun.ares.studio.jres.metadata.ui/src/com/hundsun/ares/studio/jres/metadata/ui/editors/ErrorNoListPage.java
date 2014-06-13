/**
 * Դ�������ƣ�ErrorNoListPage.java
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

import com.hundsun.ares.studio.jres.metadata.ui.block.ErrorNoListViewerBlock;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock;

/**
 * ����ű༭������ϸ����
 *
 */
public class ErrorNoListPage extends AbstractMetadataFormPage {

	private ErrorNoListViewerBlock errorNoListViewerBlock;
	
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public ErrorNoListPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	@Override
	protected void createMetadataComposite(Composite body, FormToolkit toolkit) {
		errorNoListViewerBlock = new ErrorNoListViewerBlock(this, getEditingDomain(), getSite(), getEditor().getARESResource(), getProblemPool());
		errorNoListViewerBlock.setEditableControl(getEditableControl());
		errorNoListViewerBlock.createControl(body, toolkit);
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(errorNoListViewerBlock);
		addPropertyListener(errorNoListViewerBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(errorNoListViewerBlock);
	}
	
	@Override
	public void infoChange() {
		errorNoListViewerBlock.setInput(getInfo());
		super.infoChange();
	}

	/**
	 * @return the errorNoListViewerBlock
	 */
	public ErrorNoListViewerBlock getErrorNoListViewerBlock() {
		return errorNoListViewerBlock;
	}
	
	@Override
	public void dispose() {
		removePropertyListener(errorNoListViewerBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(errorNoListViewerBlock);
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataFormPage#getViewerBlock()
	 */
	@Override
	protected ColumnViewerBlock getViewerBlock() {
		return errorNoListViewerBlock;
	}
	
}
