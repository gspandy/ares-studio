/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * ʹ��֮ǰ�����ȴ򿪵�Ԫ�ص�ͳһ�ӿڡ�
 * @author sundl
 */
public interface IOpenable {

	/**
	 * �Ƿ��Ѿ��򿪡�
	 * @return ����Ѿ��򿪣��򷵻�<code>true</code>�����򷵻�<code>false</code>
	 */
	boolean isOpen();
	
	void open(IProgressMonitor monitor) throws ARESModelException;
	
	void close() throws ARESModelException;
	
}
