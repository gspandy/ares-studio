/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.aresaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.util.ARESElementUtil;
import com.hundsun.ares.studio.core.util.Util;
import com.hundsun.ares.studio.internal.ui.aresaction.ARESActionRegistry;
import com.hundsun.ares.studio.internal.ui.aresaction.AresActionDelareDescriptor;
import com.hundsun.ares.studio.internal.ui.aresaction.AresActionImplementationDescriptor;

/**
 * ִ��AresAction�ĸ�����
 * 
 * @author sundl
 */
public class AresActionExecuter {

	private AresActionExecuteContext context;

	/**
	 * ����һ��ִ��ָ��id�Ĳ�����ִ����
	 * 
	 * @param context
	 */
	public AresActionExecuter(AresActionExecuteContext context) {
		this.context = context;
	}

	public void excute(IProgressMonitor monitor) {
		ARESActionRegistry reg = ARESActionRegistry.getInstance();
		String actionId = context.getActionId();

		AresActionDelareDescriptor declare = reg.getActionDeclare(actionId);
		IAresActionAdvisor advisor = declare.createAdvisor();
		advisor.init(context);
		
		// 2012-03-12 sundl ѯ��advisor���Action�Ƿ���޸�workspace������ᣬ��ŵ�IWorkspaceRunnable��ִ��
		if (advisor.willModifyWorkspace()) {
			try {
				ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
					@Override
					public void run(IProgressMonitor monitor) throws CoreException {
						doExecute(monitor);
					}
				}, monitor);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} else {
			doExecute(monitor);
		}
	}
	
	// 2012-03-12 sundl ����ִ��Action
	private void doExecute(IProgressMonitor monitor) {
		ARESActionRegistry reg = ARESActionRegistry.getInstance();
		String actionId = context.getActionId();

		// Action������
		AresActionDelareDescriptor declare = reg.getActionDeclare(actionId);
		IAresActionAdvisor advisor = declare.createAdvisor();
		advisor.init(context);

		// action���������
		Object entryPoint = context.getEntryPoint();
		IARESResource[] resources = collectResources(entryPoint);
		advisor.preExcute();

		IProgressMonitor pm = Util.monitorFor(monitor);
		pm.beginTask(declare.getName(), resources.length + 10);

		// ��ÿ����Դ���е���
		for (IARESResource res : resources) {
			if (pm.isCanceled())
				break;

			pm.subTask("��Դ: " + res.getFullyQualifiedName());
			AresActionImplementationDescriptor impl = reg.getActionImplementation(actionId, res.getType());
			IARESAction action = impl.createAction();
			context.setCurrentRes(res);
			action.init(context);
			
			if (action.isEnabled())
				action.execute(Util.monitorFor(null));
			
			pm.worked(1);
			action = null;
		}

		pm.subTask("");
		advisor.postExcute();
		pm.worked(10);
		pm.done();
	}

	private IARESResource[] collectResources(Object entryPoint) {

		ARESActionRegistry reg = ARESActionRegistry.getInstance();
		String[] resTypes = reg.computeSupportedResTypes(context.getActionId());
		try {
			if (entryPoint instanceof IARESElement) {
				IARESElement element = (IARESElement) entryPoint;
				switch (element.getElementType()) {
				case IARESElement.ARES_PROJECT:
					IARESProject aresProject = (IARESProject) element;
					return aresProject.getResources(resTypes);
				case IARESElement.COMMON_MODULE_ROOT:
					IARESModuleRoot root = (IARESModuleRoot) element;
					return root.getResources(resTypes);
				case IARESElement.COMMON_MODULE:
					IARESModule module = (IARESModule) element;
					return module.getARESResources(resTypes, true);
				case IARESElement.ARES_RESOURCE:
					IARESResource res = (IARESResource) element;
					if (ArrayUtils.contains(resTypes, res.getType()))
						return new IARESResource[] { (IARESResource) element };
					else
						return new IARESResource[0];
				}
			} else if (entryPoint instanceof Collection) {
				List<IARESResource> resources = new ArrayList<IARESResource>();
				Object[] entries = ARESElementUtil.toARESElement(((Collection) entryPoint).toArray());
				for (Object entry : entries) {
					resources.addAll(Arrays.asList(collectResources(entry)));
				}
				return resources.toArray(new IARESResource[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new IARESResource[0];
	}

}

