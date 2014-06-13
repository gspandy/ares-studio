/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.AtomService;
import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.mysql.token.PORCStatementToken;
import com.hundsun.ares.studio.core.IARESProject;
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
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;


/**
 * @author zhuyf
 *
 */
public class PROCStatementMacroHandler implements IMacroTokenHandler {
	
	private List<String> queryFieldList;//���ڱ���Select���Ĳ�ѯ�ֶ�
	
	private List<String> inFieldList;//���ڱ��涯̬���������ֶ�
	

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
		queryFieldList = new ArrayList<String>();
		inFieldList = new ArrayList<String>();
		
		addMacroNameToMacroList(token,context);//��ӵ����б��
		if(token.getParameters().length > 0){
			if((token.getParameters()[0].indexOf("select") >= 0) && (token.getParameters()[0].indexOf("into") >= 0)){//������MySQL�﷨Ҫ��
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("SQL[%s]������MySQL�﷨Ҫ��ȥ��select intoд������Ϊselect as��", token.getParameters()[0]);
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			}
			splitFieldList(token,context);//����SQL����е�@���������뵽PRO*C���������б���
			BasicResourceInfo brInfo = (BasicResourceInfo) context.get(IAtomEngineContextConstantMySQL.ResourceModel);
			String sql = PseudoCodeParser.insertCommonForSql(token.getParameters()[0], brInfo.getObjectId());
			ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
			Set<String> rsList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_RESULTSET_LIST);
			Set<String> stateList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_STATEMENT_LIST);
			AtomFunction func = (AtomFunction)context.get(IAtomEngineContextConstantMySQL.ResourceModel);
			int rsTotalSizeAdd1 = rsList.size() + 1;
			int stateTotalSizeAdd1 = stateList.size() + 1;
			String objectId = func.getObjectId();
			if(StringUtils.isBlank(objectId) && (func instanceof AtomService)){
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = "��ȡ���ܺ�ʧ��,�޷���������lpResultSet";
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			}else if(StringUtils.isBlank(objectId) && !(func instanceof AtomService)){
				objectId = func.getName();
			}
			String rsID = objectId + rsTotalSizeAdd1;
			String stateID = objectId + stateTotalSizeAdd1;
			helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_STATEMENT_LIST,stateID);//�����ͷŵĶ�̬����б�
			if(sql.startsWith("select") || sql.startsWith("SELECT")){//selectʱ������Ҫ�����������
				helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_RESULTSET_LIST,rsID);//�����ͷŵĽ�����б�
				helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_GETLAST_RESULTSET,rsID);//
			}
			codeToken.add(new PORCStatementToken(sql,rsID,stateID,queryFieldList,inFieldList));//���ɶ�Ӧ��sql���
		}else{
			fireEventLessParameter(context);//����ȱ�ٲ����¼�
		}
		return codeToken.iterator();
	}
	
	/**
	 * ����SQL����е�@���������뵽PRO*C���������б���
	 * @param procVarList
	 */
	private void splitFieldList(IMacroToken token,Map<Object, Object> context){
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		Pattern p_as = Pattern.compile("\\s+as\\s+@[\\w\\d_]+");
		Pattern p = Pattern.compile("\\s*=\\s*@[\\w\\d_]+");
		//sql����ڵ�һ������
		String sql = token.getParameters()[0];
		Matcher m_as = p_as.matcher(sql);
		while (m_as.find()) {
			int index = m_as.group().indexOf("@");
			String field = m_as.group().substring(index + 1);
			queryFieldList.add(field);
			//�����滻�б�ע�����ﲻ����helper.addAttribute(IAtomEngineContextConstant.PSEUDO_CODE_PARA_LIST, field);
			popVarList.add(field);
		}
		Matcher m = p.matcher(sql);
		while (m.find()) {
			int index = m.group().indexOf("@");
			String field = m.group().substring(index + 1);
			inFieldList.add(field);
			//�����滻�б�ע�����ﲻ����helper.addAttribute(IAtomEngineContextConstant.PSEUDO_CODE_PARA_LIST, field);
			popVarList.add(field);
		}
		
		
	}
	
	
	/**
	 * �Ѻ������뵽���б���
	 */
	private void addMacroNameToMacroList(IMacroToken token,Map<Object, Object> context){
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_DATABASE_MACRO,token.getKeyword());//��ӵ����ݿ��б���
	    helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_MACRO,token.getKeyword());//��ӵ�proc���б���
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
