/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IOpenable;
import com.hundsun.ares.studio.core.IParent;

/**
 * �����⡣
 * @author sundl
 */
public interface IReferencedLibrary extends IOpenable, IParent, IARESElement, IBasicReferencedLibInfo, IResPathEntryElement, IDependencyUnit, IARESBundle{
	
	String PROPERTIE_FILE = ".aar";
	
	/**
	 * �����������Щ��.
	 * @return
	 */
	IARESModuleRoot[] getRoots() throws ARESModelException;
	
	IBasicReferencedLibInfo getBasicInfo();
	
//	IARESResource findResource(String path);
	
	boolean isExternal();
}
