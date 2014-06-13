package com.hundsun.ares.studio.procedure.ui.editor.page.block;
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
public class ProcedureInterfaceParameterColLabProvider extends
		ParameterColumnLabelProvider {

	private static String DEFAULT_TYPE = "oracle";
	
	public ProcedureInterfaceParameterColLabProvider(IARESResource resource,
			EStructuralFeature attribute) {
		super(resource, attribute);
	}

	@Override
	protected String getParameterDefaultValue(String defValue,
			StandardField field) {
		try {
			String dt = (String) ((ARESProjectProperty)project.getProjectProperty()).getProperties().get("tabledir");
			int _index = -1 ;
			int dotIndex = -1;
			if((_index=StringUtils.lastIndexOf(dt,"_" ))>-1  && (dotIndex=StringUtils.lastIndexOf(dt,"."))>-1 ){
				dt = StringUtils.substring(dt,_index+1, dotIndex).toLowerCase();
			}else{
				dt = DEFAULT_TYPE;
			}
			if (StringUtils.isBlank(defValue)) {
					TypeDefaultValue tdv = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName(project, field.getName());
					if (tdv != null) {
						if (StringUtils.isNotBlank(dt)) {
							return tdv.getData().get(dt.toLowerCase());
						}
					}
			}else {
				TypeDefaultValue tdv = MetadataServiceProvider.getTypeDefaultValueByName(project, defValue);
				if (tdv != null) {
					if (StringUtils.isNotBlank(dt)) {
						return tdv.getData().get(dt.toLowerCase());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defValue;
	}
	
	@Override
	protected String getNonStdParameterDefaultValue(String defValue, Parameter p) {
		try {
			String dt = (String) ((ARESProjectProperty)project.getProjectProperty()).getProperties().get("tabledir");
			int _index = -1 ;
			int dotIndex = -1;
			if((_index=StringUtils.lastIndexOf(dt,"_" ))>-1  && (dotIndex=StringUtils.lastIndexOf(dt,"."))>-1 ){
				dt = StringUtils.substring(dt,_index+1, dotIndex).toLowerCase();
			}else{
				dt = DEFAULT_TYPE;
			}
			if (StringUtils.isBlank(defValue)) {
					TypeDefaultValue tdv = MetadataServiceProvider.getTypeDefaultValueOfBizTypeByName(project, p.getType());
					if (tdv != null) {
						if (StringUtils.isNotBlank(dt)) {
							return tdv.getData().get(dt.toLowerCase());
						}
					}
			}else {
				TypeDefaultValue tdv = MetadataServiceProvider.getTypeDefaultValueByName(project, defValue);
				if (tdv != null) {
					if (StringUtils.isNotBlank(dt)) {
						return tdv.getData().get(dt.toLowerCase());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defValue;
	}
	
}
