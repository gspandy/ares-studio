package com.hundsun.ares.studio.engin.parser;

public interface IMacroElement {

	/**
	 * ��ȡ�ؼ���
	 * @return
	 */
	public String getKeyword();

	/**
	 * ��ȡ��־λ
	 * @return
	 */
	public String getFlag();

	/**
	 * ��ȡ�����
	 * @return
	 */
	public String[] getParameters();
	
	/**
	 * ��ȡ�к�
	 * @return
	 */
	public int getLineNo();
	
	/**
	 *  ��ȡ����
	 * @return
	 */
	public String getAliasName();
}
