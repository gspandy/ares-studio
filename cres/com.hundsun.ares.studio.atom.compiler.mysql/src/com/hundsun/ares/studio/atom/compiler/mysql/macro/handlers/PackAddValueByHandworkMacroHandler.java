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

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.mysql.token.PackAddFieldBodyToken;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.ITokenDomain;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;

/**
 * @author zhuyf
 *
 */
public class PackAddValueByHandworkMacroHandler implements IMacroTokenHandler {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.PACK_ADDVALUE_HANDWORK_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*����������
		[�ֹ������]
		�������̣�
		1.����Ƿ���[�ֹ����ͷ]�ɶ�ƥ��
		2.�ӳɶԵ�[�ֹ����ͷ]�л�ȡ�ֶ��б��������ֶΣ���ÿһ�ֶ����ɣ�
			lpOutPacker->Add[�ֶ�����]([�ֶ���]); //[�ֶ�ע��]
		3.[�ֹ������][client_id][@unpack_sett]����ָ�������������������䣺
			v_unpack_sett->Add[�ֶ�����]([�ֶ���]); //[�ֶ�ע��]
			v_unpack_sett->EndPack();
		4.[�ֹ������][client_id][@unpack_sett][N]��������v_unpack_sett->EndPack();
		
	 * 
	 */
		List<ICodeToken> tokens = new ArrayList<ICodeToken>();
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		helper.addAttribute(IAtomEngineContextConstantMySQL.IS_ALREADY_RETURN_RESULTSET, "true");
		IDomainHandler handler = (IDomainHandler) context.get(IEngineContextConstant.DOMAIN_HANDLER);
		ITokenDomain packAddFieldHead =handler.getDomain(MacroConstant.PACK_ADDFIELD_HANDWORK_MACRONAME);
		AtomFunction atomFunction = (AtomFunction) context.get(IAtomEngineContextConstantMySQL.ResourceModel);
		helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_ACK_ADDVALUE_HANDWORK_LIST,atomFunction.getObjectId());//�ֹ�������б�
		if (packAddFieldHead == null) {
			//throw new RuntimeException(getKey() + " �����ڴ��ͷ");
			//������������֧�У����ͷ������δ�سɶԳ��֣���ȥ���ü�顣
		}
		
		String param_0 = "";
		String param_1 = "";
		String param_2 = "";
		if (token.getParameters().length > 1) {
			param_0 = StringUtils.trim(token.getParameters()[1]);
			if(!StringUtils.endsWith(param_0, "ResultSet")){
				param_0+="ResultSet";
			}
		}
		if (token.getParameters().length > 2) {
			param_2 = StringUtils.trim(token.getParameters()[2]);
		}
		if (token.getParameters().length > 0) {
			param_1 = StringUtils.trim(token.getParameters()[0]);
		}
		 String headFeilds = "";
		if (packAddFieldHead != null && packAddFieldHead.getArgs()!=null) {
			headFeilds = (String) packAddFieldHead.getArgs()[0];
		}
		
		tokens.add(new PackAddFieldBodyToken(param_0, param_1,param_2,headFeilds));
		
		//ɾ����
		handler.removeDomain(MacroConstant.PACK_ADDFIELD_HANDWORK_MACRONAME);
		return tokens.iterator();
	}

}
