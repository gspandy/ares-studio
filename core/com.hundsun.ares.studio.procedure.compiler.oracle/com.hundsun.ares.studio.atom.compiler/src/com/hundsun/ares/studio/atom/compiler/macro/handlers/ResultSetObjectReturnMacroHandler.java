/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.macro.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.compiler.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.token.ResultSetObjectReturnToken;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;

/**
 * @author liaogc
 * ��������󷵻�
 */
public class ResultSetObjectReturnMacroHandler implements IMacroTokenHandler{

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.RESULTSET_OBJECT_RETURN_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		List<ICodeToken> codeTokens= new ArrayList<ICodeToken>();
		if (token.getParameters().length !=1) {
			ITokenListenerManager  manager =(ITokenListenerManager) context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("��[%s]ȱ�ٲ�����", MacroConstant.RESULTSET_OBJECT_RETURN_MACRONAME);
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
		}else{
			String objectParam = StringUtils.trim(token.getParameters()[0]);
			if(!StringUtils.endsWith(objectParam,"ResultSet")){//����������������ResultSet��append the suffix ResultSet
				objectParam +="ResultSet";
			}
			if(StringUtils.indexOf(objectParam,"@")>-1){
				objectParam = StringUtils.substring(objectParam,StringUtils.indexOf(objectParam,"@")+1);
			}
			codeTokens.add(new ResultSetObjectReturnToken(objectParam));
		}
		return codeTokens.iterator();
	}

}
