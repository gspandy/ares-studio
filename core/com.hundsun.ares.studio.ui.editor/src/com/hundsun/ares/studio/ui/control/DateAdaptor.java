/**
* <p>Copyright: Copyright   2010</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.control;

import java.util.Date;

import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;

import com.hundsun.ares.studio.ui.util.ImporveControlWithDitryStateContext;

public class DateAdaptor  extends ControlWithShowControl<CDateTime>{

	CDateTime combo;
	
	SelectionAdapter selectionAdapter;
	
	/**
	 * @param ��ʾ�ֶ�
	 * @param �ؼ���ʽ
	 * @param ������
	 */
	public DateAdaptor(String label,int controlStyle,ImporveControlWithDitryStateContext context) {
		super(label,controlStyle, context);
	}
	
	/**
	 * @param ��ʾ�ֶ�
	 * @param �ؼ���ʽ
	 * @param ������
	 * @param ���ֶ�����
	 */
	public DateAdaptor(String label,int controlStyle,ImporveControlWithDitryStateContext context,String beanFieldName) {
		super(label,controlStyle, context,context.getInfo(),beanFieldName);
	}
	
	/**
	 * @param ��ʾ�ֶ�
	 * @param �ؼ���ʽ
	 * @param ������
	 * @param ��ģ��
	 * @param ���ֶ�����
	 */
	public DateAdaptor(String label,int controlStyle,ImporveControlWithDitryStateContext context,Object model,String beanFieldName) {
		super(label,controlStyle, context,model,beanFieldName);
	}
	
	@Override
	public void addModifyListener() {
		if (null != this.control) {
			if(selectionAdapter == null){
				selectionAdapter = new SelectionAdapter(){
					@Override
					public void widgetSelected(SelectionEvent e) {
						fireControlValueChange();
					}
				};
			}
			combo.addSelectionListener(selectionAdapter);
		}
	}
	
	@Override
	public void removeModifyListener() {
		if (null != this.control) {
			combo.removeSelectionListener(selectionAdapter);
		}
	}

	@Override
	protected void addMouseListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return combo.getSelection();
	}

	@Override
	protected CDateTime initControl() {
		combo = new CDateTime(parent, CDT.BORDER | CDT.COMPACT | CDT.DROP_DOWN  );
		combo.setSelection(new Date());
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		return combo;
	}

	@Override
	public void setValue(Object object) {
		if(object instanceof Date){
			combo.setSelection((Date)object);
		}
	}

}
