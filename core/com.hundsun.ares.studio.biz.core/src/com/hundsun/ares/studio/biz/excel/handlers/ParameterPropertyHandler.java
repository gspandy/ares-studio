/**
 * Դ�������ƣ�ParameterPropertyHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.excel.handlers;

import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler2;

/**
 * ����ģ�͵�PropertyHandler, ���⴦��ĳЩ�С�
 * @author sundl
 *
 */
public class ParameterPropertyHandler extends EMFPropertyHandler implements IPropertyHandler2{

	private IARESProject project;
	
	/**
	 * @param feature
	 */
	public ParameterPropertyHandler(EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public String getValue(Object obj) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.IPropertyHandler2#setProject(com.hundsun.ares.studio.core.IARESProject)
	 */
	@Override
	public void setProject(IARESProject project) {
		this.project = project;
	}
	
}
