package com.hundsun.ares.studio.engin.token;

public interface IParamDefineHelper {

	//��������
	public static final int STD  = 0;
	
	public static final int RECORD_OBJECT  = 1;
	
	public static final int RECORD_INTERFACE  = 2;
	
	public static final int RECORD_GROUP = 3;//������
	
	/**
	 * ��ӳ�ʼ������
	 * �����ӳɹ���ʾû�г�ʼ��
	 * ���ʧ�ܱ�ʾ�Ѿ���ʼ��
	 * @param type
	 * @param paraName
	 * @return
	 */
	public void addInit(int type,String paraName);
	//defineHelper.addInit(IParamDefineHelper.STD, para.getName());//�жϸñ����Ƿ��Ѿ�����
	
	public boolean canInit(int type,String paraName);
}
