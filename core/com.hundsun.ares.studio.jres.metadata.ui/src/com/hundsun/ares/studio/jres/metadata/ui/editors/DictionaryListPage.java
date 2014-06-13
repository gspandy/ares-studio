/**
 * Դ�������ƣ�DictionaryListPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.editors;

//import org.eclipse.core.commands.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.jres.metadata.ui.block.DictionaryDetailViewerBlock;
import com.hundsun.ares.studio.jres.metadata.ui.block.DictionaryListViewerBlock;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryItem;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.util.CircularReferenceException;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerBlock;
/**
 * �����ֵ�༭����ϸ����
 *
 */
public class DictionaryListPage extends AbstractMetadataFormPage {
		
	private DictionaryListViewerBlock dictionaryListViewerBlock;
	private DictionaryDetailViewerBlock dictionaryDetailViewerBlock;
	
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public DictionaryListPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	/**
	 * @return the dictionaryListViewerBlock
	 */
	public DictionaryListViewerBlock getDictionaryListViewerBlock() {
		return dictionaryListViewerBlock;
	}
	
	/**
	 * @return the dictionaryDetailViewerBlock
	 */
	public DictionaryDetailViewerBlock getDictionaryDetailViewerBlock() {
		return dictionaryDetailViewerBlock;
	}

	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.MetadataListPage#isNeedValidate(org.eclipse.emf.transaction.ResourceSetChangeEvent)
	 */
	@Override
	protected boolean isNeedValidate(ResourceSetChangeEvent event) {
		for (Notification notification : event.getNotifications()) {
			if (notification.getNotifier() instanceof DictionaryItem) {
				return true;
			}

			if (notification.getFeature() == MetadataPackage.Literals.METADATA_CATEGORY__CHILDREN
					|| notification.getFeature() == MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS
					|| notification.getNotifier() instanceof MetadataItem
					|| notification.getNotifier() instanceof MetadataCategory) {
				return true;
			}
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataFormPage#createMetadataComposite(org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	protected void createMetadataComposite(Composite body, FormToolkit toolkit) {
		
		SashForm sashForm = new SashForm(body, SWT.VERTICAL);
		
		dictionaryListViewerBlock = new DictionaryListViewerBlock(this, getEditingDomain(), getSite(), getEditor().getARESResource(), getProblemPool());
		dictionaryListViewerBlock.setEditableControl(getEditableControl());
		dictionaryListViewerBlock.createControl(sashForm, toolkit);
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(dictionaryListViewerBlock);
		
		dictionaryDetailViewerBlock = new DictionaryDetailViewerBlock(getEditingDomain(), getEditor().getARESResource(), getProblemPool(),dictionaryListViewerBlock.getColumnViewer());
		dictionaryDetailViewerBlock.setEditableControl(getEditableControl());
		dictionaryDetailViewerBlock.createControl(sashForm, toolkit);
		getEditor().getActionBarContributor().addGlobalActionHandlerProvider(dictionaryDetailViewerBlock);

		sashForm.setWeights(new int[] { 6, 4 });
		
		dictionaryListViewerBlock.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection ) {
					if (!selection.isEmpty()) {
						Object element = ((IStructuredSelection)selection).getFirstElement();
						if (element instanceof DictionaryType) {
							if (MetadataUtil.isUseRefFeature(getEditor().getARESResource()) 
									&& MetadataUtil.isReferencingItem((DictionaryType)element)) {
								
								DictionaryType type = null;
								try {
									type = MetadataUtil.defaultResolve((DictionaryType)element, getEditor().getARESResource()).first;
								} catch (CircularReferenceException e) {
								}
								
								// ���õ��ֵ�����ֻ����
								dictionaryDetailViewerBlock.setDetailColumnIsRef(true);
								dictionaryDetailViewerBlock.setInput(type);
							} else {
								dictionaryDetailViewerBlock.setDetailColumnIsRef(false);
								dictionaryDetailViewerBlock.setInput(element);
							}
						}
					}else {
						dictionaryDetailViewerBlock.setInput(null);
					}
				}
			}
		});
		
		addPropertyListener(dictionaryListViewerBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(dictionaryListViewerBlock);
		
		addPropertyListener(dictionaryDetailViewerBlock);
		getEditingDomain().getCommandStack().addCommandStackListener(dictionaryDetailViewerBlock);
	}

	@Override
	public void infoChange() {
		dictionaryListViewerBlock.setInput(getInfo());
		super.infoChange();
	}
	
	@Override
	public void dispose() {
		removePropertyListener(dictionaryListViewerBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(dictionaryListViewerBlock);
		
		removePropertyListener(dictionaryDetailViewerBlock);
		getEditingDomain().getCommandStack().removeCommandStackListener(dictionaryDetailViewerBlock);
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.AbstractMetadataFormPage#getViewerBlock()
	 */
	@Override
	protected ColumnViewerBlock getViewerBlock() {
		return dictionaryListViewerBlock;
	}
	
}
