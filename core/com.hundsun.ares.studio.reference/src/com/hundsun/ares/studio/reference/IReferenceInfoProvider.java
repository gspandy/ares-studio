/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.reference;

import java.util.List;
import java.util.Map;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;

/**
 * @author gongyf
 *
 */
public interface IReferenceInfoProvider {
	
	/**
	 * ��һ��ARES��Դת��Ϊ������Ϣ
	 * @param resource
	 * @return
	 */
	List<ReferenceInfo> getReferenceInfos(IARESResource resource, Map<Object, Object> context);
}
