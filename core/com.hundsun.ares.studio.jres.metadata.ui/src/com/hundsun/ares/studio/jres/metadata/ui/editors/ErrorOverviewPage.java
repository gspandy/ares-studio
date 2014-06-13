/**
 * Դ�������ƣ�ErrorOverviewPage.java
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

import com.hundsun.ares.studio.jres.metadata.ui.block.ErrorNoListOverviewViewerBlock;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;

/**
 * @author wangxh
 *���������ҳ
 */
public class ErrorOverviewPage extends AbstractMetadataOverviewFormPage {

	private ErrorNoListOverviewViewerBlock errorNoListOverviewViewerBlock;
	
	public ErrorOverviewPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	@Override
	protected void createMetadataComposite(Composite body, FormToolkit toolkit) {
		errorNoListOverviewViewerBlock = new ErrorNoListOverviewViewerBlock(this, getEditingDomain(), getSite(), getEditor().getARESResource(), getProblemPool());
		errorNoListOverviewViewerBlock.setEditableControl(getEditableControl());
		errorNoListOverviewViewerBlock.createControl(body, toolkit);
		addPropertyListener(errorNoListOverviewViewerBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(errorNoListOverviewViewerBlock);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#infoChange()
	 */
	@Override
	public void infoChange() {
		errorNoListOverviewViewerBlock.setInput(getInfo());
		super.infoChange();
		
	}
	
	@Override
	public void dispose() {
		removePropertyListener(errorNoListOverviewViewerBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(errorNoListOverviewViewerBlock);
		super.dispose();
	}
	
}
