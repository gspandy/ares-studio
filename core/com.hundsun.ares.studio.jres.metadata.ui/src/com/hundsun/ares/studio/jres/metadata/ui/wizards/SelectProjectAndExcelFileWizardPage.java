/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.jres.metadata.ui.wizards;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.internal.core.ARESProject;

/**
 * @author gongyf
 *
 */
public abstract class SelectProjectAndExcelFileWizardPage extends WizardPage {

	protected IProject project;
	protected File excelFile;
	
	/**
	 * @param pageName
	 */
	protected SelectProjectAndExcelFileWizardPage(String pageName, IStructuredSelection selection) {
		super(pageName);
		
		IProject project = null;
		Object obj = selection.getFirstElement();
		if (obj instanceof IProject) {
			project = (IProject) obj;
		} else if (obj instanceof IARESElement) {
			project = ((IARESElement) obj).getARESProject().getProject();
		} else {
			// �ڿհ״��Ҽ������򵼵�����£�objΪ��
			// BUG #3297::�ڹ����ռ�հ״�����Ҽ�ѡ�񡰵��롱�򡰵�������˫����JRES���µĲ˵���󱨴�
			if (obj != null)
				project = (IProject) ResourceUtil.getAdapter(obj, IProject.class, true);
		}
		
		this.project = project;
	}

	/**
	 * @return the project
	 */
	public IProject getSelectedProject() {
		return project;
	}
	
	/**
	 * @return the excelFile
	 */
	public File getExcelFile() {
		return excelFile;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		
		Composite client = new Composite(parent, SWT.NONE);
		
		TableViewer viewer = new TableViewer(client, SWT.SINGLE | SWT.BORDER);
		viewer.setLabelProvider( WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider());
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects());

		// ���˵���JRES����
		viewer.setFilters(new ViewerFilter[]{
				new ViewerFilter() {
					
					@Override
					public boolean select(Viewer viewer, Object parentElement, Object element) {
						IProject project = (IProject) element;
						return ARESProject.hasARESNature(project);
					}
				}
		});
		
		if (project != null) {
			viewer.setSelection(new StructuredSelection(project), true);
		}
		
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				project = (IProject) ((IStructuredSelection)event.getSelection()).getFirstElement();
				
				validate();
			}
		});
		
		
		Label label = new Label(client, SWT.NONE);
		label.setText("Excel�ļ���");
		
		final Text text = new Text(client, SWT.SINGLE | SWT.BORDER);
		text.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				excelFile = new File(text.getText());
				validate();
			}
		});
		
		Button button = new Button(client, SWT.PUSH);
		button.setText("���...");
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = newFileDialog(getShell());
				dlg.setFilterExtensions(new String[]{"*.xls"});
				String path = dlg.open();
				if (path != null) {
					text.setText(path);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		GridLayoutFactory.swtDefaults().numColumns(3).applyTo(client);
		GridDataFactory.fillDefaults().grab(true, true).span(3, 1).applyTo(viewer.getControl());
		GridDataFactory.swtDefaults().applyTo(label);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(text);
		GridDataFactory.swtDefaults().applyTo(button);
		
		setControl(client);
		
		validate();
	}
	
	protected abstract FileDialog newFileDialog(Shell shell);

	
	protected void validate() {
		if (project == null || !(ARESProject.hasARESNature(project))) {
			setErrorMessage("��ѡ��һ������");
			setPageComplete(false);
		} else {
			setErrorMessage(null);
			setPageComplete(true);
		}
	}
	

}
