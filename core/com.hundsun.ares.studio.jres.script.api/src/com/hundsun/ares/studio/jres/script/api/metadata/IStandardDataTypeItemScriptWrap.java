/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.metadata;

import com.hundsun.ares.studio.jres.script.api.wrap.IMetadataItemScriptWrap;

/**
 * ҵ������������ϸ����
 * 
 * @author yanwj06282
 *
 */
public interface IStandardDataTypeItemScriptWrap extends IMetadataItemScriptWrap{
	
	/**
	 * ��ȡ��׼����������ʵֵ
	 * 
	 * @return
	 */
	public String getRealType(String type);
	
}
