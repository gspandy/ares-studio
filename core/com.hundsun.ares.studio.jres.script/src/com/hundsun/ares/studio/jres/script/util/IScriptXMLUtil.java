/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util;

import org.dom4j.Document;
import org.dom4j.Element;

/**
 * @author lvgao
 *
 */
public interface IScriptXMLUtil {

	/**
	 * ָ��
	 * @param rootName
	 * @return
	 */
	public Document createDoc(String rootName);
	
	/**
	 * ��ȡ����
	 * @param doc
	 * @return
	 */
	public String getContent(Document doc,String charset);
	
	/**
	 * ����ӽڵ�
	 * @param parent
	 * @param name
	 * @param value
	 */
	public Element addElement(Element parent,String name,String value);
	
	/**
	 *��ӽڵ�
	 * @param doc
	 * @param name
	 * @param value
	 */
	public Element addElement(Document doc,String name,String value);
	
	/**
	 * �������
	 * @param parent
	 * @param name
	 * @param value
	 */
	public void addAttr(Element parent,String name,String value);
	
	/**
	 * �������
	 * @param doc
	 * @param name
	 * @param value
	 */
	public void addAttr(Document doc,String name,String value);
	
}
