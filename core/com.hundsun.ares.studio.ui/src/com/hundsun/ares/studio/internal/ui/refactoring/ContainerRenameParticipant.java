package com.hundsun.ares.studio.internal.ui.refactoring;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IResPathEntry;
import com.hundsun.ares.studio.internal.core.ARESModelManager;
import com.hundsun.ares.studio.internal.core.ARESProject;
import com.hundsun.ares.studio.internal.ui.refactoring.changes.UpdateRespathOnContainerRenameChange;

/**
 * ��������Ŀ���ļ���ʱ���еĴ���
 * 		1. ���¶Ը���Ŀ������
 * 		2. ���¶������ַ�ڸ���Ŀ���ļ����µ�������Դ��������
 * @author sundl
 */
public class ContainerRenameParticipant extends RenameParticipant {

	private IProject project;
	private IContainer container;
	private IPath newPath;
	
	@Override
	protected boolean initialize(Object element) {
		if (element instanceof IContainer) {
			container = (IContainer) element;
			project = container.getProject();
			// ֻ����ARESProject
			if (ARESProject.hasARESNature(project)) {
				String newName = getArguments().getNewName();
				newPath = container.getFullPath().removeLastSegments(1).append(newName);
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "����.respath�ļ�";
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,	OperationCanceledException {
		CompositeChange changes = new CompositeChange("����.respath�ļ�");
		for (IARESProject aresProj : ARESModelManager.getManager().getModel().getARESProjects()) {
			if (hasReference(aresProj, this.container))
				changes.add(new UpdateRespathOnContainerRenameChange(aresProj, container, newPath));
		}
		return changes;
	}
	
	private boolean hasReference(IARESProject proj, IContainer container) {
		for (IResPathEntry entry : proj.getRawResPath()) {
			if (container.getFullPath().isPrefixOf(entry.getPath()))
				return true;
		}
		return false;
	}

}
