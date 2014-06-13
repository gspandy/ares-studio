/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.ui.editor.page.block;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.basicdata.constant.IBasicDataRestypes;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.procdure.ProcdurePackage;
import com.hundsun.ares.studio.procedure.ui.support.ProcedureRelatedBasicDataInfoContentProposalProvider;
import com.hundsun.ares.studio.procedure.ui.support.ProcedureRelatedBasicDataInfoSupportHelper;
import com.hundsun.ares.studio.procedure.ui.support.ProcedureRelatedInfoColumnLabelProvider;
import com.hundsun.ares.studio.procedure.ui.support.ProcedureRelatedTableInfoContentProposalProvider;
import com.hundsun.ares.studio.procedure.ui.support.ProcedureRelatedTableInfoSupportHelper;
import com.hundsun.ares.studio.ui.ColumnViewerHoverToolTip;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerAddAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerCopyAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerDeleteAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerInsertAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerMoveBottomAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerMoveDownAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerMoveTopAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerMoveUpAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerPasteAction;
import com.hundsun.ares.studio.ui.editor.actions.IActionIDConstant;
import com.hundsun.ares.studio.ui.editor.blocks.TreeViewerBlock;
import com.hundsun.ares.studio.ui.editor.editable.ActionEditableUnit;
import com.hundsun.ares.studio.ui.editor.editingsupport.JresTextEditingSupportWithContentAssist;
import com.hundsun.ares.studio.ui.editor.editingsupport.TextEditingSupport;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelColumnViewerProblemView;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelUtils;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnViewerProblemView;
import com.hundsun.ares.studio.ui.validate.IProblemPool;

/**
 * ������Ϣblock
 * 
 * @author qinyuan
 *
 */
public class RelatedInfoBlock extends TreeViewerBlock{
	
	protected EditingDomain editingDomain;
	protected IARESResource resource;
	protected IProblemPool problemPool;
	protected EReference reference;
	
	//������ť
	private ColumnViewerAddAction addAction;
	private ColumnViewerInsertAction insertAction;
	private ColumnViewerMoveUpAction moveUpAction;
	private ColumnViewerMoveDownAction moveDownAction;
	private ColumnViewerMoveTopAction moveTopAction;
	private ColumnViewerMoveBottomAction moveBottomAction;
	private ColumnViewerPasteAction pasteAction;
	
	/**
	 * 
	 */
	public RelatedInfoBlock(EReference reference, EditingDomain editingDomain, IARESResource resource, IProblemPool problemPool) {
		super();
		this.reference = reference;
		this.editingDomain = editingDomain;
		this.resource = resource;
		this.problemPool = problemPool;
	}
	
