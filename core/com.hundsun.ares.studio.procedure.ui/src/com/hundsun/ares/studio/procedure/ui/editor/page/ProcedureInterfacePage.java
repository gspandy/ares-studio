package com.hundsun.ares.studio.procedure.ui.editor.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.ui.action.IBizActionIDConstants;
import com.hundsun.ares.studio.biz.ui.block.InputParameterBlock;
import com.hundsun.ares.studio.biz.ui.block.OutputParameterBlock;
import com.hundsun.ares.studio.biz.ui.block.ParameterDefineEditingSupportDecorator;
import com.hundsun.ares.studio.biz.ui.editor.page.InterfacePage;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.model.util.RevisionHistoryUtil;
import com.hundsun.ares.studio.jres.database.oracle.constant.IOracleRefType;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalHelperWipeOffRepeatStd;
import com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalProvider;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpace;
import com.hundsun.ares.studio.jres.model.database.oracle.util.OracleDataBaseUtil;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.procdure.ProcdurePackage;
import com.hundsun.ares.studio.procdure.constants.IProcedureResType;
import com.hundsun.ares.studio.procedure.ui.editor.page.block.InternalVarParameterBlock;
import com.hundsun.ares.studio.procedure.ui.editor.page.block.ProcedureInterfaceParameterColLabProvider;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.IDiagnosticProvider;
import com.hundsun.ares.studio.ui.editor.blocks.FormWidgetUtils;
import com.hundsun.ares.studio.ui.editor.editable.JresDefaultEditableUnit;
import com.hundsun.ares.studio.ui.editor.editingsupport.JresTextEditingSupportWithContentAssist;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;

public class ProcedureInterfacePage extends InterfacePage {

	private InternalVarParameterBlock varParamBlock;
	/**
	 * ���ܺ�
	 */
	Text objectID;
	/**
	 * �汾��
	 */
	Text txtVersion;
	/**
	 * ����ʱ��
	 */
	Text txtUpdateTime;
	/**
	 * ����
	 */
	Text txtName;
	/**
	 *������
	 */
	Text txtCName;
	/**
	 * �ӿڱ�־
	 */
	Text txtFlag;
	/**
	 * ����
	 */
	Text txtDescription;
	
	/**
	 * ���ݿ�
	 */
	Combo comboCurTable;
//	Text txtDatabase;
	
	/**
	 * ���������
	 */
	Button btnResultSetReturn;

	private int[] weights = new int[]{15,15,15};
	
	public ProcedureInterfacePage(EStructuralFeature interfaceFeature,
			EMFFormEditor editor, String id, String title) {
		super(interfaceFeature, editor, id, title);
	}
	
	@Override
	protected void doCreateFormContent(IManagedForm managedForm) {
		Composite composite = managedForm.getForm().getBody();
		GridLayoutFactory.swtDefaults().applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(composite);
		FormToolkit toolkit = managedForm.getToolkit();
		
		managedForm.getForm().setText(getTitle());
		toolkit.decorateFormHeading(managedForm.getForm().getForm());
		
		createBasicInfoSection(composite,toolkit);
		
		final SashForm sf = new SashForm(composite, SWT.VERTICAL);
		GridLayoutFactory.swtDefaults().applyTo(sf);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(sf);
		
		final Section input = createInputSection(sf, toolkit);
		final Section output = createOuputSection(sf, toolkit);
		final Section internal = createInternalSection(sf,toolkit); 
		
		sf.setWeights(weights);
		final int unit = weights[0];
		
		input.addExpansionListener(new IExpansionListener() {
			
			@Override
			public void expansionStateChanging(ExpansionEvent e) {
			}
			
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				int[] weights = sf.getWeights();
				if(e.getState()){
					if(!internal.isExpanded() && !output.isExpanded()){
						//չ��ǰ��������ڲ���Ϊ��չ��״̬����ʱ�ڲ�ռ�õĿռ�Ϊչ��ʱ�Ĵ�С
						sf.setWeights(new int[]{weights[0]*unit,weights[1],weights[2]/unit});
					}else{
						//������ռ�ô�С��Ϊչ��״̬
						sf.setWeights(new int[]{weights[0]*unit,weights[1],weights[2]});
					}
				}else if(!input.isExpanded() && !output.isExpanded() && !internal.isExpanded()){
					//��������ڲ�Ϊ��չ��״̬�����ڲ�ռ�ô�С����Ϊչ��ʱ�Ĵ�С
					sf.setWeights(new int[]{1,1,unit});
				}else{
					//������ռ�ô�С��Ϊ��չ��״̬
					sf.setWeights(new int[]{weights[0]/unit,weights[1],weights[2]});
				}
			}
		});
		
