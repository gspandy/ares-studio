/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.reference.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.ARESElementChangedEvent;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESElementChangeListener;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.IReferencedLibrary;
import com.hundsun.ares.studio.model.reference.ProjectReferenceCollection;
import com.hundsun.ares.studio.model.reference.ReferenceFactory;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.model.reference.ReferenceTable;
import com.hundsun.ares.studio.reference.ReferenceForBuilderManager;
import com.hundsun.ares.studio.reference.ViewerUtils;

/**
 * @author gongyf
 *
 */
public class ReferenceTableViewer {
	
	private static class RemoveProjectInfo extends RecordingCommand {

		private ReferenceTable table;
		private IARESProject project;
		
		/**
		 * @param domain
		 * @param table
		 * @param project
		 */
		public RemoveProjectInfo(TransactionalEditingDomain domain,
				ReferenceTable table, IARESProject project) {
			super(domain);
			this.table = table;
			this.project = project;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.emf.transaction.RecordingCommand#doExecute()
		 */
		@Override
		protected void doExecute() {
			table.getProjects().remove(project);
			ReferenceForBuilderManager.getInstance().getProjects().remove(project);
		}
		
	}
	
	private static class RemoveResourceInfo extends RecordingCommand {
		
		private ReferenceTable table;
		private IARESResource resource;
		
		
		/**
		 * @param domain
		 * @param table
		 * @param resource
		 */
		public RemoveResourceInfo(TransactionalEditingDomain domain,
				ReferenceTable table, IARESResource resource) {
			super(domain);
			this.table = table;
			this.resource = resource;
		}



		/* (non-Javadoc)
		 * @see org.eclipse.emf.transaction.RecordingCommand#doExecute()
		 */
		@Override
		protected void doExecute() {
			ProjectReferenceCollection pi = table.getProjects().get(resource.getARESProject());
			EList<ReferenceInfo> referenceInfoList = pi.getReferences().get(resource.getType());
			if(referenceInfoList!=null){
				for (Iterator<ReferenceInfo> iterator = referenceInfoList.iterator(); iterator.hasNext();) {
					ReferenceInfo ref = iterator.next();
					if (ObjectUtils.equals(ref.getResource(), resource)) {
						iterator.remove();
					}
				}
			}
			String[] refTypes = ViewerUtils.getRefTypesCanSupport(resource.getType());
	        if(refTypes.length==1 && ViewerUtils.isOnlyResourceOnlyRefType(refTypes[0]) ){
	        	EList<ReferenceInfo> newReferences = new BasicEList<ReferenceInfo>();
	        	pi.updateOnlyResourceOnlyRefTypecache(refTypes[0], newReferences);
	        }
	        if(StringUtils.endsWithIgnoreCase(resource.getType(), "stdfield")){
	        	 ReferenceForBuilderManager.getInstance().clearReferences(resource);
	        }
	       
		}
		
	}
	
	private static class ChangeResourceInfo extends RecordingCommand {
		private ReferenceTable table;
		private IARESResource resource;
		private Map<Object, Object> context;
		
		
		/**
		 * @param domain
		 * @param table
		 * @param resource
		 * @param context
		 */
		public ChangeResourceInfo(TransactionalEditingDomain domain,
				ReferenceTable table, IARESResource resource,
				Map<Object, Object> context) {
			super(domain);
			this.table = table;
			this.resource = resource;
			this.context = context;
		}


