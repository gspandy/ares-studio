/**
 * Դ�������ƣ�TableIndexColumnScriptWrapImpl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse.core.script.impl;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.database.TableIndexColumn;
import com.hundsun.ares.studio.jres.script.api.database.ITableIndexColumnScriptWrap;
import com.hundsun.ares.studio.jres.script.base.CommonScriptWrap;

/**
 * @author yanwj06282
 *
 */
public class TableIndexColScriptWrapImpl extends CommonScriptWrap<TableIndexColumn> implements
		ITableIndexColumnScriptWrap {

	public TableIndexColScriptWrapImpl(TableIndexColumn indexColumn , IARESResource resource) {
		super(indexColumn ,resource);
	}

	@Override
	public String getType() {
		return null;
	}
	
	@Override
	public String getName() {
		return getOriginalInfo().getColumnName();
	}

}
