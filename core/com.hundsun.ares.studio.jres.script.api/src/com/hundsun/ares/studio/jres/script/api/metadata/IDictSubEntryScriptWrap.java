/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.metadata;

import com.hundsun.ares.studio.jres.script.api.wrap.IMetadataItemScriptWrap;

/**
 * �����ֵ���ϸ����
 * 
 * @author yanwj06282
 *
 */
public interface IDictSubEntryScriptWrap extends IMetadataItemScriptWrap{
	
	/**
	 * ��ȡ�ֵ�����
	 * 
	 * @return
	 */
	public String getSubEntry();
	
	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public String getSubEntryName();
	
	/**
	 * �ֵ䳣��
	 * 
	 * @return
	 */
	public String getCnstName();
	
	/**
	 * ��ȡ����Ŀ
	 * 
	 * @return
	 */
	public IDictEntryScriptWrap getParent();
	
}