		/* (non-Javadoc)
		 * @see org.eclipse.emf.transaction.RecordingCommand#doExecute()
		 */
		@Override
		protected void doExecute() {
			ProjectReferenceCollection pi = table.getProjects().get(resource.getARESProject());
			List<ReferenceInfo> oldList = ViewerUtils.getReferenceInfos(table, resource);
			List<ReferenceInfo> newList = ViewerUtils.getReferenceInfos(resource, context);
			
			// �ҳ���������Ŀ��ɾ������Ŀ
			
			 HashSet<ReferenceInfo> tempSetAll = new HashSet<ReferenceInfo>();
		        for (int i=0; i<oldList.size(); i++) {
		        	tempSetAll.add(oldList.get(i));
		        }
		        HashSet<ReferenceInfo> setDup = new HashSet<ReferenceInfo>();
		        ArrayList<ReferenceInfo>  newClean = new ArrayList<ReferenceInfo>();
		        for (ReferenceInfo referenceInfo: newList) {
		            if (tempSetAll.add(referenceInfo)) {  
		            	newClean.add(referenceInfo);
		            } else {
		            	setDup.add(referenceInfo);  
		            }
		        }
		        ArrayList<ReferenceInfo> oldListClean = new ArrayList<ReferenceInfo>();
		        for (ReferenceInfo referenceInfo: oldList ) {
		            if (setDup.add(referenceInfo)) {  
		            	oldListClean.add(referenceInfo);
		            }
		        }
		        
				
		        EList<ReferenceInfo> referenceInfoList = pi.getReferences().get(resource.getType());
		       
		        if(referenceInfoList!=null){
		        	referenceInfoList.removeAll(oldListClean);
			        referenceInfoList.addAll(newClean);
			        String[] refTypes =ViewerUtils.getRefTypesCanSupport(resource.getType());
			        if(refTypes.length==1 && ViewerUtils.isOnlyResourceOnlyRefType(refTypes[0]) ){
			        	EList<ReferenceInfo> newReferences = new BasicEList<ReferenceInfo>();
			        	newReferences.addAll(newList);
			        	pi.updateOnlyResourceOnlyRefTypecache(refTypes[0], newReferences);
			        }
		        }
		        
		        
		        if(StringUtils.endsWithIgnoreCase(resource.getType(), "stdfield")){
		        	   List<ReferenceInfo> changeReferenceInfoList = new ArrayList<ReferenceInfo>();
				        changeReferenceInfoList.addAll(oldListClean);
				        changeReferenceInfoList.addAll(newClean);
				        ReferenceForBuilderManager.getInstance().addReferences(resource, changeReferenceInfoList);//�������ʱ���õĸ�������
		        }
		     
		        
		}
	}
	
	private static class AddProjectInfo extends RecordingCommand {
		private ReferenceTable table;
		private IARESProject project;
		private Map<String,List<ReferenceInfo>> infoMap ;
		
		/**
		 * @param domain
		 * @param table
		 * @param project
		 * @param collecion
		 */
		public AddProjectInfo(TransactionalEditingDomain domain,
				ReferenceTable table, IARESProject project,
				Map<String,List<ReferenceInfo>> infoMap ) {
			super(domain);
			this.table = table;
			this.project = project;
			this.infoMap = infoMap;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.emf.transaction.RecordingCommand#doExecute()
		 */
		@Override
		protected void doExecute() {
			ProjectReferenceCollection value = table.getProjects().get(project);
			if (value == null) {
				value = ReferenceFactory.eINSTANCE.createProjectReferenceCollection();
				table.getProjects().put(project, value);
			}
			Set<String> resTypes = infoMap.keySet();
			for(String type :resTypes){
				if (value.getReferences().get(type)!=null){
					value.getReferences().get(type).clear();
					value.getReferences().get(type).addAll(infoMap.get(type));
					String[] refTypes =ViewerUtils.getRefTypesCanSupport(type);
			        if(refTypes.length==1 && ViewerUtils.isOnlyResourceOnlyRefType(refTypes[0]) ){
			        	EList<ReferenceInfo> newReferences = new BasicEList<ReferenceInfo>();
			        	newReferences.addAll(infoMap.get(type));
			        	value.updateOnlyResourceOnlyRefTypecache(refTypes[0], newReferences);
			        }
				}else{
					 EList<ReferenceInfo> referenceInfoList = new BasicEList<ReferenceInfo>();
					 referenceInfoList.addAll(infoMap.get(type));
					 value.getReferences().put(type, referenceInfoList);
					 String[] refTypes =ViewerUtils.getRefTypesCanSupport(type);
				        if(refTypes.length==1 && ViewerUtils.isOnlyResourceOnlyRefType(refTypes[0]) ){
				        	EList<ReferenceInfo> newReferences = new BasicEList<ReferenceInfo>();
				        	newReferences.addAll(infoMap.get(type));
				        	value.updateOnlyResourceOnlyRefTypecache(refTypes[0], newReferences);
				        }
				}
			}
			
		}
	}
	
