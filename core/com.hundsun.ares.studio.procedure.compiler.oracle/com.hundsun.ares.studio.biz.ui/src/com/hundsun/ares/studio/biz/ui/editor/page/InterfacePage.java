package com.hundsun.ares.studio.biz.ui.editor.page;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.TriggerListener;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.hundsun.ares.studio.biz.BizInterface;
import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.ui.block.ErrorInfoBlock;
import com.hundsun.ares.studio.biz.ui.block.InputParameterBlock;
import com.hundsun.ares.studio.biz.ui.block.OutputParameterBlock;
import com.hundsun.ares.studio.core.model.JRESResourceInfo;
import com.hundsun.ares.studio.core.service.DataServiceManager;
import com.hundsun.ares.studio.jres.metadata.service.IBusinessDataType;
import com.hundsun.ares.studio.jres.metadata.service.IMetadataService;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.DataBindingFormPage;
import com.hundsun.ares.studio.ui.editor.blocks.FormWidgetUtils;

/**
 * @author gongyf
 * @author sundl
 */
public class InterfacePage extends DataBindingFormPage {

	protected InputParameterBlock inputParamBlock;
	protected OutputParameterBlock outputParamBlock;
	protected ErrorInfoBlock errorInfoBlock;
	
	protected EStructuralFeature interfaceFeature;
	
