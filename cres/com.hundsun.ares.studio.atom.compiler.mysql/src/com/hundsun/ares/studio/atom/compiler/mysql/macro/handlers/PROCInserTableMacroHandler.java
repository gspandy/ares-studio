/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.AtomService;
import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.MarkConfig;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.TableResourceUtil;
import com.hundsun.ares.studio.atom.compiler.mysql.token.PROCInsertToken;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.parser.PseudoCodeParser;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.jres.model.database.TableColumn;

/**
 * @author zhuyf
 *
 */
public class PROCInserTableMacroHandler implements IMacroTokenHandler {


	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.PROC_INSERT_TABLE_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*
		 * [PRO*C������¼][table][Ĭ��ֵ]
		 * 1.���ݱ����õ������е��ֶ������ֶζ�����Proc�����б���
		 * 2.��ÿ���ֶ�ֵ��������ֵ��ȡֵ�������£�
				�����ж���[Ĭ��ֵ]���Ƿ��и��ֶε�Ĭ��ֵ��
				������ں͸��ֶ�ͬ���ı����򽫸ñ�����ֵ��Ϊ����ֵ��
				�������ȡ����Ĭ��ֵ�����û�оʹ���������������ڲ�������Ѱ�ң�
				�����û��������ֶ����ӱ�׼�ֶ��б���ȡ�ĸ��ֶε���ʵĬ��ֵ��Ϊ����ֵ��
			3.��Ӵ�������䣬����ú�ĵ�һ�б�־λ������E�������ڳ�������Ҫ�ر��α꣬
			����ٺ����ӿڱ�־��ú�ĵ�һ�б�־�а�����M�������ٳ������в�������goto svr_end����䡣
			4.֧�ֿ��û���������[PRO*C������¼][his_clientjour][clientjour]
			[PRO*C������¼][fil_clientjour][clientjour]
			[PRO*C������¼][r_clientjour][clientjour]
			[PRO*C������¼][rl_clientjour][clientjour]
		 */
		 
		  
		
		List<ICodeToken> codeToken = new ArrayList<ICodeToken>(3);
		
		addMacroNameToMacroList(token,context);//�Ѻ����ӵ������ݿ��б��Լ�proc�б���
		if(token.getParameters().length > 0){
			IARESProject project = (IARESProject) context.get(IAtomEngineContextConstantMySQL.Aresproject);
			List<TableColumn> tableColumns = TableResourceUtil.getFieldsWithoutFlag("H",token.getParameters()[0],project);//��ȡ������Ӧ����
			Map<String, String> defaultValueMap =  new HashMap<String, String>();//Ĭ��ֵ�б�
			if(isContainDefauleValue(token)){
				defaultValueMap =getDefaultValueList(token);
			}
			List<String> fieldNames = getFieldNames(tableColumns);//���Ӧ���ֶ�
			addPVarList(token,context,defaultValueMap,fieldNames);//�Ѹ��ֶμ��뵽�����б���ȥ
			ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
			Set<String> stateList = helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_STATEMENT_LIST);
			AtomFunction func = (AtomFunction)context.get(IAtomEngineContextConstantMySQL.ResourceModel);
			int stateTotalSizeAdd1 = stateList.size() + 1;
			String objectId = func.getObjectId();
			if(StringUtils.isBlank(objectId) && (func instanceof AtomService)){
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = "��ȡ���ܺ�ʧ��,�޷���������lpResultSet";
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			}else if(StringUtils.isBlank(objectId) && !(func instanceof AtomService)){
				objectId = func.getName();
			}
			String stateID = objectId + stateTotalSizeAdd1;
			helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_STATEMENT_LIST,stateID);//�����ͷŵĶ�̬����б�
			codeToken.add(new PROCInsertToken(token,defaultValueMap,tableColumns,stateID));//���proc����������select��䴦��Token
			
		}else{
			fireEventLessParameter(context);//����ȱ�ٲ����¼�
		}
		return codeToken.iterator();
	}
	
	/**
	 * ����ֶ��б�
	 * @param tableColumns
	 */
	private List<String>  getFieldNames(List<TableColumn> tableColumns){
		List<String> fieldNames = new ArrayList<String>(40);
		for(TableColumn tableColumn:tableColumns){
			fieldNames.add(tableColumn.getFieldName());
		}
		return fieldNames;
		
	}
	
	/**
	 * �ж��Ƿ����Ĭ��ֵ
	 * @param token
	 * @return
	 */
	private boolean isContainDefauleValue(IMacroToken token){
		return token.getParameters().length==2 && StringUtils.isNotBlank(token.getParameters()[1]);
		
	}

	/**
	 * ���ֶι���ӵ������б���ȥ
	 * 
	 * @param procVarList
	 * @param tableColumns
	 */
	private void addPVarList(IMacroToken token,Map<Object, Object> context,Map<String, String> defaultValueMap,List<String> fieldNames) {
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		for (String fieldName : fieldNames) {
			if (!defaultValueMap.containsKey(fieldName)) {//���Ĭ��ֵ�б���û�б��ֶζ�Ӧ��ֵ
				helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_VARIABLE_LIST,fieldName);
				popVarList.add(fieldName);
			}else if(defaultValueMap.containsKey(fieldName)){//���Ĭ��ֵ�����ֶζ�Ӧ��Ĭ��ֵ
				String valueVarName = defaultValueMap.get(fieldName);
				if (valueVarName.indexOf(MarkConfig.MARK_AT) >= 0) {// ���Ĭ�ϲ���ֵΪ����
					String procVarName = valueVarName.substring(valueVarName.indexOf(MarkConfig.MARK_AT) + 1);
					helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_VARIABLE_LIST,procVarName);
					popVarList.add(procVarName);
				}
			}
		}
		
		
	
	}
	
	/**
	 * ����Ĭ��ֵ�б�
	 * @return
	 */
	
	private Map<String, String> getDefaultValueList(IMacroToken token) {
		Map<String, String> defaultValueMap = new Hashtable<String, String>();
		// ���PROC������¼�����Ĭ��ֵ�б�
		if (token.getParameters().length > 1) {
			String fieldStr =token.getParameters()[1];
			//fieldStr = EngineUtil.replaceConstant(fieldStr, this.getFunction(), true);
			defaultValueMap = PseudoCodeParser.parserKeyValueWithAt(fieldStr);
		}
		return defaultValueMap;
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
		String message = String.format("��[%s]ȱ�ٲ����������б���������", MacroConstant.PROC_INSERT_TABLE_MACRONAME);
		manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
	
	}

	

	
	
	
}
