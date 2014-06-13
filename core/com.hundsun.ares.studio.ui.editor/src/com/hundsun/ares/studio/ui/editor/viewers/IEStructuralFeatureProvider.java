/**
 * Դ�������ƣ�IEStructuralFeatureProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.viewers;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author gongyf
 *
 */
public interface IEStructuralFeatureProvider {
	/**
	 * ���ݲ�ͬ�Ķ��󷵻ز�ͬ������
	 * @param element
	 * @return
	 */
	EStructuralFeature getFeature(Object element);
	
	/**
	 * һ����򵥵�ʵ��
	 * @author gongyf
	 *
	 */
	static class Impl implements IEStructuralFeatureProvider {
		private EStructuralFeature attribute;

		/**
		 * @param attribute
		 */
		public Impl(EStructuralFeature attribute) {
			super();
			this.attribute = attribute;
		}

		/* (non-Javadoc)
		 * @see com.hundsun.ares.studio.jres.ui.viewers.IEAttributeProvider#getAttribute(java.lang.Object)
		 */
		@Override
		public EStructuralFeature getFeature(Object element) {
			return attribute;
		}
		
	}
}
