/**
 * Դ�������ƣ�MetadataDropAdapter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.editors.dnd;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.TreeItem;

import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataViewerUtil;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.UncategorizedItemsCategoryImpl;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;

/**
 * ��קAdapter
 * @author sundl
 */
public class MetadataDropAdapter extends ViewerDropAdapter{

	private EditingDomain domain;
	private EObject dropTargetContainer;
	
	public MetadataDropAdapter(TreeViewer viewer, EditingDomain domain) {
		super(viewer);
		this.domain = domain;
	}
	
    // ����ֻ��ͨ��UI����ȡ����ǰ��
	protected Object determineTarget(DropTargetEvent event) {
    	dropTargetContainer = determainDropTargetContainer(event);
    	return super.determineTarget(event);
    }

    private EObject determainDropTargetContainer(DropTargetEvent event) {
		if (MetadataViewerUtil.isShowCategory(getColumnViewer())) {
			if (!(event.item instanceof TreeItem)) {
				return null;
			}

			TreeItem item = (TreeItem) event.item;
			if (item.getData() instanceof MetadataItem) {
				TreeItem parentItem = item.getParentItem();
				if (parentItem == null)
					return null;

				Object data = parentItem.getData();
				if (data instanceof MetadataCategory)
					return (MetadataCategory) data;
			} else if (item.getData() instanceof MetadataCategory) {
				// �������ִ�е�ʱ�򣬻�û��ִ��super.determineLocation()���Բ���getCurrentLocation
				MetadataCategory cate = (MetadataCategory) item.getData();
				int location = determineLocation(event);
				if (location == LOCATION_ON)
					return cate;
				else if (location == LOCATION_AFTER || location == LOCATION_BEFORE) {
					return cate.eContainer();
				}
			}
		} else {
			return MetadataViewerUtil.getMetadataModel(getColumnViewer());
		}
		
		return null;
    }
    
	@Override
	public boolean performDrop(Object data) {
		int operation = getCurrentOperation();
		Collection<?> elements = extractDragSource(data);
		Object target = getCurrentTarget();
		int location = getCurrentLocation();
		
		Object[] elementArray = elements.toArray();
		EStructuralFeature feature = getFeature(elementArray[0]);
		EList<?> list = (EList<?>) dropTargetContainer.eGet(getFeature(elementArray[0]));
		int index = list.indexOf(target);
		if (target.equals(dropTargetContainer)) {
			index = 0;
		} else {
			if (location == LOCATION_AFTER)
				index++;
		}
		
		Command command = null;
		
		CompoundCommand cmd = new CompoundCommand();
		if (dropTargetContainer == getOwner()) {
			// ͬһ��container�µ��϶������ƶ�˳��
			for (Object element : elements) {
				int curIndex = list.indexOf(element);
				if(curIndex<index){
					//�����϶�
					cmd.append(MoveCommand.create(domain, dropTargetContainer, feature, element, index-1));
				}else{
					cmd.append(MoveCommand.create(domain, dropTargetContainer, feature, element, index));
					index++;
				}
			}
		} else {
			//�������ʾɾ�����࣬������������ཫ������ by wangxh 2013.6.13
			if(feature == MetadataPackage.Literals.METADATA_CATEGORY__CHILDREN){
				if(elementArray[0] instanceof MetadataCategory){
					cmd.append(RemoveCommand.create(domain, ((MetadataCategory)elementArray[0]).getParent(), feature, elements));
				}
			}
			// ��ͬ��container�µ��϶������ƶ�����
			cmd.append(AddCommand.create(domain, dropTargetContainer, feature, elements, index));
			// �����ӷ�����containment���ԣ����Բ���Ҫ����ɾ��������Add��ʱ�򣬾��Ѿ���ԭ����ɾ���ˣ�
			// �������Item�Ĺ�ϵ�ǲ�ͬ�ģ���Ҫ����ɾ��
			if (operation == DND.DROP_MOVE && feature == MetadataPackage.Literals.METADATA_CATEGORY__ITEMS)
				cmd.append(RemoveCommand.create(domain, getOwner(), feature, elements));
		}

		command = cmd;
		
//		if (target instanceof MetadataCategory) {
//			command = createCommand(operation, elements, (MetadataCategory)target, -1);
//		} else if (target instanceof MetadataItem) {
//			if (dropTargetContainer != null) {
//				int index = dropTargetContainer.getItems().indexOf(target);
//				int location = getCurrentLocation();
//				if (location == LOCATION_AFTER) {
//					index++;
//				}
//				command = createCommand(operation, elements, dropTargetContainer, index);
//			}
//		}
		
		if (command != null)
			domain.getCommandStack().execute(command);
		
		return true;
	}
	
		
	private Command createCommand(int operation, Collection<?> elements, MetadataCategory target, int index) {
		if (operation == DND.DROP_MOVE) {
			return createMoveToCategoryCommand(elements, target, index, false);
		} else if (operation == DND.DROP_COPY) {
			return createMoveToCategoryCommand(elements, target, index, true);
		}
		return null;
	}
	
