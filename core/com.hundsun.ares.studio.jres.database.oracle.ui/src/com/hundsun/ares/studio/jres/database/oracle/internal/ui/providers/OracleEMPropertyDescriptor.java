/**
 * Դ�������ƣ�OracleEMPropertyDescriptor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.oracle.internal.ui.providers;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.ui.cellEditor.AresContentProposalProvider;
import com.hundsun.ares.studio.ui.editor.extend.ModelTextCellEditorWithContentAssist;
import com.hundsun.ares.studio.ui.editor.extend.TextEMPropertyDescriptor;

/**
 * @author yanwj06282
 *
 */
public class OracleEMPropertyDescriptor extends TextEMPropertyDescriptor {

	private AresContentProposalProvider proposalProvider;
	
	/**
	 * @param proposalProvider
	 * @param structuralFeature
	 */
	public OracleEMPropertyDescriptor(
			AresContentProposalProvider proposalProvider,
			EStructuralFeature structuralFeature) {
		super(structuralFeature);
		this.proposalProvider = proposalProvider;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.extend.IExtensibleModelPropertyDescriptor#createPropertyEditor(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return new ModelTextCellEditorWithContentAssist(parent,proposalProvider){
			@Override
			public IContentProposalProvider getProposalProvider() {
				return proposalProvider;
			}
		};
	}
	
}
