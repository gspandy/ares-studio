package com.hundsun.ares.studio.cres.extend.ui.project;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.hundsun.ares.studio.cres.extend.core.constants.ICresExtendConstants;
import com.hundsun.ares.studio.cres.extend.cresextend.CresProjectExtendProperty;
import com.hundsun.ares.studio.cres.extend.cresextend.CresextendFactory;
import com.hundsun.ares.studio.cres.extend.cresextend.CresextendPackage;
import com.hundsun.ares.studio.cres.extend.cresextend.FileDefine;
import com.hundsun.ares.studio.cres.extend.cresextend.GccDefine;
import com.hundsun.ares.studio.cres.extend.cresextend.MvcDefine;
import com.hundsun.ares.studio.cres.extend.cresextend.ProcDefine;
import com.hundsun.ares.studio.internal.core.ARESProjectProperty;
import com.hundsun.ares.studio.ui.editor.ProjectPropertyEditor;
import com.hundsun.ares.studio.ui.editor.blocks.EMFExtendSectionScrolledFormPage;
import com.hundsun.ares.studio.ui.editor.blocks.FormWidgetUtils;
import com.hundsun.ares.studio.ui.util.FormLayoutFactory;

public class CresProjectPropertyPage extends EMFExtendSectionScrolledFormPage<ARESProjectProperty> {

	private Text txtVersion;
	private Text txtCName;
	private Text txtShortCName;
	private Text txtID;
	private Text txtManager;
	private Text txtDeveloper;
	private Text txtUser;
	private Text txtRelation;
	private Text txtName;
	private Text txtWriter;
	private Text txtNote;
	private Text txtHeadFile;

	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public CresProjectPropertyPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		System.out.println(info);
		///
		ARESProjectProperty info1 = getInfo();
		System.out.println(info1);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.EMFExtendSectionScrolledFormPage#getEClass()
	 */
	@Override
	protected EClass getEClass() {
		return CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.EMFExtendSectionScrolledFormPage#getMapKey()
	 */
	@Override
	protected String getMapKey() {
		return ICresExtendConstants.CRES_EXTEND_PROJECT_PROPERTY;
	}

	protected SashForm sf;
	protected Section procSection;
	protected Section gccSection;
	protected Section mvcSection;
	protected Section func;
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.page.ExtendSectionScrolledFormPage#createSections(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createSections(IManagedForm managedForm) {

		Composite composite = managedForm.getForm().getBody();
		FormToolkit toolkit = managedForm.getToolkit();
		
		managedForm.getForm().setText(getTitle());
		toolkit.decorateFormHeading(managedForm.getForm().getForm());
		
		createBasicInfoSection(composite,toolkit,"������Ϣ");
		
		sf = new SashForm(composite, SWT.VERTICAL);
		TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
		twd.heightHint = 650;
		sf.setLayoutData(twd);
		sf.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
		
		createSection(toolkit);
		composite.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
		
		composite.getParent().layout();
	}

	protected void createSection(FormToolkit toolkit) {
		procSection = createFileDefineSection(sf, toolkit,"ProcԤ��������",
				CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__PROC_DEFINE,
				CresextendPackage.Literals.PROC_DEFINE);
		gccSection = createFileDefineSection(sf, toolkit,"GCC�ļ��Զ�������",
				CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__GCC_DEFINE,
				CresextendPackage.Literals.GCC_DEFINE);
		mvcSection = createFileDefineSection(sf,toolkit,"MVC�ļ��Զ�������",
				CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__MVC_DEFINE,
				CresextendPackage.Literals.MVC_DEFINE);
		func = createFuncDefineSection(sf,toolkit,"��������ͷ�ļ�����",
				CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__FUNC_DEFINE,
				CresextendPackage.Literals.FILE_DEFINE);
		
		sf.setWeights(new int[]{1,1,1,1});
		procSection.addExpansionListener(new ExpansionAdapter() {
			
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				refreshSFWeights();
			}
		});
		
