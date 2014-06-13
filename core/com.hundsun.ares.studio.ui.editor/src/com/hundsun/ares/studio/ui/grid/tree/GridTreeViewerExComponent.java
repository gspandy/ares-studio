/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.grid.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.core.model.ICreateInstance;
import com.hundsun.ares.studio.ui.editor.AbstractHSFormEditor;
import com.hundsun.ares.studio.ui.util.Clipboard;

/**
 * 
 * ��һ����ҪΪΪ���༭�������˶��� �� �˵� �縴�� �����
 * 
 * @author gongyf
 * 
 * @param <T>
 */
public abstract class GridTreeViewerExComponent<T> extends GridTreeViewerEditorableComponent<T> {
	/**
	 * �Ƿ�ɿ���
	 * 
	 * @return
	 */
	public boolean canCopy() {

		// ѡ��Ķ�����Ҫ��ͬһ���ͣ���ʵ����ICreateInstance�ӿ�
		ITreeSelection sel = (ITreeSelection) viewer.getSelection();
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
	 * �Ƿ��ɾ��
	 * 
	 * @return
	 */
	public boolean canDelete() {

		if (readOnly) {
			return false;
		}

		// �����и�λ�ñ�ѡ��
		ITreeSelection sel = (ITreeSelection) viewer.getSelection();
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
		return !readOnly && ((ITreeSelection) viewer.getSelection()).getPaths().length > 0;
	}

	/**
	 * �Ƿ��ճ��
	 * 
	 * @return
	 */
	public boolean canPaste() {

		if (readOnly) {
			return false;
		}
		// �����и�λ�ñ�ѡ��
		ITreeSelection sel = (ITreeSelection) viewer.getSelection();
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
	 * ���и��Ʋ���
	 */
	public void copy() {
		ITreeSelection sel = (ITreeSelection) viewer.getSelection();
		TreePath[] paths = sel.getPaths();

		ArrayList<Object> copied = new ArrayList<Object>();
		for (TreePath path : paths) {
			copied.add(path.getLastSegment());
		}

		// ���������
		Clipboard.instance.setData(copied);
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

	/**
	 * ���м��в���
	 */
	public void cut() {
		copy();
		deleteWithOutConfirmed();
	}

	/**
	 * 
	 * ����ɾ������
	 */
	public void delete() {
		boolean confirmed = MessageDialog.openConfirm(viewer.getGrid().getShell(), "", "ȷʵҪɾ����?");
		if (!confirmed) {
			return;
		}
		deleteWithOutConfirmed();
	}

	/**
	 * ����ɾ������
	 */
	public void deleteWithOutConfirmed() {

		ITreeSelection sel = (ITreeSelection) viewer.getSelection();
		TreePath[] paths = sel.getPaths();

		if (paths.length > 0) {
			// ������·���̵���ǰ��
			Arrays.sort(paths, new Comparator<TreePath>() {
				public int compare(TreePath o1, TreePath o2) {
					return o1.getSegmentCount() - o2.getSegmentCount();
				}
			});
			GridTreeDeleteItemOperation operation = new GridTreeDeleteItemOperation("", this, paths);
			operation.addContext(undoContext);
			try {
				AbstractHSFormEditor.getOperationHistory().execute(operation, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected int treeColumn = 0;

	@Override
	protected void initComposite(Composite client) {
		adjustStyle();
		super.initComposite(client);
		if (treeColumn >= 0 && viewer.getGrid().getColumnCount() > treeColumn) {
			if (viewer.getGrid().getColumn(treeColumn) != null) {
				viewer.getGrid().getColumn(treeColumn).setTree(true);
			}
		}

		viewer.getGrid().addKeyListener(new KeyListener() {

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
					viewer.getGrid().selectAll();
				} else if (e.keyCode == SWT.INSERT) {
					if (canInsert()) {
						insert();
					}
				} else if (e.keyCode == SWT.F5) {
					viewer.refresh();
				}
			}

			public void keyReleased(KeyEvent e) {

			}
		});
	}

	/**
	 * ��ʼ���˵����������ʵ�ָ÷��������Լ��Ĳ˵�
	 */
	protected void initTreeMenu() {
		// ��Ӹ��ƣ�ճ����ɾ��
		GridTreeViewerActionGroup group = new GridTreeViewerActionGroup(this);
		group.fillContextMenu(new MenuManager());
	}

	/**
	 * ���в������
	 */
	public void insert() {
		ITreeSelection sel = (ITreeSelection) viewer.getSelection();
		TreePath[] paths = sel.getPaths();

		TreePath parentPath = paths[0].getParentPath();
		List addItems = new ArrayList();
		addItems.add(createBlankData(parentPath.getLastSegment()));
		GridTreeAddItemOperation operation = new GridTreeAddItemOperation("add", this, paths, addItems);
		operation.addContext(undoContext);
		try {
			AbstractHSFormEditor.getOperationHistory().execute(operation, null, null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����ճ������
	 */
	public void paste() {
		ITreeSelection sel = (ITreeSelection) viewer.getSelection();
		TreePath[] paths = sel.getPaths();
		List<Object> objs = (List) Clipboard.instance.getData();
		GridTreeAddItemOperation operation = new GridTreeAddItemOperation("", this, paths, objs);
		operation.addContext(undoContext);
		try {
			AbstractHSFormEditor.getOperationHistory().execute(operation, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