	private TriggerListener trigger = new TriggerListener() {
		@Override
		protected Command trigger(TransactionalEditingDomain domain, Notification notification) {
			if (notification.getNotifier() instanceof Parameter ) {
				if ( BizPackage.Literals.PARAMETER__TYPE.equals(notification.getFeature())) {
					// �����������������������,���Զ��Ѷ�Ӧ��java���ʹ���
					final Parameter pd = (Parameter) notification.getNotifier();
					// sundl ֻ�зǱ�׼�ֶβ���Ҫ���������Ĵ���
					if (pd.getParamType() != ParamType.NON_STD_FIELD)
						return null;
					
					final String type = pd.getType();
					return new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							IMetadataService service = DataServiceManager.getInstance().getService(
									getEditor().getARESResource().getARESProject(), IMetadataService.class);
							IBusinessDataType bizType = service.getBusinessDataType(type);
							if (bizType != null) {
//								pd.setRealType(bizType.getRealType("java"));
								pd.setRealType(bizType.getRealType(getDataType()));
							}
						}
					};
				}
			}
			return null;
		}
	};
	
	protected String getDataType(){
		return MetadataServiceProvider.C_TYPE;
	}
	
	/**
	 * ����һ��ҳ���ʵ����
	 * Pageֻ��ȡ��Editor��Info�������Editor��Info����ֱ�Ӽ̳е�BizInterface�ӿڵĻ����õ���ϵķ�ʽ������ʱ�ͱ��봫��һ��EStructuredFeature��
	 * ��ӦInfo�������BizInterface���Ǹ�EMF����
	 * @param interfaceFeature 
	 * @param editor
	 * @param id
	 * @param title
	 */
	public InterfacePage(EStructuralFeature interfaceFeature, EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
		this.interfaceFeature = interfaceFeature;
		getEditingDomain().addResourceSetListener(trigger);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#doCreateFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void doCreateFormContent(IManagedForm managedForm) {
		Composite composite = managedForm.getForm().getBody();
		FormToolkit toolkit = managedForm.getToolkit();
		
		managedForm.getForm().setText(getTitle());
		toolkit.decorateFormHeading(managedForm.getForm().getForm());
		
		//composite.setLayout(new FillLayout());
		GridLayoutFactory.swtDefaults().applyTo(composite);
		Section inputSection = createInputSection(composite, toolkit);
		Section outputSection = createOuputSection(composite, toolkit);
		Section errorInfoSection = createErrorInfoSection(composite, toolkit);
		
		GridDataFactory.fillDefaults().grab(true, false).applyTo(inputSection);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(outputSection);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(errorInfoSection);
		
		composite.getParent().layout();
	}
	
	
	protected Section createInputSection(Composite composite, FormToolkit toolkit) {
		Section section = toolkit.createSection(composite, FormWidgetUtils.getDefaultSectionStyles());
		section.setText("�������");
		
		Composite content = toolkit.createComposite(section);
		GridLayoutFactory.fillDefaults().applyTo(content);
		
		// �Ƿ񼯺ϵı��
//		isInputCollectionBtn = toolkit.createButton(content, "���ݼ�", SWT.CHECK);
//		GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).grab(true, false).applyTo(isInputCollectionBtn);

		Composite block = toolkit.createComposite(content);
		GridDataFactory.fillDefaults().indent(0, 0).grab(true, true).applyTo(block);
		block.setLayout(new FillLayout());
		
		// �б�
		inputParamBlock = new InputParameterBlock(getEditingDomain(), getEditor().getARESResource(), getProblemPool());
		inputParamBlock.setEditableControl(getEditableControl());
		inputParamBlock.setDataType(getDataType());
		customizeInputParamBlock();
		inputParamBlock.createControl(block, toolkit);
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(inputParamBlock);
		
		addPropertyListener(inputParamBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(inputParamBlock);
		
		section.setClient(content);
		return section;
	}
	

	/***
	 * �������ඨ��һЩInputBlock������
	 */
	protected void customizeInputParamBlock() {
		
	}

	protected Section createOuputSection(Composite composite, FormToolkit toolkit) {
		Section section = toolkit.createSection(composite, FormWidgetUtils.getDefaultSectionStyles());
		section.setText("�������");
		
		Composite client = toolkit.createComposite(section);
		GridLayoutFactory.fillDefaults().applyTo(client);
		
		// �Ƿ񼯺ϵı��
//		isOutputCollectionBtn = toolkit.createButton(client, "���ݼ�", SWT.CHECK);
//		GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).grab(true, false).applyTo(isOutputCollectionBtn);
		
		Composite block = toolkit.createComposite(client);
		GridDataFactory.fillDefaults().indent(0, 0).grab(true, true).applyTo(block);
		block.setLayout(new FillLayout());

		outputParamBlock = new OutputParameterBlock(getEditingDomain(), getEditor().getARESResource(), getProblemPool());
		outputParamBlock.setEditableControl(getEditableControl());
		outputParamBlock.setDataType(getDataType());
		customizeOutputParamBlock();
		outputParamBlock.createControl(block, toolkit);
		
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(outputParamBlock);
		//GridDataFactory.fillDefaults().applyTo(outputParamBlock.getControl());
		
		addPropertyListener(outputParamBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(outputParamBlock);
		
		section.setClient(client);

		return section;		
	}

	/***
	 * ������������б���Ӱ�ť�������
	 */
	protected void customizeOutputParamBlock() {
		
	}
	
	protected Section createErrorInfoSection(Composite composite, FormToolkit toolkit) {
		Section section = toolkit.createSection(composite, FormWidgetUtils.getDefaultSectionStyles());
		section.setText("����˵��");
		section.setExpanded(false);
		
		Composite client = toolkit.createComposite(section);
		//GridLayoutFactory.fillDefaults().applyTo(client);
		client.setLayout(new FillLayout());
		
		errorInfoBlock = new ErrorInfoBlock(BizPackage.Literals.BIZ_INTERFACE__ERROR_INFOS, getEditingDomain(), getEditor().getARESResource(), getProblemPool());
		errorInfoBlock.setEditableControl(getEditableControl());
		errorInfoBlock.createControl(client, toolkit);
		
		addPropertyListener(errorInfoBlock);
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(errorInfoBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(errorInfoBlock);

		section.setClient(client);
		return section;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.DataBindingFormPage#doDataBingingOnControls()
	 */
	@Override
	protected void doDataBingingOnControls() {
		// ʹ�����ݰ�������
//		bingSelection(isOutputCollectionBtn, getInfo(),	BizPackage.Literals.BIZ_INTERFACE__OUTPUT_COLLECTION);
//		bingSelection(isInputCollectionBtn, getInfo(), BizPackage.Literals.BIZ_INTERFACE__INPUT_COLLECTION);
	}

	public void infoChange() {
		if ( inputParamBlock != null ) {
			inputParamBlock.setInput(getInterface());
		}
		if ( outputParamBlock != null ) {
			outputParamBlock.setInput(getInterface());
		}
		if (errorInfoBlock != null) {
			errorInfoBlock.setInput(getInterface());
		}
		super.infoChange();
	}
	
	protected BizInterface getInterface() {
		JRESResourceInfo info = getInfo();
		if(null == interfaceFeature) {
			return (BizInterface) info;
		}
		return (BizInterface) info.eGet(this.interfaceFeature);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#dispose()
	 */
	@Override
	public void dispose() {
		getEditingDomain().removeResourceSetListener(trigger);
		
		removePropertyListener(inputParamBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(inputParamBlock);
		
		removePropertyListener(outputParamBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(outputParamBlock);
		
		removePropertyListener(errorInfoBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(errorInfoBlock);

		super.dispose();
	}

}
