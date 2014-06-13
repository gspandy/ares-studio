/**
 * Դ�������ƣ�DefaultColumnViewerActionGroup.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.actions;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ColumnViewer;

/**
 * �����ӡ�ɾ�������ơ����ư�ť
 * @author qinyuan
 *
 */
public class DefaultColumnViewerActionGroup {
	
	private ColumnViewer viewer;
	private EditingDomain editingDomain;
	private EObject model;
	private EReference reference;
	private EClass eclass;
	private ButtonGroupManager btnGroupManager;
	/**
	 * 
	 */
	public DefaultColumnViewerActionGroup(ColumnViewer viewer, EditingDomain editingDomain,
			EObject model, EReference reference, EClass eclass,ButtonGroupManager btnGroupManager) {
		this.viewer = viewer;
		this.editingDomain = editingDomain;
		this.model = model;
		this.reference = reference;
		this.eclass = eclass;
		this.btnGroupManager = btnGroupManager;
	}
	
	/**
	 * ������ӡ�ɾ�������ơ����Ʋ���
	 * @param parent
	 */
	public void createDefaultButton() {
		// ������ť�б�
		IAction action = new ColumnViewerAddAction(viewer, 
				editingDomain,eclass);
		((ColumnViewerAddAction) action).setOwner(model);
		((ColumnViewerAddAction) action).setReference(reference);
		
		action.setText("����");
		btnGroupManager.add(action);
		
		action = new ColumnViewerDeleteAction(viewer, 
				editingDomain);
		action.setText("ɾ��");
		btnGroupManager.add(action);
		
		action = new ColumnViewerMoveUpAction(viewer, editingDomain);
		((ColumnViewerMoveUpAction) action).setOwner(model);
		((ColumnViewerMoveUpAction) action).setReference(reference);
		action.setText("����");
		btnGroupManager.add(action);
		
		action = new ColumnViewerMoveDownAction(viewer, editingDomain);
		((ColumnViewerMoveDownAction) action).setOwner(model);
		((ColumnViewerMoveDownAction) action).setReference(reference);
		action.setText("����");
		btnGroupManager.add(action);
		
	}
	
	/**
	 * 
	 */
//	public void dispose() {
//		btnGroupManager.dispose();
//		btnGroupManager = null;
//	}
//	
//	public Composite getControl() {
//		if(null != btnGroupManager) {
//			return btnGroupManager.getControl();
//		}
//		return null;
//	}
}
