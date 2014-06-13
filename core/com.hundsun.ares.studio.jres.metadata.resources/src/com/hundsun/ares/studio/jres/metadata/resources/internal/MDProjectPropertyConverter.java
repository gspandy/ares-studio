/**
 * Դ�������ƣ�MDProjectPropertyConverter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.metadata.resources.internal;

import org.eclipse.emf.ecore.EPackage;

import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.modelconvert.EMFExtendModelConverter;

/**
 * @author gongyf
 *
 */
public class MDProjectPropertyConverter extends EMFExtendModelConverter {
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.internal.EMFExtendModelConverter#prepareEPackageRegistry()
	 */
	@Override
	protected void prepareEPackageRegistry() {
		super.prepareEPackageRegistry();
		EPackage.Registry.INSTANCE.put(MetadataPackage.eNS_URI, MetadataPackage.eINSTANCE);
	}
}
