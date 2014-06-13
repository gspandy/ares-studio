/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.editor;

import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.core.IARESProjectProperty;
import com.hundsun.ares.studio.ui.control.IEditable;
import com.hundsun.ares.studio.ui.util.ImporveControlWithDitryStateContext;


/**
 * ��չ�ֶ�
 * @author sundl
 */
public abstract class ExtensionFieldEditor implements IEditable{

	/**
	 * ��ʼ������ܻᴫ�뵱ǰ����Ŀ���Զ���ĸ������༭��
	 * @param properties ��Ŀ����
	 */
	public abstract void init(IARESProjectProperty properties);
	
	/**
	 * ��������ؼ�
	 * @param parent
	 */
	public abstract void createControls(FormToolkit toolkit, ImporveControlWithDitryStateContext context);

	/**
	 * ˢ�½��档
	 */
	public abstract void refresh();
	
}
