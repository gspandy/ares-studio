/**
 * Դ�������ƣ�MetadataOverviewProjectColumnLabelProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.metadata.ui.viewer;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.ui.model.MetadataOverviewElement;

/**
 * �ṩ����ҳ����Ŀ�����ڹ�����
 * @author gongyf
 *
 */
public class MetadataOverviewProjectColumnLabelProvider extends
		ColumnLabelProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		IARESResource resource = ((MetadataOverviewElement) element).getResource();
		
		if (resource.getLib() != null) {
			// ���ð���
			return resource.getLib().getElementName();
		}
		return resource.getARESProject().getElementName();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		IARESResource resource = ((MetadataOverviewElement) element).getResource();
		if (resource.getLib() != null) {
			// ���ð���ͼ��
			return null;
		}
		
		// ���̵�ͼ��
		return null;
	}
	
	@Override
	public Color getBackground(Object element) {
		MetadataOverviewElement moe = (MetadataOverviewElement) element;
		if (moe.isConflict()) {
			return Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
		}
		return super.getBackground(element);
	}
}
