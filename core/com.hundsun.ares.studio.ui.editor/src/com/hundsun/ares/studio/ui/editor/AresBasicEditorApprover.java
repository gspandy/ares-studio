/**
* <p>Copyright: Copyright   2010</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.editor;

import org.eclipse.core.commands.operations.IOperationApprover;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * ��ʵ��һ���������������Ĺ�����
 * @author maxh
 *
 */
public class AresBasicEditorApprover implements IOperationApprover {

	private IUndoContext undoContext;

	public AresBasicEditorApprover(IUndoContext context) {
		super();
		this.undoContext = context;
	}
	
	public IStatus proceedRedoing(IUndoableOperation operation,
			IOperationHistory history, IAdaptable info) {
		if(!operation.hasContext(undoContext)){
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;

	}

	public IStatus proceedUndoing(IUndoableOperation operation,
			IOperationHistory history, IAdaptable info) {
		if(!operation.hasContext(undoContext)){
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}
	

}
