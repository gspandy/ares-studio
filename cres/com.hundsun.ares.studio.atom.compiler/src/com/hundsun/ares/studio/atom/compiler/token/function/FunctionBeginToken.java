/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.token.function;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.macro.MacroConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.ICodeToken;

/**
 * @author zhuyf
 *
 */
public class FunctionBeginToken implements ICodeToken {

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
		return CODE_TEXT;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#genCode(java.util.Map)
	 */
	@Override
	public String genCode(Map<Object, Object> context) throws Exception {
		// ��token������¹�����
		// 1.����ԭ�ӷ���ԭ�Ӻ����ķ���ͷ���������£�
		/*[������]
	     int PLATFORM_EXPORT F[�����](IASContext * pContext,IUnPacker * lpInUnPacker,IPacker * lpOutPacker,IConnection * lpParentConn)
	    '{'
           int iRetCode = 0;
		 */
		
		AtomFunction af = (AtomFunction) context.get(IAtomEngineContextConstant.ResourceModel);
		StringBuffer codeBuffer = new StringBuffer();
		//����������ʹ�ö���ţ�����Ų����ڣ�ʹ����ԴӢ����
		String funcName = "";
		if(StringUtils.isBlank(af.getObjectId())) {
			funcName = af.getName();
		}else {
			funcName = "F" + af.getObjectId();
		}
		codeBuffer.append(String.format(ATOM_FUNC_HAND_MSG, af.getChineseName(),funcName));
		return codeBuffer.toString();
	}
	
	private final static String ATOM_FUNC_HAND_MSG = "//%1$s" + ITokenConstant.NL +
			"int FUNCTION_CALL_MODE %2$s(IAS2Context* lpContext,IF2UnPacker * lpInUnPacker,IF2Packer * lpOutPacker,IConnection * lpParentConn)" + ITokenConstant.NL +
			"{" + ITokenConstant.NL;
	

}
