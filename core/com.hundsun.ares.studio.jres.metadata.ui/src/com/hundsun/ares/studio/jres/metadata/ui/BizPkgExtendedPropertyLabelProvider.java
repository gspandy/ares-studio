/**
 * Դ�������ƣ�BizPkgExtendedPropertyLabelProvidedr.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.metadata.ui;

import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil;
import com.hundsun.ares.studio.ui.editor.extend.ExtendsLabelProvider;

/**
 * @author sundl
 *
 */
public class BizPkgExtendedPropertyLabelProvider extends ExtendsLabelProvider{
	
	private IARESProject project;

	public BizPkgExtendedPropertyLabelProvider(String key ,EStructuralFeature feature, String extendModelKey) {
		super(key, feature, extendModelKey);
	}
	
	@Override
	public String getText(Object element) {
		String id = super.getText(element);
		MetadataUtil.getBizPropertyConfig(id, project);
		return id;
	}
	
}
