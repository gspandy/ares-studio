/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.commands.operations.IOperationApprover;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.operations.UndoRedoActionGroup;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.ui.IARESResourceEditorInput;
import com.hundsun.ares.studio.ui.control.IEditable;
import com.hundsun.ares.studio.ui.editor.outline.AresAnnotationOutline;
import com.hundsun.ares.studio.ui.editor.outline.AresExtendPointOutline;
import com.hundsun.ares.studio.ui.page.FromPageWithMyDirtySystem;
import com.hundsun.ares.studio.ui.util.EditorDirtyStatus;

/**
 * �����༭��;
 * 
 * 2010-07-20 sundl ���´�AbstractHSFormEditor���󣬽����������IAresResource�����⣻
 * [ͬʱ����ͬ���ײ��ļ���ʵ�֣�ʹ��IFileSync ��δʵ��]
 * 
 * @author maxh
 */
public abstract class BasicAresFormEditor<T> extends FormEditor implements
		PropertyChangeListener {

	private static final String ACTIVE_PAGE = "active_page";

	protected class EditorInputListener implements IEditorInputStateListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.hundsun.ares.studio.ui.editor.IEditorInputStateListener#
		 * editorInputDeleted(org.eclipse.ui.IEditorInput)
		 */
		public void editorInputDeleted(IEditorInput input) {
			if (input.equals(getEditorInput())) {
				Display display = getSite().getShell().getDisplay();
				display.asyncExec(new Runnable() {
					public void run() {
						getSite().getPage().closeEditor(
								BasicAresFormEditor.this, false);
					}
				});
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.hundsun.ares.studio.ui.editor.IEditorInputStateListener#
		 * editorInputMoved(org.eclipse.ui.IEditorInput,
		 * org.eclipse.ui.IEditorInput)
		 */
		public void editorInputMoved(final IEditorInput oldInput,
				final IEditorInput newInput) {
			if (oldInput != null && oldInput.equals(getEditorInput())) {
				Display display = getSite().getShell().getDisplay();
				display.asyncExec(new Runnable() {
					public void run() {
						handleEditorInputMoved(oldInput, newInput);
					}
				});
			}
		}

	}

	private IPartListener2 partListener = new IPartListener2() {

		@Override
		public void partActivated(IWorkbenchPartReference partRef) {
			IWorkbenchPart part = partRef.getPart(true);
			if (part == BasicAresFormEditor.this) {
				if (null != approver) {
					getOperationHistory().removeOperationApprover(approver);
				}
				approver = new AresBasicEditorApprover(undoContext);
				getOperationHistory().addOperationApprover(approver);
				if(undoContext!=null){
					getOperationHistory().setLimit(undoContext, undoLimit());
				}
				
			} else {
				if (null != approver) {
					getOperationHistory().removeOperationApprover(approver);
				}
			}
		}

		@Override
		public void partBroughtToTop(IWorkbenchPartReference partRef) {
		}

		@Override
		public void partClosed(IWorkbenchPartReference partRef) {
		}

		@Override
		public void partDeactivated(IWorkbenchPartReference partRef) {
			if (null != approver) {
				getOperationHistory().removeOperationApprover(approver);
			}
		}

		@Override
		public void partOpened(IWorkbenchPartReference partRef) {
		}

		@Override
		public void partHidden(IWorkbenchPartReference partRef) {
		}

		@Override
		public void partVisible(IWorkbenchPartReference partRef) {
		}

		@Override
		public void partInputChanged(IWorkbenchPartReference partRef) {
		}
	};

	protected boolean backUpPageIndex = true;
	protected EditorDirtyStatus dirty = new EditorDirtyStatus();
	protected T info; // info�������ฺ��Ϊ���������ֵ������ʹ�á�

	private boolean forceReadOnly = false; // ǿ��ֻ��������

	AresExtendPointOutline outline1;
	AresAnnotationOutline outline2;

	/** �༭���ײ���ļ� */
	protected IFile editingFile;

	private IARESElement aresElement;

	/** ��Ŀ */
	protected IARESProject aresProject;

	private boolean useUndoSupport = true;

	/**
	 * ��������������
	 */
	protected IUndoContext undoContext;
	/**
	 * ��������ƥ����
	 */
	IOperationApprover approver;

	private EditorInputListener editorInputListener = new EditorInputListener();

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);

		// ��ʼ����������������
		if (useUndoSupport)
			installUndoSupport();

		// ������״̬����
		dirty.addPropertyChangeListener(this);

		EditorInputInfoManager.getInstance().addListener(editorInputListener);
	}

	protected void installUndoSupport() {
		initializeOperationHistory();
		UndoRedoActionGroup undoRedoGroup = new UndoRedoActionGroup(getSite(),
				undoContext, true);
		IActionBars actionBars = getEditorSite().getActionBars();
		undoRedoGroup.fillActionBars(actionBars);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormEditor#addPage(org.eclipse.ui.IEditorPart
	 * , org.eclipse.ui.IEditorInput)
	 */
	@Override
	public int addPage(IEditorPart editor, IEditorInput input)
			throws PartInitException {
		if (editor instanceof FromPageWithMyDirtySystem) {
			FromPageWithMyDirtySystem fromPageWithMyDirtySystem = (FromPageWithMyDirtySystem) editor;
			addPageContext(fromPageWithMyDirtySystem);
			return super.addPage(editor, input);
		} else {
			return super.addPage(editor, input);
		}
	}

	@Override
	protected void setInput(IEditorInput input) {
		IEditorInput oldInput = getEditorInput();
		if (oldInput != null) {
			EditorInputInfoManager.getInstance().disconnect(oldInput);
		}

		super.setInput(input);

		updateInfo(oldInput, input);
		if (input instanceof IFileEditorInput) {
			EditorInputInfoManager.getInstance().connect(input);
		}

		// ���ñ༭������
		updatePartName();

		if (input != null && oldInput != null) {
			updateControls();
		}
	}

	/**
	 * setInput��ʱ����ô˷������±༭�����ֵ�һЩ��Ϣ�������ļ���handle,project�ȵ�;
	 * ����������õ�ʱ��editor��input�Ѿ�������Ϊ�µ�Input
	 */
	protected void updateInfo(IEditorInput oldInput, IEditorInput newInput) {
		IEditorInput input = getEditorInput();
		if (input instanceof IFileEditorInput) {
			this.editingFile = ((IFileEditorInput) input).getFile();
			aresElement = (IARESElement) ARESCore.create(editingFile);
			IProject project = editingFile.getProject();
			this.aresProject = ARESCore.create(project);
		} else if (input instanceof IARESResourceEditorInput) {
			IARESResourceEditorInput aresInput = (IARESResourceEditorInput) input;
			aresElement = aresInput.getARESResource();
			this.aresProject = aresElement.getARESProject();
		}
	}

	/**
	 * �༭�����ļ������Ժ�ˢ�½��棬���಻����ȫ��֤createPartControl()�Ѿ�ִ�й������� ������Ҫ��ִ��֮ǰ�����ж�
	 */
	protected void updateControls() {
	}

	protected void handleEditorInputMoved(IEditorInput oldInput,
			final IEditorInput newInput) {
		setInput(newInput);
	}

	public void updatePartName() {
		String partName = getPartTitleName();
		if (isReadOnly()) {
			partName = partName + "��ֻ����";
		}
		setPartName(partName);
		setTitle(partName);
		// firePropertyChange(IWorkbenchPartConstants.PROP_PART_NAME);
	}

	protected String getPartTitleName() {
		if (editingFile != null)
			return editingFile.getName();
		return "";
	}

	protected boolean isUseUndoSupport() {
		return useUndoSupport;
	}

	protected void setUseUndoSupport(boolean useUndoSupport) {
		this.useUndoSupport = useUndoSupport;
	}

	/**
	 * ��ʼ��������������
	 */
	protected void initializeOperationHistory() {
		undoContext = new ObjectUndoContext(this);
		if (approver != null) {
			getOperationHistory().removeOperationApprover(approver);
		}
		approver = new AresBasicEditorApprover(undoContext);
		getOperationHistory().addOperationApprover(approver);
		getOperationHistory().setLimit(undoContext, undoLimit());
		UndoRedoActionGroup undoRedoGroup = new UndoRedoActionGroup(getSite(),
				undoContext, true);
		IActionBars actionBars = getEditorSite().getActionBars();
		undoRedoGroup.fillActionBars(actionBars);
	}

	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		updateEditableState();
	}

	/**
	 * ����ҳ����Ҫ�������� ������״̬ ������Դ ���ݳ������������� ����ģ�� ע�ⲻҪ�ظ����ݱ���
	 * 
	 * @param page
	 */
	protected void addPageContext(FromPageWithMyDirtySystem page) {
		page.setDirtyStatus(dirty);

		if (useUndoSupport)
			page.setUndoContext(undoContext);

		page.setInfo(info);
	}

	/**
	 * ���±༭����ֻ��״̬���˷��������ڿؼ��������֮����á�
	 * 
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
		if (forceReadOnly)
			return true;

		if (editingFile != null)
			return this.editingFile.isReadOnly();

		return false;
	}

	public boolean isForceReadOnly() {
		return forceReadOnly;
	}

	public void setForceReadOnly(boolean forceReadOnly) {
		this.forceReadOnly = forceReadOnly;
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
		return PlatformUI.getWorkbench().getOperationSupport()
				.getOperationHistory();
	}

	/**
	 * ��ȡ�༭���ڴ�ģ�͵���
	 */
	protected abstract Class getModelType();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormEditor#addPage(org.eclipse.ui.forms.editor
	 * .IFormPage)
	 */
	@Override
	public int addPage(IFormPage page) throws PartInitException {
		if (page instanceof FromPageWithMyDirtySystem) {
			FromPageWithMyDirtySystem fromPageWithMyDirtySystem = (FromPageWithMyDirtySystem) page;
			addPageContext(fromPageWithMyDirtySystem);
			return super.addPage(fromPageWithMyDirtySystem);
		} else {
			return super.addPage(page);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPage(int,
	 * org.eclipse.ui.forms.editor.IFormPage)
	 */
	@Override
	public void addPage(int index, IFormPage page) throws PartInitException {

	}

	@Override
	protected void createPages() {
		super.createPages();
		IDialogSettings mySettings = ARESEditorPlugin.getDefault().getDialogSettings()
				.getSection(getSite().getId());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormEditor#createPageContainer(org.eclipse
	 * .swt.widgets.Composite) Ҫ�Ѱ������������ý�ȥ
	 */
	@Override
	protected Composite createPageContainer(Composite parent) {
		Composite container = super.createPageContainer(parent);
		if (getHelpContextId() != null) {
			PlatformUI.getWorkbench().getHelpSystem()
					.setHelp(container, getHelpContextId());
		}
		return container;
	}

	/**
	 * ���������ĵ�ID ʵ������д�÷������ð���������ID
	 * 
	 * @return
	 */
	protected String getHelpContextId() {
		return null;
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(EditorDirtyStatus.PROPERTY_VALUE)) {
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	private void closeEditor(IResource resource) {
		try {
			Display display = getSite().getShell().getDisplay();
			display.asyncExec(new Runnable() {
				public void run() {
					getSite().getPage().closeEditor(BasicAresFormEditor.this,
							false);
				}
			});
		} catch (Exception e) {
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
			if (useDefaultOutline()) {
				outline2 = new AresAnnotationOutline();
				outline2.setInput(info);
				dirty.addPropertyChangeListener2(outline2);
				return outline2;
			}
		}
		return super.getAdapter(adapter);
	}

	public T getInfo() {
		return info;
	}

	/**
	 * �Ƿ�ʹ��Ĭ�ϵĴ����ͼ
	 */
	boolean useDefaultOutline() {
		return true;
	}

	public IARESProject getARESProject() {
		return aresProject;
	}

	public IARESElement getARESElement() {
		if (editingFile != null) {
			return ARESCore.create(editingFile);
		}
		return aresElement;
	}

	protected void setSite(IWorkbenchPartSite site) {
		super.setSite(site);
		getSite().getWorkbenchWindow().getPartService()
				.addPartListener(partListener);
	}

	@Override
	public void dispose() {
		EditorInputInfoManager.getInstance().disconnect(getEditorInput());
		EditorInputInfoManager.getInstance().removeListener(editorInputListener);
		IDialogSettings settings = ARESEditorPlugin.getDefault().getDialogSettings();
		IDialogSettings mySetting = settings.addNewSection(this.getSite().getId());
		mySetting.put(ACTIVE_PAGE, getCurrentPage());

		if (useUndoSupport)
			if (approver != null) {
				getOperationHistory().removeOperationApprover(approver);
			}
		
		approver = null;
		getSite().getWorkbenchWindow().getPartService()
				.removePartListener(partListener);
		if (pages != null) {
			super.dispose();
		}
	}

}
