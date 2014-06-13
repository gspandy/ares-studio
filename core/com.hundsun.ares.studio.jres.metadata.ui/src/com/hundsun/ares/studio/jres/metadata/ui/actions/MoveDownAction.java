/**
 * Դ�������ƣ�MoveDownAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.actions;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hundsun.ares.studio.jres.metadata.ui.MetadataUI;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataViewerUtil;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.UncategorizedItemsCategoryImpl;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerMoveAction;

/**
 * ����
 * @author gongyf
 *
 */
public class MoveDownAction extends ColumnViewerMoveAction {

	/**
	 * @param viewer
	 * @param editingDomain
	 * @param moveUp
	 */
	public MoveDownAction(ColumnViewer viewer, EditingDomain editingDomain) {
		super(viewer, editingDomain, false);
		setText("����");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/down.gif"));
		setId(IMetadataActionIDConstant.CV_MOVE_DOWN);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.actions.ColumnViewerMoveAction#getOwner()
	 */
	@Override
	protected EObject getOwner() {
		
		if (MetadataViewerUtil.isShowCategory(getViewer())) {
			MetadataCategory category = MetadataViewerUtil.getSelectedCategory(getViewer(), true);
			if (category instanceof UncategorizedItemsCategoryImpl) {
				return null;
			}
			return category;
		} else {
			return MetadataViewerUtil.getMetadataModel(getViewer());
		}
	}

	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.actions.ColumnViewerMoveAction#getReference()
	 */
	@Override
	protected EReference getReference() {
		if (MetadataViewerUtil.isShowCategory(getViewer())) {
			// �������ƶ����黹���ƶ���Ŀ
			List<Object> objects = getSelectedObjects();
			if (objects.size() > 0) {
				if (objects.get(0) instanceof MetadataItem) {
					return MetadataPackage.Literals.METADATA_CATEGORY__ITEMS;
				} else if (objects.get(0) instanceof MetadataCategory) {
					return MetadataPackage.Literals.METADATA_CATEGORY__CHILDREN;
				}
			}
		} else {
			return MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS;
		}
		
		return null;
	}

}
