/**
 * Դ�������ƣ�MetadataViewerUtil.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.viewer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;

/**
 * @author gongyf
 *
 */
public class MetadataViewerUtil {
	
	/**
	 * Ԫ������ͼ�еı���Ƿ�ʹ�÷�����ʾ
	 * @param viewer
	 * @return
	 */
	static public boolean isShowCategory(ColumnViewer viewer) {
		IContentProvider provider = viewer.getContentProvider();
		if (provider instanceof MetadataTreeContentProvider ) {
			return ((MetadataTreeContentProvider) provider).isShowCategory();
		}
		
		return false;
	}
	
	/**
	 * ��ȡδ����ķ���ģ��
	 * @param viewer
	 * @return
	 */
	static public MetadataCategory getUncategorizedCategory(ColumnViewer viewer) {
		IContentProvider provider = viewer.getContentProvider();
		if (provider instanceof MetadataTreeContentProvider ) {
			return ((MetadataTreeContentProvider) provider).getUncategorizedCategory();
		}
		
		return null;
	}
	
	/**
	 * Ԫ���ݱ���е�ǰѡ�еķ��飬�����Ƿ��鱻ֱ��ѡ���ˣ�Ҳ�����Ƿ����µ���Ŀ��ѡ����<BR>
	 * ����ѡ�е�����£�ֻ����һ����ѡ�е���
	 * @param viewer
	 * @param excludeSelf ��ѡ��һ������ʱ���Ƿ��ų��Լ�������������������ڵķ���
	 * @return
	 */
	static public MetadataCategory getSelectedCategory(ColumnViewer viewer, boolean excludeSelf) {
		ITreeSelection selection = (ITreeSelection) viewer.getSelection();
		if (selection != null && !selection.isEmpty()) {
			TreePath path = selection.getPaths()[0];
			if (!excludeSelf && path.getLastSegment() instanceof MetadataCategory) {
				return (MetadataCategory) path.getLastSegment();
			} else {
				if (path.getSegmentCount() >= 2) {
					for(int i = path.getSegmentCount()-1; i >0 ; i--){
						Object obj = path.getSegment(i-1);
						if(obj instanceof MetadataCategory){
							return (MetadataCategory)obj;
						}
					}
				}
			}

		}
		if (viewer.getInput() == null) {
			return null;
		}
		
		return ((MetadataResourceData<?>) viewer.getInput()).getRoot();
	}
	
//	static public EClass getEClassForFirstSelectedObject(ColumnViewer viewer) {
//		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
//		if (selection != null && !selection.isEmpty()) {
//			return ((EObject)selection.getFirstElement()).eClass();
//		}
//		return null;
//	}
	
	static public MetadataResourceData<?> getMetadataModel(ColumnViewer viewer) {
		return (MetadataResourceData<?>) viewer.getInput();
	}
	
	
	static int getItemIndex(Object parent, TreeItem item) {
		if (parent instanceof Tree) {
			return ((Tree) parent).indexOf(item);
		} else {
			return ((TreeItem) parent).indexOf(item);
		}
	}
	
	static int getItemCount(Object parent) {
		if (parent instanceof Tree) {
			return ((Tree) parent).getItemCount();
		} else {
			return ((TreeItem) parent).getItemCount();
		}
	}
	
	static TreeItem getItem(Object parent, int index) {
		if (parent instanceof Tree) {
			return ((Tree) parent).getItem(index);
		} else {
			return ((TreeItem) parent).getItem(index);
		}
	}
	
	static Object getParentItem(TreeItem item) {
		Object parentItem = item.getParentItem();
		if (parentItem == null) {
			parentItem = item.getParent();
		}
		return parentItem;
	}
	
	static public EObject[] getVisualItems(final ColumnViewer viewer) {
		final Object[] result = new Object[1];
		Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				try {
					if (viewer instanceof TreeViewer) {
						List<TreeItem> itemList = new ArrayList<TreeItem>();
						Tree tree = ((TreeViewer) viewer).getTree();
						TreeItem topItem = tree.getTopItem();
						// ��������ʾ����Ŀ����
						int itemCount = (int) ((tree.getClientArea().height / tree.getItemHeight()) * 1.2);
						
						int curIndex = 0;
						Object parentItem = getParentItem(topItem);
						
						curIndex = getItemIndex(parentItem, topItem);
						
						while (itemCount > 0) {
							if (curIndex >= getItemCount(parentItem)) {
								if (parentItem instanceof TreeItem) {
									// �������������,�򷵻���һ��
									curIndex = getItemIndex( getParentItem((TreeItem) parentItem), (TreeItem) parentItem) + 1;
									parentItem = getParentItem((TreeItem) parentItem);
								} else {
									break;
								}

							} else {
								TreeItem curItem =  getItem(parentItem, curIndex);
								itemList.add(curItem);
								itemCount--;
								if (curItem.getExpanded()) {
									parentItem = curItem;
									curIndex = 0;
								} else {
									curIndex++;
								}
							}
						}
						
						EObject[] objects = new EObject[itemList.size()];
						
						
						for (int i = 0; i < objects.length; i++) {
							objects[i] = (EObject) itemList.get(i).getData();
						}
						
						result[0] = objects;
					}
				} catch (Exception e) {
					result[0] = new EObject[0];
				}
				
			}
		});
		
		return (EObject[]) result[0];
	}
}