	/**
	 * �༭�ڴ�ͳ��������Ϣ�ı༭��
	 */
	private TransactionalEditingDomain editingDomain;
	
	private ReferenceTable table;

	/**
	 * ���ڱ�����й�Ԥ����ķ�Χ
	 */
	private Set<Scope> processedScopes = new HashSet<Scope>();
	
	private IResourceChangeListener listener = new IResourceChangeListener() {

		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			if (delta != null) {
				ARESElementDeltaVisitor visitor = new ARESElementDeltaVisitor();
				try {
					delta.accept(visitor);
					
					synchronized (processedScopes) {
						CompoundCommand command = new CompoundCommand();
						
						for (IARESElement element : visitor.getRemovedElements()) {
							if (element instanceof IARESProject) {
								// ���̱�ɾ������Ҫɾ��������ص�����
								// ��ɾ�����
								for (Iterator<Scope> iterator = processedScopes.iterator(); iterator
										.hasNext();) {
									Scope scope = iterator.next();
									if (ObjectUtils.equals(scope.getProject(), element)) {
										iterator.remove();
									}
								}
								
								// ɾ������
								command.append(new RemoveProjectInfo(editingDomain, table, (IARESProject)element));
							} else if (element instanceof IARESResource) {
								// ������Ѿ�����ķ�Χ��������������
								IARESResource aresResource = (IARESResource) element;
								if (processedScopes.contains(new Scope(aresResource.getARESProject(), aresResource.getType()))) {
									command.append(new RemoveResourceInfo(editingDomain, table, aresResource));
								}
							}
						}
						
						for (IARESElement element : visitor.getAddedOrChangedElements()) {
							// ��Ӻ��޸Ĺ����ǲ���Ҫ�����
							if (element instanceof IARESResource) {
								IARESResource aresResource = (IARESResource) element;
								if (processedScopes.contains(new Scope(aresResource.getARESProject(), aresResource.getType()))) {
									Map<Object, Object> context = createReferenceProviderContext();
									command.append(new ChangeResourceInfo(editingDomain, table, aresResource, context));
								}
							}
						}
						
						excute(command);
					}
				} catch (CoreException e) {
				}
				
				
			}
							
		}
		
	};
	
	/**
	 * ���ڼ������ñ仯
	 */
	private IARESElementChangeListener listener2 = new IARESElementChangeListener() {

		@Override
		public void elementChanged(ARESElementChangedEvent event) {
			if (event.getType() == ARESElementChangedEvent.RES_PATH) {
				IARESProject changedProject = (IARESProject) event.getElement();
				synchronized (processedScopes) {
					for (Iterator<Scope> iterator = processedScopes.iterator(); iterator.hasNext();) {
						Scope scope = iterator.next();
						if (ObjectUtils.equals(scope.getProject(), changedProject)) {
							iterator.remove();
						}
					}
					excute(new RemoveProjectInfo(editingDomain, table, changedProject));
				}
			}
		}};
	
	/**
	 * 
	 */
	public ReferenceTableViewer() {
		// ����ģ��
		editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
		Resource xmlRes = new XMLResourceImpl();
		xmlRes.getContents().add(table = ReferenceFactory.eINSTANCE.createReferenceTable());
		editingDomain.getResourceSet().getResources().add(xmlRes);
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
		ARESCore.addElementListener(listener2);
	}
	
	
	public void dispose() {
		ARESCore.removeElementListener(listener2);
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
		editingDomain.dispose();
		editingDomain = null;
	}
	
