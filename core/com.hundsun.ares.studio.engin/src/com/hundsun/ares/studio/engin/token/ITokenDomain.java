package com.hundsun.ares.studio.engin.token;

/**
 * token��Ӧ����
 * @author Administrator
 *
 */
public interface ITokenDomain {
	/**ȫ��*/
	public static final String GLOABL = "gloabl";
	
	/**����һ�������*/
	public static final String END_BY_OTHER = "end_by_other";

	/**
	 * ��ȡ������
	 * @return
	 */
	public String getType();
	
	/**
	 * ��ȡռλ��key
	 * @return
	 */
	public String getKey();
	
	/**
	 * ��ȡ����
	 * @return
	 */
	public Object[] getArgs();
}
