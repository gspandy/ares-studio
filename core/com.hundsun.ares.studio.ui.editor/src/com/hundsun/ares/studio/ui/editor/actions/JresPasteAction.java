package com.hundsun.ares.studio.ui.editor.actions;

import java.util.Collection;
import java.util.EventObject;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.PasteFromClipboardCommand;
import org.eclipse.emf.edit.ui.action.PasteAction;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Jres�Ƚ�ͨ�õ�PasteAction. 
 * ��EMF�����PasteAction��ͬ���ǣ����ڴ���paste Commandd��ʱ��ֱ��ʹ��Viewer��input��ΪOwner��
 * ����������Щģ�ͱ����нṹ����������������Action.
 * @author sundl
 */
public abstract class JresPasteAction extends PasteAction {

	private static final Logger logger = Logger.getLogger(JresPasteAction.class);
	
	protected IWorkbenchPart part;
	@Override
	public Command createCommand(Collection<?> selection) {
		if (selection.size() == 1) {
			return PasteFromClipboardCommand.create(domain, getOwner(selection), getFeature());
		} else {
			return UnexecutableCommand.INSTANCE;
		}
	}
	
	/**
	 * @since 2.1.0
	 */
	public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart) {
		this.part = workbenchPart;
		super.setActiveWorkbenchPart(workbenchPart);
		// Paste Action�Ŀ����Բ�����Selection�йأ�����CommandStack�йأ���Ϊ���Ƶĸı�Ҳ��Ӱ��ճ���Ƿ����
		if (domain != null) {
			domain.getCommandStack().addCommandStackListener(new CommandStackListener() {
				@Override
				public void commandStackChanged(EventObject event) {
					selectionChanged(getStructuredSelection());
					logger.debug("Command stack changed, update action enablement... Class: [" + JresPasteAction.this.getClass().getSimpleName() + "] " + isEnabled());
				}
			});
		}
	}
	
	protected ColumnViewer getColumnViewer() {
		ISelectionProvider selectionProvider = part.getSite().getSelectionProvider();
		if (selectionProvider instanceof ColumnViewer) {
			return (ColumnViewer) selectionProvider;
		}
		return null;
	}
	
	@Override
	public boolean updateSelection(IStructuredSelection selection) {
		boolean enabled = super.updateSelection(selection);
		logger.debug("Paste action selection changed: [" + this.getClass().getSimpleName() + "] " + enabled);
		return enabled;
	}

	/**
	 * ��ȡճ����ʱ��ʹ�õ�Owner��
	 * Ĭ�ϵĹ���Ϊ��
	 * ���SelectionProvider��һ��Viewer���򷵻����Viewer��Input��
	 * ���򷵻�selection����
	 * @param selection
	 * @return
	 */
	protected Object getOwner(Collection<?> selection) {
		Object sel = selection.iterator().next();
		if (sel instanceof EObject) {
			return ((EObject) sel).eContainer();
		}
		return sel;
	}
	
	/**
	 * ��ȡճ����ʱ��ʹ�õ�Feature,Ĭ�Ϸ���null������EMF�Լ��ж�Ӧ��ճ�����ĸ�Feature.
	 * �޸�����������Ըı�Command�Ŀ�����(�������������һ��feature����ʵ���ϼ��а��е������޷�ճ�������feature��command�᲻����)
	 * @return
	 */
	protected Object getFeature() {
		return null;
	}
	
}
