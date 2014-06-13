/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.editor;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.hundsun.ares.studio.core.model.ICommonModel;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.internal.ui.editor.CommonModelBindingTextAdapter;
import com.hundsun.ares.studio.ui.page.SectionScrolledFormPage;
import com.hundsun.ares.studio.ui.util.ImporveControlWithDitryStateContext;

/**
 * ģ�����Ի���ҳ��
 * @author sundl
 */
public class ModulePropertyBasicPage extends SectionScrolledFormPage<ModuleProperty>{

	public ModulePropertyBasicPage(FormEditor editor) {
		super(editor, "modulepropertybasic", "������Ϣ");
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.page.SectionScrolledFormPage#createSections(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createSections(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		Section section = createSectionWithTitle(managedForm.getForm().getBody(), toolkit, "������Ϣ", true);
		Composite client = toolkit.createComposite(section, SWT.NONE);
		section.setClient(client);
		client.setLayout(new GridLayout(2, false));
		
		Label lbEName = toolkit.createLabel(client, "Ӣ����:");
		GridDataFactory.fillDefaults().applyTo(lbEName);
		String eName = ((ModulePropertyEditor) getEditor()).getResource().getModule().getElementName();
		Text txEName = toolkit.createText(client, eName,SWT.READ_ONLY);
		GridDataFactory.fillDefaults().applyTo(txEName);
		
		ImporveControlWithDitryStateContext context = new ImporveControlWithDitryStateContext(client, dirty,toolkit,getUndoContext(),managedForm.getMessageManager(),info);
		CommonModelBindingTextAdapter cName = new CommonModelBindingTextAdapter("������:", SWT.BORDER, context, ICommonModel.CNAME, info);
		cName.syncControl();
		context.getDirtyStatus().setValue(false);
		getEditableComponent().add(cName);
		refreshSection(section);
	}

}
