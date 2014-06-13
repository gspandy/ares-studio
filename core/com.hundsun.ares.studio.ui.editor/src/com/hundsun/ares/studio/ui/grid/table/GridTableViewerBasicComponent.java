/**
 * <p>Copyright: Copyright   2010</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.grid.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.ui.celleditor.ISprecialCellEditor;
import com.hundsun.ares.studio.ui.grid.GridViewerExComponent;
import com.hundsun.ares.studio.ui.util.HSColorManager;

/**
 * �����ı��༭�� ��һ���ṩ��LabelProvider��ContentProvider ������һЩ��չ����صĲ���
 * 
 * @author maxh
 * 
 * @param <T>
 */
public abstract class GridTableViewerBasicComponent<T> extends GridViewerExComponent<T> {
	/** ָ�������Ƿ���ҪΨһ�Լ�� */
	protected HashMap<String, Boolean> checkStatus = new HashMap<String, Boolean>();

	/** ָ���е��ظ���� */
	protected HashMap<String, HashMap<Object, Integer>> checkCache = new HashMap<String, HashMap<Object, Integer>>();

	/** ���������ı�� */
	protected FilteredGridTable tbComposite = null;

	/** Ԥ���༭�Ķ��� */
	protected T lastLine = createBlankData();

	/** ��ǰ�ؼ��ı����� */
	protected GridTableViewer viewer = null;

	private boolean addOnlyOne = false;

	/**
	 * �ж�ָ����Ԫ���Ƿ��ܱ༭
	 * 
	 * @param data
	 * @param property
	 * @return
	 */
	protected abstract boolean canEdit(T data, String property);

	/**
	 * �½�һ���հ�����
	 * 
	 * @return
	 */
	protected abstract T createBlankData();

	public T createNewObject() {
		return createBlankData();
	}

	protected FilteredGridTable createFilteredTable(Composite client, int style) {
		FilteredGridTable table = new FilteredGridTable(client, style, new HSGridTableFilter(), this);
		return table;
	}

	/**
	 * ȡ������ı���ɫ,��������ĺ�ɫ����������趨
	 * 
	 * @param element
	 * @param property
	 * @return
	 */
	protected Color getBackground(Object data, String property) {
		return null;
	}

	public HashMap<String, HashMap<Object, Integer>> getCheckCache() {
		return checkCache;
	}

	protected Color getForeground(Object data) {
		return null;
	}

	@Override
	protected Grid getGrid() {
		return viewer.getGrid();
	}

	/**
	 * ��õ�Ԫ���ض���ͼ��
	 * 
	 * @param data
	 * @param property
	 * @return
	 */
	protected Image getImage(T data, String property) {
		return null;
	}

	/**
	 * ��ȡ��ǰ��������в������Ϸ�������Ϣ�б�
	 */
	protected List<T> getInvalidRows() {
		List<T> invalidRows = new ArrayList<T>();
		Iterator<T> rows = input.iterator();
		while (rows.hasNext()) {
			T row = rows.next();
			boolean isValid = GridTableViewerBasicComponent.this.isValidData(row);
			if (!isValid) {
				invalidRows.add(row);
			}
		}

		return invalidRows;
	}

	/**
	 * @return the lastLine
	 */
	public T getLastLine() {
		return lastLine;
	}

	public GridTableViewer getTableViewer() {
		return viewer;
	}

	/**
	 * ��õ�Ԫ�����ʾ�ı���Ĭ��ʵ����GetValue��toString
	 * 
	 * @param data
	 * @param property
	 * @return
	 */
	protected String getText(T data, String property) {
		Object value = getValue(data, property);
		if (value != null) {
			return value.toString();
		}
		return "";
	}

	/**
	 * �õ�toolTip��Ԫ�����ɫ
	 * 
	 * @return
	 */
	public Color getToolTipColor() {
		return null;
	}

	/**
	 * �õ��õ�Ԫ��toolTip����,���Է���null
	 * 
	 * @param element
	 * @param property
	 * @return
	 */
	public String getToolTipText(T element, String property) {
		return null;
	}

