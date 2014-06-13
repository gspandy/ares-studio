package com.hundsun.ares.studio.ui.editor.blocks;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.model.CoreFactory;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.model.util.RevisionHistoryUtil;
import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;
import com.hundsun.ares.studio.ui.editor.actions.IActionIDConstant;
import com.hundsun.ares.studio.ui.editor.editingsupport.LongTextEditingSupport;
import com.hundsun.ares.studio.ui.editor.editingsupport.TextEditingSupport;
import com.hundsun.ares.studio.ui.editor.viewers.ArrayTreeContentProvider;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;

public class BatchAddModifyLogBlock extends TreeViewerBlock {

	IARESProject project;
	
	@Override
	protected String getID() {
		return getClass().getName();
	}
	
	public void setProject(IARESProject project) {
		this.project = project;
	}

	@Override
	protected IContentProvider getColumnViewerContentProvider() {
		return ArrayTreeContentProvider.getInstance();
	}

	@Override
	protected void createMenus(IMenuManager manager) {
		IAction action = getActionRegistry().getAction(IActionIDConstant.CV_ADD);
		manager.add(action);
		action = getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		manager.add(action);
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_UP);
		manager.add(action);
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_DOWN);
		manager.add(action);
	}
	
	@Override
	protected void createToolbarItems(ToolBarManager manager) {
		IAction action = getActionRegistry().getAction(IActionIDConstant.CV_ADD);
		manager.add(action);
		action = getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		manager.add(action);
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_UP);
		manager.add(action);
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_DOWN);
		manager.add(action);
	}

	@Override
	protected void createColumns(TreeViewer viewer) {

		//����У��޶�ʱ�䡢�޶��汾���޶����š������ˡ������ˡ��޸�ԭ���޸�����
		
		/**�޶�ʱ��*/
		{
			TreeViewerColumn comlumn = new TreeViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("�޶�ʱ��");
			comlumn.getColumn().setWidth(130);
			EObjectColumnLabelProvider provider = new EObjectColumnLabelProvider(CorePackage.Literals.REVISION_HISTORY__MODIFIED_DATE);
			comlumn.setLabelProvider(provider);
			comlumn.setEditingSupport(new TextEditingSupport(viewer, CorePackage.Literals.REVISION_HISTORY__MODIFIED_DATE));
		}
		/**�޶��汾*/
		{
			TreeViewerColumn comlumn = new TreeViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("�޶��汾");
			comlumn.getColumn().setWidth(160);
			EObjectColumnLabelProvider provider = new EObjectColumnLabelProvider(CorePackage.Literals.REVISION_HISTORY__VERSION);
			comlumn.setLabelProvider(provider);
			comlumn.setEditingSupport(new TextEditingSupport(viewer, CorePackage.Literals.REVISION_HISTORY__VERSION));
			comlumn.getColumn().setMoveable(true);
		}
		/**�޶�����*/
		{
			TreeViewerColumn comlumn = new TreeViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("�޶�����");
			comlumn.getColumn().setWidth(160);
			EObjectColumnLabelProvider provider = new EObjectColumnLabelProvider(CorePackage.Literals.REVISION_HISTORY__ORDER_NUMBER);
			comlumn.setLabelProvider(provider);
			comlumn.setEditingSupport(new TextEditingSupport(viewer, CorePackage.Literals.REVISION_HISTORY__ORDER_NUMBER));
			comlumn.getColumn().setMoveable(true);
		}
		
		/**������*/
		{
			TreeViewerColumn comlumn = new TreeViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("������");
			comlumn.getColumn().setWidth(100);
			EObjectColumnLabelProvider provider = new EObjectColumnLabelProvider(CorePackage.Literals.REVISION_HISTORY__MODIFIED_BY);
			comlumn.setLabelProvider(provider);
			comlumn.setEditingSupport(new TextEditingSupport(viewer, CorePackage.Literals.REVISION_HISTORY__MODIFIED_BY));
			comlumn.getColumn().setMoveable(true);
		}
		/**������ */
		{
			TreeViewerColumn comlumn = new TreeViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("������ ");
			comlumn.getColumn().setWidth(100);
			comlumn.setLabelProvider(new EObjectColumnLabelProvider(CorePackage.Literals.REVISION_HISTORY__CHARGER));
			comlumn.setEditingSupport(new TextEditingSupport(viewer, CorePackage.Literals.REVISION_HISTORY__CHARGER));
			comlumn.getColumn().setMoveable(true);
		}
		
		/**�޸�����*/
		{
			TreeViewerColumn comlumn = new TreeViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("�޸�����");
			comlumn.getColumn().setWidth(200);
			comlumn.setLabelProvider(new EObjectColumnLabelProvider(CorePackage.Literals.REVISION_HISTORY__MODIFIED));
			comlumn.setEditingSupport(new LongTextEditingSupport(viewer, CorePackage.Literals.REVISION_HISTORY__MODIFIED));
			comlumn.getColumn().setMoveable(true);
		}
		
		/**�޸�ԭ��*/
		{
			TreeViewerColumn comlumn = new TreeViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("�޸�ԭ��");
			comlumn.getColumn().setWidth(200);
			comlumn.setLabelProvider(new EObjectColumnLabelProvider(CorePackage.Literals.REVISION_HISTORY__MODIFIED_REASON));
			comlumn.setEditingSupport(new LongTextEditingSupport(viewer, CorePackage.Literals.REVISION_HISTORY__MODIFIED_REASON));
			comlumn.getColumn().setMoveable(true);
		}
		/**��ע*/
		{
			TreeViewerColumn comlumn = new TreeViewerColumn(viewer, SWT.LEFT);
			comlumn.getColumn().setText("��ע");
			comlumn.getColumn().setWidth(200);
			EObjectColumnLabelProvider provider = new EObjectColumnLabelProvider(CorePackage.Literals.REVISION_HISTORY__COMMENT);
			comlumn.setLabelProvider(provider);
			comlumn.setEditingSupport(new LongTextEditingSupport(viewer, CorePackage.Literals.REVISION_HISTORY__COMMENT));
			comlumn.getColumn().setMoveable(true);
		}
		
	
	}

	@Override
	protected void createActions() {
		IAction action = new AddAction();
		getActionRegistry().registerAction(action);
		
		action = new DeleteAction();
		getActionRegistry().registerAction(action);
		
		action = new MoveupAction();
		getActionRegistry().registerAction(action);
		
		action = new MovedownAction();
		getActionRegistry().registerAction(action);
	}
	
	public class MovedownAction extends Action{
		public MovedownAction() {
			setText("����");
			setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(ARESEditorPlugin.PLUGIN_ID, "icons/full/obj16/down.gif"));
			setId(IActionIDConstant.CV_MOVE_DOWN);
		}

		@Override
		public void run() {
			IStructuredSelection selection = (IStructuredSelection) getColumnViewer().getSelection();
			List<RevisionHistory>viewerInput = (List<RevisionHistory>) getColumnViewer().getInput();
			if (selection != null && !selection.isEmpty()) {
				Object[] objs = selection.toArray();
				for (int i = objs.length - 1; i >= 0; i--) {
					
					int oldIndex = viewerInput.indexOf(objs[i]);
					// �Ѿ�����ˣ�����������
					
					if (oldIndex == viewerInput.size() - 1) {
						
						continue;
					}
					
					// ���ܳ���һ��ѡ�е�
					if (i < objs.length - 1 && viewerInput.get(oldIndex + 1) == objs[i + 1]) {
						
						continue;
					}
					
					RevisionHistory tmp = viewerInput.get(oldIndex + 1);
					RevisionHistory item1 = CoreFactory.eINSTANCE.createRevisionHistory();
					RevisionHistory item2 = CoreFactory.eINSTANCE.createRevisionHistory();
					viewerInput.set(oldIndex + 1, item1);
					viewerInput.set(oldIndex, item2);
					viewerInput.set(oldIndex + 1, (RevisionHistory) objs[i]);
					viewerInput.set(oldIndex, tmp);
					
				}
			}
			getColumnViewer().setInput(viewerInput);
		}

	}
	
	private class MoveupAction extends Action{
		public MoveupAction() {
			setText("����");
			setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(ARESEditorPlugin.PLUGIN_ID, "icons/full/obj16/up.gif"));
			setId(IActionIDConstant.CV_MOVE_UP);
		}
		@Override
		public void run() {
			IStructuredSelection selection = (IStructuredSelection) getColumnViewer().getSelection();
			List<RevisionHistory>viewerInput = (List<RevisionHistory>) getColumnViewer().getInput();
			if (selection != null && !selection.isEmpty()) {
				Object[] objs = selection.toArray();
				for (int i = 0; i < objs.length; i++) {
					
					int oldIndex = viewerInput.indexOf(objs[i]);
					
					// �Ѿ���ˣ�����������
					if (oldIndex == 0) {
						continue;
					}
					
					// ���ܳ���һ��ѡ�е�
					if (i > 0 && viewerInput.get(oldIndex - 1) == objs[i - 1]) {
						continue;
					}
					
					RevisionHistory tmp = viewerInput.get(oldIndex - 1);
					RevisionHistory item1 = CoreFactory.eINSTANCE.createRevisionHistory();
					RevisionHistory item2 = CoreFactory.eINSTANCE.createRevisionHistory();
					viewerInput.set(oldIndex - 1, item1);
					viewerInput.set(oldIndex, item2);
					viewerInput.set(oldIndex - 1, (RevisionHistory) objs[i]);
					viewerInput.set(oldIndex, tmp);
					
				}
			}
			
			getColumnViewer().setInput(viewerInput);
		}
	}

	private class DeleteAction extends Action{
		public DeleteAction() {
			setText("ɾ��");
			
			setId(IActionIDConstant.CV_DELETE);
			
			ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
			setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
			setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		}
		@Override
		public void run() {
			IStructuredSelection select = (IStructuredSelection) getColumnViewer().getSelection();
			List<RevisionHistory>viewerInput = (List<RevisionHistory>) getColumnViewer().getInput();
			List<Object> selectObjs = select.toList();
			viewerInput.removeAll(selectObjs);
			getColumnViewer().refresh();
		}
	}

	private class AddAction extends Action{
		public AddAction() {
			setText("����޸ļ�¼");
			setId(IActionIDConstant.CV_ADD);
			setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		}
		@Override
		public void run() {
			IStructuredSelection select = (IStructuredSelection) getColumnViewer().getSelection();
			List<RevisionHistory>viewerInput = (List<RevisionHistory>) getColumnViewer().getInput();
			RevisionHistory rev = CoreFactory.eINSTANCE.createRevisionHistory();
			// ��Ŀ����
			String version = RevisionHistoryUtil.getProjectPropertyVersion(project);
			
			// ��һ���Ҳ����κμ�¼��ʱ��
			if (StringUtils.isEmpty(version)) {
				version = "1.0.0.0";
			}
			rev.setVersion(version);
			Object obj = select.getFirstElement();
			if(obj != null){
				int index = viewerInput.indexOf(obj);
				viewerInput.add(index, rev);
			}else{
				viewerInput.add(rev);
			}
			getColumnViewer().refresh();
		}
	} 
}
