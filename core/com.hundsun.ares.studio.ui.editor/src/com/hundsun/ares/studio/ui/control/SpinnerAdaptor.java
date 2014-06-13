/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.control;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Spinner;

import com.hundsun.ares.studio.ui.util.ImporveControlWithDitryStateContext;

/**
 * ΢����ť������
 * 
 * @author mawb
 */
public class SpinnerAdaptor extends ControlWithShowControl<Spinner> {
	
	ModifyListener modifyListener;
	
	private Boolean isModify = false; 
	
	/**
	 * ΢����ť���캯��
	 * 
	 * @param label ��ʾ�ֶ�
	 * @param controlStyle �ؼ���ʽ
	 * @param context �����Ļ���
	 * @param mininum ��Сֵ
	 * @param maxinum ���ֵ
	 */
	public SpinnerAdaptor(String label, int controlStyle, ImporveControlWithDitryStateContext context, int mininum, int maxinum) {
		super(label, controlStyle, context);
		this.controlStyle = controlStyle;
		initControl(mininum, maxinum, 1);
	}
	
	/**
	 * ΢����ť���캯��
	 * 
	 * @param label ��ʾ�ֶ�
	 * @param controlStyle �ؼ���ʽ
	 * @param context �����Ļ���
	 * @param mininum ��Сֵ
	 * @param maxinum ���ֵ
	 * @param beanFieldName ���ֶ�����
	 */
	public SpinnerAdaptor(String label, int controlStyle, ImporveControlWithDitryStateContext context, int mininum, int maxinum, String beanFieldName) {
		super(label, controlStyle, context, context.getInfo(), beanFieldName);
		this.controlStyle = controlStyle;
		initControl(mininum, maxinum, 1);
	}
	
	/**
	 * ΢����ť���캯��
	 * 
	 * @param label ��ʾ�ֶ�
	 * @param controlStyle �ؼ���ʽ
	 * @param context �����Ļ���
	 * @param mininum ��Сֵ
	 * @param maxinum ���ֵ
	 * @param model ��ģ��
	 * @param beanFieldName ���ֶ���
	 */
	public SpinnerAdaptor(String label, int controlStyle, ImporveControlWithDitryStateContext context, int mininum, int maxinum, Object model, String beanFieldName) {
		super(label, controlStyle, context, model, beanFieldName);
		this.controlStyle = controlStyle;
		initControl(mininum, maxinum, 1);
	}
	
	/**
	 * ΢����ť���캯��
	 * 
	 * @param label ��ʾ�ֶ�
	 * @param controlStyle �ؼ���ʽ
	 * @param context �����Ļ���
	 * @param mininum ��Сֵ
	 * @param maxinum ���ֵ
	 * @param increment ����ֵ
	 */
	public SpinnerAdaptor(String label, int controlStyle, ImporveControlWithDitryStateContext context, int mininum, int maxinum, int increment) {
		super(label, controlStyle, context);
		this.controlStyle = controlStyle;
		initControl(mininum, maxinum, increment);
	}
	
	/**
	 * ΢����ť���캯��
	 * 
	 * @param label ��ʾ�ֶ�
	 * @param controlStyle �ؼ���ʽ
	 * @param context �����Ļ���
	 * @param mininum ��Сֵ
	 * @param maxinum ���ֵ
	 * @param increment ����ֵ
	 * @param beanFieldName ���ֶ���
	 */
	public SpinnerAdaptor(String label, int controlStyle, ImporveControlWithDitryStateContext context, int mininum, int maxinum, int increment, String beanFieldName) {
		super(label, controlStyle, context, context.getInfo(), beanFieldName);
		this.controlStyle = controlStyle;
		initControl(mininum, maxinum, increment);
	}
	
	/**
	 * ΢����ť���캯��
	 * 
	 * @param label ��ʾ�ֶ�
	 * @param controlStyle �ؼ���ʽ
	 * @param context �����Ļ���
	 * @param mininum ��Сֵ
	 * @param maxinum ���ֵ
	 * @param increment ����ֵ
	 * @param model ��ģ��
	 * @param beanFieldName ���ֶ�����
	 */
	public SpinnerAdaptor(String label, int controlStyle, ImporveControlWithDitryStateContext context, int mininum, int maxinum, int increment, Object model, String beanFieldName) {
		super(label, controlStyle, context, model, beanFieldName);
		this.controlStyle = controlStyle;
		initControl(mininum, maxinum, increment);
	}
	
	/**
	 * ��ʼ��΢����ť����
	 * 
	 * @param mininum ��Сֵ
	 * @param maxinum ���ֵ
	 * @param increment ����ֵ
	 */
	protected void initControl(int mininum, int maxinum, int increment) {
		synchronized (isModify) {
			isModify = false;
			getControl().setMinimum(mininum);
			getControl().setMaximum(maxinum);
			getControl().setIncrement(increment);
			syncControl();
			isModify = true;
		}
	}
	
	@Override
	public void addModifyListener() {
		if(modifyListener == null){
			modifyListener = new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					fireControlValueChange(isModify);
				}
			};
		}
		control.addModifyListener(modifyListener);
	}
	
	/**
	 * �����ؼ�ֵ�仯�¼�
	 * 
	 * @param isModify Spinner���������Сֵʱ�ᴥ��ModifyListener���˴���isModify���������Ƿ񴥷����¼�
	 */
	protected void fireControlValueChange(boolean isModify) {
		if (isModify) {
			super.fireControlValueChange();
		}
	}

	@Override
	protected void addMouseListener() {
	}

	@Override
	public Object getValue() {
		return control.getSelection();
	}
	
	@Override
	public void setValue(Object object) {
		if (object instanceof Integer) {
			control.setSelection((Integer) object);
		}
	}

	@Override
	protected Spinner initControl() {
		Spinner spinner = new Spinner(parent, controlStyle);
		if (toolkit != null) {
			spinner.setBackground(toolkit.getColors().getBackground());
		}
		return spinner;
	}

	@Override
	public void removeModifyListener() {
		if (control != null) {
			control.removeModifyListener(modifyListener);
		}
	}

}
