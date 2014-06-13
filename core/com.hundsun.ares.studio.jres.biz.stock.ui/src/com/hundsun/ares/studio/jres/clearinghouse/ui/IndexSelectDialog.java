/**
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.clearinghouse.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.database.ui.viewer.IndexColumnLabelProvider;
import com.hundsun.ares.studio.jres.database.ui.viewer.TableColumnLabelProvider;
import com.hundsun.ares.studio.jres.model.database.DatabasePackage;
import com.hundsun.ares.studio.jres.model.database.TableIndex;
import com.hundsun.ares.studio.ui.editor.editingsupport.BooleanEditingSupport;
import com.hundsun.ares.studio.ui.editor.extend.CheckBoxColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelColumnViewerProblemView;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelUtils;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnViewerProblemView;
import com.hundsun.ares.studio.ui.editor.viewers.ReferenceTreeContentProvider;

/**
 * @author liaogc
 * ѡ�������Ի���
 */
public class IndexSelectDialog extends Dialog {

	private IARESResource resource;
	private EObject input;
	private CheckboxTreeViewer treeViewer;
	private String title;
	
	private TableIndex[] result = new TableIndex[0];
	
	public IndexSelectDialog(Shell parentShell, IARESResource resource, EObject input) {
		super(parentShell);
		this.resource = resource;
		this.input = input;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title == null ? "ѡ������" : title);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public IARESResource getResource() {
		return this.resource;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(1, false));
		
		treeViewer = new CheckboxTreeViewer(composite, SWT.CHECK | SWT.BORDER);
		treeViewer.setContentProvider(new ReferenceTreeContentProvider(DatabasePackage.Literals.TABLE_RESOURCE_DATA__INDEXES));
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.getTree().setLinesVisible(true);
		createColumns(treeViewer);
		
		treeViewer.setInput(input);
		GridDataFactory.fillDefaults().hint(-1, 300).applyTo(treeViewer.getTree());
		return composite;
	}
	
	protected void createColumns(TreeViewer viewer) {

		final TreeViewer treeViewer = (TreeViewer) viewer;
		// ����һ�����
		EObjectColumnViewerProblemView problemView = new EObjectColumnViewerProblemView(treeViewer);
		// ������չ��
		EObjectColumnViewerProblemView exProblemView = new ExtensibleModelColumnViewerProblemView(treeViewer);
		
		//�����
		{
			EAttribute attribute = DatabasePackage.Literals.TABLE_INDEX__MARK;
			
			final TreeViewerColumn tvColumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(80);
			tvColumn.getColumn().setText("���");
			
			EObjectColumnLabelProvider provider = new TableColumnLabelProvider(attribute , resource);
			provider.setDiagnosticProvider(problemView);
			tvColumn.setLabelProvider(provider);
			tvColumn.getColumn().setMoveable(true);
		}
		
		// ������
		{
			EAttribute attribute = DatabasePackage.Literals.TABLE_INDEX__NAME;
			
			final TreeViewerColumn tvColumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(130);
			tvColumn.getColumn().setText("������");
			
			EObjectColumnLabelProvider provider = new TableColumnLabelProvider(attribute , resource);
			provider.setDiagnosticProvider(problemView);
			tvColumn.setLabelProvider(provider);
			
		}
//		// �����ֶ��б�
		{
			EStructuralFeature feature = DatabasePackage.Literals.TABLE_INDEX__COLUMNS;
					
			final TreeViewerColumn tvColumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(150);
			tvColumn.getColumn().setText("�����ֶ��б�");
			
			IndexColumnLabelProvider provider = new IndexColumnLabelProvider(feature){
				@Override
				public Color getBackground(Object element) {
					if (resource.isReadOnly()) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
					return super.getBackground(element);
				}
			};
			provider.setDiagnosticProvider(problemView);
			tvColumn.setLabelProvider(provider);
			
			tvColumn.getColumn().setMoveable(true);
		}
		// �Ƿ�Ψһ
		{
			EAttribute attribute = DatabasePackage.Literals.TABLE_INDEX__UNIQUE;
					
			final TreeViewerColumn tvColumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(80);
			tvColumn.getColumn().setText("Ψһ");
					
			CheckBoxColumnLabelProvider provider = new CheckBoxColumnLabelProvider(attribute ,resource);
			tvColumn.setLabelProvider(provider);
					
			tvColumn.getColumn().setMoveable(true);
		}
		
		//�۴���
		{
			EAttribute attribute = DatabasePackage.Literals.TABLE_INDEX__CLUSTER;
			
			final TreeViewerColumn tvColumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(80);
			tvColumn.getColumn().setText("�۴�");
			
			CheckBoxColumnLabelProvider provider = new CheckBoxColumnLabelProvider(attribute , resource);
			tvColumn.setLabelProvider(provider);
			
			tvColumn.setEditingSupport(new BooleanEditingSupport(treeViewer, attribute));
			tvColumn.getColumn().setMoveable(true);
		}
		
		// ��չ��Ϣ
		ExtensibleModelUtils.createExtensibleModelTreeViewerColumns(
				treeViewer, resource, DatabasePackage.Literals.TABLE_INDEX, exProblemView);
		
	
	}
	
	public CheckboxTreeViewer getTreeViewer() {
		return this.treeViewer;
	}
	
	@Override
	protected void okPressed() {
		List<TableIndex> tableIndexs = new ArrayList<TableIndex>();
		for (Object obj : treeViewer.getCheckedElements()) {
			tableIndexs.add((TableIndex) obj);
		}
		this.result = tableIndexs.toArray(new TableIndex[0]);
		super.okPressed();
	}

	public TableIndex[] getSelection() {
		return result;
	}
}
