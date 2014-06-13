/**
 * Դ�������ƣ�JRESDefaultSyncnizeUnit.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.sync;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import com.hundsun.ares.studio.ui.IARESResourceEditorInput;

/**
 * @author lvgao
 *
 */
public  class JRESDefaultSyncnizeUnit implements IFileSyncnizeUnit{
	
	private IEditorPart editor;
	private long modifiedStamp = -1;
	
	public JRESDefaultSyncnizeUnit(IEditorPart editor){
		this.editor = editor;
	}



	private void closeEditor() {
		//�رձ༭��
		editor.getSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				editor.getSite().getWorkbenchWindow().getActivePage().closeEditor(editor, false);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.util.IFileSyncnizeUnit#beforeSave()
	 */
	@Override
	public void beforeSave() {
		if (getFile() == null) {
			// û�ж�Ӧ����Դ�ļ��򲻴�����ǰ�¼�
			return;
		}
		modifiedStamp = getFile().getModificationStamp() + 1;	
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.util.IFileSyncnizeUnit#isSaveChange(org.eclipse.core.resources.IResourceDelta)
	 */
	private boolean isSaveChange(IResourceDelta delta) {
		switch (delta.getKind()) {
		case IResourceDelta.CHANGED:
			break;
		case IResourceDelta.REMOVED:
			//���Ϊɾ��ֱ�ӷ���
			return false;
		}
		IFile resource = (IFile) delta.getResource();
		if (resource.getModificationStamp() == modifiedStamp) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.util.IFileSyncnizeUnit#getFile()
	 */
	public IFile getFile() {
		IEditorInput editorInput = editor.getEditorInput();
		if (editorInput instanceof IFileEditorInput) {
			return ((IFileEditorInput)editorInput).getFile();
		} else if (editorInput instanceof IARESResourceEditorInput) {
			return (IFile) ((IARESResourceEditorInput)editorInput).getARESResource().getLib().getResource();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.util.IFileSyncnizeUnit#handleAction(org.eclipse.core.resources.IResourceDelta)
	 */
	@Override
	public void handleAction(IResourceDelta delta) {
		try {
			delta.accept(new IResourceDeltaVisitor() {
				
				@Override
				public boolean visit(IResourceDelta delta) throws CoreException {
					IResource resource = delta.getResource();
					IFile file = getFile();
					if (resource instanceof IWorkspaceRoot) {
						return true;
					} else if (resource instanceof IProject) {
						if (resource.equals(file.getProject())) {
							// ��Ŀ��ɾ�����߹ر�ʱ�رձ༭��
							switch (delta.getKind()) {
							case IResourceDelta.REMOVED:
								closeEditor();
								return false;
							case IResourceDelta.CHANGED:
								// �رյ�ʱ��ҲҪ�ر�
								if ((delta.getFlags() & IResourceDelta.OPEN) != 0) {
									if ( !((IProject) resource).isOpen() ) {
										closeEditor();
										return false;
									}
								}
							}
							return true;
						} else {
							return false;
						}

					} else if (resource instanceof IContainer) {
						if (resource.getFullPath().isPrefixOf(file.getFullPath())) {
							if (delta.getKind() == IResourceDelta.REMOVED ) {
								closeEditor();
								return false;
							}
							return true;
						} else {
							return false;
						}
						
					} else if (resource instanceof IFile) {
						if (!ObjectUtils.equals(file, resource)) {
							return false;
						}
						switch (delta.getKind()) {
						case IResourceDelta.REMOVED:
							closeEditor();
							return false;
						case IResourceDelta.CHANGED:
							if ((delta.getFlags() & IResourceDelta.CONTENT) != 0 && resource.getModificationStamp() != modifiedStamp) {
								closeEditor();
								return false;
							}
						}
					} else {
						return false;
					}
					
					return true;
				}
			});
		} catch (CoreException e) {
		}
		
		
//		IFile file = getFile();
//		if(file != null) {
//			Map<Object, Object> context = new HashMap<Object, Object>();
//			IResourceDelta tmpdelta = delta.findMember(file.getFullPath());
//			if(tmpdelta != null ){
//				
//				if(isSaveChange(tmpdelta)){
//					//���������
//					handleSaveChange(context);
//				}else{
//					//����Ǳ������
//					handleOutAction(context);
//				}
//			} else {
//				IResourceDelta[] children = delta.getAffectedChildren(IResourceDelta.REMOVED | IResourceDelta.CHANGED);
//				
//				for (IResourceDelta child : children) {
//					if (child.getResource() instanceof IContainer) {
//						IContainer container = (IContainer) child.getResource();
//						if (container.getFullPath().isPrefixOf(file.getFullPath())) {
//							if ((child.getFlags() & IResourceDelta.OPEN) != 0 && container instanceof IProject) {
//								// ��Ŀ�Ĵ�״̬�仯��
//								if ( !((IProject)container).isOpen() ) {
//									handleOutAction(context);
//								}
//							} else if (child.getKind() == IResourceDelta.REMOVED) {
//								handleOutAction(context);
//							}
//						}
//					}
//				}
//			}
//		}

		
	}

}
