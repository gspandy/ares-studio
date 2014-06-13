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

import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.token.PackAddFieldHeadToken;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;

/**
 * @author zhuyf
 *
 */
public class PackAddFieldByHandworkMacroHandler implements IMacroTokenHandler {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return MacroConstant.PACK_ADDFIELD_HANDWORK_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*����������
			[�ֹ����ͷ][Ҫ������ֶ��б�]
			�������̣�
			1.��ȡ�ֶ��б����뵽PRO*C������
			2.ÿһ�ֶ����ɣ�
			lpOutPacker->AddField("[�ֶ���]");
			ע������������͵Ĳ���������Ҳ�������仯
			3.[�ֹ����ͷ][client_id,client_name,address][@packer]�����ɴ������£�
				v_packer->BeginPack(); 
				v_packer->AddField("client_id"); 
				v_packer->AddField("client_name");
		 * 
		 */
		List<ICodeToken> tokens = new ArrayList<ICodeToken>();
		
		String[] param_0 = new String[0];
		String param_1 = "";
		if (token.getParameters().length > 0) {
			param_0 = token.getParameters()[0].split(",");
		}
		if (token.getParameters().length > 1) {
			param_1 = token.getParameters()[1];
		}
		//�����ݿ�ı��ֶΣ����뵽proc������
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		helper.addAttribute(IAtomEngineContextConstant.IS_ALREADY_RETURN_RESULTSET, "true");
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		//������ͷ���������뵽��ͨ�����б���ȥ
		for(String fieldName : param_0){
			popVarList.add(fieldName.trim());
		}
		boolean isObjectPack = isObjectPack(param_1);
		if(isObjectPack){
			addObjectPackToVarList(param_1,context);
		}
		if(StringUtils.startsWith(param_1, "@") &&! StringUtils.endsWith(param_1, "ResultSet")){
			param_1+="ResultSet";
		}
		tokens.add(new PackAddFieldHeadToken(param_0,param_1,isObjectPack));
		
		//�����
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		handler.addDomain(new DefaultTokenDomain(getKey(), token.getParameters()));
		
		return tokens.iterator();
	}
	
	/**
	 * �Ƿ��Ƕ�����
	 * @param packName
	 * @return
	 */
	private boolean isObjectPack(String packName){
		return StringUtils.endsWith(packName, "ResultSet");
	}
	
	/**
	 * �Ѷ����������뵽��ر����б���
	 * @param packName
	 */
	private void addObjectPackToVarList(String packName,Map<Object, Object> context){
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		List<String> popObjectVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_OBJECT_PARA_LIST);
		if(StringUtils.startsWith(packName, "@") && StringUtils.endsWith(packName, "ResultSet")){
			String varName = StringUtils.replaceOnce(packName, "@", "");
			
			int endIndex = StringUtils.lastIndexOf(varName, "ResultSet");
			String objectName = StringUtils.substring(varName, 0, endIndex);
			helper.addAttribute(IAtomEngineContextConstant.ATTR_OUT_OBJECT_INIT_VARIABLE_LIST, objectName);
			
			popObjectVarList.add(objectName);
			popVarList.remove(objectName);
			
		}else if(StringUtils.startsWith(packName, "@") &&! StringUtils.endsWith(packName, "ResultSet")){

			String varName = StringUtils.replaceOnce(packName, "@", "");
			popObjectVarList.add(varName);
			helper.addAttribute(IAtomEngineContextConstant.ATTR_OUT_OBJECT_INIT_VARIABLE_LIST, varName);
			popObjectVarList.add(varName);
			popVarList.remove(varName);
		
		}
		
	}

}
