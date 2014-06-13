package com.hundsun.ares.studio.atom.ui.module;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.databinding.edit.EditingDomainEObjectObservableValue;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.hundsun.ares.studio.atom.constants.IAtomConstants;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.cres.constant.ICresUIConstant;
import com.hundsun.ares.studio.cres.extend.core.constants.ICresExtendConstants;
import com.hundsun.ares.studio.cres.extend.cresextend.CresextendPackage;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.database.oracle.util.OracleDataBaseUtil;
import com.hundsun.ares.studio.jres.model.metadata.BizPropertyConfig;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.editor.blocks.EMFExtendSectionScrolledFormPage;

public class CresModulePropertyPage extends EMFExtendSectionScrolledFormPage<ModuleProperty> {

	private Text txtSubSysID;
	Combo txtDatabase;
	private Text txtDBConn;
	private CresModuleDependsBlock block;

	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public CresModulePropertyPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.EMFExtendSectionScrolledFormPage#getEClass()
	 */
	@Override
	protected EClass getEClass() {
		return CresextendPackage.Literals.CRES_MOUDLE_EXTEND_PROPERTY;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.EMFExtendSectionScrolledFormPage#getMapKey()
	 */
	@Override
	protected String getMapKey() {
		return ICresExtendConstants.CRES_EXTEND_MOUDLE_PROPERTY;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.page.ExtendSectionScrolledFormPage#createSections(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createSections(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		
		createBaseInfoSection(managedForm, toolkit);
		if(getResource().getRoot().getType().equals(IAtomConstants.ATOM_ROOT_TYPE)){
			//ԭ��ģ�������ʾҵ�����section ����ģ����²���ʾ
			createBizPropertyConfigSection(managedForm, toolkit);
		}
		createMoudleDependsSection(managedForm, toolkit);
	}

	/**
	 * ҵ�����
	 * @param managedForm
	 * @param toolkit
	 */
	private void createBizPropertyConfigSection(IManagedForm managedForm,
			FormToolkit toolkit) {
		Section sectionBizConfig = createSectionWithTitle(managedForm.getForm()
				.getBody(), toolkit, "ҵ�����", true);
		final Composite client = toolkit.createComposite(sectionBizConfig, SWT.NONE);
		client.setLayout(new GridLayout(10, false));
		//��ȡҵ���������Ϣ
		List<ReferenceInfo> infos = ReferenceManager.getInstance().getReferenceInfos(getARESProject(), IMetadataRefType.BizPropertyConfig, true);
		//����𣬶�Ӧ��ֵΪ��ֵ""
		Button btnNoConfig = toolkit.createButton(client, "�����", SWT.RADIO);
		
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(btnNoConfig);
		//��
		getBindingContext().bindValue(SWTObservables.observeSelection(btnNoConfig), 
				new BizPropertyConfigObservableValue(getEditingDomain(), getModel(), CresextendPackage.Literals.CRES_MOUDLE_EXTEND_PROPERTY__BIZ_PROPERTY_CONFIG,""));
		for(ReferenceInfo info : infos){
			//ÿһ��ҵ�����ö�Ӧһ��radioButton
			BizPropertyConfig config = (BizPropertyConfig) info.getObject();
			//��������ʾ����ҵ�����õ�������
			Button btnConfig = toolkit.createButton(client, config.getChineseName(), SWT.RADIO);
			GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(btnConfig);
			//��ֵΪҵ�����õı��
			getBindingContext().bindValue(SWTObservables.observeSelection(btnConfig), 
					new BizPropertyConfigObservableValue(getEditingDomain(), getModel(), CresextendPackage.Literals.CRES_MOUDLE_EXTEND_PROPERTY__BIZ_PROPERTY_CONFIG,config.getName()));
			
		}
		sectionBizConfig.setClient(client);
	}

	/**
	 * @param managedForm
	 * @param toolkit
	 */
	private void createMoudleDependsSection(IManagedForm managedForm,
			FormToolkit toolkit) {
		
		Section section = createSectionWithTitle(managedForm.getForm()
				.getBody(), toolkit, "ģ������", true);
		final Composite content = toolkit.createComposite(section, SWT.NONE);
		GridLayoutFactory.fillDefaults().applyTo(content);
		
		Composite comp = toolkit.createComposite(content, SWT.NONE);
		GridDataFactory.fillDefaults().indent(0, 0).grab(true, true).applyTo(comp);
		comp.setLayout(new FillLayout());
		
		block = new CresModuleDependsBlock(comp,
				CresextendPackage.Literals.CRES_MOUDLE_EXTEND_PROPERTY__DEPENDS, 
				getEditingDomain(), resource, null);
		
		block.createControl(comp, toolkit);
		block.setInput(getInfo());
		
		addPropertyListener(block);
		getEditingDomain().getCommandStack().addCommandStackListener(block);
		
		section.setClient(content);
		toolkit.paintBordersFor(content);
	}
	
	/**
	 * ����������Ϣ����
	 * @param managedForm
	 * @param toolkit
	 */
	private void createBaseInfoSection(IManagedForm managedForm,
			FormToolkit toolkit) {
		Section sectionBaseInfo = createSectionWithTitle(managedForm.getForm()
				.getBody(), toolkit, "������Ϣ", true);
		final Composite baseInfo = toolkit.createComposite(sectionBaseInfo, SWT.NONE);
		
		Label lbSubSysID = toolkit.createLabel(baseInfo, "��ϵͳ��ţ�", SWT.NONE);
		txtSubSysID = toolkit.createText(baseInfo, "", SWT.BORDER);
		
		Label lbDBName = toolkit.createLabel(baseInfo, "���ݿ⣺", SWT.NONE);
		txtDatabase = new Combo(baseInfo, SWT.NONE);
		{
			txtDatabase.setItems(OracleDataBaseUtil.getDataBaseName(getARESProject(),true));
		}
		
		Label lbDBConn = toolkit.createLabel(baseInfo, "���ݿ����ӣ�", SWT.NONE);
		txtDBConn = toolkit.createText(baseInfo, "", SWT.BORDER);
		
		//����
		baseInfo.setLayout(new GridLayout(2, false));
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbSubSysID);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(txtSubSysID);
		
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbDBName);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(txtDatabase);
		
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbDBConn);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(txtDBConn);
		
		
		//�߼�ģ�鲻��ʾ
		if(getResource().getModule().getParent().getElementName().equals(ICresUIConstant.LOGIC_ROOT_NAME)){
			sectionBaseInfo.setBounds(0, 0, 0, 0);
			sectionBaseInfo.setVisible(false);
		}else {
			sectionBaseInfo.setClient(baseInfo);
			toolkit.paintBordersFor(baseInfo);
		}
		
		databinding();
	}
	
	/**
	 * ���ݰ�
	 */
	private void databinding() {
		
		//��ϵͳ���
		getBindingContext().bindValue(SWTObservables.observeText(txtSubSysID, SWT.Modify), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_MOUDLE_EXTEND_PROPERTY__SUB_SYS_ID));

		//���ݿ�
