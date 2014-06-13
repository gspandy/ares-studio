/**
 * Դ�������ƣ�IndexSelectEditorDialog.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.editors.dialog;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.database.ui.viewer.TableColumnLabelProvider;
import com.hundsun.ares.studio.jres.database.ui.viewer.TableColumnRefLabelProvider;
import com.hundsun.ares.studio.jres.model.database.DatabasePackage;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableIndexColumn;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.ui.editor.extend.CheckBoxColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.ReferenceContentProvider;

/**
 * @author wangxh
 * 
 */
public class IndexAddSelectEditorDialog extends Dialog{

	private IARESResource resource;
	private TableResourceData info;
	private CheckboxTableViewer tableViewer;
	private Object indexs;
	private List<TableColumn> result;
	private String title;
	/**
	 * @param parentShell
	 * @param info
	 * @param resource
	 */
	public IndexAddSelectEditorDialog(Shell parentShell,String title,
			TableResourceData info,Object index, IARESResource resource) {
		super(parentShell);
		this.title = title;
		this.info = info;
		this.indexs = index;
		this.resource = resource;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#getShellStyle()
	 */
	@Override
	protected int getShellStyle() {
		return super.getShellStyle() |SWT.RESIZE | SWT.MAX;
	}
	
	public List<TableColumn> getSelectedColumns() {
		return result;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		result = (List)Arrays.asList(tableViewer.getCheckedElements()) ;
		super.okPressed();
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		tableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER|SWT.FULL_SELECTION|SWT.V_SCROLL|SWT.H_SCROLL);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		
		GridDataFactory.swtDefaults().hint(-1, 200).grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(tableViewer.getTable());
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection sel =  (IStructuredSelection) event.getSelection();
				if (!sel.isEmpty()) {
					Object element = sel.getFirstElement();
					tableViewer.setChecked(element, !tableViewer.getChecked(element));
				}
			}
		});
		
		tableViewer.addFilter(new ViewerFilter() {
			
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				TableColumn column = (TableColumn) element;
				if (indexs instanceof List) {
					List<TableIndexColumn> inds = (List<TableIndexColumn>) indexs;
					for (TableIndexColumn indexColumn : inds) {
						if (column.getName().equals(indexColumn.getColumnName())) {
							return false;
						}
					}
				}

//				EList<TableIndexColumn> indexColumns = index.getColumns();
//				column.getName() not in index.getColumns()
				return true;
			}
		});
		tableViewer.setContentProvider(new ReferenceContentProvider(DatabasePackage.Literals.TABLE_RESOURCE_DATA__COLUMNS));
		
		// �ֶ���
		{
			EAttribute attribute = DatabasePackage.Literals.TABLE_COLUMN__FIELD_NAME;
			
			final TableViewerColumn tvColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(100);
			tvColumn.getColumn().setText("�ֶ���");
			
			EObjectColumnLabelProvider provider = new EObjectColumnLabelProvider(attribute);
			tvColumn.setLabelProvider(provider);
		}
		// ������
		{
			EAttribute attribute = DatabasePackage.Literals.TABLE_COLUMN__COLUMN_NAME;
			
			final TableViewerColumn tvColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(100);
			tvColumn.getColumn().setText("������");
			
			EObjectColumnLabelProvider provider = new EObjectColumnLabelProvider(attribute);
			tvColumn.setLabelProvider(provider);
		}
		
		// ������
		{
			final TableViewerColumn tvColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(100);
			tvColumn.getColumn().setText("������");
			
			TableColumnRefLabelProvider provider = new TableColumnRefLabelProvider(resource.getBundle(), TableColumnRefLabelProvider.TYPE.ChineseName);
			tvColumn.setLabelProvider(provider);
		}
		// �ֶ�����
		{			
			final TableViewerColumn tvColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(100);
			tvColumn.getColumn().setText("�ֶ�����");
			
			EAttribute attribute = DatabasePackage.Literals.TABLE_COLUMN__DATA_TYPE;
			TableColumnLabelProvider provider = new TableColumnLabelProvider(attribute, resource);
			tvColumn.setLabelProvider(provider);
		}
		// �Ƿ�����
		{
			EAttribute attribute = DatabasePackage.Literals.TABLE_COLUMN__PRIMARY_KEY;
			
			final TableViewerColumn tvColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(100);
			tvColumn.getColumn().setText("�Ƿ�����");
			
			CheckBoxColumnLabelProvider provider = new CheckBoxColumnLabelProvider(attribute , resource);
			tvColumn.setLabelProvider(provider);
			
		}
		// �Ƿ�Ϊ��
		{
			EAttribute attribute = DatabasePackage.Literals.TABLE_COLUMN__NULLABLE;
			
			final TableViewerColumn tvColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(100);
			tvColumn.getColumn().setText("�Ƿ�Ϊ��");
			
			CheckBoxColumnLabelProvider provider = new CheckBoxColumnLabelProvider(attribute , resource);
			tvColumn.setLabelProvider(provider);
			
		}
		
		tableViewer.setInput(info);
		
		 return composite;
	}

}
