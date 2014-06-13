/**
 * Դ�������ƣ�PasteAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.basicdata.ui.editor.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.ui.action.CommandActionHandler;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

import com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataViewerUtil;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.UncategorizedItemsCategoryImpl;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.ui.editor.actions.IActionIDConstant;
import com.hundsun.ares.studio.ui.editor.actions.IUpdateAction;

public class BasicDataPasteAction extends CommandActionHandler implements
		IUpdateAction {
	ColumnViewer viewer;
	EStructuralFeature feature;
	EClass eclass;
	
	//�ų�MetadataItem����������
	private final static EList<EStructuralFeature> exclude = MetadataPackage.Literals.METADATA_ITEM.getEAllStructuralFeatures();
	
	public BasicDataPasteAction(ColumnViewer viewer,EditingDomain domain,EStructuralFeature feature,EClass eclass) {
		super(domain, "ճ��");
		setId(IActionIDConstant.CV_PASTE);
		this.viewer = viewer;
		this.feature = feature;
		this.eclass = eclass;
	}

	@Override
	public void run() {
		IStructuredSelection selection = (IStructuredSelection)this.viewer.getSelection();
		updateSelection(selection);
		super.run();
	}
	
	@Override
	public Command createCommand(Collection<?> selection) {
		EStructuralFeature ef = feature;
		EObject owner = (EObject) viewer.getInput();
		if (domain.getClipboard() == null) {
			return UnexecutableCommand.INSTANCE;
		}
		Collection<Object> objs = getPasteObjs(EcoreUtil.copyAll(domain.getClipboard()),eclass);
		if(objs.size()>0){
			if (!MetadataViewerUtil.isShowCategory(viewer) ) {
				return AddCommand.create(getEditingDomain(), owner,
						ef, objs);
			}
			MetadataCategory cate = MetadataViewerUtil.getSelectedCategory(viewer, false);
			if (cate instanceof UncategorizedItemsCategoryImpl){
				return AddCommand.create(getEditingDomain(), owner, ef, objs);
			}else if (cate instanceof MetadataCategory) {
				CompoundCommand command = new CompoundCommand();
				command.append(AddCommand.create(getEditingDomain(), cate,
						MetadataPackage.Literals.METADATA_CATEGORY__ITEMS, objs));
				
				command.append(AddCommand.create(getEditingDomain(), owner,
						ef, objs));
				// ����ӵ�����
				
				return command;
			}
		}
		return UnexecutableCommand.INSTANCE;
	}

	/**
	 * @param copyObjs
	 * @return
	 * ת��Ϊճ������
	 */
	private Collection<Object> getPasteObjs(Collection<Object> copyObjs,EClass eclass) {
		List<Object> objs = new ArrayList<Object>();
		Map<EStructuralFeature, EStructuralFeature> map = new HashMap<EStructuralFeature, EStructuralFeature>();
		//�ɵ�һ�����ƶ����ҳ����ƶ����ճ������ƥ�������
		Iterator<Object> it = copyObjs.iterator();
		if(it.hasNext()){
			Object obj = it.next();
			if(obj instanceof EObject){
				EObject copyObj = (EObject)obj;
				//�����ͬһ���༭���ڽ���ճ����ֱ�ӷ��ظ��ƶ��󼯺�
				if(copyObj.eClass().equals(eclass)){
					return copyObjs;
				}
				//nameһ�������ʾ����ƥ��
				for(EStructuralFeature copyFeature : copyObj.eClass().getEAllStructuralFeatures()){
					if(!exclude.contains(copyFeature)){
						for(EStructuralFeature pasteFeature : eclass.getEAllStructuralFeatures()){
							if(StringUtils.equals(copyFeature.getName(), pasteFeature.getName())){
								map.put(copyFeature, pasteFeature);
								break;
							}
						}
					}
				}
			}
		}
		//���û��ƥ������ԣ��򷵻ؿռ���
		if(map.isEmpty()){
			return objs;
		}
		for(Object obj : copyObjs)
		{
			EObject copyObj = (EObject)obj;
			EObject pasteObj = eclass.getEPackage().getEFactoryInstance().create(eclass);
			for(Entry<EStructuralFeature, EStructuralFeature> entry : map.entrySet()){
				EStructuralFeature copyFeature = entry.getKey();
				EStructuralFeature pasteFeature = entry.getValue();
				if(copyFeature instanceof EAttribute){
					//����ƥ������ֵ
					pasteObj.eSet(pasteFeature, copyObj.eGet(copyFeature));
				}else if(copyFeature instanceof EReference){
					//����ƥ���Ӽ�
					Collection<Object> tmp = (Collection<Object>)copyObj.eGet(copyFeature);
					if(tmp != null){
						pasteObj.eSet(pasteFeature, getPasteObjs(EcoreUtil.copyAll(tmp) , ((EReference)pasteFeature).getEReferenceType()));
					}
				}
			}
			objs.add(pasteObj);
		}
		return objs;
	}

	/**
	 * @deprecated As of EMF 2.1.0, replaced by {@link #setActiveWorkbenchPart}.
	 */
	@Deprecated
	public void setActiveEditor(IEditorPart editorPart) {
		setActiveWorkbenchPart(editorPart);
	}

	/**
	 * @since 2.1.0
	 */
	public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart) {
		if (workbenchPart instanceof IEditingDomainProvider) {
			domain = ((IEditingDomainProvider) workbenchPart)
					.getEditingDomain();
		}
	}

	@Override
	public void update() {
		IStructuredSelection selection = (IStructuredSelection)this.viewer.getSelection();
		command = createCommand(selection.toList());
		if(null != command && command.canExecute()){
			setEnabled(true);
		}else{
			setEnabled(false);
		}
	}
}