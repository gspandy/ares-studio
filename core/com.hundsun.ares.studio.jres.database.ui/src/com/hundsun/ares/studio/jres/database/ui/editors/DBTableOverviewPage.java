/**
 * Դ�������ƣ�DBTableOverviewPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.ui.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.google.common.collect.Collections2;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.model.util.RevisionHistoryUtil;
import com.hundsun.ares.studio.core.util.ARESElementUtil;
import com.hundsun.ares.studio.jres.database.oracle.constant.IOracleConstant;
import com.hundsun.ares.studio.jres.database.oracle.constant.IOracleRefType;
import com.hundsun.ares.studio.jres.database.ui.extend.ModuleDatabasePropertyPage;
import com.hundsun.ares.studio.jres.database.utils.IRevHistoryVersionCompartor;
import com.hundsun.ares.studio.jres.model.chouse.ChousePackage;
import com.hundsun.ares.studio.jres.model.chouse.TableBaseProperty;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.jres.model.database.oracle.DatabaseModuleExtensibleProperty;
import com.hundsun.ares.studio.jres.model.database.oracle.OraclePackage;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpaceRelation;
import com.hundsun.ares.studio.jres.model.database.oracle.table_type;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.assist.IAssistantProvider;
import com.hundsun.ares.studio.ui.assist.TextContentAssistEx;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.DataBindingFormPage;
import com.hundsun.ares.studio.ui.editor.blocks.FormWidgetUtils;
import com.hundsun.ares.studio.ui.editor.editable.JresDefaultEditableUnit;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelComposite;

/**
 * @author gongyf
 *
 */
public class DBTableOverviewPage extends DataBindingFormPage {
	
	private ExtensibleModelComposite emc;
	private Text txtObjectId;
	private Text txtEnglishName;
	private Text txtChineseName;
	private Combo comboTableType;
	private Text txtDescription;
	private Button his ;
	private Button clear;
	private Button re;
	private Combo comboCurTable;
	private Text txtCurIndex;
	private Text txtHisTable;
	private Text txtHisIndex;
	private Text txtFileTS;
	private Text txtFileIndexTS;
	private Text txtReTS;
	private Text txtClearTS;
	private Text txtClearIndexTS;
	private Text txtSeqField;
	private Text txtSeqNum;
	private Text txtStartYM;
	private Button cusSeqTable;
	
	private static final String EXPRESS = "������Ϣȡ��ģ��  �����ֶ�:%1$s  ����������%2$s  ����ʱ�䣺%3$s";
	
	public static final String TABLE_BASE_KEY = "chouse";

	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public DBTableOverviewPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
		
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#doCreateFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void doCreateFormContent(IManagedForm managedForm) {
		Composite composite = managedForm.getForm().getBody();
		FormToolkit toolkit = managedForm.getToolkit();
		
		managedForm.getForm().setText(getTitle());
		toolkit.decorateFormHeading(managedForm.getForm().getForm());
		
		createBaseInfoSection(composite, toolkit);
		Section extendSection = createExtendedInfoSection(composite, toolkit);
		
		GridLayoutFactory.swtDefaults().applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(extendSection);
		
