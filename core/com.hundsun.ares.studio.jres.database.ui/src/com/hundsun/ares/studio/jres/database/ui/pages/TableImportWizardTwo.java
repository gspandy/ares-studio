package com.hundsun.ares.studio.jres.database.ui.pages;


import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hundsun.ares.studio.core.IARESModule;

public class TableImportWizardTwo extends WizardPage {
	private String file = "";
	private String targetDir = "";
	boolean genStdField = true ;
	boolean genType = true ;
	boolean genDoc = true ;
	Composite displayCom = null;
	private static final String TARGET_FILENAME = "\\��׼�ֶκϲ������¼.xls";
	
	
	public TableImportWizardTwo(String pageName,IARESModule module) {
		super(pageName);
		setTitle(pageName);
	}

	public void createControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		
		composite.setLayout(new GridLayout(4,true));
		final Text text = new Text(composite,SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
		text.setEditable(false);
		Button button = new Button(composite,SWT.NORMAL);
		button.setText("ѡ���ļ�");
		button.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));

		final Text targettext = new Text(composite,SWT.BORDER);
		targettext.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
		Button targetButton = new Button(composite,SWT.NORMAL);
		targetButton.setText("��׼�ֶκϲ������¼���Ŀ¼");
		targetButton.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(composite.getShell());
				dialog.setFilterExtensions(new String[]{"*.pdm"});
				file = dialog.open();
				
				if (StringUtils.isNotBlank(file)) {
					text.setText(file);
					targetDir = StringUtils.substringBeforeLast(file, File.separator) + TARGET_FILENAME;
					targettext.setText(targetDir);
				}
				TableImportWizardTwo.this.getContainer().updateButtons();
			}
		});
		targetButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(composite.getShell());
				targetDir = dialog.open();
				targetDir = targetDir + TARGET_FILENAME;
				targettext.setText(targetDir);
			}
		});
		targettext.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				targetDir = targettext.getText();
			}
		});
		
		//����û��ϴ���PDM�ļ�����û��package�ڵ㣬����ʾ�û��Լ�����һ��package
		displayCom = new Composite(composite,SWT.NULL);
		displayCom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 5,
				2));
		displayCom.setLayout(new GridLayout(5,true));
		//������Ĭ��Ϊ����,ֻ���ļ���ȡ�����ʱ�����ʾ
		displayCom.setVisible(false);
		
		Label packageLabel = new Label(displayCom,SWT.NORMAL);
		packageLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));
		packageLabel.setText("��ѡ��ģ������");
		setControl(composite);
	}
	
	public String getFile() {
		return file;
	}
	
	/**
	 * @return the targetDir
	 */
	public String getTargetDir() {
		if (StringUtils.endsWith(targetDir, File.separator) || StringUtils.endsWith(targetDir, "/")) {
			targetDir += TARGET_FILENAME;
		}
		if (!StringUtils.endsWith(targetDir, ".xls")) {
			targetDir += ".xls";
		}
		return targetDir;
	}

	public boolean isGenStdField(){

		return genStdField;
	}

	public boolean isGenType(){

		return genType;
	}
	
	public boolean isGenDoc(){

		return genDoc;
	}
}
