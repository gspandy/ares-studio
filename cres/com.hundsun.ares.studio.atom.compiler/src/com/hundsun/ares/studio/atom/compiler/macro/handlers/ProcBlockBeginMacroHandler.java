/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.macro.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.macro.TokenDomain;
import com.hundsun.ares.studio.atom.compiler.token.PROCBlockBeginToken;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.ITokenDomain;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;

/**
 * @author zhuyf
 *
 */
public class ProcBlockBeginMacroHandler implements IMacroTokenHandler {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.PROC_BLOCK_BEGIN_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*
		 * ����������
		[PRO*C���鿪ʼ]��[PRO*C�������]
		�������̣�
		��α����༭��������[PRO*C���鿪ʼ]��[PRO*C�������]���������Ԥ��tabҳ���鿴��Ӧ����ʵ���롣
		����������֮��Ĵ���Ҫ���������⴦��
		�ڸ�������֮��Ĵ������ʹ�ú�[������ʼ]��[���������]��[������¼]��[select������¼]�����������������Щ���˵����
		����������֮����ֵı���Ҫ������PROC�����У���������Ϊ������ֵı�����δ�ڱ������������г��֣���������PROC�������������С�
		��������֮����ֵı�����������@���Ž����滻��ʱ��Ҫͳһ���ϡ�������
		��������֮����ù���Ҫʹ��PROC���÷�ʽ��
		�����������������������ʽ��
		EXEC SQL EXECUTE //proc���鿪ʼ;
		BEGIN

		 */
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		helper.addAttribute(IAtomEngineContextConstant.ATTR_PROC_MACRO, this.getKey());
		helper.addAttribute(IAtomEngineContextConstant.ATTR_DATABASE_MACRO, this.getKey());
		List<ICodeToken> codeTokens = new ArrayList<ICodeToken>();
		if(!checkIsMacroMatch(context)){//���PRO*C���鿪ʼ]��ǰ���Ƿ���������PRO*C���鿪ʼ]��
			fireEvent(context);//���ͺ����ƥ���¼�
		}
		codeTokens.add(new PROCBlockBeginToken());//��Ӻ괦��codeToken
		addDomain(context);//�����
		
		return codeTokens.iterator();
	}
	
	/**
	 * �����PRO*C���鿪ʼ���Ƿ���������PRO*C���鿪ʼ��
	 * @return
	 */
	private boolean checkIsMacroMatch(Map<Object, Object> context){
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		ITokenDomain procBlockBeginDomain =handler.getDomain(MacroConstant.PROC_BLOCK_BEGIN_MACRONAME);
		//���ǰ����PRO*C���鿪ʼ��procBlockBeginDomain��Ϊnull
		return procBlockBeginDomain==null;
	}
	
	
	
	
	/**
	 * ���ͺ����ƥ���¼�
	 */
	private void fireEvent(Map<Object, Object> context){

		ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
		String message = String.format("��"+MacroConstant.PROC_BLOCK_BEGIN_MACRONAME+"ǰ�治����δ������[%s]��", MacroConstant.PROC_BLOCK_BEGIN_MACRONAME);
		//����PRO*C���鿪ʼ]��ǰ�治����������PRO*C���鿪ʼ]���¼�
		manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
	
		
	}
	
	/**
	 * �����
	 */
	private void addDomain(Map<Object, Object> context){
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		handler.addDomain(new TokenDomain(MacroConstant.PROC_BLOCK_BEGIN_MACRONAME, null));
		
	}
	

}
