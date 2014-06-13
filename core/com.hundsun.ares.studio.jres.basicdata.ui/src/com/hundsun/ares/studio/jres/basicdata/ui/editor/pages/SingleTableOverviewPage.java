/**
 * Դ�������ƣ�DBTableOverviewPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.basicdata.ui.editor.pages;

import java.util.EventObject;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.model.util.RevisionHistoryUtil;
import com.hundsun.ares.studio.jres.basicdata.constant.IBasicDataRestypes;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.BasicdataPackage;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.MasterSlaveLinkTable;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.MasterSlaveTable;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.PackageDefine;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.SingleTable;
import com.hundsun.ares.studio.jres.basicdata.logic.epackage.BasicDataEpackageFactory;
import com.hundsun.ares.studio.jres.basicdata.logic.util.EPackageUtil;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.DataBindingFormPage;
import com.hundsun.ares.studio.ui.editor.blocks.FormWidgetUtils;
import com.hundsun.ares.studio.ui.editor.editable.JresDefaultEditableUnit;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelComposite;
import com.hundsun.ares.studio.ui.util.ARESUIUtil;

/**
 * @author gongyf
 *
 */
public class SingleTableOverviewPage extends DataBindingFormPage {
	
	private Text txtName;
	private Text txtChineseName;
	private Text txtVersionName;
	private Text txtUpdateName;
	private Text txtDescription;
	private Text txtFile;

	private ExtensibleModelComposite emc;

	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public SingleTableOverviewPage(EMFFormEditor editor, String id, String title) {
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
		
		Section baseSection = createBaseInfoSection(composite, toolkit);
		Section basicdataSection = createBasicdataInfoSection(composite,toolkit);
		Section extendedSection = createExtendedInfoSection(composite, toolkit);
		
		GridLayoutFactory.swtDefaults().applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(baseSection);
		if(basicdataSection != null){
			GridDataFactory.fillDefaults().grab(true, false).applyTo(basicdataSection);
		}
		GridDataFactory.fillDefaults().grab(true, false).applyTo(extendedSection);
	}

