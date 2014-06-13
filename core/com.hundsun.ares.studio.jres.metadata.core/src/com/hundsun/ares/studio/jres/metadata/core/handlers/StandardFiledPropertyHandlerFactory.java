/**
 * Դ�������ƣ�StandardFiledPropertyHandlerFactory.java
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

import com.hundsun.ares.studio.core.excel.handler.Data1PropertyProvider;
import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;

/**
 * @author sundl
 *
 */
public class StandardFiledPropertyHandlerFactory extends ExtensiblePropertyHandlerFactory{
	
	public static StandardFiledPropertyHandlerFactory INSTANCE = new StandardFiledPropertyHandlerFactory();
	
	public static Map<String, IPropertyHandler> STD_FIELD_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();

	static {
		STD_FIELD_PROPERTY_HANDLERS.put("�ֶ���", new EMFPropertyHandler(MetadataPackage.Literals.NAMED_ELEMENT__NAME));
		STD_FIELD_PROPERTY_HANDLERS.put("�ֶ�����", new EMFPropertyHandler(MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME));
		STD_FIELD_PROPERTY_HANDLERS.put("��ע", new EMFPropertyHandler(MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION));
		STD_FIELD_PROPERTY_HANDLERS.put("�����ֵ�", new EMFPropertyHandler(MetadataPackage.Literals.STANDARD_FIELD__DICTIONARY_TYPE));
		STD_FIELD_PROPERTY_HANDLERS.put("���", new Data1PropertyProvider("id"));
	}
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#getStaticHandlers()
	 */
	@Override
	protected Map<String, IPropertyHandler> getStaticHandlers() {
		return STD_FIELD_PROPERTY_HANDLERS;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#geteEClass()
	 */
	@Override
	protected EClass geteEClass() {
		return MetadataPackage.Literals.STANDARD_FIELD;
	}

}
