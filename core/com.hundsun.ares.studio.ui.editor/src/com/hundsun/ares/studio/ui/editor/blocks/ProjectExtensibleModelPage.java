/**
 * 
 */
package com.hundsun.ares.studio.ui.editor.blocks;

import java.util.EventObject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.Section;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.model.Constants;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.core.model.ProjectExtensibleModel;
import com.hundsun.ares.studio.internal.core.ARESProjectProperty;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelComposite;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelUtils;

/**
 * @author gongyf
 *
 */
public class ProjectExtensibleModelPage extends EMFExtendSectionScrolledFormPage<ARESProjectProperty> {

	private ExtensibleModelComposite emc;



	public ProjectExtensibleModelPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	@Override
	protected EClass getEClass() {
		return CorePackage.Literals.PROJECT_EXTENSIBLE_MODEL;
	}

	@Override
	protected String getMapKey() {
		return Constants.PROJECT_EXTENSIBLEMODEL_KEY;
	}

	@Override
	protected ProjectExtensibleModel getModel() {
		return (ProjectExtensibleModel) super.getModel();
	}
	
	@Override
	protected void createSections(IManagedForm managedForm) {
		Section basicSection = createCompositeSection("�û�����", true);
		
		emc = new ExtensibleModelComposite(basicSection, formToolkit);
		emc.setInput(getARESProject(), getModel());
		
		basicSection.setClient(emc);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.EMFExtendSectionScrolledFormPage#processModelOnCreate(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected void processModelOnCreate(EObject model) {
		super.processModelOnCreate(model);
		ExtensibleModelUtils.extend(getARESProject(), (ExtensibleModel) model, false);
	}
	
	@Override
	public void commandStackChanged(EventObject event) {
		super.commandStackChanged(event);
		emc.refresh();
	}
	
	// FIXME �˴�Ϊ���ݿ��ǣ����ⳣ���ڿ�ܲ����������ظ�����
	// �����Ĵ���������Ҫ��չҳ��Ĺ��̶���Ҫ�Լ�����ARESCore.EXTEND_NATURE
	private static String MODULE_NATURE = "com.hundsun.ares.studio.jres.core.modulenature";
	private static String PRODUCT_NATURE = "com.hundsun.ares.studio.jres.core.productnature";

	@Override
	public boolean shouldLoad() {
		
		try {
			IProject project = getARESProject().getProject();
			if (project.hasNature(ARESCore.EXTEND_NATURE)) {
				return true;
			}else {
				if(project.hasNature(MODULE_NATURE) || project.hasNature(PRODUCT_NATURE)) {
					return true;
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return false;
	}


}
