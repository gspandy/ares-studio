/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.cres.text.assistant;

import java.util.List;

import org.eclipse.jface.text.IDocument;

/**
 * @author wangxh
 */
public interface IAssistantLoader {

	/**����������ʾ��*/
	List<String> loadAssitant(String text,IDocument doc, int offset);
	
	/**���ù�����*/
	void setFilter(IAssistantFilter filter);
	
	/**����ʾ����ʾ�ַ���ת��Ϊ�ĵ���Ӧ�滻���ַ���*/
	String getReplacement(String display);
	
	/**��ȡע����Ϣ*/
	String getDescription(String display);
	
}
