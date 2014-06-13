/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.wizard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IWorkbench;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.converter.IModelConverter;
import com.hundsun.ares.studio.core.registry.ARESResRegistry;
import com.hundsun.ares.studio.core.registry.IResDescriptor;
import com.hundsun.ares.studio.core.registry.ModuleRootType2ResTypeMap;
import com.hundsun.ares.studio.core.util.ArrayUtil;
import com.hundsun.ares.studio.ui.AresResourceCategoryFilter;
import com.hundsun.ares.studio.ui.AresResourceFilter;
import com.hundsun.ares.studio.ui.ElementSelectionWizardPageWithNameInput;
import com.hundsun.ares.studio.ui.LibFilter;

/**
 * �½���Դ��ҳ��Ļ��ࡣ
 * @author sundl
 */
public class ARESResourceWizardPage extends ElementSelectionWizardPageWithNameInput{

	private static final Logger logger = Logger.getLogger(ARESResourceWizardPage.class.getName());
	
	// ���ҳ�洴������Դ������
	private String resType;
	
	public ARESResourceWizardPage(String pageName, IWorkbench workbench, IARESElement selection, String resType) {
		super(pageName, workbench, selection);
		this.resType = resType;
	}

	@Override
	public boolean validate() {
		if (!(selection instanceof IARESModule)) {
			setErrorMessage("��ѡ��һ��Ӧ��");
			return false;
		}
		
		if(!super.validate()) {
			return false;
		}
		
		String newName = getNewName();
		IARESModule module = (IARESModule) selection;
		IARESResource exsit = module.findResource(newName, resType);
		if (exsit != null && exsit.exists()) {
			setErrorMessage("ͬ����Դ�Ѿ����ڣ�");
			return false;
		}
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.devtool.v2.ui.ARESElementSelectionWizardPage#finishPage()
	 */
	@Override
	public boolean finishPage() {
		try {
			getContainer().run(false, false, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					createResource();
				}
				
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		openEditorAndReveal();
		return true;
	}
	
	protected void createResource() {
		long t1 = System.currentTimeMillis();
		IResDescriptor resDescriptor = ARESResRegistry.getInstance().getResDescriptor(getType());
		if (resDescriptor != null) {
			Object info = resDescriptor.createInfo();
			initNewResourceInfo(info);
			IModelConverter converter = resDescriptor.getConverter();
			IARESElement selection = getSelectedElement();
			if (selection.getResource().getType() == IResource.FOLDER) {
				String name = getNewName();
				name = name + "." + getType();
				IFolder folder = (IFolder)selection.getResource();
				IFile file = folder.getFile(name);
				if (!file.exists()) {
					try {
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						converter.write(bos, info);
						file.create(new ByteArrayInputStream(bos.toByteArray()), true, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
					this.resource = file;
				}
			}			
		}
		long t2 = System.currentTimeMillis();
		logger.info("��Դ�� " + getNewName() + " �����ɹ�����ʱ" + (t2 - t1) + "ms.");
	}
	
	/**
	 * ��ʼ����Դ��Ϣ����
	 * ���������д���������ʼ��������Ϣ����Ҫ����super.initNewResourceInfo()
	 * @param info
	 */
	protected void initNewResourceInfo(Object info) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.devtool.v2.ui.ARESElementSelectionWizardPage#getType()
	 */
	public String getType() {
		return resType;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.ARESElementSelectionWizardPage#getCreatedResource()
	 */
	@Override
	public IResource getCreatedResource() {
		if (resource == null) {
			IARESElement selection = getSelectedElement();
			if (selection.getResource().getType() == IResource.FOLDER) {
				String name = getNewName();
				name = name + "." + getType();
				IFolder folder = (IFolder)selection.getResource();
				resource = folder.getFile(name);
			}			
		}
		return resource;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.CommonElementSelectionPage#addFilter()
	 * ����ģ������͹�����Դ
	 * 
	 */
	@Override
	protected void addFilter() {
		if(viewer != null){
			viewer.addFilter(new ViewerFilter(){

				@Override
				public boolean select(Viewer viewer, Object parentElement,
						Object element) {
					if(element instanceof IARESModuleRoot){
						if(resType == null || resType.length() == 0){
							return true;
						}
						String[] allowType = ModuleRootType2ResTypeMap.getInstance().getAllowedResTypes(((IARESModuleRoot) element).getType());
						if(ArrayUtil.findInArray(allowType, resType) < 0){
							return false;
						}
					}
					return true;
				}
				
			});
			
			viewer.addFilter(new LibFilter());
			viewer.addFilter(new AresResourceCategoryFilter());
			viewer.addFilter(new AresResourceFilter());
		}
	}

}
