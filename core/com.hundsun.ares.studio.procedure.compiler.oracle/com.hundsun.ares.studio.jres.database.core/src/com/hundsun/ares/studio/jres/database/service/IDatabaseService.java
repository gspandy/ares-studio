/**
 * Դ�������ƣ�IDatabaseService.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.service;

import java.util.List;

import com.hundsun.ares.studio.core.service.IDataService;

/**
 * @author gongyf
 *
 */
public interface IDatabaseService extends IDataService {
	List<? extends ITable> getTableList();
	ITable getTable(String name);
	
	List<? extends IView> getViewList();
	IView getView(String name);
}
