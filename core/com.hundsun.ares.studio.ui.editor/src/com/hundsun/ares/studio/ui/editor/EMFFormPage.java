/**
 * Դ�������ƣ�EMFFormPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor;

import java.util.EventObject;
import java.util.HashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;

import com.hundsun.ares.studio.core.model.JRESResourceInfo;
import com.hundsun.ares.studio.ui.editor.editable.IEditableControl;
import com.hundsun.ares.studio.ui.editor.validate.DefaultValidateControl;
import com.hundsun.ares.studio.ui.editor.validate.EMFAllValidateUnit;
import com.hundsun.ares.studio.ui.validate.IProblemPool;
import com.hundsun.ares.studio.ui.validate.IValidateControl;
import com.hundsun.ares.studio.validate.ValidateUtil;

/**
 * <ul>
 * <li></li>
 * </ul>
 * @author gongyf
 *
 */
public abstract class EMFFormPage extends FormPage implements CommandStackListener, IEMFFormPage {
	
	private IValidateControl validateControl;
	private IProblemPool problemPool;
	private boolean validateSystemInitialized = false;
	
	private IEditableControl editableControl;
	private ResourceSetListener resourceSetListener = new ResourceSetListenerImpl(){
		/* (non-Javadoc)
		 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#resourceSetChanged(org.eclipse.emf.transaction.ResourceSetChangeEvent)
		 */
		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			EMFFormPage.this.resourceSetChanged(event);
		}
	};
	
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
	
	public EMFFormPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
		
		// ���ģ�ͼ�����
		((TransactionalEditingDomain)getEditor().getEditingDomain()).addResourceSetListener(resourceSetListener);
	}

	/**
	 * @param editableControl the editableControl to set
	 */
	@Override
	public void setEditableControl(IEditableControl editableControl) {
		this.editableControl = editableControl;
	}
	
	protected IEditableControl getEditableControl() {
		return editableControl;
	}
	
	@Override
	public EMFFormEditor getEditor() {
		return (EMFFormEditor) super.getEditor();
	}
	
	@Override
	public TransactionalEditingDomain getEditingDomain() {
		return getEditor().getEditingDomain();
	}
	
	protected IValidateControl createValidateControl() {
		return new DefaultValidateControl();
	}
	
	/**
	 * @return the validateControl
	 */
	@Override
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
	@Override
	public IProblemPool getProblemPool() {
		return problemPool;
	}
	
	
	protected void connectValidateControlToProblemPool() {
		validateControl.setProblemPool(problemPool);
	}
	
	@Override
	public void validate() {
		// ֻ�д�����ϵͳ��ʼ�����ұ༭�����������ð��д򿪵�����²���������
		if (validateSystemInitialized && !getEditor().isInReferencedLibrary()) {
			getValidateControl().refresh();
		}
	}
	
	protected void resourceSetChanged(ResourceSetChangeEvent event) {
		if (isNeedValidate(event)) {
			validate();
		}
	}
	
	protected boolean isNeedValidate(ResourceSetChangeEvent event) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.command.CommandStackListener#commandStackChanged(java.util.EventObject)
	 */
	@Override
	public void commandStackChanged(EventObject event) {
		// do nothing
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	final protected void createFormContent(IManagedForm managedForm) {

		problemPool = createProblemPool();
		
		getEditingDomain().getCommandStack().addCommandStackListener(this);
		
		doCreateFormContent(managedForm);
		
		// ��ֹ��windows����ģʽ��û�б߿������
		managedForm.getToolkit().paintBordersFor(managedForm.getForm().getBody());
		
		infoChange();
		validateSystemInitJob.schedule();
	}
	
	protected abstract void doCreateFormContent(IManagedForm managedForm);
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.IEMFFormPage#infoChange()
	 */
	@Override
	public void infoChange() {
		//ˢ��ֻ��״̬
		getEditableControl().refreshAllUnit(new HashMap<Object, Object>());
		
		validate();
	}
	
	protected JRESResourceInfo getInfo() {
		return (JRESResourceInfo) getEditor().getInfo();
	}
	
	/**
	 * ���ô����������
	 * ��Ӽ�鵥Ԫ
	 * ���ü��������
	 */
	protected void configureValidateControl() {
		getValidateControl().addValidateUnit(new EMFAllValidateUnit(getInfo()));
		getValidateControl().setContext(ValidateUtil.getValidateContext(getEditor().getARESResource()));
	}
	
	@Override
	public void dispose() {
		((TransactionalEditingDomain)getEditor().getEditingDomain()).removeResourceSetListener(resourceSetListener);
		disposeValidateControl();
		super.dispose();
	}
	
}
