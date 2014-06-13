/**
 * Դ�������ƣ�ModifyColumnTypeWizardComposite.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.biz.stock.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.clearinghouse.composite;

import org.eclipse.emf.common.command.Command;
import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.jres.clearinghouse.command.ModifyColumnTypeCommand;
import com.hundsun.ares.studio.jres.clearinghouse.ui.action.IWizardComposite;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;

/**
 * @author sundl
 *
 */
public class ModifyColumnTypeWizardComposite extends ModifyColumnTypeComposite implements IWizardComposite{

	public ModifyColumnTypeWizardComposite(Composite parent, EMFFormEditor editor, Modification action) {
		super(parent, (TableResourceData) editor.getInfo(), editor.getARESResource(), action);
	}
	
	@Override
	public Command getCommand() {
		return new ModifyColumnTypeCommand(tableData, input);
	}
}
