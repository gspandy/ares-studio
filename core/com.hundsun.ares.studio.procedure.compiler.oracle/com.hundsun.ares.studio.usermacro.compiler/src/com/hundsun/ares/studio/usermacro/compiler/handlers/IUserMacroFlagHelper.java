/**
 * 
 */
package com.hundsun.ares.studio.usermacro.compiler.handlers;

import java.util.Map;

/**
 * @author yanwj06282
 *
 */
public interface IUserMacroFlagHelper {

	public static final String CUR_REFIX = "cursor";
	
	/**
	 * �û��Զ���꣬������
	 * 
	 * @param content
	 * @param inputParams
	 * @param vars
	 * @return
	 */
	public String genFlag(UserMacroToken token ,Map<Object, Object> context ,String content);
	
}
