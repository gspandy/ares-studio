/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui;

import org.eclipse.ui.IEditorInput;

import com.hundsun.ares.studio.core.IARESResource;

/**
 * 
 * @author sundl
 */
public interface IARESResourceEditorInput extends IEditorInput {

	/**
	 * ��ȡ��Ӧ��ARES resource.
	 * 
	 */
	IARESResource getARESResource();
	
}