	protected Command createMoveToCategoryCommand(Collection<?> items, MetadataCategory target, int index, boolean isCopy) {
		CompoundCommand command = new CompoundCommand();
		MetadataCategory source = MetadataViewerUtil.getSelectedCategory((ColumnViewer) getViewer(), true);
		
		if (source.equals(target)) {
			for (Object obj : items) {
				command.append(MoveCommand.create(domain, target, MetadataPackage.Literals.METADATA_CATEGORY__ITEMS, obj, index));
			}
		}
		else {
			command.append(AddCommand.create(domain, target, MetadataPackage.Literals.METADATA_CATEGORY__ITEMS, items, index));
			if (!isCopy)
				command.append(RemoveCommand.create(domain, source, MetadataPackage.Literals.METADATA_CATEGORY__ITEMS, items));
		}
			
		return command;
	}
	
	@Override
	public boolean validateDrop(Object target, int operation, TransferData transferType) {
		Collection<?> source = getDragSource(getCurrentEvent());
		if (source == null || source.isEmpty())
			return false;
		
		if (dropTargetContainer == null)
			return false;
		
		// �����϶���δ����
		if (dropTargetContainer instanceof UncategorizedItemsCategoryImpl)
			return false;
		
		
		Object[] elements = source.toArray();
		// 1. ����ֻ��Item����ֻ�з��飻 ������ͬһ�㣬ͬһ����������
		if (!allUnderSameContainer(elements)) 
			return false;
		
		//Ԫ������Ŀ�����϶���root���� by wangxh 2013.6.13
		EObject parent = dropTargetContainer.eContainer();
		if(parent != null && parent instanceof MetadataResourceData && elements[0] instanceof MetadataItem){
			return false;
		}
		
		// 2. target�Ƿ���
		int location = getCurrentLocation();
		if (target instanceof MetadataCategory) {
			MetadataCategory cate = (MetadataCategory) target;
			// 2.1 �����϶���δ����
			if (cate instanceof UncategorizedItemsCategoryImpl) {
				if (location == LOCATION_BEFORE)
					return true;
				else 
					return false;
			}
			
			//��������϶������������ط� by wangxh 2013.6.13
			if(dropTargetContainer instanceof MetadataCategory){
				return true;
			}
			
			// 2.2 �����϶�����ǰ����
			MetadataCategory currentCate = MetadataViewerUtil.getSelectedCategory((ColumnViewer) getViewer(), true);
			if (cate.equals(currentCate)) {
				if (location == LOCATION_BEFORE || location == LOCATION_AFTER)
					return true;
				else 
					return false;
			}
			
			// 2.3 �϶������飬ֻ����LOCATION_ON�����
			if (location != LOCATION_ON) {
				return false;
			}
			
			return true;
		} 
		// 3. target ��Item
		else if (target instanceof MetadataItem) {
			if (getFeature(elements[0]) == MetadataPackage.Literals.METADATA_CATEGORY__CHILDREN) {
				// ����������϶����ൽ����һ�������£���Ϊһ���ӷ��࣬��������������϶���Item����
				if (location == LOCATION_BEFORE)
					return true;
			}
			if (location == LOCATION_BEFORE || location == LOCATION_AFTER)
				return true;
			return false;
		}
		
		return false;
	}
	
