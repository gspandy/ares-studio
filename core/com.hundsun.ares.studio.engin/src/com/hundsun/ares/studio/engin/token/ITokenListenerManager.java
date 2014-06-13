package com.hundsun.ares.studio.engin.token;

public interface ITokenListenerManager {

	/**
	 * ��Ӽ���
	 * @param listener
	 */
	public void addListener(ITokenListener listener);
	
	
	/**
	 * ɾ������
	 * @param listener
	 */
	public void removeListener(ITokenListener listener);
	
	
	/**
	 * �����¼�
	 * @param event
	 */
	public void fireEvent(ITokenEvent event);
}
