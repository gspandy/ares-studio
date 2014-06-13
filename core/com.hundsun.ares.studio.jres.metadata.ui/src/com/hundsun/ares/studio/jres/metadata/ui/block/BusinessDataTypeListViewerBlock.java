/**
 * Դ�������ƣ�BusinessDataTypeListViewerBlock.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.block;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.util.Pair;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.metadata.ui.MetadataUI;
import com.hundsun.ares.studio.jres.metadata.ui.actions.AddItemAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.CopyAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.ExportMetadataAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.IMetadataActionIDConstant;
import com.hundsun.ares.studio.jres.metadata.ui.actions.ImportMetadataAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.InsertItemAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.MoveDownAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.MoveUpAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.PasteAction;
import com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalHelper;
import com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalProvider;
import com.hundsun.ares.studio.jres.metadata.ui.refactor.MatadataRenameAction;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataColumnLabelProvider;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataColumnViewerProblemView;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataDescColumnLabelProvider;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataHeaderColumnLabelProvider;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataItemEditingSupportDecorator;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeBusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.provider.LongTextEditingSupport;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil;
import com.hundsun.ares.studio.ui.ColumnViewerHoverToolTip;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerDeleteAction;
import com.hundsun.ares.studio.ui.editor.actions.CopyCellAction;
import com.hundsun.ares.studio.ui.editor.actions.CopyColumnAction;
import com.hundsun.ares.studio.ui.editor.actions.IActionIDConstant;
import com.hundsun.ares.studio.ui.editor.actions.ValidateAction;
import com.hundsun.ares.studio.ui.editor.editable.ActionEditableUnit;
import com.hundsun.ares.studio.ui.editor.editingsupport.JresTextEditingSupportWithContentAssist;
import com.hundsun.ares.studio.ui.editor.editingsupport.TextEditingSupport;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelUtils;
import com.hundsun.ares.studio.ui.editor.viewers.BaseEObjectColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnViewerProblemView;
import com.hundsun.ares.studio.ui.validate.IProblemPool;

/**
 * @author yanwj06282
 *
 */
public class BusinessDataTypeListViewerBlock extends MetadataListViewerBlock {

	/**
	 * @param page
	 * @param editingDomain
	 * @param site
	 * @param resource
	 * @param problemPool
	 */
	public BusinessDataTypeListViewerBlock(FormPage page,
			EditingDomain editingDomain, IWorkbenchPartSite site,
			IARESResource resource, IProblemPool problemPool) {
		super(page, editingDomain, site, resource, problemPool);
	}

	@Override
	protected boolean getDefaultShowCategory() {
		return false;
	}
	
