/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.reference;

import java.util.List;
import java.util.Map;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.model.reference.RelationInfo;

/**
 * @author gongyf
 *
 */
public interface IRelationInfoProvider {
	
	/**
	 * ��ȡһ��ARES��Դ�ж��ⲿ���õ���Ϣ�б�
	 * @param resource
	 * @return
	 */
	List<RelationInfo> getRelationInfos(IARESResource resource, Map<Object, Object> context);
}
