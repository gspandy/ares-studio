/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;

/**
 * ����ARES model�������쳣ԭ��
 * @author sundl
 */
public interface IARESModelStatus extends IStatus {

	IARESElement[] getElements();
	
	IPath getPath();
	
	boolean isDoesNotExist();
}
