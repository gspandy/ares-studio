/**
 * Դ�������ƣ�TableSpaceRelationAdapter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���С��
 */
package com.hundsun.ares.studio.jres.database.oracle.internal.service;

import com.hundsun.ares.studio.jres.database.oracle.service.ITableSpaceRelation;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpaceRelation;

/**
 * @author wangxh
 *
 */
public class TableSpaceRelationAdapter implements ITableSpaceRelation {

	protected final TableSpaceRelation tableSpaceRelation;
	
	
	public TableSpaceRelationAdapter(TableSpaceRelation tableSpaceRelation) {
		super();
		this.tableSpaceRelation = tableSpaceRelation;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.ITableSpaceRelation#getMainSpace()
	 */
	@Override
	public String getMainSpace() {
		return tableSpaceRelation.getMainSpace();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.ITableSpaceRelation#getIndexSpace()
	 */
	@Override
	public String getIndexSpace() {
		return tableSpaceRelation.getIndexSpace();
	}

}
