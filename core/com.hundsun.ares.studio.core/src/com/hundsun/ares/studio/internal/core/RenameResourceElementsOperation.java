/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.internal.core;

import com.hundsun.ares.studio.core.IARESElement;

/**
 * ������Ԫ�صĻ���������
 * @author sundl
 */
public class RenameResourceElementsOperation extends MoveResourceElementsOperation {

	public RenameResourceElementsOperation(IARESElement[] elementsToProcess, IARESElement[] containers, String[] newNames, boolean force) {
		super(elementsToProcess, containers, force);
		setRenamingList(newNames);
	}

}
