package com.hundsun.ares.studio.biz.ui.block;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.core.BizUtil;
import com.hundsun.ares.studio.biz.provider.ParameterColumnLabelProvider;
import com.hundsun.ares.studio.biz.ui.BizUIConstants;
import com.hundsun.ares.studio.biz.ui.StdObjContentPorposalHelper;
import com.hundsun.ares.studio.biz.ui.action.AddParameterAction;
import com.hundsun.ares.studio.biz.ui.action.AddParmaActionGroup;
import com.hundsun.ares.studio.biz.ui.action.AddToStdFieldAction;
import com.hundsun.ares.studio.biz.ui.action.IBizActionIDConstants;
import com.hundsun.ares.studio.biz.ui.action.ParamLinkOpenObjectAction;
import com.hundsun.ares.studio.biz.ui.action.ParameterPasteAction;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESProjectProperty;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.metadata.ui.Language;
import com.hundsun.ares.studio.jres.metadata.ui.LanguageRegister;
import com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalHelperWipeOffRepeatStd;
import com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalProvider;
import com.hundsun.ares.studio.jres.model.metadata.provider.LongTextEditingSupport;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.ui.assist.CompositeProposalHelper;
import com.hundsun.ares.studio.ui.editor.IDiagnosticProvider;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerCopyAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerDeleteAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerInsertAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerMoveBottomAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerMoveDownAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerMoveTopAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerMoveUpAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerPasteAction;
import com.hundsun.ares.studio.ui.editor.actions.CopyCellAction;
import com.hundsun.ares.studio.ui.editor.actions.CopyColumnAction;
import com.hundsun.ares.studio.ui.editor.actions.IActionIDConstant;
import com.hundsun.ares.studio.ui.editor.blocks.TreeViewerBlock;
import com.hundsun.ares.studio.ui.editor.editable.ActionEditableUnit;
import com.hundsun.ares.studio.ui.editor.editingsupport.EnumEditingSupport;
import com.hundsun.ares.studio.ui.editor.editingsupport.JresTextEditingSupportWithContentAssist;
import com.hundsun.ares.studio.ui.editor.editingsupport.TextEditingSupport;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelColumnViewerProblemView;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelUtils;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnViewerProblemView;
import com.hundsun.ares.studio.ui.util.ARESUIUtil;
import com.hundsun.ares.studio.ui.validate.IProblemPool;

/**
 * ���������Block
 * @author sundl
 *
 */
public abstract class ParameterViewerBlock extends TreeViewerBlock{

	/** Ĭ����������Action��ID�б�������ӱ�׼�ֶβ�������ӷǱ�׼�ֶβ�������Ӷ��������... 
	 *  Ĭ��ȫ������
	 */
	public static final String[] DEFAULT_ADD_ACTION_IDS = 
			new String[] {IBizActionIDConstants.CV_ADD,
						   IBizActionIDConstants.ADD_NON_STD_FIELD_PARME,
						   IBizActionIDConstants.ADD_OBJECT_PARAM,
						   IBizActionIDConstants.ADD_PARAM_GROUP
						   };
	
	// ��־λ���壬����ȷ���Ƿ�Ҫ������Ӧ����
	/** ���á���־λ���� */
	public static final int COLUMN_FLAG = 1 << 1;
	/** ���� */
	public static final int COLUMN_ID = 1 << 2;
	/** ������ */
	public static final int COLUMN_CHINESE_NAME = 1 << 3;
	/** ҵ���������� */
	public static final int COLUMN_BIZ_TYPE = 1 << 4;
	/** ��ʵ�������� */
	public static final int COLUMN_REAL_TYPE = 1 << 5;
	/** ���� */
	public static final int COLUMN_MULTIPLICITY = 1 << 6;
	/** Ĭ��ֵ */
	public static final int COLUMN_DEFAULT_VALUE = 1 << 7;
	/** ˵�� */
	public static final int COLUMN_DESCRIPTION = 1 << 8;
	/** ��ע */
	public static final int COLUMN_COMMENTS = 1 << 9;
	
