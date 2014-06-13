package com.hundsun.ares.studio.procedure.ui.refactor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;

import com.hundsun.ares.studio.biz.constants.IBizResType;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.procdure.constants.IProcedureResType;
import com.hundsun.ares.studio.procedure.ui.util.ProcedureRefactorUtil;
import com.hundsun.ares.studio.ui.refactoring.AresElementRenameParicipant;

public class ProcedureRenamePaticipant extends AresElementRenameParicipant {

	
	
	
	protected String getDesc() {
		return "�����ж������͵Ĳ������ø���";
	}
	

	protected List<String> getResTypes() {
		List<String> resTypes = new ArrayList<String>();
		resTypes.add(IProcedureResType.PROCEDURE);
		return resTypes;
	}
	
		   
		@Override
		public Change createPreChange(IProgressMonitor pm) throws CoreException,	OperationCanceledException {
			if (!getArguments().getUpdateReferences()) {
				return null;
			}
			String newName = getArguments().getNewName();
			if (element instanceof IARESModule) {
				IARESModule module = (IARESModule) element;
				IARESModule parentModule = module.getParentModule();
				String newModuleName = null;
				if (parentModule != null) {
					newModuleName = parentModule.getElementName() + "." + newName;
				} else {
					newModuleName = newName;
				}
				return createChange(module, newModuleName);
			} else if (element instanceof IARESResource) { 	// �˴�û���ٽ����ж���Դ���ͣ���Ϊ��plugin.xml���Ѿ����ж�, Object���������Żᴥ�����,���Բ����ظ��ж�
				IARESResource res = (IARESResource) element;
				IARESModule module = res.getModule();
				String newRefName = module.getElementName() + "." + newName;
				List<Change> changes = ProcedureRefactorUtil.createChanges(res, newRefName,getResTypes());
				if (!changes.isEmpty())
					return new CompositeChange(getDesc(), changes.toArray(new Change[0]));
				return null;
			}
			return null;
		}
		
		@Override
		public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
			return null;
		}
		
		
		
		protected Change createChange(IARESModule module, String newName) {
			List<Change> changes = new ArrayList<Change>();
			IARESResource[] objects = module.getARESResources(IBizResType.Object, true);
			// ��Щ����ȫ���ᱻ�ı�Ķ���
			List<String> resTypes = getResTypes();
			for (IARESResource object : objects) {
				String newObjName = newName + "." + object.getName();
				changes.addAll(ProcedureRefactorUtil.createChanges(object, newObjName,resTypes));
			}
			if (!changes.isEmpty())
				return new CompositeChange(getDesc(), changes.toArray(new Change[0]));
			else 
				return null;
		}
	
		
}
