/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.model;

/**
 * ͳһģ�ͽӿ�
 * @author sundl
 */
public interface ICommonModel {
	
	/** Ӣ����������ID	 */
	String NAME = "name";
	/** ������������ID	 */
	String CNAME= "cname";
	/** ����š�*/
	String ID = "id";
	
	/**
	 * ��ȡָ��������ֵ�����û��ָ�������ԣ����Է���null
	 * @param key ����key
	 * @return ֵ
	 */
	Object getValue(String key);
	
	/**
	 * ����ָ�������Ե�ֵ
	 * @param key
	 * @param value
	 */
	void setValue(String key, Object value);
	
	String getString(String key);
}
