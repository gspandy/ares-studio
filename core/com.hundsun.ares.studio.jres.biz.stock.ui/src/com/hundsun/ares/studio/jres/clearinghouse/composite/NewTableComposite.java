/**
 * Դ�������ƣ�NewTableComposite.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse.composite;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.transaction.impl.TransactionalCommandStackImpl;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.clearinghouse.ui.page.RevisionHistoryColumnsViewerBlock;
import com.hundsun.ares.studio.jres.clearinghouse.ui.page.RevisionHistoryIndexViewerBlock;
import com.hundsun.ares.studio.jres.clearinghouse.ui.page.RevisionHistoryKeyViewerBlock;
import com.hundsun.ares.studio.jres.model.chouse.AddTableModification;
import com.hundsun.ares.studio.jres.model.chouse.ChouseFactory;
import com.hundsun.ares.studio.jres.model.chouse.ChousePackage;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableIndex;
import com.hundsun.ares.studio.jres.model.database.TableKey;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;

/**
 * @author wangxh
 *
 */
public class NewTableComposite extends ModifyActionComposite{

	//�½�ԭ��
	Button btnBtable;
	//�½���ʷ��
	Button btnHtable;
	//ѡ��ԭ��
	Boolean btSelect;
	//ѡ����ʷ��
	Boolean htSelect;
	
	/**
	 * �½���
	 * @param parent
	 * @param resource
	 * @param modification
	 */
	public NewTableComposite(Composite parent, TableResourceData tableData, IARESResource resource,	Modification modification) {
		super(parent, tableData, resource, modification);
	}
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.ui.editors.history.component.ModifyActionComposite#initAction(com.hundsun.ares.studio.jres.model.database.Modification)
	 */
	@Override
	protected void initAction(Modification modification) {
		if(modification != null && modification instanceof AddTableModification){
			action = (AddTableModification) modification;
		}
		else{
			action = ChouseFactory.eINSTANCE.createAddTableModification();
			// 2012-05-15 sundl �½�����ʷ�޶���Ϣ�б���һ���ֶκ������б�
			// �˴�֤���ǵ�һ��ѡ���½�����ʱ��TabelData�и����ֶ�/����������Ϣ
			if (tableData != null) {
				EList<TableColumn> columns = tableData.getColumns();
				Collection<TableColumn> copyedColumns = EcoreUtil.copyAll(columns);
				((AddTableModification) action).getColumns().addAll(copyedColumns);
				
				EList<TableIndex> indexes = tableData.getIndexes();
				Collection<TableIndex> copyedIndexes = EcoreUtil.copyAll(indexes);
				((AddTableModification) action).getIndexes().addAll(copyedIndexes);
				
				EList<TableKey> keys = tableData.getKeys();
				Collection<TableKey> copyedKeys = EcoreUtil.copyAll(keys);
				((AddTableModification) action).getKeys().addAll(copyedKeys);
			}
		}
		btSelect = ((AddTableModification)action).isNewSelfTable();
		htSelect = ((AddTableModification)action).isNewHistoryTable();
	}
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.ui.editors.history.component.ModifyActionComposite#creatDetailComposite(org.eclipse.swt.widgets.Composite, com.hundsun.ares.studio.core.IARESResource)
	 */
	@Override
	protected void creatDetailComposite(Composite parent, IARESResource resource) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		
		GridDataFactory.fillDefaults().grab(true, true).applyTo(composite);
		
		btnBtable = new Button(composite,SWT.CHECK);
		btnBtable.setText("�½�ԭ��");
		
		btnHtable = new Button(composite,SWT.CHECK);
		btnHtable.setText("�½���ʷ��");
		
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(btnBtable);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(btnHtable);
		
		btnBtable.setSelection(((AddTableModification)action).isNewSelfTable());
		btnHtable.setSelection(((AddTableModification)action).isNewHistoryTable());
		
		btnBtable.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				btSelect = btnBtable.getSelection();
			}
		});
		
		btnHtable.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				htSelect = btnHtable.getSelection();
			}
		});
		
		Group columnGroup = new Group(composite, SWT.NONE);
		columnGroup.setText("�ֶ�");
		GridLayoutFactory.fillDefaults().applyTo(columnGroup);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(columnGroup);
		createColumnBlock(columnGroup);
		
		Group indexGroup = new Group(composite, SWT.NONE);
		indexGroup.setText("����");
		GridLayoutFactory.fillDefaults().applyTo(indexGroup);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(indexGroup);
		createIndexBlock(indexGroup);
		
		Group keyGroup = new Group(composite, SWT.NONE);
		keyGroup.setText("��Լ��");
		GridLayoutFactory.fillDefaults().applyTo(keyGroup);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(keyGroup);
		createKeyBlock(keyGroup);
	}

	private void createColumnBlock(Composite parent) {
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		//adapterFactory.addAdapterFactory(new ChouseItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		TransactionalCommandStackImpl commandStack = new TransactionalCommandStackImpl();
		EditingDomain editingDomain = new TransactionalEditingDomainImpl(adapterFactory, commandStack);

		RevisionHistoryColumnsViewerBlock block = new RevisionHistoryColumnsViewerBlock(editingDomain, resource);
		block.createControl(parent, new FormToolkit(getDisplay()));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(block.getControl());
		editingDomain.getCommandStack().addCommandStackListener(block);
		block.setInput(action);
	}
	
	private void createIndexBlock(Composite parent) {
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		//adapterFactory.addAdapterFactory(new ChouseItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		TransactionalCommandStackImpl commandStack = new TransactionalCommandStackImpl();
		EditingDomain editingDomain = new TransactionalEditingDomainImpl(adapterFactory, commandStack);

		RevisionHistoryIndexViewerBlock block = new RevisionHistoryIndexViewerBlock(editingDomain, resource);
		block.createControl(parent, new FormToolkit(getDisplay()));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(block.getControl());
		editingDomain.getCommandStack().addCommandStackListener(block);
		block.setInput(action);
	}
	
	private void createKeyBlock(Composite parent) {
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		//adapterFactory.addAdapterFactory(new ChouseItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		TransactionalCommandStackImpl commandStack = new TransactionalCommandStackImpl();
		EditingDomain editingDomain = new TransactionalEditingDomainImpl(adapterFactory, commandStack);

		RevisionHistoryKeyViewerBlock block = new RevisionHistoryKeyViewerBlock(editingDomain, resource ,action);
		block.createControl(parent, new FormToolkit(getDisplay()));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(block.getControl());
		editingDomain.getCommandStack().addCommandStackListener(block);
		block.setInput(action);
	}
	
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.stock3.ui.history.ModifyActionComposite#getAction()
	 */
	@Override
	public Modification getAction() {
		AddTableModification addAction = (AddTableModification)action;
		EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(action);
		
		if(addAction.isNewHistoryTable() != htSelect){
			if (editingDomain != null) {
				Command command = SetCommand.create(editingDomain, action, ChousePackage.Literals.ADD_TABLE_MODIFICATION__NEW_HISTORY_TABLE, htSelect);
				editingDomain.getCommandStack().execute(command);
			} else {
				((AddTableModification)action).setNewHistoryTable(htSelect);
			}
		}
		if(addAction.isNewSelfTable() != btSelect){
			if (editingDomain != null) {
				Command command = SetCommand.create(editingDomain, action, ChousePackage.Literals.ADD_TABLE_MODIFICATION__NEW_SELF_TABLE, btSelect);
				editingDomain.getCommandStack().execute(command);
			} else {
				((AddTableModification)action).setNewSelfTable(btSelect);
			}
		}
		return action;
	};
	
	
}
