/**
 * Դ�������ƣ�ForeignKeyFieldSelectDialog.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.editors.dialog;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESBundle;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.util.ARESElementUtil;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseResType;
import com.hundsun.ares.studio.jres.database.ui.DatabaseUI;
import com.hundsun.ares.studio.jres.database.ui.viewer.TableColumnRefLabelProvider;
import com.hundsun.ares.studio.jres.model.database.DatabaseFactory;
import com.hundsun.ares.studio.jres.model.database.DatabasePackage;
import com.hundsun.ares.studio.jres.model.database.ForeignKey;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableKey;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.jres.model.database.util.DatabaseUtil;
import com.hundsun.ares.studio.ui.assist.JresARESResourceAssistantAndSelectionField;
import com.hundsun.ares.studio.ui.dialog.ARESResourceSelectionDialog;
import com.hundsun.ares.studio.ui.dialog.ConditionalResourceSelectionDialog;
import com.hundsun.ares.studio.ui.editor.extend.CheckBoxColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;

/**
 * �⽡�ֶ�ѡ�� Dialog
 * <br>
 * 2014.01.17 sundl ���ֻ�ܹ���������һ�ű�����������Դ˴��޸�ΪֻҪѡ����г�������������Ҫѡ��ֻ�ܲ鿴;
 * ���ڶ���ֶε�������ֶε�˳��Ҳ�������ڴ˴��޸ģ� ��Ҫ�޸ĵĻ���������Լ���ġ��ֶΡ����Դ��޸��ֶε�˳��
 * 
 * @author liaogc
 */
public class ForeignKeyFieldSelectDialog extends StatusDialog {

	private IARESProject project;
	private String refTableName;
	private TableKey constraint;
	private TableResourceData refTableData;
	private JresARESResourceAssistantAndSelectionField field;
	private TableViewer tableViewer;
	
	private List<ForeignKey> result = new ArrayList<ForeignKey>();

	/**
	 * 
	 * @param parentShell
	 * @param project 
	 * @param constraint ��ǰ���ڱ༭��Լ��
	 */
	public ForeignKeyFieldSelectDialog(Shell parentShell, IARESProject project, TableKey constraint) {
		super(parentShell);
		this.project = project;
		this.constraint = constraint;
		setHelpAvailable(false);
		if (constraint.getForeignKey().size() > 0) {
			ForeignKey key = constraint.getForeignKey().get(0);
			refTableName = key.getTableName();
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("ѡ������ֶ�");
		newShell.setImage(DatabaseUI.image("table.gif"));
	}
	
	protected boolean isResizable() {
		return true;
	}

	// ��д���������Ϊ�������н�����ɴ����Ժ����һ����֤�� ��Ǳ��裬��Ҫ��д�������
	public void create() {
		super.create();
		validate();
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(3, true));
		createTopArea(composite);
		createCenterArea(composite);

		
		String fullyQualifiedName = field.getControl().getText();
		if (StringUtils.isNotBlank(fullyQualifiedName)) {
			this.updateInput(fullyQualifiedName);
		}
		
		GridDataFactory.swtDefaults().hint(500, 300).grab(true, true).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(this.field.getControl());
		GridDataFactory.fillDefaults().hint(-1, 250).span(3, 1).grab(true, false).applyTo(tableViewer.getTable());
		return composite;
	}

