/**
 * Դ�������ƣ�TableSpaceItemScriptWrapImpl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse.core.script.impl;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpace;
import com.hundsun.ares.studio.jres.script.api.database.ITableSpaceItemScriptWrap;
import com.hundsun.ares.studio.jres.script.base.CommonScriptWrap;

/**
 * @author yanwj06282
 *
 */
public class TableSpaceItemScriptWrapImpl extends CommonScriptWrap<TableSpace> implements
		ITableSpaceItemScriptWrap {

	public TableSpaceItemScriptWrapImpl(TableSpace space , IARESResource resource) {
		super(space ,resource);
	}

}
