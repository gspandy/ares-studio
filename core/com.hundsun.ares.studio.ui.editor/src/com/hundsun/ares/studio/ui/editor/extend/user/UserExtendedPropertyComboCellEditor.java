/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.editor.extend.user;

import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author sundl
 */
public class UserExtendedPropertyComboCellEditor extends ComboBoxViewerCellEditor{

	private IUserExtendedPropertyDescriptor descriptor;
	
	public UserExtendedPropertyComboCellEditor(IUserExtendedPropertyDescriptor descriptor, Composite parent, int style) {
		super(parent, style);
		this.descriptor = descriptor;
	}

}
