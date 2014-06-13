/**
 * Դ�������ƣ�RemoveAction.java
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
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataViewerUtil;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.UncategorizedItemsCategoryImpl;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.impl.MetadataCategoryImpl;
import com.hundsun.ares.studio.jres.model.metadata.impl.MetadataItemImpl;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerAction;

/**
 * �Ƴ���ǰ�������Ŀ�����Ǵ���Ŀ�����ᱻɾ��
 * @author gongyf
 *
 */
public class RemoveAction extends ColumnViewerAction {

	/**
	 * @param viewer
	 * @param editingDomain
	 */
	public RemoveAction(ColumnViewer viewer, EditingDomain editingDomain) {
		super(viewer, editingDomain);
		setId(IMetadataActionIDConstant.CV_REMOVE);
		setText("�ӷ����Ƴ�");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.actions.ColumnViewerAction#createCommand()
	 */
	@Override
	protected Command createCommand() {
		if (MetadataViewerUtil.isShowCategory(getViewer())) {
			ITreeSelection selection = (ITreeSelection) getViewer().getSelection();
			CompoundCommand cmd = new CompoundCommand();
			if (selection != null && !selection.isEmpty()){
				TreePath[] paths = selection.getPaths();{
					for(TreePath path:paths){
						int count=path.getSegmentCount();
						if(count>=2){
							Object item=path.getSegment(count-1);
							Object category = path.getSegment(count-2);
							if(item instanceof MetadataItemImpl && category instanceof MetadataCategoryImpl){
								if(category instanceof UncategorizedItemsCategoryImpl){//ѡ��δ�����µ���Ŀ
									return null;
								}
								else{
									//��ѡ����Ŀ�ӵ�ǰ����������δ������
//									MetadataResourceData<?> data=(MetadataResourceData<?>) ((MetadataItemImpl)item).eContainer();
//									UncategorizedItemsCategoryImpl uncateCategory = new UncategorizedItemsCategoryImpl(data);
//									Command addcmd = AddCommand.create(getEditingDomain(), uncateCategory, MetadataPackage.Literals.METADATA_CATEGORY__ITEMS, item);
//									cmd.append(addcmd);
									Command removecmd= RemoveCommand.create(getEditingDomain(), category, MetadataPackage.Literals.METADATA_CATEGORY__ITEMS, item);
									cmd.append(removecmd);
								}
							}
							else{//ѡ�����к��ӷ����ļ���
								return null;
							}
						}
						else{//ѡ�����к�һ�������ļ���
							return null;
						}
					}
				}
			}			
			return cmd;//RemoveCommand.create(getEditingDomain(), selectedObjects);
		}
		return null;
	}
	
	

}
