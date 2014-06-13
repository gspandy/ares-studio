/**
 * Դ�������ƣ�IARESResourceProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ���ݱ༭�����
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.metadata.ui.viewer;

import com.hundsun.ares.studio.core.IARESResource;

/**
 * @author gongyf
 *
 */
public interface IARESResourceProvider {
	
	/**
	 * ��ȡָ���������ڵ�IARESResource����
	 * @param element
	 * @return
	 */
	IARESResource getResource(Object element);
	
	/**
	 * һ����򵥵�ʵ��
	 * @author gongyf
	 *
	 */
	static class Impl implements IARESResourceProvider {
		private IARESResource resource;

	
		/**
		 * @param resource
		 */
		public Impl(IARESResource resource) {
			super();
			this.resource = resource;
		}

		/* (non-Javadoc)
		 * @see com.hundsun.ares.studio.jres.metadata.ui.viewer.IARESResourceProvider#getResource(java.lang.Object)
		 */
		@Override
		public IARESResource getResource(Object element) {
			return resource;
		}
		
	}
}
