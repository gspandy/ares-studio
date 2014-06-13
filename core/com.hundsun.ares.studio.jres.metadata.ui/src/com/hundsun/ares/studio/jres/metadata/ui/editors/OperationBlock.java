/**
 * Դ�������ƣ�OperationBlock.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IManagedForm;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.ui.actions.AddSampleFactory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;
import com.hundsun.ares.studio.jres.model.metadata.impl.OperationImpl;
import com.hundsun.ares.studio.ui.editor.IEMFFormPage;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerAddAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerDeleteAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerMoveDownAction;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerMoveUpAction;
import com.hundsun.ares.studio.ui.editor.actions.IActionIDConstant;
import com.hundsun.ares.studio.ui.editor.actions.IUpdateAction;
import com.hundsun.ares.studio.ui.editor.blocks.JresBasicMasterDetailsBlock;
import com.hundsun.ares.studio.ui.editor.editable.ActionEditableUnit;
import com.hundsun.ares.studio.ui.editor.editable.IEditableControl;
import com.hundsun.ares.studio.ui.editor.editingsupport.BooleanEditingSupport;
import com.hundsun.ares.studio.ui.editor.editingsupport.TextEditingSupport;
import com.hundsun.ares.studio.ui.editor.viewers.ColumnViewerPatternFilter;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.ReferenceTreeContentProvider;

/**
 * �����û������༭��Block
 * @author qinyuan
 *
 */
public class OperationBlock extends JresBasicMasterDetailsBlock {

	List<IAction> actions = new ArrayList<IAction>();
	private EObject info;
	private TreeViewer viewer;
	private IARESResource res;
	
	private ColumnViewerAddAction addAction;
	private ColumnViewerMoveUpAction moveUpAction;
	private ColumnViewerMoveDownAction moveDownAction;

	/**
	 * @param page
	 */
	public OperationBlock(OperationEditPage page,IEditableControl controler) {
		super(page, controler);
	}
	
	@Override
	protected void createMasterPart(final IManagedForm managedForm, Composite parent) {		
		super.createMasterPart(managedForm, parent);
		
		this.res = page.getEditor().getARESResource();
	}


