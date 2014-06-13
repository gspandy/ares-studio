/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.validate;

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IResourceDelta;

import com.hundsun.ares.studio.core.IARESResource;

/**
 * ������Դ�ṩ��
 * @author sundl
 */
public interface IRefResourceProvider {

	/**
	 * ���ݸ�����delta���󣬼��ָ����Դ�Ĺ�����Դ��
	 * @param res ��Դ
	 * @param delta ��Դ����
	 * @param contexts ������
	 * @return ��������Դ�Ĺ�����Դ��������Դ������Ҫ������Դ.
	 */
	public Collection<IARESResource> getRefResources(IARESResource res, IResourceDelta delta, Map<String, IAresContext> contexts);
	
}
