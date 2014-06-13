/**
 * Դ�������ƣ�IOracleUser.java
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
public interface IOracleUser {

	String getName();
	String getDecription();
	String getAttributes();
	
	boolean isEnable();
	
	List<? extends IOraclePrivilege> getPrivilegeList();
	IOraclePrivilege getPrivilege(String name);
}
