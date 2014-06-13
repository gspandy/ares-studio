/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.mysql.token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.constant.MarkConfig;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procedure.compiler.mysql.constant.IProcedureEngineContextConstantMySQL;
import com.hundsun.ares.studio.procedure.compiler.mysql.skeleton.util.ProcedureCompilerUtil;

/**
 * �洢���̲��������������
 * @author liaogc
 *
 */
public class ProcedureParametersDefineToken implements ICodeToken {
	private static int IN_PARAMETER = 0;//�������
	private static int OUT_PARAMETER = 1;//�������
	private static int FLAG_IN = 0;//���������"I"
	private static int FLAG_OUT = 1;//���������"O"
	private static int FLAG_INOUT = 2;//������������"IO"
	private static String DATA_TYPE_CURSOR="cursor";//�α�����
	private static String BLANK = " ";//�ո�
	private static String NL = ICodeToken.NEWLINE;//����
	private static String COMMENT="--";//ע��
	private final static String PARAM_DEFINE = "\tp_%1$s  %2$s  hstype.%3$s%4$s";
	private Procedure procedure;//����ģ��
	private static  Map<Object, Object> context;
	//private boolean notDefineConnectType;
	
	

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

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.token.ICodeToken#genCode(java.util.Map)
	 */
	@Override
	public String genCode(Map<Object, Object> context) throws Exception {
		// TODO ���̲��������������������������
		// �ο���[�����������]
		//[�����������]
		this.procedure = (Procedure)context.get(IProcedureEngineContextConstantMySQL.ResourceModel);
		this.context = context;
		//this.notDefineConnectType = (Boolean) context.get(IProcedureEngineContextConstantMySQL.not_define_connect_type);
		
		IARESProject project = (IARESProject)context.get(IProcedureEngineContextConstantMySQL.Aresproject);
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IProcedureEngineContextConstantMySQL.SKELETON_ATTRIBUTE_HELPER);
		
		Set<String> parameters = new  HashSet<String>();//��¼�����Ƕ���
		StringBuffer declareCode = new StringBuffer();
		
		int index = 0;//����
		List<Parameter> totleInputParam = new ArrayList<Parameter>();
		ProcedureCompilerUtil.getTotleParameters(project ,this.procedure.getInputParameters() ,totleInputParam);
		List<Parameter> totleOutputParam = new ArrayList<Parameter>();
		ProcedureCompilerUtil.getTotleParameters(project ,this.procedure.getOutputParameters() ,totleOutputParam);
		//�����������
		for(Parameter parameter: totleInputParam){
			if(!parameters.contains(parameter.getId())){
				parameters.add(parameter.getId());
				helper.addAttribute(IProcedureEngineContextConstantMySQL.ATTR_IN_OUT_PARAM_LIST, parameter.getId());

				try {
					String defineStr = getParamDefineCodeStr(parameter,IN_PARAMETER, project);
					if(index != totleInputParam.size() -1 ){
						defineStr += ",";
					}else {//���һ����Ҫ���Ƿ����������
						//���������������Ϊ��������أ�ҲҪ�ӡ�����
						if(totleOutputParam.size() > 0 || procedure.isOutputCollection()){
							defineStr += ",";
						}
					}
					
					if(StringUtils.isNotBlank(defineStr)){
						String chineseName = parameter.getComments();//����ʹ�ñ�ע��Ϊע����Ϣ
						if(StringUtils.isBlank(chineseName)){
							if(parameter.getParamType().getValue()==ParamType.NON_STD_FIELD_VALUE){
								chineseName = parameter.getName();
							}
							if(StringUtils.isBlank(chineseName)){
								chineseName = this.getChineseName(project, parameter.getId());
							}
						}
						declareCode.append(defineStr).append(BLANK).append(COMMENT).append(chineseName).append(NL);
						}
					} catch (Exception e) {
				}
			}
			index++;
		}
		declareCode.append(NL);
		
		index = 0;//���¼���
		//�����������
		
		if(procedure.isOutputCollection()) {
			//���ؽ����ΪTure���������α���������Լ�����IO����־����������������������������
			for(Parameter parameter: totleOutputParam){
				if(StringUtils.equals(MarkConfig.MARK_IOFLAG, parameter.getFlags())){
					if (!parameters.contains(parameter.getId())) {
						parameters.add(parameter.getId());
						helper.addAttribute(IProcedureEngineContextConstantMySQL.ATTR_IN_OUT_PARAM_LIST, parameter.getId());
						try {
							String defineStr = getParamDefineCodeStr(parameter,OUT_PARAMETER, project);
							if(index != totleOutputParam.size() - 1) {
								defineStr += ",";
							}else {//���һ����Ҫ���Ƿ�����α�
								if(!ProcedureCompilerUtil.isParameterINInputAndOutputParameterByName(procedure, "cursor",project)){
									if (!parameters.contains("cursor")) {//�������α����ͣ�����Ҫ����һ���α����ͣ��˴���δ�����һ��
										defineStr += ",";
									}
								}
							}
							
							if (StringUtils.isNotBlank(defineStr)) {
								String chineseName = parameter.getComments();//����ʹ�ñ�ע��Ϊע����Ϣ
								if(StringUtils.isBlank(chineseName)){
									if(parameter.getParamType().getValue()==ParamType.NON_STD_FIELD_VALUE){
										chineseName = parameter.getName();
									}
									if(StringUtils.isBlank(chineseName)){
										chineseName = this.getChineseName(project, parameter.getId());
									}
								}
								declareCode.append(defineStr).append(BLANK).append(COMMENT).append(chineseName).append(NL);
							}
						} catch (Exception e) {
						}
					}
				}
				index++;
			}
			//�α����
				parameters.add("cursor");
				helper.addAttribute(IProcedureEngineContextConstantMySQL.ATTR_IN_OUT_PARAM_LIST, "cursor");
				declareCode.append("	p_cursor  out  hstype.t_cursor  -- ���������α�\n");
		}else {
			for(Parameter parameter: totleOutputParam){
				if (!parameters.contains(parameter.getId())) {
					parameters.add(parameter.getId());
					helper.addAttribute(IProcedureEngineContextConstantMySQL.ATTR_IN_OUT_PARAM_LIST, parameter.getId());
					try {
						String defineStr = getParamDefineCodeStr(parameter,OUT_PARAMETER, project);
						if(index != totleOutputParam.size() - 1) {
							defineStr += ",";
						}//���һ������Ҫ���","
						
						if (StringUtils.isNotBlank(defineStr)) {
							String chineseName = parameter.getComments();//����ʹ�ñ�ע��Ϊע����Ϣ
							if(StringUtils.isBlank(chineseName)){
								if(parameter.getParamType().getValue()==ParamType.NON_STD_FIELD_VALUE){
									chineseName = parameter.getName();
								}
								if(StringUtils.isBlank(chineseName)){
									chineseName = this.getChineseName(project, parameter.getId());
								}
							}
							declareCode.append(defineStr).append(BLANK).append(COMMENT).append(chineseName).append(NL);
						}
					} catch (Exception e) {
					}
				}
				index++;
			}
		}
		
