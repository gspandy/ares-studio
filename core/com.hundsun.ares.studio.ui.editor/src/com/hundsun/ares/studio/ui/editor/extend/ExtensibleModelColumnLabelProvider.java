/**
 * Դ�������ƣ�ExtensibleModelColumnLabelProvider.java
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.ui.editor.blocks.DisplayItem;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;

/**
 * 
 * �ṩ�˶� {@link IEMLabelProviderExtension}��֧��
 * @author gongyf
 *
 */
public class ExtensibleModelColumnLabelProvider extends
		EObjectColumnLabelProvider {

	IExtensibleModelEditingSupport editingSupport;
	IExtensibleModelPropertyDescriptor descriptor;
	private IARESResource resource;
	
	/**
	 * @param editingSupport
	 * @param descriptior
	 */
	public ExtensibleModelColumnLabelProvider(
			IExtensibleModelEditingSupport editingSupport,
			IExtensibleModelPropertyDescriptor descriptor , IARESResource resource) {
		super(descriptor.getStructuralFeature());
		this.editingSupport = editingSupport;
		this.descriptor = descriptor;
		this.resource = resource;
	}

	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.BaseEObjectColumnLabelProvider#getOwner(java.lang.Object)
	 */
	@Override
	protected EObject getOwner(Object element) {
		// �ӿڲ����б��У�����ڵ�չ������ӽڵ㣬���Ͳ���ExtensibleModel
		if (element instanceof ExtensibleModel) {
			ExtensibleModel model = (ExtensibleModel) element;
			return model.getData2().get(editingSupport.getKey());
		} 
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.BaseEObjectColumnLabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		EObject owner = getOwner(element);
		if (owner != null) {
			EStructuralFeature feature = descriptor.getStructuralFeature();
			ILabelProvider labelProvider = descriptor.getLabelProvider();
			if (labelProvider instanceof IEMLabelProviderExtension) {
				((IEMLabelProviderExtension) labelProvider).setExtensibleModel((ExtensibleModel) element);
			}
			if(feature!=null){
				Object value = owner.eGet(feature);
				if (descriptor instanceof IMapExtensibleModelPropertyDescriptor) {
					Assert.isTrue(value instanceof EMap<?, ?>);
					value = ((EMap<?, ?>) value).get(((IMapExtensibleModelPropertyDescriptor) descriptor).getKey());
				}	
				return labelProvider.getText(value);
			}
			return StringUtils.EMPTY;
			
			
		} else {
			ILabelProvider labelProvider = descriptor.getLabelProvider();
			// �������չ��(XML��ǩ֮�࣬�Ƕ�̬���㣬����ȥ��չmap��ȡֵ��)
			// ���⣬Ϊʲôֻ��ʵ��IEMLabelProviderExtension�ŷ���ֵ�� ��Ϊ��Ԫ���ݴ�������Ϣ������£��������
			// Ԫ��Ҳ������getOwnerΪ�ն�ִ�е������֧��������ʱ�����labelprovider��ʾ���֡�
			if (labelProvider instanceof IEMLabelProviderExtension
					&& (element instanceof ExtensibleModel)) {
				((IEMLabelProviderExtension) labelProvider).setExtensibleModel((ExtensibleModel) element);
				String text = labelProvider.getText(element);
				if (text != null)
					return text;
			}else if (labelProvider instanceof IEMLabelProviderExtension
					&& (element instanceof DisplayItem)) {
				if(((DisplayItem)element).eObject instanceof ExtensibleModel){
					((IEMLabelProviderExtension) labelProvider).setExtensibleModel((ExtensibleModel) ((DisplayItem)element).eObject);
					String text = labelProvider.getText(element);
					if (text != null)
						return text;
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
		EObject owner = getOwner(element);
		if (owner != null) {
			EStructuralFeature feature = descriptor.getStructuralFeature();
			if (feature == null)
				return null;
			
			ILabelProvider labelProvider = descriptor.getLabelProvider();
			if (labelProvider instanceof IEMLabelProviderExtension) {
				((IEMLabelProviderExtension) labelProvider).setExtensibleModel((ExtensibleModel) element);
			}
			
			Object value = owner.eGet(feature);
			if (descriptor instanceof IMapExtensibleModelPropertyDescriptor) {
				Assert.isTrue(value instanceof EMap<?, ?>);
				value = ((EMap<?, ?>) value).get(((IMapExtensibleModelPropertyDescriptor) descriptor).getKey());
			}
			
			return labelProvider.getImage(value);
			
		}
		return null;
	}
	
	@Override
	protected Diagnostic getDiagnostic(Object element) {
		if (getDiagnosticProvider() != null) {
			EObject owner = getOwner(element);
			EStructuralFeature feature = descriptor.getStructuralFeature();
			if (descriptor instanceof IMapExtensibleModelPropertyDescriptor) {
				return getDiagnosticProvider().getDiagnostic(owner, feature, ((IMapExtensibleModelPropertyDescriptor) descriptor).getKey());
			} else {
				return getDiagnosticProvider().getDiagnostic(owner, feature);
			}
		}
		return super.getDiagnostic(element);
	}
	
	@Override
	public String getToolTipText(Object element) {
		String toolTip = super.getToolTipText(element);
		if(StringUtils.isBlank(toolTip) && descriptor instanceof LongTextMapEMPropertyDescriptor){
			return getText(element);
		}
		return toolTip;
	}
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider#getBackground(java.lang.Object)
	 */
	@Override
	public Color getBackground(Object element) {
		ILabelProvider labelProvider = descriptor.getLabelProvider();
		if (labelProvider instanceof IEMLabelProviderExtension
				&& (element instanceof DisplayItem)) {
			if (((DisplayItem) element).eObject instanceof ExtensibleModel) {
				return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
			}
		}
		return super.getBackground(element);
	}
}
