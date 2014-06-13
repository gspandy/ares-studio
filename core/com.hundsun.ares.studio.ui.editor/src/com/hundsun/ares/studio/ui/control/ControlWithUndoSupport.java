/**
 * <p>Copyright: Copyright   2010</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.control;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Control;

import com.hundsun.ares.studio.ui.editor.AbstractHSFormEditor;
import com.hundsun.ares.studio.ui.util.ImporveControlWithDitryStateContext;

/**
 * Ϊ�ؼ��ṩ������������
 * @author maxh
 *
 * @param <T>
 */
public abstract class ControlWithUndoSupport<T extends Control> extends ControlWithChecker<T> {
	public ControlWithUndoSupport(ImporveControlWithDitryStateContext context,int controlStyle) {
		super(context, controlStyle);
		this.undoContext = context.getUndoContext();
		addUndoSupport();
	}
	
	public ControlWithUndoSupport(ImporveControlWithDitryStateContext context,int controlStyle,String beanFieldName) {
		super(context, controlStyle,beanFieldName);
		this.undoContext = context.getUndoContext();
		addUndoSupport();
	}
	
	public ControlWithUndoSupport(ImporveControlWithDitryStateContext context,int controlStyle,Object model,String beanFieldName) {
		super(context, controlStyle,model,beanFieldName);
		this.undoContext = context.getUndoContext();
		addUndoSupport();
	}
	
	/**
	 * ��������������
	 */
	IUndoContext undoContext;
	
	/**
	 * ���������Ĳ���
	 */
	ImproveControlWithDitryStateOperation operation;
	
	/**
	 * û��AbstractHSFormEditor.getOperationHistory()û��ɾ������
	 * ������һ��������ȥ�滻
	 * �ﵽ��ɾ��ͬ����Ч��
	 */
	static IUndoableOperation[] EMPTY_UNDO_CONTEXT = {};
	
	/**
	 * Ϊ���������ṩ֧��
	 */
	protected void addUndoSupport(){
		if(undoContext == null){
			return;
		}
		getControl().addFocusListener(new FocusListener(){
			Object oldValue;
			Object newValue;
			public void focusGained(FocusEvent e) {
				oldValue = getValue();
				try {
					operation = new ImproveControlWithDitryStateOperation("change",ControlWithUndoSupport.this,oldValue,oldValue);
					operation.addContext(undoContext);
					AbstractHSFormEditor.getOperationHistory().execute(operation, null, null);
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				}
			}
			public void focusLost(FocusEvent e) {
				newValue = getValue();
				if(!newValue.equals(oldValue)){
					operation.setNewValue(newValue);
				}else{
					AbstractHSFormEditor.getOperationHistory().replaceOperation(operation, EMPTY_UNDO_CONTEXT);
				}
			}
		});
	}
}

class ImproveControlWithDitryStateOperation  extends AbstractOperation {

	ControlWithDataBind control;
	Object oldValue;
	Object newValue;
	
	public ImproveControlWithDitryStateOperation(String str,ControlWithDataBind control,Object oldValue,Object newValue) {
		super(str);
		this.control = control;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}
	
	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		// TODO Auto-generated method stub
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		control.setValue(newValue);
		return Status.OK_STATUS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.operations.AbstractOperation#undo(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 * ��¼��ǰ״̬ ����ʱ�ظ�
	 */
	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		newValue = control.getValue();
		control.setValue(oldValue);
		return Status.OK_STATUS;
	}
}
