/**
 * Դ�������ƣ�OracleTableSpaceAdapter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���С��
 */
package com.hundsun.ares.studio.jres.database.oracle.internal.service;

import com.hundsun.ares.studio.jres.database.oracle.service.ITableSpace;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpace;

/**
 * @author wangxh
 *
 */
public class TableSpaceAdapter implements ITableSpace {

	protected final TableSpace tableSpace;
	
	
	public TableSpaceAdapter(TableSpace tableSpace) {
		super();
		this.tableSpace = tableSpace;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleTableSpace#getName()
	 */
	@Override
	public String getName() {
		return tableSpace.getName();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleTableSpace#getChineseName()
	 */
	@Override
	public String getChineseName() {
		return tableSpace.getChineseName();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleTableSpace#getUser()
	 */
	@Override
	public String getUser() {
		return tableSpace.getUser();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleTableSpace#getFile()
	 */
	@Override
	public String getFile() {
		return tableSpace.getFile();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleTableSpace#getSize()
	 */
	@Override
	public String getSize() {
		return tableSpace.getSize();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleTableSpace#getDescription()
	 */
	@Override
	public String getDescription() {
		return tableSpace.getDescription();
	}

}
