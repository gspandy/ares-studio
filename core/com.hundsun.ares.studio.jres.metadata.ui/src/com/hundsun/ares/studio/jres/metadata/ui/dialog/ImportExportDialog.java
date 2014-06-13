/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.jres.metadata.ui.dialog;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public abstract class ImportExportDialog extends TitleAreaDialog {
	protected String title;
	protected Composite contentPane;
	protected String filePath; 
	protected Map<String, String> fileExtensionMap = new HashMap<String, String>(); 
	public ImportExportDialog(String title, Shell parentShell) {
		super(parentShell);
		this.title = title;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		
		// TitleArea�е�Title
		setTitle(getAreaTitle());

		// TitleArea�е�Message
		setMessage(getAreaMessage());
		
		// �Զ���Ҫ����������
		createContentPane(area);
		createContentControls();
		
		return area;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		// Dialog Title
		newShell.setText(title);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(400, 275);
	}

	/**
	 * �Զ��������⡣
	 * @return title
	 */
	protected abstract String getAreaTitle();
	
	/**
	 * �Զ�������Ϣ��ʾ��
	 * @return message
	 */
	protected abstract String getAreaMessage();
	
	/**
	 * �Զ�����塣
	 */ 
	protected void createContentPane(Composite parent) {
		contentPane = new Composite(parent, SWT.NULL);

		// �������е�GridLayout
		GridLayout layout = new GridLayout(4, false);
		// ����λ�ò���
		layout.marginHeight = 20;
		layout.marginWidth = 20;
//		layout.verticalSpacing = 5;
		layout.horizontalSpacing = 10;
		contentPane.setLayout(layout);
		
		// ���ò��ַ�ʽ
		contentPane.setLayoutData(new GridData(GridData.FILL_BOTH));
		contentPane.setFont(parent.getFont());
	}
	/**
	 * �����Զ�����塣
	 */
	/**
	 * �����Զ�����塣
	 */
	protected void createContentControls() {
		// �ļ����ͱ�ǩ
		Label fileType = new Label(contentPane, SWT.NONE);
		
		GridData layoutData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		fileType.setLayoutData(layoutData);
		fileType.setText("�ļ�����");

		// ����ѡ���
		final Combo typeCombo = new Combo(contentPane, SWT.NONE);
		
		layoutData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 3;
		typeCombo.setLayoutData(layoutData);
		fillTypeCombo(typeCombo);
		
		// �ļ�·����ǩ
		Label fileDir = new Label(contentPane, SWT.NONE);

		layoutData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		fileDir.setLayoutData(layoutData);
		fileDir.setText("�ļ�·��");

		// �ļ�·��ѡ���
		final Text fileDirText = new Text(contentPane, SWT.BORDER | SWT.LEAD);
		fileDirText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validate(fileDirText.getText());
			}

		});
		layoutData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 2;
		fileDirText.setLayoutData(layoutData);
		fileDirText.setToolTipText("dialog.login.passwordTooltip");
		// �ļ�·��ѡ��ť
		Button dirButton = new Button(contentPane, SWT.NONE);
		dirButton.setText("���...");
		dirButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
		        FileDialog dialog = new FileDialog(contentPane.getShell(), SWT.OPEN);
		        String filterExtension = (String) typeCombo.getData(typeCombo.getText());
		        dialog.setFilterExtensions(new String[] { filterExtension });
		        dialog.setFilterNames(new String[] { fileExtensionMap.get(filterExtension) });
		    	filePath = dialog.open();
				fileDirText.setText(filePath);
			
		        // �����ļ�·���������ͺ�׺�����
		        if(filterExtension != "All") {
			        String fileSuffix = filterExtension.substring(1);
		        	if(!filePath.endsWith(fileSuffix)) {
		        		filePath = filePath + fileSuffix;
		        	}
		        }
		    }

		});
		
		// ��������Զ�������
		Composite additionComp = createExtendContentPane(contentPane);
		if(additionComp != null) {
			layoutData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
			layoutData.horizontalSpan = 4;
			additionComp.setLayoutData(layoutData);
		}
	} 
	public abstract boolean validate(String fileText) ;
	/**
	 * ���������
	 * @param combo ���������
	 */
	protected void fillTypeCombo(Combo combo) {
		// Ŀǰֻ֧�֡�Excel���ļ�����
		combo.add("Excel");
		combo.setData("Excel", new String("*.xls"));
		fileExtensionMap.put("*.xls", "Excel�ļ�(*.xls)");
		
		combo.add("All");
		combo.setData("All", new String("*.*"));
		fileExtensionMap.put("*.*", "ȫ���ļ�(*.*)");
		
		combo.select(0);
	}
	
	/**
	 * ��չ���Զ�����塣
	 * @param parent
	 * @return
	 */
	protected abstract Composite createExtendContentPane(Composite parent);

	/**
	 * ��ȡ�ļ�·����
	 * @return �ļ�·��
	 */
	public String getFilePath() {
		return filePath;
	}
	
}
