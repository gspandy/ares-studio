/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.validate;

import java.util.Collection;
import java.util.Map;

import com.hundsun.ares.studio.core.IARESProblem;
import com.hundsun.ares.studio.core.IARESResource;

/**
 * 
 * @author sundl
 */
public interface IResValidator {

	/**
	 * ���ָ������Դ�������ض�Ӧ�Ĵ�����Ϣ��
	 * @param resource ��Դ 
	 * @return ���ܷ���<code>null</code>
	 */
	public Collection<IARESProblem> validate(IARESResource resource, Map<String, IAresContext> contexts);
	
}
