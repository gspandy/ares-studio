/**
 * Դ�������ƣ�DictionaryOverViewPage.java
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

import com.hundsun.ares.studio.jres.metadata.ui.block.DictionaryListOverviewViewerBlock;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;

/**
 * @author wangxh
 *�����ֵ�����ҳ
 */
public class DictionaryOverViewPage extends AbstractMetadataOverviewFormPage {

	private DictionaryListOverviewViewerBlock dictionaryListOverviewViewerBlock;
	
	public DictionaryOverViewPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataOverviewFormPage#createMetadataComposite(org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	protected void createMetadataComposite(Composite body, FormToolkit toolkit) {
		dictionaryListOverviewViewerBlock = new DictionaryListOverviewViewerBlock(this, getEditingDomain(), getSite(), getEditor().getARESResource(), getProblemPool());
		dictionaryListOverviewViewerBlock.setEditableControl(getEditableControl());
		dictionaryListOverviewViewerBlock.createControl(body, toolkit);
		addPropertyListener(dictionaryListOverviewViewerBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(dictionaryListOverviewViewerBlock);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#infoChange()
	 */
	@Override
	public void infoChange() {
		dictionaryListOverviewViewerBlock.setInput(getInfo());
		super.infoChange();
	}
	
	@Override
	public void dispose() {
		removePropertyListener(dictionaryListOverviewViewerBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(dictionaryListOverviewViewerBlock);
		super.dispose();
	}
	
}
