/**
 * Դ�������ƣ�IOracleTableIndex.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.oracle.service;

import java.util.List;

import com.hundsun.ares.studio.jres.database.service.ITableIndex;

/**
 * @author gongyf
 *
 */
public interface IOracleTableIndex extends ITableIndex {
	
	@Override
	List<? extends IOracleTableIndexColumn> getColumnList();
	
	@Override
	IOracleTableIndexColumn getColum(String name);
	
	boolean isReverse();
	
}
