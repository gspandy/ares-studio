/**
 * Դ�������ƣ�IndexFieldAddSelectEditorDialog.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.editors.dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.database.ui.DatabaseUI;
import com.hundsun.ares.studio.jres.database.ui.editors.dialog.inner.IndexTable;
import com.hundsun.ares.studio.jres.model.chouse.util.StockUtils;
import com.hundsun.ares.studio.jres.model.database.DatabaseFactory;
import com.hundsun.ares.studio.jres.model.database.TableIndexColumn;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;

/**
 * @author liaogc
 *
 */
public class IndexFieldAddSelectEditorDialog extends Dialog {
	
	private ColumnViewer viewer;
	private String title;
	private IARESResource resource;
	private TableResourceData tableResourceData;
	private List<TableIndexColumn> addFieldColumnList = new ArrayList<TableIndexColumn>();//���ӵ������ֶ�
	private List<TableIndexColumn> oldFeildColumnList = new ArrayList<TableIndexColumn>();//ԭ�������������ֶ�
	private List<TableIndexColumn> result = new ArrayList<TableIndexColumn>();
	private IndexTable table;
	
	
	/**
	 * @param parentShell
	 * @param Title
	 * @param Viewer
	 * @param resource
	 */
	public IndexFieldAddSelectEditorDialog(Shell parentShell, String Title, IARESResource resource,TableResourceData tableResourceData,List<TableIndexColumn> addFieldColumnList,List<TableIndexColumn> oldFeildColumnList  ) {
		super(parentShell);
		this.title = Title;
		this.resource = resource;
		this.tableResourceData = tableResourceData;
		this.addFieldColumnList = addFieldColumnList;
		this.oldFeildColumnList = oldFeildColumnList;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
		newShell.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(DatabaseUI.PLUGIN_ID, "icons/table.gif").createImage());
	}
	
	
	protected void okPressed() {
		result.clear();
		List<TableIndexColumn> totalElements = (List<TableIndexColumn>) table.getInput();
		for(TableIndexColumn tableIndexColumn:totalElements){
			boolean isRight = true;
			for(TableIndexColumn indexColumnadd1:this.oldFeildColumnList){
				if(StringUtils.equals(indexColumnadd1.getColumnName(), tableIndexColumn.getColumnName())){
					isRight = false;
					break;
				}
			}
			if(isRight){
				result.add(tableIndexColumn);
			}
		}
		
		super.okPressed();
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite= (Composite)super.createDialogArea(parent);
		GridLayoutFactory.fillDefaults().numColumns(3).margins(10, 10).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, true).hint(400, 300).applyTo(composite);
		table=new IndexTable(composite, resource ,tableResourceData);
	
		Collection<TableIndexColumn> inputs=new ArrayList<TableIndexColumn>();
		for(TableIndexColumn column:oldFeildColumnList){
			TableIndexColumn copy=DatabaseFactory.eINSTANCE.createTableIndexColumn();
			copy.setAscending(column.isAscending());
			copy.setColumnName(column.getColumnName());
			copy.setColumnType(column.getColumnType());
			inputs.add(copy);
		}
		for(TableIndexColumn column:addFieldColumnList){
			TableIndexColumn copy=DatabaseFactory.eINSTANCE.createTableIndexColumn();
			copy.setAscending(column.isAscending());
			copy.setColumnName(column.getColumnName());
			copy.setColumnType(column.getColumnType());
			inputs.add(copy);
		}
		table.addFilter(new IndexFieldAddViewerFilter());
		table.setInput(inputs);
		return composite;
	}
	
	public Object getResult(){
		return result;
	}
	/**
	 * ����ԭ�������е��ֶ�
	 * @author liaogc
	 *
	 */
	private class IndexFieldAddViewerFilter extends ViewerFilter{

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if(element instanceof TableIndexColumn){
				TableIndexColumn indexColumn = (TableIndexColumn) element;
				for(TableIndexColumn addFieldColumn:addFieldColumnList){
					if(StringUtils.equals(indexColumn.getColumnName(), addFieldColumn.getColumnName())){
						return true;
					}
				}
				return false;
			}
			return true;
		}
		
	}

}