//		getBindingContext().bindValue(SWTObservables.observeText(txtDBName, SWT.Modify), 
//				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
//						CresextendPackage.Literals.CRES_MOUDLE_EXTEND_PROPERTY__DATA_BASE_NAME));
		
		getBindingContext().bindValue(
				SWTObservables.observeSelection(txtDatabase), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_MOUDLE_EXTEND_PROPERTY__DATA_BASE_NAME));

		//���ݿ�����
		getBindingContext().bindValue(SWTObservables.observeText(txtDBConn, SWT.Modify), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_MOUDLE_EXTEND_PROPERTY__DATA_BASE_CONN));
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.page.ExtendPageWithMyDirtySystem#shouldLoad()
	 */
	@Override
	public boolean shouldLoad() {
		IProject project = getARESProject().getProject();
		try {
			if(project.hasNature(ICresExtendConstants.CRES_PROJECT_NATURE)){//cres����
				//ԭ�ӡ�����ģ��
				IFolder rootFolder = (IFolder)resource.getModule().getParent().getResource();
				if(StringUtils.equalsIgnoreCase("atom", rootFolder.getName()) ||
						StringUtils.equalsIgnoreCase("logic", rootFolder.getName())){
					return true;
				}
				return false;
			}
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	//��ҵ���������ֵ��radioButton
	private class BizPropertyConfigObservableValue extends EditingDomainEObjectObservableValue{
		String value;
		public BizPropertyConfigObservableValue(EditingDomain domain,
				EObject eObject, EStructuralFeature eStructuralFeature,String value) {
			super(domain, eObject, eStructuralFeature);
			this.value = value;
		}
		
		@Override
		protected Object doGetValue() {
			Object value = super.doGetValue();
			//�����ȡ��ֵ��button��Ӧ��ֵ��ȣ����ʾ��button��ѡ��
			if(value.equals(this.value)){
				return true;
			}
			return false;
		}
		
		@Override
		protected void doSetValue(Object value) {
			if(value instanceof Boolean){
				if((Boolean) value){
					//valueΪtrue����ʾѡ���˸�button������button��Ӧ��ֵ���õ�ģ����
					super.doSetValue(this.value);
				}
			}
		}
		
	}

}
