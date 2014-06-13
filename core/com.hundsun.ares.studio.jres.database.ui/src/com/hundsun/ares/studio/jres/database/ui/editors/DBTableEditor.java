/**
 * Դ�������ƣ�DBTableEditor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.ui.editors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.ui.PartInitException;

import com.hundsun.ares.studio.jres.database.constant.IDBConstant;
import com.hundsun.ares.studio.jres.model.database.DatabasePackage;
import com.hundsun.ares.studio.ui.CommonElementContentProvider;
import com.hundsun.ares.studio.ui.CommonElementLabelProvider;
import com.hundsun.ares.studio.ui.editor.blocks.RevisionHistoryListPage;
import com.hundsun.ares.studio.ui.editor.text.TextEditorInput;
import com.hundsun.ares.studio.ui.extendpoint.manager.IExtendedPage;
import com.hundsun.ares.studio.ui.page.ExtendPageWithMyDirtySystem;

/**
 * @author gongyf
 *
 */
public class DBTableEditor extends DatabaseEMFFormEditor {

	private CommonElementContentProvider cp = new CommonElementContentProvider();
	private CommonElementLabelProvider provider = new CommonElementLabelProvider(cp);
	
	@Override
	protected void addPages() {
		try {
			addPage(new DBTableOverviewPage(this, "overview", "������Ϣ"));
			addPage(new DBTableColumnAndIndexPage(this, "column_index", "�ֶκ�����"));
			addPage(new TableSQLPreviewPage(this, "preview", "SQLԤ��"), new TextEditorInput());
			addPage(new RevisionHistoryListPage(this, "history", "�޶���Ϣ"));
			createExtendPage();
			// ���SQLԤ����֧��
			addPageChangedListener(new TableSQLPreviewUpdater());
			
		} catch (PartInitException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void handleBeforeSave() {
		for (IExtendedPage page : extendsPages) {
			if (page instanceof ExtendPageWithMyDirtySystem) {
				((ExtendPageWithMyDirtySystem)page).doSave();
			}
		}
		super.handleBeforeSave();
	}
	
	@Override
	protected EClass getInfoClass() {
		return DatabasePackage.Literals.TABLE_RESOURCE_DATA;
	}
	
	@Override
	protected String getEditingDomainID() {
		return IDBConstant.ID_TABLE_EDITDOMAIN;
	}
	
	@Override
	protected String getEditorTitle() {
		String partName = provider.getText(getARESResource());
		if (isReadOnly()) {
			partName += "(ֻ��)";
		}
		return partName;
	}
	
}
