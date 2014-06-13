package com.hundsun.ares.studio.ui.editor.blocks;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.forms.editor.FormPage;

import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.model.util.RevisionHistoryUtil;
import com.hundsun.ares.studio.core.model.util.RevisionHistoryVersion;
import com.hundsun.ares.studio.ui.editor.actions.AddRevisionHistoryAction;
import com.hundsun.ares.studio.ui.editor.actions.IActionIDConstant;
import com.hundsun.ares.studio.ui.editor.editable.IEditableControl;
import com.hundsun.ares.studio.ui.editor.editable.IEditableUnit;
import com.hundsun.ares.studio.ui.validate.IProblemPool;

/**
 * ��Ŀ�����е��޶���¼
 * @author sundl
 */
public class ProjectRevisionHistoryListViewerBlock extends RevisionHistoryListViewerBlock {

	private AddRevisionHistoryAction increaseVersionAction;
	
	public ProjectRevisionHistoryListViewerBlock(FormPage page, EditingDomain editingDomain,IProblemPool pool) {
		super(page, editingDomain, page.getEditorSite(), null, pool);
		
		setEditableControl(new IEditableControl() {
			@Override
			public void refreshResourceReadonlyStatus() {}
			@Override
			public void refreshAllUnit(Map<Object, Object> context) {}
			@Override
			public void putUserStatus(String key, Object status) {}
			@Override
			public void notifyUserStatus(String key) {}
			@Override
			public boolean getResourceReadonlyStatus() {return false;}
			@Override
			public void addEditableUnit(IEditableUnit unit) {}
		});
	}

	@Override
	protected void createActions() {
		super.createActions();
		
		increaseVersionAction = new AddRevisionHistoryAction(getColumnViewer(), getEditingDomain(), null, CorePackage.Literals.PROJECT_REVISION_HISTORY_PROPERTY__HISTORIES) {
			@SuppressWarnings("unchecked")
			@Override
			protected String getVersion() {
				String maxVersion = RevisionHistoryUtil.getMaxVersion((List<RevisionHistory>)info.eGet(eReference));
				String basicVer = ((ProjectRevisionHistoryPage)getFormPage()).getInfo().getVersion();
				if (maxVersion == null) {
					if (StringUtils.isNotBlank(basicVer)) {
						return basicVer;
					}
					return "1.0.0.0";
				}else {
					int res = RevisionHistoryUtil.compare(maxVersion, basicVer);
					if (res < 0) {
						maxVersion = basicVer;
					}
				}
				
				RevisionHistoryVersion version = new RevisionHistoryVersion(maxVersion);
				version = new RevisionHistoryVersion(version.getMajor(), version.getMinor(), version.getMicro(), version.getQualifier() + 1);
				return version.toString();
			}
		};
		increaseVersionAction.setId("project.increaseversion");
		increaseVersionAction.setText("�Ӱ汾");
		getActionRegistry().registerAction(increaseVersionAction);
		getSelectionActions().add("project.increaseversion");
		getEditableControl().addEditableUnit(new HistoryActionEditableUnit(increaseVersionAction));
	}
	
	/**
	 * Ϊ�˸������block, ���ܻ����ڲ�ͬ�Ķ����ϣ�������Ҫ�������¶����޶���¼�б�����Ӧ��EMF����
	 * @return
	 */
	@Override
	protected EReference getReference() {
		return CorePackage.Literals.PROJECT_REVISION_HISTORY_PROPERTY__HISTORIES;
	}
	
	@Override
	public void setInput(Object input) {
		// �˴�����������super.setInput()����ΪsetInput��ˢ�¿���״̬������Ҫ��ˢ��֮ǰ��action�ı�Ҫ��Ϣ׼���á�
		increaseVersionAction.setInfo((EObject) input);
		super.setInput(input);
	}
	
	@Override
	protected void createRevisionActions() {
	}
	
	@Override
	protected void createToolbarItems(ToolBarManager buttonManager) {
		IAction action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_UP);
		buttonManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_DOWN);
		buttonManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		buttonManager.add(action);
		
		buttonManager.add(increaseVersionAction);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerListPage#createMenus(org.eclipse.jface.action.IMenuManager)
	 */
	@Override
	protected void createMenus(IMenuManager menuManager) {
		IAction action = getActionRegistry().getAction(IActionIDConstant.CV_INSERT);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		menuManager.add(action);
		
		menuManager.add(new Separator());
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_COPY);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_PASTE);
		menuManager.add(action);
		
		menuManager.add(new Separator());

		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_UP);
		menuManager.add(action);
		
		action = getActionRegistry().getAction(IActionIDConstant.CV_MOVE_DOWN);
		menuManager.add(action);

	}
}
