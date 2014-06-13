/**
 * Դ�������ƣ�MetadataPropertyHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.model.metadata.propertyHandlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.NullPropertyHandler;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;

/**
 * �����
 * @author sundl
 *
 */
public class ErrorNoItemPropertyHandlerFactory extends ExtensiblePropertyHandlerFactory {
	
	public static ErrorNoItemPropertyHandlerFactory INSTANCE = new ErrorNoItemPropertyHandlerFactory();

	public static Map<String, IPropertyHandler> ERROR_NO_ITME_PROPERTIES = new HashMap<String, IPropertyHandler>();

	static {
		ERROR_NO_ITME_PROPERTIES.put("����˵��", NullPropertyHandler.INSTANCE);
		ERROR_NO_ITME_PROPERTIES.put("�����", new EMFPropertyHandler(MetadataPackage.Literals.ERROR_NO_ITEM__NO));
		ERROR_NO_ITME_PROPERTIES.put("������Ϣ", new EMFPropertyHandler(MetadataPackage.Literals.ERROR_NO_ITEM__MESSAGE));
		ERROR_NO_ITME_PROPERTIES.put("����˵��", new EMFPropertyHandler(MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION));
		ERROR_NO_ITME_PROPERTIES.put("���󼶱�", new EMFPropertyHandler(MetadataPackage.Literals.ERROR_NO_ITEM__LEVEL));
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#getStaticHandlers()
	 */
	@Override
	protected Map<String, IPropertyHandler> getStaticHandlers() {
		return ERROR_NO_ITME_PROPERTIES;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#geteEClass()
	 */
	@Override
	protected EClass geteEClass() {
		return MetadataPackage.Literals.ERROR_NO_ITEM;
	}

}
