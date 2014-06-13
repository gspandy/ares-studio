/**
 * Դ�������ƣ�AddItemAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.actions;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hundsun.ares.studio.jres.metadata.ui.MetadataUI;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataViewerUtil;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.UncategorizedItemsCategoryImpl;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerAction;

/**
 * �����Ŀ����
 * @author gongyf
 *
 */
public class InsertItemAction extends ColumnViewerAction {

	private EClass itemClass;
	
	/**
	 * @param itemClass the itemClass to set
	 */
	public void setItemClass(EClass itemClass) {
		this.itemClass = itemClass;
	}

	/**
	 * @param viewer
	 * @param editingDomain
	 */
	public InsertItemAction(ColumnViewer viewer, EditingDomain editingDomain, EClass itemClass) {
		super(viewer, editingDomain);

		this.itemClass = itemClass;
		setText("������Ŀ");
		setId(IMetadataActionIDConstant.CV_INSERT_ITEM);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/insert.png"));
		
		//setAccelerator(SWT.INSERT);
	}

	public void setActionDefinitionId(String id) {
		super.setActionDefinitionId(id);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.actions.ColumnViewerAction#createCommand()
	 */
	@Override
	protected Command createCommand() {
		EObject item = itemClass.getEPackage().getEFactoryInstance().create(itemClass);
		MetadataResourceData<?> owner = (MetadataResourceData<?>) getViewer().getInput();
		CompoundCommand command = new CompoundCommand(getText());
		
		if (owner == null) {
			return null;
		}
		
		Object position = ((IStructuredSelection)getViewer().getSelection()).getFirstElement();
		int index = owner.getItems().indexOf(position);
		
		// �������û��ѡ���ʱ��ӵ���󣬷���ӵ�ѡ���е�λ��
		if (index < 0) {
			command.append(AddCommand.create(getEditingDomain(), owner, MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS, item));
		} else {
			command.append(AddCommand.create(getEditingDomain(), owner, MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS, item, index));
		}
		
		if (MetadataViewerUtil.isShowCategory(getViewer())) {
			MetadataCategory category = MetadataViewerUtil.getSelectedCategory(getViewer(), false);
			// �����root�ķ��࣬��Ӧ�����
			if (!(category instanceof UncategorizedItemsCategoryImpl)
					&& category != owner.getRoot()) {
				
				index = category.getItems().indexOf(position);
				if (index < 0 ) {
					command.append(AddCommand.create(getEditingDomain(), category, MetadataPackage.Literals.METADATA_CATEGORY__ITEMS, item));
				} else {
					command.append(AddCommand.create(getEditingDomain(), category, MetadataPackage.Literals.METADATA_CATEGORY__ITEMS, item, index));
				}
				
			}
		}

		return command.unwrap();
	}

}
