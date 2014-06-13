/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.editor;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.ui.IARESResourceEditorInput;
import com.hundsun.ares.studio.ui.page.FromPageWithMyDirtySystem;

/**
 * ares�����༭��
 * �ṩģ��ͨ�����л�
 * �ṩ��������������
 * �ṩֻ��״̬����
 * @author maxh
 *
 */
public abstract class AbstractHSFormEditorNoUndocontext<T> extends AbstractHSExtendPointFormEditorNoUndocontext<T> {

	private static Logger logger = Logger.getLogger(AbstractHSFormEditor.class.getName());
	
	protected IARESResource resource;

	/**
	 * ��ù�����resource
	 * @return the resource
	 */
	public IARESResource getResource() {
		return resource;
	}
	
	@Override
	protected void addPageContext(FromPageWithMyDirtySystem page){
		super.addPageContext(page);
		page.setResource(resource);
	}
	
	protected String getPartTitleName(){
		if (resource != null) {
			return resource.getName();
		}
		return super.getPartTitleName();
	}
	
	@Override
	protected boolean isReadOnly() {
		if (resource != null)
			return resource.isReadOnly();
		return super.isReadOnly();
	}

	@Override
	public void setInput(IEditorInput input) {
		super.setInput(input);
		
		if (editingFile != null) {
			try {
				IARESElement element = ARESCore.create(editingFile);
				if (element instanceof IARESResource) {
					resource = (IARESResource) element;
				}
			} catch (Exception e) {
				logger.error("read model from platform fail!", e);
			}
		} else if (input instanceof IARESResourceEditorInput) {
			resource = ((IARESResourceEditorInput)input).getARESResource();
			this.aresProject = resource.getARESProject();
		}
		
		if (resource != null) {
			try {
				info = (T) resource.getWorkingCopy(getModelType());
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
		}
		
		//����޷�����Դ��ܻ�ȡģ�� ��newһ��
		try {
			if(info == null && editingFile != null){
				info = (T)getModelType().newInstance();
			}
		} catch (Exception e) {
			logger.error("create new instance failed!", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		if (getEditorInput() instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput)getEditorInput()).getFile();
			IARESElement element = ARESCore.create(file);
			if (element instanceof IARESResource) {
				try {
					((IARESResource)element).save(info, true, monitor);
				} catch (ARESModelException e) {
					logger.error("�ļ��������", e);
				}
			}
		}
		dirty.setValue(false);
	}
	
	/**
	 * ���� 2010-8-17 17:08:59
	 * ֻ�Ǳ����ļ������ı��ļ�����״̬
	 * �ı��ļ�����״̬��������emf��һЩ����
	 * ��û���趨���µ���Դǰ�������쳣
	 * @param monitor
	 */
	protected void doSaveFile(IProgressMonitor monitor) {
		if (getEditorInput() instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput)getEditorInput()).getFile();
			IARESElement element = ARESCore.create(file);
			if (element instanceof IARESResource) {
				try {
					((IARESResource)element).save(info, true, monitor);
				} catch (ARESModelException e) {
					logger.error("�ļ��������", e);
				}
			}
		}
	}

}
