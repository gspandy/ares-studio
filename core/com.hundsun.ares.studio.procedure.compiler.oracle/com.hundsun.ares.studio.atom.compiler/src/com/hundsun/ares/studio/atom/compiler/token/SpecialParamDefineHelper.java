/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.token;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.InternalParam;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.skeleton.util.AtomFunctionCompilerUtil;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.token.IParamDefineHelper;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;

/**
 * ����������������,��Ҫ���� �꾲̬�ı��еı���
 * @author liaogc
 *
 */
public class SpecialParamDefineHelper {
	private final static String PARAM_DEFINE = "%1$s @%2$s = %3$s;" + ITokenConstant.NL;
	private final static String PARAM_DEFINE_STR = "char @%1$s[%2$s] = {0};" + ITokenConstant.NL;
	private  Map<String,ParamrDefineBean> params  = new HashMap<String,ParamrDefineBean>(10);
	public static SpecialParamDefineHelper instance = new SpecialParamDefineHelper();
	
	public SpecialParamDefineHelper(){
		initParam();
	}

	
 /**
  * 
  * @param project
  * @param context
  * @param type IAtomEngineContextConstant.ATTR_PROC_VARIABLE_LIST,IEngineContextConstant.PSEUDO_CODE_PARA_LIST֮һ
  * @return
  */
	public  String getSpecialParamsDefineCodeStr(IARESProject project,Map<Object, Object> context,Collection col){
		StringBuffer codeBuffer = new StringBuffer();
		//ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
		//Set<String> procVarList = helper.getAttribute(IAtomEngineContextConstant.ATTR_PROC_VARIABLE_LIST);//proc�ݲ�����
		//List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST); 
		List<String> popVarList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
		IParamDefineHelper defineHelper = (IParamDefineHelper)context.get(IAtomEngineContextConstant.PARAM_DEFINE_HELPER);
		//if(popVarList)
			Set<String> paramNameSet =params.keySet();
			for(String paramName:paramNameSet){
				ParamrDefineBean paramBean = null;
				if(col.contains(paramName)){
						 paramBean = params.get(paramName);
				}
			if (paramBean != null) {
				StandardDataType stdType = null;
				String dataType = paramBean.getType();
				String defaultValue = paramBean.getDefaultValue();
				int length = paramBean.getLength();
				try {
					stdType = MetadataServiceProvider
							.getStandardDataTypeOfStdFieldByName(project,
									paramName);
				} catch (Exception e) {
				}
				//����ֻ��Ҫ�����Ǳ�׼�ֶε���Щ��������������Щ�����Ѿ��ڱ�׼�ֶ��ж����ˣ���ôProc������α��������л�ͳһ��������Ҫ�����ﴦ�����ˡ�
				if (stdType == null) {
					//if(!isDefined(paramName)){
						//if (StringUtils.equals(IAtomEngineContextConstant.ATTR_PROC_VARIABLE_LIST,type)) {
						if(defineHelper.canInit(IParamDefineHelper.STD, paramName))
						{
							if (TypeRule.typeRuleCharArray(dataType)) {// �ַ���
								codeBuffer.append(String.format(PARAM_DEFINE_STR,paramName + "", length));
								//setDefined(paramName, Boolean.TRUE);
								defineHelper.addInit(IParamDefineHelper.STD, paramName);
							} else {// ��������ʱ
								codeBuffer.append(String.format(PARAM_DEFINE,dataType, paramName + "", defaultValue));
								//setDefined(paramName, Boolean.TRUE);
								defineHelper.addInit(IParamDefineHelper.STD, paramName);

							}
						//}
//						} else if (StringUtils.equals(IEngineContextConstant.PSEUDO_CODE_PARA_LIST, type)&&!procVarList.contains(paramName)) {
//							if (TypeRule.typeRuleCharArray(dataType)) {// �ַ���
//								codeBuffer.append(String.format(PARAM_DEFINE_STR,paramName + "", length));
//								setDefined(paramName, Boolean.TRUE);
//							} else {// ��������ʱ
//								codeBuffer.append(String.format(PARAM_DEFINE,dataType, paramName + "", defaultValue));
//								setDefined(paramName, Boolean.TRUE);
//							}
//						}

					}
				}else{
					if(defineHelper.canInit(IParamDefineHelper.STD, paramName)&&!isDefined(paramName)){
						codeBuffer.append(getParamDefineCodeStr(paramName,project));
						//setDefined(paramName, Boolean.TRUE);
						defineHelper.addInit(IParamDefineHelper.STD, paramName);
					}
				}

			}
		}
			
		return codeBuffer.toString();
	}
	
