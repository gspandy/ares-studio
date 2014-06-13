/**
 * Դ�������ƣ�TreeViewerBlock.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.blocks;

import java.util.Map;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.ui.editor.decorators.EnterForNewLineTreeViewerBlockDecorator;
import com.hundsun.ares.studio.ui.editor.viewers.ColumnViewerPatternFilter;

/**
 * @author gongyf
 *
 */
public abstract class TreeViewerBlock extends ColumnViewerBlock<TreeViewer> {

	
	//����һ�������˿�ı��
	protected TreeViewer doCreateColumnViewer(Composite parent, FormToolkit toolkit) {
		FilteredTree tree = new FilteredTree(parent, FormWidgetUtils.getDefaultTreeStyles(), new ColumnViewerPatternFilter(), true) {
			/* (non-Javadoc)
			 * @see org.eclipse.ui.dialogs.FilteredTree#getRefreshJobDelay()
			 */
			@Override
			protected long getRefreshJobDelay() {
				return getColumnViewer().getTree().getItemCount() / 40 + 100;
			}
		};
		
		// TASK #8541 ����ӿ�������������������Ϣʱ���޷����붨λ������
		// 2013-10-15 sundl ���������˫���༭, Ĭ�ϵĻᴥ������չ���¼������²��ܱ༭��
		// ��һ�δ�����Խ���˫��չ�������ԡ��ο�http://www.eclipse.org/forums/index.php/t/257325/
		tree.getViewer().getControl().addListener(SWT.MeasureItem, new Listener() {
			@Override
			public void handleEvent(Event event) {
			}
		});
		// ������δ�����Դﵽ���Ƶ�Ч������ͬ���ǣ����������ֹչ�������ԣ�Ҳ����˫�����Լ���CellEditor��ͬʱҲ��չ����
//		tree.getViewer().addOpenListener(new IOpenListener() {
//			@Override
//			public void open(OpenEvent event) {
//			}
//		});
		
		toolkit.adapt(tree);
		tree.getViewer().getTree().setHeaderVisible(true);
		tree.getViewer().getTree().setLinesVisible(true);
		tree.setFont(JFaceResources.getTextFont());
		
		return tree.getViewer();
	}

	
	@Override
	protected void restoreState(IDialogSettings settings) {
		super.restoreState(settings);
		
		TreeViewer viewer = getColumnViewer();
		
		{
			// �ָ��п�����
			String[] savedWidthes = settings.getArray(SAVED_WIDTHES);
			if (savedWidthes != null) {
				int[] saved = new int[savedWidthes.length];
				for (int i = 0; i < savedWidthes.length; i++) {
					saved[i] = Integer.parseInt(savedWidthes[i]);
				}
				
				if (viewer.getTree().getColumns().length == saved.length) {
					for (int i=0;i<saved.length;i++) {
						viewer.getTree().getColumns()[i].setWidth(saved[i]);
					}
				}
				
			} else {
				// û�б�����Զ�ʹ���������
				for (TreeColumn column : viewer.getTree().getColumns()) {
					//2013��5��14��14:06:33 �������г�ʼ�Զ���ֵ����ʹ�ó�ʼֵ
					if(column.getWidth() <= 0){
						column.pack();
					}
				}
			}
		}

		{
			// �ָ���˳��
			String[] savedOrder = settings.getArray(SAVED_COLUMNS);
			if (savedOrder != null) {
				int[] saved = new int[savedOrder.length];
				for (int i = 0; i < savedOrder.length; i++) {
					saved[i] = Integer.parseInt(savedOrder[i]);
				}
				if (saved.length > 0 && saved.length == viewer.getTree().getColumns().length) {
					viewer.getTree().setColumnOrder(saved);
				}
			}
		}
		

	}
	
	@Override
	protected void storeState(IDialogSettings settings) {
		super.storeState(settings);
		
		{
			// ������
			TreeColumn[] columns = getColumnViewer().getTree().getColumns();
			String[] widthes = new String[columns.length];
			for (int i = 0; i < columns.length; i++) {
				widthes[i] = String.valueOf(columns[i].getWidth());
			}
			settings.put(SAVED_WIDTHES, widthes);
		}

		
		{
			// ������
			int[] order = getColumnViewer().getTree().getColumnOrder();
			String[] strOrder = new String[order.length];
			for (int i = 0; i < order.length; i++) {
				strOrder[i] = String.valueOf(order[i]);
			}
			settings.put(SAVED_COLUMNS, strOrder);
		}

	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerBlock#configureColumnViewer(org.eclipse.jface.viewers.ColumnViewer)
	 */
	@Override
	protected void configureColumnViewer(final TreeViewer viewer) {
		
		TreeViewerEditor.create(viewer, createColumnViewerEditorActivationStrategy(viewer), 
				ColumnViewerEditor.TABBING_HORIZONTAL | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR);
		
		super.configureColumnViewer(viewer);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerBlock#configureDecorators(java.util.Map)
	 */
	@Override
	protected void configureDecorators(
			Map<String, IColumnViewerBlockDecorator<TreeViewer>> decorators) {
		super.configureDecorators(decorators);
		decorators.put(EnterForNewLineTreeViewerBlockDecorator.ID, new EnterForNewLineTreeViewerBlockDecorator());
	}
}
