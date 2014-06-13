/**
 * Դ�������ƣ�ExportInterfaceWizardPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.service.stock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.biz.constants.IBizResType;
import com.hundsun.ares.studio.biz.core.BizUtil;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.core.model.extend.UserExtendedPropertyUtils;
import com.hundsun.ares.studio.core.util.ARESElementUtil;
import com.hundsun.ares.studio.ui.AresElementComparater;
import com.hundsun.ares.studio.ui.CommonElementContentProvider;
import com.hundsun.ares.studio.ui.CommonElementLabelProvider;

/**
 * @author sundl
 *
 */
public class ExportInterfaceWizardPage extends WizardPage {
	
	private static Logger logger = Logger.getLogger(ExportInterfaceWizardPage.class);
	
	public static final String KEY_BIZ_SCOPE = "biz_scope";
	public static final String KEY_PRUD_SCOPE = "product_scope";
	public static final String KEY_FUNC_STATUS = "func_status";

	/** ��ģ���ŷ�����Դ */
	Multimap<IARESModule, IARESResource> services = ArrayListMultimap.create();
	
	Multimap<IARESModule, IARESResource> objects = ArrayListMultimap.create();
	private IARESProject project;
	
	List<String> productScopes = new ArrayList<String>();
	List<String> bizScopes = new ArrayList<String>();
	
	List<String> selectedProductScopes = new ArrayList<String>();
	List<String> selectedBizScopes = new ArrayList<String>();
	List<String> selectedFuncStatus = new ArrayList<String>();
	
	private Multimap<String, String> productScope2BizScopeMapping = ArrayListMultimap.create();
	
	String templatePath;
	
	private IARESModule module;
	
	
	private CheckboxTableViewer producCheckboxTableViewer;
	private CheckboxTableViewer bizCheckboxTableViewer;
	
	protected ExportInterfaceWizardPage(IARESProject project, IARESModule module) {
		super("ExportInterfacePage");
		this.project = project;
		this.module = module;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(3).applyTo(composite);
		
		createModuleSelectionGroup(composite);
		createProductScopeGroup(composite);
		createBizScopeGroup(composite);
		createPublicGroup(composite);
		createTemplateSelectionGroup(composite);
		
		// ��ȡ��Դ�б���������Դ��ҵ��Χ��
		if (module != null) {
			LoadResources();
			refreshScopes();
		}
		
		setControl(composite);
		validate();
	}
	
