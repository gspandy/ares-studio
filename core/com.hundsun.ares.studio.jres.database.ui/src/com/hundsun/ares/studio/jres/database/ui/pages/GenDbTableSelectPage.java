/**
 * Դ�������ƣ�GenSQLSelectPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.hundsun.ares.studio.jres.database.constant.IDBConstant;
import com.hundsun.ares.studio.jres.database.oracle.constant.IOracleResType;
import com.hundsun.ares.studio.jres.database.utils.GenDbExportServiceUtils;
import com.hundsun.ares.studio.ui.ARESElementSorter;
import com.hundsun.ares.studio.ui.ARESResourceCategory;
import com.hundsun.ares.studio.ui.CommonElementContentProvider;
import com.hundsun.ares.studio.ui.CommonElementLabelProvider;
import com.hundsun.ares.studio.ui.control.CheckboxTreeViewerEx;

/**
 * @author wangxh
 *
 */
public class GenDbTableSelectPage extends WizardPage {

	/**ѡ���б�*/
	CheckboxTreeViewerEx viewer;
	/**�Ҽ�ѡ�е�Ԫ��*/
	ISelection select;
	/**���ɵ�·��*/
	String path;
	/**·��*/
	Text txtPath;
	/**�Ƿ����ĵ�*/
	private boolean splitDoc;
	private boolean defValueCol = true;
	CommonElementContentProvider cp;
	
	List<IARESResource> result = new ArrayList<IARESResource>();
	/**
	 * @param pageName
	 */
	public GenDbTableSelectPage(String pageName,ISelection select) {
		super(pageName);
		this.select = select;
		setTitle(pageName);
		setDescription("��ѡ��Ҫ���ɵĶ����·��");
	}

	private IARESElement[] getCheckedElements(Object[] selected) {
		Set<IARESElement> checkedElements = new HashSet<IARESElement>();
		IARESProject project = null;
		try {
			for(Object obj : ARESElementUtil.toARESElement(selected)){
				if(obj instanceof IARESProject){
					project = (IARESProject) obj;
				}else if(obj instanceof IARESModuleRoot){
					if(((IARESModuleRoot) obj).getType().equals(IDBConstant.ROOT_TYPE)){
						Collections.addAll(checkedElements, ((IARESModuleRoot) obj).getChildren());
					}else{
						project = ((IARESModuleRoot) obj).getARESProject();
					}
				}else if(obj instanceof IARESModule){
					if(((IARESModule) obj).getRoot().getType().equals(IDBConstant.ROOT_TYPE)){
						checkedElements.add((IARESModule)obj);
						checkedElements.addAll(Arrays.asList(((IARESModule)obj).getSubModules()));
					}else{
						project = ((IARESModule)obj).getARESProject();
					}
				}else if(obj instanceof IARESResource){
					if(((IARESResource) obj).getRoot().getType().equals(IDBConstant.ROOT_TYPE)){
						checkedElements.add((IARESResource) obj);
					}else{
						project = ((IARESResource) obj).getARESProject();
					}
				}
				if(project != null){
					for(IARESModuleRoot root : project.getModuleRoots()){
						if(root.getType().equals(IDBConstant.ROOT_TYPE)){
							checkedElements.add(root);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return checkedElements.toArray(new IARESElement[0]);
	}
	
	private IARESModuleRoot getModuleRoot(Object element) {
		if (element instanceof IARESModuleRoot) {
			return (IARESModuleRoot) element;
		} else if (element instanceof IARESModule) {
			return ((IARESModule) element).getRoot();
		} else if (element instanceof IARESResource) {
			return ((IARESResource) element).getRoot();
		}else if (element instanceof ARESResourceCategory) {
			return ((ARESResourceCategory) element).getModule().getRoot();
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
		Object[] elements = getCheckedElements(((IStructuredSelection)select).toArray());
		
		IARESModuleRoot root = getModuleRoot(elements[0]);
		Assert.isNotNull(root);
		
		viewer.setInput(root);
		
		viewer.setCheckedElementsWithNotify(elements);
		viewer.setFilters(new ViewerFilter[]{new DBFilter()});
		
		final Group pathGroup = new Group(client, SWT.None);
		pathGroup.setText("ѡ������·��");
		pathGroup.setLayout(new GridLayout(2, false));
		
		txtPath = new Text(pathGroup, SWT.BORDER);
		// ����ѡ���
		
		Button dirButton = new Button(pathGroup, SWT.NONE);
		dirButton.setText("���...");
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
		
		
		Group docGroup = new Group(client, SWT.None);
		docGroup.setText("�ĵ�����");
		docGroup.setLayout(new GridLayout(2, false));
		
		final Button defaultValue = new Button(docGroup, SWT.CHECK);
		defaultValue.setText("����\"Ĭ��ֵ\"");//Ĭ�ϵ���Ĭ��ֵ
		defaultValue.setSelection(true);
		
		final Button splitDocBtn=new Button(docGroup, SWT.CHECK);
		splitDocBtn.setText("����ϵͳ����");
		
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(client);
		GridDataFactory.fillDefaults().hint(200, 300).grab(true, true).applyTo(viewerGroup);
		GridDataFactory.fillDefaults().grab(false, true).hint(80, -1).applyTo(buttonGroup);
		GridDataFactory.fillDefaults().span(2, -1).grab(true, false).applyTo(pathGroup);
		GridDataFactory.fillDefaults().span(2, -1).grab(true, false).applyTo(docGroup);
		
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
		
		defaultValue.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				defValueCol = defaultValue.getSelection();
				
			}
			
		});
		
		splitDocBtn.addSelectionListener(new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				splitDoc=splitDocBtn.getSelection();
			}
		});
		
		String file = GenDbExportServiceUtils.getParamter();
		if (StringUtils.isNotBlank(file)) {
			txtPath.setText(file);
		}
		
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
		viewerGroup.setText("ѡ����Դ");
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
		List<IARESResource> modules = new ArrayList<IARESResource>();
		Object obj[] = viewer.getCheckedElements();
		for (Object object : obj) {
			if (object instanceof IARESResource) {
				modules.add((IARESResource) object);
			}
		}
		return modules;
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
	
	public boolean needDevfCol(){
		return defValueCol;
	}
	
	/**
	 * �Ƿ����ĵ�
	 */
	public boolean needSplitDoc(){
		return splitDoc;
	}

	class DBFilter extends ViewerFilter {

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof IARESElement && StringUtils.equals(((IARESElement) element).getElementName(),"module.xml")) {
				return false;
			}else if ((element instanceof ARESResourceCategory) && ((ARESResourceCategory)element).getResources().length == 0) {
				return false;
			}else if (element instanceof IARESElement && StringUtils.equals(((IARESElement) element).getElementName(), IOracleResType.Space + "." + IOracleResType.Space)) {
				return false;
			}else if (element instanceof IARESElement && StringUtils.equals(((IARESElement) element).getElementName(), IOracleResType.User + "." +IOracleResType.User)) {
				return false;
			}
			return true;
		}
	}
}
