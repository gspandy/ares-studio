/**
 * Դ�������ƣ�IKeyConstructor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.validate;

public interface IKeyConstructor {

	/**
	 * ����key
	 * @param problem
	 * @return
	 */
	public KeyParameter constructKey(Object problem);
}
