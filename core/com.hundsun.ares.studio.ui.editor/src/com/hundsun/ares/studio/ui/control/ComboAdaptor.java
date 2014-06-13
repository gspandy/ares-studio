/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.control;


import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;

import com.hundsun.ares.studio.ui.util.ImporveControlWithDitryStateContext;



/**
 * ��ǩ-Combo�ؼ��顣
 * 
 * @author liuning. Created on 2008-9-18. Modified by xx on xx.
 */
public class ComboAdaptor extends ControlWithShowControl<Combo> {
	
	String[] items;
	
	SelectionAdapter selectionAdapter;
	
	/**
	 * @param ��ʾ�ֶ�
	 * @param �ؼ���ʽ
	 * @param ������
	 * @param combo��ѡ��
	 * @param ���ֶ�����
	 */
	public ComboAdaptor(String label,int controlStyle,ImporveControlWithDitryStateContext context,String[] items,String beanFieldName) {
		super(label,controlStyle,context,context.getInfo(),beanFieldName);
		initCombo(items);
	}
	
	/**
	 * @param ��ʾ�ֶ�
	 * @param �ؼ���ʽ
	 * @param ������
	 * @param combo��ѡ��
	 */
	public ComboAdaptor(String label,int controlStyle,ImporveControlWithDitryStateContext context,String[] items) {
		super(label,controlStyle,context);
		initCombo(items);
	}
	
	
	/**
	 * @param ��ʾ�ֶ�
	 * @param �ؼ���ʽ
	 * @param ������
	 * @param combo��ѡ��
	 * @param ��ģ��
	 * @param ���ֶ�����
	 */
	public ComboAdaptor(String label,int controlStyle,ImporveControlWithDitryStateContext context,String[] items,Object model,String beanFieldName) {
		super(label,controlStyle,context,model,beanFieldName);
		initCombo(items);
	}
	
	private void initCombo(String[] items){
		this.items = items;
		for(String item:items){
			getControl().add(item);
		}
		syncControl();
	}

	@Override
	public Object getValue() {
		return this.control.getText();
	}
	
	@Override
	public void setValue(Object object) {
		if(object instanceof String){
			 this.control.setText((String)object);
		}
	}
	
	@Override
	protected Combo initControl() {
		Combo combo = new Combo(parent, controlStyle);
		if (null != this.toolkit) {
			combo.setBackground(toolkit.getColors().getBackground());
		}
		return combo;
	}

	@Override
	public void addModifyListener() {
		if (null != this.control) {
			if(selectionAdapter == null){
				selectionAdapter = new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						fireControlValueChange();
					}
				};
			}
			this.control.addSelectionListener(selectionAdapter);
		}
	}
	
	@Override
	public void removeModifyListener() {
		if (null != this.control) {
			this.control.removeSelectionListener(selectionAdapter);
		}
	}

	@Override
	protected void addMouseListener() {
	}
	
	public void setItems(String[] items) {
		this.items = items;
		getControl().removeAll();
		for(String item:items){
			getControl().add(item);
		}
	}
}
