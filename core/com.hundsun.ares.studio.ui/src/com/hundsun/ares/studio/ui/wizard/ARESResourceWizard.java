/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.registry.ARESResRegistry;
import com.hundsun.ares.studio.core.registry.IResDescriptor;
import com.hundsun.ares.studio.ui.ARESElementWizard;
import com.hundsun.ares.studio.ui.ARESResourceCategory;


/**
 * ARES��Դ�����򵼵Ļ��ࡣ
 * @author sundl
 */
public abstract class ARESResourceWizard extends ARESElementWizard {

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		Object obj = selection.getFirstElement();
		if (obj instanceof ARESResourceCategory) {
			selectedElement = ((ARESResourceCategory)obj).getModule();
		} else if (obj instanceof IARESResource) {
			selectedElement = ((IARESResource) obj).getModule();
		}
		IResDescriptor resDescriptor = ARESResRegistry.getInstance().getResDescriptor(getResType());		
		setWindowTitle("�½�" + resDescriptor.getName());
	}
	
	public void addPages() {
		IResDescriptor resDescriptor = ARESResRegistry.getInstance().getResDescriptor(getResType());		
		page = new ARESResourceWizardPage("�½�һ��" + resDescriptor.getName(), workbench, selectedElement, getResType());
		page.setDescription("�½�һ��" + resDescriptor.getName());
		page.setNewName(initText_Name);
		addPage(page);
	}
	
	protected abstract String getResType();
	
}
