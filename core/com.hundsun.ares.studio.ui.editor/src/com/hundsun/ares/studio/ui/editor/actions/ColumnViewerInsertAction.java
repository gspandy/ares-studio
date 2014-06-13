/**
 * Դ�������ƣ�ColumnViewerInsertAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.actions;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;

public class ColumnViewerInsertAction extends ColumnViewerAction {

	private EObject owner;
	private EReference reference;
	private EClass eClass;
	
	
	public ColumnViewerInsertAction(ColumnViewer viewer, EditingDomain editingDomain, EClass class1) {
		this(viewer, editingDomain, null, null, class1);
	}
	
	public ColumnViewerInsertAction(ColumnViewer viewer, EditingDomain editingDomain,EReference reference, EClass class1) {
		this(viewer, editingDomain, null, reference, class1);
	}


	public ColumnViewerInsertAction(ColumnViewer viewer, EditingDomain editingDomain, EObject owner, EReference reference, EClass class1) {
		super(viewer, editingDomain);
		eClass = class1;
		
		setText("����");
		setEnabled(false);
		
		//setAccelerator(SWT.INSERT);
		
		this.owner = owner;
		this.reference = reference;
		
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(ARESEditorPlugin.PLUGIN_ID, "icons/full/obj16/insert.png"));
		setId(IActionIDConstant.CV_INSERT);
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(EObject owner) {
		this.owner = owner;
		clearCommand();
	}
	
	/**
	 * @return the owner
	 */
	public EObject getOwner() {
		return owner;
	}
	
	/**
	 * @param reference the reference to set
	 */
	public void setReference(EReference reference) {
		this.reference = reference;
		clearCommand();
	}
	
	/**
	 * @return the reference
	 */
	public EReference getReference() {
		return reference;
	}
	
	public EClass getEMFClass() {
		return eClass;
	}
	
	@Override
	protected Command createCommand() {
		if (getOwner() != null && getReference() != null) {
			EObject newObj = eClass.getEPackage().getEFactoryInstance().create(eClass);
			
			Object position = ((IStructuredSelection)getViewer().getSelection()).getFirstElement();
			
			Command command = null;
			// ��ѡ�е������ѡ��λ�ò����¶���
			if (position != null) {
				List<Object> list = (List<Object>) getOwner().eGet(getReference());
				int index = list.indexOf(position);
				if (index >= 0) {
					command = AddCommand.create(getEditingDomain(), getOwner(), getReference(), newObj, index);
				}
			}
			
			if (command == null) {
				command = AddCommand.create(getEditingDomain(), getOwner(), getReference(), newObj);
			}
			
			return command;
		}
		return null;
	}

	

}
