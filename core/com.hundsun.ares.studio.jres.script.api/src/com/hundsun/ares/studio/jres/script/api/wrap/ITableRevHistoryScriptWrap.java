/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableScriptWrap;

/**
 * �޶���¼
 * 
 * @author yanwj06282
 *
 */
public interface ITableRevHistoryScriptWrap extends IRevHistoryScriptWrap{

	/**
	 * ��ȡ�޸�����
	 * 
	 * @return
	 */
	public String getActionType ();
	
	/**
	 * ��ȡ�޶���¼��Ϣ
	 * 
	 * <li>
	 * ���ݵ�ǰ�޶���¼�����ͣ��õ����Եİ�װ�����б�����:<br>
	 * 	<i>���ӱ�{@link IAddTableModificationScriptWrap}</i><br>
	 * 	<i>���ӱ��ֶΣ�{@link IAddColModificationScriptWrap}</i><br>
	 *	<i>ɾ�����ֶΣ�{@link IRemoveColModificationScriptWrap}</i><br>
	 *	<i>���������ֶΣ�{@link IRenameTableColModificationScriptWrap}</i><br>
	 * 	<i>����������{@link IAddIndexModificationScriptWrap}</i><br>
	 * 	<i>ɾ��������{@link IRemoveIndexModificationScriptWrap}</i><br>
	 * 	<i>�޸ı����ͣ�{@link ITableColTypeModificationScriptWrap}</i><br>
	 * 	<i>����������{@link IAddPKModificationScriptWrap}</i><br>
	 * 	<i>�޸�������{@link ITableColPKModificationScriptWrap}</i><br>
	 * 	<i>ɾ��������{@link IRemovePKModificationScriptWrap}</i><br>
	 * 	<i>�޸ı��ֶ�Ϊ�գ�{@link ITableColNullableModificationScriptWrap}</i><br>
	 * 	<i>����ΨһԼ����{@link IAddUniqueModificationScriptWrap}</i><br>
	 * 	<i>�޸�ΨһԼ����{@link ITableColUniqueModificationSctiptWrap}</i><br>
	 * 	<i>ɾ��ΨһԼ����{@link IRemoveUniqueModificationScriptWrap}</i><br>
	 * </li>
	 * @return
	 * 
	 */
	public IModificationScriptWrap getAction ();
	
	/**
	 * ��ȡ���װ����
	 * 
	 * @return
	 */
	public ITableScriptWrap getTableInfo ();
	
}
