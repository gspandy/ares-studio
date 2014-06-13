/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.core;

import org.apache.log4j.Logger;

import com.hundsun.ares.studio.core.model.JRESResourceInfo;

/**
 * @author gongyf
 *
 */
public class ARESResourceObjectProvider implements IObjectProvider {
	
	private static final Logger logger = Logger.getLogger(ARESResourceObjectProvider.class);

	private ARESResourceObjectProvider() {
		
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.reference.IObjectProvider#getObject(com.hundsun.ares.studio.core.IARESResource)
	 */
	@Override
	public Object getObject(IARESResource resource) {
		try {
			return resource.getInfo(JRESResourceInfo.class);
		} catch (Exception e) {
			logger.error(String.format("������Ϣ��ȡ��Դ%s����info��ʱ�����...", resource.getElementName()), e);
		}
		return null;
	}
	
	public static final ARESResourceObjectProvider INSTANCE = new ARESResourceObjectProvider();
}
