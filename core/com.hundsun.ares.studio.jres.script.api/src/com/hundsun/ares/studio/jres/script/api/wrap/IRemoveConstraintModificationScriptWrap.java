/**
 * Դ�������ƣ�IRemoveConstraintModificationScriptWrap.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script.api
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�Dollyn
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.database.ITableKeyScriptWrap;

/**
 * @author sundl
 *
 */
public interface IRemoveConstraintModificationScriptWrap extends IModificationScriptWrap {

	/**
	 * ��ȡɾ����Լ���б�
	 * @return
	 */
	ITableKeyScriptWrap getDetails();
}