	@Override
	protected void addToolTipSupport(TreeViewer viewer) {
		ColumnViewerHoverToolTip.enableFor(viewer);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock#getID()
	 */
	@Override
	protected String getID() {
		return getClass().getName();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock#getColumnViewerContentProvider()
	 */
	@Override
	protected IContentProvider getColumnViewerContentProvider() {
		return new ProcedureRelatedInfoContentProvider(reference);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock#createMenus(org.eclipse.jface.action.IMenuManager)
	 */
	@Override
	protected void createMenus(IMenuManager menuManager) {
		IAction action = getActionRegistry().getAction(IActionIDConstant.CV_ADD);
		menuManager.add(action);
		
		menuManager.add(new Separator());
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		menuManager.add(action);
		
		menuManager.add(new Separator());
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_COPY);
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
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock#createActions()
	 */
	@Override
	protected void createActions() {
		super.createActions();
		
		addAction = new ColumnViewerAddAction(getColumnViewer(), editingDomain, null,reference,ProcdurePackage.Literals.RELATED_INFO);
		getActionRegistry().registerAction(addAction);
		getSelectionActions().add(addAction.getId());
		
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
				reference,
				ProcdurePackage.Literals.RELATED_INFO);
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
		
		pasteAction =  new ColumnViewerPasteAction(getColumnViewer(), editingDomain, null, reference);
		getActionRegistry().registerAction(pasteAction);
		getClipboardActions().add(pasteAction.getId());
		
		//ֻ������
		getEditableControl().addEditableUnit(new ActionEditableUnit(addAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(delAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(moveTopAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(moveUpAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(moveDownAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(insertAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(moveBottomAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(pasteAction));
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock#createColumns(org.eclipse.jface.viewers.ColumnViewer)
	 */
	@Override
	protected void createColumns(TreeViewer viewer) {
		EObjectColumnViewerProblemView problemView = new EObjectColumnViewerProblemView(viewer);
		// ������չ��
		EObjectColumnViewerProblemView exProblemView = new ExtensibleModelColumnViewerProblemView(viewer);
		//  ��־λ, "������", "������", "����", "java����", "��׼�ֶ�", "ֵ", "����" 
		// 80, 100, 100, 100, 100, 100, 100, 120
		{
			// ����������
			EAttribute attribute = ProcdurePackage.Literals.RELATED_INFO__ID;
			
			// ���������
			TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
			column.getColumn().setText("������ԴӢ����");
			column.getColumn().setWidth(100);
			
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new ProcedureRelatedInfoColumnLabelProvider(resource,reference,attribute);
			provider.setDiagnosticProvider(problemView);
			column.setLabelProvider(provider);
			
			if(ProcdurePackage.Literals.PROCEDURE__RELATED_TABLE_INFO.equals(reference)){//������
				// 3. ����EditingSupport, 
				ProcedureRelatedTableInfoSupportHelper helper = new ProcedureRelatedTableInfoSupportHelper();
				ProcedureRelatedTableInfoContentProposalProvider proposalProvider = 
					new ProcedureRelatedTableInfoContentProposalProvider(helper, IDatabaseRefType.Table, resource.getARESProject());
				// ���ñ༭֧��
				JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(viewer, attribute,proposalProvider);
//			TextEditingSupport es = new TextEditingSupport(viewer, attribute);
				es.setDecorator(new ProcedureRelatedInfoEditingSupportDecorator(attribute));
				column.setEditingSupport(es);
			}else if(ProcdurePackage.Literals.PROCEDURE__RELATED_BASIC_DATA_INFO.equals(reference)) {//������������
				ProcedureRelatedBasicDataInfoSupportHelper helper = new ProcedureRelatedBasicDataInfoSupportHelper();
				ProcedureRelatedBasicDataInfoContentProposalProvider proposalProvider = 
					new ProcedureRelatedBasicDataInfoContentProposalProvider(helper, IBasicDataRestypes.singleTable, resource.getARESProject());
				JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(viewer, attribute,proposalProvider);
//				TextEditingSupport es = new TextEditingSupport(viewer, attribute);
				es.setDecorator(new ProcedureRelatedInfoEditingSupportDecorator(attribute));
				column.setEditingSupport(es);
			}
		}
		{
			// ����������
			EAttribute attribute = ProcdurePackage.Literals.RELATED_INFO__COMMENT;
			
			// ���������
			TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
			column.getColumn().setText("������Դ������");
			column.getColumn().setWidth(250);
			
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new ProcedureRelatedInfoColumnLabelProvider(resource,reference,attribute);
			provider.setDiagnosticProvider(problemView);
			column.setLabelProvider(provider);
			
			// ���ñ༭֧��
			TextEditingSupport es = new TextEditingSupport(viewer, attribute);
			es.setDecorator(new ProcedureRelatedInfoEditingSupportDecorator(attribute));
			column.setEditingSupport(es);
		}
		{
			// ����������
			EAttribute attribute = ProcdurePackage.Literals.RELATED_INFO__PATH;
			
			// ���������
			TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
			column.getColumn().setText("������Դ·��");
			column.getColumn().setWidth(300);
			
			// ���ñ�ǩ�ṩ��
			EObjectColumnLabelProvider provider = new ProcedureRelatedInfoColumnLabelProvider(resource,reference,attribute);
			provider.setDiagnosticProvider(problemView);
			column.setLabelProvider(provider);
			
			// ���ñ༭֧��
			TextEditingSupport es = new TextEditingSupport(viewer, attribute);
			es.setDecorator(new ProcedureRelatedInfoEditingSupportDecorator(attribute));
			column.setEditingSupport(es);
		}
		
		// ��չ��Ϣ
		ExtensibleModelUtils.createExtensibleModelTreeViewerColumns(
				viewer, resource, BizPackage.Literals.PARAMETER, exProblemView);
		
		if (this.problemPool != null) {
			this.problemPool.addView(problemView);
			this.problemPool.addView(exProblemView);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock#createToolbarItems(org.eclipse.jface.action.ToolBarManager)
	 */
	@Override
	protected void createToolbarItems(ToolBarManager buttonManager) {
		// ������ť�б�
		IAction action = getActionRegistry().getAction(IActionIDConstant.CV_ADD);
		buttonManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_INSERT);
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
		addAction.setOwner((EObject) input);
		insertAction.setOwner((EObject) input);
		moveDownAction.setOwner((EObject) input);
		moveUpAction.setOwner((EObject) input);
		moveBottomAction.setOwner((EObject) input);
		moveTopAction.setOwner((EObject) input);
		pasteAction.setOwner((EObject) input);
		super.setInput(input);
	}
}