	abstract public Object getValue(T data, String property);

	// /**
	// * �õ��õ�Ԫ��toolTip����(��������ɫ��Ϣ)
	// * <br>ͬһ��Ԫ��,���getToolTipTextҲ������, �����ݿ��ܱ�����
	// *
	// * @param element
	// * @param property
	// * @return
	// */
	// public ErrorMessage getToolTipTextBackground(Object element, String
	// property) {
	// return null;
	// }
	//
	// /**
	// * �õ��õ�Ԫ��toolTip����(����ǰ��ɫ��Ϣ)
	// * <br>
	// * @param element
	// * @param property
	// * @param isBackGroundAble ��ʾ�Ƿ��ڵ�ǰ����Ϊ�յ�ʱ�� ��������ɫ(ֵΪ��ʱ, ǰ��ɫ���޷���ʾ��)
	// * @return
	// */
	// public ErrorMessage getToolTipTextForeground(Object element, String
	// property) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	public GridTableViewer getViewer() {
		return this.viewer;
	}

	protected void initComposite(Composite client) {
		tbComposite = createFilteredTable(client, getStyle());
		tbComposite.setLayoutData(getCompositeLayoutData());
		viewer = tbComposite.getViewer();
		initViewer(viewer);

		viewer.setContentProvider(new TableViewerContentProvider());
		viewer.setRowHeaderLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell viewerCell) {
				updateRowHeader(viewerCell);
			}

		});
		viewer.setInput(input);
	}

	/**
	 * @return the addOnlyOne
	 */
	public boolean isAddOnlyOne() {
		return addOnlyOne;
	}

	final protected boolean isLastLine(T data) {
		return data == lastLine;
	}

	/**
	 * ������֤<BR>
	 * ���ش�����Ϣ������null�����޴���
	 * 
	 * @param data
	 * @param property
	 * @return
	 */
	protected String isValid(T data, String property) {
		return null;
	}

	/**
	 * ������֤<BR>
	 * �����Ƿ�ͨ����֤������true�������ݺϷ�
	 * 
	 * @param data
	 * @return
	 */
	protected boolean isValidData(T data) {
		return true;
	}

	/**
	 * ֻ�����һ����¼�������Զ�����
	 * 
	 * @param addOnlyOne
	 *            the addOnlyOne to set
	 */
	public void setAddOnlyOne(boolean addOnlyOne) {
		this.addOnlyOne = addOnlyOne;
		this.useAutoGrow = false;
	}

	/**
	 * ���ñ�����Ƿ����Ψһ�Լ��<BR>
	 * Ψһ�Ե��ж�������GetValue�������ص�Object��equals���
	 * 
	 * @param property
	 * @param docheck
	 */
	final protected void setUniqueCheck(String property, boolean docheck) {
		boolean isCheck = checkStatus.get(property);
		if (isCheck != docheck) {
			checkStatus.put(property, docheck);
			viewer.getGrid().setRedraw(false);
			viewer.refresh();
			viewer.getGrid().setRedraw(true);
		}
	}

	protected void updateRowHeader(ViewerCell viewerCell) {

	}

	protected class DelegateCellLabelProvider extends ColumnLabelProvider {

		protected String property;
		private boolean isBackground = true;
		/** �����Ӧ�еĴ�����Ϣ */
		private Map<Object, String> errMsgMap = new HashMap<Object, String>();

		public DelegateCellLabelProvider(String property) {
			super();
			this.property = property;
		}

		@Override
		public Color getBackground(Object element) {
			if (element != lastLine) {
				// �ⲿ�ּ��ؼ����Ƿ����ظ�
				String errMessage = GridTableViewerBasicComponent.this.isValid((T) element, property);
				errMsgMap.put(element, errMessage);
				if (checkStatus.get(property)) {
					Integer times = checkCache.get(property).get(getValue((T) element, property));
					if (times != null && times > 1) {
						errMessage = "�������ظ�����";
						errMsgMap.put(element, errMessage);
						return colorManager.getColor(HSColorManager.RED);
					}
				}
				if (errMessage != null) {
					return colorManager.getColor(HSColorManager.RED);
				}
				// �ⲿ�ּ���Ƿ��д���
				errMessage = GridTableViewerBasicComponent.this.isValid((T) element, property);
				errMsgMap.put(element, errMessage);
				if (errMessage != null) {
					return colorManager.getColor(HSColorManager.RED);
				}
			}
			return GridTableViewerBasicComponent.this.getBackground(element, property);
		}

		@Override
		public Image getImage(Object element) {
			if (GridTableViewerBasicComponent.this.getCellEditor(element, property) instanceof ISprecialCellEditor) {
				ISprecialCellEditor cellEditor = (ISprecialCellEditor) GridTableViewerBasicComponent.this
						.getCellEditor(element, property);
				return cellEditor.getImage(GridTableViewerBasicComponent.this.getValue((T) element, property));
			}
			return GridTableViewerBasicComponent.this.getImage((T) element, property);
		}

		@Override
		public String getText(Object element) {
			if (GridTableViewerBasicComponent.this.getCellEditor(element, property) instanceof ISprecialCellEditor) {
				ISprecialCellEditor cellEditor = (ISprecialCellEditor) GridTableViewerBasicComponent.this
						.getCellEditor(element, property);
				return cellEditor.getText(GridTableViewerBasicComponent.this.getValue((T) element, property));
			}
			return GridTableViewerBasicComponent.this.getText((T) element, property);
		}

		@Override
		public int getToolTipDisplayDelayTime(Object object) {
			return 300;
		}

		@Override
		public Point getToolTipShift(Object object) {
			return new Point(5, 5);
		}

		@Override
		public String getToolTipText(Object element) {
			if (errMsgMap.get(element) != null) {
				return errMsgMap.get(element);
			}
			if (GridTableViewerBasicComponent.this.getToolTipText((T) element, property) != null) {
				return GridTableViewerBasicComponent.this.getToolTipText((T) element, property);
			}
			if (getText(element) == null || getText(element).length() < 40) {
				return null;
			}
			StringBuffer buffer = new StringBuffer(getText(element));
			int lenght = buffer.length();
			int i = 39;
			while (i < lenght) {
				buffer.insert(i, '\n');
				i += 40;
			}
			return buffer.toString();
		}

		@Override
		public int getToolTipTimeDisplayed(Object object) {
			return -1;
		}

		/**
		 * @return the isBackground
		 */
		public boolean isBackground() {
			return isBackground;
		}

		/**
		 * @param isBackground
		 *            the isBackground to set
		 */
		public void setBackground(boolean isBackground) {
			this.isBackground = isBackground;
		}
	}

	/**
	 * �ṩ�ṩ��
	 * 
	 * @author gongyf
	 * 
	 */
	private class TableViewerContentProvider implements IStructuredContentProvider {

		public void dispose() {

		}

		/**
		 * ��ʾһ���հ���
		 */
		public Object[] getElements(Object inputElement) {
			// �����
			for (String property : checkCache.keySet()) {
				checkCache.put(property, new HashMap<Object, Integer>());
			}
			for (T obj : input) {
				for (String property : checkCache.keySet()) {
					Map<Object, Integer> objCount = checkCache.get(property);
					Object value = getValue(obj, property);
					if (objCount.containsKey(value)) {
						objCount.put(value, objCount.get(value) + 1);
					} else {
						objCount.put(value, 1);
					}
				}
			}
			List<T> all = new ArrayList<T>();
			all.addAll(input);

			// �����Ĭ����
			if (useAutoGrow && !readOnly) {
				all.add(lastLine);
			}
			if (addOnlyOne && input.size() < 1) {
				all.add(lastLine);
			}
			return all.toArray();// input.toArray();

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	public void refresh() {
		if (viewer != null) {
			viewer.refresh();
		}
	}
}
