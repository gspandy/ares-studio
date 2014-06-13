/**
 * Դ�������ƣ�TableSpaceScriptWrapImpl.java
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
import com.hundsun.ares.studio.jres.model.database.oracle.OracleSpaceResourceData;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpace;
import com.hundsun.ares.studio.jres.script.api.database.ITableSpaceItemScriptWrap;
import com.hundsun.ares.studio.jres.script.api.database.ITableSpaceScriptWrap;
import com.hundsun.ares.studio.jres.script.base.ResourceWrapBase;

/**
 * @author yanwj06282
 *
 */
public class TableSpaceScriptWrapImpl extends ResourceWrapBase<OracleSpaceResourceData> implements ITableSpaceScriptWrap{

	public TableSpaceScriptWrapImpl(IARESResource resource) {
		super(resource);
	}

	public ITableSpaceItemScriptWrap[] getSpace(){
		List<ITableSpaceItemScriptWrap> spaces = new ArrayList<ITableSpaceItemScriptWrap>();
		for (TableSpace space : getOriginalInfo().getSpaces()){
			spaces.add(new TableSpaceItemScriptWrapImpl(space , resource));
		}
		return spaces.toArray(new ITableSpaceItemScriptWrap[spaces.size()]);
	}

	@Override
	public Class<OracleSpaceResourceData> getInfoClass() {
		return OracleSpaceResourceData.class;
	}
	
}
