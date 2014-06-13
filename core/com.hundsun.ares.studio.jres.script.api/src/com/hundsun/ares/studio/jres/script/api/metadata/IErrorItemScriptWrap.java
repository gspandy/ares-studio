/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.metadata;

import com.hundsun.ares.studio.jres.script.api.wrap.IMetadataItemScriptWrap;

/**
 * �������ϸ����
 * 
 * @author yanwj06282
 *
 */
public interface IErrorItemScriptWrap extends IMetadataItemScriptWrap{
	
	/**
	 * ��ȡ�����
	 * 
	 * @return
	 */
	public String getErrorNo();
	
	/**
	 * ��ȡ������Ϣ
	 * 
	 * @return
	 */
	public String getErrorInfo();
	
	/**
	 * ��ȡ����ų���
	 * 
	 * @return
	 */
	public String getCnstName();
	
	/**
	 * ��ȡ���󼶱�
	 * 
	 * @return
	 */
	public String getErrorLevel();
	
}
