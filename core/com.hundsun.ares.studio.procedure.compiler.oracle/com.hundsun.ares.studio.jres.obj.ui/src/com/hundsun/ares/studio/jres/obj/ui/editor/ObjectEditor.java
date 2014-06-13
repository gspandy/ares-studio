package com.hundsun.ares.studio.jres.obj.ui.editor;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.RevisionHistoryListPage;

public class ObjectEditor extends EMFFormEditor{

	ObjectPage objectPage;

	@Override
	protected EClass getInfoClass() {
		return BizPackage.Literals.ARES_OBJECT;
	}

	@Override
	protected void addPages() {
		try {
			objectPage = new ObjectPage(this, "object", "������Ϣ");
			addPage(objectPage);

			addPage(new ObjectParameterPage(this, "parm", "����"));
			
			addPage(new RevisionHistoryListPage(this, "revisionHistory", "�޶���Ϣ"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}