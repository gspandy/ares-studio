package com.hundsun.ares.studio.ui;

import java.util.ArrayList;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class TreeListView extends ViewPart {
	private Tree tree;
	private TreeViewer listViewer;
	private Action doubleClickAction;

	public static final String VIEW_ID = "com.rayootech.xdesktop.ui.views.TreeListView";

	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	class TreeObject implements IAdaptable {
		private String name;
		private TreeParent parent;

		public TreeObject(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setParent(TreeParent parent) {
			this.parent = parent;
		}

		public TreeParent getParent() {
			return parent;
		}

		public String toString() {
			return getName();
		}

		public Object getAdapter(Class key) {
			return null;
		}
	}

	class TreeParent extends TreeObject {
		private ArrayList children;

		public TreeParent(String name) {
			super(name);
			children = new ArrayList();
		}

		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}

		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}

		public TreeObject[] getChildren() {
			return (TreeObject[]) children.toArray(new TreeObject[children
					.size()]);
		}

		public boolean hasChildren() {
			return children.size() > 0;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider,
			ITreeContentProvider {
		private TreeParent invisibleRoot;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot == null)
					initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}

		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject) child).getParent();
			}
			return null;
		}

		public Object[] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).getChildren();
			}
			return new Object[0];
		}

		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent) parent).hasChildren();
			return false;
		}

		/*
		 * We will set up a dummy model to initialize tree heararchy. In a real
		 * code, you will connect to a real model and expose its hierarchy.
		 */
		private void initialize() {
			TreeParent root_one = new TreeParent("���Ž���");
			
			TreeParent child_one = new TreeParent("�Ŷӵ�������");
			
			
			TreeObject child_one_one = new TreeObject("���뵵��");
			TreeObject child_one_two = new TreeObject("�ֹ�¼��Ԥ�㵵��");
			TreeObject child_one_three = new TreeObject("�ֹ�¼����㵵��");
			TreeObject child_one_four = new TreeObject("Ԥ�㵵������");
			TreeObject child_one_five = new TreeObject("���㵵������");
			TreeObject child_one_six = new TreeObject("���������");
			
			root_one.addChild(child_one);
			
			child_one.addChild(child_one_one);
			child_one.addChild(child_one_two);
			child_one.addChild(child_one_four);
			child_one.addChild(child_one_three);
			child_one.addChild(child_one_five);
			child_one.addChild(child_one_six);
			
			
			TreeParent child_two = new TreeParent("���˿����");
			
			TreeObject child_two_one = new TreeObject("ϵͳ�տ�");
			TreeObject child_two_two = new TreeObject("�ֹ�¼���տ�ƾ֤");
			TreeObject child_two_three= new TreeObject("�����˿�����");
			TreeObject child_two_four = new TreeObject("�ֹ�¼���˿�ƾ֤");
			TreeObject child_two_five = new TreeObject("�˿��ƾ֤��ˣ�");
			TreeObject child_two_six = new TreeObject("���˿���Чƾ֤��ѯ");
			TreeObject child_two_seven = new TreeObject("�ͻ�Ԥ���˻�����");
			TreeObject child_two_eight = new TreeObject("��Ʊ����");
			
			
			
			root_one.addChild(child_two);
			
			child_two.addChild(child_two_one);
			child_two.addChild(child_two_two);
			child_two.addChild(child_two_three);
			child_two.addChild(child_two_four);
			child_two.addChild(child_two_five);
			child_two.addChild(child_two_six);
			child_two.addChild(child_two_seven);
			child_two.addChild(child_two_eight);
			
			
			TreeParent child_three = new TreeParent("�������");
			
			
			TreeObject child_three_one = new TreeObject("ϵͳ�������봦��");
			TreeObject child_three_two = new TreeObject("�ֹ�¼�븶��ƾ֤");
			TreeObject child_three_three = new TreeObject("�����ƾ֤��ˣ�");
			TreeObject child_three_four = new TreeObject("����ƾ֤��ѯ");
			TreeObject child_three_five = new TreeObject("��Ӧ��Ԥ���˻�����");
			
			root_one.addChild(child_three);
			
			child_three.addChild(child_three_one);
			child_three.addChild(child_three_two);
			child_three.addChild(child_three_four);
			child_three.addChild(child_three_three);
			child_three.addChild(child_three_five);
			
			
			TreeParent child_four = new TreeParent("�Ƶ�������");
			
			
			TreeObject child_four_one = new TreeObject("ϵͳ�Ƶ�����������");
			TreeObject child_four_two = new TreeObject("�ֹ�¼��Ƶ����ƾ֤");
			TreeObject child_four_three = new TreeObject("�Ƶ�����");
			TreeObject child_four_four = new TreeObject("�Ƶ�����ѯ");
			TreeObject child_four_five = new TreeObject("�Ƶ�����");
			TreeObject child_four_six = new TreeObject("�Ƶ��˻�����");
			
			root_one.addChild(child_four);
			
			child_four.addChild(child_four_one);
			child_four.addChild(child_four_two);
			child_four.addChild(child_four_four);
			child_four.addChild(child_four_three);
			child_four.addChild(child_four_five);
			child_four.addChild(child_four_six);
			
			
			TreeParent child_five = new TreeParent("Ӧ��Ӧ��");
			
			
			TreeObject child_five_one = new TreeObject("Ӧ�ղ�ѯ����������λ��");
			TreeObject child_five_two = new TreeObject("Ӧ�ղ�ѯ�����Ŷӣ�");
			TreeObject child_five_three = new TreeObject("Ӧ����ѯ����������λ��");
			TreeObject child_five_four = new TreeObject("Ӧ����ѯ�����Ŷӣ�");
			TreeObject child_five_five = new TreeObject("Ӧ��Ԥ��");
			
			root_one.addChild(child_five);
			
			child_five.addChild(child_five_one);
			child_five.addChild(child_five_two);
			child_five.addChild(child_five_four);
			child_five.addChild(child_five_three);
			child_five.addChild(child_five_five);
			
			TreeObject child_six = new TreeObject("�������");
			
			root_one.addChild(child_six);
			
			
			

			TreeParent root_two = new TreeParent("ϵͳ����");
			
			TreeParent parent_one = new TreeParent("������Ŀ");
			
			TreeObject parent_one_one = new TreeObject("ϵͳ��λ�鿴");
			TreeObject parent_one_two = new TreeObject("�����û�����");
			TreeObject parent_one_three = new TreeObject("�����ɫ");
			TreeObject parent_one_four = new TreeObject("Ȩ�޷���");
			TreeObject parent_one_five = new TreeObject("����ά��");

			root_two.addChild(parent_one);
			
			parent_one.addChild(parent_one_one);
			parent_one.addChild(parent_one_two);
			parent_one.addChild(parent_one_three);
			parent_one.addChild(parent_one_four);
			parent_one.addChild(parent_one_five);
			
			
			TreeParent parent_two = new TreeParent("������λ");
			
			TreeObject parent_two_one = new TreeObject("��־����");

			root_two.addChild(parent_two);
			
			parent_two.addChild(parent_two_one);
			
			
			TreeParent root_three = new TreeParent("��������");
			
			TreeParent parent_three = new TreeParent("������Ϣ");
			
			TreeObject parent_three_one = new TreeObject("�ɱ���Ŀ");
			TreeObject parent_three_two = new TreeObject("·�߱�׼");
			TreeObject parent_three_three = new TreeObject("·�߷���");
			TreeObject parent_three_four = new TreeObject("���ʽ");
			TreeObject parent_three_five = new TreeObject("������Ϣ");

			root_three.addChild(parent_three);
			
			parent_three.addChild(parent_three_one);
			parent_three.addChild(parent_three_two);
			parent_three.addChild(parent_three_three);
			parent_three.addChild(parent_three_four);
			parent_three.addChild(parent_three_five);
			
			
			TreeParent parent_four = new TreeParent("������λ");
			
			TreeObject parent_four_one = new TreeObject("�ͻ��������̣�������Ϣ����");
			TreeObject parent_four_two = new TreeObject("�ͻ�������������");
			TreeObject parent_four_three = new TreeObject("�ͻ�����/����");
			TreeObject parent_four_four = new TreeObject("��Ӧ�̻�����Ϣ����");
			TreeObject parent_four_five = new TreeObject("Ѻ�����");

			root_three.addChild(parent_four);
			
			parent_four.addChild(parent_four_one);
			parent_four.addChild(parent_four_two);
			parent_four.addChild(parent_four_three);
			parent_four.addChild(parent_four_four);
			parent_four.addChild(parent_four_five);
			


			invisibleRoot = new TreeParent("");
			invisibleRoot.addChild(root_one);
			invisibleRoot.addChild(root_two);
			invisibleRoot.addChild(root_three);
		}
	}

	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}

		public Image getImage(Object obj) {
			// ���ӽڵ�ͼƬ
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof TreeParent)
				// �Ÿ��ڵ�ͼƬ
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}


	/**
	 * The constructor.
	 */
	public TreeListView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		listViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		tree = listViewer.getTree();
		//tree.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));
		listViewer.setContentProvider(new ViewContentProvider());
		listViewer.setLabelProvider(new ViewLabelProvider());
		listViewer.setInput(getViewSite());

		makeActions();
		hookDoubleClickAction();
		initializeToolBar();
	}

	private void makeActions() {
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = listViewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				// showMessage("Double-click detected on " + obj.toString());
				if (!selection.isEmpty()) {
					IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IViewPart iv = null;
				}
			}
		};
	}

	
	private void hookDoubleClickAction() {
		listViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		listViewer.getControl().setFocus();
	}
	private void initializeToolBar() {
		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
	}
}