/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.logic.compiler.tokens;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.logic.compiler.constant.ILogicEngineContextConstant;

/**
 * �߼����񴴽�
 * @author qinyuan
 *
 */
public class LogicServiceBeginToken implements ICodeToken {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#getContent()
	 */
	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#getType()
	 */
	@Override
	public int getType() {
		return CODE_TEXT;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#genCode(java.util.Map)
	 */
	@Override
	public String genCode(Map<Object, Object> context) throws Exception {
		
		AtomFunction ls = (AtomFunction)context.get(ILogicEngineContextConstant.ResourceModel);
		if(StringUtils.isBlank(ls.getObjectId())){
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = "��Դ:"+ls.getName()+"�����ù��ܺ�";
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
		}
		//����������ʹ�ö���ţ�����Ų����ڣ�ʹ����ԴӢ����
		String funcName = "";
		if(StringUtils.isBlank(ls.getObjectId())) {
			funcName = ls.getName();
		}else {
			funcName = "F" + ls.getObjectId();
		}
		
		return String.format(LOGIC_SERVICE_CREATE_METHOD_INFO, ls.getChineseName(),funcName);
	}

	private final static String LOGIC_SERVICE_CREATE_METHOD_INFO = "//%s" + NEWLINE +
			"int FUNCTION_CALL_MODE %s(IAS2Context * lpContext,IF2UnPacker * lpInUnPacker,IF2Packer * lpOutPacker)" + NEWLINE +
			"{" + NEWLINE;
}
