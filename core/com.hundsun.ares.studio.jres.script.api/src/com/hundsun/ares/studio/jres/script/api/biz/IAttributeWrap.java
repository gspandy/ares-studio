package com.hundsun.ares.studio.jres.script.api.biz;

public interface IAttributeWrap {
	/**
	 * ��������Ӣ����
	 * @return
	 */
	String getName();
	
	/**
	 * ���ò�������ֵ��
	 * set�ӿ�ע����Դ��ȡ����Ҫ�԰���������Դ���ã� res.becomeWorkingCopy() ����Դת���ɿ�д״̬��
	 */
	void setName(String name);
	
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
	 * ����ҵ������
	 * 
	 * @param bizType
	 * @return
	 */
	void setBizType(String bizType);
	
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
	 * ��ȡC#����
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
	
}
