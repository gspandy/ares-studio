/**
 * Դ�������ƣ�StdFieldSetUserPropertyCommand.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata.util;

import com.hundsun.ares.studio.core.model.extendable.ExtensibleModelUtil;
import com.hundsun.ares.studio.core.util.log.Log;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;

public class StdFieldSetUserPropertyCommand extends AbstractMetadataModifyCommand {
	
	String key;
	String value;
	
	public StdFieldSetUserPropertyCommand(String id, String key, String value) {
		super(id);
		this.key = key;
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.metadata.util.MetadataModifyOperation.MetadataModifyCommand#excute(com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData, com.hundsun.ares.studio.core.util.log.Log)
	 */
	@Override
	public void excute(MetadataItem data, Log log) {
		ExtensibleModelUtil.setUserExtendedProperty(data, key, value);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.metadata.util.IMetadataModifyCommand#getDescription()
	 */
	@Override
	public String getDescription() {
		return String.format("�����û�����'%s'Ϊ'%s'", key, value);
	}
	
}