/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.metadata.IStandardFieldScriptWrap;
import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;

/**
 * @author lvgao
 *
 */
public interface ISingleTableScriptWrap extends IScriptModelWrap,IResourceModifyHistory {

	/**
	 * ��ȡ�����ֶ�
	 * @return
	 */
	public IStandardFieldScriptWrap[] getMasterStandardFields();
	
	/**
	 * ��ȡ��������
	 * @return
	 */
	public String[] getMasterAttrs();
	
	/**
	 * ��ȡ������
	 * @return
	 */
	public String getMasterTableName();
	
}
