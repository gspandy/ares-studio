/**
 * Դ�������ƣ�IValidateUnit.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.validate;

import java.util.Map;


/**
 * �����鵥Ԫ
 * @author lvgao
 *
 */
public interface IValidateUnit {
	/**
	 * �������⻺��
	 * @param pool      ���⻺��
	 * @param context    ������
	 * @throws Exception
	 */
	public void updateProblemPool(IProblemPool pool, Map<Object, Object> context) throws Exception;

}
