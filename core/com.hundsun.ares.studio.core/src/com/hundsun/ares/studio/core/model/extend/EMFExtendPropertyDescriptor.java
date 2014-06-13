/**
 * Դ�������ƣ�EMFExtendPropertyDescriptor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.model.extend;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.core.model.extendable.ExtensibleModelUtil;

/**
 * ����EMF ���Ե���չ����
 * @author sundl
 */
public class EMFExtendPropertyDescriptor extends BasicExtendPropertyDescriptor implements IEMFExtendPropertyDescriptor{

	private String key;
	private EStructuralFeature feature;
	
	public EMFExtendPropertyDescriptor(String key, EStructuralFeature feature) {
		this.key = key;
		this.feature = feature;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.extend.IBasicExtendPropertyDescriptor#getValue()
	 */
	@Override
	public String getValue(ExtensibleModel model) {
		Object obj = model.getData2().get(key);
		if (obj instanceof EObject) {
			EObject eObj = (EObject) obj;
			Object result = eObj.eGet(feature);
			return String.valueOf(result);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.extend.IBasicExtendPropertyDescriptor#setValue(java.lang.String)
	 */
	@Override
	public void setValue(ExtensibleModel model, String value) {
		Object obj = model.getData2().get(key);
//		 TODO obj=null��δ���
//		if (obj == null) {
//			
//		}
		if (obj instanceof EObject) {
			EObject eObj = (EObject) obj;
			Object v = ExtensibleModelUtil.convert(value, feature.getEType().getInstanceClass());
			eObj.eSet(feature, v);
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.extend.IEMFExtendPropertyDescriptor#getStructuralFeature()
	 */
	@Override
	public EStructuralFeature getStructuralFeature() {
		return feature;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.extend.IEMFExtendPropertyDescriptor#getKey()
	 */
	@Override
	public String getKey() {
		return key;
	}

}
