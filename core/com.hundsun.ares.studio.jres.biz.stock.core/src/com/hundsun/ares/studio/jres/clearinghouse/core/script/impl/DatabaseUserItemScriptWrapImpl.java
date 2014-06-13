/**
 * Դ�������ƣ�DatabaseUserItemScriptWrapImpl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse.core.script.impl;

import java.util.ArrayList;
import java.util.List;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.database.oracle.OraclePrivilege;
import com.hundsun.ares.studio.jres.model.database.oracle.OracleUser;
import com.hundsun.ares.studio.jres.script.api.database.IDatabaseUserItemScriptWrap;
import com.hundsun.ares.studio.jres.script.api.database.IDatabaseUserPrivilegesScriptWrap;
import com.hundsun.ares.studio.jres.script.base.CommonScriptWrap;

/**
 * @author yanwj06282
 *
 */
public class DatabaseUserItemScriptWrapImpl extends CommonScriptWrap<OracleUser> implements
		IDatabaseUserItemScriptWrap {

	public DatabaseUserItemScriptWrapImpl(OracleUser user ,IARESResource resource) {
		super(user ,resource);
	}

	@Override
	public boolean isEnable() {
		return getOriginalInfo().isEnable();
	}
	
	@Override
	public IDatabaseUserPrivilegesScriptWrap[] getPrivileges() {
		List<IDatabaseUserPrivilegesScriptWrap> userItems = new ArrayList<IDatabaseUserPrivilegesScriptWrap>();
		for(OraclePrivilege privileges : getOriginalInfo().getPrivileges()){
			userItems.add(new DatabaseUserPrivilegesScriptWrapImpl(privileges,resource));
		}
		
		return userItems.toArray(new IDatabaseUserPrivilegesScriptWrap[userItems.size()]);
	}

	@Override
	public String getAttributes() {
		return getOriginalInfo().getAttributes();
	}

	@Override
	public String getDescription() {
		return getOriginalInfo().getDecription();
	}

	@Override
	public String getPassword() {
		return getOriginalInfo().getPassword();
	}
	
	@Override
	public String getDefaultTableSpace() {
		return getOriginalInfo().getDefaultTableSpace();
	}
	
}
