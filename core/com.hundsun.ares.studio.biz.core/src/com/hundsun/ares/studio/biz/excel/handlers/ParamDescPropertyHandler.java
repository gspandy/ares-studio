/**
 * Դ�������ƣ�ParamDescPropertyHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.excel.handlers;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author sundl
 *
 */
public class ParamDescPropertyHandler extends ParameterRefPropertyHandler {

	/**
	 * @param targetFeature
	 */
	public ParamDescPropertyHandler(EStructuralFeature targetFeature, EStructuralFeature feature) {
		super(targetFeature, feature);
	}

}