	public TreeViewer getViewer() {
		return this.viewer;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.page.masterdetail.JresBasicMasterDetailsBlock#getPageHeadName()
	 */
	@Override
	protected String getPageHeadName() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.page.masterdetail.JresBasicMasterDetailsBlock#getSectionName()
	 */
	@Override
	protected String getSectionName() {
		return "�û�����";
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.page.masterdetail.JresBasicMasterDetailsBlock#getDescription()
	 */
	@Override
	protected String getDescription() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.page.masterdetail.JresBasicMasterDetailsBlock#getElementTypes()
	 */
	@Override
	protected Object[] getElementTypes() {
		return new Object[]{OperationImpl.class};
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.page.masterdetail.JresBasicMasterDetailsBlock#getDetailPage(java.lang.Object)
	 */
	@Override
	protected IDetailsPage getDetailPage(Object type) {
		return new OperationDetails();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.page.masterdetail.JresBasicMasterDetailsBlock#createViewer(org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.IManagedForm, com.hundsun.ares.studio.jres.ui.form.EMFFormPage)
	 */
	@Override
	protected ColumnViewer createViewer(Composite client, IManagedForm managedForm, IEMFFormPage page) {
		toolkit = managedForm.getToolkit();
		//��ʼ��ģ��
		info = page.getEditor().getInfo();
		
		FilteredTree table = new FilteredTree(client, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, new ColumnViewerPatternFilter(), true);
		viewer = table.getViewer();//new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		viewer.getTree().setHeaderVisible(true);
		viewer.getTree().setLinesVisible(true);
		createTableColumns();
		
		fillTableContents();
		
		// �������tooltip��ʾ
		ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.RECREATE);
		
		return viewer;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.page.masterdetail.JresBasicMasterDetailsBlock#setMasterLayout(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void setMasterLayout(Composite client) {
		// ���ò���
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(client);
		GridDataFactory.fillDefaults().hint(50, 50).grab(true, true).applyTo(viewer.getControl());
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.page.masterdetail.JresBasicMasterDetailsBlock#createMenus(org.eclipse.jface.action.IMenuManager)
	 */
	//����Ҽ��˵�
	@Override
	protected void createMenus(IMenuManager manager) {
		super.createMenus(manager);
		
		for (IAction action : actions) {
			manager.add(action);
		}
		
		//��������Բ�ͬ�༭��������µĽű�ʾ��
		List<IAction>sampleActionList=AddSampleFactory.getSampleActions(viewer, page.getEditingDomain(),info,res);
		if(sampleActionList.size()>0){
			manager.add(new Separator());
			MenuManager   submenu   =   new   MenuManager( "���ʾ��"); 
			for(IAction action:sampleActionList){
				submenu.add(action);
			}
			manager.add(submenu);
		}
		updateActions();
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerBlock#createActions()
	 */
	@Override
	protected void createActions() {
		super.createActions();
		
		addAction = new ColumnViewerAddAction(viewer, 
				page.getEditingDomain(), 
				info,
				MetadataPackage.Literals.METADATA_RESOURCE_DATA__OPERATIONS,
				MetadataPackage.Literals.OPERATION);
		getActionRegistry().registerAction(addAction);
		getSelectionActions().add(addAction.getId());
		
		IAction delAction = new ColumnViewerDeleteAction(getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(delAction);
		getSelectionActions().add(delAction.getId());
		
		moveUpAction = new ColumnViewerMoveUpAction(getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(moveUpAction);
		getSelectionActions().add(moveUpAction.getId());
		getStackActions().add(moveUpAction.getId());
		
		moveDownAction = new ColumnViewerMoveDownAction(getColumnViewer(), getEditingDomain());
		getActionRegistry().registerAction(moveDownAction);
		getSelectionActions().add(moveDownAction.getId());
		getStackActions().add(moveDownAction.getId());
		
		//ֻ������
		getEditableControl().addEditableUnit(new ActionEditableUnit(addAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(delAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(moveUpAction));
		getEditableControl().addEditableUnit(new ActionEditableUnit(moveDownAction));
	}

	/**
	 * @param buttonManager
	 */
	@Override
	protected void createToolbarItems(ToolBarManager buttonManager) {
		IAction action = getActionRegistry().getAction(IActionIDConstant.CV_ADD);
		buttonManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		buttonManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_UP);
		buttonManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_DOWN);
		buttonManager.add(action);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerBlock#setInput(java.lang.Object)
	 */
	@Override
	public void setInput(Object input) {
		addAction.setOwner((EObject) input);
		moveDownAction.setOwner((EObject) input);
		moveDownAction.setReference( MetadataPackage.Literals.METADATA_RESOURCE_DATA__OPERATIONS);
		moveUpAction.setOwner((EObject) input);
		moveUpAction.setReference( MetadataPackage.Literals.METADATA_RESOURCE_DATA__OPERATIONS);
		super.setInput(input);
	}

	private void updateActions() {
		for (IAction action : actions) {
			if (action instanceof IUpdateAction) {
				((IUpdateAction) action).update();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#createToolBarActions(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		
	}
	
	/**
	 * 
	 */
	private void createTableColumns() {
		//TODO#�����߼�#��Ԫ#��ͨ#����#����״̬ #���ʱ�� #������(�������հ��к�ע����) #��ʱ(��ȷ������) #��һ�����ؼ������û�������ע�����ϵͳ�ֶ���չ֧��
		{
			TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
			column.getColumn().setText("��������");
			column.getColumn().setWidth(100);
			column.setLabelProvider(new EObjectColumnLabelProvider(MetadataPackage.Literals.OPERATION__TITLE));
			column.setEditingSupport(new TextEditingSupport(viewer, MetadataPackage.Literals.OPERATION__TITLE));
		}
		
		{
			TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
			column.getColumn().setText("�Ƿ��Զ�����");
			column.getColumn().setWidth(100);
			column.setLabelProvider(new EObjectColumnLabelProvider(MetadataPackage.Literals.OPERATION__AUTOBUILD));
			column.setEditingSupport(new BooleanEditingSupport(viewer, MetadataPackage.Literals.OPERATION__AUTOBUILD));
		}
		
	}
	private void fillTableContents() {
		
		EReference reference = MetadataPackage.Literals.METADATA_RESOURCE_DATA__OPERATIONS;
		
		viewer.setContentProvider(new ReferenceTreeContentProvider(reference));

		if(info instanceof MetadataResourceData) {
			viewer.setInput(info);
		}
	}

}
