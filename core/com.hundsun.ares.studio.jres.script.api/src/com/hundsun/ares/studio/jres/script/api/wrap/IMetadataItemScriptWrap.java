/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;

/**
 * Ԫ������ϸ��Ŀ����
 * 
 * @author yanwj06282
 *
 */
public interface IMetadataItemScriptWrap extends IScriptModelWrap{

	/**
	 * ��ȡ������
	 * 
	 * @return
	 */
	public String getChineseName();
	
	/**
	 * ��ȡ������Ϣ
	 * 
	 * @return
	 */
	public String getDescription();
	
}
