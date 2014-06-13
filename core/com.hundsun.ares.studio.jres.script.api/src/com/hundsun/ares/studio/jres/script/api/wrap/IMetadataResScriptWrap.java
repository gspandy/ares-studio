/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;

/**
 * Ԫ������Դ����
 * 
 * @author yanwj06282
 *
 */
public interface IMetadataResScriptWrap extends IScriptModelWrap,IResourceModifyHistory {

	/**
	 * ��ȡԪ������Ŀ
	 * 
	 * @return
	 */
	public IMetadataItemScriptWrap[] getItems();
	
	/**
	 * ��ȡ������Ϣ,�ɻ�ȡ�÷����µ�������Ŀ��Ϣ
	 * 
	 * @return
	 */
	public IMetadataCategoryScriptWrap[] getCategories (); 
	
	/**
	 * ��ȡδ�������Ŀ
	 * 
	 * @return
	 */
	public IMetadataItemScriptWrap[] getNotCateItems ();
	
	/**
	 * ����name����Ԫ������Ŀ��Ϣ
	 * 
	 * @param name ��Ŀ��
	 * @param ignoreCase �Ƿ���Դ�Сд
	 * @return
	 */
	public IMetadataItemScriptWrap findItemByName(String name , boolean ignoreCase);
	
	/**
	 * ����Ԫ������Ŀ
	 * 
	 * @return
	 */
	public IMetadataItemScriptWrap addItem();
	
	/**
	 * ����IDɾ����Ӧ��Ŀ�����û�ж�ӦID���򲻲���
	 * 
	 * @param id
	 */
	public boolean removeItemById(String id);
	
}
