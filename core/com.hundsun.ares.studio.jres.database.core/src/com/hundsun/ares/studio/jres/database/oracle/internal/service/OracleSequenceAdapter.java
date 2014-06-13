/**
 * Դ�������ƣ�OracleSequenceAdapter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���С��
 */
package com.hundsun.ares.studio.jres.database.oracle.internal.service;

import com.hundsun.ares.studio.jres.database.oracle.service.IOracleSequence;
import com.hundsun.ares.studio.jres.model.database.oracle.SequenceResourceData;

/**
 * @author wangxh
 *
 */
public class OracleSequenceAdapter implements IOracleSequence {

	protected final SequenceResourceData sequenceResourceData;
	
	
	public OracleSequenceAdapter(SequenceResourceData sequenceResourceData) {
		super();
		this.sequenceResourceData = sequenceResourceData;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleSequence#getTableName()
	 */
	@Override
	public String getTableName() {
		return sequenceResourceData.getTableName();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleSequence#getStart()
	 */
	@Override
	public String getStart() {
		return sequenceResourceData.getStart();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleSequence#getIncrement()
	 */
	@Override
	public String getIncrement() {
		return sequenceResourceData.getIncrement();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleSequence#getMinValue()
	 */
	@Override
	public String getMinValue() {
		return sequenceResourceData.getMinValue();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleSequence#getMaxValue()
	 */
	@Override
	public String getMaxValue() {
		return sequenceResourceData.getMaxValue();
	}


	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleSequence#getCache()
	 */
	@Override
	public String getCache() {
		return sequenceResourceData.getCache();
	}

	@Override
	public String getName() {
		return sequenceResourceData.getName();
	}

	@Override
	public String getChineseName() {
		return sequenceResourceData.getChineseName();
	}

	@Override
	public String getDescription() {
		return sequenceResourceData.getDescription();
	}

	@Override
	public boolean isCycle() {
		return sequenceResourceData.isCycle();
	}

	@Override
	public boolean isUseCache() {
		return sequenceResourceData.isUseCache();
	}

}
