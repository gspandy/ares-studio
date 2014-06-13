/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.jres.metadata.ui.wizards;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.hundsun.ares.studio.internal.core.ARESProject;
import com.hundsun.ares.studio.jres.metadata.ui.dialog.ImportDialog;

/**
 * @author gongyf
 *
 */
public class ImportExcelWizardPage extends SelectProjectAndExcelFileWizardPage {

	private Button coverButton;
	private Button combButton;
	private int importType = ImportDialog.IMPORT_TYPE_COVER; 
	/**
	 * @param pageName
	 * @param selection
	 */
	public ImportExcelWizardPage(String pageName,
			IStructuredSelection selection) {
		super(pageName, selection);
		
		setTitle("�����׼�ֶ�");
		setDescription("�����׼�ֶ��Լ������Ϣ��������ҵ���������͡���׼�������͡�Ĭ��ֵ.ѡ����Ҫ����Ԫ���ݵ�ARES���̺͵����Ԫ���ݶ����ļ�");
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.wizards.SelectProjectAndExcelFileWizardPage#newFileDialog(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected FileDialog newFileDialog(Shell shell) {
		FileDialog dlg = new FileDialog(shell, SWT.OPEN);
		return dlg;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.wizards.SelectProjectAndExcelFileWizardPage#validate()
	 */
	@Override
	protected void validate() {
		if (project == null || !ARESProject.hasARESNature(project)) {
			setErrorMessage("��ѡ��һ������");
			setPageComplete(false);
		} else if (getExcelFile() == null || !getExcelFile().exists()) {
			setErrorMessage("ѡ��ı�׼�ֶζ����ļ�������");
			setPageComplete(false);
		} else {
			setErrorMessage(null);
			setPageComplete(true);
		}
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		Composite group = new Composite((Composite) getControl(), SWT.NONE);
		Label mode = new Label(group, SWT.NONE);
		mode.setText("����ģʽ");
		
		coverButton = new Button(group, SWT.NONE | SWT.RADIO);
		coverButton.setText("����ʽ");
		coverButton.setSelection(true);
		
		combButton = new Button(group, SWT.NONE | SWT.RADIO);
		combButton.setText("�ϲ�ʽ");
		
		combButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button button = (Button) e.getSource();
				if (button.getSelection()) {
					 importType = ImportDialog.IMPORT_TYPE_COMB;
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		GridDataFactory.fillDefaults().span(3, 1).applyTo(group);
		GridLayoutFactory.fillDefaults().numColumns(3).applyTo(group);
		GridDataFactory.swtDefaults().applyTo(mode);
		GridDataFactory.swtDefaults().applyTo(coverButton);
		GridDataFactory.swtDefaults().applyTo(combButton);
	}
	
	public int getImportMode (){
		return importType;
	}
	
}
