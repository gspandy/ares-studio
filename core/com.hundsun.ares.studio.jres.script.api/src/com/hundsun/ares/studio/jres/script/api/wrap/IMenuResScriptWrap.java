/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.metadata.IFunctionProxyScriptWrap;
import com.hundsun.ares.studio.jres.script.api.metadata.IMenuFunctionScriptWrap;

/**
 * Ŀ¼�Ͳ˵���Դ����
 * 
 * @author yanwj06282
 *
 */
public interface IMenuResScriptWrap extends IMetadataResScriptWrap {

	public IMenuFunctionScriptWrap[] getFunctions();
	
	public IFunctionProxyScriptWrap[] getFunctionProxys();
	
	public IMenuFunctionScriptWrap addFunction();
	
}
