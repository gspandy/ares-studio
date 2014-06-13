/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.control.deprecated;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.core.model.ICreateInstance;
import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;
import com.hundsun.ares.studio.ui.editor.AbstractHSFormEditor;
import com.hundsun.ares.studio.ui.grid.EditorComponent;
import com.hundsun.ares.studio.ui.util.Clipboard;
import com.hundsun.ares.studio.ui.util.HSColorManager;

/**
 * ��д�����������
 * Ŀǰ�޷�û��Ψһ�Լ��������ƶ�����
 * ����ĵ�Ԫ���޸ķ�ʽΪ˫���޸�<BR>
 * Ĭ����֤ʧ�ܵ�����ʾΪǰ����ɫ<BR>
 * ������ʵ����ICreateInstance�ӿں󣬲ſ��Խ��и���ճ��<BR>
 * 
 * <BR>
 * <BR>
 * <B>PS:��Ҫע����Ǳ�ʵ��������ⲿֱ��������ڲ����ݵ�ʱ���絼��ʱ�����input�������������е�map�����޷����£���������ö���Ķѻ�
 * ������Щ������ڱ༭���ر�ʱ����������ڴ����ʹ�õĻ��Ტ���� </B>
 * 
 * @author gongyf
 *
 * @param <T>
 * 
 * @deprecated
 */
public abstract class TreeViewerExComponent<T/* extends IHaveChildren*/> extends EditorComponent< List<T> > {

	protected TreeViewer viewer = null;
	
	/** ��������������ͼ */
	protected FilteredTree filteredTree;
	
	/** �������а�ť */
	private List<Button> buttons = null;
	
	/** �Ƿ�ʹ���Զ������� */
	protected boolean useAutoGrow = true;
	
	/** �Ƿ�Ϊֻ����ֻ�����ɼ��У�ճ�����Զ����������޸ĵ�Ԫ�� */
	protected boolean readOnly = false;
	
	/** ��һ��ĸ��ӱ༭�� */
	protected T lastLine = (T)createBlankData(null);
	
	/** ���ڵ�͸����еĹ���map */
	protected HashMap<Object, Object> childrenLastLine = new HashMap<Object, Object>();
	
	/** ���ڵ�ͺ��ӽڵ�Ĺ���map */
	protected HashMap<Object, List<Object>> childrenMap = new HashMap<Object, List<Object>>();
	
	/** �ض��ĵ�Ԫ��༭�������ȼ�����Ĭ�� */
	protected HashMap<String, HashMap<Object, CellEditor> > specialEditorMap = new HashMap<String, HashMap<Object,CellEditor>>();
	
	/** ���������ж���ӳ�� */
	protected HashMap<String, TreeViewerColumn> columnMap = new HashMap<String, TreeViewerColumn>();
	
	/** ��������Ĭ�ϵ�Ԫ���޸���ӳ�� */
	protected HashMap<String, CellEditor> editorMap = new HashMap<String, CellEditor>();
	
	/** ��ɫ��Դͳһ���� */ 
	protected HSColorManager colorManager = ARESEditorPlugin.getDefault().getColorManager();
	
	protected String[] treePropertys;
	protected String[] treeTitles;
	
	/**
	 * �����޸� ���� ˫���޸�
	 */
	private boolean doubleCheckChange = true;
	
	public void setDoubleCheckChange(boolean doubleCheckChange) {
		this.doubleCheckChange = doubleCheckChange;
	}
	/**
	 * �ж��Ƿ��Ǹ�����
	 * @param obj
	 * @return
	 */
	final protected boolean isLastLine(Object obj) {
		return obj == lastLine || childrenLastLine.containsValue(obj);
	}
	
	/**
	 * ��ó��˸�����������еĸ��ڵ㣬�����Ļ�����null
	 * @param element
	 * @return
	 */
	protected Object getParent(Object element) {
		// ֱ����map�в�ѯ
		for (Object parent : childrenMap.keySet()) {
			if (childrenMap.get(parent).indexOf(element) != -1) {
				return parent;
			}
		}
		return null;
	}
	
