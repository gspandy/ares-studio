/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.assist;

/**
 * ������ʾ�ı����ṩ����
 * 
 * @author mawb
 */
public interface IAssistantProvider {
	
	/**
	 * �ṩ����������ʾ������
	 * @return
	 */
	public Object[] getProposals();
	
	/**
	 * ����ʵ���滻������
	 * 
	 * @param obj
	 * @return
	 */
	public String getContent(Object obj);

	/**
	 * ����������ʾ�Ҳ����ϸ��Ϣ
	 * 
	 * @param obj
	 * @return
	 */
	public String getDescription(Object obj);

	/**
	 * ������ʾ�����ݣ�һ�㼴Ϊ�滻����
	 * 
	 * @param obj
	 * @return
	 */
	public String getLabel(Object obj);
	
}
