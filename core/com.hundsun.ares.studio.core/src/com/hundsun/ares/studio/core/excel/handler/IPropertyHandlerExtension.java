/**
 * Դ�������ƣ�IPropertyHandlerExtension.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.excel.SheetParser;

/**
 * @author sundl
 *
 */
public interface IPropertyHandlerExtension {

	void init(SheetParser sheetParser, IARESProject project);
	
}
