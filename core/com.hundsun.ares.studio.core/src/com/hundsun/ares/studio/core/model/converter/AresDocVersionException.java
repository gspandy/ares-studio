/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.model.converter;

import java.io.PrintWriter;

/**
 * ת�������ĵ��İ汾�г�ͻ���µĶ�ȡ�쳣
 * @author maxh
 */
public class AresDocVersionException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3581446075769761092L;

	String converterVersion;
	String docVersion;
	
	/**
	 * 
	 */
	public AresDocVersionException(String converterVersion,String docVersion) {
		this.converterVersion = converterVersion;
		this.docVersion = docVersion;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
	 */
	@Override
	public void printStackTrace(PrintWriter s) {
		s.println("/*************************************************************************/");
		s.println("�ĵ��汾�����ݣ��������޷�����");
		s.println("�������汾:" + converterVersion);
		s.println("�ĵ��汾:" + docVersion);
		s.println("/*************************************************************************/");
		super.printStackTrace(s);
	}
}
