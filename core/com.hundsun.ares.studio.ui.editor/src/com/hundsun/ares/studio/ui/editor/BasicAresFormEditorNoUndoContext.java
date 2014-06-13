/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.commands.operations.IOperationApprover;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.ui.control.IEditable;
import com.hundsun.ares.studio.ui.editor.outline.AresAnnotationOutline;
import com.hundsun.ares.studio.ui.editor.outline.AresExtendPointOutline;
import com.hundsun.ares.studio.ui.page.FromPageWithMyDirtySystem;
import com.hundsun.ares.studio.ui.util.EditorDirtyStatus;

/**
 * �����༭��;
 * 
 * 2010-07-20 sundl
 * ���´�AbstractHSFormEditor���󣬽����������IAresResource�����⣻
 * [ͬʱ����ͬ���ײ��ļ���ʵ�֣�ʹ��IFileSync  ��δʵ��]
 * 
 * 2010-8-30 9:27:57  ����
 * ����ȷ��undocontext�����������༭������ʹ��undo redo�����
 *����ȥ��
 * 
 * @author lvgao
 */
public abstract class BasicAresFormEditorNoUndoContext<T> extends FormEditor implements IResourceChangeListener, IResourceDeltaVisitor, PropertyChangeListener {
	
	private static final String ACTIVE_PAGE = "active_page";
	protected boolean backUpPageIndex = true;
	protected EditorDirtyStatus dirty = new EditorDirtyStatus();
	protected T info;			// info�������ฺ��Ϊ���������ֵ������ʹ�á�
	protected FileSynchronizer synchronizer = new FileSynchronizer(this);
	
	AresExtendPointOutline outline1;
	AresAnnotationOutline outline2;
	
	/** �༭���ײ���ļ� */
	protected IFile editingFile;
	
	/** ��Ŀ */
	protected IARESProject aresProject;

	/**
	 * ��������ƥ����
	 */
	IOperationApprover approver;
	
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		//������״̬����
		dirty.addPropertyChangeListener(this);

		//���ñ༭������
		updatePartName();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPage(org.eclipse.ui.IEditorPart, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public int addPage(IEditorPart editor, IEditorInput input)
			throws PartInitException {
		if(editor instanceof FromPageWithMyDirtySystem){
			FromPageWithMyDirtySystem fromPageWithMyDirtySystem = (FromPageWithMyDirtySystem) editor;
			addPageContext(fromPageWithMyDirtySystem);
			return super.addPage(editor, input);
		}else{
			return super.addPage(editor, input);
		}
	}
	
	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		if (input instanceof IFileEditorInput) {
			this.editingFile = ((IFileEditorInput)input).getFile();
			IProject project = editingFile.getProject();
			this.aresProject = ARESCore.create(project);
			synchronizeWithFile();
		}
	}

	protected void updatePartName() {
		String partName = getPartTitleName();
		if (isReadOnly()) {
			partName = partName + "��ֻ����";
		}
		setPartName(partName);
	}
	
	protected String getPartTitleName(){
		if (editingFile != null)
			return editingFile.getName();
		return "";
	}
	
