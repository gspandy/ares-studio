/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.wizard;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESProblem;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.registry.ResValidaterRegistry;
import com.hundsun.ares.studio.ui.CommonElementSelectionPage;

/**
 * @author sundl
 */
public class AresLibExportWizardPage extends CommonElementSelectionPage {

	private static final Logger log = Logger.getLogger(AresLibExportWizardPage.class);
	
	private static final String[][] types = new String[][] {
		{String.valueOf(IARESElement.ARES_PROJECT), "��Ŀ"}
	};
	
	private static final int[] FILTER_TYPES= new int[] {IARESElement.ARES_PROJECT};

	private Label lb_Path;
	private Text tx_Path;
	private Button bt_Browse;
	
	protected Text tx_Publisher;

	private String path;
	private String publiser;
	
	private boolean validateOK = false;
	private boolean validating = false;
	
	private ValidateProjectJob job = new ValidateProjectJob();
	
	class ValidateProjectJob extends Job {

		IARESProject project;
		
		public ValidateProjectJob() {
			super("�����Ŀ");
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			validateOK = false;
			validating = true;
			
			if (project == null)
				return Status.CANCEL_STATUS;
				
			ResValidaterRegistry reg = ResValidaterRegistry.getInstance();
			Collection<IARESProblem> problems = reg.validateProject(project, monitor);
			if (problems.isEmpty())
				validateOK = true;
			
			validating = false;
			
			getControl().getDisplay().syncExec(new Runnable() {
				
				public void run() {
					updateComplete();
				}
			});
			
			return Status.OK_STATUS;
		}
		
	}
	
	public AresLibExportWizardPage(String pageName, IWorkbench workbench, IARESElement selection) {
		super(pageName, workbench, selection);
		setMessage("ѡ��Ҫ��������Ŀ��·��");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);

		GridLayoutFactory.fillDefaults().numColumns(3).applyTo(composite);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
	
		Composite treeContainer = new Composite(composite, SWT.NONE);
		GridLayoutFactory.fillDefaults().applyTo(treeContainer);
		
		super.createControl(treeContainer);
		viewer.getControl().setEnabled(false);
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalIndent = 0;
		gd.horizontalIndent = 0;
		gd.horizontalSpan = 3;
		treeContainer.setLayoutData(gd);
		
		lb_Path = new Label(composite, SWT.NONE);
		lb_Path.setText("·��:");
		tx_Path = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		tx_Path.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tx_Path.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				path = tx_Path.getText();
				updateComplete();
			} 
		});
		
		bt_Browse = new Button(composite, SWT.PUSH);
		bt_Browse.setText("���");
		bt_Browse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell());
				dialog.setFilterNames(new String[] {"Ares������Դ��"});
				dialog.setOverwrite(true);
				dialog.setFilterExtensions(getFilters());
				dialog.setText("����������Դ��");
				dialog.setFilterPath(getFilterPath());
				
				String result = dialog.open();
				if (result != null) {
					if (!result.endsWith(getPacketPostfix())) {
						result = result.concat(getPacketPostfix());
					}
					File file = new File(result);
					if (file.exists()) {
						boolean overwrite = MessageDialog.openConfirm(getShell(), "����", "�ļ��Ѵ����Ƿ񸲸�?");
						if (overwrite) {
							tx_Path.setText(result);
						} else {
							tx_Path.setText("");
						}
						return;
					}
					tx_Path.setText(result);
				}
			}
			
		});
		
		Label lb_Publisher = new Label(composite, SWT.NONE);
		lb_Publisher.setText("������:");
		
		tx_Publisher = new Text(composite, SWT.BORDER);
		tx_Publisher.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent e) {
				publiser = tx_Publisher.getText();
			}
		});
		
		GridDataFactory.fillDefaults().applyTo(tx_Publisher);
		
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (selection instanceof IARESProject) {
					try {
						getContainer().run(false, false, new IRunnableWithProgress() {
							public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
								validateProject(monitor);
							}
						});
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		if (selection != null) {
			viewer.setSelection(new StructuredSelection(selection), true);
		}
		
//		try {
//			getContainer().run(false, false, new IRunnableWithProgress() {
//				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
//					validateProject(monitor);
//				}
//			});
//		} catch (InvocationTargetException e1) {
//			e1.printStackTrace();
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}		
		
		setControl(composite);
		updateComplete();
	}
	
	/**��ȡ������ʼ·����*/
	protected String getFilterPath() {
		return "";
	}

	public String getPublisher() {
		return publiser;
	}
	
	private void validateProject(IProgressMonitor monitor) {
//      2011-11-2 sundl ��ʱȥ��������
//		validateOK = true;
		validateOK = false;
		ResValidaterRegistry reg = ResValidaterRegistry.getInstance();
		Collection<IARESProblem> problems = reg.validateProject((IARESProject) selection, monitor);
		if (!hasError(problems)) 
			validateOK = true;
		else {
			if (log.isDebugEnabled()) {
				log.debug("Errors found while exporting ref-lib for project " + ((IARESProject) selection).getElementName());
				for (IARESProblem problem : problems) {
					if (problem.isError())
						log.debug(problem.getMessage());
				}
			}
		}
	}
	
	private boolean hasError(Collection<IARESProblem> problems) {
		if (problems == null || problems.isEmpty())
			return false;
		
		for (IARESProblem problem : problems) {
			if (problem.isError())
				return true;
		}
		
		return false;
	}
	
	public boolean validate() {		
		if (!super.validate())
			return false;
		
		if (!validateOK) {
			setErrorMessage("��Ŀ�л��д���!");
			return false;
		}
		
		if (StringUtils.isEmpty(path)) {
			setErrorMessage("·������Ϊ��!");
			return false;
		}
		
		return true;
	}
	
	/**
	 * ����Ҫ����ѡ�����Դ����; ��λ���飬��һ�����͵ڶ������֡�<br>
	 * ������ص���null������Ϊ�κ�ѡ�����Ч/�Ϸ�
	 */
	protected String[][] getSelctingElementTypes() {
		return types;
	}
	
	protected int[] getDisplayedElementTypes() {
		return FILTER_TYPES;
	}
	
	public String getPath() {
		return path;
	}
	
	/**
	 * ��ȡ�ļ�������
	 * @return
	 */
	protected String[] getFilters() {
		return new String[] {"*.ares"};
	}
	
	/**
	 * ��ȡ�����������ƺ�׺
	 * @return
	 */
	protected String getPacketPostfix() {
		return ".ares";
	}

}
