/**
 * Դ�������ƣ�ModifyColumnTypeCommand.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.biz.stock.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.clearinghouse.command;

import java.util.List;

import com.hundsun.ares.studio.core.model.CoreFactory;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.jres.model.chouse.CTCTMDetail;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnTypeModification;
import com.hundsun.ares.studio.jres.model.chouse.ChouseFactory;
import com.hundsun.ares.studio.jres.model.chouse.IStock3Constant;
import com.hundsun.ares.studio.jres.model.chouse.RevisionHistoryProperty;
import com.hundsun.ares.studio.jres.model.chouse.util.StockUtils;
import com.hundsun.ares.studio.jres.model.database.ColumnType;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;

/**
 * @author sundl
 *
 */
public class ModifyColumnTypeCommand extends ColumnChangeCommand<CTCTMDetail> {

	public ModifyColumnTypeCommand(TableResourceData tableData, List<CTCTMDetail> changes) {
		super(tableData, changes);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.RecordingCommand#doExecute()
	 */
	@Override
	protected void doExecute() {
		for (CTCTMDetail change : changes) {
			TableColumn c = findColumn(change, tableData);
			// �Ǳ�׼�ֶβ�����ֱ���޸��ֶ�����
			if (c.getColumnType() == ColumnType.NON_STD_FIELD)
				c.setDataType(change.getNewType());
			changedColumns.add(c);
		}
		
		RevisionHistory rh = CoreFactory.eINSTANCE.createRevisionHistory();
		RevisionHistoryProperty p = ChouseFactory.eINSTANCE.createRevisionHistoryProperty();
		ChangeTableColumnTypeModification action = ChouseFactory.eINSTANCE.createChangeTableColumnTypeModification();
		action.getDetails().addAll(changes);
		p.setAction(action);
		rh.setModified(StockUtils.getModificationDescription(tableData, action));
		rh.getData2().put(IStock3Constant.HISTORY_DATA2_KEY, p);
		// 2012-05-18 sundl �޸ļ�¼�Զ���ӵ���һ����¼
		tableData.getHistories().add(0, rh);
	}

}
