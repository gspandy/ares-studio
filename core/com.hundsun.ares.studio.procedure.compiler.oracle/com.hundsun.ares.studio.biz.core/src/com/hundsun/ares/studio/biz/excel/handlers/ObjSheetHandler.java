/**
 * Դ�������ƣ�ObjSheetHandler.java
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

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.biz.ARESObject;
import com.hundsun.ares.studio.biz.BizFactory;
import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.constants.IBizResType;
import com.hundsun.ares.studio.core.excel.Module;
import com.hundsun.ares.studio.core.excel.Resource;
import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.EMFSheetHandler;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.NullPropertyHandler;

/**
 * @author sundl
 *
 */
public class ObjSheetHandler extends EMFSheetHandler {
	
	private static Map<String, IPropertyHandler> OBJECT_PROPERTY_HANDLERS = new HashMap<String, IPropertyHandler>();
	private static Map<String, IPropertyHandler> OBJECT_PROPERTY_PROPERTY_HANDLERS = new HashMap<String, IPropertyHandler>();
	
	static {
		OBJECT_PROPERTY_HANDLERS.putAll(EMFPropertyHandler.BASIC_HANDLERS);
		OBJECT_PROPERTY_HANDLERS.put("������", EMFPropertyHandler.NAME_PROPERTY_HANDLER);
		OBJECT_PROPERTY_HANDLERS.put("����������", EMFPropertyHandler.CNAME_PROPERTY_HANDLER);
		OBJECT_PROPERTY_HANDLERS.put("��������", EMFPropertyHandler.DESCRIPTION_PROPERTY_HANDLER);
		OBJECT_PROPERTY_HANDLERS.put("˵��", EMFPropertyHandler.DESCRIPTION_PROPERTY_HANDLER);
		OBJECT_PROPERTY_HANDLERS.put("�޸ļ�¼", NullPropertyHandler.INSTANCE);
		//OBJECT_PROPERTY_HANDLERS.put("XML��ǩ", NullPropertyHandler.INSTANCE);
		//OBJECT_PROPERTY_HANDLERS.putAll(EMFPropertyHandler.createExtendedHandlers(BizPackage.Literals.ARES_OBJECT));
		
		OBJECT_PROPERTY_PROPERTY_HANDLERS.putAll(BizPropertyHandler.PARAM_BASIC_HANDLERS);
		OBJECT_PROPERTY_PROPERTY_HANDLERS.put("��������", BizPropertyHandler.PARAM_FLAG_PROPERTY_HANDLER);
		OBJECT_PROPERTY_PROPERTY_HANDLERS.put("����", BizPropertyHandler.PARAM_TYPE_PROPERTY_HANDLER);
		//OBJECT_PROPERTY_PROPERTY_HANDLERS.put("XML��ǩ", NullPropertyHandler.INSTANCE);
	}

	protected ARESObject obj;
	
	@Override
	public void startSheet(HSSFSheet sheet) {
		super.startSheet(sheet);
		module = new Module();
		//���ķָ�����Ҳ֧��
		String sheetName = StringUtils.replace(sheet.getSheetName(), "��", "-");
		String cName = StringUtils.removeStart(sheetName, "����-");
		cName = StringUtils.removeStart(cName, "ҵ�����-");
		module.cName = cName;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#endArea()
	 */
	public void endArea() {
		super.endArea();
		if (obj != null) {
			Resource resource = new Resource();
			resource.info = obj;
			resource.name = obj.getName();
			resource.type = IBizResType.Object;
			resourceFound(resource);
		}
	}
	
	@Override
	public void startArea(String startTag) {
		super.startArea(startTag);
		if (StringUtils.startsWith(startTag, "������"))
			obj = BizFactory.eINSTANCE.createARESObject();
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.EMFSheetHandler#getPropertyHandlers()
	 */
	@Override
	protected Map<String, IPropertyHandler> getPropertyHandlers() {
		if (StringUtils.equals(parser.getCurrentBlockTag(), "��������")) {
			return OBJECT_PROPERTY_PROPERTY_HANDLERS;
		}
		return OBJECT_PROPERTY_HANDLERS;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.EMFSheetHandler#getOwner()
	 */
	@Override
	protected EObject getOwner() {
		return obj;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.EMFSheetHandler#getTableFeature()
	 */
	@Override
	protected EStructuralFeature getTableFeature() {
		return BizPackage.Literals.ARES_OBJECT__PROPERTIES;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.EMFSheetHandler#getEClass()
	 */
	@Override
	protected EClass getEClass() {
		return BizPackage.Literals.PARAMETER;
	}

}
