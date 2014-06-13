package com.hundsun.ares.studio.engin.skeleton;

import java.util.Set;

/**
 * ��ͼռλ������
 * @author lvgao
 *
 */
public interface ISkeletonAttributeHelper {

	/**
	 * ���
	 * @param key
	 * @param value
	 */
	public void addAttribute(String key,String value);
	
	/**
	 * �������
	 * @param key
	 * @param tset
	 */
	public void addAllAttribute(String key,Set<String> tset);
	
	/**
	 * ��ȡ����
	 * @param key
	 * @return
	 */
	public Set<String> getAttribute(String key);
}
