/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESProblem;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESProjectProperty;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.IProjectProperty;
import com.hundsun.ares.studio.core.IRespathProvider;
import com.hundsun.ares.studio.core.preferences.ErrorCheckPreferenceHelper;
import com.hundsun.ares.studio.core.registry.ARESContextRegistry;
import com.hundsun.ares.studio.core.registry.IProjectValidatorDescriptor;
import com.hundsun.ares.studio.core.registry.IRespathProviderDescriptor;
import com.hundsun.ares.studio.core.registry.ProjectValidatorRegistry;
import com.hundsun.ares.studio.core.registry.RefResourcesProviderRegistry;
import com.hundsun.ares.studio.core.registry.ResValidaterRegistry;
import com.hundsun.ares.studio.core.registry.RespathProviderRegistry;
import com.hundsun.ares.studio.core.validate.DefaultContextProvider;
import com.hundsun.ares.studio.core.validate.IAresContext;
import com.hundsun.ares.studio.core.validate.IProjectPropertyValidator;
import com.hundsun.ares.studio.core.validate.IRefResourceForBuilderProvider;
import com.hundsun.ares.studio.core.validate.IRefResourceProvider;
import com.hundsun.ares.studio.core.validate.IResValidator;
import com.hundsun.ares.studio.internal.core.ARESProject;

/**
 * ��ĿBuilder.
 * @author sundl
 */
public class AresProjectBuilder extends IncrementalProjectBuilder implements IResourceDeltaVisitor {

	private static final Logger logger = Logger.getLogger(AresProjectBuilder.class);
	
	private Set<IARESResource> resourcesTobeChecked = new HashSet<IARESResource>();
	private Map<String, IAresContext> contexts = new HashMap<String, IAresContext>();
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IncrementalProjectBuilder#build(int, java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] build(int kind, @SuppressWarnings("rawtypes") Map args, IProgressMonitor monitor) throws CoreException {
		logger.info("Ares Builder start...");
		IProject project = getProject();
		if (project == null) 
			return null;
		
