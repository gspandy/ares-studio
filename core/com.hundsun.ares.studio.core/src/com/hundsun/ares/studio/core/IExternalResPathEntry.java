/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import com.hundsun.ares.studio.core.registry.IRespathProviderDescriptor;

/**
 * �ⲿ�ṩ��RespathEntry������Maven�ṩ�ģ�����ӿ������������������Դ�������ǹ��ߵ�.respath�ļ�����
 * ���������ͨ��IRespathProvider��չ���ṩ�ġ�
 * @author sundl
 */
public interface IExternalResPathEntry extends IResPathEntry{
	/**
	 * �������RespathEntry����Դ
	 * @return
	 */
	IRespathProviderDescriptor getProvider();
	
	public void setProvider(IRespathProviderDescriptor provider);
}
