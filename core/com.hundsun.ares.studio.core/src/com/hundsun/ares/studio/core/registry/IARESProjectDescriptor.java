/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.registry;

import org.eclipse.core.resources.IProjectNature;

/**
 * ARES��Ŀ����ע����Ϣ����
 * @author sundl
 */
public interface IARESProjectDescriptor extends ICommonDescriptor {
	
	/**
	 * ��ȡ������nature id�������ж��
	 * @see IProjectNature
	 * @return ������Nature id
	 */
	String[] getNatures();
}
