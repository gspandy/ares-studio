/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.jres.clearinghouse;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;

import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlockDecorator;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;

/**
 * ɾ��Ĭ�ϵ��޸�������һ�У���һ�л�����չ������ʾ
 * @author gongyf
 *
 */
public class TableHistoryColumnViewerBlockDecorator extends
		ColumnViewerBlockDecorator<TreeViewer> {
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerBlockDecorator#inputChanged(com.hundsun.ares.studio.jres.ui.pages.ColumnViewerBlock)
	 */
	@Override
	public void inputChanged(ColumnViewerBlock<TreeViewer> block) {
		super.inputChanged(block);
		if (block.getColumnViewer().getInput() instanceof TableResourceData) {
			// ��������ݿ��༭�����޸�������һ������չ�ģ�ԭʼ������Ҫ����
			Tree tree = block.getColumnViewer().getTree();
			for (int i = 0; i < tree.getColumnCount(); i++) {
				CellLabelProvider lp = block.getColumnViewer().getLabelProvider(i);
				if (lp instanceof EObjectColumnLabelProvider) {
					if (CorePackage.Literals.REVISION_HISTORY__MODIFIED.equals(((EObjectColumnLabelProvider) lp).getEStructuralFeature(null))   ) {
						tree.getColumn(i).dispose();
						break;
					}
				}
			}
			
		}
	}
}
