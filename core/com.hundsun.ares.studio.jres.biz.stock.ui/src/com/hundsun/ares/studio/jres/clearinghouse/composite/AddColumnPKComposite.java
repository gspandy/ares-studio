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

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataItemEditingSupportDecorator;
import com.hundsun.ares.studio.jres.model.chouse.AddTableColumnPKModification;
import com.hundsun.ares.studio.jres.model.chouse.ChouseFactory;
import com.hundsun.ares.studio.jres.model.chouse.ChousePackage;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.chouse.ModifyDetail;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.ui.editor.editingsupport.JresTextEditingSupportWithContentAssist;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;

/**
 * @author yanwj06282
 *
 */
public class AddColumnPKComposite extends ModifyActionColumnComposite<ModifyDetail>{

	public AddColumnPKComposite(Composite parent, TableResourceData tableData, IARESResource resource,
			Modification action) {
		super(parent, tableData, resource, action);
	}
	
	@Override
	protected EReference getEReference() {
		return ChousePackage.Literals.ADD_TABLE_COLUMN_PK_MODIFICATION__DETAILS;
	}

	@Override
	protected ModifyDetail creatBlankItem() {
		ModifyDetail detail = ChouseFactory.eINSTANCE.createModifyDetail();
		return detail;
	}

	@Override
	protected EList<ModifyDetail> getActionItems(Modification modification) {
		return ((AddTableColumnPKModification)action).getDetails();
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
			
			ColumnProposalProvider proposalProvider = new ColumnProposalProvider(IDatabaseRefType.TableField);
			JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(
					treeViewer,
					attribute, 
					proposalProvider);
			es.setDecorator(new MetadataItemEditingSupportDecorator(attribute,resource));
			tvColumn.setEditingSupport(es);
		}
		
	}

	@Override
	protected void initAction(Modification modification) {
		if(modification != null && modification instanceof AddTableColumnPKModification){
			action = modification;
		}
		else{
			action = ChouseFactory.eINSTANCE.createAddTableColumnPKModification();
		}
	}
	
}
