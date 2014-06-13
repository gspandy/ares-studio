/**
 * Դ�������ƣ�CopyAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.actions;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ColumnViewer;

import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerCopyAction;

/**
 * ����
 * @author gongyf
 *
 */
public class CopyAction extends ColumnViewerCopyAction {

	
	/*
	 * DESIGN#����ճ��#��Ҷ��#����#�����#ʵ�ֱ༭���ĸ���ճ��
	 *
	 * ���и���ճ�������ColumnVewerͨ�õ�
	 * ��Ҫ��2��Transfer��һ�����ڲ��ģ�����ֱ�ӷ����ڴ����Ҳ����EObject����EObject����Ӧ���Ǹ���
	 * Ҫ��ֹ������һ������û��ճ��������£��������������޸ģ�������ճ����Ķ����Ǹ���ʱ��״̬
	 * Ҳ���Ǹ����ǶԶ������һ������
	 * 
	 * ���û����ⲿճ����ʱ��Ӧ�ÿ���ճ�����������һ�У�ÿ����tab�ָ����ַ���
	 * ��Ҫʹ��TextTransfer
	 * 
	 */
	

	/**
	 * @param viewer
	 */
	public CopyAction(ColumnViewer viewer) {
		super(viewer);
	}

	
	// ����ͬʱ���Ʒ����Item
	protected boolean calculateEnabled() {
		List<EObject> objects = getSelectedObjects();
		return !objects.isEmpty() && isSameConainer(objects);
	}
	
	/**
	 * �ж��Ƿ���һ��������һ��������
	 * @param eObjs
	 * @return
	 */
	boolean isSameConainer(List<EObject> eObjs) {
		EObject contaier = eObjs.get(0).eContainer();
		EReference reference = eObjs.get(0).eContainmentFeature();
		
		for (EObject eObject : eObjs) {
			if (eObject.eContainer() != contaier || eObject.eContainmentFeature() != reference) {
				return false;
			}
		}
		
		return true;
	}
	
}