		IARESProject aresProj = ARESCore.create(project);
		if (aresProj.exists()) {
			initContexts(aresProj);
			
			if (kind == FULL_BUILD) {
				fullBuild(aresProj, monitor);
			} else /*if (kind == INCREMENTAL_BUILD)*/ {
				IResourceDelta delta = getDelta(project);
				if (delta != null) {
					if (needFullBuild(delta)) {
						fullBuild(aresProj, monitor);
					} else {
						monitor.beginTask("", 2000);
						try {
							resourcesTobeChecked.clear();
							monitor.subTask("�ռ���Ҫ������Դ...");
							// accept�������ռ���Ҫ������Դ
							delta.accept(this);
							monitor.worked(1000);
							
							monitor.subTask("�����Դ...");
							
							SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1000);
							try {
								subMonitor.beginTask("", resourcesTobeChecked.size());
								// �ռ�����Ϣ�Ժ�ͳһ���м��
								for (IARESResource res : resourcesTobeChecked) {
									if (subMonitor.isCanceled() || isInterrupted())
										break;
									subMonitor.subTask("�����Դ��" + res.getName());
									if(ErrorCheckPreferenceHelper.getInstance().isErrorCheck(res.getType())){
										validateResource(res);	
									}
									subMonitor.worked(1);
								}
							} finally {
								subMonitor.done();
							}
							
							resourcesTobeChecked.clear();
						} finally {
							monitor.done();
						}
					}
				} else {
					fullBuild(aresProj, monitor);
				}
			}
			contexts.clear();
		}
		
		logger.info("Ares Builder finished...");
		return null;
	}
	
	private boolean needFullBuild(IResourceDelta delta) {
		if (delta.findMember(new Path(ARESProject.RES_PATH_FILE)) != null) {
			return true;
		}
		
		RespathProviderRegistry reg = RespathProviderRegistry.getInstance();
		for (IRespathProviderDescriptor provider : reg.getProviders()) {
			IRespathProvider rpProvider = provider.getProvider();
			if (rpProvider.containsRespathChange(delta)) {
				return true;
			}
		}
		return false;
	}

	private void initContexts(IARESProject project) {
		ARESContextRegistry contextReg = ARESContextRegistry.getInstance();
		contexts = contextReg.createContexts(project);
	}
	
	protected void fullBuild(IARESProject  project, IProgressMonitor monitor) {	
		try {
			AresProjectBuilderUtil.clearMarkers(project.getProject(), true);
			DefaultContextProvider context = (DefaultContextProvider)contexts.get(IAresContext.DEFAULT_CONTEXT);
			Collection<IARESResource> resources = context.getResources().values();
			monitor.beginTask("", (resources.size() + 1) * 1000);
			
			for (IARESResource res : resources) {
				if (monitor.isCanceled() || isInterrupted())
					break;
				monitor.subTask("�����Դ:" + res.getName());
				if(ErrorCheckPreferenceHelper.getInstance().isErrorCheck(res.getType())){
					validateResource(res);	
				}
				
				monitor.worked(1000);
			}
			
			monitor.subTask("�����Ŀ����...");
			//�����Ŀ����
			checkProjectProperty(project.getProject());
			monitor.worked(1000);
		} finally {
			monitor.done();
		}
	}

	protected void validateResource(IARESResource res) {
		ResValidaterRegistry validatorReg = ResValidaterRegistry.getInstance();

		// ���ð������
		if (res.getRoot().isArchive())
			return;
		
		List<IARESProblem> problems = new ArrayList<IARESProblem>();
		for (IResValidator validator : validatorReg.getValidators(res.getType())) {
			if (validator != null) {
				Collection<IARESProblem> pro = validator.validate(res, contexts);
				problems.addAll(pro);
			} else {
				logger.error(String.format("��Դ����: %s ��ValidatorΪnull", res.getType()));
			}

		}
		
		// clear markers
		AresProjectBuilderUtil.clearMarkers(res);
		// create new markers
		if (!problems.isEmpty()) {
			AresProjectBuilderUtil.markProblems(res, problems);
		}
	}
	
	private void checkProjectProperty(IProject project) {
		List<IARESProblem> results = new ArrayList<IARESProblem>();
		IARESProject aresProject = ARESCore.create(project);
		IARESProjectProperty property = null;;
		try {
			property = aresProject.getProjectProperty();
		} catch (ARESModelException e1) {
			e1.printStackTrace();
		}
		
		if (property == null)
			return; 
		
		IProjectDescription desc;
		try {
			desc = project.getDescription();
			String[] natures = desc.getNatureIds();
			ProjectValidatorRegistry reg = ProjectValidatorRegistry.getInstance();
			for (String nature : natures) {
				Collection<IProjectValidatorDescriptor> validators = reg.get(nature);
				for (IProjectValidatorDescriptor descriptor : validators) {
					IProjectPropertyValidator validator = descriptor.getValidator();
					if (validator != null) {
						Collection<IARESProblem> problems = validator.validate(property, contexts);
						results.addAll(problems);
					}
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		//���marker
		IFile file = project.getFile(IARESProjectProperty.PRO_FILE);
		if (file.exists()) {
			AresProjectBuilderUtil.clearMarkers(file);
			AresProjectBuilderUtil.markProblems(file, results);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
	 */
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		int type = resource.getType();
		switch (type) {
		case IResource.PROJECT:
			// ��Ŀ��ɾ����ֱ���˳���
			if (delta.getKind() == IResourceDelta.REMOVED) {
				return false;
			}
			return true;
		case IResource.FOLDER:
			IFolder folder = (IFolder) resource;
			IARESElement element = ARESCore.create(folder);

			if (element != null && (element.getElementType() == IARESElement.COMMON_MODULE_ROOT || element.getElementType() == IARESElement.COMMON_MODULE)) {					
				switch (delta.getKind()) {
				case IResourceDelta.ADDED:
					return true;
				case IResourceDelta.REMOVED:
					return true;
				case IResourceDelta.CHANGED:
					return true;
				}
			}
			return true;
		case IResource.FILE:
			IFile file = (IFile) resource;
			// ������ARESResource
			element = ARESCore.create(file);
			if (element instanceof IARESResource) {
				IARESResource aresResource = (IARESResource)element;
				
				switch (delta.getKind()) {
				case IResourceDelta.ADDED:
				case IResourceDelta.CHANGED:
					resourcesTobeChecked.add(aresResource);
				}
				if(ErrorCheckPreferenceHelper.getInstance().isErrorCheck(aresResource.getType())&& ErrorCheckPreferenceHelper.getInstance().isRelationCheck()){//ֻ���Լ�Ҫ���д����������²Ž��������Դ�Ĵ�����
					RefResourcesProviderRegistry reg = RefResourcesProviderRegistry.getInstance();
					for (IRefResourceProvider provider : reg.getProviders(aresResource.getType())) {
						if(provider instanceof IRefResourceForBuilderProvider){
							resourcesTobeChecked.addAll(((IRefResourceForBuilderProvider)provider).getRefForBuildResources(aresResource, delta, contexts));
						}else{
							resourcesTobeChecked.addAll(provider.getRefResources(aresResource, delta, contexts));
						}
						
					}
				}
				
			} else if (element instanceof IProjectProperty) {
				checkProjectProperty(file.getProject());
			} /*else if (element instanceof IReferencedLibrary) {
				((IReferencedLibrary) element).close();
			}*/
			
//			��һ��ʼ�ͼ���Ƿ����respath�޸ģ�����оͲ��ر���delta�ˡ�
//			if (file.getName().equals(ARESProject.RES_PATH_FILE)) {
//				// �����.respath�ļ������仯�����������ɾ��������Դ��������ǣ�����Ҫ����build
//				IARESProject aresProj = ARESCore.create(file.getProject());
//				if (ARESModelManager.getManager().getDeltaProcessor().hasLibPathChanged(file.getProject())) {
//					fullBuild(aresProj);
//				}
//			}
			
		}
		return false;	
	}
	
}
