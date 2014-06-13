/**
 * Դ�������ƣ�EMFPropertyHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.excel.SheetParser;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.ExtendPropertyManager;
import com.hundsun.ares.studio.core.model.extend.IBasicExtendPropertyDescriptor;

/**
 * ���EMF�����EAttribute���͵����ԵĴ����� 
 * @author sundl
 *
 */
public class EMFPropertyHandler implements IPropertyHandler {
	
	private static final Logger LOGGER = Logger.getLogger(EMFPropertyHandler.class);
	
	public static IPropertyHandler OBJECT_ID_PROPERTY_HANDLER = new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__OBJECT_ID);
	/** רΪ��Դ�������Handler, ��ɺ��еĵ�'.'�滻���»��� */
	public static IPropertyHandler NAME_PROPERTY_HANDLER = new ResNamePropertyHandler();
	public static IPropertyHandler CNAME_PROPERTY_HANDLER = new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME);
	public static IPropertyHandler DESCRIPTION_PROPERTY_HANDLER = new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__DESCRIPTION);
	
	public static Map<String, IPropertyHandler> BASIC_HANDLERS = new HashMap<String, IPropertyHandler>();


	static {
		// 
		BASIC_HANDLERS.put("�����", OBJECT_ID_PROPERTY_HANDLER);
		BASIC_HANDLERS.put("���ܺ�", NullPropertyHandler.INSTANCE);
		
		// ȷ�����账������ԣ���NullPropertyHandler���ⱨ��
		BASIC_HANDLERS.put("�汾��", NullPropertyHandler.INSTANCE);
		BASIC_HANDLERS.put("��������", NullPropertyHandler.INSTANCE);
		
		BASIC_HANDLERS.put("������", NAME_PROPERTY_HANDLER);
		BASIC_HANDLERS.put("��������", NAME_PROPERTY_HANDLER);
		BASIC_HANDLERS.put("����˵��", CNAME_PROPERTY_HANDLER);
		BASIC_HANDLERS.put("��������", CNAME_PROPERTY_HANDLER);

	}
	
	public static IPropertyHandler getPropertyHandler(EClass eClass, String property, IARESProject project) {
		IPropertyHandlerFactory factory = PropertyHandlerFactoryManager.getPropertyHandlerFactory(eClass);
		if (factory != null) {
			IPropertyHandler handler = factory.getPropertyHandler(property, project);
			if (handler == null) {
				LOGGER.error(String.format("Eclass%s������%sû�ж�Ӧ��PropertyHandler", eClass, property));
			}
			return handler;
		}
		return null;
	}
	
	
	/**
	 * ����ָ����EClass����չ���Զ�Ӧ��Handler
	 * @param eclass
	 * @return
	 */
	public static Map<String, IPropertyHandler> createExtendedHandlers(SheetParser sheetParser, IARESElement element, EClass eclass) {
		Map<String, IPropertyHandler> handlers = new HashMap<String, IPropertyHandler>();
		List<IBasicExtendPropertyDescriptor> descriptors = ExtendPropertyManager.getInstance().getExtendedProperties(element, eclass);
		for (IBasicExtendPropertyDescriptor desc : descriptors ) {
			// descriptor ����ֱ��ʵ��PropertyHandler
			if (desc instanceof IPropertyHandler) {
				IPropertyHandler handler = (IPropertyHandler) desc;
				if (handler instanceof IPropertyHandlerExtension) {
					IPropertyHandlerExtension ext = (IPropertyHandlerExtension) handler;
					ext.init(sheetParser, element.getARESProject());
				}
				handlers.put(desc.getDisplayName(), handler);
			} else {
				handlers.put(desc.getDisplayName(), new ExtendedPropertyHandler(desc));
			}
		}
		return handlers;
	}
	
	protected EStructuralFeature feature;
	
	public EMFPropertyHandler(EStructuralFeature feature) {
		this.feature = feature;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.IPropertyHandler#setValue(java.lang.Object, java.lang.String)
	 */
	@Override
	public void setValue(Object obj, String value) {
		// �������EMF����ʲô������
		if (obj instanceof EObject) {
			((EObject) obj).eSet(feature, value);
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.IPropertyHandler#getValue()
	 */
	@Override
	public String getValue(Object obj) {
		if (obj instanceof EObject) {
			Object value = ((EObject) obj).eGet(getFeature(feature));
			if (value != null) {
				return String.valueOf(value);
			}
		}
		return null;
	}
	
	protected EStructuralFeature getFeature(EObject object) {
		return this.feature;
	}

}
