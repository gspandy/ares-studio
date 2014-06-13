/**
 * Դ�������ƣ�ServiceReferenceProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.service.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.service.reference;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.BasicEList;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.constants.IBizRefType;
import com.hundsun.ares.studio.biz.core.BizUtil;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.model.IReferenceProvider2;
import com.hundsun.ares.studio.core.model.Reference;
import com.hundsun.ares.studio.core.model.impl.TextAttributeReferenceImpl;
import com.hundsun.ares.studio.core.model.impl.TextAttributeReferenceWithNamespaceImpl;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.service.Service;

/**
 * @author sundl
 *
 */
public class ServiceReferenceProvider implements IReferenceProvider2 {
	
	public static ServiceReferenceProvider INSTANCE = new ServiceReferenceProvider();
	
	private ServiceReferenceProvider() {}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.IReferenceProvider2#getReferences(java.lang.Object, com.hundsun.ares.studio.core.IARESProject)
	 */
	@Override
	public List<Reference> getReferences(Object obj, IARESProject aresProject) {
		List<Reference> references = new ArrayList<Reference>();
		if (obj instanceof Service) {
			Service service = (Service) obj;
			BasicEList<Parameter> parametesrs = new BasicEList<Parameter>();
			parametesrs.addAll(service.getInterface().getInputParameters());//����������
			parametesrs.addAll(service.getInterface().getOutputParameters());//����������
			
			for(Parameter parametesr:parametesrs){
				Reference ref = null;
				// ���ݲ������͵Ĳ�ͬ�����õ�����Ҳ��һ��
				switch (parametesr.getParamType()) {
					case STD_FIELD:
						if (!StringUtils.isEmpty(parametesr.getId())) 
							ref = new TextAttributeReferenceWithNamespaceImpl(IMetadataRefType.StdField, parametesr, BizPackage.Literals.PARAMETER__ID);
						break;
					case OBJECT:
					case PARAM_GROUP:
						// ���ʹ�ö����׼�ֶΣ��ֶ������ö����׼�ֶ�
						if (BizUtil.hasStdObjList(aresProject)) {
							if (!StringUtils.isEmpty(parametesr.getId())) {
								ref = new TextAttributeReferenceImpl(IBizRefType.Std_Obj, parametesr, BizPackage.Literals.PARAMETER__ID);
							}
						} else 	if (!StringUtils.isEmpty(parametesr.getType())) {
							// ��ʹ�ö����׼�ֶε�����£��������ö�����Դ
							ref = new TextAttributeReferenceImpl(IBizRefType.Object, parametesr, BizPackage.Literals.PARAMETER__TYPE);
						}
						break;
					default:
						break;
				}
				
				if (ref != null)
					references.add(ref);
			}
		}
		return references;
	}

}
