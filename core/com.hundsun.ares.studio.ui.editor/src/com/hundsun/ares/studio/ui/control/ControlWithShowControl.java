/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.control;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.Hyperlink;

import com.hundsun.ares.studio.core.util.StringUtil;
import com.hundsun.ares.studio.ui.util.ImporveControlWithDitryStateContext;




/**
 * ��һ��չʾ��ǩ�Ŀؼ�
 * չʾ��ǩ������labelҲ�����ǳ�����
 *
 * @author liuning.
 * Created on 2008-9-18.
 * Modified by xx on xx.
 */
public abstract class ControlWithShowControl<T extends Control> extends ControlWithUndoSupport<T> {
	
	
	/**
	 * ��������ؼ�֮ǰ��Label�ؼ���Ϊ����ؼ��ṩ˵����
	 */
	protected Control showControl;
	String label;

	/**
	 * ����һ����Label�Ŀؼ���
	 * @param toolkit 
	 * 
	 * @param parent �������
	 * @param label ��ǩ�ؼ���
	 * @param labelStyle ��ǩ�ؼ�����ʽ��
	 * @param dirtyStatus �����Ϳؼ�����״̬����Ӧ����༭����״̬����
	 * @param filtra 
	 * @param project 
	 */
	public ControlWithShowControl(String label, int controlStyle,ImporveControlWithDitryStateContext context) {
		super(context, controlStyle);
		this.label = label;
		initLabel();
	}
	
	public ControlWithShowControl(String label, int controlStyle,ImporveControlWithDitryStateContext context,String beanFieldName) {
		super(context, controlStyle,beanFieldName);
		this.label = label;
		initLabel();
	}
	
	public ControlWithShowControl(String label, int controlStyle,ImporveControlWithDitryStateContext context,Object model,String beanFieldName) {
		super(context, controlStyle,model,beanFieldName);
		this.label = label;
		initLabel();
	}
	
	public int GetLabelStyle(){
		return SWT.NONE;
	}
	
	protected void initLabel() {
		this.showControl = new Label(parent, GetLabelStyle());
		((Label)this.showControl).setText(StringUtil.getStringSafely(label));
		this.movePosition();
		this.setStyle();
		//�����ؼ�����
		adjustControl();
	}
	
	/**
	 * �����������
	 */
	protected void setStyle() {
		setLabelLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		setControlLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}
	
	/**
	 * ��������ؼ����ǩ�ؼ���λ�á�
	 */
	private void movePosition() {
		this.showControl.moveAbove(this.control);
	}

	/**
	 * @return the label
	 */
	public Control getShowControl() {
		return showControl;
	}

	/**
	 * @param label the label to set
	 */
	public void setShowControl(Control showControl) {
		this.showControl = showControl;
	}

	
	/**
	 * ΪLabel�����Ű沼�֡�
	 * 
	 * @param layoutData
	 */
	public void setLabelLayoutData(Object layoutData) {
		if (null != this.showControl) {
			this.showControl.setLayoutData(layoutData);
		}
	}
	
	/**
	 * ����ͷ�ؼ�ת��ΪHyperlink
	 * @return
	 */
	public Hyperlink switchHyperlink(){
		if(showControl instanceof Hyperlink){
			return (Hyperlink)showControl;
		}
		String text = "";
		if(showControl instanceof Label){
			text = ((Label)showControl).getText();
		}
		showControl.dispose();
		showControl = new Hyperlink(parent, GetLabelStyle());
		((Hyperlink)showControl).setUnderlined(true);
		((Hyperlink)showControl).setText(text);
		movePosition();
		return (Hyperlink)showControl;
	}
	
}
