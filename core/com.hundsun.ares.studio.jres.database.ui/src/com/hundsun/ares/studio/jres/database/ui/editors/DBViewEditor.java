/**
 * Դ�������ƣ�DBViewEditor.java
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
import com.hundsun.ares.studio.ui.editor.blocks.RevisionHistoryListPage;
import com.hundsun.ares.studio.ui.editor.text.TextEditorInput;
import com.hundsun.ares.studio.ui.extendpoint.manager.IExtendedPage;
import com.hundsun.ares.studio.ui.page.ExtendPageWithMyDirtySystem;

/**
 * @author gongyf
 *
 */
public class DBViewEditor extends DatabaseEMFFormEditor {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	@Override
	protected void addPages() {
		try {
			addPage(new DBViewOverviewPage(this, "overview", "������Ϣ"));
			addPage(new DBSQLPreviewPage(this, "preview", "SQLԤ��"), new TextEditorInput());
			addPage(new RevisionHistoryListPage(this ,"histroy", "�޶���Ϣ"));
			createExtendPage();
			// ���SQLԤ����֧��
			addPageChangedListener(new DBSQLPreviewUpdater());
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void handleBeforeSave() {
		for (IExtendedPage page : extendsPages) {
			if (page instanceof ExtendPageWithMyDirtySystem)
				((ExtendPageWithMyDirtySystem) page).doSave();
		}
		super.handleBeforeSave();
	}
	
	@Override
	protected EClass getInfoClass() {
		return DatabasePackage.Literals.VIEW_RESOURCE_DATA;
	}
	
	@Override
	protected String getEditingDomainID() {
		return IDBConstant.ID_VIEW_EDITDOMAIN;
	}
}
