/**
 * Դ�������ƣ�SequenceInfoPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.sequence.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.oracle.ui.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.model.util.RevisionHistoryUtil;
import com.hundsun.ares.studio.core.util.ARESElementUtil;
import com.hundsun.ares.studio.jres.database.oracle.constant.IOracleConstant;
import com.hundsun.ares.studio.jres.database.oracle.constant.IOracleRefType;
import com.hundsun.ares.studio.jres.database.utils.IRevHistoryVersionCompartor;
import com.hundsun.ares.studio.jres.model.database.oracle.OraclePackage;
import com.hundsun.ares.studio.jres.model.database.oracle.SequenceResourceData;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.editor.blocks.DataBindingFormPage;
import com.hundsun.ares.studio.ui.editor.blocks.FormWidgetUtils;
import com.hundsun.ares.studio.ui.editor.editable.IEditableControl;
import com.hundsun.ares.studio.ui.editor.editable.JresDefaultEditableUnit;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelComposite;

/**
 * @author wangbin
 *
 */
public class SequenceInfoPage extends DataBindingFormPage {
	
	private ExtensibleModelComposite emc;
	private Text txtObjectID;
	
	/**
	 * ������
	 */
	private Text txtChineseName;
	
	/**
	 * Ӣ����
	 */
	private Text txtSequenceName;
	
	/**
	 * ����
	 */
	private Text txtInCreMent;
	
	/**
	 * ���ֵ
	 */
	private Text txtMaxValue;
	
	/**
	 * ��Сֵ
	 */
	private Text txtMinValue;
	
	/**
	 * ��ʼֵ
	 */
	private Text txtStart;
	
	/**
	 * �Ƿ�ѭ��
	 */
	private Combo cmbCycle;
	
	/**
	 * ����
	 */
	private Text txtCache;
	
	/**
	 * �Ƿ񻺴�
	 */
	private Combo cmbIsCache;
	
	/**
	 * �Ƿ������ʷ��
	 */
	private Button txtIsHis;
	
	/**
	 * ˵��
	 */
	private Text txDescription;
	
	/**
	 * ��ռ�
	 */
	private Combo comboCurTable;
	
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public SequenceInfoPage(SequenceEMFFormEditor sequenceEditor, String id, String title) {
		super(sequenceEditor, id, title);
	}
	
	IEditableControl controler;
	Section basicSection;
	
	protected List<Object> getTableList(String refType){

		List<Object> tableList = new ArrayList<Object>();
		
		IARESProject pro = getEditor().getARESResource().getARESProject();
		
		List<ReferenceInfo> infoList = ReferenceManager.getInstance().getReferenceInfos(pro, refType, true);
		
		for(ReferenceInfo referenceInfo : infoList){
			IARESResource owner = referenceInfo.getResource();
			tableList.add(owner.getName());
		}
		
		return tableList;
	}
	
