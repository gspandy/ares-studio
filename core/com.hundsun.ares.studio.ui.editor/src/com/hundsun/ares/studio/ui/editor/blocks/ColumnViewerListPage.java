/**
 * Դ�������ƣ�ColumnViewerListPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.blocks;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.EMFFormPage;
import com.hundsun.ares.studio.ui.editor.actions.ButtonGroupManager;
import com.hundsun.ares.studio.ui.editor.actions.IUpdateAction;
import com.hundsun.ares.studio.ui.editor.viewers.RefreshViewerJob;

/**
 * ��һ��ColumnViewer��Ϊ��Ҫ�༭������ı༭��ҳ<br>
 * �ṩ��Actionע�������ܸ�������ˢ�¼̳���{@link IUpdateAction}��Action<BR>
 * Action��ˢ�·�Ϊ3��
 * <li>��ColumnViewer��ѡ���л���ʱ��ˢ�µ� {@link #getSelectionActions()}</li>
 * <li>������ջ�仯��ʱ��ˢ�µ� {@link #getStackActions()}</li>
 * <li>�༭�����Ա仯��ʱ��ˢ�µ� {@link #getPropertyActions()}</li>
 * 
 * Action��ʹ����Ҫ����{@link #createActions()}�н��д�����ע��<br>
 * ��{@link #createButtons(ButtonGroupManager)}����Action�����ô�����ť�б�<br>
 * ��{@link #createMenus(IMenuManager)}����Action�����ô����˵��б�
 * @author gongyf
 *
 */ 
public abstract class ColumnViewerListPage extends EMFFormPage implements ISelectionChangedListener {

	private static final Logger logger = Logger.getLogger(ColumnViewerListPage.class);
	
	private ColumnViewer columnViewer;
	
	private ActionRegistry actionRegistry;
	private List<String> selectionActions;
	private List<String> stackActions;
	private List<String> propertyActions;
	
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public ColumnViewerListPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/**
	 * @param columnViewer the columnViewer to set
	 */
	protected void setColumnViewer(ColumnViewer columnViewer) {
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
	public ColumnViewer getColumnViewer() {
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
	 * @return the actionRegistry
	 */
	protected ActionRegistry getActionRegistry() {
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
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		updateActions(getSelectionActions());
	}
	
	protected Control createClient(Composite parent, FormToolkit toolkit) {
		Composite client = toolkit.createComposite(parent);
		Control viewer = createColumnViewer(client, toolkit);
		Control buttons = createViewerButtons(client, toolkit);
		
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(client);
		GridDataFactory.fillDefaults().hint(100, 100).grab(true, true).applyTo(viewer);
		GridDataFactory.fillDefaults().grab(false, true).hint(80, -1).applyTo(buttons);
		
		return client;
	}
	
	/**
	 * �ڲ�����{@link #setColumnViewer(ColumnViewer)} ��{@link #configureColumnViewer(ColumnViewer)}
	 * @param parent
	 * @param toolkit
	 * @return
	 */
	protected Control createColumnViewer(Composite parent, FormToolkit toolkit) {
		TableViewer viewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		setColumnViewer(viewer);
		
		configureColumnViewer(getColumnViewer());
		
		return viewer.getControl();
	}
	

	protected abstract void createMenus(IMenuManager menuManager);
	
	protected abstract void createColumns(ColumnViewer viewer);
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#doCreateFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void doCreateFormContent(IManagedForm managedForm) {
		Composite body = managedForm.getForm().getBody();
		FormToolkit toolkit = managedForm.getToolkit();
		
		createClient(body, toolkit);
		body.setLayout(new FillLayout());
	}
	

	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#infoChange()
	 */
	@Override
	public void infoChange() {
		
		getColumnViewer().setInput(getInfo());
		
		updateActions(getPropertyActions());
		updateActions(getSelectionActions());
		super.infoChange();
		updateActions(getStackActions());
	}
	
	protected Control createViewerButtons(Composite parent, FormToolkit toolkit) {
		
		ButtonGroupManager btnGroupManager = new ButtonGroupManager();
		createButtons(btnGroupManager);
		btnGroupManager.createControl(parent);
		
		
		toolkit.adapt(btnGroupManager.getControl());
		
		return btnGroupManager.getControl();
	}
	
	protected void createButtons(ButtonGroupManager manager) {
		
	}
	
	/**
	 * ��ñ��������ṩ��
	 * @return
	 */
	protected abstract IContentProvider getColumnViewerContentProvider();
	
	protected void configureColumnViewer(ColumnViewer viewer) {
		viewer.setUseHashlookup(true);
		
		createColumns(viewer);
		viewer.setContentProvider(getColumnViewerContentProvider());
		
		MenuManager menuManager = new MenuManager("#popup");
		menuManager.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.removeAll();
				createMenus(manager);
			}
		});

		Menu menu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		
		// �������tooltip��ʾ
		ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.RECREATE);
		
		// ��Ϊ��ť�����Ǳ����صģ����Ա���ȱ�񴴽���ɺ��ʼ������
		initializeActionRegistry();
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#commandStackChanged(java.util.EventObject)
	 */
	@Override
	public void commandStackChanged(EventObject event) {
		super.commandStackChanged(event);
		RefreshViewerJob.refresh(getColumnViewer(), null, false);
		Command cmd = ((CommandStack)event.getSource()).getMostRecentCommand();
		if (cmd != null) {
			RefreshViewerJob.refresh(getColumnViewer(), cmd.getAffectedObjects().toArray());
		}
		updateActions(getStackActions());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#firePropertyChange(int)
	 */
	@Override
	protected void firePropertyChange(int propertyId) {
		super.firePropertyChange(propertyId);
		updateActions(getPropertyActions());
	}
	
	protected void initializeActionRegistry() {
		createActions();
		updateActions(getPropertyActions());
		updateActions(getStackActions());
	}
	
	protected void createActions() {
		
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (active) {
			ColumnViewer viewer = getColumnViewer();
			if (viewer != null) {
				getSite().setSelectionProvider(viewer);
				logger.debug("Selection provider: " + viewer.hashCode() + " Site: " + getSite().hashCode());
			} else {
				logger.debug("Page actived, but no viewer!");
			}
		} 
	}
	
}
