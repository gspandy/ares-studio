/**
 * Դ�������ƣ�GenSQLInitPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.wizard.page;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.jres.database.constant.IDBConstant;
import com.hundsun.ares.studio.jres.model.database.DBGenContext;
import com.hundsun.ares.studio.jres.model.database.DBModuleCommonProperty;
import com.hundsun.ares.studio.jres.model.database.DatabaseFactory;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelComposite;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelUtils;

/**
 * @author wangxh
 *
 */
public class GenSQLInitPage extends WizardPage {
	
	/**ģʽѡ��0��ȫ����1������*/
	int mode = 0;
	/**��������*/
	String DBType = "";
	/**ȫ��*/
	Button btnDBSQL;
	/**����*/
	Button btnDBPatchSQL;
	/**��������*/
	Combo comboDBType;
	/** ��Ŀ*/
	IARESProject project;
	/** �Ƿ�����ע��*/
	Button commentButton;
	/** ����ע��*/
	boolean isComment = true;
	
	/**
	 * ����������
	 */
	DBGenContext genContext = DatabaseFactory.eINSTANCE.createDBGenContext();
	
	/**
	 * @param pageName
	 * @param selection
	 */
	public GenSQLInitPage(String pageName,ISelection selection) {
		super(pageName);
		if(selection instanceof IStructuredSelection){
			Object obj = ((IStructuredSelection)selection).getFirstElement();
			if(obj instanceof IARESElement){
				project = ((IARESElement)obj).getARESProject();
			}
		}
		setTitle(pageName);
		setDescription("��ѡ���ʼ��Ϣ");
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		
		Group modeGroup = new Group(composite, SWT.None);
		modeGroup.setText("ѡ��ģʽ");
		modeGroup.setLayout(new GridLayout());
		
		btnDBSQL = new Button(modeGroup, SWT.RADIO);
		btnDBSQL.setText("ȫ��");
		btnDBSQL.setSelection(true);
		
		btnDBPatchSQL = new Button(modeGroup, SWT.RADIO);
		btnDBPatchSQL.setText("����");
		
		Group DBTypeGroup = new Group(composite, SWT.None);
		DBTypeGroup.setText("ѡ����������");
		DBTypeGroup.setLayout(new GridLayout());
		
		comboDBType = new Combo(DBTypeGroup, SWT.READ_ONLY | SWT.BORDER);
		
		if(project != null){
			DBModuleCommonProperty property = null;
			try {
				property = (DBModuleCommonProperty) project.getProjectProperty().getMap().get(IDBConstant.MODULE_DATABASE_EXTEND_PROPERTY_KEY); 
			} catch (ARESModelException e1) {
			}
			
			property = (DBModuleCommonProperty) ObjectUtils.defaultIfNull(
					property, 
					DatabaseFactory.eINSTANCE.createDBModuleCommonProperty());
			String[] items = StringUtils.split(property.getSupportDatabases(), ",");
			comboDBType.setItems(items);
			comboDBType.setText(property.getDatabase());
			DBType = property.getDatabase();
		}
		
		Group commentGroup = new Group(composite, SWT.NONE);
		commentGroup.setText("ѡ������ע��");
		commentGroup.setLayout(new GridLayout());
		commentButton = new Button(commentGroup, SWT.CHECK);
		commentButton.setText("�Ƿ�����ע��");
		commentButton.setSelection(true);
		
		commentButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				isComment = commentButton.getSelection();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		Group contextGroup = new Group(composite, SWT.NONE);
		contextGroup.setText("���ɲ���");
		contextGroup.setLayout(new FillLayout());
		ExtensibleModelComposite emc = new ExtensibleModelComposite(contextGroup, new FormToolkit(Display.getDefault()));
		ExtensibleModelUtils.extend(project, genContext, true);
		emc.setInput(project, genContext);
		
		
		GridLayoutFactory.swtDefaults().applyTo(composite);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(modeGroup);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(DBTypeGroup);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(contextGroup);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(commentGroup);
		
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(btnDBSQL);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(btnDBPatchSQL);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(comboDBType);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(commentButton);
		
		btnDBSQL.addSelectionListener(new SelectionAdapter() {

			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnDBSQL.getSelection()){
					mode = 0;
				}
				else{
					mode = 1;
				}
			}
		});
		
		
		comboDBType.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				DBType = comboDBType.getText();
			}
		});
		

	
		
		setControl(composite);

	}

	/**
	 * @return 
	 * ģʽѡ��mode 0��ȫ����1������
	 */
	public String getMode() {
		if(mode == 0){
			return IDBConstant.COMPILE_DATABASE_FULL;
		}
		return IDBConstant.COMPILE_DATABASE_PATCH;
	}

	/**
	 * @return the genContext
	 */
	public DBGenContext getGenContext() {
		return genContext;
	}

	/**
	 * @return the dBType
	 * ��������
	 */
	public String getDBType() {
		return DBType;
	}

	public boolean isPageActive(){
		return isCurrentPage();
	}
	
	public boolean isComment (){
		return isComment;
	}
	
}