	/**
	 * @return the table
	 */
	public ReferenceTable getTable() {
		return table;
	}
	
	/**
	 * ������ȡ��Դ������Ϣʱ��������
	 * @return
	 */
	private Map<Object, Object> createReferenceProviderContext() {
		return Collections.emptyMap();
	}
	
	/**
	 * ������Ҫ�Ĺ��̣����һ�������Ѿ�������򲻻��ٽ��д���
	 * @param project
	 * @param refType
	 * @param useRequiredProjects
	 */
	private void process(IARESProject project, String refType, boolean useRequiredProjects) {
		synchronized (processedScopes) {
			List<IARESProject> needProcessProjects = new ArrayList<IARESProject>();
			if (useRequiredProjects) {
				needProcessProjects.addAll(Arrays.asList(ViewerUtils.getAllRequiredProject(project)));
			} else {
				needProcessProjects.add(project);
			}
			
			Map<Object, Object> context = createReferenceProviderContext();
			
			CompoundCommand command = new CompoundCommand();
			
			String[] resTypes = ViewerUtils.getResTypesCanSupport(refType);
			
			for (IARESProject p : needProcessProjects) {
				for (String resType : resTypes) {
					Scope key = new Scope(p, resType);
					
					if (!processedScopes.contains(key)) {
						
						Map<String,List<ReferenceInfo>> infoMap = new HashMap<String,List<ReferenceInfo>>();
						
						// ��Ҫ�ռ����̱�������ð�����
						List<IARESResource> resourceList = new ArrayList<IARESResource>();
						
						// ������Ҫע�����һ���������Ϳ����Ƕ�����Դ�ṩ�ģ����߶���������Դ��һ����Դ�ṩ��
						// FIXME
						try {
							String[] types = new String[]{resType};
							resourceList.addAll(Arrays.asList(p.getResources(types)));
							for (IReferencedLibrary lib : p.getReferencedLibs()) {
								resourceList.addAll(Arrays.asList(lib.getResources(types)));
							}
						} catch (ARESModelException e) {
						}
						
						for (IARESResource res : resourceList) {
							if(infoMap.get(res.getType())!=null){
								infoMap.get(res.getType()).addAll(ViewerUtils.getReferenceInfos(res, context));
							}else{
									List<ReferenceInfo> resInfoList = new ArrayList<ReferenceInfo>();
									resInfoList.addAll(ViewerUtils.getReferenceInfos(res, context));
									infoMap.put(res.getType(), resInfoList);
							}
						}
						//��ֹ��ģ�����û����Դ�����,��Ӧ��Դ���͵Ļ���Ϊnull���
						if(infoMap.get(resType)==null ){
							List<ReferenceInfo> resInfoList = new ArrayList<ReferenceInfo>();
							infoMap.put(resType, resInfoList);
						}
						
						command.append(new AddProjectInfo(editingDomain, table, p, infoMap));
						
						// ���ñ��
						processedScopes.add(key);
					}
				}
				
			}
			
			excute(command);
		}
	}
	
	public List<ReferenceInfo> getReferenceInfos(final IARESProject project,final String refType,final String refName,final String refNamespace, final boolean useRequiredProjects) {
		
		process(project, refType, useRequiredProjects);
		
		try {
			return TransactionUtil.runExclusive(editingDomain, new RunnableWithResult.Impl<List<ReferenceInfo>>(){

				@Override
				public void run() {
					List<ReferenceInfo> result = new ArrayList<ReferenceInfo>();
					
					if (useRequiredProjects) {
						for (IARESProject p : ViewerUtils.getAllRequiredProject(project)) {
							ProjectReferenceCollection pi = table.getProjects().get(p);
							if (pi != null) {
								result.addAll(pi.getReferenceInfos(refType, refName, refNamespace));
							}
						}
					} else {
						ProjectReferenceCollection pi = table.getProjects().get(project);
						if (pi != null) {
							result.addAll(pi.getReferenceInfos(refType, refName, refNamespace));
						}
					}
					
					
					setResult(result);
					
				}});
		} catch (InterruptedException e) {
			return Collections.emptyList();
		}
	}
	
