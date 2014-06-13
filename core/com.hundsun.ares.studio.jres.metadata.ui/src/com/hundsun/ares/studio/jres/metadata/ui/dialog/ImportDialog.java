/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.jres.metadata.ui.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ImportDialog extends ImportExportDialog {
	// ����ģʽ������
	public static final int IMPORT_TYPE_COVER = 0;
	// ����ģʽ���ϲ�
	public static final int IMPORT_TYPE_COMB = 1;
	
	private  String areaMessage = "���Ѵ��ڵ�Excel�ļ����뵽��Ŀ��.";
	private int importType = IMPORT_TYPE_COVER; 
	
	protected Button importButton;
	private Button coverButton;
	private Button combButton;
	
	private String coverString = "����ʽ";
	private String combString = "�ϲ�ʽ";
	private Image image;
	//�Ƿ���Ҫ�ϲ��͸���ѡ��
	private boolean needComb = true;
	
	public ImportDialog(Shell parentShell) {
		super("Import", parentShell);
	}
	
	public ImportDialog(Shell parentShell,boolean needComb,String title,Image image,String dialogMessage) {
		super("Import", parentShell);
		this.needComb = needComb;
		this.title = title;
		this.image = image;
		areaMessage = dialogMessage;
	}
	
	public ImportDialog(Shell parentShell,boolean needComb,String coverString,String combString,String title,Image image,String dialogMessage) {
		super("Import", parentShell);
		this.needComb = needComb;
		this.combString = combString;
		this.coverString = coverString;
		this.title = title;
		this.image = image;
		areaMessage = dialogMessage;
	}
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText(title);
		if(image!=null){
			newShell.setImage(image);
		}
	}
	
	public boolean validate(String fileText) {
		 if (filePath == null || (filePath != null && !filePath.equals(fileText))) {
				setErrorMessage("����ѡ����ȷ�ĵ����ļ���·�����ܽ��е������!");
				importButton.setEnabled(false);
			return false;
		}
		setErrorMessage(null);
		importButton.setEnabled(true);
		return true;
	}
	@Override
	protected Composite createExtendContentPane(Composite parent) {
		Composite extendComp = new Composite(parent, SWT.NONE);
		// ���ö��е�GridLayout
		GridLayout layout = new GridLayout(3, false);
		extendComp.setLayout(layout);
		
		Label label = new Label(extendComp, SWT.NONE);
		label.setText("����ģʽ");
		
		coverButton = new Button(extendComp, SWT.NONE | SWT.RADIO);
		coverButton.setText(coverString);
		coverButton.setSelection(true);
		
		combButton = new Button(extendComp, SWT.NONE | SWT.RADIO);
		combButton.setText(combString);
		
		if(!needComb){
			label.setVisible(false);
			coverButton.setVisible(false);
			combButton.setVisible(false);
		}
		GridLayoutFactory.fillDefaults().numColumns(4).applyTo(extendComp);
		GridDataFactory.fillDefaults().span(1, 1).applyTo(label);
		GridDataFactory.fillDefaults().span(1, 1).applyTo(coverButton);
		GridDataFactory.fillDefaults().span(1, 1).applyTo(combButton);
		return extendComp;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		importButton =createButton(parent, IDialogConstants.OK_ID, "����", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "ȡ��", false);
		importButton.setEnabled(false);
	}

	@Override
	protected String getAreaMessage() {
		return areaMessage;
	}

	@Override
	protected String getAreaTitle() {
		return title;
	}
	
	@Override
	protected void okPressed() {
		if(needComb){
			importType = coverButton.getSelection() ? IMPORT_TYPE_COVER : IMPORT_TYPE_COMB;
		}
		super.okPressed();
	}

	/**
	 * ��������
	 */
	public int getImportType() {
		return importType;
	}
	
}
