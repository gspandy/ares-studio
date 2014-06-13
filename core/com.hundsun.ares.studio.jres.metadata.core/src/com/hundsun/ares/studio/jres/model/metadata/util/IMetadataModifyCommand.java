/**
 * Դ�������ƣ�IMetadataModifyCommand.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.model.metadata.util;

import com.hundsun.ares.studio.core.util.log.Log;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;

/**
 * һ��command ����һ����Ŀ
 * @author sundl
 *
 */
public interface IMetadataModifyCommand {
	
	/**
	 * command ����һ����Ŀ�� �������ȷ��Ҫ��������
	 * @return
	 */
	String getId();
	
	/**
	 * ��������������Ϣ
	 * @return
	 */
	String getDescription();

	void excute(MetadataItem data, Log log);
}
