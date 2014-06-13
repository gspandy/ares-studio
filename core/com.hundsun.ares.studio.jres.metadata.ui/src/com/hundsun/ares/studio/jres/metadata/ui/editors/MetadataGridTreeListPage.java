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

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataTreeContentProvider;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;

/**
 * ��Ԫ���ݵı��ʹ�ô����˿��{@link FilteredTree}��ʵ��<br>
 * �������ṩ��ͳһʹ��{@link MetadataTreeContentProvider}<br>
 * �����˱���ϵ���ק֧��
 * @author gongyf
 *
 */
public abstract class MetadataGridTreeListPage extends MetadataListPage {

	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public MetadataGridTreeListPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	protected Control createColumnViewer(Composite parent, FormToolkit toolkit) {
		GridTreeViewer viewer = new GridTreeViewer(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI| SWT.V_SCROLL | SWT.H_SCROLL);
		
//		viewer.getGrid().set
		
		toolkit.adapt(viewer.getGrid());
		viewer.getGrid().setHeaderVisible(true);
		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setTreeLinesVisible(false);
		setColumnViewer(viewer);
		
		configureColumnViewer(getColumnViewer());
		
		viewer.getGrid().setRowHeaderVisible(true);
		
		return viewer.getGrid();
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
		
//		int dndOperations = DND.DROP_COPY | DND.DROP_MOVE;
//		Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance() };
//		viewer.addDragSupport(dndOperations, transfers, new ViewerDragAdapter(viewer));
//		viewer.addDropSupport(dndOperations, transfers, new MetadataDropAdapter((TreeViewer)viewer, getEditDomain()));
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.MetadataListPage#getColumnViewer()
	 */
	@Override
	public GridTreeViewer getColumnViewer() {
		return (GridTreeViewer) super.getColumnViewer();
	}
}
