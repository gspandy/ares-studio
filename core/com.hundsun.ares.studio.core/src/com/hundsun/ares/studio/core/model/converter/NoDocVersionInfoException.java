/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.model.converter;

import java.io.PrintWriter;

/**
 * 
 * @author maxh
 */
public class NoDocVersionInfoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1377965873445420198L;
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
	 */
	@Override
	public void printStackTrace(PrintWriter s) {
		s.println("/*************************************************************************/");
		s.println("�Ҳ����ĵ��İ汾��Ϣ");
		s.println("/*************************************************************************/");
		super.printStackTrace(s);
	}

}
