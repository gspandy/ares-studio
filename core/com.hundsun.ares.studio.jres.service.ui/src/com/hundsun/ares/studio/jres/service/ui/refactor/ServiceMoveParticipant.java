package com.hundsun.ares.studio.jres.service.ui.refactor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;

import com.hundsun.ares.studio.biz.constants.IBizResType;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.ui.refactoring.AresElementMoveParticipant;


/**
 * Service����м�����Դ�ƶ���������Ӧ����
 * @author sundl
 *
 */
public class ServiceMoveParticipant extends AresElementMoveParticipant {

	public ServiceMoveParticipant() {
	}
	
	@Override
	public Change createPreChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		if (!getArguments().getUpdateReferences()) {
			return null;
		}
		
		Object newDestination = getArguments().getDestination();
		if (element instanceof IARESModule) {
			IARESModule module = (IARESModule) element;
			String newModuleName = null;
			if (newDestination instanceof IARESModule) {
				IARESModule destModule = (IARESModule) newDestination;
				newModuleName = destModule.getElementName() + "." + module.getShortName();
			} else if (newDestination instanceof IARESModuleRoot) {
				newModuleName = module.getShortName();
			}
			return createChange(module, newModuleName);
		} else if (element instanceof IARESResource) {
			IARESResource res = (IARESResource) element;
			String newObjName = null;
			if (newDestination instanceof IARESModule) {
				newObjName = ((IARESModule) newDestination).getElementName() + "." + res.getName();
			} else if (newDestination instanceof IARESModuleRoot) {
				newObjName = res.getName();
			}
			if (newObjName == null)
				return null;
			List<Change> changes = ServiceRefactorUtil.createChanges(res, newObjName);
			if (!changes.isEmpty())
				return new CompositeChange("������������б��еĶ������͵Ĳ������ø���", changes.toArray(new Change[0]));
		}
		return null;
	}
	
	private Change createChange(IARESModule module, String newModuleName) {
		List<Change> changes = new ArrayList<Change>();
		IARESResource[] objects = module.getARESResources(IBizResType.Object, true);
		// ��Щ����ȫ���ᱻ�ı�Ķ���
		for (IARESResource object : objects) {
			String newObjName = newModuleName + "." + object.getName();
			changes.addAll(ServiceRefactorUtil.createChanges(object, newObjName));
		}
		if (!changes.isEmpty())
			return new CompositeChange("������������б��еĶ������͵Ĳ������ø���", changes.toArray(new Change[0]));
		else 
			return null;
	}
	
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		return null;
	}

}
