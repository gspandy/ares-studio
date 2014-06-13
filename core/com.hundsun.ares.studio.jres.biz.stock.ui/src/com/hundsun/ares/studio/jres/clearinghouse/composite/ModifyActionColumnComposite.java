/**
 * Դ�������ƣ�ModifyActionColumnComposite.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse.composite;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.chouse.Stock3ColumnAdapter;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableIndex;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.ui.cellEditor.AresContentProposalProvider;
import com.hundsun.ares.studio.ui.editor.blocks.FormWidgetUtils;
import com.hundsun.ares.studio.ui.editor.viewers.ColumnViewerPatternFilter;

/**
 * @author wangxh
 *
 */
public abstract class ModifyActionColumnComposite<T> extends ModifyActionComposite {

	//��������
	protected List<T>input;
	protected TreeViewer treeViewer ;
	
	// 2013-12-05 sundl 
	protected Button btnAdd;
	protected Button btnRemove;
	protected Button btnMoveUp;
	protected Button btnMoveDown;
	
	/**
	 * �����޶������б�������
	 * @param parent
	 * @param resource
	 * @param action
	 */
	public ModifyActionColumnComposite(Composite parent, TableResourceData tableData,
			IARESResource resource, Modification action) {
		super(parent, tableData, resource, action);
	}

