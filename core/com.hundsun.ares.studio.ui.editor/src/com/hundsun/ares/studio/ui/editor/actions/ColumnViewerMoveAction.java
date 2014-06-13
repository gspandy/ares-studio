/**
 * Դ�������ƣ�ColumnViewerMoveAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//import org.apache.commons.lang.ArrayUtils;
//import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ColumnViewer;

/**
 * @author gongyf
 *
 */
public abstract class ColumnViewerMoveAction extends ColumnViewerAction {

	private boolean moveUp;
	
	/**
	 * @param viewer
	 * @param editingDomain
	 */
	protected ColumnViewerMoveAction(ColumnViewer viewer,
			EditingDomain editingDomain, boolean moveUp) {
		super(viewer, editingDomain);
		this.moveUp = moveUp;
	}

	/**
	 * @return the owner
	 */
	protected abstract EObject getOwner();
	
	/**
	 * @return the reference
	 */
	protected abstract EReference getReference();
	
	/**
	 * 
	 * @param objects
	 * @return
	 */
	protected boolean isCommonContainer(EObject[] objects) {
//		if (objects.length >= 1) {
//			EObject container = objects[0].eContainer();
//			EReference reference = objects[0].eContainmentFeature();
//			for (int i = 1; i < objects.length; i++) {
//				if (container != objects[i].eContainer() || reference != objects[i].eContainingFeature()) {
//					return false;
//				}
//			}
//			return true;
//		}
		Collection collection = (Collection) getOwner().eGet(getReference());
		for (EObject eObject : objects) {
			if (!collection.contains(eObject)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected Command createCommand() {
		if (getOwner() == null || getReference() == null) {
			return null;
		}
		List<Object> selectedObjects = getSelectedObjects();
		if (selectedObjects.isEmpty()) {
			return null;
		}
		
//		EObject owner = objects[0].eContainer();
//		EReference reference = objects[0].eContainmentFeature();
		
		final EList<Object> list = (EList<Object>) getOwner().eGet(getReference());
		
		// ģ��������б�
		List<Object> tmpList = new ArrayList<Object>(list);
		
		
//		sort(tmpList,selectedObjects);
		// ǿת��ǰ���ж��Ƿ�EObject����
		for (Object obj : selectedObjects) {
			if (!(obj instanceof EObject))
				return null;
		}
		EObject[] objects = selectedObjects.toArray(new EObject[selectedObjects.size()]);
		
		if (!isCommonContainer(objects)) {
			return null;
		}
		
		boolean needChanged = false;
		if (moveUp) {
			if(0 == tmpList.indexOf(objects[0])){  //��һ��ѡ�ж������ǵ�һ��
				return null;
			}
			
			for (int i = 0; i < objects.length; i++) {
				int index = tmpList.indexOf(objects[i]);
				if((index - 1)>=0){
					Collections.swap(tmpList, index, index - 1);
					needChanged = true;
				}
				
			}
		} else {
			if((tmpList.size() - 1) == tmpList.indexOf(objects[objects.length - 1])){ //���һ��ѡ�ж����������һ��
				return null;
			}
			int i = objects.length - 1;
			for (; i > -1 ; i--) {
				int index = tmpList.indexOf(objects[i]);
				if(index + 1< tmpList.size()){
					Collections.swap(tmpList, index, index + 1);
					needChanged = true;
				}
				
			}
		}

		
		// ���˳���ޱ仯����Ҫִ������
		if (!needChanged) {
			return null;
		}
		CompoundCommand command = new CompoundCommand(getText());
		if (moveUp) {  //���ƺ����ƴ���һ��,����ʱҪ���ƶ��������Ǹ�������ʱҪ���ƶ��������
			for (Object object : selectedObjects) {
				command.append(MoveCommand.create(getEditingDomain(), getOwner(), getReference(), object, tmpList.indexOf(object)));
			}
		}else{
			for (int i = (selectedObjects.size() -1); i > -1 ; i--) {
				Object object = selectedObjects.get(i);
				command.append(MoveCommand.create(getEditingDomain(), getOwner(), getReference(), object, tmpList.indexOf(object)));
			}
		}
		
		return command.unwrap();
	}
	
	
//	/**
//	 * ����
//	 * @param objList
//	 * @param selectedObjects
//	 */
//	private void sort(final List<Object> objList,List<Object> selectedObjects){
//		Collections.sort(selectedObjects, new Comparator<Object>() {
//			@Override
//			public int compare(Object arg0, Object arg1) {
//				return objList.indexOf(arg0) - objList.indexOf(arg1);
//			}
//		});
//	}

}
