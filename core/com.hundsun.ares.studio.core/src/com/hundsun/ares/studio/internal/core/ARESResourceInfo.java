/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.internal.core;

import com.hundsun.ares.studio.internal.core.OpenableElementInfo;

/**
 * 
 * @author sundl
 */
public class ARESResourceInfo extends OpenableElementInfo {

	Object realInfo;
	
	public Object getRealInfo() {
		return realInfo;
	}
	
	public String toString() {
		return "ARES Resource Info, RealInfo is: " + String.valueOf(realInfo);
	}
}
