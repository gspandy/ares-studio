/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.ui.editor.blocks;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

import com.hundsun.ares.studio.ui.editor.EMFKeyConstructor;
import com.hundsun.ares.studio.ui.editor.FormProblemPool;
import com.hundsun.ares.studio.ui.editor.validate.DefaultValidateControl;
import com.hundsun.ares.studio.ui.editor.validate.EMFAllValidateUnit;
import com.hundsun.ares.studio.ui.validate.IProblemPool;
import com.hundsun.ares.studio.ui.validate.IValidateControl;
import com.hundsun.ares.studio.validate.ValidateUtil;

/**
 * ���Խ��д��������չҳ��Ҫ����Ի�ȡIARESResource
 * @author gongyf
 *
 */
public abstract class EMFESSFormPageWithValidate<T> extends
		EMFExtendSectionScrolledFormPage<T> {

	private IValidateControl validateControl;
	private IProblemPool problemPool;
	private boolean validateSystemInitialized = false;
	
	/**
	 * ��ʼ����������ƣ��첽��Ϊ�˷�ֹ�����߳��Ͽ���
	 */
	private Job validateSystemInitJob = new Job("�������ʼ��") {
		
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			
			validateControl = createValidateControl();
			connectValidateControlToProblemPool();
			configureValidateControl();
			
			validateSystemInitialized = true;
			
			validate();
			
			return Status.OK_STATUS;
		}
	};
	
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public EMFESSFormPageWithValidate(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	/**
	 * @return the validateControl
	 */
	public IValidateControl getValidateControl() {
		return validateControl;
	}
	
	protected void disposeValidateControl() {
		if (validateControl != null) {
			validateControl.destroyAll();
		}
		
	}
	
	protected IProblemPool createProblemPool() {
		IProblemPool problemPool = new FormProblemPool(getManagedForm().getMessageManager());
		problemPool.setKeyConstructor(new EMFKeyConstructor());
		return problemPool;
	}
	
	/**
	 * @return the problemPool
	 */
	public IProblemPool getProblemPool() {
		return problemPool;
	}
	
	
	protected void connectValidateControlToProblemPool() {
		validateControl.setProblemPool(problemPool);
	}
	
	public void validate() {
		// ֻ�д�����ϵͳ��ʼ�����ұ༭�����������ð��д򿪵�����²���������
		if (validateSystemInitialized && !isInReferencedLibrary()) {
			getValidateControl().refresh();
		}
	}
	
	/**
	 * ���ô����������
	 * ��Ӽ�鵥Ԫ
	 * ���ü��������
	 */
	protected void configureValidateControl() {
		getValidateControl().addValidateUnit(new EMFAllValidateUnit(getModel()));
		getValidateControl().setContext(ValidateUtil.getValidateContext(getResource()));
	}
	
	protected void resourceSetChanged(ResourceSetChangeEvent event) {
		super.resourceSetChanged(event);
		if (isNeedValidate(event)) {
			validate();
		}
	}
	
	protected boolean isNeedValidate(ResourceSetChangeEvent event) {
		return true;
	}

	protected IValidateControl createValidateControl() {
		return new DefaultValidateControl();
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.page.ExtendSectionScrolledFormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	final protected void createFormContent(IManagedForm managedForm) {

		problemPool = createProblemPool();
		
		getEditingDomain().getCommandStack().addCommandStackListener(this);
		
		super.createFormContent(managedForm);
		
		validateSystemInitJob.schedule();
		
		validate();
	}
	
}
