/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.metadata.IStandardFieldScriptWrap;

/**
 * @author yanwj06282
 *
 */
public interface IMasterSlaveLinkTableScriptWrap extends IMasterSlaveTableScriptWrap {

	/**
	 * ��ȡ�������ֶ�
	 * @return
	 */
	public IStandardFieldScriptWrap[] getLinkStandardFields();
	
	/**
	 * ��ȡ����������
	 * @return
	 */
	public String[] getLinkAttrs();
	
	/**
	 * ��ȡ��������
	 * @return
	 */
	public String getLinkTableName();
	
}
