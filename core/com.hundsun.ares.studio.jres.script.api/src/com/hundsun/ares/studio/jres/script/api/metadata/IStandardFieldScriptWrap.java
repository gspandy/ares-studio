/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.api.metadata;

import com.hundsun.ares.studio.jres.script.api.wrap.IMetadataItemScriptWrap;

/**
 * ��׼�ֶζ���
 * 
 * @author lvgao
 *
 */
public interface IStandardFieldScriptWrap extends IMetadataItemScriptWrap{

	/**
	 * ��ȡҵ����������
	 * 
	 */
	public IBizDataTypeItemScriptWrap getBizDataType();
	
	/**
	 * ��ȡҵ����������
	 * 
	 */
	public String getStrBizDataType();
	
	/**
	 * ��ȡҵ�����ͳ���
	 * <p>�����ֵ�����������ͣ�����0</p>
	 * 
	 * @return
	 */
	public int getLength();

	/**
	 * ��ȡҵ�����;���
	 * <p>�����ֵ�����������ͣ�����0</p>
	 * 
	 * @return
	 */
	public int getPrecision();
	
	/**
	 * ��ȡ��ʵ��������
	 * @param type  ����:oracle\db2\sqlserver\c\java\mysql\informix\sybase\C#�ȣ�ע���Сд��
	 * @return
	 */
	public String  getRealType(String type);
	
	/**
	 * ��ȡ�����ֵ�info
	 * 
	 * @return
	 */
	public IDictEntryScriptWrap getDictInfo();
	
	/**
	 * ��ȡ�����ֵ�
	 * 
	 */
	public String getStrDictInfo();
	
	/**
	 * ���ñ�׼�ֶ���
	 * 
	 * @param name
	 */
	public void setName(String name);
	
	/**
	 * ���ñ�׼�ֶ�������
	 * 
	 * @param chinaeseName
	 */
	public void setChineseName(String chinaeseName);
	
	/**
	 * ����������Ϣ
	 * 
	 * @param description
	 */
	public void setDescription(String description);
	
	/**
	 * ���������ֵ�
	 * 
	 * @param dictId �����ֵ���Ŀ��
	 */
	public void setDictInfo(String dictId);
	
	/**
	 * ����ҵ����������
	 * 
	 * @param dataType ҵ������������Ŀ��
	 */
	public void setDataType(String dataType);
	
	/**
	 * ��ȡ��׼�ֶζ�Ӧ��ҵ�����͵�Ĭ��ֵ
	 * @param type  ����:oracle\db2\sqlserver\c\java\mysql\informix\sybase\C#�ȣ�ע���Сд��
	 * @return
	 */
	public String getTrueDefaultValue(String type);
	
}
