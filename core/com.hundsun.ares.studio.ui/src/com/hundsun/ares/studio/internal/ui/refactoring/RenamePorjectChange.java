/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.internal.ui.refactoring;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ltk.core.refactoring.resource.RenameResourceChange;

/**
 * 
 * @author liaogc
 */
public class RenamePorjectChange extends RenameResourceChange {

	/**
	 * @param resourcePath
	 * @param newName
	 */
	public RenamePorjectChange(IPath resourcePath, String newName) {
		super(resourcePath, newName);
		
	}
	
}
