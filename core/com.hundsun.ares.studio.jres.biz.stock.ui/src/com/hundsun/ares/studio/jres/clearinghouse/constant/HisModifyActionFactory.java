/**
 * Դ�������ƣ�DBHisDetailDialogFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse.constant;

import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.clearinghouse.composite.AddColumnComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.AddColumnPKComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.AddColumnUniqueComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.AddConstraintComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.AddIndexComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.AddIndexFieldComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.ModifyColumnNullableComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.ModifyColumnPrimaryKeyComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.ModifyColumnTypeComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.ModifyColumnUniqueComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.NewTableComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.RemoveColumnComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.RemoveColumnPKComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.RemoveColumnUniqueComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.RemoveConstraintComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.RemoveIndexComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.RemoveIndexFieldComposite;
import com.hundsun.ares.studio.jres.clearinghouse.composite.RenameColumnComposite;
import com.hundsun.ares.studio.jres.model.chouse.AddConstraintModification;
import com.hundsun.ares.studio.jres.model.chouse.AddIndexFieldModification;
import com.hundsun.ares.studio.jres.model.chouse.AddIndexModification;
import com.hundsun.ares.studio.jres.model.chouse.AddTableColumnModification;
import com.hundsun.ares.studio.jres.model.chouse.AddTableColumnPKModification;
import com.hundsun.ares.studio.jres.model.chouse.AddTableColumnUniqueModifycation;
import com.hundsun.ares.studio.jres.model.chouse.AddTableModification;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnNullableModifycation;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnPrimaryKeyModifycation;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnTypeModification;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnUniqueModifycation;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveConstraintModification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveIndexFieldModification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveIndexModification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveTableColumnModification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveTableColumnPKModification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveTableColumnUniqueModifycation;
import com.hundsun.ares.studio.jres.model.chouse.RenameTableColumnModification;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;

/**
 * @author wangxh
 *
 */
public class HisModifyActionFactory {
	
	//�޸�����ö��
	public static enum MODIFYACTION_TYPE{
		�½���,
		���ӱ��ֶ�,
		ɾ�����ֶ�,
		���������ֶ�,
		��������,
		ɾ������,
		���������ֶ�,
		ɾ�������ֶ�,
		�޸ı��ֶ�����,
		��������,
		�޸�����,
		ɾ������,
		�޸ı��ֶ�Ϊ��,
		����Լ��,
		ɾ��Լ��,
		����ΨһԼ��,
		�޸�ΨһԼ��,
		ɾ��ΨһԼ��
	}
	
	/**
	 * ������ѡ�޸�����������Ӧ�Ľ���
	 * @param select �޸�����
	 * @param Comp 
	 * @param object 
	 * @param resource 
	 * @return
	 */
	public static  Composite getDetailComposite(MODIFYACTION_TYPE select, Composite Comp, TableResourceData tableData, Modification action, IARESResource resource) {
		switch(select){
		case �½���:
			return new NewTableComposite(Comp,tableData, resource, action);
		case ���ӱ��ֶ�:
			return new AddColumnComposite(Comp, tableData, resource, action);
		case ɾ�����ֶ�:
			return new RemoveColumnComposite(Comp,tableData, resource,action);
		case ���������ֶ�:
			return new RenameColumnComposite(Comp,tableData, resource, action);
		case ��������:
			return new AddIndexComposite(Comp, tableData,resource, action);
		case ɾ������:
			return new RemoveIndexComposite(Comp,tableData, resource,action);
		case ���������ֶ�:
			return new AddIndexFieldComposite(Comp,tableData, resource,action);	
		case ɾ�������ֶ�:
			return new RemoveIndexFieldComposite(Comp,tableData, resource,action);
		case �޸ı��ֶ�����:
			return new ModifyColumnTypeComposite(Comp,tableData,resource, action);
		case ��������:
			return new AddColumnPKComposite(Comp,tableData,resource, action);
		case �޸�����:
			return new ModifyColumnPrimaryKeyComposite(Comp,tableData,resource, action);
		case ɾ������:
			return new RemoveColumnPKComposite(Comp,tableData,resource, action);
		case �޸ı��ֶ�Ϊ��:
			return new ModifyColumnNullableComposite(Comp,tableData,resource, action);
		case ����ΨһԼ��:
			return new AddColumnUniqueComposite(Comp,tableData,resource, action);
		case �޸�ΨһԼ��:
			return new ModifyColumnUniqueComposite(Comp,tableData,resource, action);
		case ɾ��ΨһԼ��:
			return new RemoveColumnUniqueComposite(Comp,tableData,resource, action);
		case ����Լ��:
			return new AddConstraintComposite(Comp, tableData, resource, action);
		case ɾ��Լ��:
			return new RemoveConstraintComposite(Comp, tableData, resource, action);
		default:
			return Comp;
		}
	}

	
	public static String getActionName(Modification action){
		if(action instanceof AddTableModification){
			return MODIFYACTION_TYPE.�½���.name();
		}
		if(action instanceof AddTableColumnModification){
			return MODIFYACTION_TYPE.���ӱ��ֶ�.name();
		}
		if(action instanceof RemoveTableColumnModification){
			return MODIFYACTION_TYPE.ɾ�����ֶ�.name();
		}
		if(action instanceof RenameTableColumnModification){
			return MODIFYACTION_TYPE.���������ֶ�.name();
		}
		if(action instanceof ChangeTableColumnTypeModification){
			return MODIFYACTION_TYPE.�޸ı��ֶ�����.name();
		}
		if(action instanceof AddIndexModification){
			return MODIFYACTION_TYPE.��������.name();
		}
		if(action instanceof RemoveIndexModification){
			return MODIFYACTION_TYPE.ɾ������.name();
		}
		
		if(action instanceof RemoveIndexFieldModification){
			return MODIFYACTION_TYPE.ɾ�������ֶ�.name();
		}
		if(action instanceof AddIndexFieldModification){
			return MODIFYACTION_TYPE.���������ֶ�.name();
		}
		if(action instanceof ChangeTableColumnPrimaryKeyModifycation){
			return MODIFYACTION_TYPE.�޸�����.name();
		}
		if(action instanceof AddTableColumnPKModification){
			return MODIFYACTION_TYPE.��������.name();
		}
		if(action instanceof RemoveTableColumnPKModification){
			return MODIFYACTION_TYPE.ɾ������.name();
		}
		if(action instanceof ChangeTableColumnNullableModifycation){
			return MODIFYACTION_TYPE.�޸ı��ֶ�Ϊ��.name();
		}
		if(action instanceof AddTableColumnUniqueModifycation){
			return MODIFYACTION_TYPE.����ΨһԼ��.name();
		}
		if(action instanceof ChangeTableColumnUniqueModifycation){
			return MODIFYACTION_TYPE.�޸�ΨһԼ��.name();
		}
		if(action instanceof RemoveTableColumnUniqueModifycation){
			return MODIFYACTION_TYPE.ɾ��ΨһԼ��.name();
		}
		if (action instanceof AddConstraintModification) {
			return MODIFYACTION_TYPE.����Լ��.name();
		}
		if (action instanceof RemoveConstraintModification) {
			return MODIFYACTION_TYPE.ɾ��Լ��.name();
		}
		return "";
	}
	
}
