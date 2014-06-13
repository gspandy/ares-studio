/**
 * Դ�������ƣ�BizPropertyHandler.java
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

import org.eclipse.emf.ecore.EAttribute;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.NullPropertyHandler;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;

/**
 * @author sundl
 *
 */
public class BizPropertyHandler extends EMFPropertyHandler {

	/** ��־λ */
	public static IPropertyHandler PARAM_FLAG_PROPERTY_HANDLER = new EMFPropertyHandler(BizPackage.Literals.PARAMETER__FLAGS);
	/** id = �ֶ�����Ӣ���� */
	public static IPropertyHandler PARAM_ID_PROPERTY_HANDLER = new EMFPropertyHandler(BizPackage.Literals.PARAMETER__ID);
	/** ���ƣ������� */
	public static IPropertyHandler PARAM_NAME_PROPERTY_HANDLER = new ParameterRefPropertyHandler(MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME, BizPackage.Literals.PARAMETER__NAME);
	/** ҵ������ */
	public static IPropertyHandler PARAM_TYPE_PROPERTY_HANDLER = new ParameterRefPropertyHandler(MetadataPackage.Literals.STANDARD_FIELD__DATA_TYPE, BizPackage.Literals.PARAMETER__TYPE);
	/** Ĭ��ֵ */
	public static IPropertyHandler PARAM_DEFAULT_VALUE_HANDLER = new EMFPropertyHandler(BizPackage.Literals.PARAMETER__DEFAULT_VALUE);
	/** ������ϵ */
	public static IPropertyHandler PARAM_MULTIPLICITY_HANDLER = new MultiplicityPropertyHandler(BizPackage.Literals.PARAMETER__MULTIPLICITY);
	/**����ע��*/
	public static IPropertyHandler PARAM_COMMENTS_HANDLER = new EMFPropertyHandler(BizPackage.Literals.PARAMETER__COMMENTS);
	/** ���� */
	public static IPropertyHandler PARAM_DESCRIPTION_HANDLER = new ParameterRefPropertyHandler(MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION, BizPackage.Literals.PARAMETER__DESCRIPTION);
	
	public static Map<String, IPropertyHandler> PARAM_BASIC_HANDLERS = new HashMap<String, IPropertyHandler>();
	
	static {
		PARAM_BASIC_HANDLERS.put("�������", PARAM_FLAG_PROPERTY_HANDLER);
		PARAM_BASIC_HANDLERS.put("�������", PARAM_FLAG_PROPERTY_HANDLER);
		PARAM_BASIC_HANDLERS.put("��������", PARAM_FLAG_PROPERTY_HANDLER);

		PARAM_BASIC_HANDLERS.put("������", PARAM_ID_PROPERTY_HANDLER);
		PARAM_BASIC_HANDLERS.put("������", PARAM_NAME_PROPERTY_HANDLER);
		PARAM_BASIC_HANDLERS.put("������", PARAM_ID_PROPERTY_HANDLER);
		PARAM_BASIC_HANDLERS.put("����", PARAM_TYPE_PROPERTY_HANDLER);
		PARAM_BASIC_HANDLERS.put("��������", PARAM_TYPE_PROPERTY_HANDLER);
		PARAM_BASIC_HANDLERS.put("˵��", PARAM_NAME_PROPERTY_HANDLER);
//		PARAM_BASIC_HANDLERS.put("ȱʡֵ", NullPropertyHandler.INSTANCE);
		PARAM_BASIC_HANDLERS.put("ȱʡֵ", PARAM_DEFAULT_VALUE_HANDLER);
		PARAM_BASIC_HANDLERS.put("Ĭ��ֵ", PARAM_DEFAULT_VALUE_HANDLER);
		PARAM_BASIC_HANDLERS.put("����", NullPropertyHandler.INSTANCE);
		PARAM_BASIC_HANDLERS.put("��ϵ����", PARAM_MULTIPLICITY_HANDLER);
		PARAM_BASIC_HANDLERS.put("��ע", PARAM_COMMENTS_HANDLER);
		PARAM_BASIC_HANDLERS.put("����", PARAM_DESCRIPTION_HANDLER);
		PARAM_BASIC_HANDLERS.put("��������", NullPropertyHandler.INSTANCE);
		PARAM_BASIC_HANDLERS.put("ע��", PARAM_COMMENTS_HANDLER);
		
	}
	
	/**
	 * @param eAttribute
	 */
	public BizPropertyHandler(EAttribute eAttribute) {
		super(eAttribute);
	}

}
