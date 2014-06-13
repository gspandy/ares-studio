/**
 * Դ�������ƣ�LogicPropertyHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.logic.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.logic.excel;

import java.util.HashMap;
import java.util.Map;

import com.hundsun.ares.studio.atom.AtomPackage;
import com.hundsun.ares.studio.biz.excel.handlers.InterfacePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.NullPropertyHandler;

/**
 * @author sundl
 *
 */
public abstract class LogicPropertyHandlerFactory extends ExtensiblePropertyHandlerFactory {

	public static Map<String, IPropertyHandler> LOGIC_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();
	
	static {
		LOGIC_PROPERTY_HANDLERS.putAll(InterfacePropertyHandlerFactory.INTERFACE_PROPERTY_HANDLERS);
		LOGIC_PROPERTY_HANDLERS.put("�������ݿ�", NullPropertyHandler.INSTANCE);
		LOGIC_PROPERTY_HANDLERS.put("������", NullPropertyHandler.INSTANCE);
		LOGIC_PROPERTY_HANDLERS.put("�Ƿ񸴺�", NullPropertyHandler.INSTANCE);
		LOGIC_PROPERTY_HANDLERS.put("��������", NullPropertyHandler.INSTANCE);
		LOGIC_PROPERTY_HANDLERS.put("���˼���", NullPropertyHandler.INSTANCE);
		
		LOGIC_PROPERTY_HANDLERS.put("ҵ��������", new EMFPropertyHandler(AtomPackage.Literals.ATOM_FUNCTION__PSEUDO_CODE));
		LOGIC_PROPERTY_HANDLERS.put("����˵��", NullPropertyHandler.INSTANCE);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#getStaticHandlers()
	 */
	@Override
	protected Map<String, IPropertyHandler> getStaticHandlers() {
		return LOGIC_PROPERTY_HANDLERS;
	}

}
