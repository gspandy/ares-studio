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
public interface IBizDataTypeItemScriptWrap extends IMetadataItemScriptWrap{

	/**
	 * ��ȡ��׼����
	 * 
	 * @return
	 */
	public IStandardDataTypeItemScriptWrap getStdType();
	
	/**
	 * ��ȡ��׼����������ʵֵ
	 * @param type  ����:oracle\db2\sqlserver\c\java\mysql\informix\sybase\C#�ȣ�ע���Сд��
	 * @return
	 */
	public String getRealType(String type);
	
	/**
	 * ��ȡ����
	 * @return
	 */
	public int getLength();
	
	/**
	 * ��ȡ����
	 * @return
	 */
	public int getPrecision();
	
	/**
	 * ��ȡĬ��ֵID
	 * 
	 * @return
	 */
	public String getDefaultValueID();
	
	/**
	 * ��ȡĬ��ֵ
	 * 
	 * @return
	 */
	public ITypeDefaultValueItemScriptWrap getDefaultValue();
	
}
