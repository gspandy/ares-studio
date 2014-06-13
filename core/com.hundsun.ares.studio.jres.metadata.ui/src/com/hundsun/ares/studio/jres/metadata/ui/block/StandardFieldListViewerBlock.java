/**
 * Դ�������ƣ�StandardFieldViewerBlock.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.block;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
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
import com.hundsun.ares.studio.jres.metadata.ui.actions.AddChildCategoryAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.AddItemAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.AddSiblingCategoryAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.CopyAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.ExportMetadataAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.IMetadataActionIDConstant;
import com.hundsun.ares.studio.jres.metadata.ui.actions.ImportMetadataAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.InsertItemAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.MoveDownAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.MoveUpAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.PasteAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.RemoveAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.ShowCategoryAction;
import com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalHelper;
import com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalProvider;
import com.hundsun.ares.studio.jres.metadata.ui.refactor.MatadataRenameAction;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataColumnLabelProvider;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataColumnViewerProblemView;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataHeaderColumnLabelProvider;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataItemEditingSupportDecorator;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.StdFieldDictDescColumnLabelProvider;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeStandardField;
import com.hundsun.ares.studio.jres.model.metadata.provider.LongTextEditingSupport;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil;
import com.hundsun.ares.studio.ui.ColumnViewerHoverToolTip;
import com.hundsun.ares.studio.ui.editor.actions.ActionGroup;
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
public class StandardFieldListViewerBlock extends MetadataListViewerBlock {

	/**
	 * @param page
	 * @param editingDomain
	 * @param site
	 * @param resource
	 * @param problemPool
	 */
	public StandardFieldListViewerBlock(FormPage page, EditingDomain editingDomain,
			IWorkbenchPartSite site, IARESResource resource,
			IProblemPool problemPool) {
		super(page, editingDomain, site, resource, problemPool);
	}

	@Override
	protected boolean getDefaultShowCategory() {
		return false;
	}
	
	@Override
	protected void addToolTipSupport(TreeViewer viewer) {
		ColumnViewerHoverToolTip.enableFor(viewer);
	}
	
	@Override
	protected void createColumns(TreeViewer viewer) {
		// TODO#�����߼�#��Ԫ#��#����#�ѱ��� #2011-07-26 #88 #40 #��һ��������ڱ༭��׼�ֶΣ�֧����״�㼶
		//����У�Ӣ�Ĵ��롢���á��������ơ��ֶ����͡����ȡ����ȡ��ֵ���Ŀ���ֶ�˵������ʾĬ��ֵ����ʾ�ؼ����ؼ����ԡ��ؼ��¼�
		/*TODO#�����߼�#��Ҷ��#��#��С��#�ѱ��� #2011-8-9 #5 #3 #LabelProvider����
		 * 
		 *
		 * ����ͼ�з�����ʾģʽ�����ʱ����뱣֤�ܹ���һ����ʾ���ֶ���Ĳ�ͬ���ԣ�������Ҫʹ��MetadataEStructuralFeatureProvider
		 * ���Ӳο�Ĭ��ֵ�༭�����������ֺ�����2�������⴦��
		 */
		final TreeViewer treeViewer = viewer;
		EObjectColumnViewerProblemView problemView = new MetadataColumnViewerProblemView(treeViewer);
		/**ID*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.NAMED_ELEMENT__NAME;
			
			// ���������
			TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.LEFT);
			column.getColumn().setText("�ֶ���");
			column.getColumn().setWidth(160);
			column.getColumn().setMoveable(true);
			
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataHeaderColumnLabelProvider(attribute,getARESResource());
			provider.setDiagnosticProvider(problemView);
			column.setLabelProvider(provider);
			
			// ���ñ༭֧��
			TextEditingSupport es = new TextEditingSupport(treeViewer, attribute);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
			column.setEditingSupport(es);
		}
		/**����*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME;
			
			// ���������
			TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.LEFT);
			column.getColumn().setText("�ֶ�����");
			column.getColumn().setWidth(150);
			column.getColumn().setMoveable(true);
			
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataColumnLabelProvider(attribute, getARESResource());
			provider.setDiagnosticProvider(problemView);
			column.setLabelProvider(provider);
			
			// ���ñ༭֧��
			TextEditingSupport es = new TextEditingSupport(treeViewer, attribute);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
			column.setEditingSupport(es);
			column.getColumn().setMoveable(true);
		}
		/**��׼�ֶ�����*/
		if (MetadataUtil.isUseRefFeature(getARESResource())) {
			// ����������
			EAttribute attribute = MetadataPackage.Literals.METADATA_ITEM__REF_ID;
			
			// ���������
			TreeViewerColumn comlumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			comlumn.getColumn().setText("��׼�ֶ�����");
			comlumn.getColumn().setWidth(100);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataColumnLabelProvider(attribute, getARESResource());
			provider.setDiagnosticProvider(problemView);
			comlumn.setLabelProvider(provider);
			
			//TODO#�����߼�#��Ҷ��#��#��С��#�ѱ��� #2011-8-9 #44 #20#������ʾ,���ñ�׼�ֶκ�����ֶβ��ɱ༭
			// 1. ��ȡaresProject
			final IARESProject project = getARESResource() == null ? null : getARESResource().getARESProject();
			
			// 2. proposal provider
			MetadataContentProposalHelper helper = new MetadataContentProposalHelper(project);
			MetadataContentProposalProvider proposalProvider = new MetadataContentProposalProvider(helper, IMetadataRefType.StdField, project);
			
			// 3. ����EditingSupport, 
			JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(
					treeViewer,
					attribute, 
					proposalProvider);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
			comlumn.setEditingSupport(es);
			comlumn.getColumn().setMoveable(true);
		}
		/**ҵ����������*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.STANDARD_FIELD__DATA_TYPE;
			
			// ���������
			TreeViewerColumn comlumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			comlumn.getColumn().setText("�ֶ�����");
			comlumn.getColumn().setWidth(130);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataColumnLabelProvider(attribute, getARESResource());
			provider.setDiagnosticProvider(problemView);
			comlumn.setLabelProvider(provider);
			// 1. ��ȡaresProject
			final IARESProject project = getARESResource() == null ? null : getARESResource().getARESProject();
			
			// 2. proposal provider
			MetadataContentProposalHelper helper = new MetadataContentProposalHelper(project);
			MetadataContentProposalProvider proposalProvider = new MetadataContentProposalProvider(helper, IMetadataRefType.BizType, project);
			
			// 3. ����EditingSupport, 
			JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(
					treeViewer,
					attribute, 
					proposalProvider);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
			comlumn.setEditingSupport(es);
			comlumn.getColumn().setMoveable(true);
		}
		/**�ֵ���Ŀ*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.STANDARD_FIELD__DICTIONARY_TYPE;
			
			// ���������
			TreeViewerColumn comlumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			comlumn.getColumn().setText("�ֵ���Ŀ");
			comlumn.getColumn().setWidth(100);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataColumnLabelProvider(attribute, getARESResource());
			provider.setDiagnosticProvider(problemView);
			comlumn.setLabelProvider(provider);
			// 1. ��ȡaresProject
			final IARESProject project = getARESResource() == null ? null : getARESResource().getARESProject();
			
			// 2. proposal provider
			MetadataContentProposalHelper helper = new MetadataContentProposalHelper(project);
			MetadataContentProposalProvider proposalProvider = new MetadataContentProposalProvider(helper, IMetadataRefType.Dict, project);
			
			// 3. ����EditingSupport, 
			JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(
					treeViewer,
					attribute, 
					proposalProvider);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
			comlumn.setEditingSupport(es);
			comlumn.getColumn().setMoveable(true);
		}
		/**�ֵ���Ŀ˵��*/
		{
//			// ����������
//			EAttribute attribute = MetadataPackage.Literals.STANDARD_FIELD__DICTIONARY_TYPE;
			
			// ���������
			TreeViewerColumn comlumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			comlumn.getColumn().setText("�ֵ���Ŀ˵��");
			comlumn.getColumn().setWidth(200);
			// ���ñ�ǩ�ṩ��
			StdFieldDictDescColumnLabelProvider provider = new StdFieldDictDescColumnLabelProvider(getARESResource()){
				@Override
				public String getToolTipText(Object element) {
					String text = getText(element);
					return StringUtils.isBlank(text) ? super.getToolTipText(element) : text;
				}
			};//MetadataColumnLabelProvider(attribute, getARESResource());
			comlumn.setLabelProvider(provider);
			// ���ñ༭֧��
			comlumn.getColumn().setMoveable(true);
		}
		
		/**��ע*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION;
			
			// ���������
			TreeViewerColumn comlumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			comlumn.getColumn().setText("˵��");
			comlumn.getColumn().setWidth(200);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataColumnLabelProvider(attribute, getARESResource()){
				@Override
				public String getToolTipText(Object element) {
					String text = getText(element);
					return StringUtils.isBlank(text) ? super.getToolTipText(element) : text;
				}
			};
			provider.setDiagnosticProvider(problemView);
			comlumn.setLabelProvider(provider);
			// ���ñ༭֧��
			TextEditingSupport es = new LongTextEditingSupport(treeViewer, attribute);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
			comlumn.setEditingSupport(es);
			comlumn.getColumn().setMoveable(true);
		}
		
		// ������չ��֧��
		ExtensibleModelUtils.createExtensibleModelTreeViewerColumns(getColumnViewer(), getARESResource(), 
		MetadataPackage.Literals.STANDARD_FIELD, problemView);
		
//		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(treeViewer.getControl()));
		getProblemPool().addView(problemView);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.link.ICellLinkProvider#getLinkedObject(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public Pair<EObject, IARESResource> getLinkedObject(ViewerCell cell) {
		BaseEObjectColumnLabelProvider labelProvider = (BaseEObjectColumnLabelProvider) getColumnViewer().getLabelProvider(cell.getColumnIndex());
		
		EStructuralFeature feature = labelProvider.getEStructuralFeature(cell.getElement());
		
		DeStandardField d = MetadataUtil.decrypt((StandardField)cell.getElement(), getARESResource());
		if (MetadataPackage.Literals.METADATA_ITEM__REF_ID.equals(feature)) {
			return (Pair)d.getResolvedItem();
		} else if (MetadataPackage.Literals.STANDARD_FIELD__DATA_TYPE.equals(feature)) {
			return (Pair)d.getDataType().getResolvedItem();
		} else if (MetadataPackage.Literals.STANDARD_FIELD__DICTIONARY_TYPE.equals(feature)) {
			return (Pair)d.getDictionaryType().getResolvedItem();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerBlock#createMenus(org.eclipse.jface.action.IMenuManager)
	 */
	@Override
	protected void createMenus(IMenuManager menuManager) {

		//������׼�ֶα༭�����Ҽ��˵�
		IAction action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_ADD_ITEM);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_ADD_CHILD_CATEGORY);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_ADD_SLIBING_CATEGORY);
		menuManager.add(action);

		menuManager.add(new Separator());
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_REMOVE);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		menuManager.add(action);

		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_MOVE_UP);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_MOVE_DOWN);
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

		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_SHOW_CATEGORY);
		menuManager.add(action);
		
		action = new MatadataRenameAction(getEditingDomain(),getTreeViewerFirstColumnName(),getFormPage().getEditor(),getColumnViewer(),getARESResource());
		
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_VALIDATE);
		menuManager.add(action);
	
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_INSERT_ITEM);
		menuManager.add(action);
	}

	@Override
	protected void createToolbarItems(ToolBarManager manager) {
		super.createToolbarItems(manager);

		//������׼�ֶα༭���Ĳ����б�
		ActionGroup newGroup = new MetadataNewActionGroup(getActionRegistry());
		manager.add(newGroup);
		
		IAction
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_INSERT_ITEM);
		manager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		manager.add(action);
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_MOVE_UP);
		manager.add(action);
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_MOVE_DOWN);
		manager.add(action);

		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_IMPORT_METADATA);
		manager.add(action);
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_EXPORT_METADATA);
		manager.add(action);
	}
	
	@Override
	protected void createActions() {
		//������׼�ֶε�����action
		super.createActions();
		
		List<IAction> actions = new ArrayList<IAction>();
		
		IAction addItemAction = new AddItemAction(getColumnViewer(), getEditingDomain(), MetadataPackage.Literals.STANDARD_FIELD);
		getActionRegistry().registerAction(addItemAction);
		getSelectionActions().add(addItemAction.getId());
		actions.add(addItemAction);
		
		IAction actionInsert = new InsertItemAction(
				getColumnViewer(), 
				getEditingDomain(), 
				MetadataPackage.Literals.STANDARD_FIELD);
		getActionRegistry().registerAction(actionInsert);
		getSelectionActions().add(actionInsert.getId());
		
		IAction addChildCategoryAction = new AddChildCategoryAction(getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(addChildCategoryAction);
		getSelectionActions().add(addChildCategoryAction.getId());
		categoryWareActions.add(addChildCategoryAction.getId());
		actions.add(addChildCategoryAction);
		
		IAction addSiblingCategoryAction = new AddSiblingCategoryAction(getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(addSiblingCategoryAction);
		getSelectionActions().add(addSiblingCategoryAction.getId());
		categoryWareActions.add(addSiblingCategoryAction.getId());
		actions.add(addSiblingCategoryAction);
		
		IAction columnViewerDelAction = new ColumnViewerDeleteAction(getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(columnViewerDelAction);
		getSelectionActions().add(columnViewerDelAction.getId());
		actions.add(columnViewerDelAction);
		
		IAction removeAction = new RemoveAction(getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(removeAction);
		getSelectionActions().add(removeAction.getId());
		actions.add(removeAction);
		
		IAction moveUpAction = new MoveUpAction(getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(moveUpAction);
		getSelectionActions().add(moveUpAction.getId());
		getStackActions().add(moveUpAction.getId());
		actions.add(moveUpAction);
		
		IAction moveDownAction = new MoveDownAction(getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(moveDownAction);
		getSelectionActions().add(moveDownAction.getId());
		getStackActions().add(moveDownAction.getId());
		actions.add(moveDownAction);
		
		IAction showCategoryAction = new ShowCategoryAction(getColumnViewer());
		getActionRegistry().registerAction(showCategoryAction);
		actions.add(showCategoryAction);
		
		IAction copyAction = new CopyAction(getColumnViewer());
		getActionRegistry().registerAction(copyAction);
		getSelectionActions().add(copyAction.getId());
		
		IAction copyCellAction = new CopyCellAction(getColumnViewer());
		getActionRegistry().registerAction(copyCellAction);
		
		IAction copyColumnAction = new CopyColumnAction(getColumnViewer());
		getActionRegistry().registerAction(copyColumnAction);
		
		IAction pasteAction = new PasteAction(getColumnViewer(), getEditingDomain(),
				MetadataPackage.Literals.METADATA_CATEGORY, MetadataPackage.Literals.STANDARD_FIELD);
		getActionRegistry().registerAction(pasteAction);
		getSelectionActions().add(pasteAction.getId());
		getClipboardActions().add(pasteAction.getId());
		categoryWareActions.add(pasteAction.getId());
		
		IAction actionValidate = new ValidateAction(getFormPage());
		getActionRegistry().registerAction(actionValidate);
		actions.add(actionValidate);

		ImportMetadataAction importAction = new ImportMetadataAction(getARESResource(), getColumnViewer(),getEditingDomain());
		getActionRegistry().registerAction(importAction);
		getSelectionActions().add(importAction.getId());
		String dialogTitle	= "������׼�ֶ�";
		String dialogMessage = "����Ŀ�еı�׼�ֶε���(Excel�ļ�).";
		Image dialogImage = AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/stdFieldFile.png").createImage();
		ExportMetadataAction exportAction = new ExportMetadataAction(getARESResource(), getSite(),dialogTitle,dialogImage,dialogMessage);
		getActionRegistry().registerAction(exportAction);
		getSelectionActions().add(exportAction.getId());
		
		for(IAction action: actions) {
			getEditableControl().addEditableUnit(new ActionEditableUnit(action));
		}
		
	}
	
	@Override
	protected String getID() {
		return getClass().getName();
	}

}
