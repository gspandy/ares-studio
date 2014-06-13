/**
 * Դ�������ƣ�MDModuleCommonPropertyPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.metadata.ui.pages;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.hundsun.ares.studio.core.util.StringUtil;
import com.hundsun.ares.studio.internal.core.ARESProjectProperty;
import com.hundsun.ares.studio.jres.model.metadata.MDModuleCommonProperty;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.ui.editor.blocks.EMFExtendSectionScrolledFormPage;

/**
 * @author gongyf
 *
 */
public class MDModuleCommonPropertyPage extends EMFExtendSectionScrolledFormPage<ARESProjectProperty> {
	
	/**Ԫ����֧������*/
	Button btnModule;
	
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public MDModuleCommonPropertyPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.page.ExtendSectionScrolledFormPage#createSections(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createSections(IManagedForm managedForm) {
		// TODO Ԫ����֧������
		
		FormToolkit toolkitModule = managedForm.getToolkit();
		Section sectionModule = createSectionWithTitle(managedForm.getForm()
				.getBody(), toolkitModule, "Ԫ����", true);
		final Composite compModule = toolkitModule.createComposite(sectionModule, SWT.NONE);
		
		btnModule = new Button(compModule, SWT.CHECK);
		
		btnModule.setText("Ԫ����֧������");
		
		// ���ò���
		compModule.setLayout(new GridLayout(1, false));

		//Ԫ����֧������
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(btnModule);
		
		sectionModule.setClient(compModule);
		toolkitModule.paintBordersFor(compModule);
		
		//���ݰ�
		databinding();
	}

	@Override
	protected MDModuleCommonProperty getModel() {
		return (MDModuleCommonProperty) super.getModel();
	}
	
	/**
	 * 
	 */
	private void databinding() {
		
		IObservableValue btnModuleObserveWidget = SWTObservables.observeSelection(btnModule);
		IObservableValue btnModuleObserveValue = EMFEditObservables.observeValue(getEditingDomain(),
				getModel(), MetadataPackage.Literals.MD_MODULE_COMMON_PROPERTY__USE_REF_FEATURE);
		getBindingContext().bindValue(btnModuleObserveWidget, btnModuleObserveValue);
		
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.page.ExtendPageWithMyDirtySystem#shouldLoad()
	 */
	@Override
	public boolean shouldLoad() {
//		try {
//			if(getARESProject().getProject().hasNature(JRESCore.MODULE_NATURE)) {
//				return true;
//			}
//		} catch (CoreException e) {
//			e.printStackTrace();
//		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.EMFExtendSectionScrolledFormPage#getEClass()
	 */
	@Override
	protected EClass getEClass() {
		return MetadataPackage.Literals.MD_MODULE_COMMON_PROPERTY;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.EMFExtendSectionScrolledFormPage#getMapKey()
	 */
	@Override
	protected String getMapKey() {
		//return IJRESConstant.MDMODULE_COMMONPROPERTY_KEY;
		return StringUtil.EMPTY_STR;
	}

}
