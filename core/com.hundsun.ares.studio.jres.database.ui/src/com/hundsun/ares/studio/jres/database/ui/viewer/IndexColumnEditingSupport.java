/**
 * Դ�������ƣ�IndexColumnEditingSupport.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���С��
 */
package com.hundsun.ares.studio.jres.database.ui.viewer;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.database.ui.editors.celleditor.TableIndexColomnsCellEditor;
import com.hundsun.ares.studio.ui.editor.editingsupport.EMFEditingSupport;

/**
 * �������ֶ��б�ı༭֧��
 * 
 * @author wangxh
 * 
 */
public class IndexColumnEditingSupport extends EMFEditingSupport {

	IARESResource resource;
	/**
	 * @param viewer
	 * @param feature
	 * @param resource
	 */
	public IndexColumnEditingSupport(ColumnViewer viewer,
			EStructuralFeature feature,IARESResource resource) {
		super(viewer, feature);
		this.resource = resource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hundsun.ares.studio.jres.ui.editingsupports.BaseEditingSupport#
	 * createCellEditor()
	 */
	@Override
	protected CellEditor createCellEditor() {
		return new TableIndexColomnsCellEditor(getViewer(), resource,"���ֶ�", "��ѡ��");
	}
}
