/**
 * Դ�������ƣ�IEditableControlUnit.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�����
 */
package com.hundsun.ares.studio.ui.editor.editable;


/**
 * @author lvgao
 *
 */
public interface IEditableUnit {
	
	public static final String KEY_SYSTEM = "ϵͳֻ��״̬";

	public static final String EDITABLE_TRUE = "ϵͳ�ɱ༭״̬";
	
	public static final String EDITABLE_FALSE = "ϵͳֻ��״̬";
	
	/**
	 * ����ֻ��״̬
	 * @param status
	 */
	public void setReadonlyStatus(String key,Object status);
}
