/**
 * Դ�������ƣ�IAddConstraintModificationScriptWrap.java
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
 * ����Լ��
 * @author sundl
 *
 */
public interface IAddConstraintModificationScriptWrap extends IModificationScriptWrap{
	
	/**
	 * ��ȡ���ӵ�Լ���б�
	 * @return
	 */
	ITableKeyScriptWrap[] getDetails();
}
