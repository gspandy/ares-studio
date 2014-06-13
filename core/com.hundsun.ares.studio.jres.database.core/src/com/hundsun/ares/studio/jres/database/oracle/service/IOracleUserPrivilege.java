/**
 * Դ�������ƣ�IOracleUserPrivilege.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���С��
 */
package com.hundsun.ares.studio.jres.database.oracle.service;

import java.util.List;

/**
 * @author wangxh
 *
 */
public interface IOracleUserPrivilege {
	String getName();
	String getChineseName();
	String getDescription();
	
	public IOracleUser getUser(String name);
	public List<IOracleUser> getUserList();
	
	public IOraclePrivilege getPrivilege(String name);
	public List<IOraclePrivilege> getPrivilegeList();
}
