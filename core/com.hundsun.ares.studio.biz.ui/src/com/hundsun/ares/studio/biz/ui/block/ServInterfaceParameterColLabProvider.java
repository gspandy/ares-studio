package com.hundsun.ares.studio.biz.ui.block;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.provider.ParameterColumnLabelProvider;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.internal.core.ARESProjectProperty;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;

/**
 * Դ�������ƣ�ServInterfaceParameterColLabProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�yanwj06282
 */

/**
 * @author yanwj06282
 *
 */
public class ServInterfaceParameterColLabProvider extends
		ParameterColumnLabelProvider {

	public ServInterfaceParameterColLabProvider(IARESResource resource,
			EStructuralFeature attribute) {
		super(resource, attribute);
	}

	@Override
	protected String getParameterDefaultValue(String defValue,
			StandardField field) {
		String dt = "c";
		try {
			dt = (String) ((ARESProjectProperty)project.getProjectProperty()).getProperties().get("real_type_to_display");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (StringUtils.isNotBlank(defValue)) {
			try {
				TypeDefaultValue tdv = MetadataServiceProvider.getTypeDefaultValueByName(resource.getARESProject(), defValue);
				if (tdv != null) {
					return tdv.getData().get(dt.toLowerCase());
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}else {
			try {
				TypeDefaultValue tdv = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName(project, field.getName());
				if (tdv != null) {
					if (StringUtils.isNotBlank(dt)) {
						return tdv.getData().get(dt.toLowerCase());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return defValue;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.provider.ParameterColumnLabelProvider#getNonStdParameterDefaultValue(java.lang.String, com.hundsun.ares.studio.biz.Parameter)
	 */
	@Override
	protected String getNonStdParameterDefaultValue(String defValue, Parameter p) {
		String dt = "c";
		try {
			dt = (String) ((ARESProjectProperty)project.getProjectProperty()).getProperties().get("real_type_to_display");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (StringUtils.isNotBlank(defValue)) {
			try {
				TypeDefaultValue tdv = MetadataServiceProvider.getTypeDefaultValueByName(resource.getARESProject(), defValue);
				if (tdv != null) {
					return tdv.getData().get(dt.toLowerCase());
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}else {
			try {
				TypeDefaultValue tdv = MetadataServiceProvider.getTypeDefaultValueOfBizTypeByName(project, p.getType());
				if (tdv != null) {
					if (StringUtils.isNotBlank(dt)) {
						return tdv.getData().get(dt.toLowerCase());
					}
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		return defValue;
	}
	
}
