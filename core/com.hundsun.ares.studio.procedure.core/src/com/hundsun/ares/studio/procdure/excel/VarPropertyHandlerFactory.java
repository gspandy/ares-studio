/**
 * Դ�������ƣ�VarPropertyHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.atom.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.procdure.excel;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.NullPropertyHandler;
import com.hundsun.ares.studio.procdure.ProcdurePackage;

/**
 * �ڲ�������PropertyHandlerFactory
 * @author sundl
 *
 */
public class VarPropertyHandlerFactory extends ExtensiblePropertyHandlerFactory {

	public static final VarPropertyHandlerFactory INSTANCE = new VarPropertyHandlerFactory();
	
	public static Map<String, IPropertyHandler> VAR_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();
	
	static {
		// �ڲ�����
		VAR_PROPERTY_HANDLERS.put("����", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__FLAGS));
		VAR_PROPERTY_HANDLERS.put("������", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__ID));
		VAR_PROPERTY_HANDLERS.put("������", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__ID));
		VAR_PROPERTY_HANDLERS.put("����", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__TYPE));
		VAR_PROPERTY_HANDLERS.put("��������", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__TYPE));
		VAR_PROPERTY_HANDLERS.put("˵��", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__NAME));
		VAR_PROPERTY_HANDLERS.put("ȱʡֵ", NullPropertyHandler.INSTANCE);
		VAR_PROPERTY_HANDLERS.put("����", NullPropertyHandler.INSTANCE);
		
		VAR_PROPERTY_HANDLERS.put("Ĭ��ֵ", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__DEFAULT_VALUE));
		VAR_PROPERTY_HANDLERS.put("ע��", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__COMMENTS));
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#getStaticHandlers()
	 */
	@Override
	protected Map<String, IPropertyHandler> getStaticHandlers() {
		return VAR_PROPERTY_HANDLERS;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#geteEClass()
	 */
	@Override
	protected EClass geteEClass() {
		return ProcdurePackage.Literals.INTERNAL_PARAM;
	}

}
