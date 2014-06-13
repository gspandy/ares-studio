/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.control;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.ui.dialog.ARESResourceSelectionDialog;
import com.hundsun.ares.studio.ui.dialog.ConditionalResourceSelectionDialog;

/**
 * ��Ҫ����Դѡ���б��й��˵�һ���ֽ��ʱʹ�õ�ARES��Դ������ʾѡ��༭��
 * 
 * @see com.hundsun.ares.studio.ui.control.ARESResourceAssistantAndSelectionField
 * @author yanyl
 */
public abstract class ConditionalARESResourceAssistantAndSelectionField extends ARESResourceAssistantAndSelectionField {
	public ConditionalARESResourceAssistantAndSelectionField(Composite parent, String labelStr, int txtStyle,
			String resType, String dialogTitle) {
		super(parent, labelStr, txtStyle, resType, dialogTitle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hundsun.ares.studio.ui.control.ARESResourceAssistantAndSelectionField
	 * #createDialog(com.hundsun.ares.studio.core.IARESProject,
	 * java.lang.String)
	 */
	@Override
	protected ARESResourceSelectionDialog createDialog(IARESProject project, String resType) {
		return new ConditionalResourceSelectionDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				project, resType) {
			@Override
			public String[] getFilterExceptResources() {
				return getExceptResources();
			}
		};
	}

	/**
	 * ��ȡҪ�ų�����Դȫ·����û��Ҫ�ų���Դ�Ļ����Է���null
	 * 
	 */
	public abstract String[] getExceptResources();
}
