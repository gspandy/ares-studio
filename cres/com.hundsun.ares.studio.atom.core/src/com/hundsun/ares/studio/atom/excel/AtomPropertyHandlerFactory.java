/**
 * Դ�������ƣ�AtomPropertyHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.atom.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.atom.excel;

import java.util.HashMap;
import java.util.Map;

import com.hundsun.ares.studio.atom.AtomPackage;
import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.excel.handlers.InterfacePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.NullPropertyHandler;

/**
 * @author sundl
 *
 */
public abstract class AtomPropertyHandlerFactory extends ExtensiblePropertyHandlerFactory {

	/** �������Դ����Ӧ�б� */
	public static Map<String, IPropertyHandler> ATOM_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();
	
	static {
		ATOM_PROPERTY_HANDLERS.putAll(InterfacePropertyHandlerFactory.INTERFACE_PROPERTY_HANDLERS); 
		ATOM_PROPERTY_HANDLERS.put("�������ݿ�", new EMFPropertyHandler(AtomPackage.Literals.ATOM_FUNCTION__DATABASE));
		ATOM_PROPERTY_HANDLERS.put("�������ݿ�", new EMFPropertyHandler(AtomPackage.Literals.ATOM_FUNCTION__DATABASE));//���ڲ�Ʒ����ϵͳ06��۰��и�ʽ��
		ATOM_PROPERTY_HANDLERS.put("ҵ��������", new EMFPropertyHandler(AtomPackage.Literals.ATOM_FUNCTION__PSEUDO_CODE));
		ATOM_PROPERTY_HANDLERS.put("����˵��", NullPropertyHandler.INSTANCE);
		ATOM_PROPERTY_HANDLERS.put("������ʾ", NullPropertyHandler.INSTANCE);//���ڲ�Ʒ����ϵͳ06��۰����д���Ϣ����ȥ����
		
		ATOM_PROPERTY_HANDLERS.put("ҵ��������", new EMFPropertyHandler(AtomPackage.Literals.ATOM_FUNCTION__PSEUDO_CODE));
		ATOM_PROPERTY_HANDLERS.put("����˵��", NullPropertyHandler.INSTANCE);
		ATOM_PROPERTY_HANDLERS.put("������ʾ", NullPropertyHandler.INSTANCE);//���ڲ�Ʒ����ϵͳ06��۰����д���Ϣ����ȥ����
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#getStaticHandlers()
	 */
	@Override
	protected Map<String, IPropertyHandler> getStaticHandlers() {
		return ATOM_PROPERTY_HANDLERS;
	}

}