	/**
	 * ָ����Ԫ���Ƿ��Ѿ�������Ŀ���������
	 * @param elements
	 * @return
	 */
	private boolean isAnyElementAlreadyInTargetCategory(Object[] elements) {
		EStructuralFeature feature = getFeature(elements[0]);
		@SuppressWarnings("rawtypes")
		EList list = (EList) dropTargetContainer.eGet(feature);
		for (Object element : elements) {
			if (!list.contains(element))
				return true;
		}
		return false;
	}
	
	/**
	 * ָ����Ԫ���Ƿ��ڵ�ǰ��������
	 * ���Ԫ��������Item��Ҳ��Category��Ҳ����false
	 * @param elements
	 * @return
	 */
	private boolean allUnderSameContainer(Object[] elements) {
		if (elements == null || elements.length == 0)
			return false;
		
		if (elements.length == 1)
			return true;
		
		EStructuralFeature feature = getFeature(elements[0]);
		EObject owner = getOwner();
		
		@SuppressWarnings("rawtypes")
		EList list = (EList) owner.eGet(feature);
		for (Object element : elements) {
			if (!list.contains(element))
				return false;
		}
		return true;
	}
	
	protected EObject getOwner() {
		if (MetadataViewerUtil.isShowCategory((ColumnViewer) getViewer())) {
			MetadataCategory category = MetadataViewerUtil.getSelectedCategory(getColumnViewer(), true);
//			if (category instanceof UncategorizedItemsCategoryImpl) {
//				return null;
//			}
			return category;
		} else {
			return MetadataViewerUtil.getMetadataModel(getColumnViewer());
		}
	}
	
	private EStructuralFeature getFeature(Object element) {
		if (MetadataViewerUtil.isShowCategory((ColumnViewer) getViewer())) {
			if (element instanceof MetadataCategory) {
				return MetadataPackage.Literals.METADATA_CATEGORY__CHILDREN;
			} else if (element instanceof MetadataItem) {
				return MetadataPackage.Literals.METADATA_CATEGORY__ITEMS;
			}
		} else if (element instanceof MetadataItem) {
			return MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS;
		}
		
		// should not happen
		return null;
	}
	
	private ColumnViewer getColumnViewer(){
		return (ColumnViewer) getViewer();
	}
	
	protected Collection<?> getDragSource(DropTargetEvent event) {
		// Check whether the current data type can be transfered locally.
		//
		LocalTransfer localTransfer = LocalTransfer.getInstance();
		if (!localTransfer.isSupportedType(event.currentDataType)) {
			// Iterate over the data types to see if there is a data type that
			// supports a local transfer.
			//
			TransferData[] dataTypes = event.dataTypes;
			for (int i = 0; i < dataTypes.length; ++i) {
				TransferData transferData = dataTypes[i];

				// If the local transfer supports this data type, switch to that
				// data type
				//
				if (localTransfer.isSupportedType(transferData)) {
					event.currentDataType = transferData;
				}
			}

			return null;
		} else {
			// Transfer the data and, if non-null, extract it.
			//
			Object object = localTransfer.nativeToJava(event.currentDataType);
			return object == null ? null : extractDragSource(object);
		}
	}

	/**
	 * This extracts a collection of dragged source objects from the given
	 * object retrieved from the transfer agent. This default implementation
	 * converts a structured selection into a collection of elements.
	 */
	protected Collection<?> extractDragSource(Object object) {
		// Transfer the data and convert the structured selection to a
		// collection of objects.
		//
		if (object instanceof IStructuredSelection) {
			List<?> list = ((IStructuredSelection) object).toList();
			return list;
		} else {
			return Collections.EMPTY_LIST;
		}
	}
}
