/**
 * Դ�������ƣ�IKeyProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.core.service;

/**
 * @author gongyf
 *
 */
public interface IKeyProvider<K, T> {
	K getKey(T obj);
}
