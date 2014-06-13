/**
 * Դ�������ƣ�ServiceInterfacePage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.service.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�yanwj06282
 */
package com.hundsun.ares.studio.jres.service.ui.page;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.ui.block.InputParameterBlock;
import com.hundsun.ares.studio.biz.ui.block.OutputParameterBlock;
import com.hundsun.ares.studio.biz.ui.block.ParameterDefineEditingSupportDecorator;
import com.hundsun.ares.studio.biz.ui.block.ServInterfaceParameterColLabProvider;
import com.hundsun.ares.studio.biz.ui.editor.page.InterfacePage;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalHelperWipeOffRepeatStd;
import com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalProvider;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.FormWidgetUtils;
import com.hundsun.ares.studio.ui.editor.editingsupport.JresTextEditingSupportWithContentAssist;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;

/**
 * @author yanwj06282
 *
 */
public class ServiceInterfacePage extends InterfacePage {

	/**
	 * @param interfaceFeature
	 * @param editor
	 * @param id
	 * @param title
	 */
	public ServiceInterfacePage(EStructuralFeature interfaceFeature,
			EMFFormEditor editor, String id, String title) {
		super(interfaceFeature, editor, id, title);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.ui.editor.page.InterfacePage#createInputSection(org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	protected Section createInputSection(Composite composite,
			FormToolkit toolkit) {
		Section section = toolkit.createSection(composite, FormWidgetUtils.getDefaultSectionStyles());
		section.setText("�������");
		
		Composite content = toolkit.createComposite(section);
		GridLayoutFactory.fillDefaults().applyTo(content);
		
		Composite block = toolkit.createComposite(content);
		GridDataFactory.fillDefaults().indent(0, 0).grab(true, true).applyTo(block);
		block.setLayout(new FillLayout());
		
		// �б�
		inputParamBlock = new InputParameterBlock(getEditingDomain(), getEditor().getARESResource(), getProblemPool()){
			protected void createColumnDefaultValue(com.hundsun.ares.studio.core.IARESProject project, org.eclipse.jface.viewers.TreeViewer viewer, com.hundsun.ares.studio.ui.editor.IDiagnosticProvider problemView) {

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
		};
		inputParamBlock.setEditableControl(getEditableControl());
		inputParamBlock.setDataType(getDataType());
		customizeInputParamBlock();
		inputParamBlock.createControl(block, toolkit);
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(inputParamBlock);
		
		addPropertyListener(inputParamBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(inputParamBlock);
		
		section.setClient(content);
		return section;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.ui.editor.page.InterfacePage#createOuputSection(org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	protected Section createOuputSection(Composite composite,
			FormToolkit toolkit) {
		Section section = toolkit.createSection(composite, FormWidgetUtils.getDefaultSectionStyles());
		section.setText("�������");
		
		Composite client = toolkit.createComposite(section);
		GridLayoutFactory.fillDefaults().applyTo(client);
		
		Composite block = toolkit.createComposite(client);
		GridDataFactory.fillDefaults().indent(0, 0).grab(true, true).applyTo(block);
		block.setLayout(new FillLayout());

		outputParamBlock = new OutputParameterBlock(getEditingDomain(), getEditor().getARESResource(), getProblemPool()){
			protected void createColumnDefaultValue(com.hundsun.ares.studio.core.IARESProject project, org.eclipse.jface.viewers.TreeViewer viewer, com.hundsun.ares.studio.ui.editor.IDiagnosticProvider problemView) {

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
		};
		outputParamBlock.setEditableControl(getEditableControl());
		outputParamBlock.setDataType(getDataType());
		customizeOutputParamBlock();
		outputParamBlock.createControl(block, toolkit);
		
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(outputParamBlock);
		
		addPropertyListener(outputParamBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(outputParamBlock);
		
		section.setClient(client);

		return section;		
	}
	
}
