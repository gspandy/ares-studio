/**
 * Դ�������ƣ�ViewAdapter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���С��
 */
package com.hundsun.ares.studio.jres.database.internal.service;

import com.hundsun.ares.studio.jres.database.service.IView;
import com.hundsun.ares.studio.jres.model.database.ViewResourceData;

/**
 * @author wangxh
 *
 */
public class ViewAdapter implements IView {

	final protected ViewResourceData view;
	
	
	public ViewAdapter(ViewResourceData view) {
		super();
		this.view = view;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.service.IView#getName()
	 */
	@Override
	public String getName() {
		return view.getName();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.service.IView#getChineseName()
	 */
	@Override
	public String getChineseName() {
		return view.getChineseName();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.service.IView#getDescription()
	 */
	@Override
	public String getDescription() {
		return view.getDescription();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.service.IView#getSQL()
	 */
	@Override
	public String getSQL() {
		return view.getSql();
	}

}
