/**
 * Դ�������ƣ�OracleServiceFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.oracle.internal.service;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.service.IDataService;
import com.hundsun.ares.studio.core.service.IDataServiceFactory;

/**
 * @author gongyf
 *
 */
public class OracleServiceFactory implements IDataServiceFactory {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.core.service.IDataServiceFactory#createService(com.hundsun.ares.studio.jres.core.service.IARESProject)
	 */
	@Override
	public IDataService createService(IARESProject project) {
		return new BasicOracleDBService(project);
	}

}
