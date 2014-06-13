/**
 * Դ�������ƣ�IEditableControl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�����
 */
package com.hundsun.ares.studio.ui.editor.editable;

import java.util.Map;

/**
 * @author lvgao
 *
 */
public interface IEditableControl {

	/**
	 * ��ȡ��Դֻ��״̬
	 * @return
	 */
	public boolean getResourceReadonlyStatus();
	
	/**
	 * ˢ����Դֻ��״̬
	 * @return
	 */
	public void refreshResourceReadonlyStatus();
	
	/**
	 * �����û�״̬
	 * @param key
	 * @param status
	 */
	public void putUserStatus(String key,Object status);
	
	
	/**
	 * ֪ͨ�û��ֶ�״̬
	 * @param staus
	 */
	public void notifyUserStatus(String key);
	
	/**
	 * ���ֻ�����Ƶ�Ԫ
	 * @param unit
	 */
	public void addEditableUnit(IEditableUnit unit);
	
	/**
	 * ˢ������ֻ�����Ƶ�Ԫ��ֻ��״̬
	 * @param context    ������
	 */
	public void refreshAllUnit(Map<Object, Object> context);
}
