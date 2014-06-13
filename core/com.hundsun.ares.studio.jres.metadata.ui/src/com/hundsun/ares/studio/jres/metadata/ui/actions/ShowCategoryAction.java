/**
 * Դ�������ƣ�ShowCategoryAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataTreeContentProvider;

/**
 * ������ʾ����
 * @author gongyf
 *
 */
public class ShowCategoryAction extends Action {
	private ColumnViewer viewer;

	/**
	 * @param viewer
	 */
	public ShowCategoryAction(ColumnViewer viewer) {
		super("Show Category", AS_CHECK_BOX);
		this.viewer = viewer;
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER));
		setId(IMetadataActionIDConstant.CV_SHOW_CATEGORY);
		
		IContentProvider provider = getViewer().getContentProvider();
		if (provider instanceof MetadataTreeContentProvider ) {
			boolean showCategory = ((MetadataTreeContentProvider) provider).isShowCategory();
			setChecked(showCategory);
		}
	}
	
	
	@Override
	public String getText() {
		boolean showCategory = false;
		IContentProvider provider = getViewer().getContentProvider();
		if (provider instanceof MetadataTreeContentProvider ) {
			showCategory = ((MetadataTreeContentProvider) provider).isShowCategory();
		}
		
		return showCategory ? "���ط���" : "��ʾ����";
	}
	
	/**
	 * @return the viewer
	 */
	public ColumnViewer getViewer() {
		return viewer;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		IContentProvider provider = getViewer().getContentProvider();
		if (provider instanceof MetadataTreeContentProvider ) {
			boolean showCategory = ((MetadataTreeContentProvider) provider).isShowCategory();
			if (showCategory) {
				((MetadataTreeContentProvider) provider).setShowCategory(false);
			} else {
				((MetadataTreeContentProvider) provider).setShowCategory(true);
			}
			
		}
		
		getViewer().refresh();
		
		// 2012��2��15��15:59:24 ��Ҷ��
		// BUG #2449::��׼�ֶε�Ԫ������Դ�Ҽ��˵�����ʾ���顱���Ʋ���
		// ��Ҫ��ʽ�����ı�
		setText(getText());
	}
}
