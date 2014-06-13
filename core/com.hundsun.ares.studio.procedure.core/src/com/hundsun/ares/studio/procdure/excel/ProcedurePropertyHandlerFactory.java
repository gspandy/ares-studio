/**
 * Դ�������ƣ�ProcedurePropertyHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.procedure.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.procdure.excel;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.excel.handlers.InterfacePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.NullPropertyHandler;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.procdure.ProcdurePackage;

/**
 * @author sundl
 *
 */
public class ProcedurePropertyHandlerFactory extends ExtensiblePropertyHandlerFactory {

	public static final ProcedurePropertyHandlerFactory INSTANCE = new ProcedurePropertyHandlerFactory();
	
	public static Map<String, IPropertyHandler> PROCEDURE_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();
	
	static {
		PROCEDURE_PROPERTY_HANDLERS.putAll(InterfacePropertyHandlerFactory.INTERFACE_PROPERTY_HANDLERS);
		PROCEDURE_PROPERTY_HANDLERS.put("�������ݿ�", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__DATABASE));
		PROCEDURE_PROPERTY_HANDLERS.put("�������ݿ�", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__DATABASE));
		PROCEDURE_PROPERTY_HANDLERS.put("�������ݿ�", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__DATABASE));
		PROCEDURE_PROPERTY_HANDLERS.put("�ӿڱ�־", new EMFPropertyHandler(BizPackage.Literals.BIZ_INTERFACE__INTERFACE_FLAG));
		//--����֤ȯ����
		PROCEDURE_PROPERTY_HANDLERS.put("ҵ������", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__BIZ_TYPE));
		PROCEDURE_PROPERTY_HANDLERS.put("��������", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__DEFINE_TYPE));
		PROCEDURE_PROPERTY_HANDLERS.put("�汾���", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__CREATE_DATE));
		PROCEDURE_PROPERTY_HANDLERS.put("���̷�������", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__RETURN_TYPE));
		PROCEDURE_PROPERTY_HANDLERS.put("������", EMFPropertyHandler.NAME_PROPERTY_HANDLER);
		PROCEDURE_PROPERTY_HANDLERS.put("��������", EMFPropertyHandler.NAME_PROPERTY_HANDLER);
		PROCEDURE_PROPERTY_HANDLERS.put("����˵��", new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME));
		PROCEDURE_PROPERTY_HANDLERS.put("����˵��", new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME));
		
		PROCEDURE_PROPERTY_HANDLERS.put("ҵ��������", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__PSEUDO_CODE));
		PROCEDURE_PROPERTY_HANDLERS.put("����˵��", NullPropertyHandler.INSTANCE);
		//--����֤ȯ����
		PROCEDURE_PROPERTY_HANDLERS.put("ǰ�ô���", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__BEGIN_CODE));
		PROCEDURE_PROPERTY_HANDLERS.put("���ô���", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__END_CODE));
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#getStaticHandlers()
	 */
	@Override
	protected Map<String, IPropertyHandler> getStaticHandlers() {
		return PROCEDURE_PROPERTY_HANDLERS;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#geteEClass()
	 */
	@Override
	protected EClass geteEClass() {
		return ProcdurePackage.Literals.PROCEDURE;
	}

}