	/**
	 * ��������������Ϣ
	 * @param composite
	 * @param toolkit
	 * @return
	 */
	private Section createBasicdataInfoSection(Composite parent, FormToolkit toolkit) {
		IARESResource res = getEditor().getARESResource();
		//Ԫ���ݵĻ������ݲ���ʾ��section
		if (!StringUtils.equals(IBasicDataRestypes.singleTable,res.getType())
				&& !StringUtils.equals(IBasicDataRestypes.MasterSlaveTable,res.getType())
				&& !StringUtils.equals(IBasicDataRestypes.MasterSlaveLinkTable,res.getType())){
			return null;
		}
		
		Section section = toolkit.createSection(parent, FormWidgetUtils.getDefaultSectionStyles());
		section.setText("�������Ϣ");
		
		// �����ؼ�
		Composite client = toolkit.createComposite(section);
		
		Label lbType = toolkit.createLabel(client, "�����������ͣ�");
		Text txtType = toolkit.createText(client, StringUtils.EMPTY, SWT.BORDER);
		
		Hyperlink lbMaster = toolkit.createHyperlink(client, "������", SWT.None);
		final Text txtMaster = toolkit.createText(client, StringUtils.EMPTY, SWT.BORDER);
		
		Hyperlink lbSlave = toolkit.createHyperlink(client, "�ӱ�����",SWT.None);
		final Text txtSlave = toolkit.createText(client, StringUtils.EMPTY, SWT.BORDER);
		
		Hyperlink lbLink = toolkit.createHyperlink(client, "��Ϣ������",SWT.None);
		final Text txtLink = toolkit.createText(client, StringUtils.EMPTY, SWT.BORDER);
		
		Label lbFile = toolkit.createLabel(client, "���ɽű��ļ�����");
		txtFile = toolkit.createText(client, StringUtils.EMPTY, SWT.BORDER);
		
		// ֻ������
		txtType.setEditable(false);
		txtType.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		txtMaster.setEditable(false);
		txtMaster.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		txtSlave.setEditable(false);
		txtSlave.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		txtLink.setEditable(false);
		txtLink.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		
		
		// ����
		GridLayoutFactory.swtDefaults().numColumns(4).applyTo(client);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbType);
		GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).applyTo(txtType);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbMaster);
		GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).applyTo(txtMaster);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbSlave);
		GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).applyTo(txtSlave);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbLink);
		GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).applyTo(txtLink);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lbFile);
		GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).applyTo(txtFile);
		
		//���ÿؼ����Ӻ�ֵ
		String type = EPackageUtil.getBasicDataType(res.getARESProject());
		if(StringUtils.isNotBlank(type)){
			String name = EPackageUtil.getEpackageFactoryItemName(type);
			txtType.setText(name);
		}
		PackageDefine define = null;
		try {
			define = BasicDataEpackageFactory.eINSTANCE.getDefine(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(define != null){
			if(define instanceof SingleTable){
				txtMaster.setText(((SingleTable) define).getMaster());
				lbSlave.setVisible(false);
				txtSlave.setVisible(false);
				lbLink.setVisible(false);
				txtLink.setVisible(false);
			}else if(define instanceof MasterSlaveTable){
				lbMaster.setText("��������");
				txtMaster.setText(((MasterSlaveTable) define).getMaster());
				txtSlave.setText(((MasterSlaveTable) define).getSlave());
				lbLink.setVisible(false);
				txtLink.setVisible(false);
			}else if(define instanceof MasterSlaveLinkTable){
				lbMaster.setText("��������");
				txtMaster.setText(((MasterSlaveLinkTable) define).getMaster());
				txtSlave.setText(((MasterSlaveLinkTable) define).getSlave());
				lbLink.setText("����������");
				txtLink.setText(((MasterSlaveLinkTable) define).getLink());
			}
		}
		
		
		section.setClient(client);
		
		//��ת�����
		lbMaster.addHyperlinkListener(new HyperlinkAdapter(){
			@Override
			public void linkActivated(HyperlinkEvent e) {
				linkARESResource(txtMaster.getText());
			}
		});
		
		lbSlave.addHyperlinkListener(new HyperlinkAdapter(){
			@Override
			public void linkActivated(HyperlinkEvent e) {
				linkARESResource(txtSlave.getText());
			}
		});
		
		lbLink.addHyperlinkListener(new HyperlinkAdapter(){
			@Override
			public void linkActivated(HyperlinkEvent e) {
				linkARESResource(txtLink.getText());
			}
		});
		
		return section;
	}
	
	//�򿪱༭��
	private void linkARESResource(String resName) {
		IARESResource res = getEditor().getARESResource();
		ReferenceInfo info = ReferenceManager.getInstance().getFirstReferenceInfo(res.getARESProject(), EPackageUtil.getBasicDataType(res.getARESProject()), resName, true);
		if(info != null ){
			try {
				ARESUIUtil.openEditor(info.getResource());
			} catch (PartInitException e) {
				e.printStackTrace();
			}	
		}
	}

	/**
	 * ����������Ϣҳ
	 * @param parent
	 * @param toolkit
	 * @return
	 */
	protected Section createBaseInfoSection(Composite parent, FormToolkit toolkit) {
		Section section = toolkit.createSection(parent, FormWidgetUtils.getDefaultSectionStyles());
		section.setText("������Ϣ");
		
		// �����ؼ�
		Composite client = toolkit.createComposite(section);
		
		Label lblName = toolkit.createLabel(client, "���ƣ�");
		txtName = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
		
		Label lblChineseName = toolkit.createLabel(client, "��������");
		txtChineseName = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
		
		Label lblVersionName = toolkit.createLabel(client, "�汾�ţ�");
		txtVersionName = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
		
		Label lblUpdateName = toolkit.createLabel(client, "����ʱ�䣺");
		txtUpdateName = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultSingleLineTextStyles());
		
		Label lblDescription = toolkit.createLabel(client, "˵����");
		txtDescription = toolkit.createText(client, StringUtils.EMPTY, FormWidgetUtils.getDefaultMultiLinesTextStyles());
		
		{
			//�ҳ����µİ汾��
			RevisionHistory his = RevisionHistoryUtil.getMaxVersionHisInfo(getEditor().getARESResource().getModule(),IBasicDataRestypes.singleTable);
			if (his != null) {
				txtVersionName.setText(his.getVersion());
				txtUpdateName.setText(his.getModifiedDate());
			}else {
				his = RevisionHistoryUtil.getMaxVersionHisInfo(getEditor().getARESResource().getModule());
				if (his != null) {
					txtVersionName.setText(his.getVersion());
					txtUpdateName.setText(his.getModifiedDate());
				}else {
					String projectVersion = RevisionHistoryUtil.getProjectPropertyVersion(getEditor().getARESResource().getARESProject());
					if (StringUtils.isBlank(projectVersion)) {
						projectVersion = "1.0.0.1";
					}
					txtVersionName.setText(projectVersion);
				}
			}
		}
		
		// ֻ������
		txtName.setEditable(false);
		txtName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		txtVersionName.setEditable(false);
		txtVersionName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		txtUpdateName.setEditable(false);
		txtUpdateName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		//getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtName));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtChineseName));
		getEditableControl().addEditableUnit(new JresDefaultEditableUnit(txtDescription));
		
		// ����
		GridLayoutFactory.swtDefaults().numColumns(4).applyTo(client);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lblName);
		GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).applyTo(txtName);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lblChineseName);
		GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).applyTo(txtChineseName);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lblVersionName);
		GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).applyTo(txtVersionName);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lblUpdateName);
		GridDataFactory.fillDefaults().grab(true, false).hint(10, SWT.DEFAULT).applyTo(txtUpdateName);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(lblDescription);
		GridDataFactory.fillDefaults().span(3, 1).grab(true, true).hint(10, 50).applyTo(txtDescription);
		
		section.setClient(client);
		return section;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.DataBindingFormPage#doDataBingingOnControls()
	 */
	@Override
	protected void doDataBingingOnControls() {
		//bingText(txtName, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__NAME);
		// BUG #5701 ������������Դ������ʱ��Դ�༭��������δ�޸�
		// �༭���е���Դ����Ҫ��ģ���е�����
		IARESResource res = getEditor().getARESResource();
		txtName.setText(res == null ? StringUtils.EMPTY : res.getName());
		bingText(txtChineseName, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME);
		bingText(txtDescription, getInfo(), CorePackage.Literals.BASIC_RESOURCE_INFO__DESCRIPTION);
		if(null != txtFile){//Ԫ���ݵĻ��������С����ɽű��ļ�������Ϊ�գ���Ҫ����
			bingText(txtFile, getInfo(), BasicdataPackage.Literals.BASIC_DATA_BASE_MODEL__FILE);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.DataBindingFormPage#infoChange()
	 */
	@Override
	public void infoChange() {
		emc.setInput(getEditor().getARESResource(), getInfo());
		super.infoChange();
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#commandStackChanged(java.util.EventObject)
	 */
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
	
}
