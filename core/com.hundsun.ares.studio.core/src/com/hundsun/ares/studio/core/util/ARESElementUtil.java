/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESBundle;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.IReferencedLibrary;
import com.hundsun.ares.studio.core.model.BasicResourceInfo;
import com.hundsun.ares.studio.core.model.ICommonModel;

/**
 * 
 * @author sundl
 */
public class ARESElementUtil {
	
	private static Logger logger = Logger.getLogger(ARESElementUtil.class);

	public static final String UNKNOWN = "ares.unknown";
	
	public static final IARESElement[] NO_ELEMENT = new IARESElement[0];
	
	/**
	 * ת����
	 * 
	 * @param elements
	 * @return
	 */
	public static IARESElement[] toARESElement(Object[] elements) {
		List<IARESElement> result = new ArrayList<IARESElement>();
		for (Object obj : elements) {
			if (obj instanceof IResource) {
				IARESElement aresEelement = ARESCore.create((IResource) obj);
				if (aresEelement != null) {
					result.add(aresEelement);
				}
			} else if (obj instanceof IARESElement) {
				result.add((IARESElement) obj);
			}
		}
		return result.toArray(new IARESElement[result.size()]);
	}

	/**
	 * ת����
	 * 
	 * @param element
	 * @return
	 */
	public static IARESElement toARESElement(Object obj) {
	
		if (obj instanceof IResource) {
			IARESElement aresEelement = ARESCore.create((IResource) obj);
			if (aresEelement != null) {
				return aresEelement;
			}
		} else if (obj instanceof IARESElement) {
			return (IARESElement) obj;
		}
		return null;
	}

	/**
	 * ת����IResource����
	 * @param elements ��ת��������
	 * @return ת���������
	 */
	public static IResource[] toResource(Object[] elements) {
		List<IResource> results = new ArrayList<IResource>();
		for (Object obj : elements) {
			if (obj instanceof IResource) {
				results.add((IResource) obj);
			} else if (obj instanceof IARESElement) {
				IARESElement aresElement = (IARESElement) obj;
				IResource res = aresElement.getResource();
				if (res != null) {
					results.add(res);
				}
			}
		}
		return results.toArray(new IResource[results.size()]);
	}

	/**
	 * ת����ָ����չ��IResource����
	 * 
	 * @param elements ��ת��������
	 * @param extension ��ƥ�����չ��
	 * @return ת���������
	 */
	public static IResource[] toResource(Object[] elements, String extension) {
		List<IResource> results = new ArrayList<IResource>();
		for (Object obj : elements) {
			if (obj instanceof IResource) {
				String srcExtension = ((IResource)obj).getFileExtension();
				if (srcExtension.equals(extension)) {
					results.add((IResource)obj);
				}
			}
		}
		return results.toArray(new IResource[results.size()]);
	}

