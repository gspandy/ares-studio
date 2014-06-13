/**
 * Դ�������ƣ�IOracleTable.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.oracle.service;

import java.util.List;

import com.hundsun.ares.studio.jres.database.service.ITable;

/**
 * @author gongyf
 *
 */
public interface IOracleTable extends ITable {
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.service.ITable#getColum(java.lang.String)
	 */
	@Override
	public IOracleTableColumn getColum(String name);
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.service.ITable#getColumnList()
	 */
	@Override
	public List<? extends IOracleTableColumn> getColumnList();
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.service.ITable#getIndex(java.lang.String)
	 */
	@Override
	public IOracleTableIndex getIndex(String name);
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.service.ITable#getIndexList()
	 */
	@Override
	public List<? extends IOracleTableIndex> getIndexList();
	
	String getSpace();
	
	String getIndexSpace();
	
//	public IOracleTableProperty getProperty();
}
