/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.reference.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;

/**
 * @author gongyf
 *
 */
public class ARESElementDeltaVisitor implements IResourceDeltaVisitor {

	List<IARESElement> addedElements = new ArrayList<IARESElement>();
	List<IARESElement> removedElements = new ArrayList<IARESElement>();
	List<IARESElement> changedElements = new ArrayList<IARESElement>();
	List<IARESElement> addedOrChangedElements = new ArrayList<IARESElement>();
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
	 */
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {

		IResource resource = delta.getResource();
		int type = resource.getType();
		switch (type) {
		case IResource.PROJECT: {
			IARESProject aresProject = ARESCore.create((IProject)resource);
			if (delta.getKind() == IResourceDelta.REMOVED
					|| (delta.getKind() == IResourceDelta.CHANGED
							&& ((delta.getFlags() & IResourceDelta.OPEN) != 0) && ((IProject) resource)
							.isOpen())) {
				removedElements.add(aresProject);
			} else if (delta.getKind() == IResourceDelta.ADDED) {
				addedElements.add(aresProject);
				addedOrChangedElements.add(aresProject);
			} else {
				// �Ƿ����ù�ϵ�仯
				
			}
		}
		case IResource.FOLDER: {
			// ֻ����ģ����µ��ļ�
			IARESElement element = ARESCore.create(resource);
			if (element == null) {
				return false;
			}

			return true;
		}
		case IResource.FILE: {
			IARESElement element = ARESCore.create(resource);
			if (element instanceof IARESResource) {
				switch (delta.getKind()) {
				case IResourceDelta.ADDED:
					// ��Ӹ���Դ�������Ŀ�������Ϣ
					addedElements.add(element);
					addedOrChangedElements.add(element);
					break;
				case IResourceDelta.REMOVED:
					// ɾ������Դ���ṩ�Ŀ�������Ϣ
					removedElements.add(element);
					break;
				case IResourceDelta.CHANGED:
					if ((delta.getFlags() & IResourceDelta.CONTENT) != 0) {
						// ���¸���Դ������������Ϣ��������ɾ�������ӵķ�ʽ�����߲��öԱȺ�ֻ���±仯�Ĳ���
						changedElements.add(element);
						addedOrChangedElements.add(element);
					}
				default:
					break;
				}
			}
			return true;
		}
		}
		
		return true;
	}

	/**
	 * @return the addedElements
	 */
	public List<IARESElement> getAddedElements() {
		return addedElements;
	}
	
	/**
	 * @return the changedElements
	 */
	public List<IARESElement> getChangedElements() {
		return changedElements;
	}
	
	/**
	 * @return the removedElements
	 */
	public List<IARESElement> getRemovedElements() {
		return removedElements;
	}
	
	/**
	 * @return the addedOrChangedElements
	 */
	public List<IARESElement> getAddedOrChangedElements() {
		return addedOrChangedElements;
	}
}
