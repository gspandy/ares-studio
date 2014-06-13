/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.usermacro.compiler.handlers;

import java.util.Map;
import java.util.Set;

import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.usermacro.UserMacroItem;


/**
 * @author zhuyf
 *
 */
public interface IUserMacroTokenService {
	
	public boolean isUserMacro(String macroName);
	
	/**
	 * ���ݺ�����ȡ�û�����Ŀ
	 * 
	 * @param macroName
	 * @return
	 */
	public UserMacroItem getUserMacro(String macroName);
	
   /**
    * ����û���
    * @param macroName
    * @param context
    * @param paramsMap
    * @return
    */
	public IMacroTokenHandler getUserMacroHandler(String macroName,Map<Object, Object> context,Map<String, Object> paramsMap);
	
	
	/**
	 * ��ȡ�����û���
	 * @return
	 */
	public Set<IMacroTokenHandler> getUserMacros();

}