	public HashMap<Object, Object> getChildrenLastLine() {
		return childrenLastLine;
	}
	
	/**
	 * ��ø����еĸ��ڵ�
	 * 
	 * @param element
	 * @return
	 */
	final protected Object getLastLineParent(Object element) {
		if (element == lastLine) {
			return null;
		}
		for (Object parent : childrenLastLine.keySet()) {
			if (childrenLastLine.get(parent) == element) {
				return parent;
			}
		}
		return null;
	}
	
	/**
	 * �����ṩ��
	 * <br>ע�� jdk6��jdk5��@override���岻һ��
	 * @author gongyf
	 *
	 */
	public class TreeViewerContentProvider implements  ITreeContentProvider {
		public Object[] getChildren(Object parentElement) {
			
			List<Object> children = TreeViewerExComponent.this.getChildren(parentElement);
			// ɸѡ״̬�¿��ܻ���null
			if (children == null) {
				return new Object[0];
			}
			childrenMap.put(parentElement, children);
			if (useAutoGrow && !readOnly) {

					
				List<Object> lstShowChildren = new ArrayList<Object>();
				lstShowChildren.addAll(children);
				
				Object thisLastLine = childrenLastLine.get(parentElement);
				if (thisLastLine == null) {
					// ��һ����Ҫ����һ��
					thisLastLine = createBlankData(parentElement);
					childrenLastLine.put(parentElement, thisLastLine);
				}
				
				lstShowChildren.add(thisLastLine);
				
				return lstShowChildren.toArray();
			}
			return children.toArray();
		}

		
		public Object getParent(Object element) {
			return TreeViewerExComponent.this.getParent(element);
		}

		
		public boolean hasChildren(Object element) {
			// ���ӱ༭��û�к��ӽڵ�
			if (isLastLine(element)) {
				return false;
			}
			
			return TreeViewerExComponent.this.getChildren(element) != null;
		}

		
		public Object[] getElements(Object inputElement) {
			
			// ��ĩ�м�¼���
			//lastLines.clear();
			
			List<Object> objs = new ArrayList<Object>();
			
			// �ȼ�������
			objs.addAll((List)inputElement);
			
			// ������ĩ��
			if (useAutoGrow && !readOnly) {
				objs.add(lastLine);
			}

			return objs.toArray();
		}

		
		public void dispose() {
			
		}

		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			
		}
		
	}
	
	protected class DelegateCellLabelProvider extends ColumnLabelProvider {

		private String property;
		
		/** �����Ӧ�еĴ�����Ϣ */
		private Map<Object, String> errMsgMap = new HashMap<Object, String>();
		
		public DelegateCellLabelProvider(String property) {
			super();
			this.property = property;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getBackground(java.lang.Object)
		 */
		@Override
		public Color getBackground(Object element) {
			if (!isLastLine(element)) {
				String errMessage = TreeViewerExComponent.this.isValid(element, property);
				errMsgMap.put(element, errMessage);
				if (errMessage != null) {
					return colorManager.getColor(HSColorManager.RED);
				}
				String requiredMessage = TreeViewerExComponent.this.isRequired(element, property);
				if(requiredMessage!=null)
				{
					errMsgMap.put(element, requiredMessage);
					return colorManager.getColor(HSColorManager.GREEN);
				}
			}
			
			return TreeViewerExComponent.this.getBackground(element, property);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getImage(java.lang.Object)
		 */
		@Override
		public Image getImage(Object element) {
			return TreeViewerExComponent.this.getImage(element, property);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object element) {
			return TreeViewerExComponent.this.getText(element, property);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.CellLabelProvider#getToolTipDisplayDelayTime(java.lang.Object)
		 */
		@Override
		public int getToolTipDisplayDelayTime(Object object) {
			return 300;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.CellLabelProvider#getToolTipShift(java.lang.Object)
		 */
		@Override
		public Point getToolTipShift(Object object) {
			return new Point(5, 5);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.CellLabelProvider#getToolTipText(java.lang.Object)
		 */
		@Override
		public String getToolTipText(Object element) {
			return errMsgMap.get(element);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.CellLabelProvider#getToolTipTimeDisplayed(java.lang.Object)
		 */
		@Override
		public int getToolTipTimeDisplayed(Object object) {
			return -1;
		}
		
	}
	
	protected class DelegateEditingSupport extends EditingSupport {

		private String property;
		
		public DelegateEditingSupport(String property) {
			super(TreeViewerExComponent.this.viewer);
			this.property = property;
		}

		@Override
		protected boolean canEdit(Object element) {
			return !readOnly && TreeViewerExComponent.this.canEdit(element, property);
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			HashMap<Object, CellEditor> map = specialEditorMap.get(property);
			if (map.containsKey(element)) {
				return map.get(element);
			}
			return TreeViewerExComponent.this.editorMap.get(property);
		}

		@Override
		protected Object getValue(Object element) {
			return TreeViewerExComponent.this.getValue(element, property);
		}

		@Override
		protected void setValue(Object element, Object value) {
			Object oldValue = getValue(element);
			if (value != null && !value.equals(oldValue)) {
				TreeChangeValueOperation operation = new TreeChangeValueOperation("change",TreeViewerExComponent.this,element,property,value);
				operation.addContext(undoContext);
				try {
					AbstractHSFormEditor.getOperationHistory().execute(operation, null, null);
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				// �ж��Ƿ��ڶ����һ�н���д��
				if (isLastLine(element)) {
					if (value != null && !value.equals(oldValue)) {
						commit(TreeViewerExComponent.this.getLastLineParent(element), element);
						
					}
				}
				
				dirty.setValue(true);
			}
		}
		
	}
	
	private class TreePatternFilter extends PatternFilter {
		
		/* (non-Javadoc)
		 * @see org.eclipse.ui.dialogs.PatternFilter#isElementVisible(org.eclipse.jface.viewers.Viewer, java.lang.Object)
		 */
		@Override
		public boolean isElementVisible(Viewer viewer, Object element) {
			// ��������ɸѡ״̬�²���ʾ
			if (isLastLine(element)) {
				return false;
			}
			return super.isElementVisible(viewer, element);
		}

		protected boolean isLeafMatch(Viewer viewer, Object element) {
			for (String property : columnMap.keySet()) {
				String label = TreeViewerExComponent.this.getText(element, property);
				if (label != null && wordMatches(label)) {
					return true;
				}
			}
			
			return false;
		}
	}
	
	/**
	 * ����������ӵ�ʵ��������ȥ
	 * 
	 * @param parent
	 * @param child
	 */
	protected void commit(Object parent, Object child) {
		if (parent == null) {
			input.add((T)child);
			lastLine = (T)createBlankData(null);
		} else {
			List<Object> lstChildren = childrenMap.get(parent);
			lstChildren.add(child);
			childrenLastLine.remove(parent);
		}
	}

	public String isRequired(Object element, String property) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * ���༭���Ƿ��д
	 * 
	 * @return
	 */
	public boolean canCopy() {
		
		// ѡ��Ķ�����Ҫ��ͬһ���ͣ���ʵ����ICreateInstance�ӿ�
		ITreeSelection sel = (ITreeSelection)viewer.getSelection();
		TreePath[] paths = sel.getPaths();
		
		// ��Ҫ��ѡ�����Ŀ
		if (paths.length == 0) {
			return false;
		}
		
		// ��Ҫ���Կ�¡��
		if (!(paths[0].getLastSegment() instanceof ICreateInstance)) {
			return false;
		}
		
		// ��Ҫѡ�����Ŀ��һ������
		Class<?> cls = paths[0].getLastSegment().getClass();
		for (TreePath path : paths) {
			if (path.getLastSegment().getClass() != cls) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * �Ƿ�ɼ���
	 * 
	 * @return
	 */
	public boolean canCut() {
		return canCopy() && canDelete();
	}
	
	/**
	 * �Ƿ��ճ��
	 * 
	 * @return
	 */
	public boolean canPaste() {
		
		if (readOnly) return false;
		
		// �����и�λ�ñ�ѡ��
		ITreeSelection sel = (ITreeSelection)viewer.getSelection();
		TreePath[] paths = sel.getPaths();
		if (paths.length == 0) {
			return false;
		}
		
		// �����ж�����
		Object objTest = paths[0].getLastSegment();
		
		
		// ��Ҫ�����岻Ϊ�գ��������뵱ǰλ�õ�����һ��
		Object obj = Clipboard.instance.getData();
		if (obj != null && obj instanceof List) {
			// ��������ճ������
			if (!((List) obj).isEmpty()) {
				if (((List) obj).get(0).getClass() == objTest.getClass()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * �Ƿ��ɾ��
	 * 
	 * @return
	 */
	public boolean canDelete() {
		
		if (readOnly) {
			return false;
		}    
		
		// �����и�λ�ñ�ѡ��
		ITreeSelection sel = (ITreeSelection)viewer.getSelection();
		TreePath[] paths = sel.getPaths();
		if (paths.length == 0) {
			return false;
		}
		return true;
	}
	   
	/**
	 * �Ƿ�ɲ���
	 * 
	 * @return
	 */
	public boolean canInsert() {
		return !readOnly && ((ITreeSelection)viewer.getSelection()).getPaths().length > 0;
	}
	
	/**
	 * ���и��Ʋ���
	 */
	public void copy() {
		ITreeSelection sel = (ITreeSelection)viewer.getSelection();
		TreePath[] paths = sel.getPaths();
		
		ArrayList<Object> copied = new ArrayList<Object>();
		for (TreePath path : paths) {
			copied.add(path.getLastSegment());
		}
		
		// ���������
		Clipboard.instance.setData(copied);
	}
	
	/**
	 * ����Ϊ��������
	 */
	 public void generateNoExsitParams(){
		 
	 }
	
	/**
	 * ���м��в���
	 */
	public void cut() {
		copy();
		deleteWithOutConfirmed();
	}
	
	/**
	 * ����ճ������
	 */
	public void paste() {
		ITreeSelection sel = (ITreeSelection)viewer.getSelection();
		TreePath[] paths = sel.getPaths();
		
		// �����Ҫճ����λ�ú����ݣ���Ϊ�����ɷ�ճ�����жϺ���Щ�ض�����ȷ����
		TreePath parentPath = paths[0].getParentPath();
		
		List<Object> objs = (List)Clipboard.instance.getData();
		
		if (parentPath == TreePath.EMPTY) {
			int index = input.indexOf(paths[0].getLastSegment());
			if (index == -1) {
				index = input.size();
			}
			for (Object obj : objs) {
				input.add( index, ((ICreateInstance<T>)obj).getNewInstance());
			}
		} else {
			List<Object> lst = childrenMap.get(parentPath.getLastSegment());
			int index = lst.indexOf(paths[0].getLastSegment());
			if (index == -1) {
				index = lst.size();
			}
			for (Object obj : objs) {
				lst.add(index,((ICreateInstance<Object>)obj).getNewInstance());
			}
		}
		
		dirty.setValue(true);
		viewer.refresh();
	}

	/**
	 * ����ɾ������
	 */
	public void delete() {
		boolean confirmed = MessageDialog.openConfirm(viewer.getTree().getShell(),"","ȷʵҪɾ����");
		if(!confirmed){
			return ;
		}
		deleteWithOutConfirmed();
	}
	
	public HashMap<Object, List<Object>> getChildrenMap() {
		return childrenMap;
	}
	
	//������ȷ�Ͼ�ɾ��
	public void deleteWithOutConfirmed(){

		ITreeSelection sel = (ITreeSelection)viewer.getSelection();
		TreePath[] paths = sel.getPaths();
		
		// ���ܻ�ɾ����ͬ��������
		
		// ������·���̵���ǰ��
		Arrays.sort(paths, new Comparator<TreePath>(){

			public int compare(TreePath o1, TreePath o2) {
				return o1.getSegmentCount() - o2.getSegmentCount();
			}});
		
		// ����Ҫ��·��
		ArrayList<TreePath> needlessPaths = new ArrayList<TreePath>();
		
		// ��Ҫ����ɾ����·��
		ArrayList<TreePath> deletePaths = new ArrayList<TreePath>();
		for (TreePath path : paths) {
			TreePath parentPath = path.getParentPath();
			if (deletePaths.indexOf(parentPath) != -1 || needlessPaths.indexOf(parentPath) != -1) {
				needlessPaths.add(path);
				continue;
			}
			
			deletePaths.add(path);
		}
		// ����ɾ������
		for (TreePath path : deletePaths) {
			TreePath parentPath = path.getParentPath();
			if (parentPath == TreePath.EMPTY) {
				input.remove(path.getLastSegment());
			} else {
				childrenMap.get(parentPath.getLastSegment()).remove(path.getLastSegment());
				childrenLastLine.remove(path.getLastSegment());
			}
		}
		viewer.refresh();
		dirty.setValue(true);
	} 
	
	/**
	 * ���в������
	 */
	public void insert() {
		ITreeSelection sel = (ITreeSelection)viewer.getSelection();
		TreePath[] paths = sel.getPaths();
		
		TreePath parentPath = paths[0].getParentPath();
		List addItems = new ArrayList();
		addItems.add(createBlankData(parentPath.getLastSegment()));
		TreeAddItemOperation operation = new TreeAddItemOperation("add",this,paths,addItems);
		operation.addContext(undoContext);
		try {
			AbstractHSFormEditor.getOperationHistory().execute(operation, null, null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		dirty.setValue(true);
		
	}
	
	/**
	 * ȡ������ı���ɫ,��������ĺ�ɫ����������趨
	 * 
	 * @param element
	 * @param property
	 * @return
	 */
	protected Color getBackground(Object element, String property) {
		return null;
	}
	
	/**
	 * ȡ�ú��ӽڵ��б�<BR>
	 * 
	 * <B>ע�⣺��ͼ�ϵĲ�����ֱ�ӷ�Ӧ�����List�ϵģ����Ա������ݵ�ʱ���������ListΪ׼</B><BR>
	 * 
	 * @param parentElement
	 * @return null ��ʾ�ýڵ㲻���к��ӽڵ�
	 */
	protected abstract List getChildren(Object parentElement);
	
	/**
	 * ����һ��Button����
	 * 
	 * @param toolkit
	 * @param client
	 * @param caption
	 * @return
	 */
	final protected Button createButton(FormToolkit toolkit, Composite client,
			String caption) {
		
		Button button = null;
		if (toolkit == null) {
			button = new Button(client, SWT.PUSH);
			button.setText(caption);
		} else {
			button = toolkit.createButton(client, caption, SWT.PUSH);
		}
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.horizontalAlignment = SWT.FILL;
		button.setLayoutData(gd);

		return button;
	}
	
	/**
	 * Ĭ�ϵĴ�����ť������������ �����ƶ� 2����ť
	 * 
	 * @param toolkit
	 * @param client
	 * @return
	 */
	protected List<Button> createButtons(FormToolkit toolkit, Composite client) {

		List<Button> buttons = new ArrayList<Button>();
		return buttons;
	}
	
	@Override
	final public Composite create(FormToolkit toolkit, Composite parent) {
		Composite client = null;
		if (toolkit == null) {
			client = new Composite(parent, SWT.NONE);
		} else {
			client = toolkit.createComposite(parent);
		}
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		client.setLayout(layout);
		client.setLayoutData(new GridData(GridData.FILL_BOTH));

		// ��ʼ���ؼ�
		initComposite(client);

		buttons = createButtons(toolkit, client);

		initTreeMenu();

		int size = buttons.size();
		
		// �������
		((GridData) filteredTree.getLayoutData()).verticalSpan = size > 0 ? size : 1;
		return client;
	}

	public TreeViewer getTreeViewer() {
		return viewer;
	}
	
	/**
	 * ���ڹ�����
	 * 
	 * @return
	 */
	protected PatternFilter getPatternFilter() {
		return new TreePatternFilter();
	}
	
	protected void initComposite(Composite client) {
		
		int style = SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL
			| SWT.MULTI | SWT.BORDER;
		filteredTree = new FilteredTree(client, style, getPatternFilter());
		
		viewer = filteredTree.getViewer();
		
		final Tree tree = viewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 200;
		gd.widthHint = 100;
		gd.verticalSpan = 1;
		filteredTree.setLayoutData(gd);
		
		// ˫�������޸ĵ�Ԫ��
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
				viewer) {
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				if(doubleCheckChange){
					return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL || 
					event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION || 
					event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
				}else{
					return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL || 
					event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION || 
					event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
				}
			}
		};

		TreeViewerEditor.create(viewer, actSupport,
				ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_VERTICAL
						| ColumnViewerEditor.KEYBOARD_ACTIVATION);

		// ����Tooltip��ʾ
		ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.RECREATE);
		
		tree.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 'c' && e.stateMask == SWT.CTRL) {
					if (canCopy()) {
						copy();
					}
				} else if (e.keyCode == 'x' && e.stateMask == SWT.CTRL) {
					if (canCut()) {
						cut();
					}
				} else if (e.keyCode == 'v' && e.stateMask == SWT.CTRL) {
					if (canPaste()) {
						paste();
					}
				} else if (e.keyCode == SWT.DEL) {
					if (canDelete()) {
						delete();
					}
				} else if (e.keyCode == 'a' && e.stateMask == SWT.CTRL) {
					tree.selectAll();
				} else if(e.keyCode == SWT.INSERT) {
					if(canInsert()) {
						insert();
					}
				}
			}

			public void keyReleased(KeyEvent e) {
				
			}
		});
		
		initViewColumn();
		
		viewer.setContentProvider(new TreeViewerContentProvider());
		viewer.setInput(input);
	}
	
	/**
	 * ��ʼ������������ز���<BR>
	 * ʹ��setColumns����
	 */
	protected abstract void initViewColumn();
	
	/**
	 * ���ó�ʼ��Ϣ<BR>
	 * 
	 * 
	 * @param captions ����
	 * @param widths ������
	 * @param styles ��񣬿�Ϊnull����ȫ��ΪĬ�ϵ�SWT.NONE
	 * @param propertys ����������Ϊnull�����Ա�������Ϊ������
	 * @param editors ��Ԫ��༭��,��Ϊnull����ʹ��TextCellEditor
	 */
	 protected void setColumns(String[] captions, int[] widths,
			int[] styles, String[] propertys, CellEditor[] editors) {

		if (captions == null) {
			return;
		}
		
		if( styles == null) {
			styles = new int[captions.length];
			for (int i = 0; i < styles.length; i++) {
				styles[i] = SWT.NONE;
			}
		}
		
		if (propertys == null) {
			propertys = captions;
		}
		this.treeTitles = new String[captions.length];
		this.treePropertys = new String[propertys.length];
		System.arraycopy(propertys, 0, treePropertys, 0, propertys.length);
		System.arraycopy(captions, 0,  treeTitles, 0, captions.length);
		
		if (editors == null) {
			editors = new CellEditor[captions.length];
			for (int i = 0; i < editors.length; i++) {
				editors[i] = new TextCellEditor(viewer.getTree());
			}
		}
		
		if (captions.length == widths.length
				&& captions.length == styles.length
				&& captions.length == propertys.length
				&& captions.length == editors.length) {

			for (int i = 0; i < captions.length; i++) {
				TreeColumn column = new TreeColumn(viewer.getTree(),
						styles[i]);
				column.setText(captions[i]);
				column.setWidth(widths[i]);
				
				TreeViewerColumn viewercolumn = new TreeViewerColumn(viewer, column);
				viewercolumn.setEditingSupport(new DelegateEditingSupport(propertys[i]));
				viewercolumn.setLabelProvider(new DelegateCellLabelProvider(propertys[i]));

				editorMap.put(propertys[i], editors[i]);
				columnMap.put(propertys[i], viewercolumn);
				
				specialEditorMap.put(propertys[i], new HashMap<Object, CellEditor>());
			}

		}
 	}
	
	/**
	 * ��ʼ���˵�
	 */
	protected void initTreeMenu() {
		// ��Ӹ��ƣ�ճ����ɾ��
		TreeViewerActionGroup group = new TreeViewerActionGroup(this);
		group.fillContextMenu(new MenuManager());

	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void setEditable(boolean editable) {
		readOnly = !editable;
		if(buttons == null){
			return;
		}
		for (Button btn : buttons) {
			btn.setEnabled(editable);
		}
	}
	
	/**
	 * �ж�ָ����Ԫ���Ƿ��ܱ༭
	 * 
	 * @param data ����ģ��
	 * @param property ����
	 * @return
	 */
	protected abstract boolean canEdit(Object data, String property);

	/**
	 * ���ָ����Ԫ���ֵ������CellEditor
	 * 
	 * @param data ����ģ��
	 * @param property ����
	 * @return
	 */
	public abstract Object getValue(Object data, String property);

	/**
	 * ��CellEditor��ֵ���ûص�Ԫ���Ƿ�Ӧ�ñ����ˢ�»��ڻ�������ɣ�������������ж�
	 * 
	 * @param data ����ģ��
	 * @param property ����
	 * @param value ���õ�ֵ
	 */
	public abstract void setValue(Object data, String property, Object value,boolean shouldRefresh);

	/**
	 * �½�һ���հ�����
	 * 
	 * @param parent ���ڵĸ��ڵ㣬null��ʱ�����Ϊ�����
	 * @return
	 */
	protected abstract Object createBlankData(Object parent);

	/**
	 * ��õ�Ԫ�����ʾ�ı���Ĭ��ʵ����GetValue��toString
	 * 
	 * @param data
	 * @param property
	 * @return
	 */
	protected String getText(Object data, String property) {
		Object value = getValue(data, property);
		if (value != null) {
			return value.toString();
		}
		return "";
		
	}

	/**
	 * ��õ�Ԫ���ض���ͼ��
	 * 
	 * @param data
	 * @param property
	 * @return
	 */
	protected Image getImage(Object data, String property) {
		return null;
	}
	
	/**
	 * ������֤<BR>
	 * ���ش�����Ϣ������null�����޴���
	 * 
	 * @param data
	 * @param property
	 * @return
	 */
	protected String isValid(Object data, String property) {
		return null;
	}
	

	public boolean isReadOnly() {
		return readOnly;
	}
	protected int getComboValue(String value,String[] sourceProperties) {
		for (int i = 0; i < sourceProperties.length; i++) {
			if (value.equals(sourceProperties[i]))
				return i;
		}
		return 0;
	}

	
	public TreeViewer getViewer() {
		return viewer;
	}
}
