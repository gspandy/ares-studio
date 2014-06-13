package com.hundsun.ares.studio.jres.script.api.biz;

import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;


public interface IParameterWrap extends IScriptModelWrap{
	
	/**
	 * ��ȡ���
	 * 
	 * @return
	 */
	public String getFlag();
	
	/**
	 * ��������Ӣ����
	 * @return
	 */
	String getName();
	
	/**
	 * ������
	 * @return
	 */
	String getCName();
	
	/**
	 * ˵��
	 * @return
	 */
	String getDesc();
	
	/**
	 * ��������, ����׼�ֶΣ����󣬻�Ǳ�׼�ֶ�
	 * @return
	 */
	String getParamType();
	
	/**
	 * ҵ����������
	 * @return
	 */
	String getBizType();
	
	/**
	 * ��ʵ�������ͣ���Java���ͻ���C���͵�...  ���巵�������Ǹ�����Ŀ�����е����ã�����ӿ���ʹ���������Ծ����ġ�
	 * ##��δʵ��
	 * @return
	 */
	String getRealType();
	
	/**
	 * ��ȡ��Ӧ��Java����, ����Ҳ����򷵻�null
	 * @return
	 */
	String getJavaType();
	
	/**
	 * ��ȡC#��������
	 * @return
	 */
	String getCSharpType();
	
	/**
	 * ������ϵ�������ַ�����ʽ�� [0..1], [1..1], [0..n], [1..n]
	 * 
	 * @return
	 */
	String getMultiplicity();
	
	/**
	 * Ĭ��ֵ
	  * @param type  ����:oracle\db2\sqlserver\c\java\mysql\informix\sybase\C#�ȣ�ע���Сд��
	 * @return
	 */
	String getDefaultValue(String type);
	
	/**
	 * ��ע
	 * @return
	 */
	String getComments();
	
	/**
	 * ���ñ��
	 * 
	 * @return
	 */
	void setFlag(String flag);
}
