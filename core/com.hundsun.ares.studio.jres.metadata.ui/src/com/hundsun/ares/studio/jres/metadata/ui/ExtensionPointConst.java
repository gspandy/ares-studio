/**
 * Դ�������ƣ�ExtensionPointConst.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui;

/**
 * ��չ����صĳ�������
 * @author gongyf
 *
 */
public interface ExtensionPointConst {
	/**
	 * ������չ��
	 * @author gongyf
	 *
	 */
	public interface Languages {
		
		/**
		 * ��չ����
		 */
		String NAME = MetadataUI.PLUGIN_ID + ".languages";
		
		/**
		 * ��չ��Ԫ��
		 * @author gongyf
		 *
		 */
		public interface Language {
			String NAME = "language";
			String ATTR_ID = "id";
			String ATTR_NAME = "name";
		}
	}
	
}
