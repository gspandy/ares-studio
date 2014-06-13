/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.celleditor;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;

/**
 * ��չComboBoxCellEditor���ܣ���ҪӦ���������б���ʾֵ����ʵֵ��ͬ�������
 * 
 * @author mawb
 */
public class ComboBoxCellEditor2 extends ComboBoxCellEditor {
	
	/**
	 * <��ʵֵ����ʾֵ>��ֵ��
	 */
	private Map<String, String> items;
	
	/**
	 * @param parent ������
	 * @param items <��ʵֵ����ʾֵ>��ֵ��
	 * @param style ��Ԫ����ʽ
	 */
	public ComboBoxCellEditor2(Composite parent, Map<String, String> items, int style) {
		super(parent, items.values().toArray(new String[items.size()]), style);
		this.items = items;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.celleditor.ComboBoxCellEditor#getRealGetValue(java.lang.Object)
	 */
	@Override
	public Object getRealGetValue(Object value) {
		if (value instanceof String) {
			value = getDisplayValue((String) value);
		}
		return super.getRealGetValue(value);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.celleditor.ComboBoxCellEditor#getRealSetValue(java.lang.Object)
	 */
	@Override
	public Object getRealSetValue(Object value) {
		value = super.getRealSetValue(value);
		if (value instanceof String) {
			return getRealValue((String) value);
		}
		return value;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.celleditor.ComboBoxCellEditor#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object value) {
		if (value instanceof String) {
			return getDisplayValue((String) value);
		}
		return super.getText(value);
	}
	
	/**
	 * ���������б����ʾֵ��ȡ��ʵֵ��
	 * 
	 * @param comboValues
	 * @param displayValue
	 * @return
	 */
	private String getRealValue(String displayValue) {
		for (String realValue : items.keySet()) {
			if (displayValue.equals(items.get(realValue))) {
				return realValue;
			}
		}
		return displayValue;
	}
	
	/**
	 * ���������б����ʵֵ��ȡ��ʾֵ��
	 * 
	 * @param comboValues
	 * @param displayValue
	 * @return
	 */
	private String getDisplayValue(String realValue) {
		String displayValue = items.get(realValue);
		if (displayValue != null) {
			return displayValue;
		}
		return realValue;
	}

}
