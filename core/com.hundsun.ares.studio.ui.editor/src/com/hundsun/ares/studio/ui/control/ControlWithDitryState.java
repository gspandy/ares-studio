/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.control;


import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.ui.util.EditorDirtyStatus;



/**
 * @author ����״̬�Ŀؼ�
 *
 * @param <T>
 */
public abstract class ControlWithDitryState<T extends Control> implements IEditable {

	/**
	 * �ؼ���״̬��
	 */
	protected EditorDirtyStatus dirty;

	protected FormToolkit toolkit;

	protected Composite parent;

	/**
	 * �ɱ༭�Ϳؼ���
	 */
	protected T control;

	protected int controlStyle;
	

	/**
	 * 
	 * @param parent
	 * @param dirtyStatus
	 */
	public ControlWithDitryState(Composite parent, int controlStyle, EditorDirtyStatus dirtyStatus) {
		this.dirty = dirtyStatus;
		this.parent = parent;
		this.controlStyle = controlStyle;

		this.control = this.initControl();
		this.initControlSize();
	}

	/**
	 * ���ÿռ䳤��
	 * 
	 * @return
	 */
	protected void initControlSize() {
	};

	
	public ControlWithDitryState(FormToolkit toolkit, Composite parent, int controlStyle, EditorDirtyStatus dirtyStatus) {
		this.dirty = dirtyStatus;
		this.parent = parent;
		this.controlStyle = controlStyle;
		this.toolkit = toolkit;
		this.control = this.initControl();
		this.initControlSize();
	}


	/**
	 * ����޸��¼�������
	 */
	public abstract void addModifyListener();
	
	/**
	 * ɾ���޸��¼�������
	 */
	public abstract void removeModifyListener();

	/**
	 * ����޸��¼�������
	 */
	protected abstract void addFocusListener();

	/**
	 * �����������
	 */
	protected abstract void addMouseListener();

	/**
	 * �ڹ���ʱ����ó�ʼ����������������ڸ÷�����ʵ����������һ�������Ϳؼ���
	 * 
	 * @param items
	 * 
	 * @return
	 */
	protected abstract T initControl();

	protected T initControlByArray(String[] items,String[] infos) {
		return control;
	};

	/**
	 * @param project
	 * @param filtra 
	 * @return
	 */
	protected T initControlByProject(IProject project, String filtra) {
		return control;
	};

	/**
	 * ��ȡ�ؼ�ֵ������Text�Ϳؼ�Ӧ����Text.getText()��ֵ��
	 * 
	 * @return
	 */
	public abstract Object getValue();
	
	/**
	 * ���ÿؼ�ֵ������Text�Ϳؼ�Ӧ����Text.getText()��ֵ��
	 * 
	 * @return
	 */
	public abstract void setValue(Object object);

	/**
	 * ���ؼ�ֵ�����仯ʱ����Ҫ���¶�Ӧ��ģ��ֵ��
	 * 
	 */
	public abstract void syncModel();

	/**
	 * Ϊ�ؼ�����ģ��ֵ��
	 */
	public abstract void syncControl();

	/**
	 * @return the control
	 */
	public T getControl() {
		return control;
	}

	/**
	 * @param control
	 *            the control to set
	 */
	public void setControl(T control) {
		this.control = control;
	}

	/**
	 * ���ÿؼ���ҳ�沼�֡�
	 * 
	 * @param layoutData
	 */
	public void setControlLayoutData(Object layoutData) {
		if (null != this.control) {
			this.control.setLayoutData(layoutData);
		}
	}

	/**
	 * @return the dirty
	 */
	public EditorDirtyStatus getDirty() {
		return dirty;
	}

	/**
	 * @param dirty
	 *            the dirty to set
	 */
	public void setDirty(EditorDirtyStatus dirty) {
		this.dirty = dirty;
	}

	/**
	 * �Կؼ������Զ��������Ĭ�ϲ����κ���Ϊ��������Ը��Ǹ÷������Կؼ������Զ�������� �������������ʱ����
	 */
	protected void adjustControl() {
	}

	/**
	 * ���������Ϊnull���򷵻ؿ��ַ��������򷵻�ԭ�ַ�����
	 * 
	 * @param maybeNull
	 *            ����Ϊnull���ַ�����
	 * @return ��null�ַ�����
	 */
	public static String wrapString(String maybeNull) {
		return (null == maybeNull || "null".equals(maybeNull)) ? "" : maybeNull;
	}

	public void setEditable(boolean editable) {
		this.control.setEnabled(editable);
	}
	
	public void refresh() {
		removeModifyListener();
		syncControl();
		addModifyListener();
	}
	
	public void setHelpContextId(String id){
		if(id != null){
			PlatformUI.getWorkbench().getHelpSystem().setHelp(control, id);
		}
	}
}
