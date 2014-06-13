/**
 * Դ�������ƣ�DatabaseOpenResourcePage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.blocks;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.CorePackage;

/**
 * @author yanwj06282
 *
 */
public class OpenResourcePage extends FormPage {

	private String[] stitles = new String[]{"�����","Ӣ����","������"};
	private EStructuralFeature[] sfeatures = new EStructuralFeature[]{CorePackage.Literals.BASIC_RESOURCE_INFO__OBJECT_ID,
			CorePackage.Literals.BASIC_RESOURCE_INFO__NAME,CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME};
	private OpenResourceBlock block;
	private IARESElement element;
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public OpenResourcePage(FormEditor editor, IARESElement element ,String id, String title) {
		super(editor, id, title);
		this.element = element;
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {

		Composite body = managedForm.getForm().getBody();
		FormToolkit toolkit = managedForm.getToolkit();
		
		createComposite(body, toolkit);
		body.setLayout(new FillLayout());
	}

	/**
	 * @param body
	 * @param toolkit
	 */
	private void createComposite(Composite body, FormToolkit toolkit) {
		block = new OpenResourceBlock(element ,stitles ,sfeatures);
		block.createControl(body, toolkit);
		IARESResource[] resources = null;
		List<OpenResourceInfo> databaseList = new ArrayList<OpenResourceInfo>();
		try {
			if(element instanceof IARESModuleRoot) {
				resources = ((IARESModuleRoot) element).getResources();
			}else if (element instanceof IARESModule) {
				resources = ((IARESModule) element).getARESResources(true);
			}
			
			for (IARESResource resource : resources) {
				EObject dbinfo = resource.getInfo(EObject.class);
				if (dbinfo != null) {
					OpenResourceInfo info = new OpenResourceInfo(resource, dbinfo);
					databaseList.add(info);
				}
			}
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
		block.setInput(databaseList);
	}
	
}
