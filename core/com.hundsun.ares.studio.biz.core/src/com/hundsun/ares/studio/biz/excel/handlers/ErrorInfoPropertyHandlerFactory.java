/**
 * Դ�������ƣ�ErrorInfoPropertyHandlerFactory.java
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
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.reference.RefEMFPropertyHandler;

/**
 * @author sundl
 *
 */
public class ErrorInfoPropertyHandlerFactory extends ExtensiblePropertyHandlerFactory {
	
	public static final ErrorInfoPropertyHandlerFactory INSTANCE = new ErrorInfoPropertyHandlerFactory();
	
	public static Map<String, IPropertyHandler> ERR_INFO_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();

	static {
		ERR_INFO_PROPERTY_HANDLERS.put("����˵��", NullPropertyHandler.INSTANCE);

		ERR_INFO_PROPERTY_HANDLERS.put("�����", new EMFPropertyHandler(MetadataPackage.Literals.ERROR_NO_ITEM__NO));
		ERR_INFO_PROPERTY_HANDLERS.put("������Ϣ", new RefEMFPropertyHandler(MetadataPackage.Literals.ERROR_NO_ITEM__NO, 
																				IMetadataRefType.ErrNo_No, 
																				MetadataPackage.Literals.ERROR_NO_ITEM__MESSAGE, 
																				MetadataPackage.Literals.ERROR_NO_ITEM__MESSAGE));
		ERR_INFO_PROPERTY_HANDLERS.put("���󼶱�", new RefEMFPropertyHandler(MetadataPackage.Literals.ERROR_NO_ITEM__NO, 
				IMetadataRefType.ErrNo_No, 
				MetadataPackage.Literals.ERROR_NO_ITEM__LEVEL, 
				MetadataPackage.Literals.ERROR_NO_ITEM__LEVEL));
		ERR_INFO_PROPERTY_HANDLERS.put("����˵��", new RefEMFPropertyHandler(MetadataPackage.Literals.ERROR_NO_ITEM__NO, 
				IMetadataRefType.ErrNo_No, 
				MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION, 
				MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION));
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#getStaticHandlers()
	 */
	@Override
	protected Map<String, IPropertyHandler> getStaticHandlers() {
		return ERR_INFO_PROPERTY_HANDLERS;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#geteEClass()
	 */
	@Override
	protected EClass geteEClass() {
		return BizPackage.Literals.ERROR_INFO;
	}

}
