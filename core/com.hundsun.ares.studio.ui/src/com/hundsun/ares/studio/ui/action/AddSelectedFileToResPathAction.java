/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IReferencedLibrary;
import com.hundsun.ares.studio.core.IResPathEntry;
import com.hundsun.ares.studio.internal.core.ARESModelManager;
import com.hundsun.ares.studio.internal.core.ReferencedLibrayUtil;

/**
 * ��ѡ����ļ���ӵ�ResPath
 * @author sundl
 */
public class AddSelectedFileToResPathAction extends ResPathModifyAction{

	protected AddSelectedFileToResPathAction() {
		super("���Ϊ���ð�");
	}
	
	@Override
	public void run() {
		List<IFile> files = getSelectedFiles();
		
		// ȡ����Ŀ
		IFile testfile = files.get(0);
		IProject project = testfile.getProject();
		IARESProject aresProject = ARESCore.create(project);

		List<IResPathEntry> respath = new ArrayList<IResPathEntry>();
		for (IFile file : files) {
			IResPathEntry path = ARESCore.newLibEntry(file.getFullPath());
			respath.add(path);
		}
		IResPathEntry[] oldpath = aresProject.getRawResPath();
		IResPathEntry[] added = respath.toArray(new IResPathEntry[0]);
		IResPathEntry[] newPath = new IResPathEntry[oldpath.length + added.length];
		System.arraycopy(oldpath, 0, newPath, 0, oldpath.length);
		System.arraycopy(added, 0, newPath, oldpath.length, added.length);
		aresProject.setRawResPath(newPath, null);
		ARESModelManager.getManager().getDeltaProcessor().entriesAdded(aresProject, added);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.action.ResPathModifyAction#canHandle(org.eclipse.core.resources.IFile[])
	 */
	@Override
	protected boolean canHandle(List<IFile> files) {
		for (IFile file : files) {
			// �Ƿ�Ϸ������ð�			
			IStatus status = ReferencedLibrayUtil.testFile(file);
			if (!status.isOK()) {
				return false;
			}
			// �Ƿ��Ѿ���respath����
			IProject project = file.getProject();
			IARESProject aresProject = ARESCore.create(project);
			if (aresProject.exists()) {
				for (IResPathEntry entry : aresProject.getRawResPath()) {
					if (entry.getEntryKind() == IResPathEntry.RPE_LIBRAY) {
						if (entry.getPath().equals(file.getFullPath())) {
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}

	// 2012-04-11 sundl ����һ���жϣ����ð������Ҽ���������ӡ�
	@Override
	protected boolean updateSelection(IStructuredSelection selection) {
		if (selection.isEmpty())
			return false;
		
		for (Object obj : selection.toArray()) {
			if (obj instanceof IReferencedLibrary) {
				return false;
			}
		}
		return super.updateSelection(selection);
	}

}
