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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataItemEditingSupportDecorator;
import com.hundsun.ares.studio.jres.model.chouse.CTCUMDetail;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnUniqueModifycation;
import com.hundsun.ares.studio.jres.model.chouse.ChouseFactory;
import com.hundsun.ares.studio.jres.model.chouse.ChousePackage;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.ui.editor.editingsupport.BooleanEditingSupport;
import com.hundsun.ares.studio.ui.editor.editingsupport.JresTextEditingSupportWithContentAssist;
import com.hundsun.ares.studio.ui.editor.editingsupport.TextEditingSupport;
import com.hundsun.ares.studio.ui.editor.extend.CheckBoxColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;

/**
 * @author yanwj06282
 *
 */
public class ModifyColumnUniqueComposite extends ModifyActionColumnComposite<CTCUMDetail>{

	private EList<CTCUMDetail> input;
	
	public ModifyColumnUniqueComposite(Composite parent, TableResourceData tableData, IARESResource resource,
			Modification action) {
		super(parent, tableData, resource, action);
	}
	
	@Override
	protected EReference getEReference() {
		return ChousePackage.Literals.CHANGE_TABLE_COLUMN_TYPE_MODIFICATION__DETAILS;
	}

	@Override
	protected CTCUMDetail creatBlankItem() {
		CTCUMDetail detail =  ChouseFactory.eINSTANCE.createCTCUMDetail();
		return detail;
	}

	@Override
	protected EList<CTCUMDetail> getActionItems(Modification modification) {
		return input;
	}

	@Override
	protected void creatColumnComposite(TreeViewer treeViewer,
			IARESResource resource) {
		
		{
			EAttribute attribute = ChousePackage.Literals.MODIFY_DETAIL__MARK;
			
			final TreeViewerColumn tvColumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(100);
			tvColumn.getColumn().setText("���");
			
			EObjectColumnLabelProvider provider = new EObjectColumnLabelProvider(attribute);
			tvColumn.setLabelProvider(provider);
			
			tvColumn.getColumn().setMoveable(true);
		}
		
		// �ֶ���
		{
			EAttribute attribute = ChousePackage.Literals.MODIFY_DETAIL__NAME;
			
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
		
		// �Ƿ�Ψһ
		{
			EAttribute attribute = ChousePackage.Literals.CTCUM_DETAIL__UNIQUE;
			
			final TreeViewerColumn tvColumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(100);
			tvColumn.getColumn().setText("�Ƿ�Ψһ");
			
			CheckBoxColumnLabelProvider provider = new CheckBoxColumnLabelProvider(attribute , resource);
			tvColumn.setLabelProvider(provider);
			BooleanEditingSupport support = new BooleanEditingSupport(treeViewer, attribute);
			tvColumn.setEditingSupport(support);
			tvColumn.getColumn().setMoveable(true);
		}
		
	}

	@Override
	protected void initAction(Modification modification) {
		if(modification != null && modification instanceof ChangeTableColumnUniqueModifycation){
			action = modification;
			input = ((ChangeTableColumnUniqueModifycation)action).getDetails();
		}
		else{
			action = ChouseFactory.eINSTANCE.createChangeTableColumnUniqueModifycation();
			input = ((ChangeTableColumnUniqueModifycation)action).getDetails();
			if (tableData == null) {
				try {
					tableData = resource.getInfo(TableResourceData.class);
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
			for (TableColumn column : tableData.getColumns()){
				CTCUMDetail de = ChouseFactory.eINSTANCE.createCTCUMDetail();
				de.setName(column.getName());
				if (column.isUnique()) {
					de.setUnique(column.isUnique());
					input.add(de);
				}
			}
		}
	}
	
}
