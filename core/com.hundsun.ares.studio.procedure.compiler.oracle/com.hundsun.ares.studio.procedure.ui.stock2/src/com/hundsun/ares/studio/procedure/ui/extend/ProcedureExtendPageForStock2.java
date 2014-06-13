package com.hundsun.ares.studio.procedure.ui.extend;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.hundsun.ares.studio.procdure.ProcdurePackage;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.EMFExtendSectionScrolledFormPage;
import com.hundsun.ares.studio.ui.editor.blocks.FormWidgetUtils;
import com.hundsun.ares.studio.ui.util.FormLayoutFactory;

public class ProcedureExtendPageForStock2 extends EMFExtendSectionScrolledFormPage<Procedure> {

	public static final String key = "procedureExtendPage";
	
	private int[] weights = new int[]{15,15};

	private Text returnType;
	
	private Text createDate;
	
	private Combo bizType;

	private Combo DefineType;

	private Text beginCode;

	private Text endCode;
	
	//��������
	private Button autoTransction;
	
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public ProcedureExtendPageForStock2(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	@Override
	public boolean shouldLoad() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.EMFExtendSectionScrolledFormPage#getEClass()
	 */
	@Override
	protected EClass getEClass() {
		return ProcdurePackage.Literals.PROCEDURE;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.EMFExtendSectionScrolledFormPage#getMapKey()
	 */
	@Override
	protected String getMapKey() {
		return key;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.page.ExtendSectionScrolledFormPage#createSections(com.hundsun.ares.studio.ui.page.IManagedForm)
	 */
	@Override
	protected void createSections(IManagedForm managedForm) {
		Composite composite = managedForm.getForm().getBody();
		FormToolkit toolkit = managedForm.getToolkit();
		
		managedForm.getForm().setText(getTitle());
		toolkit.decorateFormHeading(managedForm.getForm().getForm());
		
		createBasicInfoSection(composite,toolkit);
		
		final SashForm sf = new SashForm(composite, SWT.VERTICAL);
		TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
		twd.heightHint = 650;
		sf.setLayoutData(twd);
		sf.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
		
		final Section before = createBeforeCodeSection(sf, toolkit);
		final Section after = createAfterCodeSection(sf, toolkit);
		
		//databinding
		databinding();
		
		sf.setWeights(weights);
		final int unit = weights[0];
		
		before.addExpansionListener(new IExpansionListener() {
			
			@Override
			public void expansionStateChanging(ExpansionEvent e) {
			}
			
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				int[] weights = sf.getWeights();
				if(e.getState()){
					if( !after.isExpanded()){
						//չ��ǰ��������ڲ���Ϊ��չ��״̬����ʱ�ڲ�ռ�õĿռ�Ϊչ��ʱ�Ĵ�С
						sf.setWeights(new int[]{weights[0]*unit,weights[1],weights[2]/unit});
					}else{
						//������ռ�ô�С��Ϊչ��״̬
						sf.setWeights(new int[]{weights[0]*unit,weights[1],weights[2]});
					}
				}else if(!before.isExpanded() && !after.isExpanded() ){
					//��������ڲ�Ϊ��չ��״̬�����ڲ�ռ�ô�С����Ϊչ��ʱ�Ĵ�С
					sf.setWeights(new int[]{1,1,unit});
				}else{
					//������ռ�ô�С��Ϊ��չ��״̬
					sf.setWeights(new int[]{weights[0]/unit,weights[1],weights[2]});
				}
			}
		});
		
		after.addExpansionListener(new IExpansionListener() {
			
			@Override
			public void expansionStateChanging(ExpansionEvent e) {
			}
			
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				int[] weights = sf.getWeights();
				if(e.getState()){
					if(!before.isExpanded() ){
						sf.setWeights(new int[]{weights[0],weights[1]*unit,weights[2]/unit});
					}else{
						sf.setWeights(new int[]{weights[0],weights[1]*unit,weights[2]});
					}
				}else if(!before.isExpanded() && !after.isExpanded() ){
					sf.setWeights(new int[]{1,1,unit});
				}else{
					sf.setWeights(new int[]{weights[0],weights[1]/unit,weights[2]});
				}
			}
		});
		composite.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
		
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
		
		Label lbID = toolkit.createLabel(client, "�������ͣ�");
		returnType = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
		
		//��������
		Label lbCreateDate = toolkit.createLabel(client, "�������ڣ�");
		createDate = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles()|SWT.COLOR_GRAY);
		
		//ҵ������
		Label lbBizType = toolkit.createLabel(client, "ҵ�����ͣ�");
		bizType = new Combo(client, SWT.BORDER);
		
		//��ȡҵ������
		EList<EEnumLiteral> bizTypes = ProcdurePackage.Literals.BIZ_TYPE.getELiterals();
		List<String> bizItems = new ArrayList<String>();
		for (EEnumLiteral eEnumLiteral : bizTypes) {
			bizItems.add(eEnumLiteral.getName());
		}
		bizType.setItems(bizItems.toArray(new String[bizItems.size()]));
		
		//��������
		Label lbDefineType = toolkit.createLabel(client, "�������ͣ�");
		DefineType = new Combo(client, SWT.BORDER);
		
		//��ȡ��������
		EList<EEnumLiteral> defineTypes = ProcdurePackage.Literals.DEFINE_TYPE.getELiterals();
		List<String> items = new ArrayList<String>();
		for (EEnumLiteral eEnumLiteral : defineTypes) {
			items.add(eEnumLiteral.getName());
		}
		DefineType.setItems(items.toArray(new String[items.size()]));
		
		//��������
		autoTransction = toolkit.createButton(client, "��������", SWT.CHECK);
		
		// ֻ������
		createDate.setEditable(false);
//		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(returnType));
		
		// ����
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		GridLayoutFactory.swtDefaults().numColumns(4).applyTo(client);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbID);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(returnType);
		
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbCreateDate);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(createDate);
		
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbBizType);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(bizType);
		
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbDefineType);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(DefineType);
		
		GridDataFactory.fillDefaults().grab(false, false).applyTo(autoTransction);
		
		section.setClient(client);
		return section;
	}
	
	//ǰ�ô���section
	protected Section createBeforeCodeSection(Composite composite,
			FormToolkit toolkit) {
		Section section = toolkit.createSection(composite, FormWidgetUtils.getDefaultSectionStyles());
		section.setText("ǰ�ô���");
		section.setExpanded(true);
		
		// �����ؼ�
		Composite client = toolkit.createComposite(section);
		
		Label lbID = toolkit.createLabel(client, "ǰ�ô��룺");
		beginCode = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultMultiLinesTextStyles() | SWT.V_SCROLL);
		
		// ֻ������
