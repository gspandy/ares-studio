/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

/**
 * ����������
 * @author sundl
 */
public interface IDependenceDescriptor {

	/**
	 * �������ID
	 * @return
	 */
	String getId();
	
	/**
	 * ���ͣ�����Jres,����Acide...
	 * @return
	 */
	String getType();
	
	/**
	 * ������İ汾����
	 * @return
	 */
	String getVersionConstraint();
	
	/**
	 * �жϸ����İ汾�Ƿ�����Լ������
	 * @param version �汾
	 * @return ������㣬�򷵻�<code>true</code>,���򷵻�<code>false</code>
	 */
	boolean isValideVersion(String version);
}
