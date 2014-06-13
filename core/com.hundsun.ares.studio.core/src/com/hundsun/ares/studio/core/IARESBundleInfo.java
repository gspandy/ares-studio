/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import com.hundsun.ares.studio.core.model.extendable.IExtendAbleModel;

/**
 * 
 * @author sundl
 */
public interface IARESBundleInfo extends IExtendAbleModel {

	/**
	 * id
	 * @return
	 */
	String getId();
	
	/**
	 * ����
	 * @return
	 */
	String getName();
	
	/**
	 * �汾��
	 * @return
	 */
	String getVersion();
	
	/**
	 * �ṩ��(������)�����֣���ѡ��
	 * @return
	 */
	String getProvider();
	String getNote();
	String getPublisher();
	String getPubTime();
	
	String getString(String name);
	String getString(String name, String defaultValue);
	int getInt(String name);
	int getInt(String name, int defaultValue);
	boolean getBoolean(String name);
	boolean getBoolean(String name, boolean defaultValue);
	
	void setValue(String name, String value);
	void setValue(String name, int value);
	void setValue(String name, boolean value);
	
	/**
	 * ��ȡ�������б�
	 * @return
	 */
	IDependenceDescriptor[] getDependencies();

	public abstract String getContact();
}
