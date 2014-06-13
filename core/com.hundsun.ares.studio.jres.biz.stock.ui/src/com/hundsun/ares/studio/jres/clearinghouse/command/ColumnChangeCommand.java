/**
 * Դ�������ƣ�ChangeColumnTypeCommand.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.biz.stock.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.clearinghouse.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.hundsun.ares.studio.jres.model.chouse.ColumnChangeDetail;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;

/**
 * @author sundl
 *
 */
public abstract class ColumnChangeCommand<T extends ColumnChangeDetail> extends RecordingCommand{

	protected List<TableColumn> changedColumns = new ArrayList<TableColumn>();
	protected TableResourceData tableData;
	protected List<T> changes;
	
	public ColumnChangeCommand(TableResourceData tableData, List<T> changes) {
		super((TransactionalEditingDomain) AdapterFactoryEditingDomain.getEditingDomainFor(tableData));
		this.tableData = tableData;
		this.changes = changes;
	}
	
	@Override
	public Collection<?> getAffectedObjects() {
		return changedColumns;
	}
	
	/**
	 * �ڱ�ģ���в���change��Ӧ����
	 * @return
	 */
	protected static TableColumn findColumn(ColumnChangeDetail change, TableResourceData tableData) {
		for (TableColumn c : tableData.getColumns()) {
			if (EcoreUtil.equals(c, change.getSnapshot())) 
				return c;
		}
		// ��Ӧ�÷��������
		return null;
	}

}
