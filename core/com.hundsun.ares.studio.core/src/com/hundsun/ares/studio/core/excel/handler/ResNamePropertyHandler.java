/**
 * Դ�������ƣ�ResNamePropertyHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

import com.hundsun.ares.studio.core.model.CorePackage;

/**
 * ���⴦����ԴӢ������Handler���滻��Դ���е�'.'�ַ�Ϊ'_'
 * @author sundl
 *
 */
public class ResNamePropertyHandler extends EMFPropertyHandler {

	public ResNamePropertyHandler() {
		super(CorePackage.Literals.BASIC_RESOURCE_INFO__NAME);
	}

	@Override
	public void setValue(Object obj, String value) {
		if (value != null) {
			value = value.replace('.', '_');
		}
		super.setValue(obj, value);
	}
	
}
