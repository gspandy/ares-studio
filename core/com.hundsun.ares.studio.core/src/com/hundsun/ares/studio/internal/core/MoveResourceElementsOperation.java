/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.internal.core;

import com.hundsun.ares.studio.core.IARESElement;

/**
 * �ƶ�Ԫ�صĲ�����
 * @author sundl
 */
public class MoveResourceElementsOperation extends CopyResourceElementsOperation {

	public MoveResourceElementsOperation(IARESElement[] elementsToProcess, IARESElement[] containers, boolean force) {
		super(elementsToProcess, containers, force);
	}
	
	protected boolean isMove() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.internal.core.MultiOperation#getMainTaskName()
	 */
	@Override
	protected String getMainTaskName() {
		return "Move";
	}
	
}
