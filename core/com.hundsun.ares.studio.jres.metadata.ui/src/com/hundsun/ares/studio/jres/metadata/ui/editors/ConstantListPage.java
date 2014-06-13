/**
 * Դ�������ƣ�ConstantListPage.java
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

import com.hundsun.ares.studio.jres.metadata.ui.block.ConstantListViewerBlock;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock;
/**
 * �û������༭����ϸ����
 *
 */
public class ConstantListPage extends AbstractMetadataFormPage {

	private ConstantListViewerBlock constantListViewerBlock;
	
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public ConstantListPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/**
	 * @return the constantListViewerBlock
	 */
	public ConstantListViewerBlock getConstantListViewerBlock() {
		return constantListViewerBlock;
	}

	@Override
	protected void createMetadataComposite(Composite body, FormToolkit toolkit) {
		constantListViewerBlock = new ConstantListViewerBlock(this, getEditingDomain(), getSite(), getEditor().getARESResource(), getProblemPool());
		constantListViewerBlock.setEditableControl(getEditableControl());
		constantListViewerBlock.createControl(body, toolkit);
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(constantListViewerBlock);
		addPropertyListener(constantListViewerBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(constantListViewerBlock);
	}

	@Override
	public void infoChange() {
		constantListViewerBlock.setInput(getInfo());
		super.infoChange();
	}
	
	@Override
	public void dispose() {
		removePropertyListener(constantListViewerBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(constantListViewerBlock);
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataFormPage#getViewerBlock()
	 */
	@Override
	protected ColumnViewerBlock getViewerBlock() {
		return constantListViewerBlock;
	}
	
}
