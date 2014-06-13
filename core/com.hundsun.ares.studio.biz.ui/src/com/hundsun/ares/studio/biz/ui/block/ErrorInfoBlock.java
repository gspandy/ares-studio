/**
 * Դ�������ƣ�ErrorInfoBlock.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.ui.block;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;

import com.hundsun.ares.studio.biz.BizInterface;
import com.hundsun.ares.studio.biz.ui.editor.page.ErrnoColumnLabelProvider;
import com.hundsun.ares.studio.biz.ui.editor.page.ErrnoContentPropasolHelper;
import com.hundsun.ares.studio.biz.ui.editor.page.ErrnoContentProposalProvider;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.metadata.ui.actions.CopyAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.IMetadataActionIDConstant;
import com.hundsun.ares.studio.jres.metadata.ui.actions.PasteAction;
import com.hundsun.ares.studio.jres.metadata.ui.actions.RemoveAction;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataHeaderColumnLabelProvider;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerAddAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerDeleteAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerMoveDownAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerMoveUpAction;
import com.hundsun.ares.studio.ui.editor.actions.CopyCellAction;
import com.hundsun.ares.studio.ui.editor.actions.CopyColumnAction;
import com.hundsun.ares.studio.ui.editor.actions.IActionIDConstant;
import com.hundsun.ares.studio.ui.editor.blocks.TableViewerBlock;
import com.hundsun.ares.studio.ui.editor.editable.ActionEditableUnit;
import com.hundsun.ares.studio.ui.editor.editingsupport.JresTextEditingSupportWithContentAssist;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelUtils;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnViewerProblemView;
import com.hundsun.ares.studio.ui.validate.IProblemPool;

/**
 * @author sundl
 *
 */
public class ErrorInfoBlock extends TableViewerBlock{

	// �༭��Parameter��Ӧ������ĸ����ԣ� 
	// ���������������������Ӧ��������������Reference������Ƕ������ԣ����Ƕ����������Reference.
	protected EReference reference;
	
	ColumnViewerAddAction addAction;

