/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.ui.editor;


/**
 * ȫ�ֿ�ݼ��ṩ����״̬�仯������
 * @author gongyf
 *
 */
public interface IGlobalActionHandlerProviderListener {

	/**
	 * �ṩ���Ѽ���
	 * @param provider
	 */
	void activated(IGlobalActionHandlerProvider provider);

	/**
	 * �ṩ����
	 * @param provider
	 */
	void deactivated(IGlobalActionHandlerProvider provider);

}