	@Override
	protected void doCreateFormContent(IManagedForm managedForm) {

		final Composite parent = managedForm.getForm().getBody();
		FormToolkit toolkit = managedForm.getToolkit();
		
		managedForm.getForm().setText(getTitle());
		toolkit.decorateFormHeading(managedForm.getForm().getForm());
		
		basicSection = toolkit.createSection(parent,  FormWidgetUtils.getDefaultSectionStyles());
		basicSection.setText("������Ϣ");
		
		Composite composite = toolkit.createComposite(basicSection,SWT.NONE);
		basicSection.setClient(composite);
		
		composite.setLayout(new GridLayout(6, false));
		
		Label lblObjectID = toolkit.createLabel(composite, "����ţ�");
		txtObjectID = toolkit.createText(composite, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
		
		//������
		Label lbSequenceName = new Label(composite, SWT.NONE);
		lbSequenceName.setText("����");
		txtSequenceName = new Text(composite, SWT.BORDER);
		txtSequenceName.setEditable(false);
		
		Label lbChineseName = new Label(composite, SWT.NONE);
		lbChineseName.setText("������");
		txtChineseName = new Text(composite, SWT.BORDER);
		
		Label lblVersion = toolkit.createLabel(composite, "�汾�ţ�");
		Text texVersion = toolkit.createText(composite, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles()|SWT.READ_ONLY);
		texVersion.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		
		Label lblUpdate = toolkit.createLabel(composite, "����ʱ�䣺");
		Text texUpdate = toolkit.createText(composite, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles()|SWT.READ_ONLY);
		texUpdate.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		
		{
			//�ҳ����µİ汾��
			EObject obj = getEditor().getInfo();
			if (obj instanceof SequenceResourceData) {
				List<RevisionHistory> hises = ((SequenceResourceData) obj).getHistories();
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
		
		
		//����
		Label lbInCreMent = new Label(composite, SWT.NONE);
		lbInCreMent.setText("����");
		txtInCreMent = new Text(composite, SWT.BORDER);
		
		//���ֵ
		Label lbMaxValue = new Label(composite, SWT.NONE);
		lbMaxValue.setText("���ֵ");
		txtMaxValue = new Text(composite, SWT.BORDER);
		
		//��Сֵ
		Label lbMinValue = new Label(composite, SWT.NONE);
		lbMinValue.setText("��Сֵ");
		txtMinValue = new Text(composite, SWT.BORDER);
		
		//��ʼֵ
		Label lbStart = new Label(composite, SWT.NONE);
		lbStart.setText("��ʼֵ");
		txtStart = new Text(composite, SWT.BORDER);
		
		//�Ƿ�ѭ��
		Label lbCycle = new Label(composite, SWT.NONE);
		lbCycle.setText("�Ƿ�ѭ��");
		cmbCycle = new Combo(composite, SWT.NONE|SWT.READ_ONLY);
		
		//�Ƿ񻺴�
		Label lbCache = new Label(composite, SWT.NONE);
		lbCache.setText("�����С");
		txtCache = new Text(composite, SWT.BORDER);
		
		//�Ƿ�ѭ��
		Label lbIsCache = new Label(composite, SWT.NONE);
		lbIsCache.setText("�Ƿ񻺴�");
		cmbIsCache = new Combo(composite, SWT.NONE|SWT.READ_ONLY);
		
//		txtIsHis = toolkit.createButton(composite, "������ʷ��",SWT.CHECK);
		Label lbIsHis = new Label(composite, SWT.NONE);
		lbIsHis.setText("������ʷ��");
		txtIsHis = new Button(composite, SWT.CHECK);
		
		Label lblCurTable = toolkit.createLabel(composite, "��ռ䣺");
		comboCurTable = new Combo(composite, SWT.READ_ONLY);
		{
			List<String> items = new ArrayList<String>();
			List<ReferenceInfo> refs = ReferenceManager.getInstance().getReferenceInfos(getEditor().getARESResource().getARESProject(),IOracleRefType.Space , true);
			for(ReferenceInfo info : refs){
				items.add(info.getRefName());
			}
//			Collections.sort(items);//����
			Collections.sort(items, new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					return o1.toUpperCase().compareTo(o2.toUpperCase());//�����ִ�Сд
				}
			});
			comboCurTable.setItems(items.toArray(new String[0]));
		}
		
		//�Ƿ�ѭ��
		Label lbDesc = new Label(composite, SWT.NONE);
		lbDesc.setText("˵��");
		txDescription = toolkit.createText(composite, StringUtils.EMPTY, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		
		GridLayoutFactory.swtDefaults().applyTo(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(basicSection);
		//
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(lbChineseName);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(lbSequenceName);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(lbInCreMent);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(lbMaxValue);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(lbMinValue);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(lbStart);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(lbCycle);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(lbCache);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(lbIsHis);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(lblCurTable);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(lbDesc);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(lblObjectID);
		
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lblVersion);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(texVersion);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lblUpdate);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(texUpdate);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(lblVersion);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(lblUpdate);
		GridDataFactory.swtDefaults().grab(true, false).span(2, 1).align(SWT.FILL, SWT.CENTER).applyTo(texVersion);
		GridDataFactory.swtDefaults().grab(true, false).span(2, 1).align(SWT.FILL, SWT.CENTER).applyTo(texUpdate);
		GridDataFactory.swtDefaults().grab(true, false).span(2, 1).align(SWT.FILL, SWT.CENTER).applyTo(txtObjectID);
		GridDataFactory.swtDefaults().grab(true, false).span(2, 1).align(SWT.FILL, SWT.CENTER).applyTo(txtChineseName);
		GridDataFactory.swtDefaults().grab(true, false).span(2, 1).align(SWT.FILL, SWT.CENTER).applyTo(txtSequenceName);
		GridDataFactory.swtDefaults().grab(true, true).span(2, 1).align(SWT.FILL, SWT.CENTER).applyTo(txtInCreMent);
		GridDataFactory.swtDefaults().grab(true, true).span(2, 1).align(SWT.FILL, SWT.CENTER).applyTo(txtMaxValue);
		GridDataFactory.swtDefaults().grab(true, true).span(2, 1).align(SWT.FILL, SWT.CENTER).applyTo(txtMinValue);
		GridDataFactory.swtDefaults().grab(true, true).span(2, 1).align(SWT.FILL, SWT.CENTER).applyTo(txtStart);
		GridDataFactory.swtDefaults().grab(true, true).span(2, 1).align(SWT.FILL, SWT.CENTER).applyTo(cmbCycle);
		GridDataFactory.swtDefaults().grab(true, true).span(2, 1).align(SWT.FILL, SWT.CENTER).applyTo(txtCache);
		GridDataFactory.swtDefaults().grab(true, true).span(2, 1).align(SWT.FILL, SWT.CENTER).applyTo(cmbIsCache);
		GridDataFactory.swtDefaults().grab(true, true).span(2, 1).align(SWT.FILL, SWT.CENTER).applyTo(txtIsHis);
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).align(SWT.FILL, SWT.CENTER).applyTo(comboCurTable);
		GridDataFactory.swtDefaults().hint(-1, 80).grab(true, true).align(SWT.FILL, SWT.TOP).span(5, 1).applyTo(txDescription);
		
		//ֻ������
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtObjectID));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtChineseName));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtInCreMent));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtMaxValue));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtMinValue));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtStart));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(cmbCycle));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtCache));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(cmbIsCache));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtIsHis));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txDescription));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(comboCurTable));

		Section extendSection = createExtendedInfoSection(parent, toolkit);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(extendSection);
		
		parent.getParent().layout();
		
		if(getInfo() !=null){
			doDataBingingOnControls();
		}
		
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
	public void infoChange() {
		emc.setInput(getEditor().getARESResource(), getInfo());
		super.infoChange();
	}

	@Override
	protected void doDataBingingOnControls() {
		bingSelection(comboCurTable, getInfo().getData2().get(IOracleConstant.SEQUENCE_DATA2_KEY),OraclePackage.Literals.ORACLE_SEQUENCE_PROPERTY__SPACE );
		bingText(txtSequenceName, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__NAME);
		bingText(txtObjectID, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__OBJECT_ID);
		bingSelection(txtIsHis, getInfo(),OraclePackage.Literals.SEQUENCE_RESOURCE_DATA__IS_HISTORY );
		bingText(txDescription, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__DESCRIPTION);
		bingText(txtChineseName, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME);
		bingText(txtCache, getInfo(), OraclePackage.Literals.SEQUENCE_RESOURCE_DATA__CACHE);
		bingText(txtMaxValue, getInfo(), OraclePackage.Literals.SEQUENCE_RESOURCE_DATA__MAX_VALUE);
		bingText(txtMinValue, getInfo(), OraclePackage.Literals.SEQUENCE_RESOURCE_DATA__MIN_VALUE);
		bingText(txtStart, getInfo(), OraclePackage.Literals.SEQUENCE_RESOURCE_DATA__START);
		bingText(txtInCreMent, getInfo(), OraclePackage.Literals.SEQUENCE_RESOURCE_DATA__INCREMENT);
		bingCombo(cmbCycle, new Object[]{Boolean.TRUE, Boolean.FALSE}, new LabelProvider() {
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				if (element instanceof Boolean) {
					if (((Boolean) element).booleanValue()) {
						return "ѭ��";
					} else {
						return "��ѭ��";
					}
				}
				return StringUtils.EMPTY;
			}
		}, getInfo(), OraclePackage.Literals.SEQUENCE_RESOURCE_DATA__CYCLE);
		bingCombo(cmbIsCache, new Object[]{Boolean.TRUE, Boolean.FALSE}, new LabelProvider() {
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				if (element instanceof Boolean) {
					if (((Boolean) element).booleanValue()) {
						return "����";
					} else {
						return "������";
					}
				}
				return StringUtils.EMPTY;
			}
		}, getInfo(), OraclePackage.Literals.SEQUENCE_RESOURCE_DATA__USE_CACHE);
	}
	
}
