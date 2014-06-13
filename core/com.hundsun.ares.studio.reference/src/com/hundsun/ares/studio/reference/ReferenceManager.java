/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.reference;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.model.reference.RelationInfo;
import com.hundsun.ares.studio.reference.internal.ReferenceTableViewer;
import com.hundsun.ares.studio.reference.internal.RelationTableViewer;

/**
 * ����ͳһ�������������Ϣ
 * @author gongyf
 *
 */
public class ReferenceManager {
	
	private ReferenceManager() {
	}
	private static ReferenceManager instance;
	
	/**
	 * ��ȡ���ù��������
	 * @return
	 */
	public synchronized static ReferenceManager getInstance() {
		if (instance == null) {
			instance = new ReferenceManager();
		}
		return instance;
	}
	
	private ReferenceTableViewer refTableViewer = new ReferenceTableViewer();
	private RelationTableViewer relTableViewer = new RelationTableViewer();
	
	/**
	 * @return the refTableViewer
	 */
	ReferenceTableViewer getRefTableViewer() {
		return refTableViewer;
	}
	
	/**
	 * @return the relTableViewer
	 */
	RelationTableViewer getRelTableViewer() {
		return relTableViewer;
	}
	
	/**
	 * @param project
	 * @param refType
	 * @param refName
	 * @param refNamespace
	 * @param useRequiredProjects
	 * @return
	 * @see com.hundsun.ares.studio.reference.internal.ReferenceTableViewer#getReferenceInfos(com.hundsun.ares.studio.core.IARESProject, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public List<ReferenceInfo> getReferenceInfos(IARESProject project,
			String refType, String refName, String refNamespace,
			boolean useRequiredProjects) {
		return refTableViewer.getReferenceInfos(project, refType, refName,
				refNamespace, useRequiredProjects);
	}



	/**
	 * @param project
	 * @param refType
	 * @param refName
	 * @param useRequiredProjects
	 * @return
	 * @see com.hundsun.ares.studio.reference.internal.ReferenceTableViewer#getReferenceInfos(com.hundsun.ares.studio.core.IARESProject, java.lang.String, java.lang.String, boolean)
	 */
	public List<ReferenceInfo> getReferenceInfos(IARESProject project,
			String refType, String refName, boolean useRequiredProjects) {
		return refTableViewer.getReferenceInfos(project, refType, refName,
				useRequiredProjects);
	}
	/**
	 * �����������׸�����
	 * @param project
	 * @param refType
	 * @param refName
	 * @param useRequiredProjects
	 * @return
	 */
	public ReferenceInfo getFirstReferenceInfo(IARESProject project,String refType, String refName,boolean useRequiredProjects) {
		List<ReferenceInfo> infos = refTableViewer.getFirstReferenceInfos(project, refType, refName,StringUtils.EMPTY,useRequiredProjects);
		if (infos.isEmpty()) {
			return null;
		}
		return infos.get(0);
	}

	/**
	 * �����������׸�����
	 * 
	 * @param project
	 * @param refType
	 * @param refName
	 * @param refNamespace
	 * @param useRequiredProjects
	 * @return
	 */
	public ReferenceInfo getFirstReferenceInfo(IARESProject project,String refType, String refName, String refNamespace,boolean useRequiredProjects) {
		List<ReferenceInfo> infos = refTableViewer.getFirstReferenceInfos(project, refType, refName,refNamespace,useRequiredProjects);
		if (infos.isEmpty()) {
			return null;
		}
		return infos.get(0);
	}
	/**
	 * @param project
	 * @param refType
	 * @param useRequiredProjects
	 * @return
	 * @see com.hundsun.ares.studio.reference.internal.ReferenceTableViewer#getReferenceInfos(com.hundsun.ares.studio.core.IARESProject, java.lang.String, boolean)
	 */
	public List<ReferenceInfo> getReferenceInfos(IARESProject project,
			String refType, boolean useRequiredProjects) {
		return refTableViewer.getReferenceInfos(project, refType,
				useRequiredProjects);
	}




	/**
	 * @param refType
	 * @param refName
	 * @param project
	 * @return
	 * @see com.hundsun.ares.studio.reference.internal.RelationTableViewer#getRelationInfoByTarget(java.lang.String, java.lang.String, com.hundsun.ares.studio.core.IARESProject)
	 */
	public List<RelationInfo> getRelationInfoByTarget(String refType,
			String refName, IARESProject project) {
		return relTableViewer
				.getRelationInfoByTarget(refType, refName, project);
	}




	/**
	 * @param refType
	 * @param refName
	 * @param refNamespace
	 * @param project
	 * @return
	 * @see com.hundsun.ares.studio.reference.internal.RelationTableViewer#getRelationInfoByTarget(java.lang.String, java.lang.String, java.lang.String, com.hundsun.ares.studio.core.IARESProject)
	 */
	public List<RelationInfo> getRelationInfoByTarget(String refType,
			String refName, String refNamespace, IARESProject project) {
		return relTableViewer.getRelationInfoByTarget(refType, refName,
				refNamespace, project);
	}


	/**
	 * @param resource
	 * @return
	 * @see com.hundsun.ares.studio.reference.internal.ReferenceTableViewer#getReferenceInfos(com.hundsun.ares.studio.core.IARESResource)
	 */
	public List<ReferenceInfo> getReferenceInfos(IARESResource resource) {
		return refTableViewer.getReferenceInfos(resource);
	}

	

//	/**
//	 * ���ҳ�ָ�����������õ�������Ϣ
//	 * 
//	 * @param path һ�������ռ�����·��
//	 * @param location ��λ�ַ���������Ϊnull
//	 * @return
//	 */
//	public List<RelationInfo> getRelationInfoBySource(String path, String location) {
//		return null;
//	}
//	
//	/**
//	 * ͨ�������õ���Ϣ���ҳ�ʹ������Щ������Ϣ�����ù�ϵ
//	 * 
//	 * @param refType ����Ϊnull
//	 * @param refNamespace ����Ϊnull
//	 * @param refName ����Ϊnull
//	 * @return
//	 */
//	public List<RelationInfo> getRelationInfoByTarget(String refType, String refNamespace, String refName) {
//		return null;
//	}
}
