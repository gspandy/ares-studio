package com.hundsun.ares.studio.engin.macrohandler;

import java.util.Set;

public interface IMacroTokenHandlerFactory {

	/**
	 * �Ƿ��ܴ���˺�
	 * @param key
	 * @return
	 */
	public boolean canHandle(String key);
	
	/**
	 * ������ڵ㴦����
	 * @param key
	 * @return
	 */
	public IMacroTokenHandler create(String key);
	
	/**
	 * ��ȡ����Ҫ����ĺ�
	 * @return
	 */
	public Set<IMacroTokenHandler> getHandledMacros();
}
