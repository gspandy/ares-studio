/**
 * Դ�������ƣ�GenSQLSelectPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.biz.ui.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.util.ARESElementUtil;
import com.hundsun.ares.studio.ui.ARESElementSorter;
import com.hundsun.ares.studio.ui.CommonElementContentProvider;
import com.hundsun.ares.studio.ui.CommonElementLabelProvider;
import com.hundsun.ares.studio.ui.control.CheckboxTreeViewerEx;

/**
 * @author wangxh
 *
 */
public class ExportWizardPage extends WizardPage {

	/**ѡ���б�*/
	CheckboxTreeViewerEx viewer;
	/**�Ҽ�ѡ�е�Ԫ��*/
	ISelection select;
	/**���ɵ�·��*/
	String path;
	/**·��*/
	protected Text txtPath;
	
	private String moduleRootName;
	
	CommonElementContentProvider cp;
	
	List<IARESResource> result = new ArrayList<IARESResource>();
	/**
	 * @param pageName
	 */
	public ExportWizardPage(String pageName,ISelection select ,String moduleRootName) {
		super(pageName);
		this.select = select;
		this.moduleRootName = moduleRootName;
		setTitle(pageName);
		setDescription("��ѡ��Ҫ���ɵĶ����·��");
	}

	private IARESElement[] getCheckedElements(Object[] selected) {
		Set<IARESElement> checkedElements = new HashSet<IARESElement>();
		IARESProject project = null;
		try {
			
			{
				Stack<IARESElement> elements = new Stack<IARESElement>();
				elements.addAll(Arrays.asList(ARESElementUtil.toARESElement(selected)));
				while (!elements.isEmpty()) {
					IARESElement obj = elements.pop();
					if(obj instanceof IARESProject){
						project = (IARESProject) obj;
					}else if(obj instanceof IARESModuleRoot){
						if(((IARESModuleRoot) obj).getElementName().equals(moduleRootName)){
//							Collections.addAll(checkedElements, ((IARESModuleRoot) obj).getChildren());
							elements.addAll(Arrays.asList(((IARESModuleRoot) obj).getChildren()));
						}else{
							project = ((IARESModuleRoot) obj).getARESProject();
						}
					}else if(obj instanceof IARESModule){
						if(((IARESModule) obj).getRoot().getElementName().equals(moduleRootName)){
							checkedElements.add((IARESModule) obj);
							elements.addAll(Arrays.asList(((IARESModule) obj).getChildren()));
						}else{
							project = ((IARESModule) obj).getARESProject();
						}
					}else if (obj instanceof IARESResource) {
						if(((IARESResource) obj).getRoot().getElementName().equals(moduleRootName)){
							checkedElements.add((IARESResource) obj);
						}else{
							project = ((IARESModule) obj).getARESProject();
						}
					}
				}
			}
			
			if(project != null && checkedElements.isEmpty()){
				for(IARESModuleRoot root : project.getModuleRoots()){
					if(root.getElementName().equals(moduleRootName)){
						checkedElements.add(root);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return checkedElements.toArray(new IARESElement[checkedElements.size()]);
	}
	
	private IARESModuleRoot getModuleRoot(IARESElement element) {
		if (element instanceof IARESModuleRoot) {
			return (IARESModuleRoot) element;
		} else if (element instanceof IARESModule) {
			return ((IARESModule) element).getRoot();
		} else if (element instanceof IARESResource) {
			return ((IARESResource) element).getRoot();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		Composite client = new Composite(parent,SWT.None);
		Group viewerGroup = createResouceTreeviewer(client);
		Group buttonGroup = createButtons(client);
		
		// ��ѡ���Ԫ��ת��Ϊ����ѡ�е�Ԫ��
		IARESElement[] elements = getCheckedElements(((IStructuredSelection)select).toArray());
		
		if (elements.length != 0) {
			IARESModuleRoot root = getModuleRoot(elements[0]);
			Assert.isNotNull(root);
			
			viewer.setInput(root);
			
			viewer.setCheckedElementsWithNotify(elements);
		}

		viewer.setFilters(new ViewerFilter[]{new DBFilter()});
		
		final Group pathGroup = new Group(client, SWT.None);
		pathGroup.setText("ѡ������·��");
		pathGroup.setLayout(new GridLayout(2, false));
		
		txtPath = new Text(pathGroup, SWT.BORDER);
		// ����ѡ���
		
		Button dirButton = new Button(pathGroup, SWT.NONE);
		dirButton.setText("���...");
		addDirButtonListener(dirButton,pathGroup);
		
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(client);
		GridDataFactory.fillDefaults().hint(200, 300).grab(true, true).applyTo(viewerGroup);
		GridDataFactory.fillDefaults().grab(false, true).hint(80, -1).applyTo(buttonGroup);
		GridDataFactory.fillDefaults().span(2, -1).grab(true, false).applyTo(pathGroup);
		
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtPath);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(dirButton);
		
		setControl(client);
		UpdatePageComplete();
		
		txtPath.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				path = txtPath.getText();
				UpdatePageComplete();
			}
		});
		
	}

	/**
	 * @param dirButton
	 */
	protected void addDirButtonListener(Button dirButton,final Group pathGroup) {
		dirButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
		        FileDialog dialog = new FileDialog(pathGroup.getShell(), SWT.OPEN);
		        String filterExtension = "*.xls";
		        dialog.setFilterExtensions(new String[] { filterExtension });
		        dialog.setFilterNames(new String[] {"Excel�ļ�(*.xls)"});
		    	String filePath = dialog.open();
		    	txtPath.setText(filePath);
			
		        // �����ļ�·���������ͺ�׺�����
		        if(filterExtension != "All") {
			        String fileSuffix = filterExtension.substring(1);
		        	if(!filePath.endsWith(fileSuffix)) {
		        		filePath = filePath + fileSuffix;
		        		txtPath.setText(filePath);
		        	}
		        }
		    }

		});
	}

	private void setAllChecked(final boolean state) {
		BusyIndicator.showWhile(viewer.getTree().getShell().getDisplay(), 
				new Runnable(){

					@Override
					public void run() {
						for (Object obj : cp.getElements(viewer.getInput())) {
							viewer.checkChange(new CheckStateChangedEvent(viewer, obj, state));
						}
					}
		});
	}
	
	/**
	 * @param client
	 * @return
	 */
	private Group createButtons(Composite client) {
		Group buttonGroup = new Group(client, SWT.None);
		Button btnSelectAll = new Button(buttonGroup, SWT.PUSH);
		btnSelectAll.setText("ȫѡ");
		btnSelectAll.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				setAllChecked(true);
				UpdatePageComplete();
			}});
		
		Button btnDeselectAll = new Button(buttonGroup, SWT.PUSH);
		btnDeselectAll.setText("ȡ��ȫѡ");
		btnDeselectAll.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				setAllChecked(false);
				UpdatePageComplete();
			}});
		
		buttonGroup.setLayout(new GridLayout(1, true));
		
		GridDataFactory.fillDefaults().grab(true, false).applyTo(btnSelectAll);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(btnDeselectAll);
		return buttonGroup;
	}


	/**
	 * @param client
	 * @return
	 */
	private Group createResouceTreeviewer(Composite client) {
		Group viewerGroup = new Group(client, SWT.None);
		viewerGroup.setText("ѡ��ģ��");
		viewerGroup.setLayout(new GridLayout(4,false));
		viewerGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		viewer = new CheckboxTreeViewerEx(viewerGroup, SWT.CHECK |SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		cp= new CommonElementContentProvider();
		viewer.setContentProvider(cp);
		viewer.setLabelProvider(new CommonElementLabelProvider(cp));
		viewer.setComparator(new ARESElementSorter());
		viewer.setUseHashlookup(true);
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 4;
		gd.widthHint = 10;
		viewer.getTree().setLayoutData(gd);

		viewer.addCheckStateListener(new ICheckStateListener(){

			public void checkStateChanged(
					final CheckStateChangedEvent event) {
				UpdatePageComplete();  //����ҳ���Ƿ����
			}});
		return viewerGroup;
	}

	private void UpdatePageComplete() {
		result = getSelectedResouces();
		
		if(result.size() > 0 && StringUtils.isNotBlank(path)){
			setPageComplete(true);
		}else{
			setPageComplete(false);
		}
	}
	
	/**
	 * ��ȡ��ѡ�е���Դ
	 * @return
	 */
	private List<IARESResource> getSelectedResouces(){
		List<IARESResource> reses = new ArrayList<IARESResource>();
		Object obj[] = viewer.getCheckedElements();
		for (Object object : obj) {
			if (object instanceof IARESResource) {
				reses.add((IARESResource) object);
			}
		}
		return reses;
	}
	
	
	/**
	 * ��ȡ����ѡ�е���Դ
	 * @return 
	 */
	public List<IARESResource> getResult() {
		return result;
	}

	/**
	 * ��ȡ·��
	 * @return the path
	 */
	public String getPath() {
		return StringUtils.defaultString(path);
	}

	class DBFilter extends ViewerFilter {

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			 if (element instanceof IARESElement && StringUtils.equals("module.xml", ((IARESElement) element).getElementName())) {
				return false;
			}
			return true;
		}
	}
}