	public ErrorInfoBlock(EReference reference, EditingDomain editingDomain, IARESResource resource,IProblemPool problemPool) {
		this.reference = reference;
		this.editingDomain = editingDomain;
		this.resource = resource;
		this.problemPool = problemPool;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock#getID()
	 */
	@Override
	protected String getID() {
		return getClass().getName();
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
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock#getColumnViewerContentProvider()
	 */
	@Override
	protected IContentProvider getColumnViewerContentProvider() {
		return new ErrorInfoContentProvider();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock#createColumns(org.eclipse.jface.viewers.ColumnViewer)
	 */
	@Override
	protected void createColumns(TableViewer viewer) {
		EObjectColumnViewerProblemView problemView = new EObjectColumnViewerProblemView(viewer);
		IARESProject project = resource.getARESProject();
		/**�����*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.ERROR_NO_ITEM__NO;
			
			// ���������
			TableViewerColumn column = new TableViewerColumn(viewer, SWT.LEFT);
			column.getColumn().setText("�����");
			column.getColumn().setWidth(200);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new MetadataHeaderColumnLabelProvider(attribute , getARESResource()){
				@Override
				public String getText(Object element) {
					EAttribute attribute = MetadataPackage.Literals.ERROR_NO_ITEM__NO;
					if (element instanceof MetadataCategory) {
						attribute = MetadataPackage.Literals.NAMED_ELEMENT__NAME;
					}
					EObject owner = getOwner(element);
					if (attribute != null && owner != null && owner.eClass().getEAllAttributes().contains(attribute)) {
						Object value = owner.eGet(attribute);
						if (value == null) {
							value = "";
						}
						return String.valueOf(value );
					}
					return "";
				}
			};
			provider.setDiagnosticProvider(problemView);
			column.setLabelProvider(provider);
			
			// ���ñ༭֧��
			ErrnoContentPropasolHelper helper = new ErrnoContentPropasolHelper();
			ErrnoContentProposalProvider cpProvider = new ErrnoContentProposalProvider(helper, IMetadataRefType.ErrNo_No, project);
			
			JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(
					viewer,
					attribute, 
					cpProvider);
			column.setEditingSupport(es);
			
			column.getColumn().setMoveable(true);
		}
		/**������ʾ��Ϣ*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.ERROR_NO_ITEM__MESSAGE;
			
			// ���������
			TableViewerColumn comlumn = new TableViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("������Ϣ");
			comlumn.getColumn().setWidth(200);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new ErrnoColumnLabelProvider(attribute, project);
			provider.setDiagnosticProvider(problemView);
			comlumn.setLabelProvider(provider);
			
			// ���ñ༭֧��
//			TextEditingSupport es = new TextEditingSupport(viewer, attribute);
//			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
//			comlumn.setEditingSupport(es);
			comlumn.getColumn().setMoveable(true);
		}

		/**���󼶱�*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.ERROR_NO_ITEM__LEVEL;
			
			// ���������
			TableViewerColumn comlumn = new TableViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("���󼶱�");
			comlumn.getColumn().setWidth(100);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new ErrnoColumnLabelProvider(attribute, project);
			provider.setDiagnosticProvider(problemView);
			comlumn.setLabelProvider(provider);
			
			// ���ñ༭֧��
//			TextEditingSupport es = new TextEditingSupport(viewer, attribute);
//			//es.setDecorator(new MetadataItemEditingSupportDecorator(attribute));
//			comlumn.setEditingSupport(es);
			comlumn.getColumn().setMoveable(true);
		}
		
		/**��ע*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION;
			
			// ���������
			TableViewerColumn comlumn = new TableViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("˵��");
			comlumn.getColumn().setWidth(200);
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new ErrnoColumnLabelProvider(attribute, project);
			provider.setDiagnosticProvider(problemView);
			comlumn.setLabelProvider(provider);
			
			// ���ñ༭֧��
//			TextEditingSupport es = new LongTextEditingSupport(viewer, attribute);
//			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,getARESResource()));
//			comlumn.setEditingSupport(es);
			comlumn.getColumn().setMoveable(true);
		}
		
		// ������չ��֧��
		ExtensibleModelUtils.createExtensibleModelTableViewerColumns(getColumnViewer(), getARESResource(), 
				MetadataPackage.Literals.ERROR_NO_ITEM, problemView);
		
//		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(viewer.getControl()));
		getProblemPool().addView(problemView);
	}
	
	@Override
	protected void createActions() {
		//ע�����е�action
		super.createActions();
		List<IAction> actions = new ArrayList<IAction>();
		
		addAction = new ColumnViewerAddAction(
				getColumnViewer(), 
				getEditingDomain(), 
				MetadataPackage.Literals.ERROR_NO_ITEM);
		getActionRegistry().registerAction(addAction);
		getSelectionActions().add(addAction.getId());
		actions.add(addAction);
		
		IAction action = new RemoveAction(getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());
		actions.add(action);
		
		action = new ColumnViewerDeleteAction(getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());
		actions.add(action);
		
		action = new ColumnViewerMoveUpAction(getColumnViewer(), getEditingDomain(), null, this.reference);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());
		getStackActions().add(action.getId());
		actions.add(action);
		
		action = new ColumnViewerMoveDownAction(getColumnViewer(), getEditingDomain(),null, this.reference);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());
		getStackActions().add(action.getId());
		actions.add(action);
		
		IAction copyAction = new CopyAction(getColumnViewer());
		getActionRegistry().registerAction(copyAction);
		getSelectionActions().add(copyAction.getId());
		
		IAction copyCellAction = new CopyCellAction(getColumnViewer());
		getActionRegistry().registerAction(copyCellAction);
		
		IAction copyColumnAction = new CopyColumnAction(getColumnViewer());
		getActionRegistry().registerAction(copyColumnAction);
		
		IAction pasteAction = new PasteAction(getColumnViewer(), getEditingDomain(),
				MetadataPackage.Literals.METADATA_CATEGORY, MetadataPackage.Literals.ERROR_NO_ITEM);
		getActionRegistry().registerAction(pasteAction);
		getSelectionActions().add(pasteAction.getId());
		getClipboardActions().add(pasteAction.getId());
	
		for (IAction item : actions ) {
			getEditableControl().addEditableUnit(new ActionEditableUnit(item));
		}
		
	}
	
	public void setInput(Object input) {
		addAction.setOwner((BizInterface) input);
		addAction.setReference(reference);
		super.setInput(input);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.MetadataListPage#createButtons(com.hundsun.ares.studio.jres.ui.actions.ButtonGroupManager)
	 */
	@Override
	protected void createToolbarItems(ToolBarManager manager) {
		
		IAction
		action = getActionRegistry().getAction(IActionIDConstant.CV_ADD);
		manager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		manager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_UP);
		manager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_DOWN);
		manager.add(action);
		
	}

	@Override
	protected void createMenus(IMenuManager menuManager) {
		//��������ű༭�����Ҽ��˵�
		IAction action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_ADD_ITEM);
		menuManager.add(action);
		
		menuManager.add(new Separator());
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		menuManager.add(action);
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_REMOVE);
		menuManager.add(action);
		menuManager.add(new Separator());
		
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
		
		menuManager.add(new Separator());

		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_INSERT_ITEM);
		menuManager.add(action);
	}
}
