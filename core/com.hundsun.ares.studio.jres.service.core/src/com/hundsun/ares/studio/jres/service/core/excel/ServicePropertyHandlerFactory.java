/**
 * Դ�������ƣ�ServicePropertyHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.service.stock
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.service.core.excel;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.excel.handlers.InterfacePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.HisPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.NullPropertyHandler;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.jres.service.ServicePackage;

/**
 * @author sundl
 *
 */
public class ServicePropertyHandlerFactory extends ExtensiblePropertyHandlerFactory implements IPropertyHandlerFactory {
	
	public static final ServicePropertyHandlerFactory INSTANCE = new ServicePropertyHandlerFactory();

	public static Map<String, IPropertyHandler> SERVICE_PROPERTY_HANDLERS = new HashMap<String, IPropertyHandler>();

	static{
		SERVICE_PROPERTY_HANDLERS.putAll(InterfacePropertyHandlerFactory.INTERFACE_PROPERTY_HANDLERS);
		SERVICE_PROPERTY_HANDLERS.put("���ܺ�", new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__OBJECT_ID));
		SERVICE_PROPERTY_HANDLERS.put("������", EMFPropertyHandler.NAME_PROPERTY_HANDLER);
		//2014-01-28 modified by zhuyf ���Ӣ�������������ӿڣ�ʱ�ĵ����д˸�ʽ��Ϣ��
		SERVICE_PROPERTY_HANDLERS.put("Ӣ����", EMFPropertyHandler.NAME_PROPERTY_HANDLER);
		SERVICE_PROPERTY_HANDLERS.put("����������", new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME));
		SERVICE_PROPERTY_HANDLERS.put("��������", new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME));
		//�����ڷ���ӿ� ����Ϊ������������
		SERVICE_PROPERTY_HANDLERS.put("��������", new InOutCollectionPropertyHandler(BizPackage.Literals.BIZ_INTERFACE__INPUT_COLLECTION));
		SERVICE_PROPERTY_HANDLERS.put("��������", new InOutCollectionPropertyHandler(BizPackage.Literals.BIZ_INTERFACE__OUTPUT_COLLECTION));
		SERVICE_PROPERTY_HANDLERS.put("���������", new InOutCollectionPropertyHandler(BizPackage.Literals.BIZ_INTERFACE__OUTPUT_COLLECTION));
		
		//�����Կ���  ��ɾ��ԭ��������
		SERVICE_PROPERTY_HANDLERS.put("�����Ƿ�����", new InOutCollectionPropertyHandler(BizPackage.Literals.BIZ_INTERFACE__INPUT_COLLECTION));
		SERVICE_PROPERTY_HANDLERS.put("����Ƿ�����", new InOutCollectionPropertyHandler(BizPackage.Literals.BIZ_INTERFACE__OUTPUT_COLLECTION));
		SERVICE_PROPERTY_HANDLERS.put("�Ƿ񹫿�", NullPropertyHandler.INSTANCE);
		SERVICE_PROPERTY_HANDLERS.put("�Ƿ񸴺�", NullPropertyHandler.INSTANCE);
		SERVICE_PROPERTY_HANDLERS.put("��������", NullPropertyHandler.INSTANCE);
		SERVICE_PROPERTY_HANDLERS.put("���˼���", NullPropertyHandler.INSTANCE);
		SERVICE_PROPERTY_HANDLERS.put("ҵ��������", NullPropertyHandler.INSTANCE);
		SERVICE_PROPERTY_HANDLERS.put("�˻�2.0", NullPropertyHandler.INSTANCE);
		
		SERVICE_PROPERTY_HANDLERS.put("�޸ļ�¼", new HisPropertyHandler(CorePackage.Literals.JRES_RESOURCE_INFO__HISTORIES));
	}
	
	@Override
	protected Map<String, IPropertyHandler> getStaticHandlers() {
		return SERVICE_PROPERTY_HANDLERS;
	}
	
	@Override
	protected EClass geteEClass() {
		return ServicePackage.Literals.SERVICE;
	}
}
