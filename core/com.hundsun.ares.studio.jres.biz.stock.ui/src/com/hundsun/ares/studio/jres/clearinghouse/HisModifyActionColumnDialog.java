/**
 * Դ�������ƣ�HisInternalVerColumnDialog.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.jres.clearinghouse.composite.ModifyActionComposite;
import com.hundsun.ares.studio.jres.clearinghouse.constant.HisModifyActionFactory;
import com.hundsun.ares.studio.jres.clearinghouse.constant.HisModifyActionFactory.MODIFYACTION_TYPE;
import com.hundsun.ares.studio.jres.model.chouse.IStock3Constant;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.chouse.util.StockUtils;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;

/**
 * ��ʷ��¼���޸����ݵ�����
 * @author wangxh
 *
 */
public class HisModifyActionColumnDialog extends Dialog {
	
	//�޸�����
	Combo comboVersion;
	
	//��ϸ�޸���Ϣ����
	Composite detailComp;
	
	//�޶���¼��action
	Modification action;
	
	IARESResource resource;
	private TableResourceData tableData;
	
	//���������޸����Ͷ�Ӧ���޸���Ϣ����
	List<Composite>detailComps = new ArrayList<Composite>();
	
	//�����޸ĺ��modifyaction��Ϣ
	Modification result;
	
	//ѡ�е��޸���Ϣ����
	Composite topComp;
	
	//ѡ�е��޸�����
	MODIFYACTION_TYPE topVersion;
	
	RevisionHistory rh;
	
	//�����޸�����
	final MODIFYACTION_TYPE[] values = HisModifyActionFactory.MODIFYACTION_TYPE.values();
	
	final StackLayout layout=new StackLayout();
	/**
	 * @param parentShell
	 * @param action 
	 * @param resource 
	 */
	// 2012-05-15 sundl ��Ӳ������Ա�����ڶԻ����л�ȡ�����Ϣ
	public HisModifyActionColumnDialog(Shell parentShell, TableResourceData tableData, Modification action, IARESResource resource ,RevisionHistory rh) {
		super(parentShell);
		this.action = action;
		this.resource = resource;
		this.result = action;
		this.tableData = tableData;
		this.rh = rh;
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(IStock3Constant.TABLE_CHANGE_TITLE);
		newShell.setImage(AbstractUIPlugin.imageDescriptorFromPlugin("com.hundsun.ares.studio.jres.database.resources", "icons/���ݿ�.gif").createImage());
	}
	
	@Override
	protected Button createButton(org.eclipse.swt.widgets.Composite parent,
			int id, String label, boolean defaultButton) {
		Button btn = null;
		if (IDialogConstants.OK_ID == id) {
			btn = super.createButton(parent, id, "ȷ��", true);
		} else if (IDialogConstants.CANCEL_ID == id) {
			btn = super.createButton(parent, id, "ȡ��", false);
		}
		return btn;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		Composite verComp = new Composite(composite, SWT.BORDER);
		detailComp = new Composite(composite, SWT.BORDER);
		
		creatVersionComposite(verComp,action);
		
		detailComp.setLayout(layout);
		
		
		for(int i =0;i<values.length;i++){
			MODIFYACTION_TYPE select = values[i];
			detailComps.add(HisModifyActionFactory.getDetailComposite(select,detailComp,tableData, action,resource));
		}
		
		GridDataFactory.fillDefaults().grab(true, true).hint(-1, 70).applyTo(verComp);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(detailComp);
		return composite;
	}

	/**
	 * @param verComp
	 * @param action 
	 */
	private void creatVersionComposite(Composite verComp, Modification action) {

		String version = HisModifyActionFactory.getActionName(action);
		verComp.setLayout(new GridLayout(2, false));
		Label lbVersion = new Label(verComp, SWT.None);
		lbVersion.setText("��ѡ���޸����ͣ�");
		comboVersion = new Combo(verComp, SWT.BORDER | SWT.READ_ONLY);
		
		int select = 0;
		comboVersion.removeAll();
		for(int i = 0; i<values.length; i++){
			comboVersion.add(values[i].name(), i);
			if(version != null && version.equals(values[i].name())){
				select = i;
			}
		}
		comboVersion.select(select);
		
		GridDataFactory.fillDefaults().grab(false, true).applyTo(lbVersion);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(comboVersion);
		
		//������ѡ���޸�������ʾ��Ӧ�Ľ���
		comboVersion.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				int i = comboVersion.getSelectionIndex();
				topComp = detailComps.get(i);
				topVersion = values[i];
				layout.topControl = topComp;
				detailComp.layout();
				
			}
		});
		
		comboVersion.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				int i = comboVersion.getSelectionIndex();
				topComp = detailComps.get(i);
				topVersion = values[i];
				layout.topControl = topComp;
				detailComp.layout();
			}
		});
		
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		super.okPressed();
		if(topComp instanceof ModifyActionComposite){
			result = ((ModifyActionComposite)topComp).getAction();
		}
		EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(tableData);
		Command command = new SetCommand(editingDomain, rh, CorePackage.Literals.REVISION_HISTORY__MODIFIED, StockUtils.getModificationDescription(tableData, result));
		editingDomain.getCommandStack().execute(command);
//		rh.setModified(StockUtils.getModificationDescription(result));
	}
	
	/**
	 * ��ȡ�Ի���ѡ������
	 * 
	 * @return
	 */
	public Object getResult() {
		return result;
	}

}
