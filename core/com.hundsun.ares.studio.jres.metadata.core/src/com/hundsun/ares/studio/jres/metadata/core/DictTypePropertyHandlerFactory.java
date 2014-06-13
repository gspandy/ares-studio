/**
 * Դ�������ƣ�DictTypePropertyHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.metadata.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.core.excel.handler.Data1PropertyProvider;
import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.NullPropertyHandler;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;

/**
 * @author sundl
 *
 */
public class DictTypePropertyHandlerFactory extends ExtensiblePropertyHandlerFactory{

	public static final DictTypePropertyHandlerFactory INSTANCE = new DictTypePropertyHandlerFactory();
	
	public static final Map<String, IPropertyHandler> DICT_TYPE_HANDLERS = new HashMap<String, IPropertyHandler>();
	static {
		DICT_TYPE_HANDLERS.put("�ֵ���", new EMFPropertyHandler(MetadataPackage.Literals.NAMED_ELEMENT__NAME));
		DICT_TYPE_HANDLERS.put("�ֵ�������", new EMFPropertyHandler(MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME));
		DICT_TYPE_HANDLERS.put("���", new Data1PropertyProvider("id"));
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#getStaticHandlers()
	 */
	@Override
	protected Map<String, IPropertyHandler> getStaticHandlers() {
		return DICT_TYPE_HANDLERS;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#geteEClass()
	 */
	@Override
	protected EClass geteEClass() {
		return MetadataPackage.Literals.DICTIONARY_TYPE;
	}

}