		output.addExpansionListener(new IExpansionListener() {
			
			@Override
			public void expansionStateChanging(ExpansionEvent e) {
			}
			
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				int[] weights = sf.getWeights();
				if(e.getState()){
					if(!input.isExpanded() && !internal.isExpanded()){
						sf.setWeights(new int[]{weights[0],weights[1]*unit,weights[2]/unit});
					}else{
						sf.setWeights(new int[]{weights[0],weights[1]*unit,weights[2]});
					}
				}else if(!input.isExpanded() && !output.isExpanded() && !internal.isExpanded()){
					sf.setWeights(new int[]{1,1,unit});
				}else{
					sf.setWeights(new int[]{weights[0],weights[1]/unit,weights[2]});
				}
			}
		});
		
		internal.addExpansionListener(new IExpansionListener() {
			
			@Override
			public void expansionStateChanging(ExpansionEvent e) {
			}
			
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				int[] weights = sf.getWeights();
				if(e.getState()){
					if(!input.isExpanded() && !output.isExpanded()){
						sf.setWeights(weights);
					}else{
						sf.setWeights(new int[]{weights[0],weights[1],weights[2]*unit});
					}
				}else if(!input.isExpanded() && !output.isExpanded() && !internal.isExpanded()){
					sf.setWeights(new int[]{1,1,unit});
				}else{
					sf.setWeights(new int[]{weights[0],weights[1],weights[2]/unit});
				}
			}
		});
		
		
		composite.getParent().layout();
	}
	
	//������Ϣsection
	protected Section createBasicInfoSection(Composite composite,
			FormToolkit toolkit) {
		Section section = toolkit.createSection(composite, FormWidgetUtils.getDefaultSectionStyles());
		section.setText("������Ϣ");
		section.setExpanded(true);
		
		// �����ؼ�
		Composite client = toolkit.createComposite(section);
		
		Label lbID = toolkit.createLabel(client, "���ܺţ�");
		objectID = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
		
		Label lbName = toolkit.createLabel(client, "���ƣ�");
		txtName = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
		
		Label lbCName = toolkit.createLabel(client, "��������");
		txtCName = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());

		Label lbVersion = toolkit.createLabel(client, "�汾�ţ�");
		txtVersion = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
		
		Label lbUpdateTome = toolkit.createLabel(client, "����ʱ�䣺");
		txtUpdateTime = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
		
		Label lbDatabase = toolkit.createLabel(client, "���ݿ⣺");
		comboCurTable = new Combo(client, SWT.DROP_DOWN | SWT.BORDER);
		comboCurTable.setItems(OracleDataBaseUtil.getDataBaseName(getEditor().getARESResource().getARESProject(),true));
		
		Label lbFlag = toolkit.createLabel(client, "�ӿڱ�־��");
		txtFlag = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
		
		Label lbDescription = toolkit.createLabel(client, "˵����");
		txtDescription = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultMultiLinesTextStyles());

		Label lbResultSetReturn = toolkit.createLabel(client, "��������أ�");
		btnResultSetReturn = toolkit.createButton(client, "", SWT.CHECK);
		
		{
			//�ҳ����µİ汾��
			RevisionHistory his = RevisionHistoryUtil.getMaxVersionHisInfo(getEditor().getARESResource().getModule(),IProcedureResType.PROCEDURE);
			if (his != null) {
				txtVersion.setText(his.getVersion());
				txtUpdateTime.setText(his.getModifiedDate());
			}else {
				his = RevisionHistoryUtil.getMaxVersionHisInfo(getEditor().getARESResource().getModule());
				if (his != null) {
					txtVersion.setText(his.getVersion());
					txtUpdateTime.setText(his.getModifiedDate());
				}else {
					String projectVersion = RevisionHistoryUtil.getProjectPropertyVersion(getEditor().getARESResource().getARESProject());
					txtVersion.setText(projectVersion);
				}
			}
		}
		
		// ֻ������
		txtName.setEditable(false);
		txtUpdateTime.setEditable(false);
		txtVersion.setEditable(false);
		txtName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		txtUpdateTime.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		txtVersion.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtCName));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtDescription));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(objectID));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtFlag));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(comboCurTable));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(btnResultSetReturn));
		
		// ����
		GridLayoutFactory.swtDefaults().applyTo(section);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(section);
		GridLayoutFactory.swtDefaults().numColumns(4).applyTo(client);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbID);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbVersion);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbFlag);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbResultSetReturn);
		
		GridDataFactory.fillDefaults().grab(true, true).applyTo(objectID);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(txtVersion);
		GridDataFactory.fillDefaults().span(3, 1).grab(true, true).grab(true, true).applyTo(txtFlag);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(btnResultSetReturn);
		
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbName);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbCName);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbDescription);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbUpdateTome);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbDatabase);
		
		GridDataFactory.fillDefaults().grab(true, true).applyTo(txtName);
		GridDataFactory.fillDefaults().grab(true, true).hint(10, SWT.DEFAULT).applyTo(txtCName);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(txtUpdateTime);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(comboCurTable);
		GridDataFactory.fillDefaults().span(3, 1).grab(true, true).hint(10, 50).applyTo(txtDescription);
		
		section.setClient(client);
		return section;
	}
	
	protected Section createInputSection(Composite composite, FormToolkit toolkit) {
		Section section = toolkit.createSection(composite, FormWidgetUtils.getDefaultSectionStyles());
		section.setText("�������");
		
		Composite content = toolkit.createComposite(section);
		GridLayoutFactory.fillDefaults().applyTo(content);
		
		Composite block = toolkit.createComposite(content);
		GridDataFactory.fillDefaults().indent(0, 0).grab(true, true).applyTo(block);
		block.setLayout(new FillLayout());
		
		// �б�
		inputParamBlock = new InputParameterBlock(getEditingDomain(), getEditor().getARESResource(), getProblemPool()){
			@Override
			protected void createColumnDefaultValue(IARESProject project,
					TreeViewer viewer, IDiagnosticProvider problemView) {
				// ����������
				EAttribute attribute = BizPackage.Literals.PARAMETER__DEFAULT_VALUE;
				
				// ���������
				TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
				column.getColumn().setText("Ĭ��ֵ");
				column.getColumn().setWidth(100);
				column.getColumn().setMoveable(true);
				
				// ���ñ�ǩ�ṩ��
				EObjectColumnLabelProvider provider = new ProcedureInterfaceParameterColLabProvider(resource,attribute);
				provider.setDiagnosticProvider(problemView);
				column.setLabelProvider(provider);
				
				// ���ñ༭֧��
				MetadataContentProposalHelperWipeOffRepeatStd helper = new MetadataContentProposalHelperWipeOffRepeatStd(resource.getARESProject());
				MetadataContentProposalProvider proposalProvider = new MetadataContentProposalProvider(helper, IMetadataRefType.DefValue, resource.getARESProject());
				
				JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(
						viewer,
						attribute, 
						proposalProvider);
				es.setDecorator(new ParameterDefineEditingSupportDecorator(project, attribute));
				column.setEditingSupport(es);
			}
		};
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
	
	protected Section createOuputSection(Composite composite, FormToolkit toolkit) {
		Section section = toolkit.createSection(composite, FormWidgetUtils.getDefaultSectionStyles());
		section.setText("�������");
		
		Composite client = toolkit.createComposite(section);
		GridLayoutFactory.fillDefaults().applyTo(client);
		
		Composite block = toolkit.createComposite(client);
		GridDataFactory.fillDefaults().indent(0, 0).grab(true, true).applyTo(block);
		block.setLayout(new FillLayout());

		outputParamBlock = new OutputParameterBlock(getEditingDomain(), getEditor().getARESResource(), getProblemPool()){
			@Override
			protected void createColumnDefaultValue(IARESProject project,
					TreeViewer viewer, IDiagnosticProvider problemView) {
				// ����������
				EAttribute attribute = BizPackage.Literals.PARAMETER__DEFAULT_VALUE;
				
				// ���������
				TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
				column.getColumn().setText("Ĭ��ֵ");
				column.getColumn().setWidth(100);
				column.getColumn().setMoveable(true);
				
				// ���ñ�ǩ�ṩ��
				EObjectColumnLabelProvider provider = new ProcedureInterfaceParameterColLabProvider(resource,attribute);
				provider.setDiagnosticProvider(problemView);
				column.setLabelProvider(provider);
				
				// ���ñ༭֧��
				MetadataContentProposalHelperWipeOffRepeatStd helper = new MetadataContentProposalHelperWipeOffRepeatStd(resource.getARESProject());
				MetadataContentProposalProvider proposalProvider = new MetadataContentProposalProvider(helper, IMetadataRefType.DefValue, resource.getARESProject());
				
				JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(
						viewer,
						attribute, 
						proposalProvider);
				es.setDecorator(new ParameterDefineEditingSupportDecorator(project, attribute));
				column.setEditingSupport(es);
			}
		};
		outputParamBlock.setEditableControl(getEditableControl());
		outputParamBlock.setDataType(getDataType());
		customizeOutputParamBlock();
		outputParamBlock.createControl(block, toolkit);
		
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(outputParamBlock);
		
		addPropertyListener(outputParamBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(outputParamBlock);
		
		section.setClient(client);

		return section;		
	}
	
	//�ڲ�����section
	protected Section createInternalSection(Composite composite,
			FormToolkit toolkit) {
		Section section = toolkit.createSection(composite, FormWidgetUtils.getDefaultSectionStyles());
		section.setText("�ڲ�����");
		
		Composite content = toolkit.createComposite(section);
		GridLayoutFactory.fillDefaults().applyTo(content);
		
		Composite block = toolkit.createComposite(content);
		GridDataFactory.fillDefaults().indent(0, 0).grab(true, true).applyTo(block);
		block.setLayout(new FillLayout());
		
		// �б�
		varParamBlock = new InternalVarParameterBlock(getEditingDomain(), getEditor().getARESResource(), getProblemPool());
		varParamBlock.setEditableControl(getEditableControl());
		varParamBlock.setDataType(getDataType());
		//������Ӱ�ť
		varParamBlock.setAddActionIds(new String[] {IBizActionIDConstants.CV_ADD,
				   IBizActionIDConstants.ADD_NON_STD_FIELD_PARME,
				   IBizActionIDConstants.ADD_OBJECT_PARAM});
		varParamBlock.createControl(block, toolkit);
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(varParamBlock);
		
		addPropertyListener(varParamBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(varParamBlock);
		
		section.setClient(content);
		return section;
	}
	
	@Override
	protected void customizeInputParamBlock() {
		inputParamBlock.setAddActionIds(new String[] {IBizActionIDConstants.CV_ADD ,IBizActionIDConstants.ADD_PARAM_GROUP});
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.ui.editor.page.InterfacePage#initOutputBlockAction()
	 */
	@Override
	protected void customizeOutputParamBlock() {
		outputParamBlock.setAddActionIds(new String[] {IBizActionIDConstants.CV_ADD ,IBizActionIDConstants.ADD_PARAM_GROUP});
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.DataBindingFormPage#infoChange()
	 */
	@Override
	public void infoChange() {
		
		if ( varParamBlock != null ) {
			varParamBlock.setInput(getInfo());
		}
		super.infoChange();
	}
	
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.DataBindingFormPage#dispose()
	 */
	@Override
	public void dispose() {
		removePropertyListener(varParamBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(varParamBlock);
		
		super.dispose();
	}
	
	@Override
	protected void doDataBingingOnControls() {
		super.doDataBingingOnControls();
		bingText(objectID, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__OBJECT_ID);
		bingText(txtName, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__NAME);
		bingText(txtCName, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME);
		bingText(txtFlag, getInfo(), BizPackage.Literals.BIZ_INTERFACE__INTERFACE_FLAG);
		bingText(txtDescription, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__DESCRIPTION);
		bingSelection(comboCurTable, getInfo(),ProcdurePackage.Literals.PROCEDURE__DATABASE);
		
		bingSelection(btnResultSetReturn, getInfo(), BizPackage.Literals.BIZ_INTERFACE__OUTPUT_COLLECTION);
	}

}
