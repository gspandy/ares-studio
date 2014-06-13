/**
 * Դ�������ƣ�EMFEditingSupport.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.editingsupport;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ColumnViewer;

import com.hundsun.ares.studio.ui.editor.blocks.DisplayItem;
import com.hundsun.ares.studio.ui.editor.viewers.IEStructuralFeatureProvider;

/**
 * ����EMFģ�͵ı༭֧�֣���������EditDomain�µı༭����ʱ���������ķ�ʽִ��
 * @author gongyf
 */
public abstract class EMFEditingSupport extends BaseEditingSupport {

	protected IEStructuralFeatureProvider featureProvider = null;

	public EMFEditingSupport(ColumnViewer viewer, EStructuralFeature feature) {
		this(viewer, new IEStructuralFeatureProvider.Impl(feature));
	}

	public EMFEditingSupport(ColumnViewer viewer, IEStructuralFeatureProvider featureProvider) {
		super(viewer);
		this.featureProvider = featureProvider;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
	 */
	@Override
	protected Object getValue(Object element) {
		EObject owner = getOwner(element);
		if (owner == null) {
			return null;
		}
		return owner.eGet(featureProvider.getFeature(element));
	}
	
	@Override
	protected boolean doCanEdit(Object element) {
		
		if (element instanceof DisplayItem)
			return false;
		
		EObject owner = getOwner(element);
		if (owner == null) {
			return false;
		}
		return super.doCanEdit(element) && owner.eClass().getEAllStructuralFeatures().contains(featureProvider.getFeature(element));
	}
	
	/**
	 * ��ȡ��Ҫ������EObject
	 * @param element
	 * @return
	 */
	protected EObject getOwner(Object element) {
		return (EObject) element;
	}
	
	protected EStructuralFeature getFeature(Object element) {
		return featureProvider.getFeature(element);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		
		// ���û���޸ĵı�Ҫ��ֱ�ӷ���
		Object oldValue = getValue(element);
		if (ObjectUtils.equals(oldValue, value) || value == null) {
			return;
		}
		EObject owner = getOwner(element);
		
		if (EcoreUtil.getRootContainer(owner.eContainer()) instanceof ChangeDescription) {
			// FIXME ���ڼ���celleditor��ʱ����г������������ܵ�����������Ϊ�仯��һ���֣������������Ȼ�޷��õ��༭�򣬵���Ҳ��Ӧ�ý��б༭
			return;
		}
		
		EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(owner);
		
		if (editingDomain != null) {
			Command command = SetCommand.create(editingDomain, owner, featureProvider.getFeature(element), value);
			editingDomain.getCommandStack().execute(command);
		} else {
			owner.eSet(featureProvider.getFeature(element), value);
		}
		
		// ���ʹ��RefreshViewerJob���ӳ���ʾ��ȷ����
		getViewer().update(element, null);
	}


}
