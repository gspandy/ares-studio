/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IResPathEntry;
import com.hundsun.ares.studio.internal.core.ReferencedLibrayUtil;
import com.hundsun.ares.studio.ui.ARESUI;

/**
 * ��ӹ����������ð��ļ���respath
 * @author sundl
 */
public class AddWorkspaceFileToResPathAction extends AddToResPathAction {

	/**
	 * @param shell
	 * @param text
	 */
	public AddWorkspaceFileToResPathAction(Shell shell) {
		super(shell, "��ӹ��������ڵ����ð�");
	}

	public void run() {
		final IARESProject aresProject = ARESCore.create(project);
		ElementTreeSelectionDialog selectionDialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(), new WorkbenchContentProvider());
		selectionDialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		selectionDialog.setDoubleClickSelects(true);
		
		selectionDialog.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof IFile) {
					if (isOnRespath((IFile) element, aresProject)) {
						return false;
					}
				}
				return true;
			}
		});
		
		selectionDialog.setValidator(new ISelectionStatusValidator() {
			@Override
			public IStatus validate(Object[] selection) {
				if (selection == null || selection.length == 0)
					return new Status(IStatus.ERROR, ARESUI.PLUGIN_ID, "��ѡ�����ð��ļ�");
				for (Object obj : selection) {
					if (!(obj instanceof IFile)) {
						return new Status(IStatus.ERROR, ARESUI.PLUGIN_ID, "ֻ��ѡ�����ð��ļ�");
					} else {
						IFile file = (IFile) obj;
						IStatus status = ReferencedLibrayUtil.testFile((IFile) obj);
						if (!status.isOK()) {
							return new Status(IStatus.ERROR, ARESUI.PLUGIN_ID, String.format("�ļ�%s������Ч�����ð���", file.getName()));
						}
					}
				}
				return Status.OK_STATUS;
			}
		});
		
		if (selectionDialog.open() == Dialog.OK) {
			Object[] result = selectionDialog.getResult();
			List<IResPathEntry> toAdd = new ArrayList<IResPathEntry>();
			for (Object obj : result) {
				IFile file = (IFile) obj;
				IResPathEntry entry = ARESCore.newLibEntry(file.getFullPath());
				toAdd.add(entry);
			}
			addResEntryToResPath(toAdd.toArray(new IResPathEntry[0]), aresProject);
		}
		
//		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//		FilteredResourcesSelectionDialog dialog = new FilteredResourcesSelectionDialog(shell, true, root, IResource.FILE);
//		dialog.open();
		//dialog.setTitle("ѡ��������Դ��");
		//dialog.set
		//ListSelectionDialog dialog = new ListSelectionDialog(shell, projects, new ArrayContentProvider(), new ARESElementLabelProvider(), "ѡ��������Ŀ");
		//dialog.setTitle("������Ŀ");
/*		if (dialog.open() == Dialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length > 0) {
				List<IResPathEntry> entries = new ArrayList<IResPathEntry>();
				for (Object obj : result) {
					IARESProject aresProject = (IARESProject) obj;
					IPath path = aresProject.getPath();
					entries.add(ARESCore.newProjectEntry(path));
				}
				addResEntryToResPath(entries.toArray(new IResPathEntry[0]), ARESCore.create(project));
			}
		}*/
	}
	
}
