/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.macro.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hundsun.ares.studio.atom.compiler.macro.MacroConstant;
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
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procedure.compiler.constant.IProcedureEngineContextConstant;
import com.hundsun.ares.studio.procedure.compiler.skeleton.util.ProcedureCompilerUtil;
import com.hundsun.ares.studio.procedure.compiler.token.SelectInsertTableInProcBlockToken;

/**
 * @author zhuyf
 *
 */
public class SelectInsertTableInProcBlockMacroHandler implements
		IMacroTokenHandler {
	
	private Procedure p;//ԭ�Ӻ���ģ��

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#getKey()
	 */
	@Override
	public String getKey() {
		return MacroConstant.SELECT_INSERT_TABLE_INPROCBLOCK_MACRONAME;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler#handle(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		/*
		 * ����������
		[SELECT������¼][Ŀ���][Դ��][Ĭ��ֵ]
		�������̣�
		��α����༭��������[select������¼][Ŀ���][Դ��][Ĭ��ֵ]���������Ԥ��tabҳ���鿴��Ӧ����ʵ���롣
		1.�ھ���Ĵ�������У�����ȡ��Ŀ����Դ��ı����������һ�����ڣ����׳��쳣��������¼,�����ڡ����ٸ���Ŀ������õ�Ŀ������е��ֶ�����������ֶ��ڱ�׼�ֶ����Ҳ��������׳��쳣�����ֶβ����ڡ�����ÿ���ֶ�ֵ��������ֵ��ȡֵ�������£�
		�����ж���[Ĭ��ֵ]���Ƿ��и��ֶε�Ĭ��ֵ���������ȡ����Ĭ��ֵ�����û�оʹ�Դ�����ң����Դ���д��ں͸��ֶ���ͬ�ı��ֶ���ȡ��Դ����SELECTȡ�ø��ֶε�ֵ��Ϊ����ֵ�����û�оʹ���������������ڲ�������Ѱ�ң�������ں͸��ֶ�ͬ���ı����򽫸ñ�����ֵ��Ϊ����ֵ�������û��������ֶ����ӱ�׼�ֶ��б���ȡ�ĸ��ֶε���ʵĬ��ֵ��Ϊ����ֵ��
		2.�ú�һ������ڹ��̻�����PROC�����С�
		3.���δ��PROC������ʹ�ã����׳��쳣��
		4.Ŀ�����Դ��֧�ֿ��û�������¼����[SELECT������¼][hs_data.rl_stockholderjour][hs_asset.cl_stockholderjour]
		5.Դ��֧��SQL����[SELECT������¼][clientjour][select a.*,b.full_name from client a,clientinfo b where a.client_id=b.client_id]
		6.֧�ֿ��û���������[SELECT������¼][his_clientjour][select a.*,b.full_name from client a,clientinfo b where a.client_id=b.client_id][clientjour]
			[SELECT������¼][fil_clientjour][select a.*,b.full_name from client a,clientinfo b where a.client_id=b.client_id][clientjour]
			[SELECT������¼][r_clientjour][select a.*,b.full_name from client a,clientinfo b where a.client_id=b.client_id][clientjour]
			[SELECT������¼][rl_clientjour][select a.*,b.full_name from client a,clientinfo b where a.client_id=b.client_id][clientjour]
		7.�������������
		[SELECT������¼][hs_his.his_demoinfojour][select a.*, 
		nvl( (select * from sysdictionary b 
         where b.dict_entry = < CNST_DICT_USER_TYPE > 
   			and b.subentry = a.user_type) ,' ') as user_type_name 
  			from hs_user.demoinfojour a][demoinfojour]
		 */
		this.p = (Procedure) context.get(IProcedureEngineContextConstant.ResourceModel);
		this.addMacroNameToMacroList(token, context);
		List<ICodeToken> codes = new ArrayList<ICodeToken>();
		if (token.getParameters().length >=2) {		
//			if (!isInProcBlock(context)) {//�ж��Ƿ���ProcBlock����
//				throw new MacroNotInProcBlockException("[SELECT������¼]");
//			}else {
				Map<String, String> defaultValueMap = new HashMap<String, String>();
				if(token.getParameters().length> 2){
					defaultValueMap = PseudoCodeParser.parserKeyValueWithAt(token.getParameters()[2]);//���� Ĭ��ֵ�б�
				}
				addPVarList(defaultValueMap,context);//��Ĭ��ֵ�б��еı����ӵ������б���
				codes.add(new SelectInsertTableInProcBlockToken(token, context,defaultValueMap));
//			}
		} else {
			fireEventLessParameter(context);// ����ȱ�ٲ����¼�
		}
		
		return codes.iterator();
	}
	/**
	 * ����ȱ�ٲ����¼�
	 */
	
	private void fireEventLessParameter(Map<Object, Object> context){

		ITokenListenerManager  manager =(ITokenListenerManager) context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
		String message = String.format("��[%s]ȱ�ٲ����������б���������", MacroConstant.SELECT_INSERT_TABLE_INPROCBLOCK_MACRONAME);
		manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
	
	}
	
	/**
	 * ���ֶι���ӵ������б���ȥ
	 * 
	 * @param procVarList
	 * @param tableColumns
	 */
	private void addPVarList(Map<String, String> defaultValueMap,
			Map<Object, Object> context) {
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IProcedureEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		IARESProject project = (IARESProject) context.get(IProcedureEngineContextConstant.Aresproject);
		for(String paramKey: defaultValueMap.keySet()){
			String valueVarName =defaultValueMap.get(paramKey);
			if (valueVarName.indexOf("@") >= 0 && !ProcedureCompilerUtil.isParameterINProcedureParameterByName(this.p, valueVarName.substring(valueVarName.indexOf("@") + 1),project)) {// ���Ĭ�ϲ���ֵΪ����
				String procVarName = valueVarName.substring(valueVarName.indexOf("@") + 1);
				((ISkeletonAttributeHelper)context.get(IProcedureEngineContextConstant.SKELETON_ATTRIBUTE_HELPER)).addAttribute(IProcedureEngineContextConstant.ATTR_PROC_VARIABLE_LIST,procVarName);
				helper.addAttribute(IProcedureEngineContextConstant.ATTR_PROC_VARIABLE_LIST,procVarName);
				popVarList.add(procVarName);
			}
		}
		
	
	}
	

	
	/**
	 * �Ѻ������뵽���б���
	 */
	private void addMacroNameToMacroList(IMacroToken token,Map<Object, Object> context){
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IProcedureEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		helper.addAttribute(IProcedureEngineContextConstant.ATTR_DATABASE_MACRO,token.getKeyword());//��ӵ����ݿ��б���
	    helper.addAttribute(IProcedureEngineContextConstant.ATTR_PROC_MACRO,token.getKeyword());//��ӵ�proc���б���
	}
	

}
