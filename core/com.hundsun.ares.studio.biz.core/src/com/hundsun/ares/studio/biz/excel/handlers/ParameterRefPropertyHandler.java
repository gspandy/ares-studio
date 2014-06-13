/**
 * Դ�������ƣ�ParameterRefPropertyHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.excel.handlers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.constants.IBizRefType;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.reference.RefEMFPropertyHandler;

/**
 * ��������������˵����������Ϣ��RefEMFPropertyHandler�� 
 * ����ʵ������Ϊ��ʵ�����ֱ�׼�ֶ����͵Ĳ����ͷǱ�׼�ֶ����͵Ĳ������������Ҫ���ֿ���ֱ��ʹ��
 * RefEMFPropertyHandler
 * @author sundl
 *
 */
public class ParameterRefPropertyHandler extends RefEMFPropertyHandler {

	public ParameterRefPropertyHandler(EStructuralFeature targetFeature, EStructuralFeature feature) {
		super(BizPackage.Literals.PARAMETER__ID, null, targetFeature, feature);
	}
	
	protected String getRefType(EObject object) {
		if (object instanceof Parameter) {
			Parameter p = (Parameter) object;
			switch (p.getParamType()) {
			case STD_FIELD:
				return IMetadataRefType.StdField;	// ���ñ�׼�ֶ�
			case OBJECT:
				return IBizRefType.Object;			// ���ö���
			case NON_STD_FIELD:
			case COMPONENT:
				return null;
			default:
				break;
			}
		}
		return null;
	}
	
	@Override
	protected EStructuralFeature getFeature(EObject object) {
		if (object instanceof Parameter) {
			Parameter p = (Parameter) object;
			switch (p.getParamType()) {
			case OBJECT:
				EStructuralFeature feature = super.getFeature(object);
				if (feature == MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME) {
					feature = CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME;
				} else if (feature == MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION) {
					feature = CorePackage.Literals.BASIC_RESOURCE_INFO__DESCRIPTION;
				}
				return feature;
			default:
				return super.getFeature(object);
			}
		}
		return super.getFeature(object);
	}
	
}
