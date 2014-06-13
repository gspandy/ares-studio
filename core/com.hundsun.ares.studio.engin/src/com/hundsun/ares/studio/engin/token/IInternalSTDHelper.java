package com.hundsun.ares.studio.engin.token;

public interface IInternalSTDHelper {

	/**
	 * �Ƿ��Ǳ�׼�ֶ����͵��ڲ�����
	 * @param paraname
	 * @return
	 */
	public boolean isInternalSTD(String paraname);
	
	/**
	 * �Ƿ���ʹ�ù��ı�׼���Ͳ�������׼�ֶκͱ�׼�������͵���ʱ������
	 * @param paraname
	 * @return
	 */
	public boolean isUsedSTD(String paraname);
	
	/**
	 * �Ƿ�����ʱ��¼
	 * @param paraname
	 * @return
	 */
	public boolean isInternalRecord(String paraname);
	
	/**
	 * �Ƿ�����������ڲ�����
	 * @param paraname
	 * @return
	 */
	public boolean isInternalComponent(String paraname);
	
	/**
	 * �Ƿ�����������ڲ�����������
	 * @param paraname
	 * @return
	 */
	public String getInternalComponentType(String paraname);

	/**
	 * �Ƿ���������������������
	 * @param paraname
	 * @return
	 */
	public boolean isInOutComponent(String paraname);
	
	/**
	 * ��ȡ��ʱ��������
	 * @param paraname
	 * @return
	 */
	public String getInternalSTDType(String paraname);
}