	/**
	 * ������ϸ����
	 * @param parent
	 * @param resource
	 */
	@Override
	protected void creatDetailComposite(Composite parent,	IARESResource resource) {
		FilteredTree tree = new FilteredTree(parent, FormWidgetUtils.getDefaultTreeStyles(), new ColumnViewerPatternFilter(), true);
		
		treeViewer = tree.getViewer();
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.setContentProvider(getContentProvider());
		
		creatColumnComposite(treeViewer,resource);
		if(input == null || input.size()>0){
			input = new ArrayList<T>();
		}
		input.addAll(getActionItems(action));
		treeViewer.setInput(input);
		
		Composite ButtonGroup = creatButtons(parent,treeViewer);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tree);
		GridDataFactory.fillDefaults().hint(70, -1).applyTo(ButtonGroup);
	}
	
	/**
	 * ����button���漰����
	 * @param parent
	 * @param viewer
	 * @return 
	 */
	protected  Composite creatButtons(Composite parent, final TreeViewer viewer){
		Composite buttonGroup = new Composite(parent, SWT.None);
		buttonGroup.setLayout(new GridLayout());
		
		createAddButton(buttonGroup);
		createRemoveButton(buttonGroup);
		createMoveUpButton(buttonGroup);
		createMoveDownButton(buttonGroup);
		
		//������ѡ����������Ƿ��û�
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection select = (IStructuredSelection)viewer.getSelection();
				if(select == null || select.toList().size()==0){
					if (btnRemove != null)
						btnRemove.setEnabled(false);
					if (btnMoveDown != null)
						btnMoveDown.setEnabled(false);
					if (btnMoveUp != null)
						btnMoveUp.setEnabled(false);
				}
				else{
					List<?> selectObjects = select.toList();
					int size = selectObjects.size();
					int last = input.indexOf(selectObjects.get(size-1));
					int first = input.indexOf(selectObjects.get(0));
					if(input.size()==size){
						if (btnRemove != null)
							btnRemove.setEnabled(true);
						if (btnMoveDown != null)
							btnMoveDown.setEnabled(false);
						if (btnMoveUp != null)
							btnMoveUp.setEnabled(false);
					}
					else if(last == (size -1)){
						if (btnRemove != null)
							btnRemove.setEnabled(true);
						if (btnMoveDown != null)
							btnMoveDown.setEnabled(true);
						if (btnMoveUp != null)
							btnMoveUp.setEnabled(false);
					}
					else if(first == input.size()-size){
						if (btnRemove != null)
							btnRemove.setEnabled(true);
						if (btnMoveDown != null)
							btnMoveDown.setEnabled(false);
						if (btnMoveUp != null)
							btnMoveUp.setEnabled(true);
					}
					else{
						if (btnRemove != null)
							btnRemove.setEnabled(true);
						if (btnMoveDown != null)
							btnMoveDown.setEnabled(true);
						if (btnMoveUp != null)
							btnMoveUp.setEnabled(true);
					}
				}
			}
		});
		
		return buttonGroup;
	}
	
	/**
	 * ����Add��ť�¼��������������������೹����д�߼�
	 */
	protected void handleAdd() {
		T object = creatBlankItem();
		input.add(object);
		this.treeViewer.setInput(input);
	}
	
	protected void createAddButton(Composite buttonGroup) {
		btnAdd = new Button(buttonGroup, SWT.None);
		btnAdd.setText("����");
		btnAdd.setEnabled(true);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(btnAdd);
		//����
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleAdd();
			}
		});
	}
	
	protected void createRemoveButton(Composite buttonGroup) {
		btnRemove = new Button(buttonGroup, SWT.None);
		btnRemove.setText("ɾ��");
		btnRemove.setEnabled(false);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(btnRemove);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection selected = getViewer().getSelection();
				if(selected instanceof IStructuredSelection){
					@SuppressWarnings("unchecked")
					List<Object> selectObjs = ((IStructuredSelection)selected).toList();
					input.removeAll(selectObjs);
					getViewer().setInput(input);
				}
			}
		});
	}
	
	protected void createMoveUpButton(Composite buttonGroup) {
		btnMoveUp = new Button(buttonGroup, SWT.None);
		btnMoveUp.setText("����");
		btnMoveUp.setEnabled(false);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(btnMoveUp);
		//����
		btnMoveUp.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) getViewer().getSelection();
				if (selection != null && !selection.isEmpty()) {
					Object[] objs = selection.toArray();
					for (int i = 0; i < objs.length; i++) {
						int oldIndex = input.indexOf(objs[i]);
						// �Ѿ���ˣ�����������
						if (oldIndex == 0) {
							continue;
						}
						
						// ���ܳ���һ��ѡ�е�
						if (i > 0 && input.get(oldIndex - 1) == objs[i - 1]) {
							continue;
						}

						T tmp = input.get(oldIndex - 1);
						T item1 = creatBlankItem();
						T item2 = creatBlankItem();
						input.set(oldIndex - 1, item1);
						input.set(oldIndex, item2);
						input.set(oldIndex - 1, (T) objs[i]);
						input.set(oldIndex, tmp);
					}
				}

				getViewer().setInput(input);
			}
		});
	}
	
	protected void createMoveDownButton(Composite buttonGroup) {
		btnMoveDown = new Button(buttonGroup, SWT.None);
		btnMoveDown.setText("����");
		btnMoveDown.setEnabled(false);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(btnMoveDown);
		//����
		btnMoveDown.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) getViewer().getSelection();				
				//EList<T> input = getActionItems(action);
				if (selection != null && !selection.isEmpty()) {
					Object[] objs = selection.toArray();
					for (int i = objs.length - 1; i >= 0; i--) {
						
						int oldIndex = input.indexOf(objs[i]);
						// �Ѿ�����ˣ�����������
					
						if (oldIndex == input.size() - 1) {
							continue;
						}

						// ���ܳ���һ��ѡ�е�
						if (i < objs.length - 1 && input.get(oldIndex + 1) == objs[i + 1]) {
							continue;
						}

						T tmp = input.get(oldIndex + 1);
						T item1 = creatBlankItem();
						T item2 = creatBlankItem();
						input.set(oldIndex + 1, item1);
						input.set(oldIndex, item2);
						input.set(oldIndex + 1, (T) objs[i]);
						input.set(oldIndex, tmp);
					}
				}
				getViewer().setInput(input);
			}
			
		});

	}
	
	protected TreeViewer getViewer() {
		return this.treeViewer;
	}
	
	/**
	 * ��ȡ�޸�����Reference
	 * @return
	 */
	protected abstract EReference getEReference() ;


	/**
	 * ����һ���յ��б���Ŀ
	 * @return
	 */
	protected abstract T creatBlankItem();

	/**
	 * ��ȡ�޶����ݵ�����item
	 * @param modification
	 * @return
	 */
	protected abstract EList<T> getActionItems(Modification modification);

	/**
	 * ������ϸ�б�
	 * @param treeViewer
	 * @param resource
	 * @param action
	 */
	protected abstract void creatColumnComposite(TreeViewer treeViewer,	IARESResource resource);
	
	/**
	 * �б�ContentProvider
	 * @return
	 */
	protected  IContentProvider getContentProvider(){
	    return new ITreeContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			
			@Override
			public void dispose() {
			}
			
			@Override
			public boolean hasChildren(Object element) {
				return getChildren(element).length>0;
			}
			
			@Override
			public Object getParent(Object element) {
				return null;
			}
			
			@Override
			public Object[] getElements(Object element) {
				return getChildren(element);
			}
			
			@Override
			public Object[] getChildren(Object element) {
				if(element instanceof List){
					return ((List<?>)element).toArray(new Object[0]);
				}
				return new Object[0];
			}
		};
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.stock3.ui.history.ModifyActionComposite#getAction()
	 */
	@Override
	public Modification getAction() {
		final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(action);
		if(!ObjectUtils.equals(input, getActionItems(action))){
				if (editingDomain != null) {
				Command command = SetCommand.create(editingDomain, action, getEReference(), input);
				editingDomain.getCommandStack().execute(command);
			} else {
				getActionItems(action).clear();
				getActionItems(action).addAll(input);
			}
		}
		return action;
	}
	
	//�ṩ��ǰ���ݿ���е��ֶκ�������ContentProposalProvider,���Ա��������ʾ
	protected class ColumnProposalProvider extends AresContentProposalProvider{
		String type ;
		
		public ColumnProposalProvider(String type) {
			super();
			this.type = type;
		}

		@Override
		public void updateContent(Object element) {
			List<ExtensibleModel>input = new ArrayList<ExtensibleModel>();
			if(resource != null){
				try {
					Object obj = resource.getInfo(Object.class);
					if(obj instanceof TableResourceData){
						TableResourceData resData = (TableResourceData)obj;
						if(StringUtils.equals(type, IDatabaseRefType.TableField)){
							input.addAll(resData.getColumns());
						}
						else if(StringUtils.equals(type, IDatabaseRefType.TableIndex)){
							input.addAll(resData.getIndexes());
						}
					}
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
			setInput(input.toArray(new ExtensibleModel[0]));
			
		}

		/* (non-Javadoc)
		 * @see com.hundsun.ares.studio.ui.cellEditor.AresContentProposalProvider#getProposals(java.lang.String, int)
		 */
		@Override
		public IContentProposal[] getProposals(String contents, int position) {
			//return super.getProposals(contents, position);
			List<IContentProposal> proposals = new ArrayList<IContentProposal>();
			if (input != null) {
				for (Object obj : input) {
					if(obj instanceof TableColumn){
						TableColumn item = (TableColumn)obj;
						if(filter(item.getName(), contents)){
							Stock3ColumnAdapter adapter = new Stock3ColumnAdapter(item, resource.getARESProject());
							String description = adapter.getChineseName();
							if (StringUtils.isNotBlank(item.getColumnName())) {
								description = String.format("(���ñ�׼�ֶΣ�%s) %s", item.getFieldName(), description);
							}
							IContentProposal proposal = new columnProposal(item.getName(), description);
							if (proposal != null) {
								proposals.add(proposal);
							}
						}
					}
					else if(obj instanceof TableIndex){
						TableIndex item = (TableIndex)obj;
						if(filter(item.getName(), contents)){
							IContentProposal proposal = new columnProposal(item.getName(), "");
							if (proposal != null) {
								proposals.add(proposal);
							}
						}
					}
				}
			}
			return proposals.toArray(new IContentProposal[0]);
		}
		
		/**
		 * ������ʾ��Ϣ
		 * @param name ������Ҫ��ʾ�Ķ��������
		 * @param str ������ַ���
		 * @return �Ƿ�Ҫ��ʾ
		 */
		private boolean filter(String name,String str){
			if(StringUtils.startsWith(name, str)){
				return true;
			}
			return false;
		}
		
		
	}
	
	//��ǰ���е��ֶκ�������ContentProposal
	protected class columnProposal implements IContentProposal{
		String name = "";
		String label = "";
		
		
		public columnProposal(String name, String description) {
			super();
			this.name = name;
			this.label = name + "--->" + description;
		}

		@Override
		public String getContent() {
			return name;
		}

		@Override
		public int getCursorPosition() {
			return name.length();
		}

		@Override
		public String getLabel() {
			return label;
		}

		@Override
		public String getDescription() {
			return null;
		}
		
	}

}
