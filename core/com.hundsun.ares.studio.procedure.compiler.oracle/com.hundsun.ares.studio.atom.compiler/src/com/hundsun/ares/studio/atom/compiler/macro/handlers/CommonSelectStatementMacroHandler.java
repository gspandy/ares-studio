/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.macro.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.AtomService;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.token.CommonSelectStatementToken;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.parser.PseudoCodeParser;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;

/**
 * @author zhuyf
 *
 */
public class CommonSelectStatementMacroHandler implements IMacroTokenHandler {
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.COMMON_SELECT_STATEMENT_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*
		 * ����������
		[ͨ��SELECT][select ���]
		�������̣�
		��α����༭��������[ͨ��SELECT][select ���]���������Ԥ��tabҳ���鿴��Ӧ����ʵ���롣
		����������м���һ���µĽ��������������Ϊ����ż��Ͻ�������еĳ��ȡ�
		��������sQueryText������Ϊchar sQueryText[4096]��Ĭ��ֵΪ{0}��
		���[select ���]��ȡ�õ�ֵΪ@���ſ�ͷ�ģ���˵������һ��������ֱ��д��lpConn->executeQuery()�У�������ǣ���˵�����ǹ̶����ַ�����������ַ���û�м����ţ��Զ�Ϊ����ϣ��������ַ�����ֵ������sQueryText��Ȼ���������lpConn->executeQuery(sQueryText)��

		 */
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		Set<String> rsList = helper.getAttribute(IAtomEngineContextConstant.ATTR_RESULTSET_LIST);
		AtomFunction func = (AtomFunction)context.get(IAtomEngineContextConstant.ResourceModel);
		int rsTotalSizeAdd1 = rsList.size() + 1;
		
		
		List<ICodeToken> codeTokens= new ArrayList<ICodeToken>();
		String objectId = func.getObjectId();
		if(StringUtils.isBlank(objectId) && (func instanceof AtomService)){
			fireEventLessFunctionId(context);
		}else if(StringUtils.isBlank(objectId) && !(func instanceof AtomService)){
			objectId = func.getName();
		}
		String rsID = objectId + rsTotalSizeAdd1;
		//����������ؽ���������÷��ؽ������AF�Լ�[ͨ��SELECT]������ֱ�Ӵ�����
		helper.addAttribute(IAtomEngineContextConstant.ATTR_FUNC_RESULTSET,rsID);
		helper.addAttribute(IAtomEngineContextConstant.ATTR_RESULTSET_LIST,rsID);//�����ͷŵĽ�����б�
		helper.addAttribute(IAtomEngineContextConstant.ATTR_GETLAST_RESULTSET,rsID);//ȡ�þͽ��Ľ����Id�����ǿ�����lpResultSet->ȡ�ͽ�������ģ�����Ҫ�ӵ�����б���
		//[ͨ��SELECT]Ϊ���ݿ��
		helper.addAttribute(IAtomEngineContextConstant.ATTR_DATABASE_MACRO,getKey());
		String resultSetVarName = "lpResultSet"+objectId + rsTotalSizeAdd1;
		String sql = PseudoCodeParser.insertCommonForSql(token.getParameters()[0], func.getObjectId());
		codeTokens.add(new CommonSelectStatementToken(resultSetVarName,sql));//���ͨ��select����Token
		return codeTokens.iterator();
	}
	
	/**
	 * ȱ�ٹ��ܺ�
	 * @param context
	 */
	private void fireEventLessFunctionId(Map<Object, Object> context){

		ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
		String message = "��ȡ���ܺ�ʧ��,�޷���������lpResultSet";
		manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
	
	}
	
	

}
