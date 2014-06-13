/**
 * Դ�������ƣ�AddColumnComposite1.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse.composite;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.clearinghouse.ui.page.RevisionHistoryColumnsViewerBlock;
import com.hundsun.ares.studio.jres.database.ui.actions.AddNonStdFiledColumnAction;
import com.hundsun.ares.studio.jres.model.chouse.AddTableColumnModification;
import com.hundsun.ares.studio.jres.model.chouse.ChouseFactory;
import com.hundsun.ares.studio.jres.model.chouse.ChousePackage;
import com.hundsun.ares.studio.jres.model.chouse.HisTableColumn;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerAddAction;
import com.hundsun.ares.studio.ui.editor.editable.ActionEditableUnit;

/**
 * @author wangxh
 *
 */
public class AddColumnComposite extends ModifyActionColumnComposite<HisTableColumn> {

	/**
	 * ���ӱ��ֶ�
	 * @param parent
	 * @param resource
	 * @param action
	 */
	public AddColumnComposite(Composite parent, TableResourceData tableData, IARESResource resource, Modification action) {
		super(parent, tableData, resource, action);
		EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(tableData);
		RevisionHistoryColumnsViewerBlock block = new RevisionHistoryColumnsViewerBlock(domain, resource) {
			protected EReference getEReference() {
				return ChousePackage.Literals.ADD_TABLE_COLUMN_MODIFICATION__COLUMNS;
			}
			
			protected void createAddAction() {
				addAction = new ColumnViewerAddAction(
						getColumnViewer(), 
						getEditingDomain(),
						null,
						getEReference(),
						ChousePackage.Literals.HIS_TABLE_COLUMN);
				addAction.setText("���ӱ�׼�ֶ�");
				getActionRegistry().registerAction(addAction);
				getSelectionActions().add(addAction.getId());
			}
			
			/**
			 * �½��Ǳ�׼�ֶε�Action
			 */
			protected void createAddNonStdAction() {
				addNonStdAction = new AddNonStdFiledColumnAction(
										getColumnViewer(), 
										getEditingDomain(), 
										null,
										getEReference(),
										ChousePackage.Literals.HIS_TABLE_COLUMN);
				getActionRegistry().registerAction(addNonStdAction);
				getSelectionActions().add(addNonStdAction.getId());
				//ֻ������
				// 2012-05-15 sundl �༭����ʹ�õ�ʱ�򣬿�����null
				if (getEditableControl() != null) {
					getEditableControl().addEditableUnit(new ActionEditableUnit(addNonStdAction));
				}
			}
		};
		
		block.createControl(this, new FormToolkit(getDisplay()));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(block.getControl());
		domain.getCommandStack().addCommandStackListener(block);
		block.setInput(this.action);
	}
	
	/**
	 * ������ϸ����
	 * @param parent
	 * @param resource
	 */
	@Override
	protected void creatDetailComposite(Composite parent,	IARESResource resource) {
	}
	

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.ui.editors.history.component.ModifyActionColumnComposite#creatBlankItem()
	 */
	@Override
	protected HisTableColumn creatBlankItem() {
		return ChouseFactory.eINSTANCE.createHisTableColumn();
	}

	
	@Override
	protected EList<HisTableColumn> getActionItems(Modification action) {
		if(action instanceof AddTableColumnModification){
			return ((AddTableColumnModification)action).getColumns();
		}
		return new BasicEList<HisTableColumn>();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.ui.editors.history.component.ModifyActionComposite#initAction(com.hundsun.ares.studio.jres.model.database.Modification)
	 */
	@Override
	protected void initAction(Modification modification) {
		if(modification != null && modification instanceof AddTableColumnModification){
			action = (AddTableColumnModification) modification;
		}
		else
		{
			action = ChouseFactory.eINSTANCE.createAddTableColumnModification();
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.ui.editors.history.component.ModifyActionColumnComposite#getEReference()
	 */
	@Override
	protected EReference getEReference() {
		return ChousePackage.Literals.ADD_TABLE_COLUMN_MODIFICATION__COLUMNS;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.clearinghouse.composite.ModifyActionColumnComposite#creatColumnComposite(org.eclipse.jface.viewers.TreeViewer, com.hundsun.ares.studio.core.IARESResource)
	 */
	@Override
	protected void creatColumnComposite(TreeViewer treeViewer, IARESResource resource) {
		
	}
	
	@Override
	public Modification getAction() {
		return action;
	}

}
