/**
 * Դ�������ƣ�PasteAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.actions;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataViewerUtil;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.UncategorizedItemsCategoryImpl;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerAction;
import com.hundsun.ares.studio.ui.editor.actions.IActionIDConstant;
import com.hundsun.ares.studio.ui.util.ARESEMFClipboard;

/**
 * ճ��
 * @author sundl
 */
public class PasteAction extends ColumnViewerAction {

	private EClass cateClass;
	private EClass itemClass;
	
	/**
	 * 
	 * @param viewer
	 * @param editingDomain
	 * @param cateClass ���������
	 * @param itemClass ��Ŀ������
	 */
	public PasteAction(ColumnViewer viewer, EditingDomain editingDomain, EClass cateClass, EClass itemClass) {
		super(viewer, editingDomain);
		setText("ճ��");
		setEnabled(false);
		
		setId(IActionIDConstant.CV_PASTE);
		
		this.cateClass = cateClass;
		this.itemClass = itemClass;
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
	}

	@Override
	protected Command createCommand() {
		EditingDomain domain = getEditingDomain();
		if (domain == null)
			return null;
		
		Command command = null;

		// ����ճ�������������4��
		// 1.������Ԫ������Ŀ��ճ����������
		// �����Щ��Ŀ��������ѡ�еķ���
		
		// 2.������Ԫ���ݷ��飬ճ����������
		// ����·��飬ԭ�����µ���Ŀ�������˴����������⣩
		
		// 3.������Ԫ������Ŀ��ճ����δ�����£���������ʾ�����ʱ��
		// ��Ӹ�����Ŀ
		
		// 4.������Ԫ���ݷ��飬ճ����Ϊ�����£���������ʾ�����ʱ��
		// �޷�Ӧ
		
		List<MetadataCategory> copiedCategories = (List<MetadataCategory>) ARESEMFClipboard.getInstance().pasteFromClipboard(cateClass.getInstanceClass());
		List<MetadataItem> copiedItems = (List<MetadataItem>) ARESEMFClipboard.getInstance().pasteFromClipboard(itemClass.getInstanceClass());
		
		if (MetadataViewerUtil.isShowCategory(getViewer()) ) {
			MetadataCategory cate = MetadataViewerUtil.getSelectedCategory(getViewer(), false);
			// ����getSelectedCategory����û��ѡ�������·���root���࣬����ճ������������£����ִ��������������Ŀ�ڽ����Ͽ�����
			MetadataResourceData<?> model = MetadataViewerUtil.getMetadataModel(getViewer());
			if (model == null) // ��������ĵ�ʱ��setInput֮ǰ����Ҳ����õ�
				return null;
			
			if (cate == model.getRoot())
				cate = MetadataViewerUtil.getUncategorizedCategory(getViewer());
			
			if (!copiedCategories.isEmpty()) {
				// �����Ƶ��Ƿ����򽫷��鸺��ѡ�еķ����£�������Ŀ���ٽ��и���
				if (cate instanceof UncategorizedItemsCategoryImpl) {
					command = createCommandForCopyCategoriesToNotCategory(copiedCategories, MetadataViewerUtil.getMetadataModel(getViewer()));
				} else {
					command = createCommandForCopyCategoriesToCategory(copiedCategories, cate);
				}
			} else if (!copiedItems.isEmpty()) {
				// ��������Ŀ
				if (cate instanceof UncategorizedItemsCategoryImpl) {
					command = createCommandForCopyItemsToNotCategory(copiedItems, MetadataViewerUtil.getMetadataModel(getViewer()));
				} else {
					command = createCommandForCopyItemsToCategory(copiedItems, cate);
				}
			}
			
		} else {
			if (!copiedCategories.isEmpty()) {
				// �����Ƶ��Ƿ����򽫷��鸺��ѡ�еķ����£�������Ŀ���ٽ��и���
				command = createCommandForCopyCategoriesToNotCategory(copiedCategories, MetadataViewerUtil.getMetadataModel(getViewer()));
			} else if (!copiedItems.isEmpty()) {
				// ��������Ŀ
				command = createCommandForCopyItemsToNotCategory(copiedItems, MetadataViewerUtil.getMetadataModel(getViewer()));
			}
		}
		return command;
	}

	/**
	 * 
	 * @param categories �޷�������������Ŀ����ɾ��
	 * @param container
	 * @return
	 */
	private Command createCommandForCopyCategoriesToCategory(Collection<? extends MetadataCategory> categories, MetadataCategory container) {
		// �ж��Ƿ���һ���ļ��н��и���
		for (MetadataCategory category : categories) {
			if (!category.getItems().isEmpty()) {
				// FIXME ������Ҫ�ܹ���proxy�����ý��и���
				EObject obj = category.getItems().get(0);
				if (EcoreUtil.resolve(obj,(EObject)null) == obj) {
					// ����޷�������������Ŀ���������Ŀ�ĸ���
					// ��Ϊ��������ǴӼ��������ģ��Ѿ��Ǹ��Ƶ��ˣ������޸�û�й�ϵ
					category.getItems().clear();
				}
			}
		}

		Command command = AddCommand.create(getEditingDomain(), container, MetadataPackage.Literals.METADATA_CATEGORY__CHILDREN, categories);
		return command;
	}
	
	private Command createCommandForCopyCategoriesToNotCategory(Collection<? extends MetadataCategory> categories, MetadataResourceData<?> container) {
		return null;
	}
	
	private Command createCommandForCopyItemsToCategory(Collection<? extends MetadataItem> items, MetadataCategory container) {
		CompoundCommand command = new CompoundCommand();
		// ����ӵ����б�
		command.append(AddCommand.create(getEditingDomain(), 
				EcoreUtil.getRootContainer(container), 
				MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS, items))	;
		// ����ӵ�����
		command.append(AddCommand.create(getEditingDomain(), container,
				MetadataPackage.Literals.METADATA_CATEGORY__ITEMS, items));
		return command;
	}
	
	private Command createCommandForCopyItemsToNotCategory(Collection<? extends MetadataItem> items, MetadataResourceData<?> container) {
		Command command = AddCommand.create(getEditingDomain(), container, MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS, items);
		return command;
	}

}
