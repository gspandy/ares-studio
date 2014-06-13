/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.token;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.constant.MarkConfig;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procedure.compiler.constant.IProcedureEngineContextConstant;
import com.hundsun.ares.studio.procedure.compiler.skeleton.util.ProcedureCompilerUtil;

/**
 * ���̵����ӹ���Token
 * @author liaogc
 *
 */
public class SubProcedureCallToken implements ICodeToken{
	private static final String NL = ITokenConstant.NL;
	private static String FLAG_M="M";//m���
	private static String PREFIX_P="p_";//ǰ׺p
	private static String PREFIX_V="v_";//ǰ׺v
	private static String ORACLE_ASSIGN=" => ";
	private IMacroToken macroToken;
	private Procedure procedure;//���õĹ���ģ��
	private Procedure subProcedure;//�����õĹ���ģ��
	private Map<String, String> defaultValueMap ;//Ĭ��ֵ�б�Map<Object, Object> context
	private boolean isInTransaction;//�Ƿ���������
	
	
	public SubProcedureCallToken(IMacroToken macroToken,Procedure procedure,Procedure subProcedure,Map<String, String> defaultValueMap,boolean isInTransaction){
		this.macroToken = macroToken;
		this.procedure = procedure;
		this.subProcedure = subProcedure;
		this.defaultValueMap = defaultValueMap;
		this.isInTransaction = isInTransaction;
		
	}
	
	

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#getContent()
	 */
	@Override
	public String getContent() {
		return StringUtils.EMPTY;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#getType()
	 */
	@Override
	public int getType() {
		return ICodeToken.CODE_TEXT;
	}
	
	/**
	 * �����õĹ���ҵ�������Ƿ�Ϊ����
	 * ����Ϊ����ʱ��û�з���ֵ
	 * @return 
	 * 			true   ���� 
	 * 			false  ����
	 */
	private boolean isSubProcedureProcdure(){
		if(null != subProcedure &&
				StringUtils.equalsIgnoreCase(subProcedure.getBizType(), IProcedureEngineContextConstant.procedure)) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#genCode(java.util.Map)
	 */
	@Override
	public String genCode(Map<Object, Object> context) throws Exception {
		StringBuffer subCallCode = new StringBuffer();
		IARESProject project = (IARESProject)context.get(IProcedureEngineContextConstant.Aresproject);
		subCallCode.append("--���ù��� :" ).append(subProcedure.getChineseName()).append(NL) ;
		if(isSubProcedureProcdure()){//����Ϊ����ʱ��û�з���ֵ������Ҫ�������չ��̷���ֵ��ֱ�ӵ��ü���
			subCallCode.append(subProcedure.getName()).append(NL) ;
		}else {
			subCallCode.append("@error_no := ").append(subProcedure.getName()).append(NL) ;
		}
		subCallCode.append(MarkConfig.MARK_LEFTSIGN).append(NL);
		for(Parameter parameter :subProcedure.getInputParameters()){

			String defaultValue = this.defaultValueMap.get(parameter.getId());//��Ĭ��ֵ������
			
			if (defaultValue != null){//��Ĭ��ֵ������
				subCallCode.append(PREFIX_P).append(parameter.getId()).append(ORACLE_ASSIGN).append(defaultValue).append(NL);
				
			}else if(ProcedureCompilerUtil.isParameterINInputAndOutputParameterByName(procedure, parameter.getId(),project)){//�����������ǰ�ڲ�������
				subCallCode.append(PREFIX_P).append(parameter.getId()).append(ORACLE_ASSIGN).append(MarkConfig.MARK_AT).append(parameter.getId()).append(MarkConfig.MARK_COMMA).append(NL);
			} else {
				//�ں���Ҫ���������ӹ��������ֶ����ӵ�������ȥ
				subCallCode.append(PREFIX_P).append(parameter.getId()).append(ORACLE_ASSIGN).append(PREFIX_V).append(parameter.getId()).append(MarkConfig.MARK_COMMA).append(NL);
				
			}
		}
		for(Parameter parameter :subProcedure.getOutputParameters()){

			String defaultValue = this.defaultValueMap.get(parameter.getId());//��Ĭ��ֵ������
			
			if (defaultValue != null){//��Ĭ��ֵ������
				subCallCode.append(PREFIX_P).append(parameter.getId()).append(ORACLE_ASSIGN).append(defaultValue).append(NL);
				
			}else if(ProcedureCompilerUtil.isParameterINInputAndOutputParameterByName(procedure, parameter.getId(),project)){//�����������ǰ�ڲ�������
				subCallCode.append(PREFIX_P).append(parameter.getId()).append(ORACLE_ASSIGN).append(MarkConfig.MARK_AT).append(parameter.getId()).append(MarkConfig.MARK_COMMA).append(NL);
			} else {
				//�ں���Ҫ���������ӹ��������ֶ����ӵ�������ȥ
				subCallCode.append(PREFIX_P).append(parameter.getId()).append(ORACLE_ASSIGN).append(PREFIX_V).append(parameter.getId()).append(MarkConfig.MARK_COMMA).append(NL);
				
			}
			
		}
		if(subCallCode.lastIndexOf(",") != -1){//���һ�����Ŵ���һ��
			subCallCode.deleteCharAt(subCallCode.lastIndexOf(","));
		}
		subCallCode.append(MarkConfig.MARK_RIGHTSIGN).append(MarkConfig.MARK_SEMICOLON).append(NL);
		
		if(!isSubProcedureProcdure()){//����Ϊ����ʱ��û�д���ֵ���أ�Ҳ�������д�������Ϣ
			subCallCode.append(getErrorString());//������
			subCallCode.append("else").append(NL);
			subCallCode.append("  @error_pathinfo := v_error_pathinfo_tmp;").append(NL);
			subCallCode.append("end if;").append(NL);
		}
		
		
		return subCallCode.toString();
	}
	/**
	 * ����������
	 * @return  �������ַ���
	 */
	private String getErrorString(){
		StringBuffer error = new StringBuffer();
		error.append("if @error_no != 0 then").append(NL);
		boolean noText = true;
		
		
		if(isInTransaction){//����ӹ���������֮�У���������Ҫrollback
			noText = false;
			error.append("rollback;").append(NL);
		 }
		
		if(procedure.isOutputCollection()){//����ӹ����ǽ�������أ���Ҫ�α걨����
			noText = false;
			error.append("open @cursor for").append(NL);
			error.append("select @error_pathinfo as error_pathinfo,@error_no as error_no, @error_info as error_info,@error_id as error_id, @error_sysinfo as error_sysinfo\n");
			error.append("from dual;").append(NL);
		}
		if(macroToken.getFlag().indexOf(FLAG_M) == -1){
			noText = false;
			error.append("  return(@error_no);").append(NL);
		}
		if(noText){
			error.append("  NULL;").append(NL);
		}
		return error.toString();
	}

}
