/**
 * Դ�������ƣ�IRefResourceForBuilderProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�liaogc
 */
package com.hundsun.ares.studio.core.validate;

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IResourceDelta;

import com.hundsun.ares.studio.core.IARESResource;

/**
 * 
 * @author liaogc
 */
public interface IRefResourceForBuilderProvider {


	/**
	 * ���ݸ�����delta���󣬼��ָ����Դ�Ĺ�����Դ��
	 * @param res ��Դ
	 * @param delta ��Դ����
	 * @param contexts ������
	 * @return ��������Դ�Ĺ�����Դ��������Դ������Ҫ������Դ.
	 */
	public Collection<IARESResource> getRefForBuildResources(IARESResource res, IResourceDelta delta, Map<String, IAresContext> contexts);
	

}
