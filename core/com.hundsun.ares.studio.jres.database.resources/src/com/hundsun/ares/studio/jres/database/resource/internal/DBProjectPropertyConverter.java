/**
 * Դ�������ƣ�DBProjectPropertyConverter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.resource.internal;

import org.eclipse.emf.ecore.EPackage;

import com.hundsun.ares.studio.jres.model.database.DatabasePackage;
import com.hundsun.ares.studio.jres.modelconvert.EMFExtendModelConverter;

/**
 * @author gongyf
 *
 */
public class DBProjectPropertyConverter extends EMFExtendModelConverter {
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.internal.EMFExtendModelConverter#prepareEPackageRegistry()
	 */
	@Override
	protected void prepareEPackageRegistry() {
		super.prepareEPackageRegistry();
		EPackage.Registry.INSTANCE.put(DatabasePackage.eNS_URI, DatabasePackage.eINSTANCE);
	}
}
