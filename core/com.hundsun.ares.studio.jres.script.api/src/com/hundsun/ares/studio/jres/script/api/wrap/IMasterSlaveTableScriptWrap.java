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
public interface IMasterSlaveTableScriptWrap extends ISingleTableScriptWrap {

	/**
	 * ��ȡ�ӱ��ֶ�
	 * @return
	 */
	public IStandardFieldScriptWrap[] getSlaveStandardFields();
	
	/**
	 * ��ȡ�ӱ�����
	 * @return
	 */
	public String[] getSlaveAttrs();
	
	/**
	 * ��ȡ�ӱ���
	 * @return
	 */
	public String getSlaveTableName();
	
}
