/**
 * Դ�������ƣ�BizPkgPropertyHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.metadata.core.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;

/**
 * @author sundl
 *
 */
public class BizPkgPropertyHandlerFactory extends ExtensiblePropertyHandlerFactory{
	public static BizPkgPropertyHandlerFactory INSTANCE = new BizPkgPropertyHandlerFactory();
	
	public static Map<String, IPropertyHandler> BIZ_PKG_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();

	static {
		BIZ_PKG_PROPERTY_HANDLERS.put("���", new EMFPropertyHandler(MetadataPackage.Literals.NAMED_ELEMENT__NAME));
		BIZ_PKG_PROPERTY_HANDLERS.put("����", new EMFPropertyHandler(MetadataPackage.Literals.BIZ_PROPERTY_CONFIG__ENAME));
		BIZ_PKG_PROPERTY_HANDLERS.put("������", new EMFPropertyHandler(MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME));
		BIZ_PKG_PROPERTY_HANDLERS.put("˵��", new EMFPropertyHandler(MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION));

	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#getStaticHandlers()
	 */
	@Override
	protected Map<String, IPropertyHandler> getStaticHandlers() {
		return BIZ_PKG_PROPERTY_HANDLERS;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#geteEClass()
	 */
	@Override
	protected EClass geteEClass() {
		return MetadataPackage.Literals.BIZ_PROPERTY_CONFIG;
	}

}
