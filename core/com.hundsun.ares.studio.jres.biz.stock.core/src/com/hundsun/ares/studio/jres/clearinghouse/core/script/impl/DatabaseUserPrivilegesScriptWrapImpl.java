/**
 * Դ�������ƣ�DatabaseUserPrivilegesScriptWrapImpl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse.core.script.impl;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.database.oracle.OraclePrivilege;
import com.hundsun.ares.studio.jres.script.api.database.IDatabaseUserPrivilegesScriptWrap;
import com.hundsun.ares.studio.jres.script.base.CommonScriptWrap;

/**
 * @author yanwj06282
 *
 */
public class DatabaseUserPrivilegesScriptWrapImpl extends CommonScriptWrap<OraclePrivilege>
		implements IDatabaseUserPrivilegesScriptWrap {

	public DatabaseUserPrivilegesScriptWrapImpl(OraclePrivilege privileges ,IARESResource resource) {
		super(privileges ,resource);
	}

}
