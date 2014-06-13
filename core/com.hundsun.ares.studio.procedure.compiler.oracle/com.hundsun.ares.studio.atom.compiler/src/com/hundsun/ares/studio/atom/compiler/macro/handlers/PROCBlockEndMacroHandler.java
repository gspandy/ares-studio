/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.macro.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.token.PROCBlockEndToken;
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
public class PROCBlockEndMacroHandler implements IMacroTokenHandler {


	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.PROC_BLOCK_END_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*
		 * ����Ƿ���[PRO*C���鿪ʼ]�ɶ�ƥ��
		 * ���������
				<<svr_end>>
				: iReturnCode := : p_error_no;
				END;
				END-EXEC;
				if ( (SQLCODE == -28) || (SQLCODE == -1012) )
				lpConn->setErrMessage(HSDB_CONNECTION_STATUS_DISCONN,SQLCODE,sqlca.sqlerrm.sqlerrmc);
				if (SQLCODE != OK_SUCCESS)
				{
				p_error_no = 119;
				iReturnCode = 119;
				hs_strcpy(p_error_info,"PROC�����ڲ�����");
				v_error_id = SQLCODE;
				hs_strcpy(v_error_sysinfo,sqlca.sqlerrm.sqlerrmc);
				goto svr_end;
				}

		 */
		addMacroNameToMacroList(token,context);//�Ѻ����ӵ������ݿ��б��Լ�proc�б���
		List<ICodeToken> codeList = new ArrayList<ICodeToken>();
		addVarList(token,context);
		if(!checkIsMacroMatch(context)){//����˺겻��PRO*C���鿪ʼ��ƥ��
			fireEvent(context);//����ȱ��PRO*C���鿪ʼ���¼�
		}
		codeList.add(new PROCBlockEndToken());//��Ӵ���codeToken
		removeDomain(context);//ɾ����
		
	  return codeList.iterator();//����codeToken�б�
	}
	
	
	/**
	 * �����PRO*C��������Ƿ���PRO*C���鿪ʼƥ��
	 * @return
	 */
	private boolean checkIsMacroMatch(Map<Object, Object> context){
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		ITokenDomain procBlockBeginDomain =handler.getDomain(MacroConstant.PROC_BLOCK_BEGIN_MACRONAME);
		//���ǰ����PRO*C���鿪ʼ��procBlockBeginDomain��Ϊnull
		return procBlockBeginDomain!=null;
	}
	
	
	/**
	 * ɾ����
	 */
	private void removeDomain(Map<Object, Object> context){
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		handler.removeDomain(MacroConstant.PROC_BLOCK_BEGIN_MACRONAME);
	}
	/**
	 * ����PRO*C���鿪ʼ�¼�
	 */
	private void fireEvent(Map<Object, Object> context){
		ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
		String message = String.format("��[%1s]ȱ�ٺ�[%2s]", MacroConstant.PROC_BLOCK_END_MACRONAME,MacroConstant.PROC_BLOCK_BEGIN_MACRONAME);
		manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
	}
	
	/**
	 * �ѱ�����ӵ�proc�б���ȥ
	 * 
	 * @param procVarList
	 */
	private void addVarList(IMacroToken token,
			Map<Object, Object> context) {
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
			//���뵽proc�����б��У�����proc����������proc����������
		List<String> varList = new ArrayList<String>(4);
			varList.add("error_no");
		for(String varName:varList){
			helper.addAttribute(IAtomEngineContextConstant.ATTR_PROC_VARIABLE_LIST, varName);
			popVarList.add(varName);
		}
			
	}
	/**
	 * �Ѻ������뵽���б���
	 */
	private void addMacroNameToMacroList(IMacroToken token,Map<Object, Object> context){
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		helper.addAttribute(IAtomEngineContextConstant.ATTR_DATABASE_MACRO,token.getKeyword());//��ӵ����ݿ��б���
	    helper.addAttribute(IAtomEngineContextConstant.ATTR_PROC_MACRO,token.getKeyword());//��ӵ�proc���б���
	}
	
}
