/**
 * Դ�������ƣ�TextAttributeReferenceImpl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.basicdata.logic.util;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;

import com.hundsun.ares.studio.core.model.impl.ReferenceImpl;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;


public class BaiscDataReferenceImpl extends ReferenceImpl {
	
	EAttribute attr;
	/**
	 * @param object
	 * @param attribute
	 */
	public BaiscDataReferenceImpl(EAttribute attr) {
		super();
		this.type = IMetadataRefType.StdField;
		this.attr = attr;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.impl.ReferenceImpl#getValue()
	 */
	@Override
	public String getValue() {
		return attr.getName();
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.core.impl.ReferenceImpl#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String value) {
		attr.setName(value);
		if(null != ((BasicExtendedMetaData.EStructuralFeatureExtendedMetaData.Holder)attr).getExtendedMetaData() ){
			((BasicExtendedMetaData.EStructuralFeatureExtendedMetaData.Holder)attr).getExtendedMetaData().setName(value);
		}
	}
	
}
