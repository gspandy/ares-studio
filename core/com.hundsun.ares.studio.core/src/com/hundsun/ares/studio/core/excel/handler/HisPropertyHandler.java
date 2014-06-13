/**
 * Դ�������ƣ�HisPropertyHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.core.model.RevisionHistory;

/**
 * 
 * @author sundl
 */
public class HisPropertyHandler extends EMFPropertyHandler {

	/**
	 * @param feature
	 */
	public HisPropertyHandler(EStructuralFeature feature) {
		super(feature);
	}
	
	@Override
	public void setValue(Object obj, String value) {
		// �������EMF����ʲô������
//		if (obj instanceof EObject) {
//			((EObject) obj).eSet(feature, value);
//		}
	}

	@Override
	public String getValue(Object obj) {
		if (obj instanceof EObject) {
			List<RevisionHistory> histories = (List<RevisionHistory>) ((EObject) obj).eGet(feature);
			if (histories != null) {
				StringBuilder builder = new StringBuilder();
				for (RevisionHistory his : histories) {
					String version = his.getVersion();
					if (StringUtils.isNotEmpty(version)) {
						version = StringUtils.startsWithIgnoreCase(version, "v") ? version : "V" + version;
					}
					String str = StringUtils.join(new String[] {
								version, 
								his.getModifiedDate(), 
								his.getModifiedBy() + "����",
								his.getCharger() + "�޸�",
								his.getModified()}, " ");
					builder.append(str);
					builder.append("\n");
				}
				return builder.toString();
			}
		}
		return null;
	}

}
