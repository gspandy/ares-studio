/**
 * Դ�������ƣ�JresTextEditingSupportWithContentAssist.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.editingsupport;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.ui.cellEditor.AresContentProposalProvider;
import com.hundsun.ares.studio.ui.cellEditor.TextCellEditorWithContentAssist;
import com.hundsun.ares.studio.ui.editor.viewers.IEStructuralFeatureProvider;

public class JresTextEditingSupportWithContentAssist extends TextEditingSupport{

	private ColumnViewer columnViewer;
	private IContentProposalProvider proposalProvider;
	
	public JresTextEditingSupportWithContentAssist(ColumnViewer viewer, EStructuralFeature feature, IContentProposalProvider proposalProvider) {
		super(viewer, feature);
		this.columnViewer = viewer;
		this.proposalProvider = proposalProvider;
	}

	public JresTextEditingSupportWithContentAssist(ColumnViewer viewer, IEStructuralFeatureProvider featureProvider, AresContentProposalProvider proposalProvider) {
		super(viewer, featureProvider);
		this.columnViewer = viewer;
		this.proposalProvider = proposalProvider;
	}
	
	@Override
	protected CellEditor createCellEditor() {
		return new TextCellEditorWithContentAssist((Composite) columnViewer.getControl()) {
			@Override
			public IContentProposalProvider getProposalProvider() {
				return proposalProvider;
			}
		};
	}

	@Override
	protected CellEditor doGetCellEditor(Object element) {
		TextCellEditorWithContentAssist celleditor = (TextCellEditorWithContentAssist) super.doGetCellEditor(element);
		if (celleditor.getProposalProvider() instanceof AresContentProposalProvider) {
			AresContentProposalProvider proposalProvider = (AresContentProposalProvider) celleditor.getProposalProvider();
			proposalProvider.updateContent(element);
		}

		return celleditor;
	}
	
}
