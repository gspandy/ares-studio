/**
 * 
 */
package com.hundsun.ares.studio.ui.editor.extend;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * sundl: 2012-08-01�����Ժ��������൱����չע����Ϣ��һ�������ࣻ ���������ʾ������
 * {@link ExtensibleModelEditingCategory} �����
 * @author gongyf
 * @author sundl
 *
 */
public class ExtensibleModelEditingGroup {
	private ExtensibleModelEditingRoot root;
	private IExtensibleModelEditingSupport editingSupport;
	private List<ExtensibleModelEditingEntry> entries;
	
	/**
	 * @param root
	 * @param editingSupport
	 */
	public ExtensibleModelEditingGroup(ExtensibleModelEditingRoot root,
			IExtensibleModelEditingSupport editingSupport) {
		super();
		this.root = root;
		this.editingSupport = editingSupport;
	}

	public ExtensibleModelEditingRoot getRoot() {
		return root;
	}
	
	public IExtensibleModelEditingSupport getEditingSupport() {
		return editingSupport;
	}
	
	public List<ExtensibleModelEditingEntry> getEntries() {
		if (entries == null) {
			entries = new ArrayList<ExtensibleModelEditingEntry>();
			for (IExtensibleModelPropertyDescriptor desc : getEditingSupport().getPropertyDescriptors(getRoot().getARESElement(), getRoot().getEClass())) {
				entries.add(new ExtensibleModelEditingEntry(this, desc));
			}
		}
		return entries;
	}
	
}
