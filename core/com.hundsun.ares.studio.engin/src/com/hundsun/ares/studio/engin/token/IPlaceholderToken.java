package com.hundsun.ares.studio.engin.token;

/**
 * ռλtoken
 *
 */
public interface IPlaceholderToken extends ICodeToken{

	/**
	 * ��ȡռλ��key
	 * @return
	 */
	public String getKey();
	
	/**
	 * ��ȡ����
	 * @return
	 */
	public String[] getArgs();
}
