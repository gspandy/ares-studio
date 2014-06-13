/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.refactoring;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.resource.ResourceChange;

import com.hundsun.ares.studio.core.IARESResource;

/**
 * 
 * @author sundl
 */
public abstract class AresResourceChange extends ResourceChange {

	protected IARESResource resource;
	
	public AresResourceChange(IARESResource resource) {
		this.resource = resource;
	}
	
	// ��ʱû��ʵ�֣������ʵ�ֶ����change���һ��������Change������������Ԥ����ʱ����ʾΪ�ӽڵ㣬�ܹ���ʾ��ϸ����θı�����
//	private AresResourceInfoChange[] changes;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.resource.ResourceChange#getModifiedResource()
	 */
	@Override
	protected IResource getModifiedResource() {
		return resource.getResource();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#getName()
	 */
	@Override
	public String getName() {
		return "����" + resource.getName() + "�е�����";
	}

	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		Object info = resource.getWorkingCopy(getInfoClassType());
		modifyInfo(info);
		resource.save(info, true, null);
		return createUndoChange();
	}
	
	protected abstract Class<?> getInfoClassType();
	
	protected abstract Change createUndoChange();
	
	protected abstract void modifyInfo(Object info);
}
