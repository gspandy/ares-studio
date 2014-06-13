/**
 * Դ�������ƣ�BusinessDataTypeOverviewPage.java
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

import com.hundsun.ares.studio.jres.metadata.ui.block.BusinessDataTypeListOverviewViewerBlock;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;

/**
 * @author wangxh
 *ҵ��������������ҳ
 */
public class BusinessDataTypeOverviewPage extends AbstractMetadataOverviewFormPage {

	private BusinessDataTypeListOverviewViewerBlock businessDataTypeListOverviewViewerBlock;
	
	public BusinessDataTypeOverviewPage(EMFFormEditor editor, String id,
			String title) {
		super(editor, id, title);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataOverviewFormPage#createMetadataComposite(org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	protected void createMetadataComposite(Composite body, FormToolkit toolkit) {
		businessDataTypeListOverviewViewerBlock = new BusinessDataTypeListOverviewViewerBlock(this, getEditingDomain(), getSite(), getEditor().getARESResource(), getProblemPool());
		businessDataTypeListOverviewViewerBlock.setEditableControl(getEditableControl());
		businessDataTypeListOverviewViewerBlock.createControl(body, toolkit);
		addPropertyListener(businessDataTypeListOverviewViewerBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(businessDataTypeListOverviewViewerBlock);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#infoChange()
	 */
	@Override
	public void infoChange() {
		businessDataTypeListOverviewViewerBlock.setInput(getInfo());
		super.infoChange();
	}
	
	@Override
	public void dispose() {
		removePropertyListener(businessDataTypeListOverviewViewerBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(businessDataTypeListOverviewViewerBlock);
		super.dispose();
	}

}
