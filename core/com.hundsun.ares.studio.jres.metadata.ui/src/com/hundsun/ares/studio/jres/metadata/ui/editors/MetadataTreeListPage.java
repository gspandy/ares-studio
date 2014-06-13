/**
 * Դ�������ƣ�MetadataTreeListPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.editors;

import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.emf.edit.ui.dnd.ViewerDragAdapter;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.jres.metadata.ui.editors.dnd.MetadataDropAdapter;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataTreeContentProvider;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.viewers.ColumnViewerPatternFilter;
import com.hundsun.ares.studio.ui.viewers.link.CellLinkColumnViewerEditorActivationStrategy;
import com.hundsun.ares.studio.ui.viewers.link.CellLinkMouseListener;
import com.hundsun.ares.studio.ui.viewers.link.ICellLinkProvider;

/**
 * ��Ԫ���ݵı��ʹ�ô����˿��{@link FilteredTree}��ʵ��<br>
 * �������ṩ��ͳһʹ��{@link MetadataTreeContentProvider}<br>
 * �����˱���ϵ���ק֧��
 * @author gongyf
 *
 */
public abstract class MetadataTreeListPage extends MetadataListPage implements ICellLinkProvider {

	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public MetadataTreeListPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	//����һ�������˿�ı��
	protected Control createColumnViewer(Composite parent, FormToolkit toolkit) {
		FilteredTree tree = new FilteredTree(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, new ColumnViewerPatternFilter(), true);
		
		toolkit.adapt(tree);
		tree.getViewer().getTree().setHeaderVisible(true);
		tree.getViewer().getTree().setLinesVisible(true);
		setColumnViewer(tree.getViewer());
		
		configureColumnViewer(getColumnViewer());
		

		
		return tree;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerListPage#getColumnViewerContentProvider()
	 */
	@Override
	protected IContentProvider getColumnViewerContentProvider() {
		return new MetadataTreeContentProvider();
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerListPage#configureColumnViewer(org.eclipse.jface.viewers.ColumnViewer)
	 */
	@Override
	protected void configureColumnViewer(ColumnViewer viewer) {
		super.configureColumnViewer(viewer);
		
		int dndOperations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance() };
		//�༭���б�֧����ק����
		viewer.addDragSupport(dndOperations, transfers, new ViewerDragAdapter(viewer));
		viewer.addDropSupport(dndOperations, transfers, new MetadataDropAdapter((TreeViewer)viewer, getEditingDomain()));
		
		TreeViewer treeViewer = (TreeViewer) viewer;
		
		// ��ֹ��Ctrl���ֱ༭��
		TreeViewerEditor.create(treeViewer, 
				new CellLinkColumnViewerEditorActivationStrategy(treeViewer), ColumnViewerEditor.DEFAULT);
		
		treeViewer.getTree().addMouseListener(new CellLinkMouseListener(viewer, this));
		
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.MetadataListPage#getColumnViewer()
	 */
	@Override
	public TreeViewer getColumnViewer() {
		return (TreeViewer) super.getColumnViewer();
	}
	
}
