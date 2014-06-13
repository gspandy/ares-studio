/**
 * Դ�������ƣ�InterfacePropertyHandlerFactory.java
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
import com.hundsun.ares.studio.core.excel.handler.BooleanEMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.NullPropertyHandler;
import com.hundsun.ares.studio.core.model.CorePackage;

/**
 * @author sundl
 *
 */
public class InterfacePropertyHandlerFactory extends ExtensiblePropertyHandlerFactory implements IPropertyHandlerFactory {
	
	public static InterfacePropertyHandlerFactory INSTANCE = new InterfacePropertyHandlerFactory();
	
	/** �ӿڶ���һ�����Ե�handler */
	public static Map<String, IPropertyHandler> INTERFACE_PROPERTY_HANDLERS = new HashMap<String, IPropertyHandler>();

	static {
		// ����Interface��һ�����Ժʹ������б� ���ྡ����Ҫֱ������Щmap�м����ݣ���������֮�以�����
		INTERFACE_PROPERTY_HANDLERS.put("�����", new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__OBJECT_ID));
		// ȷ�����账������ԣ���NullPropertyHandler���ⱨ��
		INTERFACE_PROPERTY_HANDLERS.put("�汾��", NullPropertyHandler.INSTANCE);
		INTERFACE_PROPERTY_HANDLERS.put("��������", NullPropertyHandler.INSTANCE);
		INTERFACE_PROPERTY_HANDLERS.put("������", EMFPropertyHandler.NAME_PROPERTY_HANDLER);
		INTERFACE_PROPERTY_HANDLERS.put("��������", EMFPropertyHandler.NAME_PROPERTY_HANDLER);
		INTERFACE_PROPERTY_HANDLERS.put("��������", EMFPropertyHandler.NAME_PROPERTY_HANDLER);
		INTERFACE_PROPERTY_HANDLERS.put("����˵��", EMFPropertyHandler.CNAME_PROPERTY_HANDLER);
		INTERFACE_PROPERTY_HANDLERS.put("����˵��", EMFPropertyHandler.CNAME_PROPERTY_HANDLER);
		INTERFACE_PROPERTY_HANDLERS.put("��������", EMFPropertyHandler.CNAME_PROPERTY_HANDLER);
		INTERFACE_PROPERTY_HANDLERS.put("���������", new BooleanEMFPropertyHandler(BizPackage.Literals.BIZ_INTERFACE__OUTPUT_COLLECTION));
		INTERFACE_PROPERTY_HANDLERS.put("�ӿڱ�־", new EMFPropertyHandler(BizPackage.Literals.BIZ_INTERFACE__INTERFACE_FLAG));
		INTERFACE_PROPERTY_HANDLERS.put("˵��", new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__DESCRIPTION));
		INTERFACE_PROPERTY_HANDLERS.put("���ܺ�", NullPropertyHandler.INSTANCE);
		INTERFACE_PROPERTY_HANDLERS.put("�������ݿ�", NullPropertyHandler.INSTANCE);
		INTERFACE_PROPERTY_HANDLERS.put("������ɫ", NullPropertyHandler.INSTANCE);
		INTERFACE_PROPERTY_HANDLERS.put("�������", NullPropertyHandler.INSTANCE);
		INTERFACE_PROPERTY_HANDLERS.put("������", NullPropertyHandler.INSTANCE);
		INTERFACE_PROPERTY_HANDLERS.put("���󼶱�", NullPropertyHandler.INSTANCE);
		INTERFACE_PROPERTY_HANDLERS.put("ҵ������", new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__DESCRIPTION));
		//2014-01-28 modified by zhuyf ��ӹ����������������ӿڣ�ʱ�ĵ����д˸�ʽ��Ϣ��
		INTERFACE_PROPERTY_HANDLERS.put("��������", new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__DESCRIPTION));
		INTERFACE_PROPERTY_HANDLERS.put("����˵��", NullPropertyHandler.INSTANCE);
		//2014-01-28 modified by zhuyf ���ҵ��˵�����������ӿڣ�ʱ�ĵ����д˸�ʽ��Ϣ��
		INTERFACE_PROPERTY_HANDLERS.put("ҵ��˵��", NullPropertyHandler.INSTANCE);
		
		INTERFACE_PROPERTY_HANDLERS.put("�޸ļ�¼", NullPropertyHandler.INSTANCE);
		
		INTERFACE_PROPERTY_HANDLERS.put("����", NullPropertyHandler.INSTANCE);
		INTERFACE_PROPERTY_HANDLERS.put("�ڲ�����", NullPropertyHandler.INSTANCE);
		INTERFACE_PROPERTY_HANDLERS.put("���̱���", NullPropertyHandler.INSTANCE);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.ExtensiblePropertyHandlerFactory#getStaticHandlers()
	 */
	@Override
	protected Map<String, IPropertyHandler> getStaticHandlers() {
		return INTERFACE_PROPERTY_HANDLERS;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#geteEClass()
	 */
	@Override
	protected EClass geteEClass() {
		return BizPackage.Literals.ARES_OBJECT;
	}
	
}