	/** Ĭ����ȫ���ж����� */
	public static final int DEFAULT_COLUMNS_STYLE = COLUMN_FLAG | COLUMN_ID | COLUMN_CHINESE_NAME | COLUMN_BIZ_TYPE
									| COLUMN_REAL_TYPE | COLUMN_MULTIPLICITY | COLUMN_DEFAULT_VALUE | COLUMN_DESCRIPTION | COLUMN_COMMENTS;
	
	// �༭��Parameter��Ӧ������ĸ����ԣ� 
	// ���������������������Ӧ��������������Reference������Ƕ������ԣ����Ƕ����������Reference.
	protected EReference reference;
	
	protected AddParameterAction addAction;
	protected AddParameterAction addObjParamAction;
	protected AddParameterAction addNonStdFieldParamAction;
	protected AddParameterAction addParameterGroupAction;
	protected String[] addActionIds = DEFAULT_ADD_ACTION_IDS;
	protected ColumnViewerInsertAction insertAction;
	protected ColumnViewerMoveUpAction moveUpAction;
	protected ColumnViewerMoveDownAction moveDownAction;
	protected ColumnViewerMoveTopAction moveTopAction;
	protected ColumnViewerMoveBottomAction moveBottomAction;
	protected ColumnViewerPasteAction pasteAction;
	
	private String dataType = MetadataServiceProvider.C_TYPE;
	
	private int columnsStyle = DEFAULT_COLUMNS_STYLE;
	
