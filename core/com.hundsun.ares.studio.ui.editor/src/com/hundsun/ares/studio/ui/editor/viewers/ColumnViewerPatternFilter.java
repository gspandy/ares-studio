/**
 * Դ�������ƣ�ColumnViewerPatternFilter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.viewers;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.PatternFilter;

/**
 * @author gongyf
 *
 */
public class ColumnViewerPatternFilter extends PatternFilter {
	
	public ColumnViewerPatternFilter(){
		//TASK #9491 ���б��ɸѡ���У�Ĭ��֧��ͨ���*
		setIncludeLeadingWildcard(true);
	}
	
    protected boolean isLeafMatch(Viewer viewer, Object element){
    	int columnCount = 0;
    	Control control = viewer.getControl();
    	if (control instanceof Tree) {
    		columnCount = ((Tree) control).getColumnCount();
		} else if (control instanceof Table) {
			columnCount = ((Table) control).getColumnCount();
		}
    	
    	for (int i = 0; i < columnCount; i++) {
    		CellLabelProvider labelProvider = ((ColumnViewer) viewer).getLabelProvider(i);
			if (labelProvider != null && labelProvider instanceof ColumnLabelProvider) {
				if (wordMatches( ((ColumnLabelProvider)labelProvider).getText(element))) {
					return true;
				}
			}
		}
    	
        return false;
    }
}
