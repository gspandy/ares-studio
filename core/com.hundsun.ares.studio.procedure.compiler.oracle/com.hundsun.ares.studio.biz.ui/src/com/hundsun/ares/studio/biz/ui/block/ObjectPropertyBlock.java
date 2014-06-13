/**
 * Դ�������ƣ�ObjectPropertyBlock.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.ui.block;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalHelperWipeOffRepeatStd;
import com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalProvider;
import com.hundsun.ares.studio.ui.ColumnViewerHoverToolTip;
import com.hundsun.ares.studio.ui.editor.IDiagnosticProvider;
import com.hundsun.ares.studio.ui.editor.editingsupport.JresTextEditingSupportWithContentAssist;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;
import com.hundsun.ares.studio.ui.validate.IProblemPool;

/**
 * ���������б�ı༭Block
 * @author sundl
 *
 */
public class ObjectPropertyBlock extends ParameterViewerBlock {

	/**
	 * @param reference
	 * @param editingDomain
	 * @param resource
	 * @param problemPool
	 */
	public ObjectPropertyBlock(EditingDomain editingDomain, IARESResource resource, IProblemPool problemPool) {
		super(BizPackage.Literals.ARES_OBJECT__PROPERTIES, editingDomain, resource, problemPool);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock#getHeadColumnFeature()
	 */
	@Override
	protected EStructuralFeature getHeadColumnFeature() {
		return BizPackage.Literals.PARAMETER__ID;
	}
	
	@Override
	protected void addToolTipSupport(TreeViewer viewer) {
		ColumnViewerHoverToolTip.enableFor(viewer);
	}

	/**
	 * ������Ĭ��ֵ����
	 */
	protected void createColumnDefaultValue(IARESProject project, TreeViewer viewer, IDiagnosticProvider problemView) {
		// ����������
		EAttribute attribute = BizPackage.Literals.PARAMETER__DEFAULT_VALUE;
		
		// ���������
		TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("Ĭ��ֵ");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		
		// ���ñ�ǩ�ṩ��
		EObjectColumnLabelProvider provider = new ServInterfaceParameterColLabProvider(resource,attribute);
		provider.setDiagnosticProvider(problemView);
		column.setLabelProvider(provider);
		
		// ���ñ༭֧��
		MetadataContentProposalHelperWipeOffRepeatStd helper = new MetadataContentProposalHelperWipeOffRepeatStd(resource.getARESProject());
		MetadataContentProposalProvider proposalProvider = new MetadataContentProposalProvider(helper, IMetadataRefType.DefValue, resource.getARESProject());
		
		JresTextEditingSupportWithContentAssist es = new JresTextEditingSupportWithContentAssist(
				viewer,
				attribute, 
				proposalProvider);
		es.setDecorator(new ParameterDefineEditingSupportDecorator(project, attribute));
		column.setEditingSupport(es);
	}
	
}
