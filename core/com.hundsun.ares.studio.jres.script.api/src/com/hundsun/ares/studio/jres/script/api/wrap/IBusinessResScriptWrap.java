/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;

/**
 * ҵ���߼���Դ����
 * 
 * @author yanwj06282
 *
 */
public interface IBusinessResScriptWrap extends IScriptModelWrap{
	
	/**
	 * ��ȡ��Դ������
	 * 
	 * @return
	 */
	public String getChineseName();
	
	/**
	 * ��ȡ��Դ��������ģ����¿�ʼ���ԡ�.�����ָ���
	 * <p>���磺sys.ser.AddUser</p>
	 * 
	 * @return
	 */
	public String getFullyQualifiedName();
	
	/**
	 * ��ȡ���ܺ�
	 * 
	 * @return
	 */
	public String getFunctionId();
	
	/**
	 * ��Դ����
	 * 
	 * @return
	 */
	public String getDescription();
	
}
