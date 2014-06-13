/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.ui.editor.blocks;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ColumnViewer;

import com.hundsun.ares.studio.ui.editor.actions.ButtonGroupManager;

/**
 * װ���������Դ���ķ�ʽ��̬�޸�Block�еı�񣬲˵�����������ť��...
 * @author gongyf
 *
 * @param <T>
 */
public interface IColumnViewerBlockDecorator<T extends ColumnViewer> {

	public abstract void initialize(IDialogSettings settings);

	public abstract void dispose();

	/**
	 * װ�α����ͼ������ʱ�����Ҳ�Ѿ��������
	 * 
	 * @param block
	 * @param viewer
	 */
	public abstract void decorateViewer(ColumnViewerBlock<T> block, T viewer);

	/**
	 * 
	 * װ���Ҽ��˵�
	 * @param block
	 * @param menuManager
	 */
	public abstract void decorateMenu(ColumnViewerBlock<T> block, IMenuManager menuManager);

	/**
	 * װ���Ҳఴť��
	 * @param block
	 * @param manager
	 */
	public abstract void decorateButtons(ColumnViewerBlock<T> block,	ButtonGroupManager manager);
	
	/**
	 * ����Toolbar���滻ԭ����ButtonManager��
	 * @param block
	 * @param manager
	 */
	void decorateToolbar(ColumnViewerBlock<T> block, ToolBarManager manager);
	
	/**
	 * input�仯��
	 * @param block
	 */
	public abstract void inputChanged(ColumnViewerBlock<T> block);

}