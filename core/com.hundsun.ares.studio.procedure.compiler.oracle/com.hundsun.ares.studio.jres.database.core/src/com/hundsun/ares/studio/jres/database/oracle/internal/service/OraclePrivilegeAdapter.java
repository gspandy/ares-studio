/**
 * Դ�������ƣ�OraclePrivilegeAdapter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���С��
 */
package com.hundsun.ares.studio.jres.database.oracle.internal.service;

import com.hundsun.ares.studio.jres.database.oracle.service.IOraclePrivilege;
import com.hundsun.ares.studio.jres.model.database.oracle.OraclePrivilege;

/**
 * @author wangxh
 *
 */
public class OraclePrivilegeAdapter implements IOraclePrivilege {

	protected final OraclePrivilege privilege;
	
	
	public OraclePrivilegeAdapter(OraclePrivilege privilege) {
		super();
		this.privilege = privilege;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOraclePrivilege#getName()
	 */
	@Override
	public String getName() {
		return privilege.getName();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOraclePrivilege#getType()
	 */
	@Override
	public String getType() {
		return privilege.getType();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOraclePrivilege#getDescription()
	 */
	@Override
	public String getDescription() {
		return privilege.getDecription();
	}

}
