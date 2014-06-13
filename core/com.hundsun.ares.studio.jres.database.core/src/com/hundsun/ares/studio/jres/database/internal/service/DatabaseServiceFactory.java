/**
 * Դ�������ƣ�DatabaseServiceFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.internal.service;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.service.IDataService;
import com.hundsun.ares.studio.core.service.IDataServiceFactory;

/**
 * @author gongyf
 *
 */
public class DatabaseServiceFactory implements IDataServiceFactory {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.core.service.IDataServiceFactory#createService(com.hundsun.ares.studio.core.IARESProject)
	 */
	@Override
	public IDataService createService(IARESProject project) {
		return new BasicDatabaseService(project);
	}

}
