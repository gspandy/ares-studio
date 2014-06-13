/**
 * Դ�������ƣ�JresDefaultEditableUnit.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�����
 */
package com.hundsun.ares.studio.ui.editor.editable;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * @author lvgao
 *
 */
public class JresDefaultEditableUnit implements IEditableUnit{

	Control control;
	
	public JresDefaultEditableUnit(Control control){
		this.control = control;
	}
	
	@Override
	public void setReadonlyStatus(String key, Object status) {
		if(StringUtils.equals(KEY_SYSTEM, key)){
			if(EDITABLE_TRUE.equals(status)){
				setControlStatus(true);
			}else{
				setControlStatus(false);
				this.control.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
			}
		}
	}

	private void setControlStatus(boolean status){
		if (this.control instanceof Text) {
			((Text)this.control).setEditable(status);
		}else {
			this.control.setEnabled(status);
		}
	}
	
}
