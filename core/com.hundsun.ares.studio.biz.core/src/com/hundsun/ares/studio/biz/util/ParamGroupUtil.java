/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.biz.util;

import java.util.List;

import com.hundsun.ares.studio.biz.ARESObject;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;

/**
 * @author liaogc
 *
 */
public class ParamGroupUtil {

	/**
	 * �ݹ���������(������������Ϊ��׼�ֶεĲ����Լ�����Ĳ���)
	 * @param gruopParam
	 * @param retParameter
	 * @param isInParameter
	 * @param project
	 */
	public static void parserParamGroup( Parameter gruopParam,List<Parameter> retParameter,int callDepth,IARESProject project){
		if(project==null){
			return;
		}
		String path = gruopParam.getType();
		try {
			IARESResource objectRes = project.findResource(path, "object");
			ARESObject aresObject = objectRes.getInfo(ARESObject.class);
			List<Parameter> properties = aresObject.getProperties();
			for(Parameter parameter:properties){
				if (parameter.getParamType().getValue() == ParamType.PARAM_GROUP_VALUE) {// ����ǲ�������ݹ����
					if(callDepth<=5){
						parserParamGroup(parameter, retParameter,callDepth+1, project);
					}
					
				} else if (parameter.getParamType().getValue() == ParamType.OBJECT_VALUE) {
					retParameter.add(parameter);
				} else if (parameter.getParamType().getValue() == ParamType.STD_FIELD_VALUE) {
					retParameter.add(parameter);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * �ݹ�η��س�����������в���(�����齫չ��)
	 * @param gruopParam
	 * @param retParameter
	 * @param isInParameter
	 * @param project
	 */
	public static void parserParametersWithNoObjectParameter( Parameter gruopParam,List<Parameter> retParameter,int callDepth,IARESProject project){
		if(project==null){
			return;
		}
		String path = gruopParam.getType();
		try {
			IARESResource objectRes = project.findResource(path, "object");
			ARESObject aresObject = objectRes.getInfo(ARESObject.class);
			List<Parameter> properties = aresObject.getProperties();
			for(Parameter parameter:properties){
				if (parameter.getParamType().getValue() == ParamType.PARAM_GROUP_VALUE) {// ����ǲ�������ݹ����
					if(callDepth<=5){
						parserParametersWithNoObjectParameter(parameter, retParameter,callDepth+1, project);
					}
					
				} else if (parameter.getParamType().getValue() != ParamType.OBJECT_VALUE) {
					retParameter.add(parameter);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * ������������������:������������в���(�����齫չ��)
	 * @param parameters
	 * @param retParameter
	 * @param project
	 */
	
	public static void parserParametersWithNoObjectParameter( List<Parameter>parameters ,List<Parameter> retParameter,IARESProject project){
		int callDepth = 1;
		for(Parameter parameter1:parameters){
			if(parameter1.getParamType().getValue() == ParamType.PARAM_GROUP_VALUE){
				String path = parameter1.getType();
				try {
					IARESResource objectRes = project.findResource(path, "object");
					ARESObject aresObject = objectRes.getInfo(ARESObject.class);
					List<Parameter> properties = aresObject.getProperties();
					for(Parameter parameter2:properties){
						if (parameter2.getParamType().getValue() == ParamType.PARAM_GROUP_VALUE) {// ����ǲ�������ݹ����
							if(callDepth<=5){
								parserParametersWithNoObjectParameter(parameter2, retParameter,callDepth+1, project);
							}
							
						} else if (parameter2.getParamType().getValue() != ParamType.OBJECT_VALUE) {
							retParameter.add(parameter2);
						} 
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(parameter1.getParamType().getValue() != ParamType.OBJECT_VALUE){
				retParameter.add(parameter1);
			}
		}
		
		
	}
	
	

	/**
	 * �ж�����������Ƿ��������
	 * @param parameters
	 * @param project
	 * @return
	 */
	
	public static boolean isContainObjectParameter( List<Parameter>parameters ,IARESProject project){
		int callDepth = 1;
		for(Parameter parameter1:parameters){
			if(parameter1.getParamType().getValue() == ParamType.PARAM_GROUP_VALUE){
				String path = parameter1.getType();
				try {
					IARESResource objectRes = project.findResource(path, "object");
					ARESObject aresObject = objectRes.getInfo(ARESObject.class);
					List<Parameter> properties = aresObject.getProperties();
					for(Parameter parameter2:properties){
						if (parameter2.getParamType().getValue() == ParamType.PARAM_GROUP_VALUE) {// ����ǲ�������ݹ����
							if(callDepth<=5){
								boolean isContainObjectParameter =  isContainsObjectParameter(parameter2,callDepth+1, project);
								if(isContainObjectParameter){
									return true;
								}
							}
							
						} else if (parameter2.getParamType().getValue() == ParamType.OBJECT_VALUE) {
							return true;
						} 
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(parameter1.getParamType().getValue() == ParamType.OBJECT_VALUE){
				return true;
			}
		}
		return false;
		
		
	}

	/**
	 * �жϲ��������Ƿ��������
	 * @param gruopParam
	 * @param callDepth
	 * @param project
	 * @return
	 */
	public static boolean isContainsObjectParameter( Parameter gruopParam,int callDepth,IARESProject project){
		if(project==null){
			return false;
		}
		String path = gruopParam.getType();
		try {
			IARESResource objectRes = project.findResource(path, "object");
			ARESObject aresObject = objectRes.getInfo(ARESObject.class);
			List<Parameter> properties = aresObject.getProperties();
			for(Parameter parameter:properties){
				if (parameter.getParamType().getValue() == ParamType.PARAM_GROUP_VALUE) {// ����ǲ�������ݹ����
					if(callDepth<=5){
						boolean isContainObjectParameter =isContainsObjectParameter(parameter,callDepth+1, project);
						if(isContainObjectParameter){
							return true;
						}
					}
					
				} else if (parameter.getParamType().getValue() == ParamType.OBJECT_VALUE) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	/**
	 * ������������������
	 * @param parameters
	 * @param retParameter
	 * @param project
	 */
	
	public static void parserParameters( List<Parameter>parameters ,List<Parameter> retParameter,IARESProject project){
		int callDepth = 1;
		for(Parameter parameter1:parameters){
			if(parameter1.getParamType().getValue() == ParamType.PARAM_GROUP_VALUE){
				String path = parameter1.getType();
				try {
					IARESResource objectRes = project.findResource(path, "object");
					ARESObject aresObject = objectRes.getInfo(ARESObject.class);
					List<Parameter> properties = aresObject.getProperties();
					for(Parameter parameter2:properties){
						if (parameter2.getParamType().getValue() == ParamType.PARAM_GROUP_VALUE) {// ����ǲ�������ݹ����
							if(callDepth<=5){
								parserParamGroup(parameter2, retParameter,callDepth+1, project);
							}
							
						} else if (parameter2.getParamType().getValue() == ParamType.OBJECT_VALUE) {
							retParameter.add(parameter2);
						} else if (parameter2.getParamType().getValue() == ParamType.STD_FIELD_VALUE) {
							retParameter.add(parameter2);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				retParameter.add(parameter1);
			}
		}
		
		
	}
}
