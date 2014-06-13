/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.reference;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.model.reference.RelationInfo;

/**
 * @author gongyf
 *
 */
public class ReferenceUtils {
	
//	/**
//	 * ͨ���������������ͺ�����������һ����Χ���ҵ�������ÿ��ܵ������ռ�<BR>
//	 * ��Ϊ���������ͺ����������ظ���ʱ�����Բ�ʹ�������ռ�
//	 * 
//	 * @param project
//	 * @param refType
//	 * @param refName
//	 * @param useRequiredProjects
//	 * @return
//	 */
//	public static String getPossibleNamespace(IARESProject project, String refType, String refName, boolean useRequiredProjects) {
//		List<ReferenceInfo> infoList = ReferenceManager.getInstance().getReferenceInfos(project, refType, refName, useRequiredProjects);
//		if (infoList.size() > 0) {
//			return infoList.get(0).getRefNamespace();
//		}
//		return StringUtils.EMPTY;
//	}
	
	/**
	 * ����һ����Դ�б���Щ��Դ�������˲�����Դ������
	 * 
	 * @param resource
	 * @return
	 */
	public static List<IARESResource> getRefResources(IARESResource resource) {
		List<IARESResource> result = new ArrayList<IARESResource>();
		
		// �����Ǹ��ݵ�ǰ�����½��еģ��޷���֪֮ǰ�����ù�ϵ
		List<ReferenceInfo> infoList = ViewerUtils.getReferenceInfos(ReferenceManager.getInstance().getRefTableViewer().getTable(), resource);
		for (ReferenceInfo info : infoList) {
			
			List<RelationInfo> rels = ReferenceManager.getInstance().getRelationInfoByTarget(info.getRefType(), info.getRefName(), info.getRefNamespace(), resource.getARESProject());
			for (RelationInfo rel : rels) {
				result.add(rel.getHostResource());
			}
		}
		
		return result;
	}
	
}
