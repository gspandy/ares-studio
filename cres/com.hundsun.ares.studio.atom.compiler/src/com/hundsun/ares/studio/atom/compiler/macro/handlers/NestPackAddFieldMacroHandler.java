/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.macro.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.token.NestPackAddFieldToken;
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
 * @author liaogc
 *
 */
public class NestPackAddFieldMacroHandler  implements IMacroTokenHandler{

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.NEST_PACK_ADD_FIELD_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		

		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		List<ICodeToken> codeTokens= new ArrayList<ICodeToken>();
		if (token.getParameters().length !=2) {
			ITokenListenerManager  manager =(ITokenListenerManager) context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
			String message = String.format("��[%s]ȱ�ٲ�����", MacroConstant.NEST_PACK_ADD_FIELD_MACRONAME);
			manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
		}else{
			String parentPackName = StringUtils.trim(token.getParameters()[0]);//������
			String paramName = "";//������ֶ�(����)
			 String packName = "";//��ʵ�ı�����ֶ�ֵ
			String childrenParam  = StringUtils.trim(token.getParameters()[1]);
			Map<String,String> aliasParameters = PseudoCodeParser.parserKeyValueWithAt(childrenParam);
			if(!aliasParameters.keySet().isEmpty()){
				paramName = StringUtils.trim(aliasParameters.keySet().toArray(new String[aliasParameters.keySet().size()])[0]);
				packName = StringUtils.trim(aliasParameters.get(aliasParameters.keySet().toArray(new String[aliasParameters.keySet().size()])[0]));
			}else{
				paramName = childrenParam;
				packName = childrenParam;
			}

			if(StringUtils.indexOf(packName,"@")>-1){//��ȡȡ����Ӧ�Ĳ�������Ķ�����
				packName = StringUtils.substring(packName,StringUtils.indexOf(packName,"@")+1);
				if(StringUtils.endsWith(packName,"ResultSet") && !StringUtils.equals("lpOutPacker", packName)){
					packName = StringUtils.substring(packName,0,StringUtils.indexOf(packName,"ResultSet"));
				}
				
			}
			if(aliasParameters.keySet().isEmpty()){
				paramName = packName;
			}
			if(StringUtils.indexOf(parentPackName,"@")>-1){
				parentPackName = StringUtils.substring(parentPackName,StringUtils.indexOf(parentPackName,"@")+1);
				if(!StringUtils.endsWith(parentPackName,"ResultSet") && !StringUtils.equals("lpOutPacker", parentPackName)){
					parentPackName+="ResultSet";
				}
				
			}
			
			codeTokens.add(new NestPackAddFieldToken(parentPackName,paramName,packName));//������Ӧ��token
			helper.addAttribute(IAtomEngineContextConstant.ATTR_OUT_OBJECT_INIT_VARIABLE_LIST, packName);//���뵽Ҫ�����Ķ��������б���
			List<String> popObjectVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_OBJECT_PARA_LIST);
			popObjectVarList.add(packName);//���뵽����α�����б���
			List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
			popVarList.remove(packName);//����ͨα�����б� ��ɾ��
		}
		
		
		return codeTokens.iterator();
	
		
		
	}

}
