/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.extendpoint.manager;

import org.eclipse.ui.forms.editor.FormEditor;

import com.hundsun.ares.studio.ui.page.IExtendItemLoader;

/**
 * ��չҳ��ӿ�
 * @author sundl
 */
public interface IExtendedPage extends IExtendItemLoader{

	public void init(FormEditor editor);
	
	public void onCreate();
	public void beforeSave();
	public void afterSave();
	
}
