/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

/**
 * ��������
 * @author sundl
 */
public interface IARESElementChangeListener {

	/**
	 * ��ʱ��ʵ��һ�������ṹ�������������֪ͨ��������
	 */
	// sundl ���Ӳ������ü���������ȷ���¼��ľ�����Ϣ���������ͣ����ĵ�Ԫ�صȣ�Ŀǰ��ʵ�ֽ�֧��respath�仯�ľ��廯
	// ���������Ƿ�����
	void elementChanged(ARESElementChangedEvent event);
}
