/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.ui.editor.decorators;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewerColumn;

import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlockDecorator;

/**
 * @author gongyf
 *
 */
public class ColumnSelectableBlockDecorator<T extends ColumnViewer> extends ColumnViewerBlockDecorator<T> {
	
	public void decorateViewer(ColumnViewerBlock<T> block, T viewer) {
		TableViewerColumn s;
		//s.getColumn().addSelectionListener(listener)
	}
}
