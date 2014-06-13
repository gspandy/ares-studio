/**
 * Դ�������ƣ�ValidateAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;
import com.hundsun.ares.studio.ui.editor.EMFFormPage;
import com.hundsun.ares.studio.ui.editor.IEMFFormPage;

/**
 * @author wangxh
 *
 */
public class ValidateAction extends Action {
	IFormPage page;
	/**
	 * 
	 */
	public ValidateAction(IFormPage page) {
		super();
		this.page = page;
		setText("������");
		setId(IActionIDConstant.CV_VALIDATE);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(ARESEditorPlugin.PLUGIN_ID, "icons/full/obj16/validate.png"));
	}

	@Override
	public void run() {
		if(page instanceof EMFFormPage){
			((IEMFFormPage)page).validate();
		}
	}
	

}