		composite.getParent().layout();
	}

	/**
	 * ����������Ϣҳ
	 * @param parent
	 * @param toolkit
	 * @return
	 */
	protected void createBaseInfoSection(Composite parent, FormToolkit toolkit) {
		Composite plCom = toolkit.createComposite(parent);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(plCom);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(plCom);
		{
			{
				Section section = toolkit.createSection(plCom, FormWidgetUtils.getDefaultSectionStyles());
				section.setText("������Ϣ");
				
				// �����ؼ�
				Composite client = toolkit.createComposite(section);
				
				Label lblName = toolkit.createLabel(client, "����ţ�");
				txtObjectId = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
				
				Label lblEnglishName = toolkit.createLabel(client, "Ӣ������");
				txtEnglishName = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles()|SWT.READ_ONLY);
				txtEnglishName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
				
				Label lblChineseName = toolkit.createLabel(client, "��������");
				txtChineseName = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
				
				Label lblTableType = toolkit.createLabel(client, "�����ͣ�");
				comboTableType = new Combo(client, SWT.DROP_DOWN | SWT.READ_ONLY);
				
				Label lblVersion = toolkit.createLabel(client, "�汾�ţ�");
				Text texVersion = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles()|SWT.READ_ONLY);
				texVersion.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
				
				Label lblUpdate = toolkit.createLabel(client, "����ʱ�䣺");
				Text texUpdate = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles()|SWT.READ_ONLY);
				texUpdate.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
				
				{
					//�ҳ����µİ汾��
					EObject obj = getEditor().getInfo();
					if (obj instanceof TableResourceData) {
						List<RevisionHistory> hises = ((TableResourceData) obj).getHistories();
						List<RevisionHistory> tempHis = (List<RevisionHistory>) EcoreUtil.copyAll(hises);
						Collections.sort(tempHis, new IRevHistoryVersionCompartor());
						if (hises.size() > 0) {
							texVersion.setText(tempHis.get(0).getVersion());
							texUpdate.setText(tempHis.get(0).getModifiedDate());
						}else {
							//2013��5��24��14:43:41 ���û���޸ļ�¼��Ϣ����ȡ������ϵͳ��ǰ�汾��+1
							IARESResource aresResource = getEditor().getARESResource();
							
							IARESModule topModule = null; 
							if (aresResource == null) {
								topModule = null; 
							} else {
								String rootType = aresResource.getRoot().getType(); 
								if (ARESElementUtil.isDatabaseRoot(rootType)) {
									topModule = ARESElementUtil.getTopModule(aresResource);
								} else if (ARESElementUtil.isMetadataRoot(rootType)) {
									// topModuleΪnull��Ч�����ǲ�����ģ��
									topModule = null;
								} else {
									topModule = aresResource.getModule();
								}
							}
							
							// ��ǰ�Ѿ��������Դ�е����汾
							RevisionHistory his = RevisionHistoryUtil.getMaxVersionHisInfo(topModule);
							if (his != null) {
								String currentVersion = his.getVersion();
								// ��Ŀ����
								String projectVersion = RevisionHistoryUtil.getProjectPropertyVersion(aresResource.getARESProject());
								
								// ������3�����ֵ
								String versionStr = RevisionHistoryUtil.max(Arrays.asList(currentVersion, projectVersion));
								// ��һ���Ҳ����κμ�¼��ʱ��
								if (StringUtils.equals(currentVersion, versionStr)) {
									texUpdate.setText(his.getModifiedDate());
								}
								if (StringUtils.isEmpty(versionStr)) {
									versionStr = "1.0.0.0";
								} 
								try{
									texVersion.setText(RevisionHistoryUtil.increase(versionStr));
									
								}catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				
				Label lblDescription = toolkit.createLabel(client, "˵����");
				txtDescription = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultMultiLinesTextStyles());
				
				// ֻ������
				getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtObjectId));
				getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtChineseName));
				getEditableControl().addEditableUnit(new JresDefaultEditableUnit(comboTableType));
				getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtDescription));
				
				// ����
				GridDataFactory.fillDefaults().span(1, 1).grab(true, true).applyTo(section);
				GridLayoutFactory.swtDefaults().numColumns(4).applyTo(client);
				GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblName);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(txtObjectId);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblEnglishName);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(txtEnglishName);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblChineseName);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(txtChineseName);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblTableType);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(comboTableType);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblVersion);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(texVersion);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblUpdate);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(texUpdate);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblDescription);
				GridDataFactory.fillDefaults().span(3, 1).grab(true, true).hint(10, 50).span(3, 1).applyTo(txtDescription);
				
				section.setClient(client);
			}
			{
				Section section = toolkit.createSection(plCom, FormWidgetUtils.getDefaultSectionStyles());
				section.setText("���ݿ�(��ռ�)");
				
				// �����ؼ�
				final Composite client = toolkit.createComposite(section);
				Label lblCurTable = toolkit.createLabel(client, "��ǰ��");
				comboCurTable = new Combo(client, SWT.DROP_DOWN | SWT.BORDER);
				{
					List<String> items = new ArrayList<String>();
					List<ReferenceInfo> refs = ReferenceManager.getInstance().getReferenceInfos(getEditor().getARESResource().getARESProject(),IOracleRefType.Space , true);
					for(ReferenceInfo info : refs){
						items.add(info.getRefName());
					}
//					Collections.sort(items);//����
					Collections.sort(items, new Comparator<String>() {

						@Override
						public int compare(String o1, String o2) {
							return o1.toUpperCase().compareTo(o2.toUpperCase());//�����ִ�Сд
						}
					});
					comboCurTable.setItems(items.toArray(new String[0]));
					
					comboCurTable.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							ReferenceInfo info = ReferenceManager.getInstance().getFirstReferenceInfo(getEditor().getARESResource().getARESProject(), IOracleRefType.Space, comboCurTable.getText(), true);
							if (info != null) {
								txtCurIndex.setText(getSpaceText(OraclePackage.Literals.TABLE_SPACE_RELATION__INDEX_SPACE));
								txtHisTable.setText(getSpaceText(ChousePackage.Literals.TABLE_SPACE_RELATION_PROPERTY__HIS_SPACE));
								txtHisIndex.setText(getSpaceText(ChousePackage.Literals.TABLE_SPACE_RELATION_PROPERTY__HIS_INDEX_SPACE));
								txtFileTS.setText(getSpaceText(ChousePackage.Literals.TABLE_SPACE_RELATION_PROPERTY__FILE_SPACE));
								txtFileIndexTS.setText(getSpaceText(ChousePackage.Literals.TABLE_SPACE_RELATION_PROPERTY__FILE_INDEX_SPACE));
								txtReTS.setText(getSpaceText(ChousePackage.Literals.TABLE_SPACE_PROPERTY__REDU_TABLE));
								txtClearTS.setText(getSpaceText(ChousePackage.Literals.TABLE_SPACE_PROPERTY__CHEAR_TABLE));
								txtClearIndexTS.setText(getSpaceText(ChousePackage.Literals.TABLE_SPACE_PROPERTY__CHEAR_TABLE_INDEX));
							}else {
								txtCurIndex.setText(StringUtils.EMPTY);
								txtHisTable.setText(StringUtils.EMPTY);
								txtHisIndex.setText(StringUtils.EMPTY);
								txtFileTS.setText(StringUtils.EMPTY);
								txtFileIndexTS.setText(StringUtils.EMPTY);
								txtReTS.setText(StringUtils.EMPTY);
								txtClearTS.setText(StringUtils.EMPTY);
								txtClearIndexTS.setText(StringUtils.EMPTY);
							}
						}
					});
					
				}
				
				Label lblCurIndex = toolkit.createLabel(client, "��ǰ������");
				txtCurIndex = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles()|SWT.READ_ONLY);
				txtCurIndex.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
				
				Label lblHisTable = toolkit.createLabel(client, "��ʷ��");
				txtHisTable = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles()|SWT.READ_ONLY);
				txtHisTable.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
				
				Label lblHisIndex = toolkit.createLabel(client, "��ʷ������");
				txtHisIndex = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles()|SWT.READ_ONLY);
				txtHisIndex.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
				
				Label lblFileTS = toolkit.createLabel(client, "�鵵��");
				txtFileTS = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles()|SWT.READ_ONLY);
				txtFileTS.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
				
				Label lblFileIndexTS = toolkit.createLabel(client, "�鵵������");
				txtFileIndexTS = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles()|SWT.READ_ONLY);
				txtFileIndexTS.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
				
				Label lblReTS = toolkit.createLabel(client, "���ࣺ");
				txtReTS = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles()|SWT.READ_ONLY);
				txtReTS.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
				
				Label lblClearTS = toolkit.createLabel(client, "���㣺");
				txtClearTS = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles()|SWT.READ_ONLY);
				txtClearTS.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
				
				Label lblClearIndexTS = toolkit.createLabel(client, "����������");
				txtClearIndexTS = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles()|SWT.READ_ONLY);
				txtClearIndexTS.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
				
				// ֻ������
				getEditableControl().addEditableUnit(new JresDefaultEditableUnit(comboCurTable));
				
				// ����
				GridDataFactory.fillDefaults().span(1, 1).grab(true, true).applyTo(section);
				GridLayoutFactory.swtDefaults().numColumns(4).applyTo(client);
				GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblCurTable);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(comboCurTable);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblCurIndex);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(txtCurIndex);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblHisTable);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(txtHisTable);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblHisIndex);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(txtHisIndex);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblFileTS);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(txtFileTS);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblFileIndexTS);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(txtFileIndexTS);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblReTS);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(txtReTS);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblClearTS);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(txtClearTS);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblClearIndexTS);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(txtClearIndexTS);
				section.setClient(client);
			}
			
		}
		{
			{
				
				Section section = toolkit.createSection(plCom, FormWidgetUtils.getDefaultSectionStyles());
				section.setText("������");
				
				Composite client = toolkit.createComposite(section);
				
				his = toolkit.createButton(client, "������ʷ��", SWT.CHECK);
				clear = toolkit.createButton(client, "���������", SWT.CHECK);
				re = toolkit.createButton(client, "���������", SWT.CHECK);
				re.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						if (re.getSelection() && !his.getSelection()) {
							his.setSelection(re.getSelection());
							Command command = new SetCommand(getEditingDomain(), getInfo().getData2().get(TABLE_BASE_KEY), ChousePackage.Literals.TABLE_BASE_PROPERTY__HISTORY, true);
							getEditingDomain().getCommandStack().execute(command);
						}
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});
				
				// ֻ������
				getEditableControl().addEditableUnit(new JresDefaultEditableUnit(his));
				getEditableControl().addEditableUnit(new JresDefaultEditableUnit(clear));
				getEditableControl().addEditableUnit(new JresDefaultEditableUnit(re));
				
				GridDataFactory.fillDefaults().span(1, 1).grab(true, true).applyTo(section);
				GridLayoutFactory.swtDefaults().numColumns(1).applyTo(client);
				GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(his);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(clear);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(re);
				section.setClient(client);
			}
			{
				Section section = toolkit.createSection(plCom, FormWidgetUtils.getDefaultSectionStyles());
				section.setText("������Ϣ");
				
				Composite client = toolkit.createComposite(section);
				
				Label lblSeqField = toolkit.createLabel(client, "�����ֶΣ�");
				{
					txtSeqField = new TextContentAssistEx(client, SWT.BORDER, new IAssistantProvider() {
						
						TableResourceData tableInfo = (TableResourceData)getInfo();
						
						@Override
						public Object[] getProposals() {
							List<String> columns = new ArrayList<String>();
							for(TableColumn column : tableInfo.getColumns()){
								columns.add(column.getFieldName());
							}
							return columns.toArray(new String[0]);
						}
						
						@Override
						public String getLabel(Object obj) {
							return obj.toString();
						}
						
						@Override
						public String getDescription(Object obj) {
							return null;
						}
						
						@Override
						public String getContent(Object obj) {
							return getLabel(obj);
						}
					});
				}
				
				Label lblSeqNum = toolkit.createLabel(client, "����������");
				txtSeqNum = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
				
				Label lblStartYM = toolkit.createLabel(client, "��ʼ���ڣ�");
				txtStartYM = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
				
				cusSeqTable = toolkit.createButton(client, "�Ƿ��Զ�������", SWT.CHECK);
				
				final Label moduleSplit = toolkit.createLabel(client, "");
				moduleSplit.setVisible(false);
				EObject object = getInfo().getData2().get(TABLE_BASE_KEY);
				if (object instanceof TableBaseProperty && !((TableBaseProperty)(object)).isUserSplit()) {
					moduleSplit.setText(getModuleConfig(getEditor().getARESResource()));
					moduleSplit.setVisible(true);
					txtSeqField.setEditable(false);
					txtSeqNum.setEditable(false);
					txtStartYM.setEditable(false);
				}
				cusSeqTable.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						if (!cusSeqTable.getSelection()) {
							moduleSplit.setVisible(true);
							txtSeqField.setEditable(false);
							txtSeqNum.setEditable(false);
							txtStartYM.setEditable(false);
						}else {
							moduleSplit.setVisible(false);
							txtSeqField.setEditable(true);
							txtSeqNum.setEditable(true);
							txtStartYM.setEditable(true);
							moduleSplit.setText(getModuleConfig(getEditor().getARESResource()));
						}
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						
					}
				});
				
				// ֻ������
				getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtSeqField));
				getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtSeqNum));
				getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtStartYM));
				getEditableControl().addEditableUnit(new JresDefaultEditableUnit(cusSeqTable));
				
				GridDataFactory.fillDefaults().span(1, 1).grab(true, true).applyTo(section);
				GridLayoutFactory.swtDefaults().numColumns(4).applyTo(client);
				GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblSeqField);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(txtSeqField);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblSeqNum);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(txtSeqNum);
				GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(lblStartYM);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(txtStartYM);
				GridDataFactory.fillDefaults().grab(false, false).span(2, 1).applyTo(cusSeqTable);
				GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).span(3, 1).applyTo(moduleSplit);
				section.setClient(client);
			}
		}
	}

	private String getModuleConfig(IARESResource resource){
		
		List<String> values = new ArrayList<String>();
		IARESModule module = resource.getModule();
		while (module != null) {
			IARESResource mr = module.getARESResource(IARESModule.MODULE_PROPERTY_FILE);
			try {
				if (mr != null && mr.exists()) {
					ModuleProperty mp = mr.getInfo(ModuleProperty.class);
					if (mp != null && mp.getMap().get(ModuleDatabasePropertyPage.KEY) instanceof DatabaseModuleExtensibleProperty) {
						DatabaseModuleExtensibleProperty mem = (DatabaseModuleExtensibleProperty) mp.getMap().get(ModuleDatabasePropertyPage.KEY);
						if (mem != null) {
							if (StringUtils.isNotBlank(mem.getSplitField())
									&& StringUtils.isNotBlank(mem.getSplitNum())
									&& StringUtils.isNotBlank(mem.getStartDate())) {
								values.add(StringUtils.defaultString(mem.getSplitField()));
								values.add(StringUtils.defaultString(mem.getSplitNum()));
								values.add(StringUtils.defaultString(mem.getStartDate()));
								return String.format(EXPRESS, values.toArray());
							}
						}
					}
				}
				module = module.getParentModule();
			} catch (ARESModelException e) {
				e.printStackTrace();
				break;
			}
		}
		return StringUtils.EMPTY;
	}
	
	@Override
	public void infoChange() {
		emc.setInput(getEditor().getARESResource(), getInfo());
		super.infoChange();
	}
	
	@Override
	public void commandStackChanged(EventObject event) {
		super.commandStackChanged(event);
		emc.refresh();
	}
	
	protected Section createExtendedInfoSection(Composite parent, FormToolkit toolkit) {
		Section section = toolkit.createSection(parent, FormWidgetUtils.getDefaultSectionStyles());
		section.setText("��չ��Ϣ");
		
		emc = new ExtensibleModelComposite(section, toolkit);
		emc.setProblemPool(getProblemPool());
//		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(emc));
		
		section.setClient(emc);
		return section;
	}
	
	@Override
	protected void doDataBingingOnControls() {
		//��������
		bingText(txtObjectId, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__OBJECT_ID);
		bingText(txtEnglishName, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__NAME);
		bingText(txtChineseName, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME);
		bingCombo(comboTableType, new table_type[]{table_type.COMMON,table_type.TEMP_NO_VALUE,table_type.TEMP_WITH_VALUE} , new LabelProvider(){
			@Override
			public String getText(Object element) {
				table_type type = (table_type)element;
				if(type.getValue() == table_type.COMMON_VALUE) {
					return "һ���";
				}else if(type.getValue() == table_type.TEMP_NO_VALUE_VALUE){
					return "��ʱ��(����������)";
				}else if(type.getValue() == table_type.TEMP_WITH_VALUE_VALUE) {
					return "��ʱ��(��������)";
				}
				
				return super.getText(element);
			}
		}, getInfo().getData2().get(IOracleConstant.TABLE_DATA2_KEY), OraclePackage.Literals.ORACLE_TABLE_PROPERTY__TABLETYPE);
		bingText(txtDescription, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__DESCRIPTION);
		//���ݿ�
		bingSelection(comboCurTable, getInfo().getData2().get(IOracleConstant.TABLE_DATA2_KEY),OraclePackage.Literals.ORACLE_TABLE_PROPERTY__SPACE );
		//������
		bingSelection(his, getInfo().getData2().get(TABLE_BASE_KEY), ChousePackage.Literals.TABLE_BASE_PROPERTY__HISTORY);
		bingSelection(clear, getInfo().getData2().get(TABLE_BASE_KEY), ChousePackage.Literals.TABLE_BASE_PROPERTY__IS_CLEAR);
		bingSelection(re, getInfo().getData2().get(TABLE_BASE_KEY), ChousePackage.Literals.TABLE_BASE_PROPERTY__IS_REDU);
		//������Ϣ
		bingText(txtSeqField, getInfo().getData2().get(TABLE_BASE_KEY),ChousePackage.Literals.TABLE_BASE_PROPERTY__SPLIT_FIELD);
		bingText(txtSeqNum, getInfo().getData2().get(TABLE_BASE_KEY),ChousePackage.Literals.TABLE_BASE_PROPERTY__SPLIT_NUM);
		bingText(txtStartYM, getInfo().getData2().get(TABLE_BASE_KEY),ChousePackage.Literals.TABLE_BASE_PROPERTY__START_DATA );
		bingSelection(cusSeqTable, getInfo().getData2().get(TABLE_BASE_KEY), ChousePackage.Literals.TABLE_BASE_PROPERTY__USER_SPLIT);
	}
	
	private String getSpaceText( EStructuralFeature feature) {
		String space = StringUtils.defaultString(comboCurTable.getText());
		ReferenceInfo info = ReferenceManager.getInstance().getFirstReferenceInfo(getEditor().getARESResource().getARESProject(),IOracleRefType.SpaceRelation ,space, true);
		if(info != null){
			TableSpaceRelation obj = (TableSpaceRelation) info.getObject();
			if (feature == OraclePackage.Literals.TABLE_SPACE_RELATION__INDEX_SPACE) {
				return obj.getIndexSpace();
			}
			if (feature == OraclePackage.Literals.TABLE_SPACE_RELATION__MAIN_SPACE) {
				return obj.getMainSpace();
			}
			if (obj != null) {
				for(Iterator<String> it = obj.getData2().keySet().iterator();it.hasNext();){
					String key = it.next();
					if (obj.getData2().get(key).eClass().getEAllStructuralFeatures().contains(feature)) {
						Object o = obj.getData2().get(key).eGet(feature);
						return o == null ? StringUtils.EMPTY : o.toString();
					}
				}
			}
		}
		return StringUtils.EMPTY;
	}
	
}
