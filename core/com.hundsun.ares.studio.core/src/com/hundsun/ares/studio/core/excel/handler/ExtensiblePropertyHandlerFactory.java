/**
 * Դ�������ƣ�ExtensiblePropertyHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.model.ExtendPropertyManager;
import com.hundsun.ares.studio.core.model.extend.IBasicExtendPropertyDescriptor;

/**
 * @author sundl
 *
 */
public abstract class ExtensiblePropertyHandlerFactory implements IPropertyHandlerFactory {
	
	@Override
	public IPropertyHandler getPropertyHandler(String key, IARESProject project) {
		IPropertyHandler handler = getExtendedPropertyHandlers(geteEClass(), project).get(key);
		if (handler == null)
			handler = getStaticHandlers().get(key);
		
		if (handler == null)
			handler = (IPropertyHandler) getDynamicHandlers().get(key); 
		
		if (handler instanceof IPropertyHandler2) {
			((IPropertyHandler2) handler).setProject(project);
		}
		return handler;
	}
	
	/** ��̬handler,һ������������  */
	protected abstract Map<String, IPropertyHandler> getStaticHandlers();
	/** ��̬handler����Щ��project��ص�handler���������򵥵ľ�̬���������Էֿ����� */
	@SuppressWarnings("unchecked")
	protected Map<String, IPropertyHandler2> getDynamicHandlers() {
		return Collections.EMPTY_MAP;
	};
	
	protected abstract EClass geteEClass();

	protected Map<String, IPropertyHandler> getExtendedPropertyHandlers(EClass eClass, IARESProject project) {
		if(project == null) {//�մ���
			return new HashMap<String, IPropertyHandler>();
		}
		return createExtendedHandlers(project, eClass);
	}
	
	public static Map<String, IPropertyHandler> createExtendedHandlers(IARESElement element, EClass eclass) {
		Map<String, IPropertyHandler> handlers = new HashMap<String, IPropertyHandler>();
		List<IBasicExtendPropertyDescriptor> descriptors = ExtendPropertyManager.getInstance().getExtendedProperties(element, eclass);
		for (IBasicExtendPropertyDescriptor desc : descriptors ) {
			handlers.put(desc.getDisplayName(), new ExtendedPropertyHandler(desc));
		}
		return handlers;
	}
}
