/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.token.service;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomService;
import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;

/**
 * @author zhuyf
 *
 */
public class ServiceBeginToken implements ICodeToken {

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
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#genCode(java.util.Map)
	 */
	@Override
	public String genCode(Map<Object, Object> context) throws Exception {
		// ��token������¹�����
		// 1.����ԭ�ӷ���ԭ�Ӻ����ķ���ͷ���������£�
		/*[������]
	     int PLATFORM_EXPORT F[�����](IASContext * pContext,IUnPacker * lpInUnPacker,IPacker * lpOutPacker)
	    '{'
           int iRetCode = 0;
		 */
		
		AtomService as = (AtomService)context.get(IAtomEngineContextConstantMySQL.ResourceModel);
		StringBuffer codeBuffer = new StringBuffer();
		if(StringUtils.isBlank(as.getObjectId())){
			ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = "��Դ:"+as.getName()+"�����ù��ܺ�";
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
		}
		codeBuffer.append(String.format(ATOM_SERV_HAND_MSG, as.getChineseName(),as.getObjectId()));
		return codeBuffer.toString();
	}

	private final static String ATOM_SERV_HAND_MSG = "//%1$s\r\n" +
					"int FUNCTION_CALL_MODE F%2$s(IAS2Context* lpContext,IF2UnPacker * lpInUnPacker,IF2Packer * lpOutPacker)\r\n" +
					"{\r\n" +
					"int iReturnCode = 0;\r\n";
}
