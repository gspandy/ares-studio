/**
* <p>Copyright: Copyright (c) 2011</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.clearinghouse.ui.util;

import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.jres.clearinghouse.composite.AddColumnWizardComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.AddConstraintWizardComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.AddIndexFieldWizardComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.AddIndexWizardComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.ModifyColumnNullableWizardComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.ModifyColumnTypeWizardComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.RemoveColumnWizardComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.RemoveConstraintWizardComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.RemoveIndexFieldWizardComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.RemoveIndexWizardComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.RenameColumnWizardComposite;
import com.hundsun.ares.studio.jres.model.chouse.AddConstraintModification;
import com.hundsun.ares.studio.jres.model.chouse.AddIndexModification;
import com.hundsun.ares.studio.jres.model.chouse.AddTableColumnModification;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnNullableModifycation;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnTypeModification;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveIndexModification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveTableColumnModification;
import com.hundsun.ares.studio.jres.model.chouse.RenameTableColumnModification;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;

/**
 * @author qinyuan
 *
 */
public class HisModifyWizardFactory {

	//�޸�����ö��
	public static enum MODIFYACTION_TYPE{
		���ӱ��ֶ�,
		ɾ�����ֶ�,
		���������ֶ�,
		��������,
		ɾ������,
		ɾ�������ֶ�,
		���������ֶ�,
		�޸��ֶ�����,
		�޸��ֶοɷ�Ϊ��,
		����Լ��,
		ɾ��Լ��
	}
	
	/**
	 * ������ѡ�޸�����������Ӧ�Ľ���
	 * @param select �޸�����
	 * @param comp 
	 * @param object 
	 * @param editorPart 
	 * @return
	 */
	public static  Composite getDetailComposite(MODIFYACTION_TYPE select, Composite comp, Modification action, EMFFormEditor editorPart) {
		switch(select){
		case ���ӱ��ֶ�:
			return new AddColumnWizardComposite(comp, editorPart, action);
		case ɾ�����ֶ�:
			return new RemoveColumnWizardComposite(comp, editorPart,action);
		case ���������ֶ�:
			return new RenameColumnWizardComposite(comp, editorPart, action);
		case ��������:
			return new AddIndexWizardComposite(comp, editorPart, action);
		case ɾ������:
			return new RemoveIndexWizardComposite(comp, editorPart,action);
		case ɾ�������ֶ�:
			return new RemoveIndexFieldWizardComposite(comp, editorPart,action);
		case ���������ֶ�:
			return new AddIndexFieldWizardComposite(comp, editorPart,action);
		case �޸��ֶ�����:
			return new ModifyColumnTypeWizardComposite(comp, editorPart, action);
		case �޸��ֶοɷ�Ϊ��:
			return new ModifyColumnNullableWizardComposite(comp, editorPart, action);
		case ����Լ��:
			return new AddConstraintWizardComposite(comp, editorPart, action);
		case ɾ��Լ��:
			return new RemoveConstraintWizardComposite(comp, editorPart, action);
		default:
			return comp;
		}
	}

	
	public static String getActionName(Modification action){
		if(action instanceof AddTableColumnModification){
			return MODIFYACTION_TYPE.���ӱ��ֶ�.name();
		}
		if(action instanceof RemoveTableColumnModification){
			return MODIFYACTION_TYPE.ɾ�����ֶ�.name();
		}
		if(action instanceof RenameTableColumnModification){
			return MODIFYACTION_TYPE.���������ֶ�.name();
		}
		if(action instanceof AddIndexModification){
			return MODIFYACTION_TYPE.��������.name();
		}
		if(action instanceof RemoveIndexModification){
			return MODIFYACTION_TYPE.ɾ������.name();
		}
		if (action instanceof ChangeTableColumnTypeModification) {
			return MODIFYACTION_TYPE.�޸��ֶ�����.name();
		}
		if (action instanceof ChangeTableColumnNullableModifycation) {
			return MODIFYACTION_TYPE.�޸��ֶοɷ�Ϊ��.name();
		}
		if (action instanceof AddConstraintModification) {
			return MODIFYACTION_TYPE.����Լ��.name();
		}
		if (action instanceof RemoveIndexModification) {
			return MODIFYACTION_TYPE.ɾ��Լ��.name();
		}
		return "";
	}
	
}
