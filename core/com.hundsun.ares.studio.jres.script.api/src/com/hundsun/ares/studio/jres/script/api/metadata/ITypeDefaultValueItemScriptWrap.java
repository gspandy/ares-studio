/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.metadata;

import com.hundsun.ares.studio.jres.script.api.wrap.IMetadataItemScriptWrap;

/**
 * ҵ������������ϸ����
 * 
 * @author yanwj06282
 *
 */
public interface ITypeDefaultValueItemScriptWrap extends IMetadataItemScriptWrap{
	
	/**
	 * ��ȡָ�����͵�Ĭ��ֵ
	 * @param type  ����:oracle\db2\sqlserver\c\java\mysql\informix\sybase\C#�ȣ�ע���Сд��
	 * @return
	 */
	public String getValue(String type);
	
}