	private  void initParam(){
		params.put("result_num",new ParamrDefineBean("result_num","int","0",0,Boolean.FALSE));
		params.put("error_pathinfo_tmp",new ParamrDefineBean("error_pathinfo_tmp","char[501]","{0}",501,Boolean.FALSE));
		params.put("error_no",new ParamrDefineBean("error_no","int","0",0,Boolean.FALSE));
		params.put("error_info",new ParamrDefineBean("error_info","char[501]","{0}",500,Boolean.FALSE));
		params.put("error_id",new ParamrDefineBean("error_id","int","0",0,Boolean.FALSE));
		params.put("error_sysinfo",new ParamrDefineBean("error_sysinfo","char[501]","{0}",500,Boolean.FALSE));
		params.put("error_pathinfo",new ParamrDefineBean("error_pathinfo","char[501]","{0}",500,Boolean.FALSE));
	}
	
	public boolean isSpecialParam(String param_name){
		if(params.get(param_name) == null){
			return false;
		}
		return true;
	}
	
	private  boolean isDefined(String paramName){
		if(params.get(paramName)!=null){
			return params.get(paramName).isDefined();
		}	
		return Boolean.TRUE;
		
	}
	private  void setDefined(String paramName,boolean isDefined){
		if(params.get(paramName)!=null){
			params.get(paramName).setDefined(isDefined);
		}
	}
	
	
	private  class ParamrDefineBean{
		private String name;//������
		private String type;//��������
		private String defaultValue;//Ĭ��ֵ
		private boolean isDefined = false;//�Ƿ��Ѿ�����
		private int length = 0;
		
		
		
		public ParamrDefineBean(String name,String type,String defaultValue,int length,boolean isDefined){
			this.name = name;
			this.type = type;
			this.defaultValue = defaultValue;
			this.isDefined = isDefined;
			this.length = length;
		}
		/**
		 * @return the name
		 */
		private String getName() {
			return name;
		}
		
		/**
		 * @return the type
		 */
		private String getType() {
			return type;
		}
		
		/**
		 * @return the defaultValue
		 */
		private String getDefaultValue() {
			return defaultValue;
		}
		
		/**
		 * @return the isDefined
		 */
		private boolean isDefined() {
			return isDefined;
		}
		/**
		 * @param isDefined the isDefined to set
		 */
		private void setDefined(boolean isDefined) {
			this.isDefined = isDefined;
		}
		/**
		 * @return the length
		 */
		private int getLength() {
			return length;
		}
		/**
		 * @param length the length to set
		 */
		private void setLength(int length) {
			this.length = length;
		}
		
		
	}
	/**
	 * 
	 * @param paramName
	 * @param project
	 * @return
	 */
	private String getParamDefineCodeStr(String paramName,IARESProject project){
		StringBuffer codeBuffer = new StringBuffer();
			StandardDataType stdType = null;
			try {
				stdType = MetadataServiceProvider.getStandardDataTypeOfStdFieldByName(project, paramName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			TypeDefaultValue typpeDefValue = null;
			try {
				typpeDefValue = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName(project, paramName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			BusinessDataType busType = null;
			try {
				busType = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(project, paramName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			if((stdType != null) && ( typpeDefValue!= null) && ( busType!= null))//��׼�ֶ�
			{
				String dataType = StringUtils.defaultIfBlank(stdType.getValue(MetadataServiceProvider.C_TYPE), "");
				String defValue = typpeDefValue.getValue(MetadataServiceProvider.C_TYPE);
				int length = 0;
				if(busType.getLength() != null){
					try {
						length = Integer.parseInt(busType.getLength()) + 1;
					} catch (Exception e) {
						//throw new Exception(String.format("ҵ����������:%1$s�ĳ���Ϊ�Ƿ����֣�%2$s��", busType.getName(),busType.getLength()));
					}
					//����Char�������ʱ������Ҫ��1
					dataType = dataType.replace("$L", length + "");
				}
				if(TypeRule.typeRuleCharArray(dataType)){
					codeBuffer.append(String.format(PARAM_DEFINE_STR,paramName ,length));
				}else{
					codeBuffer.append(String.format(PARAM_DEFINE,dataType,paramName ,defValue));
				}
				
			}
		return codeBuffer.toString();
	}
	

}