		return declareCode.toString();
	}

	/**
	 * ����һ���������������Ͷ��崮
	 * @param parameter ����
	 * @param type ��������������
	 * @param project ��ǰ��Ŀ
	 * @return
	 */
	
	private String getParamDefineCodeStr(Parameter parameter,int type,IARESProject project) throws Exception{
		StringBuffer codeBuffer = new StringBuffer();
		String inOutStr = StringUtils.EMPTY;
		int flag = FLAG_IN ;
		if(StringUtils.equals(MarkConfig.MARK_IOFLAG, parameter.getFlags())){
			inOutStr = "in  out ";
			flag = FLAG_INOUT;
		}else if(IN_PARAMETER==type){
			inOutStr = StringUtils.EMPTY;
			flag = FLAG_IN;
		}else if(OUT_PARAMETER==type){
			inOutStr = "out";
			flag = FLAG_OUT;
		}
		if(StringUtils.indexOf( parameter.getFlags(), "O")>-1 ||StringUtils.indexOf( parameter.getFlags(), "o")>-1){
			flag = FLAG_OUT;
		}
		if(parameter.getParamType().getValue() == ParamType.NON_STD_FIELD_VALUE){//����Ǳ�׼�ֶ�
			String dataType = parameter.getType();
			if(StringUtils.isNotBlank(dataType)){
				if(StringUtils.equals(DATA_TYPE_CURSOR, dataType)){//�����α�(���������ʱ�Ȳ�����)
					codeBuffer.append(String.format(PARAM_DEFINE, parameter.getId(),inOutStr,dataType,StringUtils.EMPTY));
				}else{//�������
					codeBuffer.append(String.format(PARAM_DEFINE, parameter.getId(),inOutStr,dataType,"%type"));
				}
			}
			
		}else if(parameter.getParamType().getValue() == ParamType.STD_FIELD_VALUE){
			BusinessDataType businessDataType = null;
			try {
				businessDataType = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(project, parameter.getId());
			} catch (Exception e) {
				ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
				String message = String.format("����[%1s]]���ҵ�����ͳ���.����ԭ��:�ò���Ϊ��׼�ֶβ���,���ǻ�ȡ��Ӧ��ҵ������ʱ����",parameter.getId());
				manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
			}
			if(businessDataType!=null){
				String dataType= businessDataType.getName();
				String realType = StringUtils.EMPTY;
				StandardDataType stdType = MetadataServiceProvider.getStandardDataTypeOfBizTypeByName(project, dataType);
				if(null != stdType) {
					realType = stdType.getValue(ProcedureCompilerUtil.getDatabaseType(project));
				}
				
				if(StringUtils.isNotBlank(dataType)){
					if(StringUtils.equals(DATA_TYPE_CURSOR, realType)){//�����α�(���������ʱ�Ȳ�����)
						codeBuffer.append(String.format(PARAM_DEFINE, parameter.getId(),inOutStr,dataType,StringUtils.EMPTY));
					}else{//�������					
						codeBuffer.append(String.format(PARAM_DEFINE, parameter.getId(),inOutStr,dataType,"%type"));
						
						//����������defaultValue
						if(IN_PARAMETER==type && flag==FLAG_IN){
							String defaultValue = "";
							try {
								defaultValue = ProcedureCompilerUtil.getParameterDefaultValue(parameter, project);
								codeBuffer.append("  DEFAULT  " + defaultValue);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		
//		codeBuffer.append(",");
		return codeBuffer.toString();
	}
	
	/**
	 * ���ر�׼�ֶε�������
	 * @param project
	 * @param fieldName
	 * @return
	 */
	private String getChineseName(IARESProject project, String fieldName){
		StandardField field=null;
		try {
			field = MetadataServiceProvider.getStandardFieldByName(project, fieldName);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		if(field!=null){
			return StringUtils.defaultIfBlank(field.getChineseName(), StringUtils.EMPTY);
		}else {
			return StringUtils.defaultIfBlank(fieldName, StringUtils.EMPTY);
		}
	}

}