		gccSection.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				refreshSFWeights();
			}
		});
		
		mvcSection.addExpansionListener(new ExpansionAdapter() {
			
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				refreshSFWeights();
			}
		});
		
		func.addExpansionListener(new ExpansionAdapter() {
			
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				refreshSFWeights();
			}
		});
	}

	private void refreshSFWeights(){
		int unit = 31;
		int shrinkNum = 0;
		if(procSection != null && !procSection.isExpanded()){
			shrinkNum ++;
		}
		if(!gccSection.isExpanded()){
			shrinkNum ++;
		}
		if(!mvcSection.isExpanded()){
			shrinkNum ++;
		}
		if(!func.isExpanded()){
			shrinkNum ++;
		}
		if(shrinkNum == 4){
			sf.setWeights(new int[]{1,1,1,unit-3});
			return;
		}
		int inputWeight = procSection.isExpanded() ? (unit-shrinkNum)/(4-shrinkNum) : 1;
		int outputWeight = gccSection.isExpanded() ? (unit-shrinkNum)/(4-shrinkNum) : 1;
		int internalWeight = mvcSection.isExpanded() ? (unit-shrinkNum)/(4-shrinkNum) : 1;
		int funcWeight = func.isExpanded() ? (unit-shrinkNum)/(4-shrinkNum) : 1;
		sf.setWeights(new int[]{inputWeight,outputWeight,internalWeight,funcWeight});
	}
	/**
	 * @param sf
	 * @param toolkit
	 * @param title
	 * @param reference
	 * @param procDefine
	 * @return
	 */
	protected Section createFileDefineSection(SashForm sf, FormToolkit toolkit,
			String title, EReference reference,
			EClass eclass) {
		Section section = toolkit.createSection(sf, FormWidgetUtils.getDefaultSectionStyles());
		section.setText(title);
		
		Composite content = toolkit.createComposite(section);
		GridLayoutFactory.fillDefaults().applyTo(content);
		
		Composite comp = toolkit.createComposite(content, SWT.NONE);
		GridDataFactory.fillDefaults().indent(0, 0).grab(true, true).applyTo(comp);
		comp.setLayout(new FillLayout());
		
		CresProjectFileDefineBlock block = new CresProjectFileDefineBlock(
				reference, 
				getEditingDomain(), resource, 
				eclass,null);
		
		block.createControl(comp, toolkit);
		block.setInput(getInfo());
		
		addPropertyListener(block);
		getEditingDomain().getCommandStack().addCommandStackListener(block);
		
		section.setClient(content);
		toolkit.paintBordersFor(content);
		
		return section;
	}
	
	/**
	 * @param sf
	 * @param toolkit
	 * @param title
	 * @param reference
	 * @param procDefine
	 * @return
	 */
	protected Section createFuncDefineSection(SashForm sf, FormToolkit toolkit,
			String title, EReference reference,
			EClass eclass) {
		Section section = toolkit.createSection(sf, FormWidgetUtils.getDefaultSectionStyles());
		section.setText(title);
		
		Composite content = toolkit.createComposite(section);
		GridLayoutFactory.fillDefaults().applyTo(content);
		
		Composite comp = toolkit.createComposite(content, SWT.NONE);
		GridDataFactory.fillDefaults().indent(0, 0).grab(true, true).applyTo(comp);
		comp.setLayout(new FillLayout());
		
		PublicFunctionDefineBlock block = new PublicFunctionDefineBlock(
				reference, 
				getEditingDomain(), 
				((ProjectPropertyEditor)getEditor()).getARESProject(), 
				eclass,null);
		
		block.createControl(comp, toolkit);
		block.setInput(getInfo());
		
		addPropertyListener(block);
		getEditingDomain().getCommandStack().addCommandStackListener(block);
		
		section.setClient(content);
		toolkit.paintBordersFor(content);
		
		return section;
	}

	/**
	 * @param composite
	 * @param toolkit
	 * @param title
	 */
	private void createBasicInfoSection(Composite composite,
			FormToolkit toolkit, String title) {
		
		Section section = toolkit.createSection(composite, FormWidgetUtils.getDefaultSectionStyles());
		section.setText(title);

		// �����ؼ�
		Composite baseInfo = toolkit.createComposite(section);

		Label lbVersion = toolkit.createLabel(baseInfo, "��Ʒ�汾��", SWT.NONE);
		txtVersion = toolkit.createText(baseInfo, "", SWT.BORDER);
		
		Label lbCName = toolkit.createLabel(baseInfo, "��Ʒ���ƣ�", SWT.NONE);
		txtCName = toolkit.createText(baseInfo, "", SWT.BORDER);
		
		Label lbShortCName = toolkit.createLabel(baseInfo, "��Ʒ��ƣ�", SWT.NONE);
		txtShortCName = toolkit.createText(baseInfo, "", SWT.BORDER);
		
		Label lbID = toolkit.createLabel(baseInfo, "��Ŀ��ţ�", SWT.NONE);
		txtID = toolkit.createText(baseInfo, "", SWT.BORDER);
		
		Label lbManager = toolkit.createLabel(baseInfo, "��������ߣ�", SWT.NONE);
		txtManager = toolkit.createText(baseInfo, "", SWT.BORDER);
		
		Label lbDeveloper = toolkit.createLabel(baseInfo, "�����ߣ�", SWT.NONE);
		txtDeveloper = toolkit.createText(baseInfo, "", SWT.BORDER);
		
		Label lbUser = toolkit.createLabel(baseInfo, "�û���", SWT.NONE);
		txtUser = toolkit.createText(baseInfo, "", SWT.BORDER);
		
		Label lbRelation = toolkit.createLabel(baseInfo, "ͬ����ϵͳ��ϵ��", SWT.NONE);
		txtRelation = toolkit.createText(baseInfo, "", SWT.BORDER);

		Label lbName = toolkit.createLabel(baseInfo, "Ӣ����д��", SWT.NONE);
		txtName = toolkit.createText(baseInfo, "", SWT.BORDER);

		Label lbWriter = toolkit.createLabel(baseInfo, "��д�ˣ�", SWT.NONE);
		txtWriter = toolkit.createText(baseInfo, "", SWT.BORDER);

		Label lbNote = toolkit.createLabel(baseInfo, "��д˵����", SWT.NONE);
		txtNote = toolkit.createText(baseInfo, "", SWT.BORDER);

		Label lbHeadFile = toolkit.createLabel(baseInfo, "�ļ�ͷע�ͣ�", SWT.NONE);
		txtHeadFile = toolkit.createText(baseInfo, "", SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		
		//����
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		baseInfo.setLayout(new GridLayout(2, false));
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbVersion);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(txtVersion);
		
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbCName);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(txtCName);
		
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbShortCName);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(txtShortCName);

		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbID);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(txtID);

		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbManager);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(txtManager);

		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbDeveloper);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(txtDeveloper);

		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbUser);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(txtUser);

		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbRelation);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(txtRelation);

		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbName);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(txtName);

		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbWriter);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(txtWriter);

		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbNote);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(txtNote);
		
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, false).span(1, 1).applyTo(lbHeadFile);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).hint(20, 80).grab(true, false).span(1, 1).applyTo(txtHeadFile);
		
		section.setClient(baseInfo);
		toolkit.paintBordersFor(baseInfo);
		
		databinding();
	}

	/**
	 * 
	 */
	private void databinding() {
		//��Ʒ�汾
		getBindingContext().bindValue(SWTObservables.observeText(txtVersion, SWT.Modify), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__VERSION));
		//��Ʒ����
		getBindingContext().bindValue(SWTObservables.observeText(txtCName, SWT.Modify), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__CNAME));
		//��Ʒ���
		getBindingContext().bindValue(SWTObservables.observeText(txtShortCName, SWT.Modify), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__SHORT_CNAME));
		//��Ŀ���
		getBindingContext().bindValue(SWTObservables.observeText(txtID, SWT.Modify), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__ID));
		//���������
		getBindingContext().bindValue(SWTObservables.observeText(txtManager, SWT.Modify), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__MANAGER));
		//������
		getBindingContext().bindValue(SWTObservables.observeText(txtDeveloper, SWT.Modify), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__DEVELOPER));
		//�û�
		getBindingContext().bindValue(SWTObservables.observeText(txtUser, SWT.Modify), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__USER));
		//ͬ����ϵͳ��ϵ
		getBindingContext().bindValue(SWTObservables.observeText(txtRelation, SWT.Modify), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__RELATION));
		//Ӣ�ļ��
		getBindingContext().bindValue(SWTObservables.observeText(txtName, SWT.Modify), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__NAME));
		//��д��
		getBindingContext().bindValue(SWTObservables.observeText(txtWriter, SWT.Modify), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__WRITER));
		//��д˵��
		getBindingContext().bindValue(SWTObservables.observeText(txtNote, SWT.Modify), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__NOTE));
		//�ļ�ͷע��
		getBindingContext().bindValue(SWTObservables.observeText(txtHeadFile, SWT.Modify), 
				EMFEditObservables.observeValue(getEditingDomain(), getModel(), 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__HEAD_FILE));
		
		////
		initInfo();
	}
	
	/**
	 * �����������Ϣ�󣬳�ʼ��ģ��
	 * @param owner
	 */
	protected void initInfo() {
		if(info instanceof ARESProjectProperty) {
			ARESProjectProperty pp = (ARESProjectProperty)info;
			CresProjectExtendProperty owner = (CresProjectExtendProperty)pp.getMap().get(ICresExtendConstants.CRES_EXTEND_PROJECT_PROPERTY);
			if(null == owner) {
				return;
			}
			//��ʼ��
			//gcc
			EList<GccDefine> gccDefine = owner.getGccDefine();
			if(gccDefine.isEmpty()) {
				List<GccDefine> tmpGcc = new ArrayList<GccDefine>();
				//PROC_INCLUDE
				GccDefine gcc1 = CresextendFactory.eINSTANCE.createGccDefine();
				gcc1.setIsUsed(true);
				gcc1.setParameter("PROC_INCLUDE");
				gcc1.setValue("$(ORACLE_HOME)/precomp/public," +
						"$(ORACLE_HOME)/oci/include,"+
						"$(FBASE_HOME),"+
						"include=/usr/lib/gcc-lib/i386-redhat-linux/$(GCC_VER)/include");
				gcc1.setNote("ProcԤ������������Ŀ¼���ö��Ÿ���");
				tmpGcc.add(gcc1);
				//CC_INCLUDE
				GccDefine gcc2 = CresextendFactory.eINSTANCE.createGccDefine();
				gcc2.setIsUsed(true);
				gcc2.setParameter("CC_INCLUDE");
				gcc2.setValue("$(FBASE_HOME),$(ORACLE_HOME)/precomp/public,$(ORACLE_HOME)/oci/include");
				gcc2.setNote("C/C++�������İ���Ŀ¼�����Ÿ���");
				tmpGcc.add(gcc2);
				//"LIBS"
				GccDefine gcc3 = CresextendFactory.eINSTANCE.createGccDefine();
				gcc3.setIsUsed(true);
				gcc3.setParameter("LIBS");
				gcc3.setValue("");
				gcc3.setNote("��Ҫ���ӵĵ��������ļ�,���Ÿ���");
				tmpGcc.add(gcc3);
				
				//"FC"
				GccDefine gcc4 = CresextendFactory.eINSTANCE.createGccDefine();
				gcc4.setIsUsed(true);
				gcc4.setParameter("FC");
				gcc4.setValue("");
				gcc4.setNote("Ҫ����������,���������obj�ļ������,���Ÿ���");
				tmpGcc.add(gcc4);
				
				//ʹ��Command���
				Command command = AddCommand.create(getEditingDomain(), owner, 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__GCC_DEFINE, tmpGcc);
				getEditingDomain().getCommandStack().execute(command);
			}
			//proc
			EList<ProcDefine> procDefine = owner.getProcDefine();
			if(procDefine.isEmpty()) {
				List<ProcDefine> tmpProc = new ArrayList<ProcDefine>();
				//ireclen
				ProcDefine proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("ireclen");
				proc.setValue("132");
				proc.setNote("");
				tmpProc.add(proc);
				//oreclen
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("oreclen");
				proc.setValue("132");
				proc.setNote("");
				tmpProc.add(proc);
				//auto_connect
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("auto_connect");
				proc.setValue("no");
				proc.setNote("�����Զ����ӵ� ops$ �ʻ�");
				tmpProc.add(proc);
				//char_map
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("char_map");
				proc.setValue("string");
				proc.setNote("����ӳ���ַ�������ַ���");
				tmpProc.add(proc);
				//close_on_commit
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("close_on_commit");
				proc.setValue("yes");
				proc.setNote("�ر����� COMMIT �α�");
				tmpProc.add(proc);
				//cmax
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("cmax");
				proc.setValue("100");
				proc.setNote("�������ӳص� CMAX ֵ 0 - 65535");
				tmpProc.add(proc);
				//cmin
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("cmin");
				proc.setValue("2");
				proc.setNote("�������ӳص� CMIN ֵ 1 - 65535");
				tmpProc.add(proc);
				//cincr
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("cincr");
				proc.setValue("1");
				proc.setNote("�������ӳص� CINCR ֵ 1 - 65535");
				tmpProc.add(proc);
				//ctimeout
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("ctimeout");
				proc.setValue("0");
				proc.setNote("�������ӳص� CTIMEOUT ֵ 1 - 65535");
				tmpProc.add(proc);
				//cnowait
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("cnowait");
				proc.setValue("0");
				proc.setNote("�������ӳص� CNOWAIT ֵ 1 - 65535");
				tmpProc.add(proc);
				//code
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("code");
				proc.setValue("cpp");
				proc.setNote("��Ҫ���ɵĴ�������");
				tmpProc.add(proc);
				//comp_charset
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("comp_charset");
				proc.setValue("multi_byte");
				proc.setNote("C ������֧�ֵ��ַ�������");
				tmpProc.add(proc);
				//config
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("config");
				proc.setValue("default");
				proc.setNote("ʹ����һ�����ļ�����ϵͳ�����ļ�");
				tmpProc.add(proc);
				//cpool
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("cpool");
				proc.setValue("no");
				proc.setNote("֧�����ӹ���");
				tmpProc.add(proc);
				//cpp_suffix
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("cpp_suffix");
				proc.setValue("cpp");
				proc.setNote("����Ĭ�ϵ� C++ �ļ�����׺");
				tmpProc.add(proc);
				//dbms
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("dbms");
				proc.setValue("native");
				proc.setNote("v6/v7/v8 ����ģʽ");
				tmpProc.add(proc);
				//def_sqlcode
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("def_sqlcode");
				proc.setValue("yes");
				proc.setNote("���� '#define SQLCODE sqlca.sqlcode' ��");
				tmpProc.add(proc);
				//define
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("define");
				proc.setValue("USE_PRO_C");
				proc.setNote("����Ԥ����������");
				tmpProc.add(proc);
				//duration
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("duration");
				proc.setValue("transaction");
				proc.setNote("transaction");
				tmpProc.add(proc);
				//dynamic
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("dynamic");
				proc.setValue("oracle");
				proc.setNote("ָ�� Oracle �� ANSI ��̬ SQL ����");
				tmpProc.add(proc);
				//errors
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("errors");
				proc.setValue("yes");
				proc.setNote("������Ϣ�Ƿ��͵��ն�");
				tmpProc.add(proc);
				//events
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("events");
				proc.setValue("no");
				proc.setNote("֧�ַ���-�����¼�֪ͨ");
				tmpProc.add(proc);
				//fips
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("fips");
				proc.setValue("none");
				proc.setNote("ANSI �������÷��� FIPS ��־");
				tmpProc.add(proc);
				//hold_cursor
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("hold_cursor");
				proc.setValue("yes");
				proc.setNote("�����α���ٻ����е��α������");
				tmpProc.add(proc);
				//lines
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("lines");
				proc.setValue("no");
				proc.setNote("�����ɵĴ������������ָ��");
				tmpProc.add(proc);
				//ltype
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("ltype");
				proc.setValue("short");
				proc.setNote("���б��ļ����ɵ�������");
				tmpProc.add(proc);
				//maxliteral
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("maxliteral");
				proc.setValue("1024");
				proc.setNote("���ɵ��ַ�������������󳤶� 10 - 1024");
				tmpProc.add(proc);
				//maxopencursors
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("maxopencursors");
				proc.setValue("60");
				proc.setNote("���ٻ���Ĵ��α���������");
				tmpProc.add(proc);
				//mode
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("mode");
				proc.setValue("ansi");
				proc.setNote("����� Oracle �� ANSI �����˳Ӧ��");
				tmpProc.add(proc);
				//native_types
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("native_types");
				proc.setValue("no");
				proc.setNote("��������/˫����֧��");
				tmpProc.add(proc);
				//nls_local
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("nls_local");
				proc.setValue("no");
				proc.setNote("���������� NLS �ַ�����");
				tmpProc.add(proc);
				//objects
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("objects");
				proc.setValue("yes");
				proc.setNote("֧�ֶ�������");
				tmpProc.add(proc);
				//oraca
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("oraca");
				proc.setValue("no");
				proc.setNote("���� ORACA ��ʹ��");
				tmpProc.add(proc);
				//pagelen
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("pagelen");
				proc.setValue("80");
				proc.setNote("�б��ļ���ҳ���� 30 - 256");
				tmpProc.add(proc);
				//parse
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("parse");
				proc.setValue("partial");
				proc.setNote("���ƶ���һ �� SQL ��������﷨����");
				tmpProc.add(proc);
				//prefetch
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("prefetch");
				proc.setValue("200");
				proc.setNote("���α� OPEN ʱԤ����ȡ������ 0 - 65535");
				tmpProc.add(proc);
				//release_cursor
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("release_cursor");
				proc.setValue("no");
				proc.setNote("���ƴ��α���ٻ����ͷŵ��α���");
				tmpProc.add(proc);
				//select_error
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("select_error");
				proc.setValue("yes");
				proc.setNote("����ѡ�����ı�־");
				tmpProc.add(proc);
				//sqlcheck
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("sqlcheck");
				proc.setValue("syntax");
				proc.setNote("����ʱ SQL �ļ����");
				tmpProc.add(proc);
				//threads
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("threads");
				proc.setValue("yes");
				proc.setNote("��ʾ���̵߳�Ӧ�ó���");
				tmpProc.add(proc);
				//type_code
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("type_code");
				proc.setValue("oracle");
				proc.setNote("ʹ�� Oracle ��̬ SQL �� ANSI ���ʹ���");
				tmpProc.add(proc);
				//unsafe_null
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("unsafe_null");
				proc.setValue("no");
				proc.setNote("����ʹ��ָʾ�����е� NULL ��ȡ");
				tmpProc.add(proc);
				//userid
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(true);
				proc.setParameter("userid");
				proc.setValue("hs_his/handsome@gfdb");
				proc.setNote("�û���/���� [@dbname] �����ַ���");
				tmpProc.add(proc);
				//utf16_charset
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("utf16_charset");
				proc.setValue("nchar_charset");
				proc.setNote("�� UTF16 ����ʹ�õ��ַ�����");
				tmpProc.add(proc);
				//varchar
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("varchar");
				proc.setValue("no");
				proc.setNote("����ʹ����ʽ varchar �ṹ");
				tmpProc.add(proc);
				//version
				proc = CresextendFactory.eINSTANCE.createProcDefine();
				proc.setIsUsed(false);
				proc.setParameter("version");
				proc.setValue("recent");
				proc.setNote("Ҫ������һ�汾�Ķ���");
				tmpProc.add(proc);
				
				//ʹ��Command���
				Command command = AddCommand.create(getEditingDomain(), owner, 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__PROC_DEFINE, tmpProc);
				getEditingDomain().getCommandStack().execute(command);
			}
			
			//mvc
			EList<MvcDefine> mvcDefine = owner.getMvcDefine();
			if(mvcDefine.isEmpty()) {
				List<MvcDefine> tmpMvc = new ArrayList<MvcDefine>();
				//"FBASE_HOME"
				MvcDefine mvc1 = CresextendFactory.eINSTANCE.createMvcDefine();
				mvc1.setIsUsed(true);
				mvc1.setParameter("FBASE_HOME");
				mvc1.setValue("../..");
				mvc1.setNote("�����õ�Ĭ��ֵΪ../..");
				tmpMvc.add(mvc1);
				//"OUTDIR"
				MvcDefine mvc2 = CresextendFactory.eINSTANCE.createMvcDefine();
				mvc2.setIsUsed(true);
				mvc2.setParameter("OUTDIR");
				mvc2.setValue("$(FBASE_HOME)\\Bin");
				mvc2.setNote("�����õ�Ĭ��ֵΪ$(FBASE_HOME)\\Bin");
				tmpMvc.add(mvc2);
				//"PROC_INCLUDE"
				mvc2 = CresextendFactory.eINSTANCE.createMvcDefine();
				mvc2.setIsUsed(true);
				mvc2.setParameter("PROC_INCLUDE");
				mvc2.setValue("$(ORACLE_HOME)/precomp/public," +
						"$(ORACLE_HOME)/oci/include,"+
						"$(FBASE_HOME),"+
						"$(VC_HOME)");
				mvc2.setNote("ProcԤ������������Ŀ¼���ö��Ÿ���");
				tmpMvc.add(mvc2);
				//"CC_INCLUDE"
				mvc2 = CresextendFactory.eINSTANCE.createMvcDefine();
				mvc2.setIsUsed(true);
				mvc2.setParameter("CC_INCLUDE");
				mvc2.setValue("$(FBASE_HOME),$(ORACLE_HOME)/precomp/public,$(ORACLE_HOME)/oci/include");
				mvc2.setNote("C/C++�������İ���Ŀ¼�����Ÿ���");
				tmpMvc.add(mvc2);
				//"LIBS"
				mvc2 = CresextendFactory.eINSTANCE.createMvcDefine();
				mvc2.setIsUsed(true);
				mvc2.setParameter("LIBS");
				mvc2.setValue("");
				mvc2.setNote("��Ҫ���ӵĵ��������ļ�,���Ÿ���");
				tmpMvc.add(mvc2);
				
				//ʹ��Command���
				Command command = AddCommand.create(getEditingDomain(), owner, 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__MVC_DEFINE, tmpMvc);
				getEditingDomain().getCommandStack().execute(command);
			}
			
			EList<FileDefine> funcDefine = owner.getFuncDefine();
			if(funcDefine.isEmpty()){
				FileDefine define = CresextendFactory.eINSTANCE.createFileDefine();
				define.setIsUsed(true);
				define.setValue("src\\s_libpublic.h");
				define.setNote("ϵͳ��������");
				//ʹ��Command���
				Command command = AddCommand.create(getEditingDomain(), owner, 
						CresextendPackage.Literals.CRES_PROJECT_EXTEND_PROPERTY__FUNC_DEFINE, define);
				getEditingDomain().getCommandStack().execute(command);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.page.ExtendPageWithMyDirtySystem#shouldLoad()
	 */
	@Override
	public boolean shouldLoad() {
		
		IProject project = getARESProject().getProject();
		try {
			if(project.hasNature(ICresExtendConstants.CRES_PROJECT_NATURE)){
				return true;
			}
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

}
