/**
 * Դ�������ƣ�ModifyColumnTypeComposite.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse.composite;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.clearinghouse.ui.ColumnSelectionDialog;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalHelper;
import com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalProvider;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataItemEditingSupportDecorator;
import com.hundsun.ares.studio.jres.model.chouse.CTCTMDetail;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnTypeModification;
import com.hundsun.ares.studio.jres.model.chouse.ChouseFactory;
import com.hundsun.ares.studio.jres.model.chouse.ChousePackage;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.ui.editor.editingsupport.JresTextEditingSupportWithContentAssist;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;

/**
 * @author wangxh
 *
 */
public class ModifyColumnTypeComposite extends ModifyActionColumnComposite<CTCTMDetail>{

	/**
	 * �޸ı��ֶ�����
	 * @param parent
	 * @param tableData 
	 * @param resource
	 * @param action
	 */
	public ModifyColumnTypeComposite(Composite parent, TableResourceData tableData, IARESResource resource,
			Modification action) {
		super(parent, tableData, resource, action);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.ui.editors.history.component.ModifyActionColumnComposite#getEReference()
	 */
	@Override
	protected EReference getEReference() {
		return ChousePackage.Literals.CHANGE_TABLE_COLUMN_TYPE_MODIFICATION__DETAILS;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.ui.editors.history.component.ModifyActionColumnComposite#creatBlankItem()
	 */
	@Override
	protected CTCTMDetail creatBlankItem() {
		return ChouseFactory.eINSTANCE.createCTCTMDetail();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.ui.editors.history.component.ModifyActionColumnComposite#getActionItems(com.hundsun.ares.studio.jres.model.database.Modification)
	 */
	@Override
	protected EList<CTCTMDetail> getActionItems(Modification modification) {
		return ((ChangeTableColumnTypeModification)action).getDetails();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.ui.editors.history.component.ModifyActionColumnComposite#creatColumnComposite(org.eclipse.jface.viewers.TreeViewer, com.hundsun.ares.studio.core.IARESResource)
	 */
	@Override
	protected void creatColumnComposite(TreeViewer treeViewer,
			IARESResource resource) {
		
		{
			EAttribute attribute = ChousePackage.Literals.CTCTM_DETAIL__MARK;
			
			final TreeViewerColumn tvColumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(100);
			tvColumn.getColumn().setText("���");
			
			EObjectColumnLabelProvider provider = new EObjectColumnLabelProvider(attribute);
			tvColumn.setLabelProvider(provider);
			
			tvColumn.getColumn().setMoveable(true);
		}
		
		// �ֶ���
		{
			EAttribute attribute = ChousePackage.Literals.CTCTM_DETAIL__NAME;
			
			TreeViewerColumn tvColumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(200);
			tvColumn.getColumn().setText("�ֶ���");
			
			EObjectColumnLabelProvider provider = new EObjectColumnLabelProvider(attribute);
			tvColumn.setLabelProvider(provider);
			
			//tvColumn.setEditingSupport(new TextEditingSupport(treeViewer, attribute));
			ColumnProposalProvider proposalProvider = new ColumnProposalProvider(IDatabaseRefType.TableField);
			JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(
					treeViewer,
					attribute, 
					proposalProvider);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,resource));
			tvColumn.setEditingSupport(es);
		}
		// �ֶ�����
		{
			EAttribute attribute = ChousePackage.Literals.CTCTM_DETAIL__NEW_TYPE;
			
			TreeViewerColumn tvColumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(200);
			tvColumn.getColumn().setText("�ֶ�����");
			
			EObjectColumnLabelProvider provider = new EObjectColumnLabelProvider(attribute);
			tvColumn.setLabelProvider(provider);
			
			// 2. proposal provider
			MetadataContentProposalHelper helper = new MetadataContentProposalHelper(resource.getARESProject());
			MetadataContentProposalProvider proposalProvider = new MetadataContentProposalProvider(helper, IMetadataRefType.BizType, resource.getARESProject());
			
			// 3. ����EditingSupport, 
			JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(
					treeViewer,
					attribute, 
					proposalProvider);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,resource));
			tvColumn.setEditingSupport(es);
		}
		
	}

	@Override
	protected void handleAdd() {
		ColumnSelectionDialog dialog = new ColumnSelectionDialog(getShell(), resource, tableData) {
			protected Control createDialogArea(Composite parent) {
				Control control = super.createDialogArea(parent);
				getTreeViewer().addFilter(new ViewerFilter() {
					@Override
					public boolean select(Viewer viewer, Object parentElement, Object element) {
						if (element instanceof TableColumn) {
							TableColumn column = (TableColumn) element;
							for (CTCTMDetail removed : input) {
								if (StringUtils.equals(column.getName(), removed.getSnapshot().getName())) {
									return false;
								}
							}
							return true;
						}
						return false;
					}
				});
				return control;
			}
		};
		dialog.setTitle("ѡ����");
		if (dialog.open() == IDialogConstants.OK_ID) {
			TableColumn[] columns = dialog.getSelection();
			for (TableColumn c : columns) {
				CTCTMDetail detail = ChouseFactory.eINSTANCE.createCTCTMDetail();
				detail.setSnapshot(EcoreUtil.copy(c));
				input.add(detail);
			}
			treeViewer.refresh();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.ui.editors.history.component.ModifyActionComposite#initAction(com.hundsun.ares.studio.jres.model.database.Modification)
	 */
	@Override
	protected void initAction(Modification modification) {
		if(modification != null && modification instanceof ChangeTableColumnTypeModification){
			action = modification;
		}
		else{
			action = ChouseFactory.eINSTANCE.createChangeTableColumnTypeModification();
		}
	}
	
}
