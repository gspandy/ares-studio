/**
 * Դ�������ƣ�BizPkgPropertyDescriptor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.metadata.ui;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.ui.editor.extend.AbstractMapEMPropertyDescriptor;

/**
 * �û�������չ�е� ��ҵ��������͵���չ���Ե���������
 * @author sundl
 *
 */
public class BizPkgPropertyDescriptor extends AbstractMapEMPropertyDescriptor{

	/**
	 * @param structuralFeature
	 * @param key
	 * @param extendModelKey
	 */
	public BizPkgPropertyDescriptor(EStructuralFeature structuralFeature, Object key, String extendModelKey) {
		super(structuralFeature, key, extendModelKey);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelPropertyDescriptor#getLabelProvider()
	 */
	@Override
	public ILabelProvider getLabelProvider() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelPropertyDescriptor#createPropertyEditor(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
