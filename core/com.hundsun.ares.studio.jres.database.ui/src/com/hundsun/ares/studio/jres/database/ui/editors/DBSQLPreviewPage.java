/**
 * Դ�������ƣ�DBSQLPreviewPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.ui.editors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;

import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.TextEditorEMFFormPage;
import com.hundsun.ares.studio.ui.editor.text.sql.SQLSourceViewerConfiguration;
import com.hundsun.ares.studio.ui.editor.text.sql.SQLTextEditorInputDocumentProvider;

/**
 * @author gongyf
 *
 */
public class DBSQLPreviewPage extends TextEditorEMFFormPage {

	/* ��ǰ�Ƿ��������ɴ���ı�־λ */
	private boolean generating = false;
	/* ҳ��Ҫ��ʾ���ı� */
	private String text;
	
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public DBSQLPreviewPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
		
		SQLSourceViewerConfiguration configuration = new SQLSourceViewerConfiguration();
		setSourceViewerConfiguration(configuration);
		
		setDocumentProvider(new SQLTextEditorInputDocumentProvider());
		
		IPreferenceStore[] stores = new IPreferenceStore[2];
		stores[0] = EditorsUI.getPreferenceStore();
		stores[1] = ARESEditorPlugin.getDefault().getPreferenceStore();
		setPreferenceStore(new ChainedPreferenceStore(stores));
	}
	
	public void setGenerating(boolean generating) {
		this.generating = generating;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void update() {
		IDocument doc = getDocumentProvider().getDocument(getEditorInput());
		if (generating) {
			doc.set("�������ɴ���,���Ժ�...");
		} else {
			doc.set(text);
		}
	}

}