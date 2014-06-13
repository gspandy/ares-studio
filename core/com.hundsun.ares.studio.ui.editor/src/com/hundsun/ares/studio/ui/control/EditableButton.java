/**
* <p>Copyright: Copyright   2010</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.control;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * �ɿ���ֻ��״̬�İ�ť
 * @author maxh
 *
 */
public class EditableButton implements IEditable {
	Button button = null;
	public EditableButton(Composite parent, int style) {
		button = new Button(parent, style);
	}

	public void setEditable(boolean editable) {
		if(button != null){
			button.setEnabled(editable);
		}
	}
	
	public Button getButton() {
		return button;
	}
	
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}
