/**
 * Դ�������ƣ�SelectionEditor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�����
 */
package com.hundsun.ares.studio.jres.database.oracle.ui.editors;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * @author wangbin
 *
 */
public abstract class OracleUserPrivilegeSelectionCellEditor extends DialogCellEditor {

	/**
	 * ��ѡ�б����.
	 */
	private String fromText = "";
	
	/**
	 * ��ѡ�б����.
	 */
	private String toText = "";

	/**
	 * @return the fromText
	 */
	public String getFromText() {
		return fromText;
	}

	/**
	 * @param fromText the fromText to set
	 */
	public void setFromText(String fromText) {
		this.fromText = fromText;
	}

	/**
	 * @return the toText
	 */
	public String getToText() {
		return toText;
	}

	/**
	 * @param toText the toText to set
	 */
	public void setToText(String toText) {
		this.toText = toText;
	}

	public OracleUserPrivilegeSelectionCellEditor(Composite parent,String fromText, String toText) {
		super(parent);
		setFromText(fromText);
		setToText(toText);
	}

}
