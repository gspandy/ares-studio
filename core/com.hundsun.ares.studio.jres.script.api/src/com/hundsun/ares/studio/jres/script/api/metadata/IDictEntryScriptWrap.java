/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.metadata;

import com.hundsun.ares.studio.jres.script.api.wrap.IMetadataItemScriptWrap;

/**
 * �����ֵ����
 * 
 * @author yanwj06282
 *
 */
public interface IDictEntryScriptWrap extends IMetadataItemScriptWrap{

	/**
	 * ��ȡ�����ֵ���Ŀ��ϸ
	 * 
	 * @return
	 */
	public IDictSubEntryScriptWrap[] getSubEntries();
	
}
