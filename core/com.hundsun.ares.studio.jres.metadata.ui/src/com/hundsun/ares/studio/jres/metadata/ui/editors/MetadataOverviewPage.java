/**
 * Դ�������ƣ�MetadataOverviewPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.editors;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.ui.actions.IMetadataActionIDConstant;
import com.hundsun.ares.studio.jres.metadata.ui.actions.JumpAction;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataOverviewContentProvider;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.actions.ButtonGroupManager;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerListPage;
import com.hundsun.ares.studio.ui.editor.viewers.ColumnViewerPatternFilter;
import com.hundsun.ares.studio.ui.viewers.link.CellLinkColumnViewerEditorActivationStrategy;
import com.hundsun.ares.studio.ui.viewers.link.CellLinkMouseListener;
import com.hundsun.ares.studio.ui.viewers.link.ICellLinkProvider;

/**
 * @author wangxh
 *Ԫ��������ҳ����
 */
public abstract class MetadataOverviewPage extends ColumnViewerListPage implements ICellLinkProvider{

	private static final String ID_REFRESH = "MetadataOverviewPage_Refresh";
	
	public MetadataOverviewPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	protected Control createColumnViewer(Composite parent, FormToolkit toolkit) {
		FilteredTree tree = new FilteredTree(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, new ColumnViewerPatternFilter(), true);
		
		toolkit.adapt(tree);
		tree.getViewer().getTree().setHeaderVisible(true);
		tree.getViewer().getTree().setLinesVisible(true);
		setColumnViewer(tree.getViewer());
		
		configureColumnViewer(getColumnViewer());
		
		return tree;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#validate()
	 */
	@Override
	public void validate() {
		// ����Ҫ������
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerListPage#configureColumnViewer(org.eclipse.jface.viewers.ColumnViewer)
	 */
	@Override
	protected void configureColumnViewer(ColumnViewer viewer) {
		super.configureColumnViewer(viewer);
		TreeViewer treeViewer = (TreeViewer) viewer;
		// ��ֹ��Ctrl���ֱ༭��
		TreeViewerEditor.create(treeViewer, 
				new CellLinkColumnViewerEditorActivationStrategy(treeViewer), ColumnViewerEditor.DEFAULT);
		
		treeViewer.getTree().addMouseListener(new CellLinkMouseListener(viewer, this));
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerListPage#createMenus(org.eclipse.jface.action.IMenuManager)
	 */
	@Override
	protected void createMenus(IMenuManager menuManager) {
		IAction action = getActionRegistry().getAction(ID_REFRESH);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IMetadataActionIDConstant.CV_JUMP);
		menuManager.add(action);
	}

	@Override
	protected void createActions() {
		super.createActions();
		IAction refreshAction = new Action("ˢ��"){
			@Override
			public void run() {
				getColumnViewer().refresh();
			}
		};
		refreshAction.setId(ID_REFRESH);
		getActionRegistry().registerAction(refreshAction);
		getSelectionActions().add(refreshAction.getId());
		
		IAction jumpAction = new JumpAction(getColumnViewer());
		getActionRegistry().registerAction(jumpAction);
		getSelectionActions().add(jumpAction.getId());
	}

	@Override
	protected void createButtons(ButtonGroupManager manager) {
		super.createButtons(manager);
		IAction action = getActionRegistry().getAction(ID_REFRESH);
		manager.add(action);
	}


	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerListPage#getColumnViewerContentProvider()
	 */
	@Override
	protected IContentProvider getColumnViewerContentProvider() {
		return new MetadataOverviewContentProvider(getARESResource(), getReferenceType());
	}
	
	protected abstract String getReferenceType();
	
	protected IARESResource getARESResource() {
		return getEditor().getARESResource();
	}
}
