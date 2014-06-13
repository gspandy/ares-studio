/**
 * Դ�������ƣ�RevisionHistoryPropertyHandlerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.core.model.CorePackage;

/**
 * 
 * @author sundl
 */
public class RevisionHistoryPropertyHandlerFactory extends ExtensiblePropertyHandlerFactory {
	
	public static final RevisionHistoryPropertyHandlerFactory INSTANCE = new RevisionHistoryPropertyHandlerFactory();

	public static final Map<String, IPropertyHandler> HIS_PROPERTY_HANDLERS = new HashMap<String, IPropertyHandler>();
	
	static {
		HIS_PROPERTY_HANDLERS.put("�޸İ汾", new HisVersionPropertyHandler());
		HIS_PROPERTY_HANDLERS.put("�޶�����", new EMFPropertyHandler(CorePackage.Literals.REVISION_HISTORY__MODIFIED_DATE));
		HIS_PROPERTY_HANDLERS.put("�޸�����", new EMFPropertyHandler(CorePackage.Literals.REVISION_HISTORY__MODIFIED));
		HIS_PROPERTY_HANDLERS.put("�޸ĵ����", new EMFPropertyHandler(CorePackage.Literals.REVISION_HISTORY__ORDER_NUMBER));
		HIS_PROPERTY_HANDLERS.put("������", new EMFPropertyHandler(CorePackage.Literals.REVISION_HISTORY__MODIFIED_BY));
		HIS_PROPERTY_HANDLERS.put("������", new EMFPropertyHandler(CorePackage.Literals.REVISION_HISTORY__CHARGER));
		HIS_PROPERTY_HANDLERS.put("�޸���", new EMFPropertyHandler(CorePackage.Literals.REVISION_HISTORY__CHARGER));

		HIS_PROPERTY_HANDLERS.put("ҵ��Χ", NullPropertyHandler.INSTANCE);
		HIS_PROPERTY_HANDLERS.put("����״̬", NullPropertyHandler.INSTANCE);
		HIS_PROPERTY_HANDLERS.put("��Ӧ��Ʒ", NullPropertyHandler.INSTANCE);
		HIS_PROPERTY_HANDLERS.put("���ϰ汾�Ƿ����", NullPropertyHandler.INSTANCE);
		HIS_PROPERTY_HANDLERS.put("Ԥ�Ʒ����汾", NullPropertyHandler.INSTANCE);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#getStaticHandlers()
	 */
	@Override
	protected Map<String, IPropertyHandler> getStaticHandlers() {
		return HIS_PROPERTY_HANDLERS;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.ExtensiblePropertyHandlerFactory#geteEClass()
	 */
	@Override
	protected EClass geteEClass() {
		return CorePackage.Literals.REVISION_HISTORY;
	}

}
