/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.mysql.token.NestObjectResultsetToken;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;

/**
 * @author liaogc
 *
 */
public class NestObjectResultsetMacroHandler implements IMacroTokenHandler{

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.NEST_OBJECT_RESULTSET_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		List<ICodeToken> codeTokens= new ArrayList<ICodeToken>();
		if (token.getParameters().length !=2) {
			ITokenListenerManager  manager =(ITokenListenerManager) context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("��[%s]ȱ�ٲ�����", MacroConstant.NEST_OBJECT_RESULTSET_MACRONAME);
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
		}else{
			String parent = StringUtils.trim(token.getParameters()[0]);
			String child  = StringUtils.trim(token.getParameters()[1]);
			if(StringUtils.indexOf(parent,"@")>-1){//��ȡȡ����Ӧ�Ĳ����Ķ�����
				parent = StringUtils.substring(parent,StringUtils.indexOf(parent,"@")+1);
				if(StringUtils.endsWith(parent,"ResultSet")){
					parent = StringUtils.substring(parent,0,StringUtils.indexOf(parent,"ResultSet"));
				}
				
			}
			if(StringUtils.indexOf(child,"@")>-1){//��ȡȡ����Ӧ�Ĳ����Ķ�����
				child = StringUtils.substring(child,StringUtils.indexOf(child,"@")+1);
				if(StringUtils.endsWith(child,"ResultSet")){
					child = StringUtils.substring(child,0,StringUtils.indexOf(child,"ResultSet"));
				}
				
			}
			codeTokens.add(new NestObjectResultsetToken(parent,child));//������Ӧ��token
			helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_IN_OBJECT_INIT_VARIABLE_LIST, parent);//���뵽Ҫ�����Ķ����б���
			helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_IN_OBJECT_NOINIT_VARIABLE_LIST, child);//���뵽����Ҫ�����Ķ����б���
			List<String> popObjectVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_OBJECT_PARA_LIST);
			popObjectVarList.add(parent);//���뵽����α�����б���
			popObjectVarList.add(child);
			List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
			popVarList.remove(parent);//����ͨα�����б� ��ɾ��
			popVarList.remove(child);
		}
		
		
		return codeTokens.iterator();
	}
	
	

}
