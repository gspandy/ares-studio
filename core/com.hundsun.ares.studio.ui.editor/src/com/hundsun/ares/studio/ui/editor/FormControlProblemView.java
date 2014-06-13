/**
 * Դ�������ƣ�FormControlProblemView.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.IMessageManager;

import com.hundsun.ares.studio.ui.validate.IProblemView;
import com.hundsun.ares.studio.ui.validate.KeyParameter;
import com.hundsun.ares.studio.ui.validate.ProblemPoolChangeEvent;

/**
 * @author gongyf
 *
 */
public class FormControlProblemView implements IProblemView {

	public static final String KEY_MESSAGEMANAGER = "org.eclipse.ui.forms.IMessageManager";
	private KeyParameter key;
	private Control control;
	
	
	
	/**
	 * @param key
	 * @param control
	 */
	public FormControlProblemView(KeyParameter key, Control control) {
		super();
		this.key = key;
		this.control = control;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IProblemView#refresh(com.hundsun.ares.studio.jres.ui.validate.IProblemPool, java.util.Map)
	 */
	@Override
	public void refresh(ProblemPoolChangeEvent event) {
		IMessageManager manager =  (IMessageManager) event.getContext().get(KEY_MESSAGEMANAGER);
		if (manager != null) {
			Object[] problems = event.getSource().getProblem(key);

			// TODO#������#��Ҷ��#��ͨ#�����д���ϲ������󼶱�ʹ�����Ϣ������ӵ�MessageManager
			if (problems != null && problems.length > 0) {
				Diagnostic diagnostic = ((Diagnostic)problems[0]);
				manager.addMessage(key, diagnostic.getMessage(), diagnostic,
						convertDiagnosticSeverity(diagnostic.getSeverity()), control);
			} else {
				manager.removeMessage(key, control);
			}
		}
	}

	public static int convertDiagnosticSeverity(int severity) {
		switch (severity) {
		case Diagnostic.ERROR: return IMessage.ERROR;
		case Diagnostic.INFO: return IMessage.INFORMATION;
		case Diagnostic.WARNING: return IMessage.WARNING;
		default: return IMessage.NONE;
		}
	}
}
