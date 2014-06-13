package com.hundsun.ares.studio.ui.editor.actions;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hundsun.ares.studio.core.model.CoreFactory;
import com.hundsun.ares.studio.core.model.RevisionHistory;

/**
 * ����޸ļ�¼��ص�Action����
 * @author sundl
 *
 */
public abstract class AddRevisionHistoryAction extends ColumnViewerAction {

	protected EObject info;
	protected EReference eReference;
	
	/**
	 * @param viewer
	 * @param editingDomain
	 * @param info 				�������޶���ʷ��¼�б��EMF����
	 * @param eRefernece		�����ж�Ӧ�޶���¼�б��EMF����
	 */
	public AddRevisionHistoryAction(ColumnViewer viewer, EditingDomain editingDomain, EObject info, EReference eRefernece) {
		super(viewer, editingDomain);
		this.info = info;
		this.eReference = eRefernece;
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
	}
	
	public void setInfo(EObject info) {
		this.info = info;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.actions.ColumnViewerAction#createCommand()
	 */
	@Override
	protected Command createCommand() {
		if (info == null)
			return null;
		
		RevisionHistory newObj = CoreFactory.eINSTANCE.createRevisionHistory();
		// BUG #3085::[����]�޸ļ�¼�޸� ���ڸ�ʽ�޸ģ���ܲ���Factory�д��������������һ����ʽ���˴��������ó��¸�ʽ
		newObj.setModifiedDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm"));
		
		newObj.setVersion(getVersion());
		Command command = AddCommand.create(getEditingDomain(), info, eReference, newObj, 0);
		return command;
	}

	/**
	 * �����½���¼�İ汾��
	 * @return
	 */
	protected abstract String getVersion();

}
