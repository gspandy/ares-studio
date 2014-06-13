/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.logic.compiler.macro.service;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;

/**
 * @author qinyuan
 *
 */
public interface IAtomServiceMacroTonkenService {

	/**
	 * �Ƿ�Ϊԭ�ӷ���
	 * @param serviceName
	 * @return
	 */
	boolean isAtomService(String serviceName);
	
	/**
	 * @param serviceName
	 * @return
	 */
	IMacroTokenHandler getHandler(String serviceName);
	
	/**
	 * ��ȡ��Դ
	 * @param serviceName
	 * @return
	 */
	IARESResource getAresResource(String serviceName);
}
