/**
 * Դ�������ƣ�StringProposal.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.editors.dialog.inner;

import org.eclipse.jface.fieldassist.IContentProposal;

/**
 * @author yanwj06282
 *
 */
public class StringProposal implements IContentProposal{
	private Object content;

	public StringProposal(Object content) {
		this.content = content;
	}

	@Override
	public String getLabel() {
		return content.toString();
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public int getCursorPosition() {
		return content.toString().length();
	}

	@Override
	public String getContent() {
		return content.toString();
	}
}
