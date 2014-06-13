/**
 * <p>Copyright: Copyright   2010</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.control;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

import com.hundsun.ares.studio.ui.util.EditorDirtyStatus;
import com.hundsun.ares.studio.ui.util.ImporveControlWithDitryStateContext;
import com.hundsun.ares.studio.ui.util.ReflectInvokeHelper;

/**
 * Ϊ�ؼ��ṩ���ݰ󶨹���
 * 
 * @author maxh
 * @param <T>
 */
public abstract class ControlWithDataBind<T extends Control> extends ControlWithDitryState<T> {
	private static Logger logger = Logger.getLogger(ControlWithDataBind.class);

	public ControlWithDataBind(ImporveControlWithDitryStateContext context, int controlStyle) {
		super(context.getToolkit(), context.getParent(), controlStyle, context.getDirtyStatus());
		addListener();
	}

	public ControlWithDataBind(ImporveControlWithDitryStateContext context, int controlStyle, String beanFieldName) {
		super(context.getToolkit(), context.getParent(), controlStyle, context.getDirtyStatus());
		try {
			refleckHelper = new ReflectInvokeHelper(context.getInfo(), beanFieldName);
		} catch (Exception e) {
			logger.debug("�ؼ����ݰ󶨷����쳣", e);
			refleckHelper = null;
		}
		addListener();
	}

	public ControlWithDataBind(ImporveControlWithDitryStateContext context, int controlStyle, Object model,
			String beanFieldName) {
		super(context.getToolkit(), context.getParent(), controlStyle, context.getDirtyStatus());
		try {
			refleckHelper = new ReflectInvokeHelper(model, beanFieldName);
		} catch (Exception e) {
			logger.debug("�ؼ����ݰ󶨷����쳣", e);
			refleckHelper = null;
		}
		addListener();
	}

	public ControlWithDataBind(Control control, EditorDirtyStatus dirty, String beanFieldName) {
		super(control.getParent(), SWT.None, dirty);
		setControl((T) control);
		try {
			refleckHelper = new ReflectInvokeHelper(new Object(), beanFieldName);
		} catch (Exception e) {
			logger.debug("�ؼ����ݰ󶨷����쳣", e);
			refleckHelper = null;
		}
		addListener();
	}

	public ControlWithDataBind(EditorDirtyStatus dirty, String beanFieldName) {
		super(null, null, SWT.None, dirty);
		try {
			refleckHelper = new ReflectInvokeHelper(new Object(), beanFieldName);
		} catch (Exception e) {
			logger.debug("�ؼ����ݰ󶨷����쳣", e);
			refleckHelper = null;
		}
		addListener();
	}

	void addListener() {
		this.syncControl();
		this.addModifyListener();
		this.addFocusListener();
		this.addMouseListener();
	}

	/**
	 * �ؼ���ֵ�����ı� 1.ˢ��ģ�� 2.������״̬ 3.������
	 */
	protected void fireControlValueChange() {
		syncModel();
		dirty.setValue(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hundsun.ares.devtool.v2.ui.control.ControlWithDitryState#addFocusListener
	 * ()
	 */
	@Override
	protected void addFocusListener() {

	}

	protected ReflectInvokeHelper refleckHelper;

	public ReflectInvokeHelper getRefleckHelper() {
		return refleckHelper;
	}

	@Override
	public void syncControl() {
		if (refleckHelper != null) {
			Object value = refleckHelper.invokeGetMothod();
			if (value != null) {
				setValue(value);
			}
		}
	}

	@Override
	public void syncModel() {
		if (refleckHelper != null) {
			Object value = getValue();
			refleckHelper.invokeSetMothod(value);
		}
	}
}
