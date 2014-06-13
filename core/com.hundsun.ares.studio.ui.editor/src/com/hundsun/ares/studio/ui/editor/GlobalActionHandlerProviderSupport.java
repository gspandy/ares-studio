/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.ui.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author gongyf
 *
 */
public class GlobalActionHandlerProviderSupport {
	
	private List<IGlobalActionHandlerProviderListener> listeners = new ArrayList<IGlobalActionHandlerProviderListener>();
	private IGlobalActionHandlerProvider provider;
	
	/**
	 * @param provider
	 */
	public GlobalActionHandlerProviderSupport(
			IGlobalActionHandlerProvider provider) {
		super();
		this.provider = provider;
	}

	public synchronized void addListener(IGlobalActionHandlerProviderListener listener) {
		listeners.add(listener);
	}
	
	public synchronized void removeListener(IGlobalActionHandlerProviderListener listener){
		listeners.remove(listener);
	}
	
	/**
	 * ֪ͨ���м�����
	 */
	public void fireProviderActived() {
		for (Iterator<IGlobalActionHandlerProviderListener> iterator = listeners.iterator(); iterator.hasNext();) {
			IGlobalActionHandlerProviderListener listener = iterator.next();
			listener.activated(provider);
		}
	}
	
	/**
	 * ֪ͨ���м�����
	 */
	public void fireProviderDeactived() {
		for (Iterator<IGlobalActionHandlerProviderListener> iterator = listeners.iterator(); iterator.hasNext();) {
			IGlobalActionHandlerProviderListener listener = iterator.next();
			listener.deactivated(provider);
		}
	}
}
