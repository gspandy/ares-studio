/**
 * Դ�������ƣ�EMFFormEditor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.TriggerListener;
import org.eclipse.emf.transaction.impl.EditingDomainManager;
import org.eclipse.emf.transaction.impl.TransactionalCommandStackImpl;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.ui.ARESElementLabelProvider;
import com.hundsun.ares.studio.ui.IARESResourceEditorInput;
import com.hundsun.ares.studio.ui.editor.editable.IEditableControl;
import com.hundsun.ares.studio.ui.editor.editable.JresDefaultEditableControler;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelUtils;
import com.hundsun.ares.studio.ui.editor.sync.IFileSyncnizeUnit;
import com.hundsun.ares.studio.ui.editor.sync.JRESDefaultSyncnizeUnit;
import com.hundsun.ares.studio.ui.editor.sync.JRESEditorSyncManager;
import com.hundsun.ares.studio.ui.extendpoint.manager.ExtendPageInfo;
import com.hundsun.ares.studio.ui.extendpoint.manager.ExtendPageManager;
import com.hundsun.ares.studio.ui.extendpoint.manager.IExtendedPage;
import com.hundsun.ares.studio.ui.page.ExtendPageWithMyDirtySystem;
import com.hundsun.ares.studio.ui.page.IExtendItemLoader;
import com.hundsun.ares.studio.ui.util.ARESUIUtil;

/**
 * <ul>
 * <li>����{@link EditingDomain}��Ϊ�༭�򲢶�ȡģ����Ϣ</li>
 * <li></li>
 * <li>��״̬������<code>CommandStack</code>����ɡ�</li>
 * <li>{@link #getAdapter}֧�ַ���{@link EditingDomain}��{@link Resource}</li>
 * </ul>
 * ע������
 * <ul>
 * <li>���༭����ע��ʱ��Ӧ����Դ��ע���ʱ��info-class������{@link EObject}��������</li>
 * </ul>
 * @author gongyf
 *
 */
public abstract class EMFFormEditor extends FormEditor implements IEditingDomainProvider {

	public static ILabelProvider TITLE_LABEL_PROVIDER = new ARESElementLabelProvider();
	
	protected static final String ACTIVE_EDITOR_INDEX = "active_editor_index";//�༭����������id
	private int editorIndex = 0;//��󼤻�ı༭��
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	//�ļ�ͬ����Ԫ
	protected IFileSyncnizeUnit fileSyncUnit;
	
	/**
	 * ʹ��ͳһ������
	 */
	static protected Collection<Object> myclipboard;
	
	private TransactionalEditingDomain editingDomain;
	
	private EObject info = null;
	
	private IEditableControl editableControl;
	
	// ��չҳ���б�
	protected List<IExtendedPage> extendsPages = new ArrayList<IExtendedPage>();
	
	public TransactionalEditingDomain getEditingDomain() {
		return editingDomain;
	}
	
	public EMFFormActionBarContributor getActionBarContributor() {
		return (EMFFormActionBarContributor) getEditorSite().getActionBarContributor();
	}
	
	/**
	 * @return the info
	 */
	public EObject getInfo() {
		return info;
	}
	

	
	/**
	 * ��ʼ��editingDomain
	 * �����������adapterFactory
	 * @param adapterFactory
	 */
	protected void configureComposedAdapterFactory(ComposedAdapterFactory adapterFactory) {
		// do nothing
	}
	
