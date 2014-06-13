/**
 * Դ�������ƣ�IEMFFormPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor;

import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.ui.forms.editor.IFormPage;

import com.hundsun.ares.studio.ui.editor.editable.IEditableControl;
import com.hundsun.ares.studio.ui.validate.IProblemPool;
import com.hundsun.ares.studio.ui.validate.IValidateControl;

/**
 * @author gongyf
 *
 */
public interface IEMFFormPage extends IFormPage, IEditingDomainProvider {
	/**
	 * ָʾģ����Ϣ�������Ҫ����ˢ�²���
	 */
	public abstract void infoChange();

	public abstract void validate();

	public abstract IProblemPool getProblemPool();

	public abstract IValidateControl getValidateControl();

	public abstract TransactionalEditingDomain getEditingDomain();

	public abstract EMFFormEditor getEditor();

	public abstract void setEditableControl(IEditableControl editableControl);
}
