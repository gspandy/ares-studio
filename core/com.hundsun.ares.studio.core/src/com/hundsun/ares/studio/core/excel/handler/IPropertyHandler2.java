/**
 * Դ�������ƣ�IPropertyHandler2.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

import com.hundsun.ares.studio.core.IARESProject;

/**
 * PropertyHandler����ǿ�ӿڣ�ĳЩʱ��get, set����ҪIARESProject����Ϣ,����ʵ������ӿڿ����ɿ��
 * ��IARESProject����ע���ȥ��
 * @author sundl
 */
public interface IPropertyHandler2 extends IPropertyHandler{
	
	void setProject(IARESProject project);

}
