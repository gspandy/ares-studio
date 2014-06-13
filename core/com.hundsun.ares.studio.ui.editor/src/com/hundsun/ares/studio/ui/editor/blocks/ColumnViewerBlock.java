/**
 * Դ�������ƣ�ColumnViewerBlock.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.blocks;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;
import com.hundsun.ares.studio.ui.editor.GlobalActionHandlerProviderSupport;
import com.hundsun.ares.studio.ui.editor.IGlobalActionHandlerProvider;
import com.hundsun.ares.studio.ui.editor.IGlobalActionHandlerProviderListener;
import com.hundsun.ares.studio.ui.editor.actions.IActionIDConstant;
import com.hundsun.ares.studio.ui.editor.actions.IUpdateAction;
import com.hundsun.ares.studio.ui.editor.editable.IEditableControl;
import com.hundsun.ares.studio.ui.editor.viewers.RefreshViewerJob;
import com.hundsun.ares.studio.ui.editor.viewers.SynRefreshViewerJob;
import com.hundsun.ares.studio.ui.util.ARESEMFClipboard;
import com.hundsun.ares.studio.ui.util.IJRESClipboardListener;
import com.hundsun.ares.studio.ui.validate.IProblemPool;
import com.hundsun.ares.studio.ui.viewers.link.JRESColumnViewerEditorActivationStrategy;

/**
 * @author gongyf
 *
 */
// TODO: Ӧ��֧��Block�ڷǱ༭���Ļ�����ʹ�ã������ڶԻ�����
// 1. toolkit֧��Ϊnull���Ի����ж���û��toolkit�� �Ի���ʹ��Form��ʽҲ��̫Э��
// 2. ֻ�������õ�EditControl����Ϊnull
// 3. ProblemPool����Ϊnull
public abstract class ColumnViewerBlock<T extends ColumnViewer> implements ISelectionChangedListener, ISelectionProvider, IPropertyListener, CommandStackListener, IGlobalActionHandlerProvider {
	
	protected EditingDomain editingDomain;
	protected IARESResource resource;
	protected IProblemPool problemPool;
	
	protected static final String SAVED_WIDTHES = "saved_width";
	protected static final String SAVED_COLUMNS = "saved_column";
	
	/**
	 * ��ǰ���װ����
	 */
	private Map<String, IColumnViewerBlockDecorator<T> > decorators;
	
	private GlobalActionHandlerProviderSupport support = new GlobalActionHandlerProviderSupport(this);
	// ��¼��ǰ�Ƿ񼤻���ȫ�ֿ�ݼ�
	private boolean globalActionHandleEnabled = false;
	private IActionBars actionBars;
	
	private Control control;
	private T columnViewer;
	
	private ActionRegistry actionRegistry;
	private List<String> selectionActions;
	private List<String> stackActions;
	private List<String> propertyActions;
	private List<String> clipboardActions;
	
	private IEditableControl editableControl;
	
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	public IARESResource getARESResource() {
		return resource;
	}

	public IProblemPool getProblemPool() {
		return problemPool;
	}

	private IJRESClipboardListener clipboardListener = new IJRESClipboardListener() {
		@Override
		public void clipboardChanged(ARESEMFClipboard clipboard) {
			updateActions(getClipboardActions());
		}
		
		@Override
		public void clipboardAboutToBeChanged(ARESEMFClipboard clipboard) {
			
		}
	};
	
	/**
	 * �༭��ID
	 */
	abstract protected String getID();

	/**
	 * 
	 * ��ȡװ�����б���һ�λ����������չ���е�����
	 * @return the decorators
	 */
	public Map<String, IColumnViewerBlockDecorator<T> > getDecorators() {
		if (decorators == null) {
			decorators = new HashMap<String, IColumnViewerBlockDecorator<T>>();
			
			// ����չ�����ȡ
			IExtensionRegistry reg = Platform.getExtensionRegistry();
			IConfigurationElement[] elements = reg.getConfigurationElementsFor(ARESEditorPlugin.PLUGIN_ID , "BlockDecorators");
			
			for (IConfigurationElement element : elements) {
				try {
					String id = element.getAttribute("id");
					String blockId = element.getAttribute("blockId");
					if (StringUtils.equals(blockId, getID())) {
						IColumnViewerBlockDecorator<T> decorator = (IColumnViewerBlockDecorator<T>) element.createExecutableExtension("class");
						decorators.put(id, decorator);
					}
				} catch (Exception e) {
				}
			}
			
			configureDecorators(decorators);
		}
		return decorators;
	}
	
	/**
	 * ������Ҫ��װ����
	 * @param decorators
	 */
	protected void configureDecorators(Map<String, IColumnViewerBlockDecorator<T> > decorators) {
		
	}
	
