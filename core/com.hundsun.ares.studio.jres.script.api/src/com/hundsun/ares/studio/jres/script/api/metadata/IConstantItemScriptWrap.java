/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.metadata;

import com.hundsun.ares.studio.jres.script.api.wrap.IMetadataItemScriptWrap;

/**
 * �û�������ϸ����
 * 
 * @author yanwj06282
 *
 */
public interface IConstantItemScriptWrap extends IMetadataItemScriptWrap{

	/**
	 * ��ȡҵ����������
	 * 
	 * @return
	 */
	public String getDataType(String type);
	
	/**
	 * ��ȡ����ֵ
	 * 
	 * @return
	 */
	public String getContantValue();
	
}
