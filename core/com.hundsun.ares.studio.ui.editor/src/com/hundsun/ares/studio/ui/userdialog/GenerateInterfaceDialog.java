/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.userdialog;

import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
 * FIXME ���ɽ���Ի���
 * ����xml���ö�̬�����û�����
 * ���ṩCHECK,RADIO,TEXT,COMBO���ֿؼ�������
 * @author maxh
 * @version 1.0
 * @history
 */
public class GenerateInterfaceDialog extends org.eclipse.jface.dialogs.Dialog {
	DialogInterfaceXml xmlDialog;

	public GenerateInterfaceDialog (Shell parentShell, DialogInterfaceXml xmlDialog) {
		super (parentShell);
		this.xmlDialog = xmlDialog;
	}

	// ���ɿؼ�������
	Control[][] control;
	// ���ڷ�װ�ؼ�value
	/*
	 * �ؼ����ݼ��� keyΪ��ſؼ�xml�ж�������Լ�value��map valueΪ�ؼ��Ƿ�ѡ�еĲ���ֵ����ҪΪcheck��radio����
	 */
	HashMap<String, Object> resultMap = new HashMap<String, Object> ();

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea (parent);
		control = new Control[xmlDialog.getLstMenuInterfaceGroup().size ()][];
		for (int groupIndex = 0; groupIndex < xmlDialog.getLstMenuInterfaceGroup().size (); groupIndex++) {
			DialogInterfaceGroup group = xmlDialog.getLstMenuInterfaceGroup().get (groupIndex);
			final List<DialogInterfaceItem> lstItem = group.getLstMenuInterfaceItem ();
			Composite subComposite;
			if (group.isUse ()) {
				Group menuGroup = new Group (composite, SWT.NONE);
				menuGroup.setVisible (true);
				menuGroup.setLayout (new RowLayout ());
				menuGroup.setText (group.getGroupName ());
				subComposite = menuGroup;
			}else{
				// ����һ������������ڰڷ�group֮��Ĳ��ֿؼ�
				subComposite = new Composite (composite, SWT.NONE);
			}
			GridLayout layout = new GridLayout (6, false);
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			subComposite.setLayout (layout);
			control[groupIndex] = new Control[lstItem.size ()];
			for (int i = 0; i < lstItem.size (); i++) {

				if (lstItem.get (i).getSwtType ().equalsIgnoreCase ("CHECK")||lstItem.get (i).getSwtType ().equalsIgnoreCase ("RADIO")) {
					// ��ť�ؼ�
					int swtType = SWT.NULL;
					if (lstItem.get (i).getSwtType ().equalsIgnoreCase ("CHECK")) swtType = SWT.CHECK;
					if (lstItem.get (i).getSwtType ().equalsIgnoreCase ("RADIO")) swtType = SWT.RADIO;
					Button btn = new Button (subComposite, swtType);
					btn.setText (lstItem.get (i).getLableName ());
					btn.setData (lstItem.get (i).getId());
					btn.setSelection(lstItem.get(i).getValue().equalsIgnoreCase("true"));
					control[groupIndex][i] = btn;
				}else if (lstItem.get (i).getSwtType ().equalsIgnoreCase ("TEXT")) {
					// Text�ؼ�
					Label lable = new Label (subComposite, SWT.NONE);
					lable.setText (lstItem.get (i).getLableName () + ":");
					Text text = new Text (subComposite, SWT.BORDER);
					text.setData (lstItem.get (i).getId());
					text.setText(lstItem.get(i).getValue());
					control[groupIndex][i] = text;
				}else if (lstItem.get (i).getSwtType ().equalsIgnoreCase ("COMBO")) {
					// ������ؼ�
					Label lable = new Label (subComposite, SWT.NONE);
					lable.setText (lstItem.get (i).getLableName () + ":");
					Combo combo = new Combo (subComposite, SWT.BORDER);
					String[] values = lstItem.get (i).getValue ().split (",");
					for (String value : values)
						combo.add (value);
					combo.select (0);
					combo.setData (lstItem.get (i).getId());
					control[groupIndex][i] = combo;
				}
			}

		}
		return composite;
	}

	@Override
	protected void okPressed() {
		// ѭ����ȡ�ؼ��е�����
		for (int i = 0; i < control.length; i++) {
			for (int j = 0; j < control[i].length; j++) {
				String value = control[i][j].getData ().toString ();
				if (control[i][j] instanceof Button) {
					Button btn = (Button) control[i][j];
					resultMap.put(value, btn.getSelection());
				}else if (control[i][j] instanceof Text) {
					Text text = (Text) control[i][j];
					resultMap.put(value, text.getText());
				}else if (control[i][j] instanceof Combo) {
					Combo combo = (Combo) control[i][j];
					resultMap.put(value, combo.getText());
				}
			}
		}
		super.okPressed ();
	}
	
	@Override
	public Shell getShell() {
		// TODO Auto-generated method stub
		Shell shell = super.getShell();
		shell.setText(xmlDialog.getTitle());
		return shell;
	}
	
	public HashMap<String, Object> getResultMap() {
		return resultMap;
	}
}
