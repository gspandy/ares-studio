/**
 * Դ�������ƣ�IPropertyHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

import com.hundsun.ares.studio.core.IARESProject;

/**
 * ��ȡIPropertyHandler����ڡ� 
 * ��Ҫע��һ�㣺��Щhandler�Ǻ�project��صģ����ԣ���Ҫ�ѻ�ȡ��handler�ڲ�ͬ��Ŀ֮�䴫�ݣ�
 * ���ڲ�ͬ��project��ʱ����Ҫ���µ��ýӿڷ�����ȡhandler
 * @author sundl
 *
 */
public interface IPropertyHandlerFactory {

	IPropertyHandler getPropertyHandler(String key, IARESProject project);
	
}
