/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.editor;

import org.eclipse.ui.IEditorInput;

/**
 * 
 * @author sundl
 */
public interface IEditorInputStateListener {

	void editorInputDeleted(IEditorInput input);
	void editorInputMoved(IEditorInput oldInput, IEditorInput newInput);
	
}