	public List<ReferenceInfo> getReferenceInfos(final IARESProject project,final String refType,final String refName, final boolean useRequiredProjects) {
		process(project, refType, useRequiredProjects);
		
		try {
			return TransactionUtil.runExclusive(editingDomain, new RunnableWithResult.Impl<List<ReferenceInfo>>(){

				@Override
				public void run() {
					List<ReferenceInfo> result = new ArrayList<ReferenceInfo>();
					
					if (useRequiredProjects) {
						for (IARESProject p : ViewerUtils.getAllRequiredProject(project)) {
							ProjectReferenceCollection pi = table.getProjects().get(p);
							if (pi != null) {
								result.addAll(pi.getReferenceInfos(refType, refName));
							}
						}
					} else {
						ProjectReferenceCollection pi = table.getProjects().get(project);
						if (pi != null) {
							result.addAll(pi.getReferenceInfos(refType, refName));
						}
					}
					
					
					setResult(result);
					
				}});
		} catch (InterruptedException e) {
			return Collections.emptyList();
		}
	}
	
	public List<ReferenceInfo> getFirstReferenceInfos(final IARESProject project,final String refType,final String refName,final String refNamespace, final boolean useRequiredProjects) {
		process(project, refType, useRequiredProjects);
		
		try {
			return TransactionUtil.runExclusive(editingDomain, new RunnableWithResult.Impl<List<ReferenceInfo>>(){

				@Override
				public void run() {
					List<ReferenceInfo> result = new ArrayList<ReferenceInfo>();
					
					if (useRequiredProjects) {
						for (IARESProject p : ViewerUtils.getAllRequiredProject(project)) {
							ProjectReferenceCollection pi = table.getProjects().get(p);
							if (pi != null) {
								result.addAll(pi.getFirstReferenceInfos(refType, refName,refNamespace));
							}
						}
					} else {
						ProjectReferenceCollection pi = table.getProjects().get(project);
						if (pi != null) {
							result.addAll(pi.getFirstReferenceInfos(refType, refName,refNamespace));
						}
					}
					
					
					setResult(result);
					
				}});
		} catch (InterruptedException e) {
			return Collections.emptyList();
		}
	}
	
	
	
	public List<ReferenceInfo> getReferenceInfos(final IARESProject project,final String refType, final boolean useRequiredProjects) {
		process(project, refType, useRequiredProjects);
		
		try {
			return TransactionUtil.runExclusive(editingDomain, new RunnableWithResult.Impl<List<ReferenceInfo>>(){

				@Override
				public void run() {
					List<ReferenceInfo> result = new ArrayList<ReferenceInfo>();
					
					if (useRequiredProjects) {
						for (IARESProject p : ViewerUtils.getAllRequiredProject(project)) {
							ProjectReferenceCollection pi = table.getProjects().get(p);
							if (pi != null) {
								result.addAll(pi.getReferenceInfos(refType));
							}
						}
					} else {
						ProjectReferenceCollection pi = table.getProjects().get(project);
						if (pi != null) {
							result.addAll(pi.getReferenceInfos(refType));
						}
					}
					
					
					setResult(result);
					
				}});
		} catch (InterruptedException e) {
			return Collections.emptyList();
		}
	}
	
	/**
	 * ��ȡ��Դ���ݣ��õ���Դ�ṩ�Ŀ�������Ϣ
	 * @param resource
	 * @return
	 */
	public List<ReferenceInfo> getReferenceInfos(IARESResource resource) {
		return ViewerUtils.getReferenceInfos(resource, createReferenceProviderContext());
	}
	
	/**
	 * ִ�������޸�����
	 * @param command
	 */
	private void excute(Command command) {
		editingDomain.getCommandStack().execute(command);
		editingDomain.getCommandStack().flush();
	}
}
