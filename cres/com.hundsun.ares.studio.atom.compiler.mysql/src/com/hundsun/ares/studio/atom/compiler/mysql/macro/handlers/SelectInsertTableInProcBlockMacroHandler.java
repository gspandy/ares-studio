/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.mysql.constant.IAtomEngineContextConstantMySQL;
import com.hundsun.ares.studio.atom.compiler.mysql.exception.MacroNotInProcBlockException;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.MacroConstant;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.AtomFunctionCompilerUtil;
import com.hundsun.ares.studio.atom.compiler.mysql.skeleton.util.TableResourceUtil;
import com.hundsun.ares.studio.atom.compiler.mysql.token.SelectInsertTableInProcBlockToken;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.parser.PseudoCodeParser;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.ITokenDomain;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;

/**
 * @author zhuyf
 *
 */
public class SelectInsertTableInProcBlockMacroHandler implements
		IMacroTokenHandler {
	
	private AtomFunction atomFunction;//ԭ�Ӻ���ģ��

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
		this.atomFunction = (AtomFunction) context.get(IAtomEngineContextConstantMySQL.ResourceModel);
		this.addMacroNameToMacroList(token, context);
		List<ICodeToken> codes = new ArrayList<ICodeToken>();
		if (token.getParameters().length >=2) {		
			if (!isInProcBlock(context)) {//�ж��Ƿ���ProcBlock����
				throw new MacroNotInProcBlockException("[SELECT������¼]");
			}else {
				Map<String, String> defaultValueMap = new HashMap<String, String>();
				if(token.getParameters().length> 2){
					defaultValueMap = PseudoCodeParser.parserKeyValueWithAt(token.getParameters()[2]);//���� Ĭ��ֵ�б�
				}
				addFieldToProc(token,context,defaultValueMap);
				addPVarList(defaultValueMap,context);//��Ĭ��ֵ�б��еı����ӵ������б���
				codes.add(new SelectInsertTableInProcBlockToken(token, context,defaultValueMap));
			}
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
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		IARESProject project = (IARESProject) context.get(IAtomEngineContextConstantMySQL.Aresproject);
		for(String paramKey: defaultValueMap.keySet()){
			String valueVarName =defaultValueMap.get(paramKey);
			if (valueVarName.indexOf("@") >= 0 && !AtomFunctionCompilerUtil.isParameterINAtomFunctionParameterByName(getAtomFunction(), valueVarName.substring(valueVarName.indexOf("@") + 1),project)) {// ���Ĭ�ϲ���ֵΪ����
				String procVarName = valueVarName.substring(valueVarName.indexOf("@") + 1);
				((ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER)).addAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_VARIABLE_LIST,procVarName);
				helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_VARIABLE_LIST,procVarName);
				popVarList.add(procVarName);
			}
		}
		
	
	}
	
	/**
	 * ��һЩ�����ֶ����ӵ�proc�б���ȥ
	 * @param macroToken
	 * @param context
	 * @param defaultValueMap
	 * @throws Exception
	 */
	private void addFieldToProc(IMacroToken macroToken,Map<Object, Object> context,Map<String, String> defaultValueMap) throws Exception{
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		Set<String> procList = (Set<String>)helper.getAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_VARIABLE_LIST);
		IARESProject project = (IARESProject) context.get(IAtomEngineContextConstantMySQL.Aresproject);
		String targerTableName = macroToken.getParameters()[0];//Ŀ�����
		String sourceTableName = macroToken.getParameters()[1];//Դ����
		List<TableColumn>  targetColumns = TableResourceUtil.getFieldsWithoutFlag("H",targerTableName,project);//��ȡĿ���������
		
		for (int i = 0; i < targetColumns.size(); i++) {

			TableColumn targerTableColumn = targetColumns.get(i);
			StandardField tableFieldSTD  = MetadataServiceProvider.getStandardFieldByName((IARESProject)context.get(IAtomEngineContextConstantMySQL.Aresproject), targerTableColumn.getFieldName());
			if (tableFieldSTD != null) {
				if (defaultValueMap.get(tableFieldSTD.getName()) == null) {//��������ֶ���Ĭ��ֵ�б���
					
					TableColumn sourceTableColumn = null;
					// ����������Դ���в�����
					for (TableColumn f : TableResourceUtil.getFieldsWithoutFlag("H", sourceTableName,project)) {
						if (f.getFieldName().equals(targerTableColumn.getFieldName())) {
							sourceTableColumn = f;
							break;
						}
					}
					if (sourceTableColumn == null)// 
					 {//���Դ���в�����
						//�����α�������,proc��������,���,�ڲ������д���
						if (popVarList.contains(tableFieldSTD.getName())|| procList.contains(tableFieldSTD.getName())||AtomFunctionCompilerUtil.isParameterINAtomFunctionParameterByName(getAtomFunction(), tableFieldSTD.getName(),project)) {
							if(AtomFunctionCompilerUtil.isParameterINAtomFunctionParameterByName(getAtomFunction(), tableFieldSTD.getName(),project)){
								procList.add(tableFieldSTD.getName());
							}
							
						} 
					}
				}
			}
		}

		
	
	}
	
	/**
	 * ����Ƿ���proc�������
	 * @return
	 */
	private boolean isInProcBlock(Map<Object, Object> context){
		IDomainHandler handler = (IDomainHandler)  context.get(IEngineContextConstant.DOMAIN_HANDLER);
		ITokenDomain procBlockBeginDomain =handler.getDomain(MacroConstant.PROC_BLOCK_BEGIN_MACRONAME);
		//���ǰ����PRO*C���鿪ʼ��procBlockBeginDomain��Ϊnull
		return procBlockBeginDomain!=null;
	}
	/**
	 * ����ԭ�Ӻ���ģ��
	 * @return
	 */
	private AtomFunction getAtomFunction(){
		return this.atomFunction;
		}
	
	/**
	 * �Ѻ������뵽���б���
	 */
	private void addMacroNameToMacroList(IMacroToken token,Map<Object, Object> context){
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_DATABASE_MACRO,token.getKeyword());//��ӵ����ݿ��б���
	    helper.addAttribute(IAtomEngineContextConstantMySQL.ATTR_PROC_MACRO,token.getKeyword());//��ӵ�proc���б���
	}
	

}
