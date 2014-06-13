/**
 * Դ�������ƣ�TypeDefaultValueListPage.java
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

import com.hundsun.ares.studio.jres.metadata.ui.block.TypeDefaultValueListViewBlock;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock;

public class TypeDefaultValueListPage extends AbstractMetadataFormPage {

	private TypeDefaultValueListViewBlock typeDefaultValueListViewBlock;
	
	/**
	 * Ĭ��ֵ�༭������
	 * @param editor
	 * @param id
	 * @param title
	 */
	public TypeDefaultValueListPage(EMFFormEditor editor,String id, String title) {
		super(editor, id, title);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataFormPage#createMetadataComposite(org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	protected void createMetadataComposite(Composite body, FormToolkit toolkit) {
		typeDefaultValueListViewBlock = new TypeDefaultValueListViewBlock(this, getEditingDomain(), getSite(), getEditor().getARESResource(), getProblemPool());
		typeDefaultValueListViewBlock.setEditableControl(getEditableControl());
		typeDefaultValueListViewBlock.createControl(body, toolkit);
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(typeDefaultValueListViewBlock);
		addPropertyListener(typeDefaultValueListViewBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(typeDefaultValueListViewBlock);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerListPage#infoChange()
	 */
	@Override
	public void infoChange() {
		typeDefaultValueListViewBlock.setInput(getInfo());
		super.infoChange();
	}

	/**
	 * @return the typeDefaultValueListViewBlock
	 */
	public TypeDefaultValueListViewBlock getTypeDefaultValueListViewBlock() {
		return typeDefaultValueListViewBlock;
	}
	
	@Override
	public void dispose() {
		removePropertyListener(typeDefaultValueListViewBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(typeDefaultValueListViewBlock);
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataFormPage#getViewerBlock()
	 */
	@Override
	protected ColumnViewerBlock getViewerBlock() {
		return typeDefaultValueListViewBlock;
	}
	
}
