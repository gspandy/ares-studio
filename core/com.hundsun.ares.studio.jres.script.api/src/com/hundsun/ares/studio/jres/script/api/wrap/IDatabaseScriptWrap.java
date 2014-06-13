/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.IDatabaseUserScriptWrap;
import com.hundsun.ares.studio.jres.script.api.database.ISequenceScriptWrap;
import com.hundsun.ares.studio.jres.script.api.database.ITableScriptWrap;
import com.hundsun.ares.studio.jres.script.api.database.ITableSpaceScriptWrap;
import com.hundsun.ares.studio.jres.script.api.database.ITriggerScriptWrap;
import com.hundsun.ares.studio.jres.script.api.database.IViewScriptWrap;

/**
 * ���ݿ⣬��Ӧ���̽ṹ�е����ݿ�ģ���������ֱ�ӻ��߼�ӵĻ�ȡ���ݿ��µ�������Ϣ
 * 
 * @author lvgao
 *
 */
public interface IDatabaseScriptWrap {

	/**
	 * ��ȡ���е����ݿ���Դ
	 * 
	 * @return
	 */
	public IDatabaseResScriptWrap[] getAllDatabaseResources();
	
	/**
	 * ��ȡָ����ϵͳ�µ�������Դ
	 * 
	 * @param moduleName
	 * @return
	 */
	public IDatabaseResScriptWrap[] getAllDatabaseResourcesBySubsys(String subsysName);
	
	/**
	 * ��ȡָ��ģ���µ����ݿ���Դ
	 * 
	 * @param moduleName ģ�����֣�������ڶ༶���á�.���ָ�
	 * @return
	 */
	public IDatabaseResScriptWrap[] getAllDatabaseResourcesByModule(String moduleName);
	
	/**
	 * ��ȡָ����ϵͳ�µ��޶���¼
	 * 
	 * @param subsysName
	 * @return
	 */
	public String getAllHistoriesCommentBySubsys(String subsysName , String content);
	
	/**
	 * ��ȡָ��ģ���µ��޶���¼
	 * 
	 * @param moduleName
	 * @return
	 */
	public String getAllHistoriesCommentByModule(String moduleName , String content);
	
	/**
	 * ��ȡָ����ϵͳ�µ��޶���¼����
	 * 
	 * @param subsysName
	 * @return
	 */
	public ITableRevHistoryScriptWrap[] getAllHistoriesBySubsys(String subsysName);
	
	/**
	 * ��ȡָ��ģ���µ��޶���¼����
	 * 
	 * @param moduleName
	 * @return
	 */
	public ITableRevHistoryScriptWrap[] getAllHistoriesByModule(String moduleName);
	
	/**
	 * ��ȡ���б�
	 * @return
	 */
	public ITableScriptWrap[] getAllTable();
	
	/**
	 * ��ȡ������ͼ
	 * @return
	 */
	public IViewScriptWrap[] getAllView();
	
	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public ISequenceScriptWrap[] getAllSequence();
	
	/**
	 * ��ȡ���д�����
	 * 
	 * @return
	 */
	public ITriggerScriptWrap[] getAllTrigger();
	
	/**
	 * ͨ�����ֻ�ȡ��
	 * @param name
	 * @return
	 */
	public ITableScriptWrap[] getTableByName(String name);
	
	/**
	 * ͨ�����ֻ�ȡ��ͼ
	 * @param name
	 * @return
	 */
	public IViewScriptWrap[] getViewByName(String name);
	
	/**
	 * ��ȡ��ռ����
	 * 
	 * @return
	 */
	public ITableSpaceScriptWrap getTableSpace();
	
	/**
	 * ��ȡ�û���Ȩ�޶���
	 * 
	 * @return
	 */
	public IDatabaseUserScriptWrap getDBUser();
	
}
