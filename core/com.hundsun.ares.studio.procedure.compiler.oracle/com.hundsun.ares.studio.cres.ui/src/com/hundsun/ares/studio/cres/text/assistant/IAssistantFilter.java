package com.hundsun.ares.studio.cres.text.assistant;


/**
 * @author wangxh
 *	����������ʾ�����˵�������ģ���LF��AF��
 */
public interface IAssistantFilter {
	/**ÿ����ʾǰ��Щ��ʼ����������������糬��ģ���������*/
	void init();
	
	boolean filter(Object obj);
}
