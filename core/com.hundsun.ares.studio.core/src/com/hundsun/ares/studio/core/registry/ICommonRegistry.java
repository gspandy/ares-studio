/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.registry;

import java.io.PrintStream;
import java.util.Collection;

/**
 * ע���ͳһ�ӿڣ��������ʹ�á�
 * @author sundl
 */
public interface ICommonRegistry<T> {
	String getExtensionPointId();
	void print(PrintStream ps);
	Collection<T> get(String id);
	public Collection<T> getDescriptors();
}
