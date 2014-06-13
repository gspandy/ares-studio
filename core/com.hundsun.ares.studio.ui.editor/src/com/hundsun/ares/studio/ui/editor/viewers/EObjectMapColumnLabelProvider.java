/**
 * Դ�������ƣ�EObjectMapColumnLabelProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.viewers;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * @author gongyf
 *
 */
public class EObjectMapColumnLabelProvider extends ColumnLabelProvider {
	private EReference reference;
	private Object key;
	/**
	 * @param reference
	 * @param key
	 */
	public EObjectMapColumnLabelProvider(EReference reference, Object key) {
		super();
		this.reference = reference;
		this.key = key;
	}
	
	/**
	 * ��ȡ��Ҫ������EObject
	 * @param element
	 * @return
	 */
	protected EObject getOwner(Object element) {
		return (EObject) element;
	}
	
	/**
	 * @return the reference
	 */
	protected EReference getReference() {
		return reference;
	}
	
	@Override
	public String getText(Object element) {
		EObject owner = getOwner(element);
		if (!owner.eClass().getEAllReferences().contains(reference)) {
			return "";
		}
		Object value = ((EMap)owner.eGet(reference)).get(key);
		if (value == null) {
			value = "";
		}
		return String.valueOf(value);
	}
}
