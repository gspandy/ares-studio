/**
 * Դ�������ƣ�ITableColumnScriptWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.script.api.database;

import com.hundsun.ares.studio.jres.script.api.metadata.IStandardFieldScriptWrap;


/**
 * 
 * ���ݿ���ֶ�
 * 
 * @author lvgao
 *
 */
public interface ITableColScriptWrap {
	
	/**
	 * ����ֶζ�Ӧ��sql�ű����,��Ҫ�������ŵ�ת��
	 * ��ѡ���ͣ�oracle\db2\sqlserver\c\java\mysql\informix\sybase\C#
	 * 
	 * @return
	 */
	public String getEscapeSql(String type);
	
    /**
     * ����ֶζ�Ӧ��sql�ű���� �� ����Ҫ�������ŵ�ת��
     * ��ѡ���ͣ�oracle\db2\sqlserver\c\java\mysql\informix\sybase\C#
     * @return
     */
	public String getSql(String type)	;
	
	/**
	 * 	��ø��ֶ�����
	 * @return
	 */
	public String getName();
	
	/**
	 * ����ֶζ�Ӧ�ı�׼�ֶζ���
	 * 
	 * @return
	 */
	public IStandardFieldScriptWrap getstdField();
	
	/**
	 * �Ƿ�����
	 * 
	 * @return
	 */
	public boolean isPrimaryKey();
	
	/**
	 * �Ƿ�Ψһ
	 * @return
	 */
	public boolean isUnique();
	
	/**
	 * �Ƿ�����Ϊ��
	 * 
	 * @return
	 */
	public boolean isNullable();
	
	/**
	 * ��ȡĬ��ֵ
	 * ��ѡ���ͣ�oracle\db2\sqlserver\c\java\mysql\informix\sybase\C#
	 * 
	 * @return
	 */
	public String getDefaultValue(String type);
	
	/**
	 * ��ȡ���
	 * 
	 * @return
	 */
	public String getMark();
	
	/**
	 * 	��ô��ֶζ�Ӧ�����
	 * @return
	 */
	public String[] getForeignkey();
	
	/**
	 * 	��ô��ֶζ�Ӧ�ı�׼�ֶε���������
	 * @return
	 */
	public String getstdFieldChineseName();
	
	/**
	 * ��ȡ���
	 * 
	 * @return
	 */
	public ITableColForergnKeyScriptWrap[] getForeignKey(); 
	
	/**
	 * ��ȡ���ж�Ӧ��oracle����������
	 * ��ѡ���ͣ�oracle\db2\sqlserver\c\java\mysql\informix\sybase\C#
	 * 
	 * @return
	 */
	public String getRealDataType(String type);
	
	/**
	 * ���ñ�ע��Ϣ
	 * @param comments
	 */
	public void setComments(String comments);
	
	/**
	 * �����ֶ���
	 * @param name
	 */
	public void setName(String name);
	
	/**
	 * �����������ͣ�ֻ���ڷǱ��ֶ�ʱ������Ч
	 * @param name
	 */
	public void setBizType(String name);
	
	/**
	 * ��ȡ�������ͣ��ڷǱ��ֶ��£���ȡ���ֶε��ı�ҵ�����ͣ����Ϊ��׼�ֶΣ���ȡ��׼�ֶζ�Ӧ���ı�ҵ������
	 */
	public String getStrBizType();
}