	/**
	 * �жϸ��������Ԫ���Ƿ��Ǹ��������͡�
	 * 
	 * @param elements
	 *            Ԫ������
	 * @param type
	 *            ���ʹ���
	 * @return ���ȫ����ָ�������ͣ��򷵻�<code>true</code>
	 */
	public static boolean elementsIsOfType(Object[] elements, int type) {
		if (elements.length == 0) {
			return false;
		}
	
		for (Object o : elements) {
			if (!ARESElementUtil.elementIsOfType(o, type)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * �жϸ�����Ԫ���Ƿ���������͡�
	 * 
	 * @param element
	 *            Ҫ�жϵ�Ԫ��
	 * @param type
	 *            ���ʹ���
	 * @return �����ָ�������ͣ��򷵻�true������ǲ�֧�ֵ����ͣ��򷵻�false��
	 * @see IARESElement
	 */
	public static boolean elementIsOfType(Object element, int type) {
		switch (type) {
		case IARESElement.COMMON_MODULE:
			return element instanceof IARESModule;
		case IARESElement.ARES_RESOURCE:
			return element instanceof IARESResource;
		case IARESElement.COMMON_MODULE_ROOT:
			return element instanceof IARESModuleRoot;
		case IARESElement.ARES_PROJECT:
			return element instanceof IARESProject;
		}
		return false;
	}

	/**
	 * ת��IARESResource���飻����ǰ��Ҫ�Լ��ж��Ƿ�ȫ��ARESResource,��������
	 * @param elements
	 * @return
	 */
	public static IARESResource[] toARESResource(IARESElement[] elements) {
		IARESResource[] resources = new IARESResource[elements.length];
		System.arraycopy(elements, 0, resources, 0, elements.length);
		return resources;
	}

	public static IARESModule[] toARESModule(IARESElement[] elements) {
		IARESModule[] modules = new IARESModule[elements.length];
		System.arraycopy(elements, 0, modules, 0, elements.length);
		return modules;
	}

	/**
	 * �Ƿ���ͬһ��ģ���£���ʱû�����⴦��Ĭ��ģ�飬����Ĭ��ģ��͵�һ��ģ����ɵ�����᷵��true
	 * @param modules
	 * @return
	 */
	public static boolean modulesHasSameParent(IARESModule[] modules) {
		if (modules == null || modules.length ==0) 
			return false;
		
		IARESModule firstParentModule = modules[0].getParentModule();
		for (int i = 1; i < modules.length; i++) {
			IARESModule module = modules[i];
			if (firstParentModule == null) {
				if (module.getParentModule() == null) {
					continue;
				} else {
					return false;
				}
			} else {
				if (firstParentModule.equals(module.getParentModule())) {
					continue;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static final IARESElement getModulesParent(IARESModule[] modules) {
		IARESModule firstParent = modules[0].getParentModule();
		for (int i = 1; i < modules.length; i++) {
			IARESModule module = modules[i];
			if (firstParent != null && firstParent.equals(module.getParentModule())) {
				continue;
			} else if (firstParent == null && module.getParentModule() == null) {
				continue;
			} else {
				return null;
			}
		}
		
		if (firstParent == null) {
			return modules[0].getRoot();
		}
		return firstParent;
	}

	/**
	 * �ж��Ƿ�������Դ��ָ��������
	 * @param resources
	 * @param type
	 * @return
	 */
	public static boolean resourcesOfType(IARESResource[] resources, String type) {
		String realType = getResourcesType(resources);
		return realType.equals(type);
	}

	public static String getResourcesType(IARESResource[] resources) {
		int count = resources.length;
		if (count == 0)
			return UNKNOWN;
		
		String firstType = resources[0].getType();
		for (int i = 1; i < count; i++) {
			if (firstType.equals(resources[i].getType())) {
				continue;
			} else {
				return UNKNOWN;
			}
		}
		return firstType;
	}
	
	/**
	 * ���ظ�����AresResource�����а�����������Դ����
	 * @param resources AresResoruce����
	 * @return ���а�������Դ����
	 */
	public static String[] getResourcesTypes(IARESResource[] resources) {
		Set<String> types = new HashSet<String>();
		for (IARESResource res : resources) {
			types.add(res.getType());
		}
		return types.toArray(new String[0]);
	}

	/**
	 * �жϸ�����Ԫ�ؼ����Ƿ���ͬһ����������
	 * @param elements
	 * @return
	 */
	public static boolean hasSameParent(IARESElement[] elements) {
		if (elements == null || elements.length == 0)
			return false;
		if (elements.length == 1)
			return true;
		
		IARESElement parent = elements[0].getParent();
		if (parent == null)
			return false;
		
		for (int i = 1; i < elements.length; i++) {
			if (!elements[i].getParent().equals(parent))
				return false;
		}
		
		return true;
	}

	public static IARESElement getParent(IARESElement[] elements) {
		if (hasSameParent(elements)) {
			return elements[0].getParent();
		}
		return null;
	}

	/**
	 * һ������ͨ�����ַ�ʽ��ͼ��ȡ��Ӧ��ICommonMode����ͨ��ICommonModel������ͳһ�Ľӿڻ�ȡ��Դ����������
	 * 1. ���������ʵ����ICommonModel�ӿڣ�ֱ�ӷ���
	 * 2. �������ʵ����IAdaptable�ӿڣ�ͨ������ӿ�����
	 * 3. ͨ��AdapterManager����չ�㣩���ƻ�ȡ
	 * @param model
	 * @return
	 */
	public static ICommonModel getCommonModel(Object model) {
		ICommonModel commonModel = null;
		if (model == null) {
			commonModel = null;
		} else if (model instanceof ICommonModel) {
			commonModel = (ICommonModel)model;
		} else if (model instanceof IAdaptable) {
			IAdaptable adapter = (IAdaptable)model;
			commonModel = (ICommonModel) adapter.getAdapter(ICommonModel.class);
		} else {
			IAdapterManager manager = Platform.getAdapterManager();
			commonModel = (ICommonModel)manager.getAdapter(model, ICommonModel.class);
		}
		return commonModel;
	}
	
	/**
	 * ���ص�һ��ģ�飬����aaa.bb.c, ���һ��ģ�����aaa���ģ��
	 * @param resource
	 * @return
	 */
	public static IARESModule getTopModule(IARESResource resource) {
		IARESModuleRoot root = resource.getRoot();
		IARESModule module = resource.getModule();
		if (isTopModule(module))
			return module;
//		// ģ�����ڴ�ģ���ж���ƽ���ģ�������ģ��ȡ�������������ж�
//		try {
//			for (IARESModule m : root.getModules()) {
//				if (module.getElementName().startsWith(m.getElementName() + "."))
//					return m;
//			}
//		} catch (ARESModelException e) {
//			e.printStackTrace();
//		}
//		return null;
		
		String moduleName = StringUtils.substringBefore(module.getElementName(), ".");
		return root.getModule(moduleName);
	}
	
	public static IARESModule getTopModule(IARESModule module) {
		IARESModule parent = module;
		while(!isTopModule(parent)) {
			parent = module.getParentModule();
		}
		return parent;
	}
	
	/**
	 * �ж�ָ����ģ���Ƿ��һ��ģ��.
	 * @param module
	 * @return
	 */
	public static boolean isTopModule(IARESModule module) {
		return module.getElementName().indexOf('.') == -1 ;
	}

	/** JRES20���̣��Ϲ��̣��µ����ݿ���Դģ������� */
	public static final String OLD_DB_MODULE_ROOT_TYPE = "com.hundsun.ares.studio.jres.database";

	/** JRES20���̣��Ϲ��̣��µ�Ԫ������Դģ������� */
	public static final String OLD_META_MODULE_ROOT_TYPE = "com.hundsun.ares.studio.publicres.rootpublicres";

	/** ��JRES�����е����ݿ�ģ���
	 *  �������Լ���ķ�ʽ������Ĳ������ģ�����ʱ�򣬱����������ַ���ֵ��ͬ��
	 */
	public static final String DB_MODULE_ROOT_TYPE = "com.hundsun.ares.studio.jres.moduleroot.database";
	public static final String BUS_MODULE_ROOT_TYPE = "com.hundsun.ares.studio.jres.moduleroot.business";
	public static final String MD_MODULE_ROOT_TYPE = "com.hundsun.ares.studio.jres.moduleroot.metadata";
	
	
	/**
	 * UFT����ģ�����UFT������޸ļ�¼Ҫ��ģ���Ϊ���ӻ���
	 */
	public static final String UFT_STRUCTURE_ROOT_TYPE = "com.hundsun.ares.studio.uft.moduleroot.structure";
	
	/**
	 * �жϸ�����ģ��������Ƿ�UFT����ģ���
	 * @param type
	 * @return
	 */
	public static boolean isUFTStructureRoot(String type){
		return StringUtils.equals(UFT_STRUCTURE_ROOT_TYPE, type);
	}

	/**
	 * �жϸ�����ģ��������Ƿ����ݿ�ģ����� ���������Ϊ�˼����Ϲ�����ʹ�õ��ϵ����ݿ�ģ�������
	 * @param type ģ�������ID
	 * @return 
	 */
	public static boolean isDatabaseRoot(String type) {
		return StringUtils.equals(DB_MODULE_ROOT_TYPE, type)
				|| StringUtils.equals(OLD_DB_MODULE_ROOT_TYPE, type);
	}

	/**
	 * �ж��Ƿ�Ԫ����ģ���
	 * @param type
	 * @return
	 */
	public static boolean isMetadataRoot(String type) {
		return StringUtils.equals(MD_MODULE_ROOT_TYPE, type) 
				|| StringUtils.equals(OLD_META_MODULE_ROOT_TYPE, type);
	}

	public static IARESBundle getARESBundle(IARESElement aresElement) {
		if (aresElement instanceof IARESResource) {
			return ((IARESResource) aresElement).getBundle();
		} else {
			return aresElement.getARESProject();
		}
	}

	/**
	 * ��ѯ���е����ð�
	 * 
	 * @param project
	 * @return
	 */
	public static List<IReferencedLibrary> getReferenceLib(IARESProject project){
		Set<IReferencedLibrary> refLibs = new HashSet<IReferencedLibrary>();
		try {
			//���ӱ����̵����ð�
			refLibs.addAll(addLib(project));
			//�������ù��̵����ð�
			List<IARESProject> projects = getRequiredProjects(project);
			for (IARESProject pro : projects) {
				refLibs.addAll(addLib(pro));
			}
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
		return new ArrayList<IReferencedLibrary>(refLibs);
	}

	/**
	 * ��ѯ���е����ù���
	 * 
	 * @param project ��ǰ���̶���
	 * @return
	 */
	public static List<IARESProject> getRequiredProjects(IARESProject project){
		List<IARESProject> projects = new ArrayList<IARESProject>();
		if (project == null) {
			return projects;
		}
		projects.add(project);
		checkCYC(project, new ArrayList<IARESProject>());
		getReqProjects(project, projects);
		return projects;
	}

	/**
	 * �жϹ����Ƿ�ѭ������
	 * 
	 * @param project ��ǰ����
	 * @param mainProject �ظ���������
	 * @return
	 */
	public static boolean checkCYC (IARESProject project,List<IARESProject> mainProject){
		mainProject.add(project);
		IARESProject[] pros = project.getRequiredProjects();
		for (IARESProject pro : pros) {
			if (mainProject.contains(pro)) {
				logger.error("���̣�["+project.getElementName()+"],ѭ�����ã�["+pro.getElementName()+"]");
				ConsoleHelper.getLogger().error("���̣�["+project.getElementName()+"],ѭ�����ã�["+pro.getElementName()+"]");
				return false;
			}
			if (pro.getRequiredProjects().length > 0) {
				if (!checkCYC(pro, mainProject)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static List<IReferencedLibrary> addLib(IARESProject project) throws ARESModelException{
		List<IReferencedLibrary> refLibs = new ArrayList<IReferencedLibrary>();
		if (project == null) {
			return refLibs;
		}
		IReferencedLibrary[] libs = project.getReferencedLibs();
		for (IReferencedLibrary lib : libs) {
			refLibs.add(lib);
		}
		return refLibs;
	}
	
	/**
	 * �ݹ�������е����ù��� 
	 */
	public static void getReqProjects(IARESProject project,List<IARESProject> projects){
		IARESProject[] pros = project.getRequiredProjects();
		for (IARESProject pro : pros) {
			if (!projects.contains(pro)) {
				projects.add(pro);
			}else {
				continue;
			}
			if (pro.getRequiredProjects().length > 0) {
				getReqProjects(pro, projects);
			}
		}
	}

	/**
	 * ��ȡ��ǰARESProject�����ù��̺����ð�
	 * 
	 * @return
	 */
	public static IARESBundle[] getRefARESProjects(IARESProject project){
		List<IARESBundle> elements = new ArrayList<IARESBundle>();
		List<IARESProject>  projects = getRequiredProjects(project);
		List<IReferencedLibrary> reflibs = getReferenceLib(project);
		elements.addAll(projects);
		elements.addAll(reflibs);
		return elements.toArray(new IARESBundle[elements.size()]);
	}

	/**
	 * ��ѯָ�������£��������ð��������ù��̵�ָ����Դ
	 * 
	 * @param project
	 * @param resourceTypes
	 * @return
	 */
	public static IARESResource[] getAllResourceFromRefInType(IARESProject project ,String[] resourceTypes ){
	
		List<IARESResource> totalRes = new ArrayList<IARESResource>();
		List<IReferencedLibrary> reflibs = getReferenceLib(project);
		List<IARESProject>  projects = getRequiredProjects(project);
		//�������ð�
		for (IReferencedLibrary reflib : reflibs) {
			try {
				totalRes.addAll(Arrays.asList(reflib.getResources(resourceTypes)));
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
		}
		//�������ù���
		for(IARESProject pro : projects){
			try {
				totalRes.addAll(Arrays.asList(pro.getResources(resourceTypes)));
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
		}
		return totalRes.toArray(new IARESResource[totalRes.size()]);
	
	}

	/**
	 * ��ȡָ��id��ģ����ļ���
	 * @param project
	 * @param id
	 * @return
	 */
	public static IFolder getModuleRootFolder(IARESProject project, String id) {
		if (project == null)
			return null;
		try {
			for (IARESModuleRoot moduleRoot : project.getModuleRoots()) {
				if (StringUtils.equals(moduleRoot.getType(), id)) {
					return (IFolder) moduleRoot.getResource();
				}
			}
		} catch (ARESModelException e) {
		}
		return null;
	}
	
	/**
	 * ��ָ������Ŀ��������Ŀ�����ð��в�����Դ��
	 * ���ܷ���null
	 * @param project
	 * @param fullName
	 * @param resType
	 * @return
	 */
	public static IARESResource findResource(IARESProject project, String fullName, String resType) {
		IARESBundle[] bundles = getRefARESProjects(project);
		for (IARESBundle bundle : bundles) {
			try {
				IARESResource res = bundle.findResource(fullName, resType);
				if (res != null)
					return res;
			} catch (ARESModelException e) {
				logger.error("", e);
			}
		}
		logger.error(String.format("�Ҳ�����Դ%s������%s", fullName, resType));
		return null;
	}

	/**
	 * ��ȡ��Դ�ķ�����Ϣ��
	 * @param resource
	 * @return ���ܷ���null
	 */
	public static String getGroup(IARESResource resource) {
		try {
			BasicResourceInfo info = resource.getInfo(BasicResourceInfo.class);
			if (info != null) {
				return info.getGroup();
			}
		} catch (ARESModelException e) {
			logger.error(e);
		}
		return null;
	}
	
	public static final String getModuleCName(IARESModule module) {
		IARESResource property = module.getARESResource(IARESModule.MODULE_PROPERTY_FILE);
		if (property != null && property.exists()) {
			String cname = ResourcesUtil.getCName(property);
			return cname;
		}
		return null;
	}
	
	public static final String getModuleFullCName(IARESModule module, String seperator) {
		IARESModule parentModule = module;
		String fullname = getModuleCName(module);
		while (!isTopModule(parentModule)) {
			parentModule = module.getParentModule();
			fullname = getModuleCName(parentModule) + seperator + fullname;
		}
		return fullname;
	}
	
}
