/**
 * Դ�������ƣ�AddSiblingCategoryAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.actions;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hundsun.ares.studio.jres.metadata.ui.MetadataUI;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataViewerUtil;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.UncategorizedItemsCategoryImpl;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataFactory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerAction;

/**
 * ���ͬ������
 * @author gongyf
 *
 */
public class AddSiblingCategoryAction extends ColumnViewerAction {

	/**
	 * @param viewer
	 * @param editingDomain
	 */
	public AddSiblingCategoryAction(ColumnViewer viewer,
			EditingDomain editingDomain) {
		super(viewer, editingDomain);
		
		setText("��ӷ���");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/addCategory.png"));
		setId(IMetadataActionIDConstant.CV_ADD_SLIBING_CATEGORY);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.actions.ColumnViewerAction#createCommand()
	 */
	@Override
	protected Command createCommand() {
		if (MetadataViewerUtil.isShowCategory(getViewer())) {
			MetadataCategory category = MetadataViewerUtil.getSelectedCategory(getViewer(), false);
			EObject container = null;
			if (category == null) {
				return null;
			}
			if (category instanceof UncategorizedItemsCategoryImpl) {
				// �����δ����ڵ㣬��Ӧ������ӷ��ൽ����
				container = MetadataViewerUtil.getMetadataModel(getViewer()).getRoot();
			} else {
				container = category.eContainer();
			}
			return AddCommand.create(getEditingDomain(), container, 
					MetadataPackage.Literals.METADATA_CATEGORY__CHILDREN, MetadataFactory.eINSTANCE.createMetadataCategory());
		}
		return null;
	}

}