	private Composite createTopArea(Composite composite) {
		field = new JresARESResourceAssistantAndSelectionField(composite, "��·��", SWT.BORDER, "table", "�����Դѡ���") {
			@Override
			protected IARESProject getARESProject() {
				return project;
			}

//			@Override
//			protected void okPressed(IARESResource result) {
//				super.okPressed(result);
//				updateInput(result.getFullyQualifiedName());
//			}

			@Override
			protected ARESResourceSelectionDialog createDialog(IARESProject project, String resType) {
				return new ConditionalResourceSelectionDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),	project, resType) {

					@Override
					public String[] getFilterExceptResources() {
						return new String[] { refTableName == null ? StringUtils.EMPTY : refTableName + "." + IDatabaseResType.Table };
					}

					@Override
					public IARESResource[] getResources() {
						return ARESElementUtil.getAllResourceFromRefInType(project, resourceTypes);
					}

					@Override
					protected void fillContentProvider(
							AbstractContentProvider contentProvider,
							ItemsFilter itemsFilter,
							IProgressMonitor progressMonitor)
							throws CoreException {
						IARESResource _resources[] = getResources();
						progressMonitor.beginTask("������Դ��" + resourceTypes, _resources.length);
						for (IARESResource resource : _resources) {
							if (!resource.getFullyQualifiedName().equals(refTableName)) {
								contentProvider.add(resource, itemsFilter);
								progressMonitor.worked(1);
							}
						}
						progressMonitor.done();
					}
				};
			}

		};

		if (StringUtils.isNotBlank(this.refTableName)) {
			this.field.getControl().setText(this.refTableName);
		}
		
		field.getControl().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateInput(field.getControl().getText());
				validate();
			}
		});
		return composite;
	}

	private void createCenterArea(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("�Զ�ƥ�������ֶ�:");
		GridDataFactory.fillDefaults().applyTo(label);
		
		tableViewer = new TableViewer(parent, SWT.BORDER
				| SWT.FULL_SELECTION
				| SWT.V_SCROLL | SWT.H_SCROLL);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);

		//tableViewer.setContentProvider(new ReferenceContentProvider(DatabasePackage.Literals.TABLE_RESOURCE_DATA__COLUMNS));
		tableViewer.setContentProvider(new ArrayContentProvider());
		// �ֶ���
		{
			EAttribute attribute = DatabasePackage.Literals.TABLE_COLUMN__FIELD_NAME;

			final TableViewerColumn tvColumn = new TableViewerColumn(
					tableViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(150);
			tvColumn.getColumn().setText("�ֶ���");

			EObjectColumnLabelProvider provider = new EObjectColumnLabelProvider(
					attribute);
			tvColumn.setLabelProvider(provider);
		}

		// ������
		{
			final TableViewerColumn tvColumn = new TableViewerColumn(
					tableViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(120);
			tvColumn.getColumn().setText("������");

			TableColumnRefLabelProvider provider = new TableColumnRefLabelProvider(
					project,
					TableColumnRefLabelProvider.TYPE.ChineseName);
			tvColumn.setLabelProvider(provider);
		}
		// �ֶ�����
		{
			final TableViewerColumn tvColumn = new TableViewerColumn(
					tableViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(120);
			tvColumn.getColumn().setText("�ֶ�����");

			TableColumnRefLabelProvider provider = new TableColumnRefLabelProvider(
					project,
					TableColumnRefLabelProvider.TYPE.Type);
			tvColumn.setLabelProvider(provider);
		}
		// �����
		{
			EAttribute attribute = DatabasePackage.Literals.TABLE_COLUMN__NULLABLE;

			final TableViewerColumn tvColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
			tvColumn.getColumn().setWidth(120);
			tvColumn.getColumn().setText("�����");

			CheckBoxColumnLabelProvider provider = new CheckBoxColumnLabelProvider(	attribute , null);
			tvColumn.setLabelProvider(provider);
		}

	}

	private void updateInput(String tableName) {
		this.refTableName = tableName;
		IARESBundle[] bundles = ARESElementUtil.getRefARESProjects(project);
		result.clear();
		tableViewer.setInput(new Object[0]);
		for (IARESBundle bundle : bundles) {
			IARESResource resource;
			try {
				resource = bundle.findResource(tableName, IDatabaseResType.Table);
				if (resource != null) {
					this.refTableData = resource.getInfo(TableResourceData.class);
					if (refTableData != null) {
						TableKey key = DatabaseUtil.findPrimaryKey(refTableData, constraint.getMark());
						if (key == null) {
							return;
						}
						List<TableColumn> columns = key.getColumns();
						tableViewer.setInput(columns);
						for (TableColumn col : columns) {
							ForeignKey k = DatabaseFactory.eINSTANCE.createForeignKey();
							k.setTableName(tableName);
							k.setFieldName(col.getName());
							result.add(k);
						}
					}
				}
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
//			boolean confirm = MessageDialog.openQuestion(getShell(), "������һ��", 
//					String.format("��ǰ�ֶ�������%1$d�������������%2$d����һ�£��Ƿ������",colSize,currSelectedSize));
//			if(confirm){
//				super.okPressed();
//			}else{
//				return;
//			}
		super.okPressed();
	}
	
	/**
	 * ��ȡѡ��ı����Զ�ƥ���������
	 * @return
	 */
	public List<ForeignKey> getResult() {
		return result;
	}
	
	private void validate() {
		if (StringUtils.isEmpty(refTableName)) {
			setErrorMessage("��ѡ�������Ӧ�ı�");
			return;
		}
		String shortName = StringUtils.substringAfterLast(refTableName, ".");
		if (refTableData == null) {
			setErrorMessage(String.format("ѡ��ı���Դ%s�Ҳ��������߱���Դ�ļ���ȡʧ�ܡ�", shortName));
			return;
		}
		
		// �����õı�����������������ı�־λ����͵�ǰ��Լ���ı�־λ��ͬ
		String mark = constraint.getMark();
		TableKey refTablePk = DatabaseUtil.findPrimaryKey(refTableData, constraint.getMark());
		if (refTablePk == null) {
			setErrorMessage(String.format("�Զ�ƥ������ʧ�ܣ��ڱ�%s��û���ҵ���־λΪ%s��������", shortName, 
											StringUtils.isEmpty(mark) ? "��" : "(" + mark + ")"));
			return;
		}
		
		if (!validateColumns(shortName, refTablePk))
			return;
		
		setErrorMessage(null);
	}

	// ����еĸ����������Ƿ��Ӧ
	private boolean validateColumns(String shortName, TableKey refTablePk) {
		List<TableColumn> columns = constraint.getColumns();
		List<TableColumn> refColumns = refTablePk.getColumns();
		if (columns.size() != refColumns.size()) {
			setErrorMessage(String.format("��ǰԼ�����ֶκͱ�%s�е������ֶθ�����һ�¡�", shortName));
			return false;
		}
		
		int length = columns.size();
		for (int i = 0; i < length; i++) {
			TableColumn col1 = columns.get(i);
			TableColumn col2 = refColumns.get(i);
			String bizType1 = DatabaseUtil.getBizType(project, col1);
			String bizType2 = DatabaseUtil.getBizType(project, col2);
			// ���ҵ���������Ͳ�һ��
			if (!StringUtils.equals(bizType1, bizType2)) {
				setErrorMessage(String.format("��%s���ֶε��������Ͳ�ƥ��:%s(%s) -- %s(%s)", i+1, col1.getName(), bizType1, col2.getName(), bizType2));
				return false;
			}
		}
		
		return true;
	}
	
	private void setErrorMessage(String msg) {
		if (msg == null) {
			updateStatus(Status.OK_STATUS);
		} else {
			updateStatus(new Status(Status.ERROR, DatabaseUI.PLUGIN_ID, msg));
		}
	}
	
//	private boolean validateType(int index,StringBuilder errorMessage) {
//		String id = getBusinessId(sourceTableKey.getColumns().get(index));
//		String id2 = getBusinessId(currSelectedFields.get(index));
//		IMetadataService metadataService = getMetadataService(currTableResource.getARESProject());
//		IBusinessDataType type1 = metadataService.getBusinessDataType(id);
//		IBusinessDataType type2 = metadataService.getBusinessDataType(id2);
//		if (type1 != null && type2 != null && StringUtils.equals(type1.getName(), type2.getName())) {
//			return true;
//		} else if (type1 == null && type2 == null) {
//			// �����ֶζ�Ӧ��ҵ���������Ͷ�Ϊ�յĻ��޷����
//			return true;
//		} else {
//			String stdType1 = type1 == null ? StringUtils.EMPTY : type1.getStdType().getName();
//			String stdType2 = type2 == null ? StringUtils.EMPTY : type2.getStdType().getName();
//			String len1 = type1 == null ? StringUtils.EMPTY : type1.getLength();
//			String len2 = type2 == null ? StringUtils.EMPTY : type2.getLength();
//			if(!StringUtils.equals(stdType1, stdType2)){
//				errorMessage.append(String .format("�ڡ�%1$d�����ֶε����͡�%2$s���뵱ǰѡ�е�����ֶ����͡�%3$s����һ��!", index,stdType1,stdType2));	
//				return false;
//			}else if(!StringUtils.equals(len1, len2)){
//				errorMessage.append(String .format("�ڡ�%1$d�����ֶ����ͳ��ȡ�%2$s���뵱ǰѡ�е�����ֶ����ͳ��ȡ�%3$s����һ��!!", index,len1,len2));
//				return false;
//			}
//		}
//		return true;
//
//	}

}
