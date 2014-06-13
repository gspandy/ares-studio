/**
* <p>Copyright: Copyright   2010</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.grid.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.core.util.StringUtil;
import com.hundsun.ares.studio.ui.celleditor.ISprecialCellEditor;
import com.hundsun.ares.studio.ui.grid.GridViewerExComponent;
import com.hundsun.ares.studio.ui.util.HSColorManager;

/**
 * ���������༭�� ��һ���ṩ��LabelProvider��ContentProvider
 * ������һЩ��չ����صĲ���
 * @author maxh
 *
 * @param <T>
 */
public abstract class GridTreeViewerBasicComponent<T> extends GridViewerExComponent<T>  {

	protected GridTreeViewer viewer = null;

	
	/** ��������������ͼ */
	protected GridFilteredTree filteredTree;
	
	/** ��һ��ĸ��ӱ༭�� */
	protected T lastLine = (T)createBlankData(null);
	
	/** ���ڵ�͸����еĹ���map */
	protected HashMap<Object, Object> childrenLastLine = new HashMap<Object, Object>();
	
	/** ���ڵ�ͺ��ӽڵ�Ĺ���map */
	protected HashMap<Object, List<Object>> childrenMap = new HashMap<Object, List<Object>>();
	
	/**
	 * �ж�ָ����Ԫ���Ƿ��ܱ༭
	 * 
	 * @param data ����ģ��
	 * @param property ����
	 * @return
	 */
	protected abstract boolean canEdit(Object data, String property);
	
	/**
	 * �½�һ���հ�����
	 * 
	 * @param parent ���ڵĸ��ڵ㣬null��ʱ�����Ϊ�����
	 * @return
	 */
	protected abstract Object createBlankData(Object parent);
	
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

	public HashMap<Object, Object> getChildrenLastLine() {
		return childrenLastLine;
	}

