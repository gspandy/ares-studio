/**
 * Դ�������ƣ�IServiceExtentionPoint.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.core.service;

import com.hundsun.ares.studio.core.ARESCore;

/**
 * @author gongyf
 *
 */
public interface IServiceExtentionPoint {
	public static final String NAMESPACE = ARESCore.PLUGIN_ID;
	public static final String EP_Name = "services";
	public static final String EP_Attribute_ID = "id";
	public static final String EP_Attribute_Type = "type";
	public static final String EP_Attribute_Factory = "factory";
}
