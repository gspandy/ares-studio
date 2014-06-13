/**
 * Դ�������ƣ�ExtensibleModelColumnLabelProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.basicdata.ui.extend;

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

import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.ui.editor.blocks.DisplayItem;
import com.hundsun.ares.studio.ui.editor.extend.IEMLabelProviderExtension;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelEditingSupport;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.extend.IMapExtensibleModelPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.extend.LongTextMapEMPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;

/**
 * ���ڴӱ�������Ϣ�����չ������
 * @author wangxh
 *
 */
public class RefExtensibleModelColumnLabelProvider extends
		EObjectColumnLabelProvider {

	IExtensibleModelEditingSupport editingSupport;
	IExtensibleModelPropertyDescriptor descriptor;
	EStructuralFeature infoReference;
	
	/**
	 * @param editingSupport
	 * @param descriptior
	 */
	public RefExtensibleModelColumnLabelProvider(
			IExtensibleModelEditingSupport editingSupport,
			IExtensibleModelPropertyDescriptor descriptor,
			EStructuralFeature infoReference) {
		super(descriptor.getStructuralFeature());
		this.editingSupport = editingSupport;
		this.descriptor = descriptor;
		this.infoReference = infoReference;
	}

	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.BaseEObjectColumnLabelProvider#getOwner(java.lang.Object)
	 */
	@Override
	protected EObject getOwner(Object element) {
		Object refer =  getRefObject(element);
		// �ӿڲ����б��У�����ڵ�չ������ӽڵ㣬���Ͳ���ExtensibleModel
		if (null != refer && refer instanceof ExtensibleModel) {
			ExtensibleModel model = (ExtensibleModel) refer;
			return model.getData2().get(editingSupport.getKey());
		} 
		return null;
	}
	
	private ExtensibleModel getRefObject(Object element){
		EObject eobj = (EObject) element;
		Object refer =  eobj.eGet(infoReference);
		//��ȡ���õ���Ϣ����Ŀ
		if(null != refer && refer instanceof ExtensibleModel){
			return (ExtensibleModel) refer;
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
				((IEMLabelProviderExtension) labelProvider).setExtensibleModel(getRefObject(element));
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
		return null;
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
		return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	}
}
