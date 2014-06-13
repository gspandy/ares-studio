/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.engin.exception;

/**
 * �쳣��Ļ���
 * 
 * @author zhuyf
 * 
 */
public class HSException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1215302580689587751L;
	
	/**
	 * �쳣��ʾ��Ϣ
	 */
	private String errorInfo;

	public HSException(String errorInfo) {
		super(errorInfo);
		this.errorInfo = errorInfo;
	}
	
	public String getErrorInfo()
	{
		return errorInfo;
	}
	
	public void printErrorInfo()
	{
		System.out.println(getErrorInfo());
	}
}
