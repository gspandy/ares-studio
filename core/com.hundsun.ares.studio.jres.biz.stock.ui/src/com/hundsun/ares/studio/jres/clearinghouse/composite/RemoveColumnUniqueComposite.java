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
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.chouse.ChouseFactory;
import com.hundsun.ares.studio.jres.model.chouse.ChousePackage;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.chouse.ModifyDetail;
import com.hundsun.ares.studio.jres.model.chouse.RemoveTableColumnUniqueModifycation;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;

/**
 * @author yanwj06282
 *
 */
public class RemoveColumnUniqueComposite extends ModifyActionColumnComposite<ModifyDetail>{

	public RemoveColumnUniqueComposite(Composite parent, TableResourceData tableData, IARESResource resource,
			Modification action) {
		super(parent, tableData, resource, action);
	}
	
	@Override
	protected EReference getEReference() {
		return ChousePackage.Literals.REMOVE_TABLE_COLUMN_UNIQUE_MODIFYCATION__DETAILS;
	}

	@Override
	protected ModifyDetail creatBlankItem() {
		ModifyDetail detail = ChouseFactory.eINSTANCE.createModifyDetail();
		return detail;
	}

	@Override
	protected EList<ModifyDetail> getActionItems(Modification modification) {
		return ((RemoveTableColumnUniqueModifycation)action).getDetails();
	}

	@Override
	protected void creatColumnComposite(TreeViewer treeViewer,
			IARESResource resource) {}

	@Override
	protected Composite creatButtons(Composite parent, TreeViewer viewer) {
		Composite buttonGroup = new Composite(parent, SWT.None);
		buttonGroup.setLayout(new GridLayout());
		return buttonGroup;
	}
	
	@Override
	protected void initAction(Modification modification) {
		if(modification != null && modification instanceof RemoveTableColumnUniqueModifycation){
			action = modification;
		}
		else{
			action = ChouseFactory.eINSTANCE.createRemoveTableColumnUniqueModifycation();
		}
	}
	
}
