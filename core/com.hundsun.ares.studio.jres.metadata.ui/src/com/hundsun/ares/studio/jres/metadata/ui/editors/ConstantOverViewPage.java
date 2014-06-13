/**
 * Դ�������ƣ�ConstantOverViewPage.java
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

import com.hundsun.ares.studio.jres.metadata.ui.block.ConstantListOverviewViewerBlock;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;

/**
 * @author wangxh
 *�û���������ҳ
 */
public class ConstantOverViewPage extends AbstractMetadataOverviewFormPage {

	private ConstantListOverviewViewerBlock sonstantListOverviewViewerBlock;
	
	public ConstantOverViewPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataOverviewFormPage#createMetadataComposite(org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	protected void createMetadataComposite(Composite body, FormToolkit toolkit) {
		sonstantListOverviewViewerBlock = new ConstantListOverviewViewerBlock(this, getEditingDomain(), getSite(), getEditor().getARESResource(), getProblemPool());
		sonstantListOverviewViewerBlock.setEditableControl(getEditableControl());
		sonstantListOverviewViewerBlock.createControl(body, toolkit);
		addPropertyListener(sonstantListOverviewViewerBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(sonstantListOverviewViewerBlock);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#infoChange()
	 */
	@Override
	public void infoChange() {
		sonstantListOverviewViewerBlock.setInput(getInfo());
		super.infoChange();
	}
	
	@Override
	public void dispose() {
		removePropertyListener(sonstantListOverviewViewerBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(sonstantListOverviewViewerBlock);
		super.dispose();
	}
	
}