//		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(returnType));
		
		// ����
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(client);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbID);
		
		GridDataFactory.fillDefaults().grab(true, true).hint(10, 50).applyTo(beginCode);
		
		section.setClient(client);
		return section;
	}
	
	//���ô���section
	protected Section createAfterCodeSection(Composite composite,
			FormToolkit toolkit) {
		Section section = toolkit.createSection(composite, FormWidgetUtils.getDefaultSectionStyles());
		section.setText("���ô���");
		section.setExpanded(true);
		
		// �����ؼ�
		Composite client = toolkit.createComposite(section);
		
		Label lbID = toolkit.createLabel(client, "���ô��룺");
		endCode = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultMultiLinesTextStyles() | SWT.V_SCROLL);
		
		
		// ֻ������
//		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(returnType));
		
		// ����
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(client);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbID);
		
		GridDataFactory.fillDefaults().grab(true, true).hint(10, 50).applyTo(endCode);
		
		section.setClient(client);
		return section;
	}
	
	/**
	 * ���ݰ�
	 */
	private void databinding() {
		
		//��������
		getBindingContext().bindValue(SWTObservables.observeText(returnType, SWT.Modify), 
				EMFEditObservables.observeValue(((EMFFormEditor)getEditor()).getEditingDomain(),
						getInfo(), 
						ProcdurePackage.Literals.PROCEDURE__RETURN_TYPE));
		
		//createDate
		getBindingContext().bindValue(SWTObservables.observeText(createDate, SWT.Modify), 
				EMFEditObservables.observeValue(((EMFFormEditor)getEditor()).getEditingDomain(),
						getInfo(), 
						ProcdurePackage.Literals.PROCEDURE__CREATE_DATE));
		
		//ҵ������
		getBindingContext().bindValue(
				SWTObservables.observeSelection(bizType), 
				EMFEditObservables.observeValue(((EMFFormEditor)getEditor()).getEditingDomain(), getInfo(), 
						ProcdurePackage.Literals.PROCEDURE__BIZ_TYPE));

		//��������
		getBindingContext().bindValue(
				SWTObservables.observeSelection(DefineType), 
				EMFEditObservables.observeValue(((EMFFormEditor)getEditor()).getEditingDomain(), getInfo(), 
						ProcdurePackage.Literals.PROCEDURE__DEFINE_TYPE));
		
		//��������
		getBindingContext().bindValue(SWTObservables.observeSelection(autoTransction), 
				EMFEditObservables.observeValue(((EMFFormEditor)getEditor()).getEditingDomain(), getInfo(), 
						ProcdurePackage.Literals.PROCEDURE__AUTO_TRANSACTION));
		
		//ǰ�ô���
		getBindingContext().bindValue(SWTObservables.observeText(beginCode, SWT.Modify), 
				EMFEditObservables.observeValue(((EMFFormEditor)getEditor()).getEditingDomain(), getInfo(), 
						ProcdurePackage.Literals.PROCEDURE__BEGIN_CODE));

		//���ô���
		getBindingContext().bindValue(SWTObservables.observeText(endCode, SWT.Modify), 
				EMFEditObservables.observeValue(((EMFFormEditor)getEditor()).getEditingDomain(), getInfo(), 
						ProcdurePackage.Literals.PROCEDURE__END_CODE));
	}
}
