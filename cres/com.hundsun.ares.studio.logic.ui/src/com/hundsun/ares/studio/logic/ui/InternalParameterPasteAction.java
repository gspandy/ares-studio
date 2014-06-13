/**
 * Դ�������ƣ�ParameterPasteAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.logic.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.hundsun.ares.studio.atom.AtomFactory;
import com.hundsun.ares.studio.atom.InternalParam;
import com.hundsun.ares.studio.biz.BizFactory;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerPasteAction;
import com.hundsun.ares.studio.ui.util.ARESEMFClipboard;

/**
 * ������ص�ճ��Action����ҪΪ��ʵ�ִӱ��ֶε����䡣
 * @author sundl
 *
 */
public class InternalParameterPasteAction extends ColumnViewerPasteAction {

	/**
	 * @param viewer
	 * @param editingDomain
	 * @param owner
	 * @param reference
	 */
	public InternalParameterPasteAction(ColumnViewer viewer,
			EditingDomain editingDomain, EObject owner, EReference reference) {
		super(viewer, editingDomain, owner, reference);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.actions.ColumnViewerAction#createCommand()
	 */
	@Override
	protected Command createCommand() {
		if (owner != null && reference != null) {
			IStructuredSelection selection = (IStructuredSelection)getViewer().getSelection();
			return new ParameterPasteCommand((TransactionalEditingDomain) getEditingDomain(), owner, reference, selection.getFirstElement());
		}
		return null;
	}
	
	protected class ParameterPasteCommand extends PasteCommand{

		public ParameterPasteCommand(TransactionalEditingDomain domain, EObject owner, EReference reference, Object position) {
			super(domain, owner, reference, position);
		}
		
		protected List<EObject> getCopyedObjects() {
			List<EObject> eObjects = (List<EObject>) ARESEMFClipboard.getInstance().pasteFromClipboard(reference.getEReferenceType().getInstanceClass());
			
			if (eObjects.isEmpty()) {
				eObjects = new ArrayList<EObject>();
				List<StandardField> stds = ARESEMFClipboard.getInstance().pasteFromClipboard(StandardField.class);
				for (StandardField std : stds) {
					InternalParam p = AtomFactory.eINSTANCE.createInternalParam();
					p.setParamType(ParamType.STD_FIELD);
					p.setId(std.getName());
					eObjects.add(p);
				}
			}	
			return eObjects;
		}
		
	}

}
