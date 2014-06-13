/**
 * Դ�������ƣ�RevisionHistoryListPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse.ui.page;


import org.apache.log4j.Logger;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.TriggerListener;

import com.hundsun.ares.studio.jres.model.chouse.ChousePackage;
import com.hundsun.ares.studio.jres.model.chouse.RevisionHistoryProperty;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.RevisionHistoryListPage;

public class DBExtendsRevisionHistoryListPage extends RevisionHistoryListPage {
	
	private static final Logger logger = Logger.getLogger(DBExtendsRevisionHistoryListPage.class);
	
	private TriggerListener trigger = new TriggerListener() {
		
		@Override
		protected Command trigger(TransactionalEditingDomain domain,
				Notification notification) {

			if (notification.getNotifier() instanceof RevisionHistoryProperty ) {
				if (ChousePackage.Literals.REVISION_HISTORY_PROPERTY__ACTION_DESCRIPTION.equals(notification.getFeature())) {
					// �����������������������,���Զ��Ѷ�Ӧ��java���ʹ���
					final RevisionHistoryProperty pd = (RevisionHistoryProperty) notification.getNotifier();
					final String type = pd.getActionDescription();
					return new RecordingCommand(domain) {
						
						@Override
						protected void doExecute() {
//							IMetadataService service = DataServiceManager.getInstance().getService(
//									getEditor().getARESResource().getARESProject(), IMetadataService.class);
//							IBusinessDataType bizType = service.getBusinessDataType(type);
//							if (bizType != null) {
//								pd.setJavaType(bizType.getRealType("java"));
//							}
							
						}
					};
				}
			}
			return null;
		}
	};
	
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public DBExtendsRevisionHistoryListPage(EMFFormEditor editor,String id, String title) {
		super(editor, id, title);
		getEditingDomain().addResourceSetListener(trigger);
	}

	@Override
	public void dispose() {
		super.dispose();
		getEditingDomain().removeResourceSetListener(trigger);
	}
	
}
