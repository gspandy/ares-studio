/**
 * Դ�������ƣ�PropertyValueColumnLabelProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.extend;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hundsun.ares.studio.ui.editor.extend.user.IUserExtendedPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.IEStructuralFeatureProvider;

/**
 * 
 * �ṩ�˶� {@link IEMLabelProviderExtension}��֧��
 * @author gongyf
 *
 */
public class PropertyValueColumnLabelProvider extends EObjectColumnLabelProvider {
	
	/**
	 * @param attribute
	 */
	public PropertyValueColumnLabelProvider() {
		super(new IEStructuralFeatureProvider(){

			@Override
			public EStructuralFeature getFeature(Object element) {
				if (element instanceof ExtensibleModelEditingEntry) {
					ExtensibleModelEditingEntry entry = (ExtensibleModelEditingEntry) element;
					return entry.getDescriptor().getStructuralFeature();
				}
				return null;
			}});
	}

	@Override
	protected Diagnostic getDiagnostic(Object element) {
		if (getDiagnosticProvider() != null) {
			if (element instanceof ExtensibleModelEditingEntry) {
				ExtensibleModelEditingEntry entry = (ExtensibleModelEditingEntry) element;
				EObject owner = getOwner(element);
				if (owner != null) {
					// ����map���͵����⴦��һ�������û��Զ�����չ
					IExtensibleModelPropertyDescriptor descriptor = entry.getDescriptor();
					if (descriptor instanceof IMapExtensibleModelPropertyDescriptor) {
						return getDiagnosticProvider().getDiagnostic(owner, getEStructuralFeature(element), ((IMapExtensibleModelPropertyDescriptor) descriptor).getKey());
					} else {
						return getDiagnosticProvider().getDiagnostic(owner, getEStructuralFeature(element));
					}
					
				}
			}
			
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.BaseEObjectColumnLabelProvider#getOwner(java.lang.Object)
	 */
	@Override
	protected EObject getOwner(Object element) {
		if (element instanceof ExtensibleModelEditingEntry) {
			ExtensibleModelEditingEntry entry = (ExtensibleModelEditingEntry) element;
			String key = entry.getGroup().getEditingSupport().getKey();
			return entry.getGroup().getRoot().getModel().getData2().get(key);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.BaseEObjectColumnLabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof ExtensibleModelEditingEntry) {
			ExtensibleModelEditingEntry entry = (ExtensibleModelEditingEntry) element;
			EObject owner = getOwner(element);
			if (owner != null) {
				IExtensibleModelPropertyDescriptor descriptor = entry.getDescriptor();
				if (descriptor instanceof IUserExtendedPropertyDescriptor) {
					// �µĴ���ʽ����ȫ��element����descriptor����
					ILabelProvider labelProvider = descriptor.getLabelProvider();
					if (labelProvider != null) {
						return labelProvider.getText(entry.getGroup().getRoot().getModel());
					}
				} else {
					EStructuralFeature feature = descriptor.getStructuralFeature();
					ILabelProvider labelProvider = descriptor.getLabelProvider();
					if (labelProvider instanceof IEMLabelProviderExtension) {
						((IEMLabelProviderExtension) labelProvider).setExtensibleModel(entry.getGroup().getRoot().getModel());
					}
					
					Object value = owner.eGet(feature);
					if (descriptor instanceof IMapExtensibleModelPropertyDescriptor) {
						Assert.isTrue(value instanceof EMap<?, ?>);
						value = ((EMap<?, ?>) value).get(((IMapExtensibleModelPropertyDescriptor) descriptor).getKey());
					}
					
					return labelProvider.getText(value);
				}
			}
			
		}
		return StringUtils.EMPTY;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.EObjectColumnLabelProvider#doGetImage(java.lang.Object)
	 */
	@Override
	protected Image doGetImage(Object element) {
		if (element instanceof ExtensibleModelEditingEntry) {
			ExtensibleModelEditingEntry entry = (ExtensibleModelEditingEntry) element;
			EObject owner = getOwner(element);
			if (owner != null) {
				IExtensibleModelPropertyDescriptor descriptor = entry.getDescriptor();
				if (descriptor instanceof IUserExtendedPropertyDescriptor) {
					// �µĴ���ʽ����ȫ��element����descriptor����
					ILabelProvider labelProvider = descriptor.getLabelProvider();
					if (labelProvider != null) {
						return labelProvider.getImage(entry.getGroup().getRoot().getModel());
					}
				} else {
					EStructuralFeature feature = descriptor.getStructuralFeature();
					ILabelProvider labelProvider = descriptor.getLabelProvider();
					if (labelProvider instanceof IEMLabelProviderExtension) {
						((IEMLabelProviderExtension) labelProvider).setExtensibleModel(entry.getGroup().getRoot().getModel());
					}
					
					Object value = owner.eGet(feature);
					if (descriptor instanceof IMapExtensibleModelPropertyDescriptor) {
						Assert.isTrue(value instanceof EMap<?, ?>);
						value = ((EMap<?, ?>) value).get(((IMapExtensibleModelPropertyDescriptor) descriptor).getKey());
					}
					
					return labelProvider.getImage(value);
				}
			}
		}
		return null;
	}
	
}
