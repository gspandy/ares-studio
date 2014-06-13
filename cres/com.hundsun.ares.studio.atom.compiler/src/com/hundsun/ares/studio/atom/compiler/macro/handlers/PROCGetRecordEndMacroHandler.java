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
import com.hundsun.ares.studio.atom.compiler.token.PROCGetRecordEndToken;
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
public class PROCGetRecordEndMacroHandler implements IMacroTokenHandler {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.PROC_GET_RECORD_END_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*
		 * ����������
			[PRO*C��¼��ȡ����]
			�������̣�
			1.����Ƿ���[PRO*C��¼��ȡ��ʼ]�ɶ�ƥ��
			2.����������䣺
					EXEC SQL FETCH cursor[�����]+[���] INTO [�ֶ��б�@ת��Ϊproc����];
			        if ((SQLCODE != OK_SUCCESS) && (SQLCODE != 100) && (SQLCODE != 1403))
			              {
			                 iReturnCode = SQLCODE;
			                 @error_no = SQLCODE;
			                 
			                 hs_strcpy(@error_info, sqlca.sqlerrm.sqlerrmc);
			                 @error_id = 0;
			              }
			            } while (SQLCODE == OK_SUCCESS);
			        }
			        else if ((SQLCODE != 100) && (SQLCODE != 1403))
			        {
			            iReturnCode = SQLCODE;
			            @error_no = SQLCODE;
			            hs_strcpy(@error_info, sqlca.sqlerrm.sqlerrmc);
			            @error_id = 0;
			        }
			        EXEC SQL CLOSE cursor[�����]+[���];
			        if ( (iReturnCode != 0) || (SQLCODE != OK_SUCCESS) )
			            goto svr_end;
			�����ֶ��б�ȡ�Գɶ�ƥ���[PRO*C��¼��ȡ��ʼ][Ҫ����ֶ��б�]
		 */
		addMacroNameToMacroList(token,context);//�Ѻ����ӵ������ݿ��б��Լ�proc�б���
		List<ICodeToken> codeList = new ArrayList<ICodeToken>();
		
		if(!checkIsMacroMatch(context)){//���PRO*C��¼��ȡ�������Ƿ���PRO*C��¼��ȡ��ʼ��ƥ��
			fireEvent(context);// ����ȱ��PRO*C��¼��ȡ��ʼ���¼�
		}
		String curId = "";
		{
			IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
			ITokenDomain curTokenDomain = handler.getDomain("curList");
			if(curTokenDomain!=null){
				curId = curTokenDomain.getArgs()[curTokenDomain.getArgs().length -1].toString();
				Object[] objs = curTokenDomain.getArgs();
				String[] str = new String[objs.length - 1];
				System.arraycopy(objs, 0, str, 0, objs.length-1);
				//������������������ʱ��ɾ��ԭʼ�����һ��ͬ������ȥ��ԭʼ������һλ������
				if (objs.length > 1) {
					handler.removeDomain("curList");
					handler.addDomain(new TokenDomain("curList",str));
				}else if (objs.length <= 1) {//�������ֻ��һ����������ɾ����
					handler.removeDomain("curList");
				}
			}
		}
		String cursorName = "cursor"+curId;
		codeList.add(new PROCGetRecordEndToken(token,cursorName));//��Ӻ�codeToken
		removeDomain(context);//ɾ����
	   return codeList.iterator();
	}
	
	 /* *
	  * �����PRO*C��¼��ȡ�������Ƿ���PRO*C��¼��ȡ��ʼ��ƥ��
	 * @return
	 */
	private boolean checkIsMacroMatch(Map<Object, Object> context){
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		ITokenDomain procGetRecordBeginDomain =handler.getDomain(MacroConstant.PROC_GET_RECORD_BEGIN_MACRONAME);
		//���ǰ����PRO*C��¼��ȡ��ʼ��procGetRecordBeginDomain��Ϊnull
		return procGetRecordBeginDomain!=null;
	}
	
	/**
	 * ɾ����
	 */
	private void removeDomain(Map<Object, Object> context){
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		handler.removeDomain(MacroConstant.PROC_BLOCK_BEGIN_MACRONAME);
	}

	/**
	 * ������
	 */
	private ITokenDomain getDomain(Map<Object, Object> context){
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		return handler.getDomain(MacroConstant.PROC_GET_RECORD_BEGIN_MACRONAME);
	}

	/**
	 * ����ȱ��PRO*C��¼��ȡ��ʼ�¼�
	 */
	private void fireEvent(Map<Object, Object> context){

		ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
		String message = String.format("��[%1s]ȱ�ٺ�[%2s]", this.getKey(),MacroConstant.PROC_GET_RECORD_BEGIN_MACRONAME);
		manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
	
		
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
