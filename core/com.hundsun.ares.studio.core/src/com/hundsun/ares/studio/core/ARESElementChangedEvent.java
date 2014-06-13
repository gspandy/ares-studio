/**
 * Դ�������ƣ�ARESElementChangedEvent.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core;

/**
 * �����¼���
 * Ŀǰ��ʱ��һ���¼���������ʶrespath�ı���¼���
 * @author sundl
 */
public class ARESElementChangedEvent {

	public static final int UNKNOWN = -1;
	public static final int RES_PATH = 0;
	
	private int type = UNKNOWN;
	private IARESElement elemnent;
	
	/**
	 * �漰�ϴ���ļ������⣬�ṩĬ�ϵĲ����¼���
	 */
	public ARESElementChangedEvent() {
		this(null, UNKNOWN);
	}
	
	/**
	 * @param element ��Ӧ��element����typeΪrespath�仯��ʱ��elementΪ��Ӧ��project
	 * @param type ���ͣ�Ŀǰֻ�����֣�δ֪��respath�仯
	 */
	public ARESElementChangedEvent(IARESElement element, int type) {
		this.elemnent = element;
		this.type = type;
	}
	
	public IARESElement getElement() {
		return elemnent;
	}
	
	public int getType() {
		return this.type;
	}
	
}
