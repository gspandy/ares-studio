/**
 * Դ�������ƣ�IOracleDBService.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.oracle.service;

import java.util.List;

import com.hundsun.ares.studio.jres.database.service.IDatabaseService;

/**
 * @author gongyf
 *
 */
public interface IOracleDBService extends IDatabaseService {
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.service.IDatabaseService#getTableList()
	 */
	@Override
	List<? extends IOracleTable> getTableList();
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.service.IDatabaseService#getTable(java.lang.String)
	 */
	@Override
	IOracleTable getTable(String name);
	
	IOracleView getView(String name);
	
	List<? extends IOracleView> getViewList();
	
	
	List<? extends IOracleTrigger> getTriggerList();
	
	IOracleTrigger getTrigger(String name);
	
	List<? extends IOracleUserPrivilege> getUserList();
	
	IOracleUserPrivilege getUser(String name);
	
	List<? extends IOracleSpace> getSpaceList();
	
	IOracleSpace getSpace(String name);
	
	List<? extends IOracleSequence> getSequenceList();
	
	IOracleSequence getSequence(String name);
	
}
