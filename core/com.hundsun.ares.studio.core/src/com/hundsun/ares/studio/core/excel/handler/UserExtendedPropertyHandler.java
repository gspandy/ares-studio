/**
 * Դ�������ƣ�UserExtendedPropertyHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

import com.hundsun.ares.studio.core.excel.SheetParser;
import com.hundsun.ares.studio.core.model.extend.IBasicExtendPropertyDescriptor;


/**
 * @author sundl
 *
 */
public class UserExtendedPropertyHandler extends ExtendedPropertyHandler {

	private SheetParser sheetParser;
	
	public UserExtendedPropertyHandler(SheetParser sheetParser, IBasicExtendPropertyDescriptor descriptor) {
		super(descriptor);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.IPropertyHandler#setValue(java.lang.Object, java.lang.String)
	 */
	@Override
	public void setValue(Object obj, String value) {
		//sheetParser.exlParser.stdFieldModifyCommands;
	}

}
