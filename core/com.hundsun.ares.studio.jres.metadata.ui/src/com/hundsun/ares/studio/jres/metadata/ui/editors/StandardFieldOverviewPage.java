/**
 * Դ�������ƣ�StandardFieldOverviewPage.java
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

import com.hundsun.ares.studio.jres.metadata.ui.block.StandardFieldListOverviewViewerBlock;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;

/**
 * @author wangxh
 *��׼�ֶ�����ҳ
 */
public class StandardFieldOverviewPage extends AbstractMetadataOverviewFormPage {

	private StandardFieldListOverviewViewerBlock standardFieldListOverviewViewerBlock;
	
	public StandardFieldOverviewPage(EMFFormEditor editor, String id,
			String title) {
		super(editor, id, title);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataOverviewFormPage#createMetadataComposite(org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	protected void createMetadataComposite(Composite body, FormToolkit toolkit) {
		standardFieldListOverviewViewerBlock = new StandardFieldListOverviewViewerBlock(this, getEditingDomain(), getSite(), getEditor().getARESResource(), getProblemPool());
		standardFieldListOverviewViewerBlock.setEditableControl(getEditableControl());
		standardFieldListOverviewViewerBlock.createControl(body, toolkit);
		addPropertyListener(standardFieldListOverviewViewerBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(standardFieldListOverviewViewerBlock);
	}
	
	@Override
	public void infoChange() {
		standardFieldListOverviewViewerBlock.setInput(getInfo());
		super.infoChange();
	}
	
	@Override
	public void dispose() {
		removePropertyListener(standardFieldListOverviewViewerBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(standardFieldListOverviewViewerBlock);
		super.dispose();
	}
	
}
