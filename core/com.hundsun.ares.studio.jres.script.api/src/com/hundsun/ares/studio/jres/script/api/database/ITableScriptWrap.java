/**
 * Դ�������ƣ�ITableScriptWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.script.api.database;

import java.util.List;

import com.hundsun.ares.studio.jres.script.api.wrap.IDatabaseResScriptWrap;
import com.hundsun.ares.studio.jres.script.api.wrap.ITableRevHistoryScriptWrap;

/**
 * ���ݿ��
 * 
 * @author lvgao
 *
 */
public interface ITableScriptWrap  extends IDatabaseResScriptWrap{

	/**
	 * ��ȡ����
	 * 
	 * @return
	 */
	public String getTableName();
	
	/**
	 * ���ر�����
	 * 0 "һ���";
	 * 1 "��ʱ��(����������)";
	 * 2 "��ʱ��(��������)";
	 *	}
	 * @return
	 */
	public int getTableType();
	
	/**
	 * 	���ط����ֶΣ���ͨ����չ�ֶε�jsonȡ��ȡ�ã�
	 * 	2013��5��27��14:56:26 mod qinyuan 
	 *  ���Ϊ�Զ��壬ȡ����������ֶΣ�����ȡģ������ֶ�
	 *  ע�����ģ�������ϢΪ�գ���ȡ�游ģ��
	 * @return
	 */
	public String  getPartitionfield();
	
	/**
	 * 	�Ƿ��Զ����������ͨ����չ�ֶε�jsonȡ��ȡ�ã�
	 * @return
	 */
	public boolean isPartitionByUser();
	
	/**
	 * �����Ƿ�������ʷ��
	 * 
	 * @param isGenHisTable
	 */
	public void setGenHisTable(boolean isGenHisTable);
	
	/**
	 * �Ƿ������ʷ��
	 */
	public boolean isGenHisTable ();
	
	/**
	 * �Ƿ���������
	 */
	public boolean isGenReduTable ();
	
	/**
	 * �Ƿ���������
	 */
	public boolean isGenSettTable ();
	
	/**
	 * ���ö����
	 * 
	 * @param objectId
	 */
	public void setObjectId(String objectId);
	
	
	/**
	 * ���������ռ�
	 * 
	 * @param prefix
	 * @return
	 */
	public String getIndexTableSpace(String prefix);
	
	/**
	 * 	��÷�����ʼʱ��
	 * 2013��5��27��14:56:26 mod qinyuan 
	 *  ���Ϊ�Զ��壬ȡ�����������ʼʱ�䣻����ȡģ�������ʼʱ��
	 *  ע�����ģ�������ϢΪ�գ���ȡ�游ģ��
	 * @return
	 */
	public String getPartitionStartDate();
	
	/**
	 * 	��÷�������
	 * 2013��5��27��14:56:26 mod qinyuan 
	 *  ���Ϊ�Զ��壬ȡ�����������������ȡģ���������
	 *  ע�����ģ�������ϢΪ�գ���ȡ�游ģ��
	 * @return
	 */
	public int getPartitionNum();
	
	/**
	 * ��ñ�ע�͵�sql���
	 * @param prefix
	 * @param 	true:�������ݿ���ԴSQL�����û� ;false:�������ݿ���ԴSQL�������û�
	 * 
	 * @return
	 */
	public String getCommentSql(String prefix , boolean isUser);
	
	/**
	 * 	���ע��ͷsql�����޶���¼������֯��
	 * @return
	 */
	public String getHistoryComment(String commentMark);
	
	/**
	 * ���ñ����
	 * @return
	 */
	public void setTableColumns(List<ITableColScriptWrap> columns);
	
	/**
	 * ���ñ������
	 * @return
	 */
	public void setTableIndexs(List<ITableIndexScriptWrap> indexs);
	
	/**
	 * ���ñ��Լ��
	 * @return
	 */
	public void setTableKeys(List<ITableKeyScriptWrap> keys);
	
	/**
	 * ��ȡ�����
	 * @return
	 */
	public ITableColScriptWrap[] getTableColumns();
	
	/**
	 * ��ȡ�������
	 * @return
	 */
	public ITableIndexScriptWrap[] getTableIndexs();
	
	/**
	 * ��ȡ�������
	 * @return
	 */
	public ITableKeyScriptWrap[] getTableKeys();
	
	/**
	 * ��ȡ�ֶε�������
	 * 
	 * @param fieldName
	 * @return
	 */
	public String getStdFieldChineseName(String fieldName);
	
	/**
	 * ��ȡ��������
	 * 
	 * @param type
	 * @return
	 */
	public String getDataTypeOracle(String type);
	
	/**
	 * ��ȡ���װ����
	 * 
	 * @param revHistory
	 * @return
	 */
	public ITableScriptWrap getTableInfoByHisInfo(ITableRevHistoryScriptWrap revHistory);
	
	/**
	 * �����޶���¼�ķ�װ����
	 * 
	 * @param history
	 */
	public void setHistory(ITableRevHistoryScriptWrap history);
	
	
	/**
	 * ��ȡ�޶���¼�ķ�װ����
	 */
	public ITableRevHistoryScriptWrap getHistory();
	
	/**
	 * ���ݱ��ֶ�������ȡ�ֶζ���
	 * @param column_name
	 * @return
	 */
	public ITableColScriptWrap getTableColumnByName(String column_name);
	
}
