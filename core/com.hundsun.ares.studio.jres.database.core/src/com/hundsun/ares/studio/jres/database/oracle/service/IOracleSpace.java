/**
 * Դ�������ƣ�IOracleSpace.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.oracle.service;

import java.util.List;

/**
 * @author gongyf
 *
 */
public interface IOracleSpace {

	String getName();
	String getChineseName();
	String getDescription();
	
	
	List<? extends ITableSpace> getTableSpaceList();
	ITableSpace getTableSpace(String name);
	
	List<? extends ITableSpaceRelation> getTableSpaceRelationList();
	ITableSpaceRelation getTableSpaceRelation(String name);
}
