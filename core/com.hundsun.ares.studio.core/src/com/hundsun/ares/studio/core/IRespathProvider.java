/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import java.util.List;

import org.eclipse.core.resources.IResourceDelta;


/**
 * ��չ����Res-path���߼�������ͨ�������չ��������res-path��
 * @author sundl
 */
public interface IRespathProvider {

	List<IExternalResPathEntry> getResPathEntries(IARESProject project);
	/**
	 * ��ȡRespath���ڶ�����������ָ���Ƿ���Ҫǿ��ˢ�¡�
	 * @param project
	 * @param refresh ���Ϊtrue˵����Ҫǿ��ˢ�¡�
	 * @return
	 */
	List<IExternalResPathEntry> getResPathEntries(IARESProject project, boolean refresh);
	boolean containsRespathChange(IResourceDelta delta);
	
}
