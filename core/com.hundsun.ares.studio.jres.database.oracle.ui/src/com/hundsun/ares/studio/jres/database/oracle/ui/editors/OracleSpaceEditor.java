/**
* <p>Copyright: Copyright (c) 2011</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.database.oracle.ui.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IGotoMarker;

import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.jres.database.ui.editors.DBSQLPreviewPage;
import com.hundsun.ares.studio.jres.database.ui.editors.DatabaseEMFFormEditor;
import com.hundsun.ares.studio.jres.model.database.oracle.OraclePackage;
import com.hundsun.ares.studio.ui.editor.blocks.RevisionHistoryListPage;
import com.hundsun.ares.studio.ui.editor.text.TextEditorInput;

/**
 * @author wangxh
 *
 */
public class OracleSpaceEditor extends DatabaseEMFFormEditor {

	//Oracle�û�Ȩ�ޱ༭��ҳ��
	private OracleSpaceBasicPage oracleSpaceBasicPage;
	//�û��޶���Ϣҳ��
	protected RevisionHistoryListPage historyPage;

	@Override
	protected EClass getInfoClass() {
		return OraclePackage.Literals.ORACLE_SPACE_RESOURCE_DATA;
	}

	@Override
	protected void addPages() {
	
		try {
			
			oracleSpaceBasicPage = new OracleSpaceBasicPage(this, "oraclespace", "Oracle��ռ�");
			addPage(oracleSpaceBasicPage);
			
			addPage(new DBSQLPreviewPage(this, "preview", "SQLԤ��"), new TextEditorInput());
			
			historyPage = new RevisionHistoryListPage(this ,"histroy", "�޶���Ϣ");
			addPage(historyPage);
			
			// ���SQLԤ����֧��
			addPageChangedListener(new TableSpaceSQLPreviewUpdater());
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IGotoMarker.class) {
			return new IGotoMarker() {
				
				@Override
				public void gotoMarker(IMarker marker) {
					String uri;
					try {
						uri = (String) marker.getAttribute(IMarker.LOCATION);
						EObject obj = getInfo().eResource().getEObject(uri);
						
						if(obj instanceof RevisionHistory) {
							historyPage.getEditor().setActivePage(historyPage.getId());
							historyPage.getColumnViewer().setSelection(new StructuredSelection(obj), true);
						}
						else
						{
							oracleSpaceBasicPage.getEditor().setActivePage(oracleSpaceBasicPage.getId());
						}
					} catch (CoreException e) {
						e.printStackTrace();
					}
					
				}
			};
		}
		return super.getAdapter(adapter);
	}

}