	@Override
	protected void createColumns(TreeViewer _viewer) {
		// TODO#�����߼�#��Ԫ#��#����#�ѱ��� #2011-07-25 #54 #40 #��һ��������ڱ༭ҵ����������
		//����У�ID�����ơ�ҵ�������������á���׼�������͡����ȡ����ȡ���������Ĭ��ֵ��	��ע
		
		/*
		 * TODO#�����߼�#��Ҷ��#��#��С��#�ѱ��� #2011-8-9 #10 #5 #LabelProvider����
		 * 
		 *
		 * ����ͼ�з�����ʾģʽ�����ʱ����뱣֤�ܹ���һ����ʾ���ֶ���Ĳ�ͬ���ԣ�������Ҫʹ��MetadataEStructuralFeatureProvider
		 * ���Ӳο�Ĭ��ֵ�༭�����������ֺ�����2�������⴦��
		 */
		
		final TreeViewer viewer = (TreeViewer) _viewer;
		EObjectColumnViewerProblemView problemView = new MetadataColumnViewerProblemView(viewer);
		
		/**ID*/
		{
			// ����������
			EAttribute attribute =MetadataPackage.Literals.NAMED_ELEMENT__NAME;
			
			// ���������
			TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
			column.getColumn().setText("������");
			column.getColumn().setWidth(150);
			
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataHeaderColumnLabelProvider(attribute , getARESResource());
			provider.setDiagnosticProvider(problemView);
			column.setLabelProvider(provider);
			
			// ���ñ༭֧��
			TextEditingSupport es = new TextEditingSupport(viewer, attribute);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
			column.setEditingSupport(es);
		}
		/**����*/
		{
			// ����������
			EAttribute attribute =MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME;
			
			// ���������
			TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
			column.getColumn().setText("��������");
			column.getColumn().setWidth(120);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataColumnLabelProvider(attribute,getARESResource());
			provider.setDiagnosticProvider(problemView);
			column.setLabelProvider(provider);
			
			// ���ñ༭֧��
			TextEditingSupport es = new TextEditingSupport(viewer, attribute);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
			column.setEditingSupport(es);
			column.getColumn().setMoveable(true);
		}
		/**ҵ��������������*/
		if (MetadataUtil.isUseRefFeature(getARESResource())) {
			// ����������
			EAttribute attribute = MetadataPackage.Literals.METADATA_ITEM__REF_ID;
			
			// ���������
			TreeViewerColumn comlumn = new TreeViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("ҵ��������������");
			comlumn.getColumn().setWidth(140);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataColumnLabelProvider(attribute, getARESResource());
			provider.setDiagnosticProvider(problemView);
			comlumn.setLabelProvider(provider);
			
			//TODO#�����߼�#��Ҷ��#��#��С��#�ѱ��� #2011-8-9#45 #25 #������ʾ,����ҵ���������ͺ�����ֶβ��ɱ༭
			// 1. ��ȡaresProject
			IARESResource res = getARESResource();
			final IARESProject project = res == null ? null : res.getARESProject();
			
			// 2. proposal provider
			MetadataContentProposalHelper helper = new MetadataContentProposalHelper(project);
			MetadataContentProposalProvider proposalProvider = new MetadataContentProposalProvider(helper, IMetadataRefType.BizType, project);
			
			// 3. ����EditingSupport, 
			JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(
					viewer,
					attribute, 
					proposalProvider);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
			comlumn.setEditingSupport(es);
			comlumn.getColumn().setMoveable(true);
		}
		/**��׼������������*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.BUSINESS_DATA_TYPE__STD_TYPE;
			
			// ���������
			TreeViewerColumn comlumn = new TreeViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("��׼����");
			comlumn.getColumn().setWidth(120);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataColumnLabelProvider(attribute, getARESResource());
			provider.setDiagnosticProvider(problemView);
			comlumn.setLabelProvider(provider);
			// 1. ��ȡaresProject
			IARESResource res = getARESResource();
			final IARESProject project = res == null ? null : res.getARESProject();
			
			// 2. proposal provider
			MetadataContentProposalHelper helper = new MetadataContentProposalHelper(project);
			MetadataContentProposalProvider proposalProvider = new MetadataContentProposalProvider(helper, IMetadataRefType.StdType, project);
			
			// 3. ����EditingSupport, 
			JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(
					viewer,
					attribute, 
					proposalProvider);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
			comlumn.setEditingSupport(es);
			comlumn.getColumn().setMoveable(true);
		}
		/**����*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.BUSINESS_DATA_TYPE__LENGTH;
			
			// ���������
			TreeViewerColumn comlumn = new TreeViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("����");
			comlumn.getColumn().setWidth(70);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataColumnLabelProvider(attribute, getARESResource());
			provider.setDiagnosticProvider(problemView);
			comlumn.setLabelProvider(provider);
			// ���ñ༭֧��
			TextEditingSupport es = new TextEditingSupport(viewer, attribute);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
			comlumn.setEditingSupport(es);
			comlumn.getColumn().setMoveable(true);
		}
		/**����*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.BUSINESS_DATA_TYPE__PRECISION;
			
			// ���������
			TreeViewerColumn comlumn = new TreeViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("����");
			comlumn.getColumn().setWidth(70);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataColumnLabelProvider(attribute, getARESResource());
			provider.setDiagnosticProvider(problemView);
			comlumn.setLabelProvider(provider);
			// ���ñ༭֧��
			TextEditingSupport es = new TextEditingSupport(viewer, attribute);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
			comlumn.setEditingSupport(es);
			comlumn.getColumn().setMoveable(true);
		}
		/**��������Ĭ��ֵ����*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.BUSINESS_DATA_TYPE__DEFAULT_VALUE;
			
			// ���������
			TreeViewerColumn comlumn = new TreeViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("Ĭ��ֵ");
			comlumn.getColumn().setWidth(200);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataColumnLabelProvider(attribute, getARESResource());
			provider.setDiagnosticProvider(problemView);
			comlumn.setLabelProvider(provider);
			// 1. ��ȡaresProject
			IARESResource res = getARESResource();
			final IARESProject project = res == null ? null : res.getARESProject();
			
			// 2. proposal provider
			MetadataContentProposalHelper helper = new MetadataContentProposalHelper(project);
			MetadataContentProposalProvider proposalProvider = new MetadataContentProposalProvider(helper, IMetadataRefType.DefValue, project);
			
			// 3. ����EditingSupport,
			JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(
					viewer,
					attribute, 
					proposalProvider);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
			comlumn.setEditingSupport(es);
			comlumn.getColumn().setMoveable(true);
		}
		/**��ע*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION;
			
			// ���������
			TreeViewerColumn comlumn = new TreeViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("˵��");
			comlumn.getColumn().setWidth(200);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataDescColumnLabelProvider(attribute, getARESResource());
			provider.setDiagnosticProvider(problemView);
			comlumn.setLabelProvider(provider);
			// ���ñ༭֧��
			TextEditingSupport es = new LongTextEditingSupport(viewer, attribute);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
			comlumn.setEditingSupport(es);
			comlumn.getColumn().setMoveable(true);
		}
		
		// ������չ��֧��
		ExtensibleModelUtils.createExtensibleModelTreeViewerColumns(getColumnViewer(), getARESResource(), 
				MetadataPackage.Literals.BUSINESS_DATA_TYPE, problemView);
		
		//���������ͼ
		getProblemPool().addView(problemView);
		//���ֻ������
//		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(viewer.getControl()));
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.MetadataListPage#createActions()
	 */
	@Override
	protected void createActions() {
		super.createActions();
		
		IAction actionAdd = new AddItemAction(
				getColumnViewer(), 
				getEditingDomain(), 
				MetadataPackage.Literals.BUSINESS_DATA_TYPE);
		getActionRegistry().registerAction(actionAdd);
		getSelectionActions().add(actionAdd.getId());
		
		IAction actionInsert = new InsertItemAction(
				getColumnViewer(), 
				getEditingDomain(), 
				MetadataPackage.Literals.BUSINESS_DATA_TYPE);
		getActionRegistry().registerAction(actionInsert);
		getSelectionActions().add(actionInsert.getId());
		
		IAction actionDel = new ColumnViewerDeleteAction(getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(actionDel);
		getSelectionActions().add(actionDel.getId());
		
		IAction actionMoveUp = new MoveUpAction(getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(actionMoveUp);
		getSelectionActions().add(actionMoveUp.getId());
		getStackActions().add(actionMoveUp.getId());
		
		IAction actionMoveDown = new MoveDownAction(getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(actionMoveDown);
		getSelectionActions().add(actionMoveDown.getId());
		getStackActions().add(actionMoveDown.getId());
		
		IAction copyAction = new CopyAction(getColumnViewer());
		getActionRegistry().registerAction(copyAction);
		getSelectionActions().add(copyAction.getId());
		
		IAction copyCellAction = new CopyCellAction(getColumnViewer());
		getActionRegistry().registerAction(copyCellAction);
		
		IAction copyColumnAction = new CopyColumnAction(getColumnViewer());
		getActionRegistry().registerAction(copyColumnAction);
		
		IAction pasteAction = new PasteAction(getColumnViewer(), getEditingDomain(),
				MetadataPackage.Literals.METADATA_CATEGORY, MetadataPackage.Literals.BUSINESS_DATA_TYPE);
		getActionRegistry().registerAction(pasteAction);
		getSelectionActions().add(pasteAction.getId());
		getClipboardActions().add(pasteAction.getId());
		
		IAction actionValidate = new ValidateAction(getFormPage());
		getActionRegistry().registerAction(actionValidate);
		
		
//		ImportMetadataAction importAction = new ImportMetadataAction(getARESResource(), getSite());
		ImportMetadataAction importAction = new ImportMetadataAction(getARESResource(), getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(importAction);
		getSelectionActions().add(importAction.getId());
		String dialogTitle	= "����ҵ����������";
		String dialogMessage = "����Ŀ�е�ҵ���������͵���(Excel�ļ�).";
		Image dialogImage = AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/bizTypeFile.png").createImage();
		ExportMetadataAction exportAction = new ExportMetadataAction(getARESResource(), getSite(),dialogTitle,dialogImage,dialogMessage);
		getActionRegistry().registerAction(exportAction);
		getSelectionActions().add(exportAction.getId());
		
		//ֻ������
		getEditableControl().addEditableUnit(new ActionEditableUnit(actionAdd));
		getEditableControl().addEditableUnit(new ActionEditableUnit(actionDel));
		getEditableControl().addEditableUnit(new ActionEditableUnit(actionMoveUp));
		getEditableControl().addEditableUnit(new ActionEditableUnit(actionMoveDown));
	}
	
	@Override
	protected void createToolbarItems(ToolBarManager toolBarManager) {
		// ������ť�б�
		IAction action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_ADD_ITEM);
		toolBarManager.add(action);
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_INSERT_ITEM);
		toolBarManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		toolBarManager.add(action);
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_MOVE_UP);
		toolBarManager.add(action);
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_MOVE_DOWN);
		toolBarManager.add(action);

		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_IMPORT_METADATA);
		toolBarManager.add(action);
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_EXPORT_METADATA);
		toolBarManager.add(action);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractTreeComponetListPage#createMenus(org.eclipse.jface.action.IMenuManager)
	 */
	@Override
	protected void createMenus(IMenuManager menuManager) {
		//�����Ҽ��˵�
		IAction action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_ADD_ITEM);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		menuManager.add(action);		
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_MOVE_UP);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_MOVE_DOWN);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_COPY);
		menuManager.add(action);
		

		action = getActionRegistry().getAction(CopyCellAction.ID);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(CopyColumnAction.ID);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_PASTE);
		menuManager.add(action);
		
		action = new MatadataRenameAction(getEditingDomain(),getTreeViewerFirstColumnName(),getFormPage().getEditor(),getColumnViewer(),getARESResource());
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_VALIDATE);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_INSERT_ITEM);
		menuManager.add(action);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.link.ICellLinkProvider#getLinkedObject(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public Pair<EObject, IARESResource> getLinkedObject(ViewerCell cell) {
		BaseEObjectColumnLabelProvider labelProvider = (BaseEObjectColumnLabelProvider) getColumnViewer().getLabelProvider(cell.getColumnIndex());
		
		EStructuralFeature feature = labelProvider.getEStructuralFeature(cell.getElement());
		
		DeBusinessDataType d = MetadataUtil.decrypt((BusinessDataType)cell.getElement(), getARESResource());
		if (MetadataPackage.Literals.METADATA_ITEM__REF_ID.equals(feature)) {
			return (Pair)d.getResolvedItem();
		} else if (MetadataPackage.Literals.BUSINESS_DATA_TYPE__DEFAULT_VALUE.equals(feature)) {
			return (Pair)d.getDefaultValue().getResolvedItem();
		} else if (MetadataPackage.Literals.BUSINESS_DATA_TYPE__STD_TYPE.equals(feature)) {
			return (Pair)d.getStdType().getResolvedItem();
		}
		
		return null;
	}

	@Override
	protected String getID() {
		return getClass().getName();
	}

	@Override
	protected void restoreShowCategoryState(IDialogSettings settings) {
		// do nothing
	}
	
	@Override
	protected void storeShowCategoryState(IDialogSettings settings) {
		// do nothing
	}
	@Override
	protected void addToolTipSupport(TreeViewer viewer) {
		ColumnViewerHoverToolTip.enableFor(viewer);
	}
}
