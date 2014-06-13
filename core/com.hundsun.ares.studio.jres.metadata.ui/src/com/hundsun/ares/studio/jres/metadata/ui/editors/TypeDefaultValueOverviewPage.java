/**
 * Դ�������ƣ�TypeDefaultValueOverviewPage.java
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

import com.hundsun.ares.studio.jres.metadata.ui.block.TypeDefaultValueListOverviewViewBlock;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;

/**
 * @author wangxh
 *	Ĭ��ֵ����ҳ
 */
public class TypeDefaultValueOverviewPage extends AbstractMetadataOverviewFormPage {

	private TypeDefaultValueListOverviewViewBlock typeDefaultValueListOverviewViewBlock;
	
	public TypeDefaultValueOverviewPage(EMFFormEditor editor, String id,
			String title) {
		super(editor, id, title);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataOverviewFormPage#createMetadataComposite(org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	protected void createMetadataComposite(Composite body, FormToolkit toolkit) {
		typeDefaultValueListOverviewViewBlock = new TypeDefaultValueListOverviewViewBlock(this, getEditingDomain(), getSite(), getEditor().getARESResource(), getProblemPool());
		typeDefaultValueListOverviewViewBlock.setEditableControl(getEditableControl());
		typeDefaultValueListOverviewViewBlock.createControl(body, toolkit);
		addPropertyListener(typeDefaultValueListOverviewViewBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(typeDefaultValueListOverviewViewBlock);
	}
	
	@Override
	public void infoChange() {
		typeDefaultValueListOverviewViewBlock.setInput(getInfo());
		super.infoChange();
	}
	
	@Override
	public void dispose() {
		removePropertyListener(typeDefaultValueListOverviewViewBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(typeDefaultValueListOverviewViewBlock);
		super.dispose();
	}

}
