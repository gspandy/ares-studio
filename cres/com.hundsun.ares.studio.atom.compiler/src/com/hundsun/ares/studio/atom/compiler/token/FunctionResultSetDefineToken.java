/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.token;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.macro.func.IFunctionMacroTokenService;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.ICodeToken;

/**
 * @author zhuyf
 *
 */
public class FunctionResultSetDefineToken implements ICodeToken {

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
		// ÿһ���������ã�����һ��IF2Packer * lpOut[�����] = lpPackService->GetPacker(2);
		// ÿһ���������ã�����һ��IF2UnPacker * lpResultSet{0} = NULL;
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		Set<String> funcCallList = helper.getAttribute(IAtomEngineContextConstant.ATTR_FUNC_CALL);
		IFunctionMacroTokenService fmservice = (IFunctionMacroTokenService)context.get(IAtomEngineContextConstant.Function_Macro_Service);
		StringBuffer sb = new StringBuffer();
		if(funcCallList.size() > 0){
			sb.append("//IF2Packer��������ʼ��\r\n");
		}
		for(Iterator<String> ite = funcCallList.iterator();ite.hasNext();){
			String functionName = ite.next();
			AtomFunction func = fmservice.getFunction(functionName).getInfo(AtomFunction.class);
			sb.append("IF2Packer * lpOut"+(StringUtils.isBlank(func.getObjectId())?func.getName():func.getObjectId())+" = lpPackService->GetPacker(2);\r\n");
		}
		if(funcCallList.size() > 0){
			sb.append("//IF2UnPacker����\r\n");
		}
		for(Iterator<String> ite = funcCallList.iterator();ite.hasNext();){
			String functionName = ite.next();
			AtomFunction func = fmservice.getFunction(functionName).getInfo(AtomFunction.class);
			sb.append(String.format("IF2UnPacker * lpResultSet%1$s = NULL;\r\n",(StringUtils.isBlank(func.getObjectId())?func.getName():func.getObjectId())));
		}
		return sb.toString();
	}

}
