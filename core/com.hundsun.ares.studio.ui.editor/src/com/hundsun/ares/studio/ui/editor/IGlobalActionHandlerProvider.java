/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.ui.editor;

import org.eclipse.ui.IActionBars;

/**
 * @author gongyf
 *
 */
public interface IGlobalActionHandlerProvider  {
	
	void addGlobalActionHandlerProviderListener(IGlobalActionHandlerProviderListener listener);
	
	
	void removeGlobalActionHandlerProviderListener(IGlobalActionHandlerProviderListener listener);
	
	/**
	 * ����ȫ�ֿ�ݼ�
	 * @param actionBars
	 */
	void shareGlobalActions(IActionBars actionBars);
	
	/**
	 * ȡ��ȫ�ֿ�ݼ�
	 * @param actionBars
	 */
	void clearGlobalActions(IActionBars actionBars);
}
