/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.internal.ui;


import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Ŀ��λ��ѡ����
 * @author sundl
 */
public class DestinationSelector extends Composite {

	private Label label;
	private Text text;
	private Button button;
	
	private String[] fileExts;
	private String[] filterNames;
	private String title;
	
	private String path;
	
	public DestinationSelector(Composite parent, String title, String[] filterExts, String[] filterNames) {
		super(parent, SWT.NONE);
		this.fileExts = filterExts;
		this.filterNames = filterNames;
		this.title = title;
		init();
	}
	
	protected void init() {
		setLayout(new GridLayout(3, false));
		label = new Label(this, SWT.NONE);
		label.setText("·��:");
		text = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				path = text.getText();
			}
		});
		
		button = new Button(this, SWT.PUSH);
		button.setText("���");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell());
				dialog.setFilterNames(filterNames);
				dialog.setOverwrite(true);
				dialog.setFilterExtensions(fileExts);
				dialog.setText(title);
				
				String result = dialog.open();
				if (result != null) {
					if (!result.endsWith(".ares")) {
						result = result.concat(".ares");
					}
					File file = new File(result);
					if (file.exists()) {
						boolean overwrite = MessageDialog.openConfirm(getShell(), "����", "�ļ��Ѵ����Ƿ񸲸ǣ�");
						if (overwrite) {
							text.setText(result);
						} else {
							text.setText("");
						}
						return;
					}
					text.setText(result);
				}
			}
			
		});
	}

	public String[] getFileExts() {
		return fileExts;
	}

	public void setFileExts(String[] fileExts) {
		this.fileExts = fileExts;
	}

	public String getDestination() {
		return path;
	}

	public void setDestination(String destination) {
		this.path = destination;
	}

	public Label getLabel() {
		return label;
	}

	public Text getText() {
		return text;
	}

	public Button getButton() {
		return button;
	}
	
	
}
