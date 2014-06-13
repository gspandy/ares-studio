/**
 * Դ�������ƣ�TypeDefaultValueListViewBlock.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.block;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.forms.editor.FormPage;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.util.Pair;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.metadata.ui.Language;
import com.hundsun.ares.studio.jres.metadata.ui.LanguageRegister;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataOverviewColumnLabelProvider;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataOverviewHeaderColumnLabelProvider;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataOverviewMapColumnLabelProvider;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataOverviewProjectColumnLabelProvider;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeTypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil;
import com.hundsun.ares.studio.ui.ColumnSelectSorterListener;
import com.hundsun.ares.studio.ui.editor.viewers.BaseEObjectColumnLabelProvider;
import com.hundsun.ares.studio.ui.validate.IProblemPool;

/**
 * @author yanwj06282
 *
 */
public class TypeDefaultValueListOverviewViewBlock extends MetadataListOverviewViewerBlock {

	/**
	 * @param page
	 * @param editingDomain
	 * @param site
	 * @param resource
	 * @param problemPool
	 */
	public TypeDefaultValueListOverviewViewBlock(FormPage page,
			EditingDomain editingDomain, IWorkbenchPartSite site,
			IARESResource resource, IProblemPool problemPool) {
		super(page, editingDomain, site, resource, problemPool);
	}

	@Override
	protected void createColumns(TreeViewer viewer) {
		final TreeViewer treeViewer = (TreeViewer) viewer;
		
		/**ID*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.NAMED_ELEMENT__NAME;
			
			// ���������
			TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.LEFT);
			column.getColumn().setText("ID");
			column.getColumn().setWidth(120);
			column.getColumn().setMoveable(true);
			
			// ���ñ�ǩ�ṩ��
			ColumnLabelProvider provider = new MetadataOverviewHeaderColumnLabelProvider(attribute);
			column.setLabelProvider(provider);
			column.getColumn().addSelectionListener(new ColumnSelectSorterListener(column, provider));
			
		}
		/**����*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME;
			
			// ���������
			TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.LEFT);
			column.getColumn().setText("����");
			column.getColumn().setWidth(120);
			column.getColumn().setMoveable(true);
			
			// ���ñ�ǩ�ṩ��
			ColumnLabelProvider provider = new MetadataOverviewColumnLabelProvider(attribute);
			column.setLabelProvider(provider);
			column.getColumn().addSelectionListener(new ColumnSelectSorterListener(column, provider));
			
		}
		/**������Ŀ*/
		{
			// ���������
			TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.LEFT);
			column.getColumn().setText("������Ŀ");
			column.getColumn().setWidth(200);
			// ���ñ�ǩ�ṩ��
			ColumnLabelProvider provider = new MetadataOverviewProjectColumnLabelProvider();
			column.setLabelProvider(provider);
			column.getColumn().addSelectionListener(new ColumnSelectSorterListener(column, provider));
			column.getColumn().setMoveable(true);
		}
		/**Ĭ��ֵ����*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.METADATA_ITEM__REF_ID;
			
			// ���������
			TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.LEFT);
			column.getColumn().setText("Ĭ��ֵ����");
			column.getColumn().setWidth(120);
			column.getColumn().setMoveable(true);
			
			// ���ñ�ǩ�ṩ��
			ColumnLabelProvider provider = new MetadataOverviewColumnLabelProvider(attribute);
			column.setLabelProvider(provider);
			column.getColumn().addSelectionListener(new ColumnSelectSorterListener(column, provider));
		}
		/**��������*/
		{
			// ����������
			EReference reference = CorePackage.Literals.EXTENSIBLE_MODEL__DATA;
			
			Language[] languages = LanguageRegister.getInstance().getRegisteredLanguages();
			for (Language language : languages) {
				
				// ���������
				TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.LEFT);
				column.getColumn().setText(language.getName());
				column.getColumn().setWidth(100);
				column.getColumn().setMoveable(true);
				
				// ���ñ�ǩ�ṩ��
				ColumnLabelProvider provider = 
					new MetadataOverviewMapColumnLabelProvider(reference, language.getId());
				column.setLabelProvider(provider);
				column.getColumn().addSelectionListener(new ColumnSelectSorterListener(column, provider));
				
			}
		}

		
		/**��ע*/
		{
			// ����������
			EAttribute attribute = MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION;
			
			// ���������
			TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.LEFT);
			column.getColumn().setText("��ע");
			column.getColumn().setWidth(200);
			column.getColumn().setMoveable(true);
			
			// ���ñ�ǩ�ṩ��
			ColumnLabelProvider provider = new MetadataOverviewColumnLabelProvider(attribute);
			column.setLabelProvider(provider);
			column.getColumn().addSelectionListener(new ColumnSelectSorterListener(column, provider));
			
		}
	}
	
	@Override
	protected String getReferenceType() {
		return IMetadataRefType.DefValue;
	}
	
	@Override
	public Pair<EObject, IARESResource> getLinkedObject(ViewerCell cell) {
		BaseEObjectColumnLabelProvider labelProvider = (BaseEObjectColumnLabelProvider) getColumnViewer().getLabelProvider(cell.getColumnIndex());
		
		EStructuralFeature feature = labelProvider.getEStructuralFeature(cell.getElement());
		Pair<MetadataItem, IARESResource> pair = (Pair<MetadataItem, IARESResource>) cell.getElement();
		DeTypeDefaultValue d = MetadataUtil.decrypt((TypeDefaultValue) pair.first, pair.second);

		if (MetadataPackage.Literals.METADATA_ITEM__REF_ID.equals(feature)) {
			if(((Pair)d.getResolvedItem()).equals(pair)){
				return null;
			}
			return (Pair)d.getResolvedItem();
		} 
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.block.MetadataListViewerBlock#getID()
	 */
	@Override
	protected String getID() {
		return getClass().getName();
	}
	
}
