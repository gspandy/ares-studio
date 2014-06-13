/**
 * Դ�������ƣ�OracleTriggerAdapter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���С��
 */
package com.hundsun.ares.studio.jres.database.oracle.internal.service;

import com.hundsun.ares.studio.jres.database.oracle.service.IOracleTrigger;
import com.hundsun.ares.studio.jres.model.database.oracle.TriggerResourceData;

/**
 * @author wangxh
 *
 */
public class OracleTriggerAdapter implements IOracleTrigger {

	protected final TriggerResourceData triggerResourceData;
	
	
	public OracleTriggerAdapter(TriggerResourceData triggerResourceData) {
		super();
		this.triggerResourceData = triggerResourceData;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleTrigger#getName()
	 */
	@Override
	public String getName() {
		return triggerResourceData.getName();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleTrigger#getChineseName()
	 */
	@Override
	public String getChineseName() {
		return triggerResourceData.getChineseName();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleTrigger#getDescription()
	 */
	@Override
	public String getDescription() {
		return triggerResourceData.getDescription();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleTrigger#getSQL()
	 */
	@Override
	public String getSQL() {
		return triggerResourceData.getSql();
	}

}
