/**
 * Դ�������ƣ�IRemoveIndexFieldModificationScriptWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�liaogc
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableIndexScriptWrap;

/**
 * @author liaogc
 *
 */
public interface IRemoveIndexFieldModificationScriptWrap extends IModificationScriptWrap {
/**
 * ��ȡɾ������������ϸ��¼
 * @return
 */
public ITableIndexScriptWrap[] getDetails();
}
