/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.core;


/**
 * ��һ��ARES��Դ���һ������<BR>
 * ��Ҫע��� {@link #equals(Object)} ��{@link #hashCode()}������ʵ��
 * @author gongyf
 *
 */
public interface IObjectProvider {
	Object getObject(IARESResource resource);
}
