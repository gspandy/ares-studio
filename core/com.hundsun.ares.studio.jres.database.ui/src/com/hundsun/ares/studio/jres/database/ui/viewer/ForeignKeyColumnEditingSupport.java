/**
 * Դ�������ƣ�ForeignKeyColumnEditingSupport.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.viewer;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.database.ui.editors.celleditor.TableForeignKeyColumnCellEditor;
import com.hundsun.ares.studio.ui.editor.editingsupport.EMFEditingSupport;

/**
 * @author liaogc
 * 
 */
public class ForeignKeyColumnEditingSupport extends EMFEditingSupport {
	
	private IARESResource resource;

	/**
	 * @param viewer
	 * @param feature
	 * @param resource
	 */
	public ForeignKeyColumnEditingSupport(ColumnViewer viewer,
			EStructuralFeature feature, IARESResource resource) {
		super(viewer, feature);
		this.resource = resource;
	}

	@Override
	protected CellEditor createCellEditor() {
		return new TableForeignKeyColumnCellEditor(getViewer(), resource, "���ֶ�",	"���");
	}

}
