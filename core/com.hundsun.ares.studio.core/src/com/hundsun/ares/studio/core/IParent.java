/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

/**
 * ��������Ԫ�ص�Ԫ��
 * @author sundl
 */
public interface IParent {

	IARESElement[] getChildren() throws ARESModelException;
	
	boolean hasChildren() throws ARESModelException;
}