	/**
	 * ����һ���༭Parameter�б��Block
	 * @param reference 		�༭��Parameter�б��Ӧ���ĸ�EMF���Ե�ERefence����;
	 * @param editingDomain		EditingDomain
	 * @param resource			AresResource
	 * @param problemPool
	 */
	public ParameterViewerBlock(EReference reference, EditingDomain editingDomain, IARESResource resource, IProblemPool problemPool) {
		super();
		this.reference = reference;
		this.editingDomain = editingDomain;
		this.resource = resource;
		this.problemPool = problemPool;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerBlock#getID()
	 */
	@Override
	protected String getID() {
		return getClass().getName();
	}

	@Override
	protected Point getViewerPreferredSize() {
		return new Point(100, 200);
	}
	
	/**
	 * @return the dataType
	 */
	public String getDataType() {
		// ���ȼ����Ŀ�����е����ã����û�����þͲ���Ĭ��ֵ
		IARESProjectProperty projectPro;
		try {
			projectPro = resource.getARESProject().getProjectProperty();
			if (projectPro != null) {
				String langname = projectPro.getString(BizUIConstants.REAL_TYPE_TO_DISPLAY);
				Language lang = LanguageRegister.getInstance().getLanguageByName(langname);
				if (lang != null)
					return lang.getId();
			}
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
		return dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	/**
	 * ����������Щ�еı�־λ���������Ŀǰֻ���ڴ�������֮ǰ���á�
	 * @param style
	 */
	public void setColumnsStyle(int style) {
		this.columnsStyle = style;
	}
	
	/**
	 * @param problemPool the problemPool to set
	 */
	public void setProblemPool(IProblemPool problemPool) {
		this.problemPool = problemPool;
	}

	/**
	 * @return the reference
	 */
	public EReference getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(EReference reference) {
		this.reference = reference;
	}
	
	/**
	 * ͨ������������ƿ��������Щ���͵Ĳ���
	 * @param ids
	 */
	public void setAddActionIds(String[] ids) {
		this.addActionIds = ids;
	}

	@Override
	protected TreeViewer doCreateColumnViewer(Composite parent, FormToolkit toolkit) {
		final TreeViewer viewer = super.doCreateColumnViewer(parent, toolkit);
		if (resource  != null) {
			ParamLinkOpenObjectAction action = new ParamLinkOpenObjectAction(resource.getARESProject());
			ARESUIUtil.addLinkSupport(viewer.getTree(), action);
		}
		return viewer;
	}
	
	@Override
	protected IContentProvider getColumnViewerContentProvider() {
		return new ParameterConentProvider(this.reference, this.resource.getARESProject());
	}

	@Override
	protected EStructuralFeature getHeadColumnFeature() {
		return BizPackage.Literals.PARAMETER__ID;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerBlock#createColumns(org.eclipse.jface.viewers.ColumnViewer)
	 */
	@Override
	protected void createColumns(TreeViewer viewer) {
		IARESProject project = resource.getARESProject();
		EObjectColumnViewerProblemView problemView = new EObjectColumnViewerProblemView(viewer);
		// ������չ��
		EObjectColumnViewerProblemView exProblemView = new ExtensibleModelColumnViewerProblemView(viewer);
		//  ��־λ, "������", "������", "����", "java����", "��׼�ֶ�", "ֵ", "����" 
		// 80, 100, 100, 100, 100, 100, 100, 120
		if ((columnsStyle & COLUMN_FLAG) != 0) {
			createFlagsColumn(viewer, project, problemView);
		}
		
		// ID ����
		if ((columnsStyle & COLUMN_ID) != 0) {
			createParamNameColumn(viewer, project, problemView);
		}
		
		// ������
		if ((columnsStyle & COLUMN_CHINESE_NAME) != 0) {
			createChineseNameColumn(viewer, project, problemView);
		}
		
		// ����
		if ((columnsStyle & COLUMN_BIZ_TYPE) != 0) {
			createBizTypeColumn(viewer, project, problemView);
		}
		
		// "��ʵ����"
		if ((columnsStyle & COLUMN_REAL_TYPE) != 0) {
			createRealTypeColumn(viewer, project, problemView);
		}

		// ����
		if ((columnsStyle & COLUMN_MULTIPLICITY) != 0) {
			createMultiplicityColumn(project, viewer, problemView);
		}

		// "Ĭ��ֵ"
		if ((columnsStyle & COLUMN_DEFAULT_VALUE) != 0) {
			createColumnDefaultValue(project, viewer, exProblemView);
		}
		
		// "˵��"
		if ((columnsStyle & COLUMN_DESCRIPTION) != 0) {
			createDescriptionColumn(viewer, project, problemView);
		}
		
		// "��ע"
		if ((columnsStyle & COLUMN_COMMENTS) != 0) {
			createCommentColumn(viewer, project);
		}
		
		// ��չ��Ϣ
		ExtensibleModelUtils.createExtensibleModelTreeViewerColumns(
				viewer, resource, BizPackage.Literals.PARAMETER, exProblemView);
		
		if (this.problemPool != null) {
			this.problemPool.addView(problemView);
			this.problemPool.addView(exProblemView);
		}
	
	}

	/**
	 * ��������ע���еķ���
	 * @param viewer
	 * @param project
	 */
	protected void createCommentColumn(TreeViewer viewer, IARESProject project) {
		// ����������
		EAttribute attribute = BizPackage.Literals.PARAMETER__COMMENTS;
		
		// ���������
		TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("��ע");
		column.getColumn().setWidth(120);
		column.getColumn().setMoveable(true);
		
		// ���ñ�ǩ�ṩ��
		EObjectColumnLabelProvider provider = new ParameterColumnLabelProvider(resource,attribute){

			@Override
			public String getToolTipText(Object element) {
				String text = super.getToolTipText(element);
				if(StringUtils.isBlank(text)){
					return getText(element);
				}
				return text;
			}
		
		};
		column.setLabelProvider(provider);
		
		// ���ñ༭֧��
		TextEditingSupport es =  new LongTextEditingSupport(viewer, attribute);
		es.setDecorator(new ParameterDefineEditingSupportDecorator(project, attribute));
		column.setEditingSupport(es);
	}

	/**
	 * �������������еķ���
	 * @param viewer
	 * @param project
	 * @param problemView
	 */
	protected void createDescriptionColumn(TreeViewer viewer, IARESProject project, EObjectColumnViewerProblemView problemView) {
		// ����������
		EAttribute attribute = BizPackage.Literals.PARAMETER__DESCRIPTION;
		
		// ���������
		TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("����");
		column.getColumn().setWidth(120);
		column.getColumn().setMoveable(true);
		
		// ���ñ�ǩ�ṩ��
		EObjectColumnLabelProvider provider = new ParameterColumnLabelProvider(resource,attribute){
			@Override
			public String getToolTipText(Object element) {
				String text = super.getToolTipText(element);
				if(StringUtils.isBlank(text)){
					return getText(element);
				}
				return text;
			}
		};

		provider.setDiagnosticProvider(problemView);
		column.setLabelProvider(provider);
		
		// ���ñ༭֧��
		TextEditingSupport es = new TextEditingSupport(viewer, attribute);
		es.setDecorator(new ParameterDefineEditingSupportDecorator(project, attribute));
		column.setEditingSupport(es);
	}

	/**
	 * �������������͡�������ʵ������
	 * @param viewer
	 * @param project
	 * @param problemView
	 */
	protected void createRealTypeColumn(TreeViewer viewer, IARESProject project, EObjectColumnViewerProblemView problemView) {
		// ����������
		EAttribute attribute = BizPackage.Literals.PARAMETER__REAL_TYPE;
		
		// ���������
		TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("��������");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		
		// ���ñ�ǩ�ṩ��
		ParameterColumnLabelProvider provider = new ParameterColumnLabelProvider(resource,attribute);
		provider.setDataType(getDataType());
		provider.setDiagnosticProvider(problemView);
		column.setLabelProvider(provider);
		
		// ���ñ༭֧��
		TextEditingSupport es = new TextEditingSupport(viewer, attribute);
		es.setDecorator(new ParameterDefineEditingSupportDecorator(project, attribute));
		column.setEditingSupport(es);
	}

	/**
	 * ����ҵ��������
	 * @param viewer
	 * @param project
	 * @param problemView
	 */
	protected void createBizTypeColumn(TreeViewer viewer, IARESProject project, EObjectColumnViewerProblemView problemView) {
		// ����������
		EAttribute attribute = BizPackage.Literals.PARAMETER__TYPE;
		
		// ���������
		TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("ҵ������");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		
		// ���ñ�ǩ�ṩ��
		EObjectColumnLabelProvider provider = new ParameterColumnLabelProvider(resource,attribute);
		provider.setDiagnosticProvider(problemView);
		column.setLabelProvider(provider);
		
		ParameDataTypeContentProposalProvider proposalProvider = new ParameDataTypeContentProposalProvider(resource.getARESProject());
		JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(
				viewer,
				attribute, 
				proposalProvider);
		es.setDecorator(new ParameterDefineEditingSupportDecorator(project, attribute));
		column.setEditingSupport(es);
	}

	/**
	 * ����������������
	 * @param viewer
	 * @param project
	 * @param problemView
	 */
	protected void createChineseNameColumn(TreeViewer viewer, IARESProject project, EObjectColumnViewerProblemView problemView) {
		// ����������
		EAttribute attribute = BizPackage.Literals.PARAMETER__NAME;
		
		// ���������
		TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("������");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		
		// ���ñ�ǩ�ṩ��
		EObjectColumnLabelProvider provider = new ParameterColumnLabelProvider(resource,attribute){
			@Override
			public String getText(Object element) {
				return super.getText(element);
			}
		};
		provider.setDiagnosticProvider(problemView);
		column.setLabelProvider(provider);
		
		// ���ñ༭֧��
		TextEditingSupport es = new TextEditingSupport(viewer, attribute);
		es.setDecorator(new ParameterDefineEditingSupportDecorator(project, attribute));
		column.setEditingSupport(es);
	}

	/**
	 * @param viewer
	 * @param project
	 * @param problemView
	 */
	protected void createParamNameColumn(TreeViewer viewer, IARESProject project, EObjectColumnViewerProblemView problemView) {
		// ����������
		EAttribute attribute = BizPackage.Literals.PARAMETER__ID;
		
		// ���������
		TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("������");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		
		// ���ñ�ǩ�ṩ��
		EObjectColumnLabelProvider provider = new ParameterColumnLabelProvider(resource,attribute);
		provider.setDiagnosticProvider(problemView);
		column.setLabelProvider(provider);
		
		// ���ñ༭2֧��
		MetadataContentProposalHelperWipeOffRepeatStd helper1 = new MetadataContentProposalHelperWipeOffRepeatStd(resource.getARESProject());
		StdObjContentPorposalHelper heler2 = new StdObjContentPorposalHelper();
		CompositeProposalHelper helper = new CompositeProposalHelper(heler2, helper1);
		
		ParamIdContentProposalProvider proposalProvider = new ParamIdContentProposalProvider(helper, resource.getARESProject());
		
		// 3. ����EditingSupport, 
		JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(viewer, attribute,proposalProvider);
		es.setDecorator(new ParameterDefineEditingSupportDecorator(project, attribute));
		column.setEditingSupport(es);
	}

	/**
	 * @param viewer
	 * @param project
	 * @param problemView
	 */
	protected void createFlagsColumn(TreeViewer viewer, IARESProject project, EObjectColumnViewerProblemView problemView) {
		// ����������
		EAttribute attribute = BizPackage.Literals.PARAMETER__FLAGS;
		
		// ���������
		TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("��־λ");
		column.getColumn().setWidth(70);
		column.getColumn().setMoveable(true);
		
		// ���ñ�ǩ�ṩ��
		EObjectColumnLabelProvider provider = new ParameterColumnLabelProvider(resource,attribute);
		provider.setDiagnosticProvider(problemView);
		column.setLabelProvider(provider);
		
		// 3. ����EditingSupport, 
		// ���ñ༭֧��
		TextEditingSupport es = new TextEditingSupport(viewer, attribute);
		es.setDecorator(new ParameterDefineEditingSupportDecorator(project, attribute));
		column.setEditingSupport(es);
	}
	
	/**
	 * ������������ϵ����
	 */
	protected void createMultiplicityColumn(IARESProject project, TreeViewer viewer, IDiagnosticProvider problemView) {
		// CRES ����ʾ����
		if (!BizUtil.hasCRESNature(project.getProject())) {
			EAttribute attribute = BizPackage.Literals.PARAMETER__MULTIPLICITY;
			
			// ���������
			TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
			column.getColumn().setText("����");
			column.getColumn().setWidth(50);
			column.getColumn().setMoveable(true);
			
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new ParameterColumnLabelProvider(resource,attribute);
			provider.setDiagnosticProvider(problemView);
			column.setLabelProvider(provider);
			
			// ���ñ༭֧��
			EnumEditingSupport es = new EnumEditingSupport(viewer, attribute);
			es.setDecorator(new ParameterDefineEditingSupportDecorator(project, attribute));
			column.setEditingSupport(es);
		}
	}
	
	/**
	 * ������Ĭ��ֵ����
	 */
	protected void createColumnDefaultValue(IARESProject project, TreeViewer viewer, IDiagnosticProvider problemView) {
		// ����������
		EAttribute attribute = BizPackage.Literals.PARAMETER__DEFAULT_VALUE;
		
		// ���������
		TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("Ĭ��ֵ");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		
		// ���ñ�ǩ�ṩ��
		EObjectColumnLabelProvider provider = new ParameterColumnLabelProvider(resource,attribute);
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
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerBlock#createActions()
	 */
	@Override
	protected void createActions() {

		AddToStdFieldAction addStdFieldAction = 
			new AddToStdFieldAction(this.resource.getARESProject(), getColumnViewer(), editingDomain);	
		
		getActionRegistry().registerAction(addStdFieldAction);
		getSelectionActions().add(addStdFieldAction.getId());	
		
		addAction = new AddParameterAction(
				getColumnViewer(), 
				editingDomain,
				IActionIDConstant.CV_ADD,
				"���ӱ�׼�ֶβ���",
				null,
				this.reference,
				getParameterEClass(),//BizPackage.Literals.PARAMETER,
				ParamType.STD_FIELD
				);
		
		getActionRegistry().registerAction(addAction);
		getSelectionActions().add(addAction.getId());
		
		addObjParamAction = new AddParameterAction(
				getColumnViewer(), 
				this.editingDomain,
				IBizActionIDConstants.ADD_OBJECT_PARAM,
				"���Ӷ������Ͳ���",
				null,
				this.reference,
				getParameterEClass(),//BizPackage.Literals.PARAMETER,
				ParamType.OBJECT
				);
		
		
		getActionRegistry().registerAction(addObjParamAction);
		getSelectionActions().add(addObjParamAction.getId());
		
		addParameterGroupAction = new AddParameterAction(
				getColumnViewer(), 
				this.editingDomain,
				IBizActionIDConstants.ADD_PARAM_GROUP,
				"���Ӳ�����",
				null,
				this.reference,
				getParameterEClass(),//BizPackage.Literals.PARAMETER,
				ParamType.PARAM_GROUP
				);
		
		getActionRegistry().registerAction(addParameterGroupAction);
		getSelectionActions().add(addParameterGroupAction.getId());
		
		addNonStdFieldParamAction = new AddParameterAction(
				getColumnViewer(), 
				this.editingDomain,
				IBizActionIDConstants.ADD_NON_STD_FIELD_PARME,
				"���ӷǱ�׼�ֶβ���",
				null,
				this.reference,
				getParameterEClass(),//BizPackage.Literals.PARAMETER,
				ParamType.NON_STD_FIELD
				);
		
		getActionRegistry().registerAction(addNonStdFieldParamAction);
		getSelectionActions().add(addNonStdFieldParamAction.getId());
		
		IAction delAction = new ColumnViewerDeleteAction(getColumnViewer(), this.editingDomain);
		getActionRegistry().registerAction(delAction);
		getSelectionActions().add(delAction.getId());
		
		moveUpAction = new ColumnViewerMoveUpAction(getColumnViewer(), 
													this.editingDomain,
													null, 
													this.reference);
		getActionRegistry().registerAction(moveUpAction);
		getSelectionActions().add(moveUpAction.getId());
		getStackActions().add(moveUpAction.getId());
		
		moveTopAction = new ColumnViewerMoveTopAction(getColumnViewer(), 
				this.editingDomain,
				null, 
				this.reference);
		getActionRegistry().registerAction(moveTopAction);
		getSelectionActions().add(moveTopAction.getId());
		getStackActions().add(moveTopAction.getId());
		
		
		moveDownAction = new ColumnViewerMoveDownAction(getColumnViewer(), this.editingDomain,
				null, this.reference);
		getActionRegistry().registerAction(moveDownAction);
		getSelectionActions().add(moveDownAction.getId());
		getStackActions().add(moveDownAction.getId());
		
		insertAction = new ColumnViewerInsertAction(
				getColumnViewer(), 
				this.editingDomain,
				this.getReference(),
				getParameterEClass());
		getActionRegistry().registerAction(insertAction);
		getSelectionActions().add(insertAction.getId());
		
		moveBottomAction = new ColumnViewerMoveBottomAction(getColumnViewer(), this.editingDomain,
				null, this.reference);
		getActionRegistry().registerAction(moveBottomAction);
		getSelectionActions().add(moveBottomAction.getId());
		getStackActions().add(moveBottomAction.getId());
		
		IAction copyAction = new ColumnViewerCopyAction(getColumnViewer());
		getActionRegistry().registerAction(copyAction);
		getSelectionActions().add(copyAction.getId());
		
		IAction copyCellAction = new CopyCellAction(getColumnViewer());
		getActionRegistry().registerAction(copyCellAction);
		
		IAction copyColumnAction = new CopyColumnAction(getColumnViewer());
		getActionRegistry().registerAction(copyColumnAction);
		
		pasteAction =  createPasteAction();
		getActionRegistry().registerAction(pasteAction);
		getClipboardActions().add(pasteAction.getId());
		
		//ֻ������
		getEditableControl().addEditableUnit(new ActionEditableUnit(addAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(addNonStdFieldParamAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(addObjParamAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(addParameterGroupAction));

		getEditableControl().addEditableUnit(new ActionEditableUnit(delAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(moveTopAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(moveUpAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(moveDownAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(insertAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(moveBottomAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(pasteAction));

	}
	
	protected ColumnViewerPasteAction createPasteAction() {
		return new ParameterPasteAction(getColumnViewer(), this.editingDomain, null, this.reference);
	}
	
	protected EClass getParameterEClass(){
		return BizPackage.Literals.PARAMETER;
	}
	
	@Override
	protected void createMenus(IMenuManager menuManager) {
		if (this.addActionIds != null) {
			for (String id : addActionIds) {
				IAction action = getActionRegistry().getAction(id);
				menuManager.add(action);
			}
		}
		
		IAction action = getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		menuManager.add(action);
		
		menuManager.add(new Separator());
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_COPY);
		menuManager.add(action);

		action = getActionRegistry().getAction(CopyCellAction.ID);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(CopyColumnAction.ID);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_PASTE);
		menuManager.add(action);
		
		menuManager.add(new Separator());
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_TOP);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_UP);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_DOWN);
		menuManager.add(action);
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_BOTTOM);
		menuManager.add(action);
		action = getActionRegistry().getAction(IActionIDConstant.CV_INSERT);
		menuManager.add(action);
		
		menuManager.add(new Separator());
		
		// FIXME ��ӵ���׼�ֶι�����ֲ��ʱδ���
		//IAction addStdFieldAction = getActionRegistry().getAction(IBizActionIDConstants.ADD_TO_STD_FIELD);
		//menuManager.add(addStdFieldAction);
	}
	
	@Override
	protected void createToolbarItems(ToolBarManager buttonManager) {
		AddParmaActionGroup addParamActionGroup = new AddParmaActionGroup(getActionRegistry(), this.addActionIds);
		buttonManager.add(addParamActionGroup);
		
		// ������ť�б�
		IAction action = getActionRegistry().getAction(IActionIDConstant.CV_INSERT);
		buttonManager.add(action);
		
		 action = getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		buttonManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_TOP);
		buttonManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_UP);
		buttonManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_DOWN);
		buttonManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_BOTTOM);
		buttonManager.add(action);
		
	}	
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerBlock#setInput(java.lang.Object)
	 */
	@Override
	public void setInput(Object input) {
		addAction.setDefaultOwner((EObject) input);
		addObjParamAction.setDefaultOwner((EObject) input);
		addParameterGroupAction.setDefaultOwner((EObject) input);
		addNonStdFieldParamAction.setDefaultOwner((EObject) input);
		insertAction.setOwner((EObject) input);
		moveDownAction.setOwner((EObject) input);
		moveUpAction.setOwner((EObject) input);
		moveBottomAction.setOwner((EObject) input);
		moveTopAction.setOwner((EObject) input);
		pasteAction.setOwner((EObject) input);
		super.setInput(input);
	}
	
}
