/**
 * Դ�������ƣ�DisplayItem.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.ui.editor.blocks;

import org.eclipse.emf.ecore.EObject;

/**
 * ��ʾ�õĶ�����ĳЩ����£���ϣ��ֱ����ʾ��Ӧ��EObject���Ӷ���Ҫ������װһ�£���������ʾ�����ԭ�����󣬴Ӷ�����Ҫ��ʱ�򣬽��в�ͬ�Ĵ���
 * ����������������Block����ʾ�������͵����Բ�����ʱ����Щ�ֽڵ�ԭ�����Ͷ���һ����Parameter�࣬���ǣ����ǵ������Ƿ���Ա༭������ɫ�ȷ��������
 * ��Parameter����ͬ����ʱ��������������а�װ��
 * 
 * @author sundl
 * @see ParameterConentProvider
 */
public class DisplayItem {
	
	public EObject eObject;

	public DisplayItem(EObject eObject) {
		this.eObject = eObject;
	}
	
}
