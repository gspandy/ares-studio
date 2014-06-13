/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.ui.editor.viewers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * �ṩһ�ּ򵥵Ķ������ֶ��ṩ��
 * @author gongyf
 *
 */
public class MultiTypeEStructuralFeatureProvider implements
		IEStructuralFeatureProvider {

	Map<EClass, EStructuralFeature> featureMap;
	
	/**
	 * ���뱣֤2�����鳤��һ��
	 * 
	 * @param eClasses
	 * @param features
	 */
	public MultiTypeEStructuralFeatureProvider(EClass[] eClasses, EStructuralFeature[] features) {
		Assert.isNotNull(eClasses);
		Assert.isNotNull(features);
		Assert.isTrue(eClasses.length == features.length);
		
		featureMap = new HashMap<EClass, EStructuralFeature>();
		for (int i = 0; i < eClasses.length; i++) {
			featureMap.put(eClasses[i], features[i]);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.IEStructuralFeatureProvider#getFeature(java.lang.Object)
	 */
	@Override
	public EStructuralFeature getFeature(Object element) {
		if (element instanceof EObject) {
			return featureMap.get(((EObject) element).eClass());
		}
		return null;
	}

}
