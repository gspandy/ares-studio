/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;

/**
 * 
 * Ԫ���ݷ�����Ϣ��װ��
 * 
 * @author yanwj06282
 *
 */
public interface IMetadataCategoryScriptWrap extends IScriptModelWrap{

	/**
	 * ��ȡ�����µ���Ŀ
	 * 
	 * @return
	 */
	public IMetadataItemScriptWrap[] getItems();
	
	/**
	 * ��ȡ�ӷ���
	 * 
	 * @return
	 */
	public IMetadataCategoryScriptWrap[] getCategories ();
}
