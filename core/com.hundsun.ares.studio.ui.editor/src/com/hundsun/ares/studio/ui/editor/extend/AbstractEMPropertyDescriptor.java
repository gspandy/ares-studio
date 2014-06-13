/**
 * Դ�������ƣ�AbstractEMPropertyDescriptor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.extend;

import org.eclipse.emf.ecore.EStructuralFeature;


/**
 * @author gongyf
 *
 */
public abstract class AbstractEMPropertyDescriptor implements
		IExtensibleModelPropertyDescriptor {

	private String category;
	private String displayName;
	private String description;
	private EStructuralFeature structuralFeature;
	
	
	/**
	 * @param structuralFeature
	 */
	public AbstractEMPropertyDescriptor(EStructuralFeature structuralFeature) {
		super();
		this.structuralFeature = structuralFeature;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @param structuralFeature the structuralFeature to set
	 */
	public void setStructuralFeature(EStructuralFeature structuralFeature) {
		this.structuralFeature = structuralFeature;
	}
	
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.extend.IExtensibleModelPropertyDescriptor#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return displayName;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.extend.IExtensibleModelPropertyDescriptor#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.extend.IExtensibleModelPropertyDescriptor#getStructuralFeature()
	 */
	@Override
	public EStructuralFeature getStructuralFeature() {
		return structuralFeature;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.extend.IExtensibleModelPropertyDescriptor#isDerived()
	 */
	@Override
	public boolean isDerived() {
		return false;
	}
}
