/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util;

import java.util.Collection;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;

/**
 * @author liaogc
 *
 */
public interface IScriptReferenceUtil extends IJRESScript{
	/**
	 * ������������,���������Լ���Ŀ���ƻ�����ô����õ���Դ�б�
	 * @param refType
	 * @param refName
	 * @param project
	 * @return
	 */
	public Collection<IARESResource> getRefResources(String refType,String refName,IARESProject project) ;
}
