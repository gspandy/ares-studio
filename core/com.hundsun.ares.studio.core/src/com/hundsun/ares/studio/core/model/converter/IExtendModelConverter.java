/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.model.converter;

import org.dom4j.Element;

/**
 * ��չģ�͵�ת����
 * @see ExtendModelConverterUtil#
 * @author maxh
 */
public interface IExtendModelConverter {
	/**
	 * д����չģ��
	 * @param extendModel ��Ҫ��д�����չģ��
	 * @param valueRoot value�ڵ� ��չģ�;����л�Ϊ�ýڵ���ӽڵ�
	 */
	public void writeExtendModel(Object extendModel,Element valueRoot);
	/**
	 * ������չģ��
	 * @param element value�ڵ�
	 * @return ��չģ��
	 */
	public Object readExtendModel(Element element);
}
