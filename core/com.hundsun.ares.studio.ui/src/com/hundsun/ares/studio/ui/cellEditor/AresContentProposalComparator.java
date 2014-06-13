/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.cellEditor;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.IContentProposal;

/**
 * ��������IContentProposal.
 * @author sundl
 */
public class AresContentProposalComparator implements Comparator<IContentProposal> {

	private static final int TYPE_EQUALS = 100;
	private static final int TYPE_START = 200;
	private static final int TYPE_INCLUDE = 300;
	private static final int TYPE_END = 400;
	
	private static final int TYPE_DEFAULT = 1000;
	
	private String contents;
	
	/**
	 * @param contents ��ǰ���������
	 */
	public AresContentProposalComparator(String contents) {
		this.contents = contents;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(IContentProposal o1, IContentProposal o2) {
		int type1 = type(o1);
		int type2 = type(o2);
		if (type1 == type2) {
			return o1.getLabel().compareToIgnoreCase(o2.getContent());
		}
		return type1 - type2;
	}
	
	// ���࣬�������Ե�ǰ���ݿ�ͷ����β�����߰���
	private int type(IContentProposal cp) {
		String label = cp.getContent();
		if (StringUtils.equalsIgnoreCase(label, contents)) {
			return TYPE_EQUALS;
		} else if (StringUtils.startsWithIgnoreCase(label, contents)) {
			return TYPE_START;
		} else if (StringUtils.endsWithIgnoreCase(label, contents)) {
			return TYPE_END;
		} else if (StringUtils.containsIgnoreCase(label, contents)) {
			return TYPE_INCLUDE;
		}
		return TYPE_DEFAULT;
	}

}
