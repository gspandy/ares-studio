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

import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.token.PORCStatementToken;
import com.hundsun.ares.studio.core.model.BasicResourceInfo;
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
public class PROCStatementMacroHandler implements IMacroTokenHandler {
	
	

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.PROC_STATEMENT_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*1.����SQL����е�@���������뵽PRO*C���������б���
		 *2.����һ�����������ĵ��еĶ�Ӧ����codetoken
		 */
		
		List<ICodeToken> codeToken = new ArrayList<ICodeToken>(1);
		
		addMacroNameToMacroList(token,context);//��ӵ����б��
		if(token.getParameters().length > 0){
			addProcVarLis(token,context);//����SQL����е�@���������뵽PRO*C���������б���
			BasicResourceInfo brInfo = (BasicResourceInfo) context.get(IAtomEngineContextConstant.ResourceModel);
			String sql = PseudoCodeParser.insertCommonForSql(token.getParameters()[0], brInfo.getObjectId());
			codeToken.add(new PORCStatementToken(sql));//���ɶ�Ӧ��sql���
		}else{
			fireEventLessParameter(context);//����ȱ�ٲ����¼�
		}
		return codeToken.iterator();
	}
	
	/**
	 * ����SQL����е�@���������뵽PRO*C���������б���
	 * @param procVarList
	 */
	private void addProcVarLis(IMacroToken token,Map<Object, Object> context){
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		Pattern p = Pattern.compile("@[\\w\\d_]+");
		//sql����ڵ�һ������
		Matcher m = p.matcher(token.getParameters()[0]);
		while (m.find()) {
			String field = m.group().substring(1);
			helper.addAttribute(IAtomEngineContextConstant.ATTR_PROC_VARIABLE_LIST,field);
			//�����滻�б�ע�����ﲻ����helper.addAttribute(IAtomEngineContextConstant.PSEUDO_CODE_PARA_LIST, field);
			popVarList.add(field);
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
	
	/**
	 * ����ȱ�ٲ����¼�
	 */
	
	private void fireEventLessParameter(Map<Object, Object> context){

		ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
		String message = String.format("��[%s]ȱ�ٲ�����������SQL��������", MacroConstant.PROC_STATEMENT_MACRONAME);
		manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
	
	}
}
