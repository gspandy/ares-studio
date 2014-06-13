/**
 * Դ�������ƣ�ColumnViewerDeleteAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.actions;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class ColumnViewerDeleteAction extends ColumnViewerAction {

	public ColumnViewerDeleteAction(ColumnViewer viewer, EditingDomain editingDomain) {
		super(viewer, editingDomain);
		setText("ɾ��");
		
		setId(IActionIDConstant.CV_DELETE);
		
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
	}

	
	@Override
	protected Command createCommand() {
		List<Object> selectedObjects = getSelectedObjects();
		if (selectedObjects.isEmpty()) {
			return null;
		}
		
		return DeleteCommand.create(getEditingDomain(), selectedObjects);
	}
}