	private void createModuleSelectionGroup(Composite composite) {
		Composite parent = new Composite(composite, SWT.NONE);
		GridDataFactory.fillDefaults().span(3, 1).grab(true, false).applyTo(parent);
		GridLayoutFactory.fillDefaults().numColumns(3).applyTo(parent);
		Label label = new Label(parent, SWT.NONE);
		label.setText("ģ�飺");
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(label);
		
		final Text text = new Text(parent, SWT.READ_ONLY | SWT.BORDER);
		if (module != null) {
			text.setText(ARESElementUtil.getModuleFullCName(module, "_"));
		}
		GridDataFactory.fillDefaults().grab(true, false).applyTo(text);
		
		Button button = new Button(parent, SWT.PUSH);
		button.setText("ѡ��...");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(), 
						new CommonElementLabelProvider(null), 
						new CommonElementContentProvider()) {
				    protected TreeViewer createTreeViewer(Composite parent) {
				    	TreeViewer viewer = super.createTreeViewer(parent);
				    	viewer.expandAll(); 
				    	return viewer;
				    }
				};
				dialog.addFilter(new ViewerFilter() {
					@Override
					public boolean select(Viewer viewer, Object parentElement, Object element) {
						if (element instanceof IARESModule)
							return true;
						return false;
					}
				});
				dialog.setComparator(new AresElementComparater());
				dialog.setInput(BizUtil.getBizRoot(project)); 
				dialog.setTitle("ѡ��ģ��");
				dialog.setAllowMultiple(false);
				if (dialog.open() == Dialog.OK) {
					Object[] module = dialog.getResult();
					if (module.length > 0) {
						ExportInterfaceWizardPage.this.module = (IARESModule) module[0];
						text.setText(ARESElementUtil.getModuleFullCName(ExportInterfaceWizardPage.this.module, "_"));
					}
					LoadResources();
					refreshScopes();
					validate();
				}
			}
		});
	}
	
	private void createTemplateSelectionGroup(Composite composite) {
		Composite client = new Composite(composite, SWT.NONE);
		GridDataFactory.fillDefaults().indent(0, 0).span(3, 1).grab(true, false).applyTo(client);
		GridLayoutFactory.fillDefaults().numColumns(3).applyTo(client);
		
		Label label = new Label(client, SWT.NONE);
		GridDataFactory.fillDefaults().applyTo(label);
		label.setText("ģ���ļ�:");
		final Text txtTemplate = new Text(client, SWT.READ_ONLY | SWT.BORDER);
		txtTemplate.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				templatePath = txtTemplate.getText();
			}
		});
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtTemplate);
		
		Button btTemplate = new Button(client, SWT.PUSH);
		btTemplate.setText("���...");
		GridDataFactory.fillDefaults().applyTo(btTemplate);
		btTemplate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
				dialog.setFilterExtensions(new String[] {"*.xls"});
				String path = dialog.open();
				if (path != null) {
					txtTemplate.setText(path);
				}
			}
		});
	}

	private void createProductScopeGroup(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).hint(-1, 300).applyTo(group);
		group.setText("��Ʒ��Χ:");
		group.setLayout(new FillLayout());
		
		producCheckboxTableViewer = CheckboxTableViewer.newCheckList(group, SWT.V_SCROLL | SWT.BORDER);
		producCheckboxTableViewer.getTable().setLinesVisible(false);
		producCheckboxTableViewer.getTable().setHeaderVisible(false);
		producCheckboxTableViewer.setContentProvider(new ArrayContentProvider());
		producCheckboxTableViewer.setLabelProvider(new LabelProvider());
		producCheckboxTableViewer.setInput(productScopes);
		producCheckboxTableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				selectedProductScopes.clear();
				for (Object obj : producCheckboxTableViewer.getCheckedElements()) {
					selectedProductScopes.add((String) obj);
				}
				// �����ռ����Ĳ�Ʒ��Χ��ҵ��Χ֮��Ķ�Ӧ��ϵ���ڹ�ѡ��Ʒ��Χ��ʱ���Զ���ѡ��ҵ��Χ
				String element = (String) event.getElement();
				if (element != null && event.getChecked()) {
					Collection<String> bizScopes = ExportInterfaceWizardPage.this.productScope2BizScopeMapping.get(element);
					if (!bizScopes.isEmpty()) {
						Iterator<String> it = bizScopes.iterator();
						while (it.hasNext()) {
							String biz = it.next();
							bizCheckboxTableViewer.setChecked(biz, true);
						}
						bizCheckboxTableViewer.setSelection(new StructuredSelection(bizScopes.toArray()), true);
						//������Ĵ�������ѡ��״̬��������¼�, ������Ҫ�ֶ�ˢ��ѡ���ҵ��Χ
						selectedBizScopes.clear();
						for (Object obj : bizCheckboxTableViewer.getCheckedElements()) {
							selectedBizScopes.add(obj.toString());
						}
					}
				}
			}
		});
	}
	
	private void createBizScopeGroup(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).hint(-1, 300).applyTo(group);
		group.setText("ҵ��Χ:");
		group.setLayout(new FillLayout());
		
		bizCheckboxTableViewer = CheckboxTableViewer.newCheckList(group, SWT.V_SCROLL | SWT.BORDER);
		bizCheckboxTableViewer.getTable().setLinesVisible(false);
		bizCheckboxTableViewer.getTable().setHeaderVisible(false);
		bizCheckboxTableViewer.setContentProvider(new ArrayContentProvider());
		bizCheckboxTableViewer.setLabelProvider(new LabelProvider());
		bizCheckboxTableViewer.setInput(bizScopes);
		bizCheckboxTableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				selectedBizScopes.clear();
				for (Object obj : bizCheckboxTableViewer.getCheckedElements()) {
					selectedBizScopes.add((String) obj);
				}
			}
		});
	}
	
	public void createPublicGroup(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group);
		group.setText("״̬:");
		GridLayoutFactory.fillDefaults().applyTo(group);
		
		final Button btPublic = new Button(group, SWT.CHECK);
		btPublic.setText("����");

		
		final Button btNonPublic=  new Button(group, SWT.CHECK);
		btNonPublic.setText("������");
		
		btPublic.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedFuncStatus.clear();
				if (btPublic.getSelection())
					selectedFuncStatus.add("����");
				if (btNonPublic.getSelection()) {
					selectedFuncStatus.add("������");
				}
			}
			
		});
		
		btNonPublic.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedFuncStatus.clear();
				if (btPublic.getSelection())
					selectedFuncStatus.add("����");
				if (btNonPublic.getSelection()) {
					selectedFuncStatus.add("������");
				}
			}
			
		});
		btPublic.setSelection(true);
		selectedFuncStatus.add("����");
	}
	
	private void LoadResources() {
		if (module == null)
			return;
		services.clear();
		objects.clear();
		productScope2BizScopeMapping.clear();
		productScopes.clear();
		bizScopes.clear();
		try {
			IARESResource[] resources = module.getARESResources(true);
			for (IARESResource res : resources) {
				if (res.getType().equals(IBizResType.Service)) {
					services.put(res.getModule(), res);
					ExtensibleModel info = res.getInfo(ExtensibleModel.class);
					if (info != null) {
						// 1. ��¼����Щҵ��Χ����Ʒ��Χ
						// 2. ��¼��Ʒ��Χ��ҵ��Χ֮���ӳ���ϵ�������ڹ���Ʒ��Χ��ʱ�򣬿����Զ����϶�Ӧ��ҵ��Χ
						List<String> prodScopes = collectProductScope(info);
						String bizScope = collectBizScope(info);
						for (String product : prodScopes) {
							if (!this.productScopes.contains(product)) {
								this.productScopes.add(product);
							}
							productScope2BizScopeMapping.put(product, bizScope);
						}
						if (!this.bizScopes.contains(bizScope)) {
							this.bizScopes.add(bizScope);
						}
					} else {
						logger.error("�����ӿ��ĵ���ʱ���ȡ��Դģ��ʧ�ܣ���Դ��" + res.getPath());
					}
				}
				else if (res.getType().equals(IBizResType.Object)) {
					objects.put(res.getModule(), res);
				}
			}
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * �ռ����еĲ�Ʒ��Χ
	 */
	private List<String> collectProductScope(ExtensibleModel model) {
		List<String> scopeList = new ArrayList<String>();
		String product = UserExtendedPropertyUtils.getUserExtendedProperty(model, KEY_PRUD_SCOPE);
		if (product != null) {
			String[] scopes = product.split(",");
			for (String scope : scopes) {
				if (StringUtils.isNotEmpty(scope)) {
					scopeList.add(scope);
				}
			}
		}
		return scopeList;
	}
	
	private String collectBizScope(ExtensibleModel model) {
		return UserExtendedPropertyUtils.getUserExtendedProperty(model, KEY_BIZ_SCOPE);
	}
	
	private void refreshScopes() {
		producCheckboxTableViewer.setInput(productScopes.toArray());
		bizCheckboxTableViewer.setInput(bizScopes.toArray());
	}
	
	public void filter() {
		//Multimap<IARESModule, IARESResource> services = ArrayListMultimap.create();
		for (IARESModule module : this.services.keySet()) {
			Iterator<IARESResource> it = services.get(module).iterator();
			for (; it.hasNext();) {
				IARESResource res = it.next();
				try {
					ExtensibleModel model = res.getInfo(ExtensibleModel.class);
					if (model != null) {
						String bizscope = UserExtendedPropertyUtils.getUserExtendedProperty(model, KEY_BIZ_SCOPE);
						String prodScope = UserExtendedPropertyUtils.getUserExtendedProperty(model, KEY_PRUD_SCOPE);
						String[] prodScopes = new String[0];
						if (prodScope != null) {
							prodScopes = prodScope.split(",");
						} 
						String funcStatus = UserExtendedPropertyUtils.getUserExtendedProperty(model, KEY_FUNC_STATUS);
						if (selectedBizScopes.contains(bizscope) && CollectionUtils.containsAny(selectedProductScopes, Arrays.asList(prodScopes)) && 
								selectedFuncStatus.contains(funcStatus)) {
							// ���Ϲ�������
						} else {
							it.remove();
						}
					} else {
						it.remove();
					}
				} catch (ARESModelException e) {
					logger.error(e);
				}
			}
		}
	}

	private void validate() {
		if (module == null) {
			updatePageComplate("��ѡ��Ҫ������ģ��.");
			return;
		}
		
		if (services.isEmpty()) {
			updatePageComplate("û�п��Ե�������Դ����ѡ������ģ��.");
			return;
		}
		
		updatePageComplate(null);
	}
	
	private void updatePageComplate(String msg) {
		setErrorMessage(msg);
		setPageComplete(msg == null);
	}
	
}