	/**
	 * @return the support
	 */
	protected GlobalActionHandlerProviderSupport getProviderSupport() {
		return support;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.IGlobalActionHandlerProvider#addGlobalActionHandlerProviderListener(com.hundsun.ares.studio.jres.ui.form.IGlobalActionHandlerProviderListener)
	 */
	@Override
	public void addGlobalActionHandlerProviderListener(
			IGlobalActionHandlerProviderListener listener) {
		getProviderSupport().addListener(listener);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.IGlobalActionHandlerProvider#removeGlobalActionHandlerProviderListener(com.hundsun.ares.studio.jres.ui.form.IGlobalActionHandlerProviderListener)
	 */
	@Override
	public void removeGlobalActionHandlerProviderListener(
			IGlobalActionHandlerProviderListener listener) {
		getProviderSupport().removeListener(listener);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.IGlobalActionHandlerProvider#clearGlobalActions(org.eclipse.ui.IActionBars)
	 */
	@Override
	final public void clearGlobalActions(IActionBars actionBars) {
		globalActionHandleEnabled = false;
		doClearGlobalActions(actionBars);
		this.actionBars = null;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.IGlobalActionHandlerProvider#shareGlobalActions(org.eclipse.ui.IActionBars)
	 */
	@Override
	final public void shareGlobalActions(IActionBars actionBars) {
		globalActionHandleEnabled = true;
		doShareGlobalActions(actionBars);
		this.actionBars = actionBars;
	}

	protected void doShareGlobalActions(IActionBars actionBars) {
		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), getActionRegistry().getAction(IActionIDConstant.CV_COPY));
		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), getActionRegistry().getAction(IActionIDConstant.CV_PASTE));
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), getActionRegistry().getAction(IActionIDConstant.CV_DELETE));
	}
	
	protected void doClearGlobalActions(IActionBars actionBars) {
		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), null);
		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), null);
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), null);
	}
	
	/**
	 * �����ؼ�
	 * @param parent
	 * @param toolkit
	 */
	public void createControl(Composite parent, FormToolkit toolkit) {
		
		// ����������
		ARESEMFClipboard.getInstance().addClipboardListener(clipboardListener);
		
		Composite client = toolkit.createComposite(parent);
		
		Control viewer = createColumnViewer(client, toolkit);
		
		if (needButtonGroupArea()) {
			Control buttons = createViewerButtons(client, toolkit);
			
			GridLayoutFactory.swtDefaults().numColumns(2).applyTo(client);
			GridDataFactory.fillDefaults().hint(getViewerPreferredSize()).grab(true, true).applyTo(viewer);
			GridDataFactory.fillDefaults().grab(false, true).applyTo(buttons);
		} else {
			// ռ�����пռ�
			GridLayoutFactory.swtDefaults().applyTo(client);
			GridDataFactory.fillDefaults().hint(getViewerPreferredSize()).grab(true, true).applyTo(viewer);
		}
		
		control = client;
	}
	
	/**
	 * ��ȡ�����ͼ����Ѵ�С
	 * @return
	 */
	protected Point getViewerPreferredSize() {
		return new Point(100, 100);
	}
	
	/**
	 * �Ƿ���Ҫ������ť��
	 * @return
	 */
	protected boolean needButtonGroupArea() {
		return true;
	}
	
	/**
	 * @return the control
	 */
	public Control getControl() {
		return control;
	}
	
	/**
	 * @param editableControl the editableControl to set
	 */
	public void setEditableControl(IEditableControl editableControl) {
		this.editableControl = editableControl;
	}
	
	
	/**
	 * @return the editableControl
	 */
	public IEditableControl getEditableControl() {
		return editableControl;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.command.CommandStackListener#commandStackChanged(java.util.EventObject)
	 */
	@Override
	public void commandStackChanged(EventObject event) {
		RefreshViewerJob.refresh(getColumnViewer(), null, false);
		Command cmd = ((CommandStack)event.getSource()).getMostRecentCommand();
		if (cmd != null) {
			SynRefreshViewerJob.refresh(getColumnViewer(), cmd.getAffectedObjects().toArray() ,cmd , getHeadColumnFeature());
		}
		updateActions(getStackActions());
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	protected EStructuralFeature getHeadColumnFeature(){
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPropertyListener#propertyChanged(java.lang.Object, int)
	 */
	@Override
	public void propertyChanged(Object source, int propId) {
		updateActions(getPropertyActions());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (getColumnViewer() != null) {
			getColumnViewer().addSelectionChangedListener(listener);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		if (getColumnViewer() != null) {
			return getColumnViewer().getSelection();
		}
		return StructuredSelection.EMPTY;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		if (getColumnViewer() != null) {
			getColumnViewer().removeSelectionChangedListener(listener);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void setSelection(ISelection selection) {
		if (getColumnViewer() != null) {
			getColumnViewer().setSelection(selection);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		updateActions(getSelectionActions());
	}
	
	/**
	 * ��������
	 * @param input
	 */
	public void setInput(Object input) {
		if (getColumnViewer() != null) {
			getColumnViewer().setInput(input);
			
			updateActions(getPropertyActions());
			updateActions(getSelectionActions());
			updateActions(getStackActions());
			
			for (IColumnViewerBlockDecorator<T> d : getDecorators().values()) {
				d.inputChanged(this);
			}
		}
	}
	
	/**
	 * �ڲ�����{@link #setColumnViewer(ColumnViewer)} ��{@link #configureColumnViewer(ColumnViewer)}
	 * @param parent
	 * @param toolkit
	 * @return
	 */
	protected Control createColumnViewer(Composite parent, FormToolkit toolkit) {
		T viewer = doCreateColumnViewer(parent, toolkit);
		setColumnViewer(viewer);
		
		configureColumnViewer(getColumnViewer());
		
		// CellEditor���ֺ���Ҫ���ε���ݼ�
		viewer.getColumnViewerEditor().addEditorActivationListener(new ColumnViewerEditorActivationListener() {
			
			@Override
			public void beforeEditorDeactivated(
					ColumnViewerEditorDeactivationEvent event) {
			}
			
			@Override
			public void beforeEditorActivated(ColumnViewerEditorActivationEvent event) {
			}
			
			@Override
			public void afterEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
				if (globalActionHandleEnabled && actionBars != null) {
					doShareGlobalActions(actionBars);
				}
				
			}
			
			@Override
			public void afterEditorActivated(ColumnViewerEditorActivationEvent event) {
				if (actionBars != null) {
					doClearGlobalActions(actionBars);
				}
			}
		});
		
		
		addDoubleClickListener(viewer);
		
		return viewer.getControl();
	}
	
	/**
	 * ������˫������
	 * TASK #9521 ���б��˫��������������
	 * @param viewer
	 */
	protected void addDoubleClickListener(final ColumnViewer viewer) {
		viewer.getControl().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				ViewerCell cell = viewer.getCell(new Point(e.x, e.y));
				//cellΪnull ��ʾ�ǵ�Ԫ��Ϊ����
				if(null == cell){
					IAction action = getAddAction();
					if(action != null){
						action.run();
					}
				}
			}
		});
	}
	
	/**
	 * Ĭ�ϵ���������action��˫������ʱ����
	 * TASK #9521 ���б��˫��������������
	 * @return
	 */
	protected IAction getAddAction(){
		return getActionRegistry().getAction(IActionIDConstant.CV_ADD);
	}

	/**
	 * �������
	 * @param parent
	 * @param toolkit
	 * @return
	 */
	protected abstract T doCreateColumnViewer(Composite parent, FormToolkit toolkit);
	
	protected Control createViewerButtons(Composite parent, FormToolkit toolkit) {
		ToolBar toolbar = new ToolBar(parent, SWT.VERTICAL);
		ToolBarManager manager = new ToolBarManager(toolbar);
		createToolbarItems(manager);
		toolkit.adapt(toolbar);
		manager.update(true);
		return toolbar;
	}
	
//  2012-09-05 sundl ��Toolbar�滻
//	/**
//	 * ��ӿ��ư�ť
//	 * @param manager
//	 */
//	protected void createButtons(ButtonGroupManager manager) {
//		
//	}
	
	protected void createToolbarItems(ToolBarManager manager) {
		
	}
	
	/**
	 * ��ñ��������ṩ��
	 * @return
	 */
	protected abstract IContentProvider getColumnViewerContentProvider();
	
	protected abstract void createMenus(IMenuManager menuManager);
	
	protected abstract void createColumns(T viewer);
	
	protected ColumnViewerEditorActivationStrategy createColumnViewerEditorActivationStrategy(T viewer) {
		// ˫�������޸ĵ�Ԫ��
		ColumnViewerEditorActivationStrategy actSupport = new JRESColumnViewerEditorActivationStrategy(viewer);
		
		return actSupport;
	}
	
	protected void addToolTipSupport(T viewer){
		ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.RECREATE);
	}
	
	protected void configureColumnViewer(T viewer) {
		viewer.setUseHashlookup(true);
		
		createColumns(viewer);
		viewer.setContentProvider(getColumnViewerContentProvider());
		
		MenuManager menuManager = new MenuManager("#popup");
		menuManager.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.removeAll();
				createMenus(manager);
				// װ��������װ��
				for (IColumnViewerBlockDecorator<T> d : getDecorators().values()) {
					d.decorateMenu(ColumnViewerBlock.this, manager);
				}
			}
		});

		Menu menu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		
		// �������tooltip��ʾ
		addToolTipSupport(viewer);
		
		// ��Ϊ��ť�����Ǳ����صģ����Ա���ȱ�񴴽���ɺ��ʼ������
		initializeActionRegistry();
		
		// ֻ������
		if (getEditableControl() != null) {
//			getEditableControl().addEditableUnit(new JresDefaultEditableUnit(viewer.getControl()));
		}
		
		restoreState(getDialogSettings());
		
		viewer.getControl().addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				onDispose();
			}
		});
		
		// ֻ�м����ʱ�򣬿�ݼ�����Ч
		viewer.getControl().addListener(SWT.Activate, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				getProviderSupport().fireProviderActived();
			}
		});
		
		viewer.getControl().addListener(SWT.Deactivate, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				getProviderSupport().fireProviderDeactived();
			}
		});
		

		// ����һЩ�Զ���Ŀ�ݼ�����Щ��ݼ�ֻ��Ҫ��Action�ж��岢��ӵ�ActionRegister�м���
		viewer.getControl().addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// �鿴��ǰAction�Ƿ�����Ҫ�Ŀ�ݼ�
				for (Iterator<IAction> iterator = getActionRegistry().getActions(); iterator
						.hasNext();) {
					IAction action = iterator.next();
					if (action.getAccelerator() != 0 && action.getAccelerator() == (e.stateMask | e.keyCode)) {
						if (action.isEnabled()) {
							action.run();
							return;
						}
					}
				}
			}
		});
		
		// װ��������װ��
		for (IColumnViewerBlockDecorator<T> d : getDecorators().values()) {
			d.decorateViewer(this, viewer);
		}
		
	}
	
	private IDialogSettings getDialogSettings() {
		IDialogSettings settings = ARESEditorPlugin.getDefault().getDialogSettings();
		IDialogSettings blockSettings = settings.getSection(getID());
		if (blockSettings == null) {
			blockSettings = settings.addNewSection(getID());
		}
		return blockSettings;
	}
	
	/**
	 * ���ؼ����ٵ�ʱ�򴥷�
	 */
	protected void onDispose () {
		ARESEMFClipboard.getInstance().removeClipboardListener(clipboardListener);
		storeState(getDialogSettings());
	}
	
	protected void storeState(IDialogSettings settings) {
		
	}
	
	protected void restoreState(IDialogSettings settings) {
		
	}
	
	protected void initializeActionRegistry() {
		createActions();
		updateActions(getPropertyActions());
		updateActions(getStackActions());
		updateActions(getSelectionActions());
		updateActions(getClipboardActions());
	}
	
	protected void createActions() {
		
	}
	
	/**
	 * @param columnViewer the columnViewer to set
	 */
	protected void setColumnViewer(T columnViewer) {
		if (this.columnViewer != null) {
			this.columnViewer.removeSelectionChangedListener(this);
		}
		this.columnViewer = columnViewer;
		if (this.columnViewer != null) {
			this.columnViewer.addSelectionChangedListener(this);
		}
	}
	
	/**
	 * @return the columnViewer
	 */
	public T getColumnViewer() {
		return columnViewer;
	}
	
	/**
	 * @return the selectionActions
	 */
	protected List<String> getSelectionActions() {
		if (selectionActions == null) {
			selectionActions = new ArrayList<String>();
		}
		return selectionActions;
	}
	
	/**
	 * @return the stackActions
	 */
	protected List<String> getStackActions() {
		if (stackActions == null) {
			stackActions = new ArrayList<String>();
		}
		return stackActions;
	}
	
	/**
	 * @return the propertyActions
	 */
	protected List<String> getPropertyActions() {
		if (propertyActions == null) {
			propertyActions = new ArrayList<String>();
		}
		return propertyActions;
	}
	
	/**
	 * @return the clipboardActions
	 */
	protected List<String> getClipboardActions() {
		if (clipboardActions == null) {
			clipboardActions = new ArrayList<String>();
		}
		return clipboardActions;
	}
	
	/**
	 * @return the actionRegistry
	 */
	public ActionRegistry getActionRegistry() {
		if (actionRegistry == null)
			actionRegistry = new ActionRegistry();
		return actionRegistry;
	}
	
	protected void updateActions(final List<String> actionIds) {
		Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				ActionRegistry registry = getActionRegistry();
				Iterator<String> iter = actionIds.iterator();
				while (iter.hasNext()) {
					IAction action = registry.getAction(iter.next());
					if (action instanceof IUpdateAction)
						((IUpdateAction) action).update();
				}
			}
		});

	}
}
