package com.hundsun.ares.studio.jres.database.ui.editors.dialog.inner;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;

import com.hundsun.ares.studio.core.ConsoleHelper;

public abstract class BasicTableViewer extends TableViewer {
	public static final Logger LOGGER = ConsoleHelper.getLogger();

	public static final int DEFAULT_STYLE = SWT.FULL_SELECTION | SWT.BORDER | SWT.V_SCROLL;

	public BasicTableViewer(Composite parent) {
		this(parent, DEFAULT_STYLE);
	}

	public BasicTableViewer(Composite parent, int style) {
		super(parent, style);
		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
		Layout layout = (Layout) parent.getLayout();
		getTable().setLayoutData(
				getDefaultLayoutData(layout instanceof GridLayout ? ((GridLayout) layout).numColumns : 1));
		getTable().addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				event.height = height;
			}
		});
		setContentProvider(createContentProvider());
		
		initColumns();
	}
	
	/**
	 * Ĭ���и�
	 */
	public static int DEFAULT_HEIGHT = 22;

	private int height = DEFAULT_HEIGHT;

	/**
	 * �����и�
	 * 
	 * @param height
	 */
	public void setRowHeight(int height) {
		this.height = height;
	}

	/**
	 * ��ȡ�и�
	 * 
	 * @return
	 */
	public int getRowHeight() {
		return height;
	}

	/**
	 * ����Ĭ��LayoutData���ڳ�ʼ��ʱ����
	 * 
	 * @param cols��������������
	 * @return
	 */
	protected GridData getDefaultLayoutData(int cols) {
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = cols;
		data.heightHint = 220;
		return data;
	}

	private Map<String, TableViewerColumn> colsMap = new HashMap<String, TableViewerColumn>();

	/**
	 * ��ȡ��������е��У�Map��Key�Ǳ���еı���
	 * 
	 * @return
	 */
	public Map<String, TableViewerColumn> getColumnsMap() {
		return colsMap;
	}

	protected void initColumns() {
		String[] titles = createTitles();
		int[] titleWidth = createTitlesWidth();
		if (titles.length != titleWidth.length) {
			LOGGER.debug("�������п����ø�����ͬ");
			return;
		}

		for (int i = 0; i < titles.length; i++) {
			TableViewerColumn column = new TableViewerColumn(this, SWT.NONE);
			column.getColumn().setText(titles[i]);
			column.getColumn().setWidth(titleWidth[i]);
			colsMap.put(titles[i], column);
			column.setEditingSupport(createEditingSupport(i));
			ColumnLabelProvider labelProvider = createLabelProvider(i);
			if (labelProvider != null)
				column.setLabelProvider(labelProvider);
		}
	}
	
	protected IContentProvider createContentProvider() {
		return new ArrayContentProvider();
	}

	/**
	 * ��������еı���
	 * 
	 * @return
	 */
	protected abstract String[] createTitles();

	/**
	 * ����еĿ��
	 * 
	 * @return
	 */
	protected abstract int[] createTitlesWidth();

	/**
	 * ��������е�LabelProvider���ڱ���ʼ��ʱָ����EditingSupport������������á�����ڳ�ʼ��ʱ���ݲ����Դ���LabelProvider����������Է���null��
	 * Ȼ��ͨ��{@link #getColumnsMap()} �õ��к�������
	 * 
	 * @param index
	 *            ����ţ������б�����{@link #createTitles()}���ص������е����
	 */
	protected abstract ColumnLabelProvider createLabelProvider(int index);

	/**
	 * ��������е�EditingSupport���ڱ���ʼ��ʱ���á�����ڳ�ʼ��ʱ���ݲ����Դ���EditingSupport����������Է���null��
	 * Ȼ��ͨ��{@link #getColumnsMap()} �õ��к�������
	 * 
	 * @param index
	 *            ����ţ������б�����{@link #createTitles()}���ص������е����
	 */
	protected abstract EditingSupport createEditingSupport(int index);
}
