/**
 * Դ�������ƣ�StandardDataTypeOverviewPage.java
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

import com.hundsun.ares.studio.jres.metadata.ui.block.StandardDataTypeListOverviewViewerBlock;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;

/**
 * @author wangxh
 *��׼������������ҳ
 */
public class StandardDataTypeOverviewPage extends AbstractMetadataOverviewFormPage {

	private StandardDataTypeListOverviewViewerBlock standardDataTypeListOverviewViewerBlock;
	
	public StandardDataTypeOverviewPage(EMFFormEditor editor, String id,
			String title) {
		super(editor, id, title);
	}

	@Override
	protected void createMetadataComposite(Composite body, FormToolkit toolkit) {
		standardDataTypeListOverviewViewerBlock = new StandardDataTypeListOverviewViewerBlock(this, getEditingDomain(), getSite(), getEditor().getARESResource(), getProblemPool());
		standardDataTypeListOverviewViewerBlock.setEditableControl(getEditableControl());
		standardDataTypeListOverviewViewerBlock.createControl(body, toolkit);
		addPropertyListener(standardDataTypeListOverviewViewerBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(standardDataTypeListOverviewViewerBlock);
	}
	
	@Override
	public void infoChange() {
		standardDataTypeListOverviewViewerBlock.setInput(getInfo());
		super.infoChange();
	}
	
	@Override
	public void dispose() {
		removePropertyListener(standardDataTypeListOverviewViewerBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(standardDataTypeListOverviewViewerBlock);
		super.dispose();
	}
	
}
