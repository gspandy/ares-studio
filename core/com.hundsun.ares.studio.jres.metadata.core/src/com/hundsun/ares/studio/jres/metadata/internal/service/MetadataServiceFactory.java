/**
 * Դ�������ƣ�MetadataServiceFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.metadata.internal.service;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.service.IDataService;
import com.hundsun.ares.studio.core.service.IDataServiceFactory;

/**
 * @author gongyf
 *
 */
public class MetadataServiceFactory implements IDataServiceFactory {

	/**
	 * 
	 */
	public MetadataServiceFactory() {
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.core.service.IDataServiceFactory#createService(com.hundsun.ares.studio.core.IARESProject)
	 */
	@Override
	public IDataService createService(IARESProject project) {
		return new BasicMetadataService(project);
	}

}
