/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.macro;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;


/**
 * @author zhuyf
 *
 */
public interface IProcedureMacroTokenService {
	
	//�ж��Ƿ��Ǵ洢����
	public boolean isProcedure(String procedureName);
	
	public IMacroTokenHandler getHandler(String procedureName);
	
	public IARESResource getProcedure(String procedureName);
	
	

}
