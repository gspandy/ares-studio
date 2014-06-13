/**
 * Դ�������ƣ�IValidateControl.java
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
 * ���������
 * @author lvgao
 *
 */
public interface IValidateControl {

	/**
	 * ��ȡ���������
	 * @return
	 */
	public Map<Object, Object> getContext();
	
	public void setContext(Map<Object, Object> context);
	
	public void setProblemPool(IProblemPool pool);
	
	public IProblemPool getProblemPool();

	/**
	 * ��Ӽ�鵥Ԫ
	 * @param markHelper
	 */
	public void addValidateUnit(IValidateUnit validateUnit);
	
	/**
	 * �Ƴ���鵥Ԫ
	 * @param markHelper
	 */
	public void removeValidateUnit(IValidateUnit validateUnit);
	
	/**
	 * �������еļ��
	 */
	public void refresh();
	
	/**
	 * ����ĳһ�������
	 */
	public void refresh(final IValidateUnit validateUnit);
	
	/**
	 * �������
	 */
	public void destroyAll();
}
