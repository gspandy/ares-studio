/**
 * Դ�������ƣ�ExtensibleModelEditingEntry.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.extend;


/**
 * @author gongyf
 *
 */
public class ExtensibleModelEditingEntry {

	private ExtensibleModelEditingGroup group;
	private ExtensibleModelEditingCategory category;
	private IExtensibleModelPropertyDescriptor descriptor;
	
	/**
	 * @param group
	 * @param descriptor
	 */
	public ExtensibleModelEditingEntry(ExtensibleModelEditingGroup group,
			IExtensibleModelPropertyDescriptor descriptor) {
		super();
		this.group = group;
		this.descriptor = descriptor;
	}
	
	public ExtensibleModelEditingCategory getCategory() {
		return category;
	}

	public void setCategory(ExtensibleModelEditingCategory category) {
		this.category = category;
	}

	public ExtensibleModelEditingGroup getGroup() {
		return group;
	}
	/**
	 * @return the descriptor
	 */
	public IExtensibleModelPropertyDescriptor getDescriptor() {
		return descriptor;
	}


}
