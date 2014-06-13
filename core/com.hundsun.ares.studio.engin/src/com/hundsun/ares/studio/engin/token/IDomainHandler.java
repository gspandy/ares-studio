package com.hundsun.ares.studio.engin.token;

public interface IDomainHandler {

	/**
	 * ��ȡ����������ڴ��򷵻�null
	 * @param key
	 * @return
	 */
	public ITokenDomain getDomain(String key);
	
	/**
	 * �����
	 * @param domain
	 */
	public void addDomain(ITokenDomain domain);
	
	/**
	 * ��ȡ�����
	 * @param key
	 * @return
	 */
	public Object[] getDomainArgs(String key);
	
	
	/**
	 * ɾ����
	 * @param key
	 */
	public void removeDomain(String key);
	
	/**
	 * ��ȡ��
	 * @return
	 */
	public ITokenDomain[] getDomains();
}