	protected void createEditDomain() {
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		configureComposedAdapterFactory(adapterFactory);
		
		// Create the editing domain with a special command stack.
		//
		
		TransactionalCommandStackImpl commandStack = new TransactionalCommandStackImpl(){
			@Override
			public void execute(Command command) {
	            if (command != null) {
	            	if (editingDomain.isReadOnly(getInfo().eResource())) {
						try{
							handleLock();
						}finally{
							command.dispose();
						}
		            }
	            }
	            super.execute(command);
		    }
		};
		
		editingDomain = new TransactionalEditingDomainImpl(adapterFactory,commandStack) {
			public boolean isReadOnly(Resource resource) {
				if (isInReferencedLibrary()) {
					// ���ð���Ϊֻ��
					return true;
				}
				
				if (resource == null || resource.getURI() == null) {
					return false;
				}

				return super.isReadOnly(resource);
			}

			@Override
			public Collection<Object> getClipboard() {
				return EMFFormEditor.myclipboard;
			}

			@Override
			public void setClipboard(Collection<Object> clipboard) {
				EMFFormEditor.myclipboard = clipboard;
			};
			
		};
		
		// ���һЩע��ļ�����
		EditingDomainManager.getInstance().configureListeners(getEditingDomainID(), editingDomain);
		
		
		// Add a listener to set the most recent command's affected objects to be the selection of the viewer with focus.
		//
		editingDomain.getCommandStack().addCommandStackListener
			(new CommandStackListener() {
				 public void commandStackChanged(final EventObject event) {
					 getSite().getShell().getDisplay().asyncExec
						 (new Runnable() {
							  public void run() {
								  firePropertyChange(IEditorPart.PROP_DIRTY);
							  }
						  });
				 }
			 });
		
		// ������չģ�͵ĳ�ʼ��֧�֣���һ���µ�ExtensibleModel���������Զ���ʼ��map����
		editingDomain.addResourceSetListener(new TriggerListener() {
			
			@Override
			protected Command trigger(TransactionalEditingDomain domain,
					Notification notification) {
				List<ExtensibleModel> modelList = new ArrayList<ExtensibleModel>();
				
				if (notification.getNewValue() instanceof ExtensibleModel) {
					modelList.add((ExtensibleModel) notification.getNewValue());
				} else if (notification.getNewValue() instanceof Collection<?>) {
					for (Object o : (Collection<?>)notification.getNewValue()) {
						if (o instanceof ExtensibleModel) {
							modelList.add((ExtensibleModel) o);
						}
					}
				}
				
				if (modelList.isEmpty()) {
					return null;
				}
				return new ExtensibleModelTriggerCommand(domain, getARESResource(), 
						modelList);
			}
		});

	}
	
