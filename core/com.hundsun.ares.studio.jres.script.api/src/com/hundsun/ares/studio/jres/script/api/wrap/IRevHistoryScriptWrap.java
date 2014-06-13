/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;

/**
 * �޶���¼
 * 
 * @author yanwj06282
 *
 */
public interface IRevHistoryScriptWrap extends IScriptModelWrap{

	/**
	 * �������޶���¼ 
	 * 
	 * @return
	 */
	public String getHistoryComment();
	
	/**
	 * ��Դ����
	 * 
	 * @return
	 */
	public IDatabaseResScriptWrap getResourceInfo ();
	
	/**
	 * �汾��
	 * 
	 * @return
	 */
	public String getVersion();
	
	/**
	 * �޸�����
	 * 
	 * @return
	 */
	public String getModifiedDate();
	
	/**
	 * �޸�ԭ��
	 * 
	 * @return
	 */
	public String getModifiedReason();
	
	/**
	 * �޸�����
	 * 
	 * @return
	 */
	public String getModified();
	
	/**
	 * �޸���
	 * 
	 * @return
	 */
	public String getModifiedBy();
	
	/**
	 * �޸ĵ���
	 * 
	 * @return
	 */
	public String getOrderNumber();
	
	/**
	 * ��ע
	 * 
	 * @return
	 */
	public String getComment();
	
	/**
	 * ������
	 * 
	 * @return
	 */
	public String getCharger();
	
}
