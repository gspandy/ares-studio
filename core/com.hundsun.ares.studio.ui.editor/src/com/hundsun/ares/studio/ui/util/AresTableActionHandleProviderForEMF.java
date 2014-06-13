/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.util;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.hundsun.ares.studio.ui.grid.table.GridTableViewerExComponent;

/**
 * ���EMF�ı���Ҽ���������ʱ��������������Ա��Ĳ�����ӵ�EMF�����ջ�С�
 * 
 * @author mawb
 */
public class AresTableActionHandleProviderForEMF<T extends EObject> extends AresTableActionHandleProvider<T> {
	private TransactionalEditingDomain editDomain;
	
	/**
	 * @param component
	 * @param data
	 */
	public AresTableActionHandleProviderForEMF(GridTableViewerExComponent<T> component, 
			T data, TransactionalEditingDomain editDomain) {
		super(component, data);
		this.editDomain = editDomain;
	}
	
	private void superDelete(List items) {
		super.delete(items);
	}
	
	private void superAdd(){
		super.add();
	}
	
	private void superPaste() {
		super.paste();
	}
	
	private void superCut() {
		super.cut();
	}
	
	private void superInsert() {
		super.insert();
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.grid.table.GridTableViewerExComponent#delete(java.util.List)
	 */
	@Override
	protected void delete(final List items) {
		RecordingCommand command = new RecordingCommand(editDomain) {

			@Override
			protected void doExecute() {
				superDelete(items);
			}
			
		};
		
		editDomain.getCommandStack().execute(command);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.grid.table.GridTableViewerExComponent#paste()
	 */
	@Override
	public void paste() {
		RecordingCommand command = new RecordingCommand(editDomain) {

			@Override
			protected void doExecute() {
				superPaste();
			}
			
		};
		
		editDomain.getCommandStack().execute(command);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.grid.table.GridTableViewerExComponent#insert()
	 */
	@Override
	public void insert() {
		RecordingCommand command = new RecordingCommand(editDomain) {

			@Override
			protected void doExecute() {
				superInsert();
			}
			
		};
		
		editDomain.getCommandStack().execute(command);
	}
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.util.AresTableActionHandleProvider#add()
	 */
	@Override
	public void add() {
		RecordingCommand command = new RecordingCommand(editDomain) {

			@Override
			protected void doExecute() {
				superAdd();
			}
		};
		editDomain.getCommandStack().execute(command);
	}
}