	public HashMap<Object, List<Object>> getChildrenMap() {
		return childrenMap;
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
	protected Image getImage(Object data, String property) {
		return null;
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
	
	/**
	 * ���ڹ�����
	 * 
	 * @return
	 */
	protected PatternFilter getPatternFilter() {
		return new TreePatternFilter();
	}
	
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
	 * �õ��õ�Ԫ��toolTip����,���Է���null
	 * 
	 * @param element
	 * @param property
	 * @return
	 */
	public String getToolTipText(Object element, String property) {
		return null;
	}
	
	public GridTreeViewer getTreeViewer() {
		return viewer;
	}
	
	/**
	 * ���ָ����Ԫ���ֵ������CellEditor
	 * 
	 * @param data ����ģ��
	 * @param property ����
	 * @return
	 */
	public abstract Object getValue(Object data, String property);
	
	public GridTreeViewer getViewer() {
		return viewer;
	}
	
	protected void initComposite(Composite client) {
		filteredTree = new GridFilteredTree(client, getStyle(), getPatternFilter(),this);
		filteredTree.setLayoutData(getCompositeLayoutData());
		viewer = filteredTree.getViewer();
		initViewer(viewer);
		viewer.setContentProvider(new TreeViewerContentProvider());
		viewer.setInput(input);
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

//	public String isRequired(Object element, String property) {
//		return null;
//	}

	/**
	 * ��CellEditor��ֵ���ûص�Ԫ���Ƿ�Ӧ�ñ����ˢ�»��ڻ�������ɣ�������������ж�
	 * 
	 * @param data ����ģ��
	 * @param property ����
	 * @param value ���õ�ֵ
	 */
	public abstract void setValue(Object data, String property, Object value);
	
	protected class DelegateCellLabelProvider extends ColumnLabelProvider {

		protected String property;
		
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
				String errMessage = GridTreeViewerBasicComponent.this.isValid(element, property);
				errMsgMap.put(element, errMessage);
				if (errMessage != null) {
					return colorManager.getColor(HSColorManager.RED);
				}
//				String requiredMessage = GridTreeViewerBasicComponent.this.isRequired(element, property);
//				if(requiredMessage!=null)
//				{
//					errMsgMap.put(element, requiredMessage);
//					return colorManager.getColor(HSColorManager.GREEN);
//				}
			}
			
			return GridTreeViewerBasicComponent.this.getBackground(element, property);
		}
		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getForeground(java.lang.Object)
		 */
		@Override
		public Color getForeground(Object element) {
			if (!isLastLine(element)) {
				String errMessage = GridTreeViewerBasicComponent.this.isValidForeground(element, property);
				errMsgMap.put(element, errMessage);
				if (errMessage != null) {
					return colorManager.getColor(HSColorManager.RED);
				}
//				String requiredMessage = GridTreeViewerBasicComponent.this.isRequired(element, property);
//				if(requiredMessage!=null)
//				{
//					errMsgMap.put(element, requiredMessage);
//					return colorManager.getColor(HSColorManager.GREEN);
//				}
			}
			
			return GridTreeViewerBasicComponent.this.getForeground(element, property);
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getImage(java.lang.Object)
		 */
		@Override
		public Image getImage(Object element) {
			if(GridTreeViewerBasicComponent.this.getCellEditor(element, property) instanceof ISprecialCellEditor){
				ISprecialCellEditor cellEditor = (ISprecialCellEditor)GridTreeViewerBasicComponent.this.getCellEditor(element, property);
				return cellEditor.getImage(GridTreeViewerBasicComponent.this.getValue(element,property));
			}
			return GridTreeViewerBasicComponent.this.getImage(element, property);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object element) {
			if(GridTreeViewerBasicComponent.this.getCellEditor(element, property) instanceof ISprecialCellEditor){
				ISprecialCellEditor cellEditor = (ISprecialCellEditor)GridTreeViewerBasicComponent.this.getCellEditor(element, property);
				return cellEditor.getText(GridTreeViewerBasicComponent.this.getValue(element,property));
			}
			return GridTreeViewerBasicComponent.this.getText(element, property);
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
			if(errMsgMap.get(element) != null && !StringUtil.isEmpty(errMsgMap.get(element))){
				return errMsgMap.get(element);
			} 
			return GridTreeViewerBasicComponent.this.getToolTipText(element,property);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.CellLabelProvider#getToolTipTimeDisplayed(java.lang.Object)
		 */
		@Override
		public int getToolTipTimeDisplayed(Object object) {
			return -1;
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
				String label = GridTreeViewerBasicComponent.this.getText(element, property);
				if (label != null && wordMatches(label)) {
					return true;
				}
			}
			
			return false;
		}
	}
	
	/**
	 * �����ṩ��
	 * <br>ע�� jdk6��jdk5��@override���岻һ��
	 * @author gongyf
	 *
	 */
	public class TreeViewerContentProvider implements  ITreeContentProvider {
		public void dispose() {
			
		}

		
		public Object[] getChildren(Object parentElement) {
			
			List<Object> children = GridTreeViewerBasicComponent.this.getChildren(parentElement);
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

		
		public Object[] getElements(Object inputElement) {
			
			// ��ĩ�м�¼���
			//lastLines.clear();
			
			List<Object> objs = new ArrayList<Object>();
			
			// �ȼ�������
			objs.addAll((List)inputElement);
			
			// ������ĩ��
			if (useAutoGrow && !readOnly && lastLine != null) {
				objs.add(lastLine);
			}

			return objs.toArray();
		}

		
		public Object getParent(Object element) {
			return GridTreeViewerBasicComponent.this.getParent(element);
		}

		
		public boolean hasChildren(Object element) {
			// ���ӱ༭��û�к��ӽڵ�
			if (isLastLine(element)) {
				return false;
			}
			
			return GridTreeViewerBasicComponent.this.getChildren(element) != null;
		}

		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			
		}
		
	}
	
	public void refresh() {
		if(viewer != null && !viewer.getControl().isDisposed()){
			viewer.refresh();
		}
	}

	/**
	 * ȡ�������ǰ��ɫ,��������ĺ�ɫ����������趨
	 * 
	 * @param element
	 * @param property
	 * @return
	 */
	public Color getForeground(Object element, String property) {
		return null;
	}

	/**
	 * @param element
	 * @param property
	 * @return
	 */
	public String isValidForeground(Object element, String property) {
		return null;
	}
}