	private void synchronizeWithFile() {
		synchronizer.install();
//		try {
//			ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
//		} catch (Exception e) {
//			// logger.error("��ȡ��Դ����", e);
//		}
	}
	

	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		updateEditableState();
	}
	
	/**
	 * ����ҳ����Ҫ��������
	 * ������״̬
	 * ������Դ
	 * ���ݳ�������������
	 * ����ģ��
	 * ע�ⲻҪ�ظ����ݱ���
	 * @param page
	 */
	protected void addPageContext(FromPageWithMyDirtySystem page){
		page.setDirtyStatus(dirty);
		page.setInfo(info);
	}
	
	/**
	 * ���±༭����ֻ��״̬���˷��������ڿؼ��������֮����á�
	 * @param element
	 */
	 public void updateEditableState() {
		 boolean readonly = isReadOnly();
		if (getActivePageInstance() instanceof IEditable) {
			((IEditable) getActivePageInstance()).setEditable(!readonly);
		} else if (getActiveEditor() instanceof IEditable) {
			((IEditable) getActiveEditor()).setEditable(!readonly);
		}
		updatePartName();
	}

	protected boolean isReadOnly() {
		return this.editingFile.isReadOnly();
	}
	 
	/**
	 * ��ó��������������
	 */
	protected int undoLimit() {
		return 100;
	}
	
	/**
	 * ��ó���������ʷ
	 */
	public static IOperationHistory getOperationHistory() {
		return PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
	}
	
	/**
	 * ��ȡ�༭���ڴ�ģ�͵���
	 */
	protected abstract Class getModelType();

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPage(org.eclipse.ui.forms.editor.IFormPage)
	 */
	@Override
	public int addPage(IFormPage page) throws PartInitException {
		if(page instanceof FromPageWithMyDirtySystem){
			FromPageWithMyDirtySystem fromPageWithMyDirtySystem = (FromPageWithMyDirtySystem) page;
			addPageContext(fromPageWithMyDirtySystem);
			return super.addPage(fromPageWithMyDirtySystem);
		}else{
			return super.addPage(page);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPage(int, org.eclipse.ui.forms.editor.IFormPage)
	 */
	@Override
	public void addPage(int index, IFormPage page) throws PartInitException {

	}
	
	@Override
	protected void createPages() {
		super.createPages();
		IDialogSettings mySettings = ARESEditorPlugin.getDefault().getDialogSettings().getSection(getSite().getId());
		if (mySettings == null) {
			return;
		}
		if (mySettings.get(ACTIVE_PAGE) != null) {
			int index = mySettings.getInt(ACTIVE_PAGE);
			if (index > 0 && backUpPageIndex)
				try {
					setActivePage(index);
				} catch (Exception e) {
					setActivePage(0);
				}
		}
	}

	/**
	 * �ƶ�����һҳ������Ѿ������һҳ�����ƶ�����һҳ
	 */
	public void nextPage() {
		int next = getCurrentPage() + 1;
		if (next >= getPageCount()) {
			next = 0;
		}
		setActivePage(next);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#createPageContainer(org.eclipse.swt.widgets.Composite)
	 * Ҫ�Ѱ������������ý�ȥ
	 */
	@Override
	protected Composite createPageContainer(Composite parent) {
		Composite container = super.createPageContainer(parent);
		if(getHelpContextId() != null){
			PlatformUI.getWorkbench().getHelpSystem().setHelp(container, getHelpContextId());
		}
		return container;
	}
	
	/**
	 * ���������ĵ�ID ʵ������д�÷������ð���������ID
	 * @return
	 */
	protected String getHelpContextId(){
		return null;
	}	

	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(EditorDirtyStatus.PROPERTY_VALUE)) {
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}
	 
	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 * ��Ŀ�رպ���Դɾ��Ҫ�رձ༭��
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i < pages.length; i++) {
						if(getEditorInput() instanceof FileEditorInput){
							if (((FileEditorInput)getEditorInput()).getFile().getProject().equals(event.getResource())) {
								IEditorPart editorPart = pages[i].findEditor(getEditorInput());
								pages[i].closeEditor(editorPart, true);
							}
						}
					}
				}
			});
		}
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			try {
				event.getDelta().accept(this);
			} catch (CoreException e) {
			}
		}
	}

	public boolean visit(IResourceDelta delta) throws CoreException {
		if (!(delta.getKind() == IResourceDelta.REMOVED)) {
			return true;
		}
		IResource resource = delta.getResource();
		closeEditor(resource);
		return false;
	}

	private void closeEditor(IResource resource) {
		try{
			Display display = getSite().getShell().getDisplay();
			display.asyncExec(new Runnable() {
				public void run() {
					getSite().getPage().closeEditor(BasicAresFormEditorNoUndoContext.this, false);
				}
			});
		}catch (Exception e) {
		}
	}
	
	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	public void setFocus() {
		super.setFocus();
		// Ϊ����л����༭��������Զ���ý�������⡣
		IFormPage page = getActivePageInstance();
		if (page != null)
			page.setFocus();
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IContentOutlinePage.class) {
			if(useDefaultOutline()){
				outline2 = new AresAnnotationOutline();
				outline2.setInput(info);
				dirty.addPropertyChangeListener2(outline2);
				return outline2;
			}
		}
		return super.getAdapter(adapter);
	}
	
	/**
	 * �Ƿ�ʹ��Ĭ�ϵĴ����ͼ
	 */
	boolean useDefaultOutline(){
		return true;
	}
	
	protected IARESProject getARESProject() {
		return aresProject;
	}
	
	protected IARESElement getARESElement(){
		if(editingFile != null){
			return ARESCore.create(editingFile);
		}
		return aresProject;
	}
	
	
	@Override
	public void dispose() {
		synchronizer.uninstall();
		IDialogSettings settings = ARESEditorPlugin.getDefault().getDialogSettings();
		IDialogSettings mySetting = settings.addNewSection(this.getSite().getId());
		mySetting.put(ACTIVE_PAGE, getCurrentPage());
//		getOperationHistory().removeOperationApprover(approver);
		super.dispose();
	}
}

