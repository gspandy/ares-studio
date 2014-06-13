/**
 * Դ�������ƣ�InOutCollectionPropertyHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.service.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.service.core.excel;

import org.eclipse.emf.ecore.EAttribute;

import com.hundsun.ares.studio.core.excel.handler.BooleanEMFPropertyHandler;
import com.hundsun.ares.studio.jres.service.Service;

/**
 * ר�Ŵ�����������������PropertyHandler
 * @author sundl
 *
 */
public class InOutCollectionPropertyHandler extends BooleanEMFPropertyHandler {

	/**
	 * @param eAttribute
	 */
	public InOutCollectionPropertyHandler(EAttribute eAttribute) {
		super(eAttribute, BooleanEMFPropertyHandler.STYLE_CN);
	}
	
	@Override
	public void setValue(Object obj, String value) {
		if (obj instanceof Service) {
			super.setValue(((Service) obj).getInterface(), value);
		}
	}

	@Override
	public String getValue(Object obj) {
		if (obj instanceof Service) {
			return super.getValue(((Service) obj).getInterface());
		}
		return super.getValue(false);
	}
}
