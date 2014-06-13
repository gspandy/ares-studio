/**
 * Դ�������ƣ�IExtensibleModelPropertyDescriptor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.extend;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;

/**
 * @author gongyf
 * @author sundl
 *
 */
public interface IExtensibleModelPropertyDescriptor {
	
	/**
	 * <ul>
	 * <li>������Ϣ���ַ�������ʾ�ã� </li>
	 * <li>�������ͬһ�������µ�������ʾ��ʱ�����ʾ��ͬһ��������</li>
	 * <li>������ﲻ�ṩ������Ϣ(�մ�����null)���ͻᰴ���ṩ���Descriptor��EditingSupport���з�����ʾ</li>
	 * </ul>
	 * @return
	 */
	String getCategory();
	
	/**
	 * ��ʾ�����ƣ�������������������ͷ
	 * @return
	 */
	String getDisplayName();
	
	/**
	 * ������Ϣ
	 * @return
	 */
	String getDescription();
	
	/**
	 * �༭������
	 * @return
	 */
	EStructuralFeature getStructuralFeature();
	
	/**
	 * ������ʾ����
	 * @return
	 */
	ILabelProvider getLabelProvider();
	
	/**
	 * ���ڱ༭���ݣ����Է���null����ʾ���ɱ༭
	 * @param parent
	 * @return
	 */
	CellEditor createPropertyEditor(Composite parent);
	
	/**
	 * �Ƿ����������ԣ��������Բ���һ����Ӧһ�������е�����
	 * @return
	 */
	boolean isDerived();
}
