/**
 * Դ�������ƣ�MetadataReferenceParser.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata.util;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.hundsun.ares.studio.core.model.Reference;
import com.hundsun.ares.studio.core.model.impl.TextAttributeReferenceWithNamespaceImpl;
import com.hundsun.ares.studio.core.model.util.IReferenceParser;

/**
 * @author gongyf
 *
 */
public class MetadataReferenceParser implements IReferenceParser {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.util.IReferenceParser#analyse(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature, java.lang.String[])
	 */
	@Override
	public List<Reference> analyse(EObject object, EStructuralFeature feature,
			String[] parameters) {
		if (parameters != null && parameters.length == 1) {
			if (feature instanceof EAttribute) {
				if (feature.getEType() == EcorePackage.Literals.ESTRING) {
					return Collections.singletonList(
							(Reference)new TextAttributeReferenceWithNamespaceImpl(
									parameters[0], object, (EAttribute)feature)) ;
				} else {
					System.out.println("��������ʱָ�������Բ����ı�����");
				}
				
			} else {
				System.out.println("��������ʱָ�������Բ�������");
			}
			
		} else {
			System.out.println("��������ʱ������Ŀ����ȷ");
		}
		return Collections.emptyList();
	}

}
