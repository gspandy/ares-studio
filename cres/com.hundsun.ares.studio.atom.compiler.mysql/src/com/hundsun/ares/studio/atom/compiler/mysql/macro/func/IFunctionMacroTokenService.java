/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.macro.func;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;


/**
 * @author zhuyf
 *
 */
public interface IFunctionMacroTokenService {
	
	//�ж��Ƿ���ԭ�Ӻ���
	public boolean isAtomFunction(String functionName);
	
	
	public IMacroTokenHandler getHandler(String functionName);
	
	public IARESResource getFunction(String functionName);
	
	

}
