/**
 * Դ�������ƣ�AbstractMetadataFormPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.editors;

import java.util.Map;

import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;
import com.hundsun.ares.studio.jres.script.engin.ScriptUtils;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.EMFFormPage;

/**
 * Ԥ��ҳ����
 * 
 * @author yanwj06282
 *
 */
public abstract class AbstractMetadataOverviewFormPage extends EMFFormPage {

	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public AbstractMetadataOverviewFormPage(EMFFormEditor editor, String id,
			String title) {
		super(editor, id, title);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#isNeedValidate(org.eclipse.emf.transaction.ResourceSetChangeEvent)
	 */
	@Override
	protected boolean isNeedValidate(ResourceSetChangeEvent event) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#validate()
	 */
	@Override
	public void validate() {
		
	}
	
	//��ȡԪ�����б�
	protected MetadataResourceData<?> getInfo() {
		return (MetadataResourceData<?>) getEditor().getInfo();
	}
	
	//�����ű�������
	protected Map<String, Object> createScriptContext() {
		return ScriptUtils.createDefaultScriptContext(ScriptUtils.MODE_EDITOR_BUTTON,null, getEditor().getARESResource(), getInfo(), getClass().getClassLoader());
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#doCreateFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void doCreateFormContent(IManagedForm managedForm) {

		Composite body = managedForm.getForm().getBody();
		FormToolkit toolkit = managedForm.getToolkit();
		
		createMetadataComposite(body, toolkit);
		body.setLayout(new FillLayout());
		
	}

	/**
	 * �����༭����
	 * 
	 * @param body
	 * @param toolkit
	 */
	protected abstract void createMetadataComposite(Composite body, FormToolkit toolkit);
	
}
