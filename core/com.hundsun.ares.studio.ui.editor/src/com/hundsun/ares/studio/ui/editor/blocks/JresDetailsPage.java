/**
 * Դ�������ƣ�JresDetailsPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.blocks;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.Section;

import com.hundsun.ares.studio.core.util.StringUtil;
import com.hundsun.ares.studio.ui.editor.IEMFFormPage;

/**
 * @author qinyuan
 *
 */
public abstract class JresDetailsPage  implements IDetailsPage {

	protected IEMFFormPage page;
	protected IManagedForm form;
	protected Section detail;
	protected Object input;
	protected DataBindingContext bindingContext;
	
	public void setPage(IEMFFormPage page) {
		this.page = page;
	}
	
	public void createContents(Composite parent) {
		parent.setLayout(new FillLayout());
		detail = form.getToolkit().createSection(parent, Section.TITLE_BAR);
		detail.setText(StringUtil.getStringSafely(getSectionName()));

		GridLayout groupLayout = new GridLayout(2, false);
		detail.setLayout(groupLayout);

		Composite client = form.getToolkit().createComposite(detail, SWT.WRAP);
		client.setLayout(getSectionLayout());

		Label descLabel = form.getToolkit().createLabel(client, StringUtil.getStringSafely(getDescription()));
		setDescLabelDescriptionConstraint(descLabel);		
		createSectionContents(client);

		detail.setClient(client);
		// �߿�
		form.getToolkit().paintBordersFor(client);
	}
	
	

	public void initialize(IManagedForm form) {
		this.form = form;
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isStale() {
		return false;
	}

	protected Layout getSectionLayout(){
		return new GridLayout(2, false);
	}
	
	public void commit(boolean onSave) {
		
	}

	public void dispose() {
		
	}

	public void setFocus() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * ��ȡdetail�ı���
	 * @return
	 */
	protected abstract String getSectionName();
	/**
	 * ��ȡdetail��������Ϣ
	 * @return
	 */
	protected abstract String getDescription();
	
	protected  void setDescLabelDescriptionConstraint(Label descLabel){
		descLabel.setLayoutData(new GridData(SWT.BEGINNING,SWT.TOP,false,false,3,1));
	}
	
	/**
	 * ����section������
	 * @param client
	 */
	protected abstract void createSectionContents(Composite client);
	
	public void selectionChanged(IFormPart part, ISelection selection) {
		Object sel = ((StructuredSelection)selection).getFirstElement();
		setFormInput(sel);
	}
	
	
	public void refresh() {
	}

	public boolean setFormInput(Object input) {
		try {
			this.input = input;
			
			if (bindingContext != null) {
				Realm.runWithDefault(SWTObservables.getRealm(form.getForm().getDisplay()), new Runnable() {
					@Override
					public void run() {
						bindingContext.dispose();
					}
				});
			}
			
			bindingContext = databinding();
			
			refresh();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * ���ݰ�
	 * @return
	 */
	protected abstract DataBindingContext databinding();
}
