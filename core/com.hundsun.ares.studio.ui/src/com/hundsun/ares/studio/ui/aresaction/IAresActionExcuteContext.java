/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.aresaction;

import com.hundsun.ares.studio.core.IARESResource;

/**
 * ����ִ��������
 * @author sundl
 */
public interface IAresActionExcuteContext {

	/**
	 * ��ڣ�����ʲô�ط�ִ�����������(�����û��Ҽ���Ŀ��ѡ��xxxAction����˷������ض�Ӧ��IARESProject)
	 * @return
	 */
	Object getEntryPoint();
	
	/**
	 * ��ǰ����ִ�е�Resource
	 * @return
	 */
	IARESResource getCurrentResource();
	
	/**
	 * ���������������õ����ݣ��û����������Դִ�й����е����ݽ�����
	 * @param key
	 * @return
	 */
	Object getData(String key);
	void setData(String key, Object value);
	
}
