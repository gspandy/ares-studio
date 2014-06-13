/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.metadata;

import com.hundsun.ares.studio.jres.script.api.wrap.IMetadataItemScriptWrap;

/**
 * ���ܶ���
 * 
 * @author yanwj06282
 *
 */
public interface IMenuFunctionScriptWrap extends IMetadataItemScriptWrap{

	/**
	 * ��ȡ���ܱ��
	 * 
	 * @return
	 */
	public String getFunctionCode();
	
	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public String getFunctionName();
	
	/**
	 * ��÷�������
	 * 
	 * @return
	 */
	public String getServiceName();
	
	/**
	 * ��ȡ������
	 * 
	 * @return
	 */
	public String getSubTransCode();
	
	/**
	 * ���ù��ܱ��
	 * 
	 * @param functionCode
	 */
	public void setFunctionCode(String functionCode);
	
	/**
	 * ���ù�������
	 * 
	 * @param functionName
	 */
	public void setFunctionName(String functionName);
	
	/**
	 * ���÷�������
	 * 
	 * @param serviceName
	 */
	public void setServiceName(String serviceName);
	
	/**
	 * �����ӽ�����
	 * 
	 * @param subTransCode
	 */
	public void setSubTransCode(String subTransCode);
	
}
