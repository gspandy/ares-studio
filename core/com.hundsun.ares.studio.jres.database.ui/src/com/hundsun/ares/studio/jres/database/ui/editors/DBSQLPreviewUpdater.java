/**
 * Դ�������ƣ�DBSQLPreviewUpdater.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.editors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;

import com.hundsun.ares.studio.jres.database.utils.DBTableGenCodeUtils;
import com.hundsun.ares.studio.ui.editor.text.TextEditorInput;

/**
 * @author gongyf
 *
 */
public class DBSQLPreviewUpdater implements IPageChangedListener {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IPageChangedListener#pageChanged(org.eclipse.jface.dialogs.PageChangedEvent)
	 */
	@Override
	public void pageChanged(PageChangedEvent event) {
		if (event.getSelectedPage() instanceof DBSQLPreviewPage) {
			DBSQLPreviewPage page = (DBSQLPreviewPage) event.getSelectedPage();
			EObject eObj = page.getEditor().getInfo();
			
			StringBuffer buffer = new StringBuffer();
			
			buffer.append(DBTableGenCodeUtils.genTableFullCode(page.getEditor().getARESResource(), eObj));
			buffer.append(DBTableGenCodeUtils.genTablePatchCode(page.getEditor().getARESResource(), eObj));
			
			page.setInput(new TextEditorInput(buffer.toString()));

		}
	}
}
