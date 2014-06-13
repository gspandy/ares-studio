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
public interface ILogicFunctionMacroTokenService {
	
	/**
	 * �Ƿ�Ϊ�߼�����
	 * @param functionName
	 * @return
	 */
	boolean isLogicFunction(String functionName);

	
	/**
	 * @param functionName
	 * @return
	 */
	IMacroTokenHandler getHandler(String functionName);
	
	/**
	 * ��ȡ��Դ
	 * @param functionName
	 * @return
	 */
	IARESResource getARESResource(String functionName);
}