	/**
	 * ��������
	 */
	private void handleLock (){
		if (this.getARESResource().getResource() == null) {
			return;
		}
		IPath path = this.getARESResource().getResource().getProjectRelativePath();
		IFile file = getARESResource().getARESProject().getProject().getFile(path);
		if (file.exists()) {
			boolean readonly = file.isReadOnly();
			
			file.getWorkspace().validateEdit(new IFile[]{file}, null);
			
			if (file.isReadOnly() != readonly) {
				Display display = getSite().getShell().getDisplay();
				display.asyncExec(new Runnable() {
					public void run() {
						getSite().getPage().closeEditor(EMFFormEditor.this, false);
						try {
							ARESUIUtil.openEditor(getARESResource());
						} catch (PartInitException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}
	
	/**
	 * ��ȡ�༭���ID������null��ʾ����Ҫid
	 * @return
	 */
	protected String getEditingDomainID() {
		return getEditorSite().getId();
	}
	
	/**
	 * �жϵ�ǰ�Ƿ���������Դ���д�
	 * @return
	 */
	public boolean isInReferencedLibrary() {
		return getARESResource().getLib() != null;
	}
	
	
	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		//����ļ�ͬ��
		fileSyncUnit = new JRESDefaultSyncnizeUnit(this);
		JRESEditorSyncManager.getInstance().addSyncUnit(fileSyncUnit);
	}

	public IARESResource getARESResource() {
		IARESResource aresResource = null;
		if (getEditorInput() instanceof IFileEditorInput) {
			aresResource = (IARESResource) ARESCore.create(((IFileEditorInput) getEditorInput()).getFile());
		} else if (getEditorInput() instanceof IARESResourceEditorInput) {
			aresResource = ((IARESResourceEditorInput)getEditorInput()).getARESResource();
		} // TODO: Զ����Դ����
		return aresResource;
	}
	
	
	
	/**
	 * ��ȡ�༭�����EMF��
	 * @return
	 */
	protected abstract EClass getInfoClass();
	
	/**
	 * �����༭��Ͷ�ȡģ����Ϣ
	 * @throws ARESModelException 
	 */
	protected void createModel() throws ARESModelException {

		//try {
			// �Ȼ�ȡARES��Դ����
			IARESResource aresResource = getARESResource();
			
			Assert.isTrue(aresResource != null, "�����޷�ת��ΪIARESResource����");
			
			if(getInfoClass() == null) {
				info = (EObject) aresResource.getWorkingCopy(getInfoClassInstance());
			}else {
				info = (EObject) aresResource.getWorkingCopy(getInfoClass().getInstanceClass());
			}
			
			// ----------------------------------------
			// ���Դ��� begin
			// ----------------------------------------
			if (info == null) {
				logger.error("û�ж�ȡ��ģ�����ݣ����Զ�����һ��ȫ��ģ�͡���Դ��" + aresResource.getElementName());
				Resource r = new XMLResourceImpl();
				info = getInfoClass().getEPackage().getEFactoryInstance().create(getInfoClass());
				r.getContents().add(info);
			}
			// ----------------------------------------
			// ���Դ��� end
			// ----------------------------------------
			ExtensibleModelUtils.extendResource(getARESResource(), info.eResource(), false);
			editingDomain.getResourceSet().getResources().clear();
			try {
				//���ԭresourceset��ע���package
				editingDomain.getResourceSet().getPackageRegistry().putAll(info.eResource().getResourceSet().getPackageRegistry());
			} catch (Exception e) {
			}
			((BasicCommandStack)editingDomain.getCommandStack()).flush();
			editingDomain.getResourceSet().getResources().add(info.eResource());
//		} catch (Exception e) {
//			//MessageDialog.openError(getSite().getShell(), "����ģ�ʹ���", e.getMessage());
//			logger.error(e.getMessage(), e);
//			throw e;
//		}
	}
	
	/**
	 * ֱ�ӻ�ȡClass���� ���û�Ԫ����
	 * ��ʱ����getInfoClass()����null
	 * @return
	 */
	protected Class<?> getInfoClassInstance() {
		return EObject.class;
	}

	
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == EditingDomain.class) {
			return getEditingDomain();
		} else if (adapter.isAssignableFrom(getInfo().getClass())) {
			return getInfo();
		}
		return super.getAdapter(adapter);
	}
	
	@Override
	public boolean isDirty() {
		return ((BasicCommandStack)editingDomain.getCommandStack()).isSaveNeeded();
	}
	
	/**
	 * ָʾ��ǰ�༭���Ƿ�ΪӦ��Ϊֻ��״̬�����������Ӱ������ϵĿؼ��ɱ༭״̬<BR>
	 * ���ñ�����{@link #createModel()}�󱻵���
	 * @return
	 */
	public boolean isReadOnly() {
		 ResourceAttributes attributes = getARESResource().getResource().getResourceAttributes();
		 boolean resourceIsReadOnly = false;//�ļ�ϵͳ�и�ϵͳ�Ƿ�Ϊֻ��,Ĭ�ϲ�Ϊ
		 if(attributes!=null){
			 resourceIsReadOnly = attributes.isReadOnly();
		 }
		return editingDomain.isReadOnly(info.eResource()) || resourceIsReadOnly;
	}
	
	protected void createEditableControl() {
		//��ʼ��ֻ��������
		editableControl = new JresDefaultEditableControler(this);
		editableControl.refreshResourceReadonlyStatus();
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		
		try {
			createEditDomain();
			createModel();
			createEditableControl();
		} catch (Exception e) {
			throw new PartInitException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void createPages() {
		super.createPages();
		
		for (IExtendedPage page : extendsPages) {
			page.onCreate();
		}
		setPartName(getEditorTitle());
		setActivePage(getEditorIndex());
	}
	
	protected String getEditorTitle() {
		// ���ñ༭������
		String partName = TITLE_LABEL_PROVIDER.getText(getARESResource());
		if (isReadOnly()) {
			partName += "(ֻ��)";
		}
		return partName;
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#configurePage(int, org.eclipse.ui.forms.editor.IFormPage)
	 */
	@Override
	protected void configurePage(int index, IFormPage page) throws PartInitException {
		super.configurePage(index, page);
		if (page instanceof IEMFFormPage) {
			((IEMFFormPage) page).setEditableControl(editableControl);
		} 
	}
	
	/**
	 * ��ȡ��չҳ��
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void createExtendPage() {
		for(ExtendPageInfo info:ExtendPageManager.getDefault().getPageInfo(getSite().getId())){
			try {
				Class cls = info.getPageClass();
				Constructor cst = null;
				try {
					cst = cls.getConstructor(new Class[] { FormEditor.class, String.class, String.class });
				} catch (Exception e) {	}
				
				IExtendedPage page = null;
				if (cst != null) {
					page = (IExtendedPage) cst.newInstance(new Object[] { this, info.getPageId(), info.getPageName() });
				} else {
					page = (IExtendedPage) cls.newInstance();
					((IExtendedPage) page).init(this);
				}
				
				if (page instanceof ExtendPageWithMyDirtySystem) {
					((ExtendPageWithMyDirtySystem)page).setInfo(getInfo());
					if (!info.isHidden() && ((IExtendItemLoader) page).shouldLoad()) {
						addPage((FormPage)page);
					}
				}
				
				extendsPages.add(page);
			} catch (Exception e) {
				logger.error("��ȡ��չҳ���쳣", e);
			}
		}
	}
	
	protected void handleBeforeSave() {
		for (IExtendedPage page : extendsPages) {
			page.beforeSave();
		}
	}
	
	protected void handleAfterSave() {
		for (IExtendedPage page : extendsPages) {
			page.afterSave();
		}
	}
	
	@Override
	final public void doSave(IProgressMonitor monitor) {
		fileSyncUnit.beforeSave();
		if (isReadOnly()) {
			MessageDialog.openInformation(getSite().getShell(), "�޷�����", "��ǰ��Դ��ֻ��״̬���޷����б���");
		} else {
			handleBeforeSave();
			
			WorkspaceModifyOperation operation =
				new WorkspaceModifyOperation() {
					@Override
					public void execute(IProgressMonitor monitor) {
						try {
							IARESResource aresResource = getARESResource();
							aresResource.save(getInfo(), true, monitor);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
					}
				};

			try {
				operation.run(monitor);
				((BasicCommandStack)editingDomain.getCommandStack()).saveIsDone();
				firePropertyChange(IEditorPart.PROP_DIRTY);
				handleAfterSave();
			} catch (Exception exception) {
				MessageDialog.openError(getSite().getShell(), "����ʧ��", "�쳣��Ϣ��" + exception.getMessage());
			}
		}
	}
	
	@Override
	public void doSaveAs() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#createToolkit(org.eclipse.swt.widgets.Display)
	 */
	@Override
	protected FormToolkit createToolkit(Display display) {
		return new FormToolkitEx(display);
	}
	
	/**
	 * ���ر༭������ı༭��
	 * @return
	 */
	protected IDialogSettings getDialogSettings() {
		IDialogSettings settings = ARESEditorPlugin.getDefault().getDialogSettings();
		EClass eClass =getInfoClass();
		IDialogSettings blockSettings = null;
		if(eClass!=null){
			 blockSettings = settings.getSection(eClass.getInstanceClassName());
			if (blockSettings == null) {
				blockSettings = settings.addNewSection(eClass.getInstanceClassName());
				blockSettings.put(ACTIVE_EDITOR_INDEX, 0);
			}
		}
		
		return blockSettings;
	}
	/**
	 * ���ر༭�����Ļ�ı༭��
	 * @return
	 */
	protected int getEditorIndex() {
		IDialogSettings settings = getDialogSettings();
		if (settings != null) {
			try{
				editorIndex = settings.getInt(ACTIVE_EDITOR_INDEX);
			}catch(Exception e){
				editorIndex = 0;
			}
			
		}
		if(editorIndex <0){
			editorIndex = 0;
		}
		return editorIndex;
	}
	
	

	@Override
	public void dispose() {
		EditingDomainManager.getInstance().deconfigureListeners(getEditingDomainID(), editingDomain);
		JRESEditorSyncManager.getInstance().removeSyncUnit(fileSyncUnit);
		super.dispose();
		IDialogSettings settings = getDialogSettings();
		if(settings!=null){
			settings.put(ACTIVE_EDITOR_INDEX, getCurrentPage());
		}
	}
	
}
