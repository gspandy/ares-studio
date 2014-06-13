/**
 * Դ�������ƣ�BooleanEMFPropertyHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�dollyn
 */
package com.hundsun.ares.studio.core.excel.handler;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * @author dollyn
 *
 */
public class BooleanEMFPropertyHandler extends EMFPropertyHandler{
	
	/** get value����ֵ��style, �Ƿ���Y���񷵻�N */
	public static final int STYLE_Y_N = 1;
	/** get value����ֵ��style, �Ƿ���true���񷵻�false */
	public static final int STYLE_TRUE_FALSE= 2;
	/** get value����ֵ��style, �Ƿ��ء��ǡ����񷵻ء��� */
	public static final int STYLE_CN = 3;

	private int style = STYLE_Y_N;
	
	/**
	 * @param eAttribute
	 */
	public BooleanEMFPropertyHandler(EAttribute eAttribute, int style) {
		super(eAttribute);
		this.style = style;
	}
	
	public BooleanEMFPropertyHandler(EAttribute eAttribute) {
		super(eAttribute);
	}

	@Override
	public void setValue(Object obj, String value) {
		if (obj instanceof EObject) {
			if (StringUtils.equalsIgnoreCase(value, "y") || StringUtils.equalsIgnoreCase(value, "true") || StringUtils.equalsIgnoreCase(value, "��")) {
				((EObject) obj).eSet(feature, true);
			} else {
				((EObject) obj).eSet(feature, false);
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.IPropertyHandler#getValue()
	 */
	@Override
	public String getValue(Object obj) {
		if (obj instanceof EObject) {
			Object value = ((EObject) obj).eGet(feature);
			if (StringUtils.equalsIgnoreCase(String.valueOf(value), "true")) {
				switch (style) {
				case STYLE_Y_N:
					return "Y";
				case STYLE_TRUE_FALSE:
					return "true";
				case STYLE_CN:
					return "��";
				default:
					break;
				}
			} else {
				switch (style) {
				case STYLE_Y_N:
					return "N";
				case STYLE_TRUE_FALSE:
					return "false";
				case STYLE_CN:
					return "��";
				default:
					break;
				}
			}
		}
		return null;
	}
}
