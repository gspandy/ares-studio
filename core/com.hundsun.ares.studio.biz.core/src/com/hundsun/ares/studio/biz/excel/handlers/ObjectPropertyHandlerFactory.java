/**
 * Դ�������ƣ�ObjectPropertyHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.excel.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.NullPropertyHandler;

/**
 * @author sundl
 *
 */
public class ObjectPropertyHandlerFactory extends ExtensiblePropertyHandlerFactory {
	
	public static final ObjectPropertyHandlerFactory INSTANCE = new ObjectPropertyHandlerFactory();

	private static Map<String, IPropertyHandler> OBJECT_PROPERTY_HANDLERS = new HashMap<String, IPropertyHandler>();

	static {
		OBJECT_PROPERTY_HANDLERS.putAll(EMFPropertyHandler.BASIC_HANDLERS);
		OBJECT_PROPERTY_HANDLERS.put("������", EMFPropertyHandler.NAME_PROPERTY_HANDLER);
		OBJECT_PROPERTY_HANDLERS.put("����������", EMFPropertyHandler.CNAME_PROPERTY_HANDLER);
		OBJECT_PROPERTY_HANDLERS.put("����������", EMFPropertyHandler.CNAME_PROPERTY_HANDLER);
		OBJECT_PROPERTY_HANDLERS.put("��������", EMFPropertyHandler.DESCRIPTION_PROPERTY_HANDLER);
		OBJECT_PROPERTY_HANDLERS.put("˵��", EMFPropertyHandler.DESCRIPTION_PROPERTY_HANDLER);
		OBJECT_PROPERTY_HANDLERS.put("�޸ļ�¼", NullPropertyHandler.INSTANCE);
		
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#getStaticHandlers()
	 */
	@Override
	protected Map<String, IPropertyHandler> getStaticHandlers() {
		return OBJECT_PROPERTY_HANDLERS;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#geteEClass()
	 */
	@Override
	protected EClass geteEClass() {
		return BizPackage.Literals.ARES_OBJECT;
	}

}
