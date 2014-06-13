/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.newwizard;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.BasicResourceInfo;
import com.hundsun.ares.studio.core.model.converter.IModelConverter;
import com.hundsun.ares.studio.core.model.converter.IModelConverter2;
import com.hundsun.ares.studio.core.registry.ARESResRegistry;
import com.hundsun.ares.studio.core.registry.IResDescriptor;
import com.hundsun.ares.studio.ui.ARESResourceCategory;

/**
 * ARES��Դ�����򵼵Ļ��ࡣ
 * 
 * @author lvgao
 */
public abstract class ARESResourceNewWizard extends Wizard implements INewWizard, IAresWizard {

	protected static Logger logger = Logger.getLogger(ARESResourceNewWizard.class);

	protected IResource resource;
	protected IWorkbench workbench;
	protected IARESElement selectedElement;

	Map<Object, Object> context = new HashMap<Object, Object>();

	List<IAresWizardPage> pageList = new ArrayList<IAresWizardPage>();

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		if (selection != null) {
			Object selected = selection.getFirstElement();
			if (selected instanceof ARESResourceCategory) {
				selectedElement = ((ARESResourceCategory) selected).getModule();
			} else if (selected instanceof IARESResource) {
				selectedElement = ((IARESResource) selected).getModule();
			} else if (selected instanceof IARESElement) {          //ģ��
				selectedElement = (IARESElement) selected;
			}else if (selected instanceof IARESModuleRoot) {   //ģ���
				selectedElement = (IARESModuleRoot) selected;
			} else if (selected instanceof IResource) {
				selectedElement = ARESCore.create((IResource) selected);
				if (selectedElement == null) {
					selectedElement = ARESCore.create(((IResource) selected).getProject());
				}
			}
		}
		if (selectedElement == null) {
			IARESProject[] projects;
			try {
				projects = ARESCore.getModel().getARESProjects();
				if (projects.length != 0) {
					selectedElement = projects[0];
				}
			} catch (ARESModelException e) {
				e.printStackTrace();
			}

		}

		IResDescriptor resDescriptor = ARESResRegistry.getInstance()
				.getResDescriptor(getResType());
		setWindowTitle("�½�" + resDescriptor.getName());
		this.workbench = workbench;
	}

	/**
	 * �����������
	 * 
	 * @param page
	 */
	protected void addContextPage(IAresWizardPage page) {
		pageList.add(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hundsun.ares.studio.jres.ui.wizards.IAresWizard#getContext()
	 */
	@Override
	public Map<Object, Object> getContext() {
		context.clear();
		for (IAresWizardPage page : pageList) {
			if (null != page) {
				page.setContext(context);
			}
		}
		return context;
	}

	public void addPages() {
		IResDescriptor resDescriptor = ARESResRegistry.getInstance()
				.getResDescriptor(getResType());
		ARESResourceNewWizardPage page = new ARESResourceNewWizardPage("�½�һ��"
				+ resDescriptor.getName(), workbench, selectedElement,
				getResType());
		page.setDescription("�½�һ��" + resDescriptor.getName());
		// page.setNewName(initText_Name);
		addPage(page);
		addContextPage(page);
	}

	/**
	 * ��ȡ��Դ����
	 * 
	 * @return
	 */
	protected abstract String getResType();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		try {
			getContainer().run(false, false, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					createResource(getContext());
				}

			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		ARESWizardUtil.openEditorAndReveal(workbench, resource);
		return true;
	}

	protected boolean createResource(Map<Object, Object> context) {
		// ���������л�ȡ������Դ����Ҫ������
		String resname = context
				.get(ARESResourceNewWizardPage.CONTEXT_KEY_NAME).toString();
		IARESElement selection = (IARESElement) context
				.get(ARESResourceNewWizardPage.CONTEXT_KEY_SELECTION);
		String restype = context.get(
				ARESResourceNewWizardPage.CONTEXT_KEY_RES_TYPE).toString();

		// �µ���Դȫ��
		String resFullName = String.format("%s.%s", resname, restype);

		long t1 = System.currentTimeMillis();
		IResDescriptor resDescriptor = ARESResRegistry.getInstance()
				.getResDescriptor(restype);
		if (resDescriptor != null) {
			Object info = resDescriptor.createInfo();
			initNewResourceInfo(info);
			IModelConverter converter = resDescriptor.getConverter();
			if (selection.getResource().getType() == IResource.FOLDER) {
				IFolder folder = (IFolder) selection.getResource();
				IFile file = folder.getFile(resFullName);
				if (!file.exists()) {
					try {
						IARESResource resource = (IARESResource) ARESCore
								.create(file);
						file.create(
								new ByteArrayInputStream(
										((IModelConverter2) converter).write(
												resource, info)), true, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
					this.resource = file;
				}
			}
		}
		long t2 = System.currentTimeMillis();
		logger.info("��Դ�� " + resFullName + " �����ɹ�����ʱ" + (t2 - t1) + "ms.");
		return false;
	}

	/**
	 * ��ʼ����Դ��Ϣ���� ���������д���������ʼ��������Ϣ����Ҫ����super.initNewResourceInfo()
	 * 
	 * @param info
	 */
	protected void initNewResourceInfo(Object info) {
		// do nothing
		if (info instanceof BasicResourceInfo) {
			((BasicResourceInfo) info).setGroup((String) getContext().get(ARESResourceNewWizardPage.CONTEXT_KEY_GROUP));
		}
	}

}
