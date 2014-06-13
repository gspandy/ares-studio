/**
 * Դ�������ƣ�FormProblemPool.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.IMessageManager;

import com.hundsun.ares.studio.ui.editor.validate.DefaultProblemPool;

/**
 * @author gongyf
 *
 */
public class FormProblemPool extends DefaultProblemPool {
	
	IMessageManager manager;
	
	public FormProblemPool(IMessageManager manager) {
		this.manager = manager;
		this.context.put(FormControlProblemView.KEY_MESSAGEMANAGER, manager);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.DefaultProblemPool#notifyViews()
	 */
	@Override
	protected void notifyViews() {
		Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				boolean isAutoUpdate = manager.isAutoUpdate();
				manager.setAutoUpdate(false);
				FormProblemPool.super.notifyViews();
				manager.setAutoUpdate(isAutoUpdate);
			}
		});
	}
}
