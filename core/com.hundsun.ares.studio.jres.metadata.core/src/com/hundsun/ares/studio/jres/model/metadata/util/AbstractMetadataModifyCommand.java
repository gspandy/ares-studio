/**
 * Դ�������ƣ�AbstractMetadataModifyCommand.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.model.metadata.util;

/**
 * @author sundl
 *
 */
public abstract class AbstractMetadataModifyCommand implements IMetadataModifyCommand{

	private String id;
	
	public AbstractMetadataModifyCommand(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
}
