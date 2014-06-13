/**
* <p>Copyright: Copyright   2010</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.celleditor;

import java.util.Arrays;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * �����Ҫ�ڱ������ؼ���ʹ��combo��celleditor
 * ��������ǵĶ���Ҫ��jface�� �����Ͳ�����дgetValue��setValue��
 * 
 * @author maxh
 *
 */
public class ComboBoxCellEditor extends org.eclipse.jface.viewers.ComboBoxCellEditor implements ISprecialCellEditor{

	public ComboBoxCellEditor(Composite parent, String[] items, int style) {
		super(parent,items,style);
	}
	
	public Image getImage(Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getRealGetValue(Object value) {
		if(value instanceof String){
			return Arrays.asList(getItems()).indexOf(value);
		}
		return value;
	}

	public Object getRealSetValue(Object value) {
		if(value instanceof Integer){
			if((Integer)value != -1){
				Integer i = (Integer)value;
				return getItems()[i];
			}
		}
		return value;
	}

	public String getText(Object value) {
		// TODO Auto-generated method stub
		return value.toString();
	}


}
